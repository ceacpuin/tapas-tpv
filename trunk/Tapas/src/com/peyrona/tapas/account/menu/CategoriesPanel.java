/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peyrona.tapas.account.menu;

import com.peyrona.tapas.persistence.Product;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JToggleButton;

/**
 *
 * @author peyrona
 */
final class CategoriesPanel extends JPanel
{
    private List<ActionListener> lstListeners = new ArrayList<ActionListener>();

    //----------------------------------------------------------------------------//

    CategoriesPanel()
    {
        setLayout( new FlowLayout( FlowLayout.CENTER, MenuPanel.nGAP, 0 ) );

        setCategories( new ArrayList<Product>( Arrays.asList( new Product[] { new Product(), new Product(), new Product() } ) ) );
    }

    //----------------------------------------------------------------------------//

    void setCategories( List<Product> categories )
    {
        // Elimina todos los componentes y notifica al LayoutManager
        removeAll();

        ButtonGroup btnGroup = new ButtonGroup();

        for( int n = 0; n < categories.size(); n++ )
        {
            final ButtonCategory button = new ButtonCategory( categories.get( n ) );

            add( button );
            btnGroup.add( button.getButton() );

            if( n == 0 )
            {
                EventQueue.invokeLater( new Runnable()
                {
                    @Override public void run()
                    {
                        button.getButton().doClick();
                    }
                } );
            }
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
    private final class ButtonCategory extends ButtonBase
    {
        ButtonCategory( Product product )
        {
            super( product, new JToggleButton() );

            getButton().addActionListener( ButtonCategory.this );
        }

        @Override
        public void actionPerformed( ActionEvent ae )
        {
            // Aquí no hace falta enviar el Producto (como ocurre en ProductsPanel) porque
            // el único que va a recibir estos eventos es una clase de este paquete y
            // por lo tanto tiene visibilidad de la clase ButtonBase, pero enviamos de
            // todos modos el Producto en lugar del Botón por coherencia con la otra clase.
            ae.setSource( getProduct() );

            for( ActionListener al : lstListeners )
            {
                al.actionPerformed( ae );
            }
        }
    }
    //------------------------------------------------------------------------//
}
