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

import com.peyrona.tapas.account.BillAndMenuPanel;
import com.peyrona.tapas.persistence.DataProvider;
import com.peyrona.tapas.persistence.Product;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Panel que contiene las categorías y los productos.
 * 
 * @author Francisco Morero Peyrona
 */
public final class MenuPanel extends JPanel
{
    final static int nGAP = 7;

    private CategoriesPanel pnlCategories = new CategoriesPanel();
    private ProductsPanel   pnlProducts   = new ProductsPanel();

    //------------------------------------------------------------------------//

    public MenuPanel()
    {
        JScrollBar scrollBar;
        Dimension  sbSize;

        // Panel de Categorias
        pnlCategories.setCategories( DataProvider.getInstance().getCategoriesAndProducts() );

        JScrollPane spCategories = new JScrollPane( pnlCategories );
                    spCategories.setBorder( null );
                    spCategories.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
                    spCategories.setVerticalScrollBarPolicy(   ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );

        // Cambia el grosor de la JScrollBar horizontal (sólo se muestra si es necesario)
        scrollBar = spCategories.getHorizontalScrollBar();
        sbSize    = new Dimension( scrollBar.getPreferredSize().width, 30 );
        scrollBar.setPreferredSize( sbSize );

        // Panel de Productos
        JScrollPane spProducts   = new JScrollPane( pnlProducts );
                    spProducts.setBorder( null );
                    spProducts.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );

        // Cambia el grosor de la JScrollBar horizontal (sólo se muestra si es necesario)
        scrollBar = spProducts.getHorizontalScrollBar();
        sbSize    = new Dimension( scrollBar.getPreferredSize().width, 30 );
        scrollBar.setPreferredSize( sbSize );

        // Lo montamos todo
        setLayout( new BorderLayout( 0, 9 ) );
        setBorder( new CompoundBorder( new LineBorder( Color.gray,2, true ),
                                       new EmptyBorder( nGAP,nGAP,nGAP,nGAP ) ) );
        setMinimumSize( BillAndMenuPanel.SUBPANEL_DIMENSION );
        setPreferredSize( getMinimumSize() );
        add( spCategories, BorderLayout.NORTH  );
        add( spProducts  , BorderLayout.CENTER );

        // Añadimos un listener para que se nos informe cada vez que se selecciona una categoría
        pnlCategories.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent ae )
            {
                Product category = (Product) ae.getSource();
                pnlProducts.setProducts( category.getSubMenu() );
            }
        } );
    }

    public void addActionListener( ActionListener al )
    {
        pnlProducts.addActionListener( al );
    }

    public void removeActionListener( ActionListener al )
    {
        pnlProducts.removeActionListener( al );
    }
}