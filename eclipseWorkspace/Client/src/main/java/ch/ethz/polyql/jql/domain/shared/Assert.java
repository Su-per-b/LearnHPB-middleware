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


// TODO: Auto-generated Javadoc
/**
 * Author: Michael Gfeller.
 */
public final class Assert {
  
  /**
   * Instantiates a new assert.
   */
  private Assert() {} // no instances

  /**
   * Assert not zero or negative.
   *
   * @param i the i
   * @param name the name
   */
  public static void assertNotZeroOrNegative( final int i, final String name ) {
    if (i <= 0) {
      throw new IllegalArgumentException( name + " cannot be negative or zero" );
    }
  }

  /**
   * Assert true.
   *
   * @param b the b
   * @param expression the expression
   */
  public static void assertTrue( final boolean b, final String expression ) {
    if (!b) {
      throw new IllegalArgumentException( "'" + expression + "' expected true" );
    }
  }

  /**
   * Assert not null or empty.
   *
   * @param s the s
   * @param expression the expression
   */
  public static void assertNotNullOrEmpty( final String s, final String expression ) {
    if (TextUtil.isEmpty( s )) {
      throw new IllegalArgumentException( "'" + expression + "' expected not null or empty" );
    }
  }

  /**
   * Assert not null.
   *
   * @param o the o
   * @param s the s
   */
  public static void assertNotNull( final Object o, final String s ) {
    if (o == null) {
      throw new IllegalArgumentException( s + " is required not to be null" );
    }
  }

  /**
   * Assert false.
   *
   * @param b the b
   * @param expression the expression
   */
  public static void assertFalse( final boolean b, final String expression ) {
    if (b) {
      throw new IllegalArgumentException( "'" + expression + "' expected false" );
    }
  }

	public static void notNull(final Object o, final String s) {
		assertNotNull(o,s);
	}
	
	public static void isTrue(final boolean b, final String expression) {
		assertTrue(b, expression);
	}


}
