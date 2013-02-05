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

import com.peyrona.tapas.Utils;
import com.peyrona.tapas.mainFrame.MainFrame;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Algunos métodos estáticos para operar con Swing.
 * 
 * @author Francisco Morero Peyrona
 */
public final class SwingUtils
{
    public static File folderChooser()
    {
        File folder = null;

        JFileChooser fc = new JFileChooser();
                     fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
                     fc.setAcceptAllFileFilterUsed( false );   // Desactiva la opción "All files"
                     fc.setMultiSelectionEnabled( false );     // Sólo se admite un dir

        if( fc.showOpenDialog( MainFrame.getInstance() ) == JFileChooser.APPROVE_OPTION )
            folder = fc.getSelectedFile();

        return folder;
    }

    public static BufferedImage ImageChooser()
    {
        BufferedImage     bufImage = null;
        ImagePreviewPanel preview  = new ImagePreviewPanel();
        JFileChooser      chooser  = new JFileChooser();
                          chooser.setAccessory( preview );
                          chooser.addPropertyChangeListener( preview );
                          chooser.addChoosableFileFilter( new ImageFilter() );
        int nAction =     chooser.showOpenDialog( MainFrame.getInstance() );

        if( nAction == JFileChooser.APPROVE_OPTION )
            bufImage = preview.getImage();

        return bufImage;
    }

    public static void showError( final Throwable th, final Level level, final String sMessage, final int nExitCode )
    {
        Utils.printError( th, level, sMessage, nExitCode );

        EventQueue.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                JOptionPane.showMessageDialog( null, sMessage );

                if( nExitCode != Utils.nEXIT_NO_EXIT )
                    System.exit( nExitCode );
            }
        } );
    }
}