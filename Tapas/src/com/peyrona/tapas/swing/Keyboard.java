/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.peyrona.tapas.swing;

import com.peyrona.tapas.mainFrame.MainFrame;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Francisco Morero Peyrona
 */
public final class Keyboard extends JDialog
{
    private JTextComponent text;   // A quien se le van a enviar las letras

    //------------------------------------------------------------------------//

    public Keyboard( JTextComponent text )
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

        private Button( String sName, ImageIcon icon )
        {
            super( icon );
            setName( sName );

            init();
            addActionListener( new java.awt.event.ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    if( "CLEAR".equals( getName() ) )
                        Keyboard.this.text.setText( null );
                    else
                        Keyboard.this.dispose();
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