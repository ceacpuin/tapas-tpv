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

import com.peyrona.tapas.persistence.Product;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author peyrona
 */
final class ProductsPanel extends JPanel
{
    private static final int nCOLS = 4;  // Nº de columnas en las que se muestran los productos

    private List<ActionListener> lstListeners = new ArrayList<ActionListener>();

    //----------------------------------------------------------------------------//

    ProductsPanel()
    {
        setLayout( new GridLayout( 5, 5, MenuPanel.nGAP, MenuPanel.nGAP ) );
        setProducts( new ArrayList<Product>() );
    }

    //----------------------------------------------------------------------------//

    void setProducts( List<Product> lstProducts )
    {
        int nRows = lstProducts.size() / nCOLS;
            nRows = (nRows < 4 ? 4 : nRows);

        // Elimina todos los componentes y notifica al LayoutManager
        removeAll();

        // Recalculamos las filas
        GridLayout layout = (GridLayout) getLayout();
                   layout.setRows( nRows );
                   layout.setColumns( nCOLS );

        // Añade los productos de la nueva categoría seleccionada
        for( Product a : lstProducts )
        {
            add( new ButtonProduct( a ) );
        }

        // Rellena con botones vacíos
        for( int n = 0; n < (nRows * nCOLS) - lstProducts.size(); n++ )
        {
            add( new JButton() );
        }

        // Reorganiza los componentes (botones)
        revalidate();
    }

    void addActionListener( ActionListener al )
    {
        if( al != null )
        {
            lstListeners.add( al );
        }
    }

    void removeActionListener( ActionListener al )
    {
        if( al != null && lstListeners.contains( al ) )
        {
            lstListeners.remove( al );
        }
    }

    //------------------------------------------------------------------------//
    // Inner class
    //------------------------------------------------------------------------//
    private final class ButtonProduct extends ButtonBase
    {
        ButtonProduct( Product product )
        {
            super( product, new JButton() );

            getButton().addActionListener( ButtonProduct.this );
        }

        @Override
        public void actionPerformed( ActionEvent ae )
        {
            // En lugar de enviar el botón como fuente del evento, enviamos el Producto,
            // es más claro y más simple.
            // Además, así podemos mantener ButtonBase con "package" scope.
            ae.setSource( getProduct() );

            for( ActionListener al : lstListeners )
            {
                al.actionPerformed( ae );
            }
        }
    }
    //------------------------------------------------------------------------//
}
