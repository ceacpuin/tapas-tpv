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

package com.peyrona.tapas.bill;

import com.peyrona.tapas.swing.TecladoVirtual;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Francisco Morero Peyrona
 */
final class BillOwnerPanel extends JPanel implements ActionListener
{
    private JTextField txtName;

    BillOwnerPanel( String sName )
    {
        JLabel lblName = new JLabel( "Cliente" );
               lblName.setFont( lblName.getFont().deriveFont( 18f ) );
        txtName = new JTextField( sName );
        txtName.setFont( txtName.getFont().deriveFont( 16f ) );
        JButton btnKeyboard = new JButton( new ImageIcon( getClass().getResource( "images/keyboard.png") ) );
                btnKeyboard.setMargin( new Insets( 2, 4, 2, 4 ) );
                btnKeyboard.addActionListener( BillOwnerPanel.this );

        setLayout( new BorderLayout( 7, 0 ) );
        add( lblName    , BorderLayout.WEST   );
        add( txtName    , BorderLayout.CENTER );
        add( btnKeyboard, BorderLayout.EAST   );
    }

    String getCustomerName()
    {
        return txtName.getText().trim();
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        (new TecladoVirtual( txtName )).showInDialog();
    }
}