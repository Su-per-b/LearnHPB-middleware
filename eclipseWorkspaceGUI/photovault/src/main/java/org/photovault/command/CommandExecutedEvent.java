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

package org.photovault.command;

import org.photovault.swingui.framework.DefaultEvent;

/**
 Superclass for events that inform about an executed command. 
 */
public class CommandExecutedEvent extends DefaultEvent<DataAccessCommand> {

    /**
     Create a new command event
     @param src source of this event
     @param cmd The command that was executed
     */
    public CommandExecutedEvent( CommandHandler src, DataAccessCommand cmd ) {
        super( src );
        setPayload( cmd );
    }
}
