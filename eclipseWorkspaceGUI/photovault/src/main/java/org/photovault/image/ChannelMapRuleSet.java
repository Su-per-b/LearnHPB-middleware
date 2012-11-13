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

package org.photovault.image;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.digester.RuleSetBase;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 Digester rule set for reading channel-map tag into ChannelMapOperation object
 */
public class ChannelMapRuleSet extends RuleSetBase {

    public ChannelMapRuleSet() {
        this( "" );
    }
    
    /**
     Creates a new instance of ChannelMapRuleSet
     @param prefix prefix path for the element to parse
     */
    public ChannelMapRuleSet( String prefix )  {
        super();
        pathPrefix = prefix;
    }

    String pathPrefix;
        
    /**
     Add rules for the channel-map reule set to digester
     @param d The digerster to which the rules will be added.
     */
    public void addRuleInstances(Digester d) {
        d.addObjectCreate( pathPrefix + "color-mapping", ChannelMapOperationFactory.class );
        d.addObjectCreate( pathPrefix + "color-mapping/channel", ColorCurve.class );
        d.addRule( pathPrefix + "color-mapping/channel", new Rule() {
            public void begin( String namespace, String name, Attributes attrs ) {
                String channelName = attrs.getValue( "name" );
                ChannelMapOperationFactory f = (ChannelMapOperationFactory) digester.peek( 1 );
                ColorCurve c = (ColorCurve) digester.peek();
                f.setChannelCurve( channelName, c );
            }
        });
        d.addCallMethod( pathPrefix + "color-mapping/channel/point", "addPoint", 2, 
                new Class[] {Double.class, Double.class} );
        d.addCallParam( pathPrefix + "color-mapping/channel/point", 0, "x" );
        d.addCallParam( pathPrefix + "color-mapping/channel/point", 1, "y" );
    }
    
}
