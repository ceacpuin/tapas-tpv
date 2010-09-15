/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.peyrona.tapas.accountDialog;

import com.peyrona.tapas.mainFrame.MainFrame;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Francisco Morero Peyrona
 */
final class Keyboard extends JDialog
{
    private JTextComponent text;   // A quien se le van a enviar las letras

    //------------------------------------------------------------------------//

    Keyboard( JTextComponent text )
    {
        super( MainFrame.getInstance() );

        setModal( true );
        setTitle( "Teclado virtual" );
        getContentPane().setLayout( new GridLayout( 4,10 ) );
        setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        initComponents();
        pack();

        this.text = text;
    }

    //------------------------------------------------------------------------//

    private void initComponents()
    {
        String sKeyboard = "1234567890"+
                           "QWERTYUIOP"+
                           "ASDFGHJKLÑ"+
                           "ZXCVBNMÇ ";

        for( int n = 0; n < sKeyboard.length(); n++ )
            add( new Button( sKeyboard.charAt( n ) ) );

        add( new Button( new ImageIcon( getClass().getResource( "images/clear.png" ) ) ) );
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
            addActionListener( new java.awt.event.ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    // Lamentablemente, JTextComponent no tiene un método append
                    Keyboard.this.text.setText( Keyboard.this.text.getText() +
                                                ((Button) ae.getSource()).getText() );
                }
            } );
        }

        private Button( ImageIcon icon )
        {
            super( icon );
            init();
            addActionListener( new java.awt.event.ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    Keyboard.this.text.setText( null );
                }
            } );
        }

        private void init()
        {
            setMaximumSize(   new Dimension( 55, 55 ) );
            setMinimumSize(   new Dimension( 50, 50 ) );
            setPreferredSize( getMaximumSize() );
            setFocusPainted( false );
            setMargin( new Insets( 4, 4, 4, 4 ) );
            setFont( getFont().deriveFont( Font.BOLD, 16f ) );
        }
    }
}