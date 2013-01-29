/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.peyrona.tapas.swing;

import com.peyrona.tapas.Utils;
import com.peyrona.tapas.mainFrame.MainFrame;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import javax.swing.ImageIcon;
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

    /**
     * Hace que un icon encaje en el componente sin perder sus proporciones.
     *
     * @param icon
     * @param comp
     * @return
     */
    public static ImageIcon scaleIcon( ImageIcon icon, Component comp )
    {
        return scaleIcon( icon, comp.getWidth(), comp.getHeight() );
    }

    public static ImageIcon scaleIcon( ImageIcon icon, int nCompWidth, int nCompHeight )
    {// FIXME: No va bien
        if( icon == null )
            return null;

        float nImgWidth  = icon.getIconWidth();
        float nImgHeight = icon.getIconHeight();
        float nRatio     = nImgWidth / nImgHeight;

        if( nImgWidth > nCompWidth || nImgHeight > nCompHeight )    // Uno de los lados de la imagen es mayor que el destino
        {
            if( nImgWidth > nCompWidth )
            {
                nImgWidth   = nCompWidth;
                nImgHeight *= nRatio;
            }

            if( nImgHeight > nCompHeight )
            {
                nImgHeight = nCompHeight;
                nImgWidth *= nRatio;
            }
        }
        else                                                       // Uno de los lados de la imagen es menor que el destino
        {

        }

        Image image = icon.getImage();
              image.getScaledInstance( Math.max( 16, (int) nImgWidth ),
                                       Math.max( 16, (int) nImgHeight ),
                                       Image.SCALE_SMOOTH );

        return (new ImageIcon( image ));
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