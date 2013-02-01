/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
