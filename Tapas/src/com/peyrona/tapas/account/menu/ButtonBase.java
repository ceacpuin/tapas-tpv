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
package com.peyrona.tapas.account.menu;

import com.peyrona.tapas.Utils;
import com.peyrona.tapas.persistence.Product;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author peyrona
 */
abstract class ButtonBase extends JPanel implements ActionListener
{
    private static final Dimension BUTTON_SIZE  = new Dimension( 110, 120 );
    private static final int       SCALE_METHOD = (Utils.getCores() > 1 ? Image.SCALE_SMOOTH : Image.SCALE_FAST);

    private Product        product;
    private AbstractButton button;
    private JLabel         label;

    //----------------------------------------------------------------------------//

    ButtonBase( Product product, AbstractButton btn )
    {
        setMinimumSize( BUTTON_SIZE );
        setMaximumSize( getMinimumSize() );
        setPreferredSize( getMinimumSize() );
        setLayout( new BorderLayout( 0,0 ) );

        button = btn;
        button.setIcon( customizeIcon( product ) );
        button.setFocusPainted( false );
        button.setMargin( new Insets( 2, 2, 2, 2 ) );

        label = new JLabel( customizeCaption( product ) );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        label.setFont( getFont().deriveFont( Font.PLAIN, 9f ) );

        add( button, BorderLayout.CENTER );
        add( label , BorderLayout.SOUTH  );

        this.product = product;
    }

    Product        getProduct() { return product; }
    AbstractButton getButton()  { return button;  }
    JLabel         getLabel()   { return label;   }

    //----------------------------------------------------------------------------//

    /**
     * Comprueba que el tamaño del icono asociado es el apropiado para el
     * botón y si no lo és, redimensiona el icono.
     *
     * El algoritmo de redimensionamiento se realiza en base a la potencia del
     * ordenador donde corre la aplicación.
     * <p>
     * Nota: el icono resultante no se guarda de vuelta en el Producto, porque
     * si este fuese guardado posteriormente en la DB, se guardaría modificado y
     * es mejor no alterar nunca los originales de forma permanente.
     *
     * @param product Del que obtener el icono
     * @return El Icono del botón en su tamaña apropiado o null si no hay icono.
     */
    private ImageIcon customizeIcon( Product product )
    {
        ImageIcon icon = ((product == null) ? null : product.getIcon());

        if( icon != null )
        {
            int width  = BUTTON_SIZE.width  - 20;
            int height = BUTTON_SIZE.height - 30;

            if( (icon.getIconWidth()  > width) ||
                (icon.getIconHeight() > height) )
            {
                Image imagen = icon.getImage();
                      imagen = imagen.getScaledInstance( width, height, SCALE_METHOD );

                icon = new ImageIcon( imagen );
            }
        }

        return icon;
    }

    /**
     * Corta el texto si fuese demasiado largo.
     * <p>
     * Nota: el icono resultante no se guarda de vuelta en el Producto, porque
     * si este fuese guardado posteriormente en la DB, se guardaría modificado y
     * es mejor no alterar nunca los originales de forma permanente.
     *
     * @param product Del que obtener el caption
     * @return
     */
    private String customizeCaption( Product product )
    {
        String caption = ((product == null) ? null : product.getCaption());

        // Next: hacerlo poniendo la elipsis en el centro, no al final (como hace la label por defecto)

        return caption;
    }
}