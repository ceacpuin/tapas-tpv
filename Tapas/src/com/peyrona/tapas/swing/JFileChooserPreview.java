/*
 * Copyright (C) 2010 Francisco José Morero Peyrona. All Rights Reserved.
 *
 * This file is part of Tapas project: http://code.google.com/p/tapas-tpv/
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 *
 * Tapas is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Tapas; see the file COPYING.  If not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.peyrona.tapas.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Francisco Morero Peyrona
 */
class PreviewImagePane extends JPanel implements PropertyChangeListener
{
    private static final int nMAX_IMG_WIDTH = 190;

    private JLabel lblImage;

    //------------------------------------------------------------------------//
    
    PreviewImagePane()
    {
        lblImage = new JLabel();
        lblImage.setBackground( Color.WHITE );
        lblImage.setOpaque( true );
        lblImage.setPreferredSize( new Dimension( 200, 200 ) );
        lblImage.setBorder( BorderFactory.createEtchedBorder() );

        setLayout( new BorderLayout( 5, 5 ) );
        setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
        add( lblImage, BorderLayout.CENTER );
    }

    public void propertyChange( PropertyChangeEvent evt )
    {
        Icon icon = null;

        if( JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals( evt.getPropertyName() ) )
        {
            File fNewImage = (File) evt.getNewValue();

            if( fNewImage != null )
            {
                String path = fNewImage.getAbsolutePath();

                if( path.endsWith( ".gif" ) || path.endsWith( ".jpg" ) || path.endsWith( ".png" ) || path.endsWith( ".bmp" ) )
                {
                    try
                    {
                        BufferedImage img    = ImageIO.read( fNewImage );
                        float         width  = img.getWidth();
                        float         height = img.getHeight();
                        float         scale  = height / width;

                        width = nMAX_IMG_WIDTH;

                        height = (width * scale); // height should be scaled from new width
                        icon = new ImageIcon( img.getScaledInstance( Math.max( 1, (int) width ), Math.max( 1, (int) height ), Image.SCALE_SMOOTH ) );
                    }
                    catch( IOException e )
                    {
                        // No se ha podido leer la imagen
                    }
                }
            }

            lblImage.setIcon( icon );
            repaint();
        }
    }
}
