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

import org.photovault.taskscheduler.BackgroundTask;
import org.photovault.taskscheduler.TaskProducer;

/**
 Listener interface for classes that need notifications about processing of 
 background tasks.
 @author Harri Kaimio
 @since 0.6.0
 */
public interface BackgroundTaskListener {
    
    /**
     Called in AWT event thread after a task has been executed
     @param producer The TaskProducer that produced the executed task
     @param task The executed task
     */
    void taskExecuted( TaskProducer producer, BackgroundTask task );
    
    /**
     Called in AWT event thread after a TaskProducer has completed its job (i.e.
     does not create new tasks.
     @param producer
     */
    void taskProducerFinished( TaskProducer producer );
}
