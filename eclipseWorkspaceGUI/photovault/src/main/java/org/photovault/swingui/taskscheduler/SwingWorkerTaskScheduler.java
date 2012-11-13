/*
  Copyright (c) 2007 Harri Kaimio
 
  This file is part of Photovault.
 
  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.
 
  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even therrore implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */


package org.photovault.swingui.taskscheduler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.context.ManagedSessionContext;
import org.jdesktop.swingworker.SwingWorker;
import org.photovault.command.CommandExecutedEvent;
import org.photovault.command.CommandListener;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.persistence.HibernateUtil;
import org.photovault.swingui.framework.AbstractController;
import org.photovault.taskscheduler.BackgroundTask;
import org.photovault.taskscheduler.TaskProducer;
import org.photovault.taskscheduler.TaskScheduler;

/**
 TaskScheduler implementation that uses SwingWorker to execute the tasks. The 
 tasks are executed in order set by producer priorities - if several TaskProducers 
 share the same priority they are scheduler by round robin algorithm.
 
 <p>
 SwingWorkerTaskScheduler can be associated with an {@link AbstractController}. If
 this is the case the scheduler send a {@link CommandEvent} to it after every 
 command execution.
 */
public class SwingWorkerTaskScheduler implements CommandListener, TaskScheduler {

    static private Log log = 
            LogFactory.getLog( SwingWorkerTaskScheduler.class.getName() );
    
    /**
     Create new SwingWorkerTaskScheduler
     @parent The controller that owns this scheduler
     */
    public SwingWorkerTaskScheduler( AbstractController parent ) {
        this.parent = parent;
    }    
    /**
     Lowest priority for tasks
     */
    public static int MIN_PRIORITY = 31;
    
    /**
     Parent controller
     */
    AbstractController parent;
    
    /**
     Priorities of each currently registered producer
     */
    Map<TaskProducer, Integer> customerPriorities = new HashMap<TaskProducer, Integer>(  );
    
    /**
     Registered producers by priority
     */
    @SuppressWarnings( value = "unchecked" )
    Queue<TaskProducer>[] waitList = new LinkedList[MIN_PRIORITY + 1];
    
    /**
     Currently active task or <code>null</code> if no task is active
     */
    BackgroundTask activeTask = null;
    
    /**
     Listeners for each task producer
     */
    HashMap<TaskProducer, Set<BackgroundTaskListener>> listeners = 
            new HashMap<TaskProducer, Set<BackgroundTaskListener>>();

    /**
     Get the task listeners registered for a certain TaskProducer
     @param p The TaskProducer we are interested in
     @return Listeners registered to lsiten for this producer
     */
    private Set<BackgroundTaskListener> getTaskListeners( TaskProducer p ) {
        Set<BackgroundTaskListener> ls = listeners.get( p );
        if ( ls == null ) {
            ls = new HashSet<BackgroundTaskListener>();
            listeners.put( p, ls );
        }
        return ls;
    }

    /**
     Ask that a {@link BackgroundTaskListener} will be notified about progress 
     of tasks produced by a certain {@link TaskProducer}
     
     @param p The TaskProducer we are interested in
     @param l The listener that will be added
     */
    public void addTaskListener( TaskProducer p, BackgroundTaskListener l ) {
        Set<BackgroundTaskListener> ls = getTaskListeners( p );
        ls.add( l );
    }

    /**
     Ask that a {@link BackgroundTaskListener} will not anymore be notified.     
     @param p The TaskProducer we are interested in
     @param l The listener that will be removed
     */
    public void removeTaskListener( TaskProducer p, BackgroundTaskListener l ) {
        Set<BackgroundTaskListener> ls = getTaskListeners( p );        
        ls.remove( l );
    }
    
    /**
     Register a new TaskProducer for execution
     @param c The TaskProducer.
     @param priority Priority for this task producer.
     */
    public void registerTaskProducer( TaskProducer c, TaskPriority priority ) {
        registerTaskProducer( c, priority.getPriority() );
    }
    
    /**
     See {@link TaskScheduler#registerTaskProducer()} for details.
     */
    public void registerTaskProducer( TaskProducer c, int priority  ) {
        log.debug( "entry: registerTaskProducer" );
        if ( priority < 0 || priority > MIN_PRIORITY ) {
            log.warn( "priority = " + priority );
            throw new IllegalArgumentException( "Priority must be between 0 and " + MIN_PRIORITY );
        }
        if ( waitList[priority] == null ) {
            waitList[priority] = new LinkedList<TaskProducer>(  );
        }
        Integer oldPriority = null;
        if ( (oldPriority = customerPriorities.put( c, priority )) != null ) {
            waitList[oldPriority].remove( c );
        }
        waitList[priority].add( c );
        if ( activeTask == null ) {
            scheduleNext(  );
        }
        log.debug( "exit: registerTaskProducer" );
    }

