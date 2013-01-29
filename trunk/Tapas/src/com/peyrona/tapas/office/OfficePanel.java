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
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * El panel principal que aparece en la dialog de "Oficina".
 *
 * @author Francisco Morero Peyrona
 */
public final class OfficePanel extends JTabbedPane
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
        (new Runnable()
        {
            @Override
            public void run()
            {
                ActionEvent ae = new ActionEvent( this, ActionEvent.ACTION_PERFORMED, null );

                for( Component comp : OfficePanel.this.getComponents() )
                {
                    if( comp instanceof ActionListener )
                    {
                        ((ActionListener) comp).actionPerformed( ae );
                    }
                }

                // No se puden destruir los componentes hasta no haber cerrado todos los tabs
                SwingUtilities.getWindowAncestor( OfficePanel.this ).dispose();
            }
        }).run();

        JOptionPane.showMessageDialog( MainFrame.getInstance(),
                                       "Los cambios realizados surtirán efecto\ncuando se reinicie la aplicación.",
                                       "Atención",
                                       JOptionPane.INFORMATION_MESSAGE );
    }

    private void initComponents()
    {
        Configuration config = DataProvider.getInstance().getConfiguration();

        // No sé por qué, pero si no hago esto, el panel Básico, aparece en la parte inferiror del tab.
        JPanel pnl = new JPanel( new BorderLayout() );
               pnl.add( new Basic( config ), BorderLayout.NORTH );

        add( "Básico", pnl );                    // Estos dos componentes comparten la misma instancia de config
        add( "Caja"  , new DailyReport() );
        add( "Carta" , new Menu() );
        add( "Ticket", new Ticket( config ) );   // Estos dos componentes comparten la misma instancia de config
        setSelectedIndex( 1 );  // Tab Caja
    }
}