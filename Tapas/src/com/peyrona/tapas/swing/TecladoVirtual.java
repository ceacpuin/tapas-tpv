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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

/**
 * Muestra un teclado en pantalla y le envía las pulsaciones de tecla al Text
 * Component asociado.
 * <p>
 * Se podría pensar que sería más elegante hacer que el JPanel admitiese
 * listeners y que informase a estos cuando se pulsa una tecla, pero no es así,
 * de hecho es menos elegante y el código resultante para utilizar esta clase
 * es mucho más engorroso.
 *
 * @author Francisco Morero Peyrona
 */
public final class TecladoVirtual extends JPanel
{
    private static final String BTN_CLEAR = "CLEAR";
    private static final String BTN_CLOSE = "CLOSE";

    private JTextComponent text   = null;   // A quién se le van a enviar las letras
    private JDialog        dialog = null;   // Para facilitar el mostrar el panel

    //------------------------------------------------------------------------//

    public TecladoVirtual( JTextComponent text )
    {
        this.text = text;
        initComponents();
    }

    //------------------------------------------------------------------------//

    public void showInDialog()
    {
        Point position = text.getLocationOnScreen(); // Si está a null, es un error del programador: que casque

        dialog = new JDialog( MainFrame.getInstance() );
        dialog.setModal( true );
        dialog.setTitle( "Teclado virtual" );
        dialog.setContentPane( this );
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        dialog.pack();
        dialog.setLocation( position.x - (getWidth() - text.getWidth()) / 2,
                            position.y + text.getHeight() + 5 );
        dialog.setVisible( true );
    }

    //------------------------------------------------------------------------//

    private void initComponents()
    {
        String sKeyboard = "1234567890"+
                           "QWERTYUIOP"+
                           "ASDFGHJKLÑ"+
                           "ZXCVBNM ";

        setLayout( new GridLayout( 4,10 ) );

        for( int n = 0; n < sKeyboard.length(); n++ )
        {
            add( new Button( sKeyboard.charAt( n ) ) );
        }

        add( new Button( BTN_CLEAR, new ImageIcon( getClass().getResource( "images/clear.png" ) ) ) );
        add( new Button( BTN_CLOSE, new ImageIcon( getClass().getResource( "images/close.png" ) ) ) );
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
                    TecladoVirtual.this.text.setText( TecladoVirtual.this.text.getText() +
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
                    if( BTN_CLEAR.equals( getName() ) )
                    {
                        TecladoVirtual.this.text.setText( null );
                    }
                    else if( BTN_CLOSE.equals( getName() ) && dialog != null )
                    {
                        dialog.dispose();
                        dialog = null;
                    }
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