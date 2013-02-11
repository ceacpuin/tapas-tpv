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

package com.peyrona.tapas;

import com.peyrona.tapas.mainFrame.MainFrame;
import com.peyrona.tapas.persistence.DataProvider;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;

/**
 * Punto de entrada de la aplicación.
 *
 * @author peyrona
 */
public final class Main
{
    /**
     * La aplicación no contempla el paso de parámetros en línea de comandos.
     *
     * @param args the command line arguments
     */
    public static void main( String[] args )
    {
        setLookAndFeel();

        // Una bonita splash a la antigua usanza
        final JWindow splash = getSplash();

        EventQueue.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                splash.setVisible( true );
            }
        } );

        // Este método se invocará cuando se vaya a cerrar la JVM
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                onShutdown();
            }
        } );

        // Para que la inicialización del DataProvider no detenga el incio del GUI
        final Thread tDB = new Thread()
        {
            @Override
            public void run()
            {
                DataProvider.setDataSourceType( DataProvider.DataSources.DerbyEmbedded );
                DataProvider.getInstance().connect();
            }
        };
        tDB.start();

        // Configuración de ventanas y dialogos
        JFrame.setDefaultLookAndFeelDecorated( false );    // Las ventanas usan el look del sistema, no del L&F de Java
        JDialog.setDefaultLookAndFeelDecorated( false );   // Los dialogos usan el look del sistema, no del L&F de Java

        // Creamos la ventana principal
        final MainFrame frame = MainFrame.getInstance();
                        frame.pack();
                        frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
                        frame.addWindowListener( new WindowAdapter()
                        {
                            @Override
                            public void windowOpened( WindowEvent we )
                            {
                                splash.dispose();
                            }
                        } );

        // Arrancamos el GUI dentro del Event Dispath Thread
        EventQueue.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                frame.setVisible( true );

                // Esperamos a que termine la Thread de inicialización de DataProvider
                try{ tDB.join(); } catch( InterruptedException ex ) { Utils.printError( ex ); }

                // NEXT: Sistema de plugins ----------------------------
                // Añadimos los plugins
                // PluginManager.getInstance().init( this );
                //------------------------------------------------------

                if( DataProvider.getInstance().getConfiguration().isFullScreenSelected() )
                {
                    setFullScreenMode( frame );
                }

                frame.requestFocus();
            }
        } );
    }

    //------------------------------------------------------------------------//

    private static JWindow getSplash()
    {
        JLabel       image    = new JLabel( new ImageIcon( Main.class.getResource( "logo.png" ) ) );
        JProgressBar progress = new JProgressBar();
                     progress.setIndeterminate( true );
        JPanel       panel    = new JPanel( new BorderLayout() );
                     panel.setBorder( new LineBorder( Color.darkGray, 3 ) );
                     panel.add( image   , BorderLayout.CENTER );
                     panel.add( progress, BorderLayout.SOUTH  );
        JWindow      window = new JWindow();
                     window.setAlwaysOnTop( true );
                     window.setLayout( new BorderLayout() );
                     window.add( panel, BorderLayout.CENTER );
                     window.pack();
                     window.setLocationRelativeTo( null );   // Centra la window en la pantalla

        return window;
    }

    // Este método se invocará cuando se vaya a cerrar la JVM
    private static void onShutdown()
    {
        // NEXT: Sistema de plugins ----------------------------
        // Informamos a los plugins que se va a cerrar
        // PluginManager.getInstance().destroy( this );
        //------------------------------------------------------

        // Nos aseguramos de que al cerrar la app, la DB se cierra apropiadamente
        DataProvider.getInstance().disconnect();
    }

    private static void setFullScreenMode( MainFrame frame )
    {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if( gd.isFullScreenSupported() )
        {
            try
            {
                gd.setFullScreenWindow( frame );
                frame.validate();
            }
            catch( Exception exc )
            {
                gd.setFullScreenWindow( null );
            }
        }
    }

    private static void setLookAndFeel()
    {
        try
        {
            for( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
            {
                if( "Nimbus".equals( info.getName() ) )
                {
                    UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch( Exception exc )
        {
            try
            {
                UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
            }
            catch( Exception e )
            {
                System.out.println( "Error iniciando L&F. Esto no debería ocurrir." );
                Utils.printError( e, Level.SEVERE, "Error iniciando L&F", Utils.nEXIT_LAF_ERROR );
            }
        }
    }
}