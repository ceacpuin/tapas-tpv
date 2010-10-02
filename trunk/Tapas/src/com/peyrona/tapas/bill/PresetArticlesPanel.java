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

import com.peyrona.tapas.persistence.Article;
import com.peyrona.tapas.persistence.DataProvider;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Francisco Morero Peyrona
 */

// NEXT: Habría que mejorar los paneles de Categorías y Artículos para que el
//       sistema de desplazamiento fuese más amigable: por ejemplo, se podrían
//       sustituir las JScrollBar de los JScrollPanel por unos botones al
//       principio y el final del panel que hicieran el scroll.

final class PresetArticlesPanel extends JPanel
{
    private final static int nGAP = 7;

    private JPanel               pnlSubMenu   = new JPanel( new GridLayout( 5, 5, nGAP, nGAP ) );
    private List<ActionListener> lstListeners = new ArrayList<ActionListener>();

    //------------------------------------------------------------------------//

    PresetArticlesPanel()
    {
        JScrollPane spCategories = new JScrollPane( getCategoriesPanel() );
                    spCategories.setBorder( null );

        JScrollPane spProducts   = new JScrollPane( pnlSubMenu );
                    spProducts.setBorder( null );

        setLayout( new BorderLayout( 0, 9 ) );
        setBorder( new CompoundBorder( new LineBorder( Color.gray,2, true ),
                                       new EmptyBorder( nGAP,nGAP,nGAP,nGAP ) ) );
        setMinimumSize( new Dimension( 510, 680 ) );
        setPreferredSize( getMinimumSize() );
        add( spCategories, BorderLayout.NORTH  );
        add( spProducts  , BorderLayout.CENTER );
    }

    public void addActionListener( ActionListener al )
    {
        if( al != null )
            lstListeners.add( al );
    }

    public void removeActionListener( ActionListener al )
    {
        if( al != null && lstListeners.contains( al ) )
            lstListeners.remove( al );
    }

    //------------------------------------------------------------------------//

    private JPanel getCategoriesPanel()
    {
        List<Article> list  = DataProvider.getInstance().getCategoriesAndProducts();
        JPanel        panel = new JPanel( new FlowLayout( FlowLayout.CENTER, nGAP, 0 ) );
        ButtonGroup   group = new ButtonGroup();

        for( int n = 0; n < list.size(); n++ )
        {
            final ButtonCategory button = new ButtonCategory( list.get( n ) );

            panel.add( button );
            group.add( button.getButton() );

            if( n == 0 )
            {
                EventQueue.invokeLater( new Runnable()
                {
                    public void run(){ button.getButton().doClick(); }
                } );
            }
        }

        return panel;
    }

    //------------------------------------------------------------------------//
    // Inner class
    //------------------------------------------------------------------------//
    private abstract class ButtonBase extends JPanel implements ActionListener
    {
        private Article        article;
        private AbstractButton button;
        private JLabel         label;

        ButtonBase( Article article, AbstractButton btn )
        {
            button = btn;
            button.setIcon( article.getIcon() );
            button.setFocusPainted( false );
            button.setMargin( new Insets( 2, 2, 2, 2 ) );

            label = new JLabel( article.getCaption() );
            label.setHorizontalAlignment( SwingConstants.CENTER );
            label.setFont( getFont().deriveFont( Font.PLAIN, 9f ) );

            setMinimumSize( new Dimension(  110, 120 ) );
            setMaximumSize( getMinimumSize() );
            setPreferredSize( getMinimumSize() );
            setLayout( new BorderLayout( 0,0 ) );

            add( button, BorderLayout.CENTER );
            add( label , BorderLayout.SOUTH  );

            this.article = article;
        }

        Article        getArticle() { return article; }
        AbstractButton getButton()  { return button;  }
        JLabel         getLabel()   { return label;   }
    }

    //------------------------------------------------------------------------//
    // Inner class
    //------------------------------------------------------------------------//
    private final class ButtonCategory extends ButtonBase
    {
        ButtonCategory( Article article )
        {
            super( article, new JToggleButton() );

            getButton().addActionListener( ButtonCategory.this );
        }

        @Override
        public void actionPerformed( ActionEvent ae )
        {
            List<Article> lstArticles = getArticle().getSubMenu();
            final int     nCols = 4;
            int           nRows = lstArticles.size() / nCols;
                          nRows = (nRows < 4 ? 4 : nRows);

            GridLayout layout = (GridLayout) pnlSubMenu.getLayout();
                       layout.setRows( nRows );
                       layout.setColumns( nCols );

            // Elimina todos los botones de productos
            for( Component comp : pnlSubMenu.getComponents() )
            {
                layout.removeLayoutComponent( comp );
                pnlSubMenu.remove( comp );
            }

            // Añade los productos de la nueva categoría seleccionada
            for( Article a : lstArticles )
                pnlSubMenu.add( new ButtonProduct( a ) );

            // Rellena con botones vacíos
            for( int n = 0; n < (nRows * nCols) - lstArticles.size(); n++ )
                pnlSubMenu.add( new JButton() );

            // Reorganiza los componentes (botones)
            layout.layoutContainer( pnlSubMenu );
            pnlSubMenu.repaint();
        }
    }

    //------------------------------------------------------------------------//
    // Inner class
    //------------------------------------------------------------------------//
    private final class ButtonProduct extends ButtonBase
    {
        ButtonProduct( Article article )
        {
            super( article, new JButton() );
            
            getButton().addActionListener( ButtonProduct.this );
        }

        @Override
        public void actionPerformed( ActionEvent ae )
        {
            ae.setSource( getArticle() );

            for( ActionListener al : lstListeners )
                al.actionPerformed( ae );
        }
    }
}