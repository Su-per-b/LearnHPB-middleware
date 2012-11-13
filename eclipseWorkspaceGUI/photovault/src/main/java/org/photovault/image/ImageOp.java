/*
  Copyright (c) 2009 Harri Kaimio

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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ImageOp is base class for all descriptions of image processing operations
 * that are part of {@link ImageOpChain}
 *
 * @author Harri Kaimio
 * @since 0.6.0
 */
@XStreamAlias( "imageop" )
public abstract class ImageOp {
    static private Log log = LogFactory.getLog( ImageOp.class );
    /**
     * Source port for the operation
     */
    static public class Source {

        /**
         * Operation that owns this source
         */
        ImageOp op;
        /**
         * Name of this port
         */
        String name;

        /**
         * Constructor
         * @param name
         * @param owner
         */
        Source ( String name, ImageOp owner ) {
            op = owner;
            this.name = name;
        }

        /**
         * Get all sinks that receive data from this source
         * @return
         */
        Set<Sink> getSinks() {
            return op.chain.getSinks( this );
        }

        /**
         * Get names of sinks that receive data from this source
         * @return
         */
        Set<String> getSinkNames() {
            Set<String> names = op.chain.sinks.get( getPortName() );
            if ( names != null ) {
                return new HashSet<String>( names );
            }
            return null;
        }

        /**
         * Get name of this port
         * @return
         */
        String getPortName() {
            return op.getName() + "." + name;
        }
    }

    /**
     * Sink port, i.e. input for image processing operation
     */
    static public class Sink {
        ImageOp op;
        String name;
        Sink( String name, ImageOp owner ) {
            this.op = owner;
            this.name = name;
        }
    
        public void setSource( Source source ) {
            if ( source.op.chain != op.chain ) {
                throw new IllegalStateException(
                        "Cannot link ports in different chains" );
            }
            op.chain.setConnection( getPortName(), source.getPortName() );
        }

        public Source getSource() {
            return op.chain.getSource( this );
        }

        public String getSourceName() {
            Source src = op.chain.getSource( this );
            return src != null ? src.getPortName() : null;
        }

        String getPortName() {
            return op.getName() + "." + name;
        }
    }

    public ImageOp() {}

    /**
     * Copy constructor intended to be called by copy constructors of derived
     * classes. It copies only {@link #name} field of the object - other fields
     * are left uncopied as they are set by the associated {@link ImageOpChain}.
     * @param op
     */
    protected ImageOp( ImageOp op ) {
        this.name = op.name;
    }

    /**
     * Name of the operation
     */
    @XStreamOmitField
    private String name;

    /**
     * {@link ImageOpChain} Chain this operation belongs to
     */
    @XStreamOmitField
    ImageOpChain chain;

    @XStreamOmitField
    private Map<String, Sink> inputPorts = new HashMap<String, Sink>();

    @XStreamOmitField
    private Map<String, Source> outputPorts = new HashMap<String, Source>();


    public void setChain( ImageOpChain chain ) {
        this.chain = chain;
        chain.addOperation( this );
    }

    /**
     * Get name of the operation
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of the operation
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public Sink getInputPort( String name ) {
        return inputPorts.get( name );
    }

    public Source getOutputPort( String name ) {
        return outputPorts.get( name );
    }

    public Map<String,Sink> getInputPorts() {
        if ( inputPorts == null ) {
            log.error( "inputPorts is null!!" );
        }
        return Collections.unmodifiableMap( inputPorts );
    }

    public Map<String,Source> getOutputPorts() {
        return Collections.unmodifiableMap( outputPorts );
    }

    protected void addInputPort( String name ) {
        Sink sink = new Sink( name, this );
        inputPorts.put(name, sink );
    }

    protected void addOutputPort( String name ) {
        Source source = new Source( name, this );
        outputPorts.put(name, source );
    }

    void initPortMaps() {
        outputPorts = new HashMap<String, Source>();
        inputPorts = new HashMap<String, Sink>();
        initPorts();
    }

    /**
     * Derived classes need to provide a method for creating a copy of themselves
     * @return
     */
    public abstract ImageOp createCopy();

    protected abstract void initPorts();

    private Object readResolve() {
        outputPorts = new HashMap<String, Source>();
        inputPorts = new HashMap<String, Sink>();
        return this;
    }

}
