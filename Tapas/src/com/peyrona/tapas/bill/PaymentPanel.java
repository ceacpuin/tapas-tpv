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

import com.peyrona.tapas.persistence.Bill;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Francisco Morero Peyrona
 */
final class PaymentPanel extends JPanel
{
    private Bill.Payment payMode = null;

    PaymentPanel()
    {
        setBorder( new EmptyBorder( 10, 0, 17, 0 ) );
        setLayout( new GridLayout( 1, 5, 2, 0 ) );

        JButton btnPaid = new Button( "Cobrar" );
                btnPaid.addActionListener( new ActionListener()
                {
                    @Override public void actionPerformed( ActionEvent ae )  { closeDialog( Bill.Payment.Paid ); }
                } );
        JButton btnDefer = new Button( "Aplazar" );
                btnDefer.addActionListener( new ActionListener()
                {
                    @Override public void actionPerformed( ActionEvent ae )  { closeDialog( Bill.Payment.Deferred ); }
                } );
        JButton btnInvitation = new Button( "Invitación" );
                btnInvitation.addActionListener( new ActionListener()
                {
                    @Override public void actionPerformed( ActionEvent ae )  { closeDialog( Bill.Payment.Invitation ); }
                } );
        JButton btnNotPaid = new Button( "Impagado" );
                btnNotPaid.addActionListener( new ActionListener()
                {
                    @Override public void actionPerformed( ActionEvent ae )  { closeDialog( Bill.Payment.NotPaid ); }
                } );

        JButton btnClose = new Button( "Cerrar" );
                btnClose.addActionListener( new ActionListener()
                {
                    @Override public void actionPerformed( ActionEvent ae )  { closeDialog( null ); }
                } );
        
        add( btnPaid );
        add( btnDefer );
        add( btnInvitation );
        add( btnNotPaid );
        add( btnClose );
    }

    Bill.Payment getPayMode()
    {
        return payMode;
    }

    @Override
    public void setEnabled( boolean b )
    {
        super.setEnabled( b );

        Component[] comp = getComponents();

        // Actúa sobre todos los botones menos el último: siempre se puede cerrar la dialog
        for( int n = 0; n < comp.length-1; n++ )
            comp[n].setEnabled( b );
    }

    //------------------------------------------------------------------------//
    
    private void closeDialog( Bill.Payment payment )
    {
        payMode =  payment;
        SwingUtilities.getWindowAncestor( this ).dispose();
    }

    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    private final class Button extends JButton
    {
        Button( String sText )
        {
            setMargin( new Insets( 16, 0, 16, 0 ) );
            setFont( getFont().deriveFont( 14f ) );
            setText( sText );
        }
    }
}