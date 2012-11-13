/*
  Copyright (c) 2008 Harri Kaimio
  
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


package org.photovault.replication;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;

/**
 Factory for constructing resolvers that can convert DTO into an object persisted 
 by Hibernate.
 
 @author Harri Kaimio
 @since 0.6.0
 @see DTOResolverFactory
 @see HibernateDTOResolver
 */
public class HibernateDtoResolverFactory implements DTOResolverFactory {

    /**
     Session used to look up or persist objects.
     */
    private Session session;

    /**
     Already created resolvers that are associated with this session.
     */
    private Map<Class, HibernateDTOResolver> resolvers = 
            new HashMap<Class, HibernateDTOResolver>();
    
    /**
     Default resolver
     */
    private DTOResolver defaultResolver = new DefaultDtoResolver();
    
    public HibernateDtoResolverFactory( Session sess ) {
        this.session = sess;
    }
    
    /**
     Get an instance of given resolver class
     @param clazz Class of the resolver
     @return Instance of clazz
     @throws IllegalArgumentException if clazz is not subclass of {@link 
     HibernateDTOResolver} or cannot be constructed by reflection.
     */
    public DTOResolver getResolver( Class<? extends DTOResolver> clazz ) {
        
        // Shortcut when no change is needed.
        if ( clazz == DefaultDtoResolver.class ) {
            return defaultResolver;
        }
        
        if ( resolvers.containsKey( clazz ) ) {
            return resolvers.get( clazz );
        }
        HibernateDTOResolver resolver = null;
        try {
            resolver = (HibernateDTOResolver) clazz.newInstance();
            resolver.setSession( session );
        } catch ( InstantiationException ex ) {
            throw new IllegalArgumentException( 
                    "Cannot instantiate " + clazz.getName(), ex );
        } catch ( IllegalAccessException ex ) {
            throw new IllegalArgumentException( 
                    "Cannot access constructor of " + clazz.getName(), ex );
        }
        return resolver;
    }

}
