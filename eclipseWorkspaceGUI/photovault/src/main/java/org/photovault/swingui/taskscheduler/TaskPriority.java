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

/**
 Centralized definition of priorities for different types of background tasks 
 used by Photovault Swing UI.
 */
public enum TaskPriority {
    /**
     Tasks that load image data that is visible in UI
     */
    LOAD_UI_IMAGE( 1 ),
    
    /**
     Tasks that create thumbnails that should be already visible in UI 
     */
    CREATE_VISIBLE_THUMBNAIL( 2 ),
    
    /**
     BackgroundINdexer that is indexing the currently selected directory
     */
    INDEX_CURRENT_DIR( 3 ),
    
    /**
     Tasks that export images based on user request
     */
    EXPORT_IMAGE( 4 ),
    
    /**
      BackgroundIndexer that does routine indexing of an external volume.
     */
    INDEX_EXTVOL( 5 );
    
    /**
     Priority associated with this type of task
     */
    private int priority;

    TaskPriority( int priority ) {
        this.priority = priority;
    }
    
    public int getPriority() {
        return priority;
    }
}
