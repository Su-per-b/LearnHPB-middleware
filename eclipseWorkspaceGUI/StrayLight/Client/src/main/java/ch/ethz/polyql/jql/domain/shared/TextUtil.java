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

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Author: Michael Gfeller
 */
public final class TextUtil {
  private static final String NEW_LINE = System.getProperty( "line.separator" );

  private TextUtil() { }

  public static boolean isEmpty( final String text ) {
    return (text == null) || ("".equals( text.trim() ));
  }

  public static String emptyIfNull( final String text ) {
    return (text == null) ? "" : text;
  }

  public static int compareIgnoreCase( final String text1, final String text2 ) {
    return emptyIfNull( text1 ).compareToIgnoreCase( emptyIfNull( text2 ) );
  }

  public static String systemDependentNewline( final String text ) {
    if (text == null) {
      return text;
    } else {
      return text.replaceAll( "\n", NEW_LINE );
    }
  }

  public static String join( final List<String> text ) {
    return StringUtils.join( text, "\n" );
  }

}
