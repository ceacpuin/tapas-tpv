/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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