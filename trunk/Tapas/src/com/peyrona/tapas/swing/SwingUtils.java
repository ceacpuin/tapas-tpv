/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.peyrona.tapas.swing;

import com.peyrona.tapas.Utils;
import com.peyrona.tapas.mainFrame.MainFrame;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class SwingUtils
{
    public static BufferedImage ImageChooser()
    {
        BufferedImage     bufImage = null;
        PreviewImagePanel preview  = new PreviewImagePanel();
        JFileChooser      chooser  = new JFileChooser();
                          chooser.setAccessory( preview );
                          chooser.addPropertyChangeListener( preview );
                          chooser.addChoosableFileFilter( new ImageFilter() );
        int nAction =     chooser.showOpenDialog( MainFrame.getInstance() );

        if( nAction == JFileChooser.APPROVE_OPTION )
            bufImage = preview.getImage();

        return bufImage;
    }

    public static ImageIcon scaleIcon( ImageIcon icon, int nCompWidth, int nCompHeight )
    {// FIXME: No va bien
        if( icon == null )
            return null;

        float nImgWidth  = icon.getIconHeight();
        float nImgHeight = icon.getIconHeight();
        float nScale     = nImgHeight / nImgWidth;

        if( nImgWidth > nCompWidth )
        {
            nImgWidth   = nCompWidth;
            nImgHeight *= nScale;
        }

        if( nImgHeight > nCompHeight )
        {
            nImgHeight = nCompHeight;
            nImgWidth *= nScale;
        }

        Image image = icon.getImage();
              image.getScaledInstance( Math.max( 16, (int)nImgWidth ),
                                       Math.max( 16, (int)nImgHeight ),
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