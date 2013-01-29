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
package com.peyrona.tapas.account;

import com.peyrona.tapas.account.products.MasterDetailPanel;
import com.peyrona.tapas.account.bill.BillPanel;
import com.peyrona.tapas.mainFrame.MainFrame;
import com.peyrona.tapas.persistence.Product;
import com.peyrona.tapas.persistence.Bill;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Cuando se abre el detalle de una cuenta, aparecen dos paneles: este es el de
 * la izquierda, que contiene el nombre del cliente, lo que ha consumido y los
 * botones para operar con ello y el de la derecha donde aparecen todos los
 * artículos (bebidas y comidas) pre-establecidas.
 *
 * @author Francisco Morero Peyrona
 */
public final class BillAndProductsPanel extends JPanel
{
    public static final Dimension SUBPANEL_DIMENSION = new Dimension( 510, 680 );
    
    private BillPanel           pnlBill;
    private MasterDetailPanel pnlProducts;

    //------------------------------------------------------------------------//

    public BillAndProductsPanel( Bill bill )
    {
        pnlBill     = new BillPanel( bill );
        pnlProducts = new MasterDetailPanel();

        pnlProducts.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent ae )
            {
                pnlBill.addProduct( (Product) ae.getSource() );
            }
        } );

        // Añadimos todos los componentes a this (el JPanel global)
        setLayout( new BorderLayout( 10, 0 ) );
        setBorder( new EmptyBorder( 9,9,9,9 ) );
        add( pnlBill    , BorderLayout.CENTER );
        add( pnlProducts, BorderLayout.EAST   );
    }

    public void showInDialog()
    {
        JDialog dlg = new JDialog( MainFrame.getInstance() );
                dlg.setModal( true );
                dlg.setTitle( "Modificando cuenta: "+ pnlBill.getCustomerName() );
                dlg.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
                dlg.setContentPane( this );
                dlg.pack();
                dlg.setLocationRelativeTo( MainFrame.getInstance() );
                dlg.setVisible( true );
    }

    public Bill getBill()
    {
        return pnlBill.getBill();
    }
}