    /**
     Called by schduleNext in AWT thread to schedule the selected task for 
     execution.
     @param nextTask The task to execute
     */
    private void runTask( final TaskProducer producer, final BackgroundTask nextTask  ) {
        final SwingWorkerTaskScheduler tthis = this;
        SwingWorker worker = new SwingWorker(  ) {

            protected Object doInBackground( ) throws Exception {
                tthis.doRunTask( nextTask );
                return null;
            }

            @Override
            protected void done( ) {
                fireTaskExecutedEvent( producer, nextTask );
                log.debug( "Task " + nextTask + " executed" );
                activeTask = null;
                scheduleNext();
            }
        };
        worker.execute(  );
    }

    /**
     Called in worker thread by runTask to actually execute the task. Sets up
     Hibernate environment and runs the task.
     @param task The task to execute
     @throws Exception if the task.run() method throws one.
     */
    protected void doRunTask( BackgroundTask task ) throws Exception {
        Session session = HibernateUtil.getSessionFactory(  ).openSession(  );
        Session oldSession = ManagedSessionContext.bind( (org.hibernate.classic.Session) session );
        PhotovaultCommandHandler cmdHandler = new PhotovaultCommandHandler( session );
        cmdHandler.addCommandListener( this );
        try {
            log.debug( "Executing task " + task.toString() );
            task.executeTask( session, cmdHandler );
            log.debug( "Finished executing task " + task.toString() );
        } catch ( Exception e ) {
            log.warn( "Exception while executing task " + task.toString(), e );
            throw e;
        } catch (Error e ) {
            log.warn( "Error executing task " + task.toString(), e );
            throw e;
        } finally {
            session.close(  );
            if ( oldSession != null ) {
                ManagedSessionContext.bind( (org.hibernate.classic.Session) oldSession );
            } else {
                ManagedSessionContext.unbind( HibernateUtil.getSessionFactory() );
            }
        }
    }

    /**
     Select the next task to execute.
     */
    protected synchronized void scheduleNext( ) {
        activeTask = null;
        for ( int n = 0 ; n <= MIN_PRIORITY && activeTask == null ; n++ ) {
            if ( waitList[n] != null ) {
                TaskProducer c;
                while ( (c = waitList[n].poll(  )) != null ) {
                    activeTask = c.requestTask(  );
                    log.debug( "Scheduling task " + activeTask );
                    if ( activeTask != null ) {
                        runTask( c, activeTask );
                        waitList[n].add(c);
                        return;
                    } else {
                        fireTaskProducerFinishedEvent( c );
                    }
                }
            }
        }
    }
    
    /**
     Informs listeners that a task has been executed. This method must be called
     in AWT event thread context.
     @param producer The producer that produced the task
     @param task The executed task
     */
    private void fireTaskExecutedEvent( final TaskProducer producer, final BackgroundTask task ) {
        for ( BackgroundTaskListener l : getTaskListeners( producer ) ) {
            l.taskExecuted( producer, task );
        }
    }
    
    /**
     Informs listeners that a task producer has finished its job. This method must
     be called in AWT event handler thread.
     @param producer The producer that has completed all its tasks
     */
    private void fireTaskProducerFinishedEvent( TaskProducer producer ) {
        for ( BackgroundTaskListener l : getTaskListeners( producer ) ) {
            l.taskProducerFinished( producer );
        }
    }

    /**
    This method is called by command handler in wirker thread when a command
    is executed. It asks the parent controller to fire the event in
    java AWT thread.
    @param e The command event
     */
    public void commandExecuted( final CommandExecutedEvent e ) {
        if ( parent == null ) {
            return;
        }
        log.debug( "Scheduling command event " + e );
        /*
         Wait until the event has been processed in AWT event thread to avoid 
         race condition while scheduling the next event
         */
        while ( true ) {
            try {

                SwingUtilities.invokeAndWait( new Runnable() {

                    public void run() {
                        log.debug( "Firing " + e );
                        parent.fireEventGlobal( e );
                    }
                } );
                break;
            } catch ( Exception ex ) {
            }
        }
    }
}