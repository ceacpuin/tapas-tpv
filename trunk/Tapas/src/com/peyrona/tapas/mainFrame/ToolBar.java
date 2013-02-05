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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * ToolBar de la ventana principal.
 * 
 * @author Francisco Morero Peyrona
 */
final class ToolBar extends JPanel
{
    public static final String sACTION_NEW_ACCOUNT  = "NewAccount";
    public static final String sACTION_FIND_ACCOUNT = "FindAccount";
    public static final String sACTION_OPEN_BOX     = "OpenBox";
    public static final String sACTION_MOSAIC       = "Mosaic";
    public static final String sACTION_OFFICE       = "Office";
    public static final String sACTION_CLOSE        = "Close";

    private List<ActionListener> lstListeners = new ArrayList<ActionListener>();

    //------------------------------------------------------------------------//

    ToolBar()
    {
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );

        add( new Button( "Cuenta"  , sACTION_NEW_ACCOUNT , "cuenta"   ) );
        add( new Button( "Buscar"  , sACTION_FIND_ACCOUNT, "buscar"   ) );
        add( new Button( "Monedero", sACTION_OPEN_BOX    , "monedero" ) );
        add( new Button( "Alinear" , sACTION_MOSAIC      , "alinear"  ) );
        add( Box.createRigidArea( new Dimension( 32, 1 ) ) );
        add( new Button( "Oficina" , sACTION_OFFICE      , "oficina"  ) );
        add( new Button( "Salir"   , sACTION_CLOSE       , "salir"    ) );
        add( Box.createHorizontalGlue() );
        add( new Clock() );
    }

    public void addActionListener( ActionListener al )
    {
        if( al != null )
        {
            lstListeners.add( al );
        }
    }

    public void removeActionListener( ActionListener al )
    {
        if( al != null && lstListeners.contains( al ) )
        {
            lstListeners.remove( al );
        }
    }

    //------------------------------------------------------------------------//

    private void fireActionEvent( ActionEvent ae )
    {
        for( ActionListener al : lstListeners )
        {
            al.actionPerformed( ae );
        }
    }

    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    private final class Button extends JButton
    {
        Button( String sText, String sActionCommand, String sIcon )
        {
            super( sText );
            setActionCommand( sActionCommand );
            setIcon( new ImageIcon( getClass().getResource( "images/"+ sIcon +".png" ) ) );
            setFont( getFont().deriveFont( Font.BOLD, 14f ) );
            setMaximumSize( new Dimension( 220, 60 ) );

            // Si la resolución horizontal es baja, hacemos los iconos más pequeños
            // poniendo las etiquetas (text) bajo el icono en lugar de a su derecha.
            // Asumimos que sólo hay una pantalla asociada (es lo normal en un TPV).
            if( Toolkit.getDefaultToolkit().getScreenSize().width < 1200 )
            {
                setMaximumSize( new Dimension( 110, 90 ) );
                setHorizontalTextPosition( AbstractButton.CENTER );
                setVerticalTextPosition( AbstractButton.BOTTOM );
            }

            addActionListener( new ActionListener()
                 {
                    @Override
                    public void actionPerformed( ActionEvent ae )
                    {
                        ToolBar.this.fireActionEvent( ae );
                    }
                 } );
        }
    }
}