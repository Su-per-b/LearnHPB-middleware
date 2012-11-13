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

/**
  TaskProducer is an object that can delegate tasks to {@link TaskScheduler} for
 asynchronous execution in background.
 */
public interface TaskProducer {
    /**
     Called by TaskScheduler when it can execute a task for this producer. Note 
     that implementation of this method must be thread safe: it can be called in
     any thread, possibly in several threads at the same time (an especially before
     the task returned from previous call has been executed.
     @return New task that must be executed of <code>null</code> if this producer 
     is ready. After this, no the producer will be unregistered  from scheduler
     and no furher tasks are requested.
     */
    BackgroundTask requestTask();
}
