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
import com.peyrona.tapas.persistence.Configuration;
import com.peyrona.tapas.persistence.DataProvider;
import com.peyrona.tapas.Utils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class OfficePanel extends JTabbedPane
{
    public OfficePanel()
    {
        initComponents();
    }

    public void showDialog()
    {
        JDialog dlg = new JDialog( MainFrame.getInstance() );
                dlg.setModal( true );
                dlg.setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );   // Se destruye manualmente (ver ::onClose())
                dlg.setTitle( "Tareas Administrativas Internas" );
                dlg.setLayout( new BorderLayout() );
                dlg.setContentPane( this );

                // Para informar a los tabs que la dialog va a cerrarse
                dlg.addWindowListener( new WindowAdapter()
                {
                    @Override
                    public void windowClosing( WindowEvent we )
                    {
                        OfficePanel.this.onClose();
                    }
                } );

                dlg.pack();
                dlg.setLocationRelativeTo( MainFrame.getInstance() );
                dlg.setVisible( true );
    }

    //------------------------------------------------------------------------//

    // Desde aquí se informa a cada uno de los tabs que la dialog va a cerrarse,
    // permitiéndoles de este modo actualizar sus datos o realizar cualquier
    // otra acción.
    // Solo los tabs que implementan ActionListener serán notifiacdos.
    private void onClose()
    {
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

                for( Component comp : OfficePanel.this.getComponents() )
                {
                    if( comp instanceof ActionListener )
                        ((ActionListener) comp).actionPerformed( ae );
                }

                // No se puden destruir los componentes hasta no haber cerrado todos los tabs
                SwingUtilities.getWindowAncestor( OfficePanel.this ).dispose();

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
        Configuration config = DataProvider.getInstance().getConfiguration();

        add( "Básico", new Basic( config ) );    // Estos dos componentes comparten la misma instancia de config
        add( "Caja"  , new DailyReport() );
        add( "Carta" , new Menu() );
        add( "Ticket", new Ticket( config ) );   // Estos dos componentes comparten la misma instancia de config
        setSelectedIndex( 1 );
    }
}