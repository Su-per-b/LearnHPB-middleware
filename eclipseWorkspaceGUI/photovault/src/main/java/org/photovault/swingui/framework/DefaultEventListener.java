/*
  Copyright (c) 2005, Christian Bauer <christian@hibernate.org>
  Copyright (c) 2007, Harri Kaimio
  
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

 
 
  Based on SWING/Hibernate framework by Christian Bauer. Original SW 
  licensed with the following conditions:

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are met:

  - Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

  - Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

  Neither the name of the original author nor the names of contributors may be
  used to endorse or promote products derived from this software without
  specific prior written permission.
 */

package org.photovault.swingui.framework;

/**
 * A listener for a particular event, needs parameterization for typesafe usage of the payload.
 *
 * @author Christian Bauer
 */
public interface DefaultEventListener<PAYLOAD> {
    public void handleEvent(DefaultEvent<PAYLOAD> event);
}
