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

/**
 * Author: Michael Gfeller
 */
public final class Assert {
  private Assert() {} // no instances

  public static void assertNotZeroOrNegative( final int i, final String name ) {
    if (i <= 0) {
      throw new IllegalArgumentException( name + " cannot be negative or zero" );
    }
  }

  public static void assertTrue( final boolean b, final String expression ) {
    if (!b) {
      throw new IllegalArgumentException( "'" + expression + "' expected true" );
    }
  }

  public static void assertNotNullOrEmpty( final String s, final String expression ) {
    if (TextUtil.isEmpty( s )) {
      throw new IllegalArgumentException( "'" + expression + "' expected not null or empty" );
    }
  }

  public static void assertNotNull( final Object o, final String s ) {
    if (o == null) {
      throw new IllegalArgumentException( s + " is required not to be null" );
    }
  }

  public static void assertFalse( final boolean b, final String expression ) {
    if (b) {
      throw new IllegalArgumentException( "'" + expression + "' expected false" );
    }
  }
}
