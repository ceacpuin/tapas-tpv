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
import com.peyrona.tapas.account.AccountPanel;
import com.peyrona.tapas.persistence.Bill;
import com.peyrona.tapas.persistence.DataProvider;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Francisco Morero Peyrona
 */
final class AccountInternalFrame extends JInternalFrame
{
    private static int nCount = 0;

    private LabelAmount lblAmount;
    private Bill        bill;

    //------------------------------------------------------------------------//
    
    AccountInternalFrame()
    {
        this( null );
    }

    AccountInternalFrame( Bill bill )
    {
        bill      = (bill == null ? new Bill() : bill);
        lblAmount = new LabelAmount();
        lblAmount.setAmount( bill.getTotal() );

        setResizable( false );
        setIconifiable( false );
        setMaximizable( false );
        setClosable( false );
        setFrameIcon( null );
        setCustomer( bill.getCustomer() );
        setLocation( nCount*12+4, nCount*12+4 );
        
        nCount = nCount > 30 ? 0 : nCount+1;

        JPanel panel = new JPanel( new BorderLayout( 0, 7 ) );
               panel.setBorder( new EmptyBorder( 9,9,9,9 ) );
               panel.add( new ButtonUpdate(), BorderLayout.CENTER );
               panel.add( lblAmount         , BorderLayout.SOUTH );

        getContentPane().add( panel );
        pack();
    }

    @Override
    public void setSelected( boolean bSelected )
    {
        try
        {
            super.setSelected( bSelected );
        }
        catch( PropertyVetoException exc )
        {
            /* Nada que hacer */
        }
    }

    //------------------------------------------------------------------------//

    private void setCustomer( String sCustomer )
    {
        sCustomer = (sCustomer.length() > 3 ? sCustomer : "Cuenta - "+ sCustomer );

        setTitle( sCustomer );
    }

    private void onCloseBill()
    {
        if( bill.getLines().size() > 0 )
        {
            DataProvider.getInstance().insertBill( bill );
            // NEXT: printTicket( bill );
        }

        dispose();
    }

    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    private final class ButtonUpdate extends JButton implements ActionListener
    {
        ButtonUpdate()
        {
            setMargin( new Insets( 9,0,9,0 ) );
            setFont( getFont().deriveFont( Font.BOLD, 16f ) );
            setText( "Modificar" );
            addActionListener( ButtonUpdate.this );
        }

        @Override
        public void actionPerformed( ActionEvent ae )
        {
            AccountPanel dialog = new AccountPanel( AccountInternalFrame.this.bill );
                          dialog.showDialog();

            bill = dialog.getBill();
            setCustomer( bill.getCustomer() ); // Por si ha cambiado (es más simple que comprobar si ha cambiado)
            lblAmount.setAmount( bill.getTotal() );

            if( bill.isClosed() )
                onCloseBill();
        }
    }

    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    private final class LabelAmount extends JLabel
    {
        LabelAmount()
        {
            setFont( getFont().deriveFont( Font.BOLD, 16f ) );
            setHorizontalAlignment( JLabel.CENTER );
        }

        void setAmount( BigDecimal nAmount )
        {
            setText( Utils.formatAsCurrency( nAmount ) );
        }
    }
}