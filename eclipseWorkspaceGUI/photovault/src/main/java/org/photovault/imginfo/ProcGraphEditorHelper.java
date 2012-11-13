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

package org.photovault.imginfo;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.PropertyUtils;
import org.photovault.image.ImageOp;
import org.photovault.replication.VersionedObjectEditor;

/**
 * Helper class for constructing {@link Change} to {@link ImageOpChain} graph
 * in processing field of {@link PhotoInfo}
 * <p>
 * This class is used to change or construct a single node in the graph. If the
 * node with given name exists, it is modified. If id does not exist in the graph,
 * a new node is created. After doing ht modifications, calling {@link
 * #addNewNode(null, null)} will add the potentially created new node to the
 * graph.
 *
 * @author Harri Kaimio
 * @since 0.6.0
 */
class ProcGraphEditorHelper {

    /**
     * Node that was created, <code>null</code> if the node existed already
     */
    ImageOp createdNode;

    /**
     * Has the node been modified?
     */
    boolean isModified;

    /**
     * Name of the node being created or edited
     */
    String nodeName;

    /**
     * Editor for the PhotoInfo object
     */
    VersionedObjectEditor editor;

    /**
     * Constructor
     * @param e editor being used
     * @param nodeName Name of the node to be edited
     * @param nodeClazz Class of the node
     * @throws InstantiationException If the node did not exist and isntantiation
     * of new node failed
     * @throws IllegalAccessException If The node did not hav epublic default
     * constructor.
     */
    ProcGraphEditorHelper( VersionedObjectEditor<PhotoInfo> e,
            String nodeName, Class<? extends ImageOp> nodeClazz )
            throws InstantiationException, IllegalAccessException {
        this.nodeName = nodeName;
        editor = e;
        PhotoInfo p = e.getTarget();
        ImageOp node = p.getProcessing().getOperation( nodeName );
        if ( node == null ) {
            createdNode = nodeClazz.newInstance();
        } 
    }

    /**
     * Set a property of the processing graph
     * @param name Name of the proeprty
     * @param value New value for the property
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    void setProperty( String name, Object value ) 
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        isModified = true;
        if ( createdNode != null ) {
            PropertyUtils.setProperty( createdNode, name, value);
        } else {
            String fullname = "processing.operation(" + nodeName + ")." + name;
            editor.setField( fullname, value);
        }
    }

    /**
     * Returns <code>true</code> if the node was already present in the graph,
     * <code>false</code> otherwise.
     * @return
     */
    boolean isNodeAlreadyPresent() {
        return createdNode == null;
    }

    /**
     * Add a newly created node to the graph. If the node did already exist, do
     * nothing
     * @param sourceName Name of the source of the node (will be connected to
     * port "nodename.in")
     * @param sinkName Name of the sink port that will use output of the created
     * node
     * @return <code>true</code> if a new node was created.
     */
    boolean addNewNode( String sourceName, String sinkName ) {
        if ( createdNode != null && isModified ) {
            String nodeFullName = "processing.operation(" + nodeName + ")";
            editor.setField( nodeFullName, createdNode );
            if ( sourceName != null ) {
                String inputName = "processing.connection(" + nodeName + ".in)";
                editor.setField( inputName, sourceName );
            }
            if ( sinkName != null ) {
                String sinkFullName = "processing.connection(" + sinkName + ")";
                editor.setField( sinkFullName, nodeName + ".out" );
            } else {
                editor.setField( "processing.head", nodeName + ".out" );
            }
            return true;
        }
        return false;
    }

    /**
     * Get the name of the node being edited/created
     * @return
     */
    public String getNodeName() {
        return nodeName;
    }
}
