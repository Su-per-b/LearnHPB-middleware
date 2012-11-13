/*
  Copyright (c) 2007 Harri Kaimio
  
  This file is part of Photovault.

  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
*/

package org.photovault.command;

/**
  Interface for classes that act as command handlers in the command pattern. 
 */
public interface CommandHandler {
    /**
     Execute a given command.
     @param command the command to execute.
     */
    public Command executeCommand( Command command ) throws CommandException;
    
    /**
     Execute a given command in environment that is set up for persistent data 
     access
     @param command the command to execute.
     */
    public DataAccessCommand executeCommand( DataAccessCommand command ) 
        throws CommandException;

    void addChangeListener(CommandChangeListener l);

    void removeChangeListener(CommandChangeListener l);
    
    void addCommandListener( CommandListener l );
    
    void removeCommandListener( CommandListener l );
}
