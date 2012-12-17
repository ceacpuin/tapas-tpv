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

package com.peyrona.tapas.swing;

import com.peyrona.tapas.mainFrame.MainFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.text.JTextComponent;

/**
 * Muestra un teclado en pantalla y le envía las pulsaciones de tecla al Text
 * Component asociado.
 *
 * @author Francisco Morero Peyrona
 */
public final class KeyboardVirtual extends JDialog
{
    private JTextComponent text;   // A quién se le van a enviar las letras

    //------------------------------------------------------------------------//

    public KeyboardVirtual( JTextComponent text )
    {
        super( MainFrame.getInstance() );

        this.text = text;

        setModal( true );
        setTitle( "Teclado virtual" );
        getContentPane().setLayout( new GridLayout( 4,10 ) );
        setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        initComponents();
        pack();

        Point position = text.getLocationOnScreen();

        setLocation( position.x - (getWidth() - text.getWidth()) / 2,
                     position.y + text.getHeight() + 5 );
    }

    //------------------------------------------------------------------------//

    private void initComponents()
    {
        String sKeyboard = "1234567890"+
                           "QWERTYUIOP"+
                           "ASDFGHJKLÑ"+
                           "ZXCVBNM ";

        for( int n = 0; n < sKeyboard.length(); n++ )
            add( new Button( sKeyboard.charAt( n ) ) );

        add( new Button( "CLEAR", new ImageIcon( getClass().getResource( "images/clear.png" ) ) ) );
        add( new Button( "CLOSE", new ImageIcon( getClass().getResource( "images/close.png" ) ) ) );
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private final class Button extends JButton
    {
        private Button( char cLetter )
        {
            setText( String.valueOf( cLetter ) );
            init();

            // No me importa que se fabrique un ActionListener nuevo para cada
            // botón porque cuando se cierre la dialog esa memoria se libera y
            // de este modo las cosas son más fáciles.
            addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent ae )
                {
                    // Lamentablemente, JTextComponent no tiene un método append
                    KeyboardVirtual.this.text.setText( KeyboardVirtual.this.text.getText() +
                                                ((Button) ae.getSource()).getText() );
                }
            } );
        }

        private Button( String sName, ImageIcon icon )
        {
            super( icon );
            setName( sName );

            init();

            addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent ae )
                {
                    if( "CLEAR".equals( getName() ) )
                        KeyboardVirtual.this.text.setText( null );
                    else
                        KeyboardVirtual.this.dispose();
                }
            } );
        }

        private void init()
        {
            setMaximumSize( new Dimension( 55, 55 ) );
            setMinimumSize( getMaximumSize() );
            setPreferredSize( getMaximumSize() );
            setFocusPainted( false );
            setMargin( new Insets( 4, 4, 4, 4 ) );
            setFont( getFont().deriveFont( Font.BOLD, 16f ) );
        }
    }
}