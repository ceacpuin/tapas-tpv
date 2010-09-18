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

package com.peyrona.tapas.office;

import com.peyrona.tapas.mainFrame.MainFrame;
import com.peyrona.tapas.utils.Utils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class OfficeDialog extends JDialog
{
    private JTabbedPane tabbedPane = new JTabbedPane();

    //------------------------------------------------------------------------//

    public OfficeDialog()
    {
        super( MainFrame.getInstance() );

        setModal( true );
        setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        setTitle( "Tareas Administrativas Internas" );
        setLayout( new BorderLayout() );

        initComponents();

        pack();
        setLocationRelativeTo( MainFrame.getInstance() );
        setVisible( true );
    }

    //------------------------------------------------------------------------//

    // Desde aquí se informa a cada uno de los tabs que la dialog va a cerrarse,
    // permitiéndoles de este modo actualizar sus datos o realizar cualquier
    // otra acción.
    // Solo los tabs que implementan ActionListener serán notifiacdos.
    private void onClose()
    {
        // Ocultamos la ventana, pero no será destruida hasta que los tabs no hayan sido notificados
        setVisible( false );

        // Cerrar los tabs implica leer y escribir en el repositorio de datos =>
        // puede ser lento. Al meterlo en una thread no paramos el GUI.
        // En Swing, la respuesta a los eventos tiene que ser muy rápida.
        // Si fuese lenta, se utiliza SwingWorker, pero como en este caso no hay
        // que actualizar el GUI, no es necesario utilizar SwingWorker y basta
        // siemplemente con una Thread.
        Thread tSave = new Thread()
        {
            @Override
            public void run()
            {
                // Mientras se estén guardando datos, no se puede salir de la app
                MainFrame.getInstance().setAllowExit( false );

                ActionEvent ae = new ActionEvent( this, ActionEvent.ACTION_PERFORMED, null );

                for( Component comp : tabbedPane.getComponents() )
                {
                    if( comp instanceof ActionListener )
                        ((ActionListener) comp).actionPerformed( ae );
                }

                // No se puden destruir los componentes hasta no haber cerrado todos los tabs
                OfficeDialog.this.dispose();

                // Por lo que a este proceso ataña, ya se puede salir de aplicación
                MainFrame.getInstance().setAllowExit( true );
            }
        };

        tSave.start();

        if( ! Utils.bDEBUGGING )
        {
            JOptionPane.showMessageDialog( MainFrame.getInstance(),
                                           "Si ha realizado cambios, estos no surtirán\n"+
                                           "efecto hasta que reinicie la aplicación." );
        }
    }

    private void initComponents()
    {
        tabbedPane.add( "Caja"  , new DailyReport() );
        tabbedPane.add( "Básico", new Basic() );
        tabbedPane.add( "Carta" , new Menu() );
        tabbedPane.add( "Ticket", new Ticket() );

        add( tabbedPane, BorderLayout.CENTER );

        // Para informar a los tabs que la dialog va a cerrarse
        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( WindowEvent we )
            {
                onClose();
            }
        } );
    }
}