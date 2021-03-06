/**
 * jQL - Calculation of Chemical Speciation in Aqueous Solution
 *
 * Copyright (C) 2009 Michael Gfeller, <mgfeller@mgfeller.net> - <http://www.mgfeller.net>
 *
 * This file is part of jQL.
 *
 * jQL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jQL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jQL.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.ethz.polyql.jql.domain.shared;

import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * Author: Michael Gfeller.
 */

final public class IdGenerator { // only static methods -> no need for inheritance

  /**
  * Instantiates a new id generator.
  */
 private IdGenerator() {} // only static methods -> no instances needed

  /**
   * Creates the uuid.
   *
   * @return the string
   */
  public static String createUUID() {
    return UUID.randomUUID().toString();
  }
}
