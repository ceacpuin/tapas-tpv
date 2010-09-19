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
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Francisco Morero Peyrona
 */
class PreviewImagePanel extends JPanel implements PropertyChangeListener
{
    private JLabel        lblImage;
    private BufferedImage buffImage;

    //------------------------------------------------------------------------//
    
    PreviewImagePanel()
    {
        lblImage = new JLabel();
        lblImage.setBackground( Color.WHITE );
        lblImage.setOpaque( true );
        lblImage.setPreferredSize( new Dimension( 200, 200 ) );
        lblImage.setBorder( BorderFactory.createEtchedBorder() );

        setLayout( new BorderLayout() );
        add( lblImage, BorderLayout.CENTER );
    }

    BufferedImage getImage()
    {
        return buffImage;
    }

    public void propertyChange( PropertyChangeEvent evt )
    {
        ImageIcon icon = null;

        if( JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals( evt.getPropertyName() ) )
        {
            File fImage = (File) evt.getNewValue();

            if( fImage != null )
            {
                try
                {
                    buffImage = ImageIO.read( fImage );

                    float nWidth  = buffImage.getWidth();
                    float nHeight = buffImage.getHeight();
                    float nScale  = nHeight / nWidth;

                    nWidth = getPreferredSize().width;

                    nHeight = (nWidth * nScale); // height should be scaled from new width
                    icon = new ImageIcon( buffImage.getScaledInstance( Math.max( 16, (int)nWidth ),
                                                                       Math.max( 16, (int)nHeight ),
                                          Image.SCALE_FAST ) );
                }
                catch( IOException e )
                {
                    // No se ha podido leer la imagen
                }
            }

            lblImage.setIcon( icon );
            repaint();
        }
    }
}