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

package org.photovault.imginfo.indexer;

/**
 Enum that represents the outcome of indexing a file.
 */
public enum IndexingResult {
    /**
     The indexed file was not previously known to Photovault.
     */
    NEW_FILE,
    /**
     The indexed file was know to Photovault but not in this location
     */
    NEW_LOCATION,
    /**
     The file had not changed after previous indexing operation
     */
    UNCHANGED, 
    /**
     The file is not recognized as image file by Photovault
     */
    NOT_IMAGE,
    /**
     An error occurred during indexing
     */
    ERROR

}
