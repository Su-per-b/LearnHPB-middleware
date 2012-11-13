/*
  Copyright (c) 2010 Harri Kaimio

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

package org.photovault.imginfo;

import java.util.EnumSet;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.command.CommandException;
import org.photovault.image.PhotovaultImage;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.taskscheduler.BackgroundTask;

/**
 * Task for creating all needed preview images for given photo
 * @author harri
 */
public class CreatePreviewImagesTask extends BackgroundTask {

    Log log = LogFactory.getLog( CreatePreviewImagesTask.class );
    private UUID photoUuid;

    public CreatePreviewImagesTask( PhotoInfo p ) {
         photoUuid = p.getUuid();

    }

    @Override
    public void run() {
        HibernateDAOFactory df =
                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        df.setSession( session );
        PhotoInfo p = df.getPhotoInfoDAO().findByUUID( photoUuid );
        if ( p != null ) {
            try {
                createPreviewInstances( null, p, df );
            } catch ( CommandException ex ) {
                log.warn( ex );
            }
        }
    }

    private void createPreviewInstances(
            PhotovaultImage img, PhotoInfo p, DAOFactory f )
            throws CommandException {
        long startTime = System.currentTimeMillis();
        ImageDescriptorBase thumbImage = p.getPreferredImage(
                EnumSet.allOf( ImageOperations.class ),
                EnumSet.allOf( ImageOperations.class ), 0, 0, 200, 200 );

        // Preview image with no cropping, longer side 1280 pixels
        int origWidth = p.getOriginal().getWidth();
        int origHeight = p.getOriginal().getHeight();

        int copyMinWidth = Math.min( origWidth, 1280 );
        int copyMinHeight = 0;
        if ( origHeight > origWidth ) {
            copyMinHeight = Math.min( origHeight, 1280 );
            copyMinWidth = 0;
        }
        EnumSet<ImageOperations> previewOps =
                EnumSet.of( ImageOperations.RAW_CONVERSION, ImageOperations.COLOR_MAP );
        ImageDescriptorBase previewImage = p.getPreferredImage(
                previewOps, previewOps,
                copyMinWidth, copyMinHeight, 1280, 1280 );


        Volume vol = f.getVolumeDAO().getDefaultVolume();
        long previewImageTime = -1;
        if ( previewImage == null ) {
            long previewStart = System.currentTimeMillis();
            CreateCopyImageCommand cmd =
                    new CreateCopyImageCommand( img, p, vol, 1280, 1280 );
            cmd.setLowQualityAllowed( false );
            cmd.setOperationsToApply( previewOps );
            cmdHandler.executeCommand( cmd );
            previewImageTime = System.currentTimeMillis() - previewStart;
        }
        long thumbTime = -1;
        if ( thumbImage == null ) {
            long thumbStart = System.currentTimeMillis();
            CreateCopyImageCommand cmd =
                    new CreateCopyImageCommand( img, p, vol, 200, 200 );
            cmd.setLowQualityAllowed( true );
            cmdHandler.executeCommand( cmd );
            thumbTime = System.currentTimeMillis() - thumbStart;
        }
        long totalTime = System.currentTimeMillis() - startTime;
        log.debug( "preview " + previewImageTime + " ms, thumb " + thumbTime +
                " ms, total " + totalTime + " ms");
    }


}
