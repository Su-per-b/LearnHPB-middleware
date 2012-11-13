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
 Interface for classes that can execute background tasks created by 
 {@link TaskProducer}
 
 */
public interface TaskScheduler {
    /**
     Register a new producer that assigns tasks to this scheduler. The scheduler
     requests new tasks by calling producer's requestTask() method.
     @param c The new task producer
     @param priority Priority of the new task producer. THis can vary from 0 
     (highest) to MIN_PRIORITY (lowest).
     */
    public void registerTaskProducer( org.photovault.taskscheduler.TaskProducer c, int priority  );

}
