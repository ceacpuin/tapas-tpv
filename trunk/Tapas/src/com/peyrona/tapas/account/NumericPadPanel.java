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

package com.peyrona.tapas.account;

import com.peyrona.tapas.Utils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Francisco Morero Peyrona
 */
final class NumericPadPanel extends JPanel
{
    final static char cENTER = '=';
    final static char cCLEAR = 'C';

    private JButton btn7      = new Button(  "7" );
    private JButton btn8      = new Button(  "8" );
    private JButton btn9      = new Button(  "9" );
    private JButton btn4      = new Button(  "4" );
    private JButton btn5      = new Button(  "5" );
    private JButton btn6      = new Button(  "6" );
    private JButton btn1      = new Button(  "1" );
    private JButton btn2      = new Button(  "2" );
    private JButton btn3      = new Button(  "3" );
    private JButton btn0      = new Button(  "0" );
    private JButton btnClear  = new Button(  "C" );
    private JButton btnDecSep = new Button( String.valueOf( Utils.cDecimalSep ) );
    private JButton btnEnter  = new Button( "Ok" );
    
    NumericPadPanel()
    {
        setLayout( new BorderLayout() );
        initComponents();
    }

    void addActionListener( ActionListener l )
    {
        listenerList.add( ActionListener.class, l );
    }

    void removeActionListener( ActionListener l )
    {
        listenerList.remove( ActionListener.class, l );
    }

    @Override
    public void setEnabled( boolean b )
    {
        super.setEnabled( b );

        btn7.setEnabled( b );
        btn8.setEnabled( b );
        btn9.setEnabled( b );
        btn4.setEnabled( b );
        btn5.setEnabled( b );
        btn6.setEnabled( b );
        btn1.setEnabled( b );
        btn2.setEnabled( b );
        btn3.setEnabled( b );
        btn0.setEnabled( b );
        btnClear.setEnabled( b );
        btnDecSep.setEnabled( b );
        btnEnter.setEnabled( b );
    }

    //------------------------------------------------------------------------//

    // Este es el mode estándar de procesar eventos: hay otros modos más sencillos.
    private void fireActionPerformed( ActionEvent ae )
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying those that are interested in this event
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == ActionListener.class )
                ((ActionListener) listeners[n+1]).actionPerformed( ae );
        }
    }

    private void initComponents()
    {
        btnEnter.setActionCommand( "=" );   // Hay que cambiarla porque es distinta de su Text
        btnEnter.setMargin( new Insets( 0,0,0,0 ) );
        btnEnter.setPreferredSize( btnEnter.getMaximumSize() );

        JPanel pnl = new JPanel( new GridLayout( 2, 6, 0, 0 ) );
               pnl.add( btn1 );
               pnl.add( btn2 );
               pnl.add( btn3 );
               pnl.add( btn4 );
               pnl.add( btn5 );
               pnl.add( btnClear );
               pnl.add( btn6 );
               pnl.add( btn7 );
               pnl.add( btn8 );
               pnl.add( btn9 );
               pnl.add( btn0 );
               pnl.add( btnDecSep );

       add( pnl     , BorderLayout.CENTER );
       add( btnEnter, BorderLayout.EAST   );
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private final class Button extends JButton
    {
        private Button( String sText )
        {
            super( sText );

            setActionCommand( sText );
            setMaximumSize(   new Dimension( 64, 64 ) );
            setMinimumSize(   new Dimension( 32, 32 ) );
            setPreferredSize( new Dimension( 48, 48 ) );
            setFocusPainted( false );
            setMargin( new Insets( 4, 4, 4, 4 ) );
            setFont( getFont().deriveFont( Font.BOLD, 16f ) );

            addActionListener( new java.awt.event.ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    NumericPadPanel.this.fireActionPerformed( ae );
                }
            } );
        }
    }
}