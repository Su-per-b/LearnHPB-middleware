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


package org.photovault.taskscheduler;

import org.hibernate.Session;
import org.photovault.command.CommandHandler;

/**
 Abstract base class for tasks that can be executed by {@link TaskScheduler}.
 Before executing, the task scheduler sets up an persistence environment with a
 fresk persistence context for this task.
 */
public abstract class BackgroundTask implements Runnable {

    /**
    Create a new BackgroundTask
     */
    public BackgroundTask() {}
    
    /**
     Command handler that can be used by the task
     */
    protected CommandHandler cmdHandler;
    
    /**
     Persistence context that can be used by the task
     */
    protected Session session;
    
    /**
     This method must be overridden by derived classes to execute the actual task.
     */
    abstract public void run( );
    
    public void executeTask( Session session, CommandHandler cmdHandler ) {
        this.session = session;
        this.cmdHandler = cmdHandler;
        run();
    }

    /**
     Set persistence context (called by TaskScheduler)
     */
    public void setSession( Session s ) {
        this.session = s;
    }
    
    /**
     Set command handler (called by TaskScheduler)
     */
    public void setCommandHandler( CommandHandler ch ) {
        this.cmdHandler = ch;
    }
    
}
