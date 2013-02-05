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

package com.peyrona.tapas.mainFrame;

import com.peyrona.tapas.Utils;
import com.peyrona.tapas.office.OfficePanel;
import com.peyrona.tapas.persistence.Bill;
import com.peyrona.tapas.persistence.Configuration;
import com.peyrona.tapas.persistence.DataProvider;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * La ventana principal de la aplicación.
 * 
 * @author Francisco Morero Peyrona
 */
public final class MainFrame extends JFrame implements ActionListener
{
    private static MainFrame instance;

    private BillsDesktop desktop;
    private ToolBar      toolbar;

    //------------------------------------------------------------------------//

    public static MainFrame getInstance()
    {
        synchronized( MainFrame.class )
        {
            if( instance == null )
                instance = new MainFrame();
        }

        return instance;
    }

    //------------------------------------------------------------------------//

    @Override
    public void actionPerformed( ActionEvent ae )
    {
        String sAction = ae.getActionCommand();

             if( sAction.equals( ToolBar.sACTION_NEW_ACCOUNT  ) ) onNewAccount();
        else if( sAction.equals( ToolBar.sACTION_OPEN_BOX     ) ) onOpenMoneyBox();
        else if( sAction.equals( ToolBar.sACTION_FIND_ACCOUNT ) ) onFindAccount();
        else if( sAction.equals( ToolBar.sACTION_MOSAIC       ) ) onMosaic();
        else if( sAction.equals( ToolBar.sACTION_OFFICE       ) ) onOffice();
        else if( sAction.equals( ToolBar.sACTION_CLOSE        ) ) onExit();
    }

    //------------------------------------------------------------------------//

    private void onNewAccount()
    {
       desktop.createAccount();
    }

    private void onOpenMoneyBox()
    {
        // TODO: implementarlo
        JOptionPane.showMessageDialog( null, "Este botón abre la caja del dinero.\nOpción pendiente de ser implementada." );
    }

    private void onFindAccount()
    {
        FindBillPanel panel = new FindBillPanel();
                         panel.showDialog();

        Bill bill = panel.getSelectedAccount();

        if( bill != null  )
            desktop.openAccount( bill );
    }

    private void onMosaic()
    {
        desktop.mosaic();
    }

    private void onOffice()
    {
        boolean       bRun   = true;
        Configuration config = DataProvider.getInstance().getConfiguration();

        if( Utils.isNotEmpty( config.getPassword() ) )
        {
            String sPassword = JOptionPane.showInputDialog( getInstance(), "Contraseña", "" );

            bRun = config.isValidPassword( sPassword.toCharArray() );
        }

        if( bRun )
        {
           (new OfficePanel()).showDialog();
        }
        else
        {
            JOptionPane.showMessageDialog( getInstance(), "Contraseña errónea.\nInténtelo de nuevo." );
        }
    }

    private void onExit()
    {
        int nOption = JOptionPane.OK_OPTION;

        if( ! desktop.isEmpty() )
        {
            nOption = JOptionPane.showConfirmDialog( this,
                      "Aún quedan cuentas abiertas.\n¿Seguro que desea salir?",
                      "Cerrando la aplicación", JOptionPane.OK_CANCEL_OPTION );
        }

        if( nOption == JOptionPane.OK_OPTION )
        {
            sendMailWithTodaySales();
            dispose();
            System.exit( Utils.nEXIT_NO_ERROR );
        }
    }

    private void sendMailWithTodaySales()
    {
        Configuration config = DataProvider.getInstance().getConfiguration();
        String        email  = config.getEmail();

        if( Utils.isNotEmpty( email ) )
        {
            // TODO: Implementarlo
        }
    }

    private void initComponents()
    {
        desktop = new BillsDesktop();
        toolbar = new ToolBar();
        toolbar.addActionListener( this );

        JPanel pnlAll = new JPanel();
               pnlAll.setBorder( new EmptyBorder( 5, 8, 5, 8 ) );
               pnlAll.setLayout( new BorderLayout( 0, 9 ) );
               pnlAll.add( toolbar, BorderLayout.NORTH  );
               pnlAll.add( desktop, BorderLayout.CENTER );

       setLayout( new BorderLayout() );
       setContentPane( pnlAll );
    }

    private MainFrame()
    {
        setTitle( "Tapas: TPV para bares - Ver. 1.0 (Licencia GPL)" );
        setIconImage( new ImageIcon( getClass().getResource( "images/logo_small.png" ) ).getImage() );
        setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        setPreferredSize( new Dimension( 1024, 768 ) );

        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( WindowEvent we )
            {
                onExit();
            }
        } );

        initComponents();
    }
}