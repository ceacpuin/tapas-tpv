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
package com.peyrona.tapas.accountDialog;

import com.peyrona.tapas.mainFrame.MainFrame;
import com.peyrona.tapas.persistence.Article;
import com.peyrona.tapas.persistence.Bill;
import com.peyrona.tapas.utils.ImageHighlightFilter;
import com.peyrona.tapas.utils.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.FilteredImageSource;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class AccountDialog extends JDialog
{
    private Bill bill;

    private BillOwnerPanel      pnlCustomer;
    private PaymentPanel        pnlPayMode;
    private ItemsPanel          pnlItems;
    private ItemEditorPanel     pnlEditItems;
    private NumericPadPanel     pnlNumericPad;
    private TotalDisplayPanel   pnlDisplay;
    private PresetArticlesPanel pnlArticles;

    //------------------------------------------------------------------------//

    public AccountDialog()
    {
        this( null );
    }

    public AccountDialog( Bill bill )
    {
        super( MainFrame.getInstance() );

        if( bill == null )
            bill = new Bill();

        this.bill = bill;
        
        setModal( true );
        setTitle( "Modificando cuenta: "+ bill.getCustomer() );
        setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        setLocationRelativeTo( null );

        initComponents( bill );
    }

    public void execute()
    {
        pack();
        setVisible( true );
    }

    public Bill getBill()
    {
        bill.setCustomer( pnlCustomer.getCustomerName() );
        bill.setPayMode( pnlPayMode.getPayMode() );
        bill.setWhen( Bill.NOW );
        bill.setLines( pnlItems.getBillLines() );

        return bill;
    }

    //------------------------------------------------------------------------//

    private void updateLeftPanelButtons()
    {
        pnlPayMode.setEnabled( pnlItems.getTotal().doubleValue() > 0 );
        pnlEditItems.setEnabled( pnlItems.isRowSelected() );
        pnlDisplay.setAmount( pnlItems.getTotal() );
    }

    private void initComponents( Bill bill )
    {
        pnlCustomer   = new BillOwnerPanel( bill.getCustomer() );
        pnlPayMode    = new PaymentPanel( this );
        pnlItems      = new ItemsPanel( bill.getLines() );
        pnlNumericPad = new NumericPadPanel();
        pnlEditItems  = new ItemEditorPanel();
        pnlDisplay    = new TotalDisplayPanel();
        pnlArticles   = new PresetArticlesPanel();

        pnlPayMode.setEnabled( pnlItems.getTotal().doubleValue() > 0 );
        pnlNumericPad.setEnabled( false );
        pnlEditItems.setEnabled( pnlItems.isRowSelected() );

        pnlArticles.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent ae )
            {
                pnlItems.add( (Article) ae.getSource() );
                AccountDialog.this.updateLeftPanelButtons();
            }
        } );

        JPanel pnlButtonsAndDisplay = new JPanel( new BorderLayout( 30, 0 ) );
               pnlButtonsAndDisplay.setBorder( new EmptyBorder( 20, 0, 10, 0 ) );
               pnlButtonsAndDisplay.add( pnlEditItems, BorderLayout.WEST   );
               pnlButtonsAndDisplay.add( pnlDisplay  , BorderLayout.CENTER );

        JPanel pnlOwnerAndPayments = new JPanel( new BorderLayout() );
               pnlOwnerAndPayments.add( pnlCustomer, BorderLayout.NORTH  );
               pnlOwnerAndPayments.add( pnlPayMode , BorderLayout.CENTER );

        JPanel pnlItemsAndNumericPad = new JPanel( new BorderLayout( 0, 7 ) );
               pnlItemsAndNumericPad.add( pnlItems     , BorderLayout.CENTER );
               pnlItemsAndNumericPad.add( pnlNumericPad, BorderLayout.SOUTH  );

        JPanel pnlBill = new JPanel( new BorderLayout() );
               pnlBill.setBorder( new CompoundBorder( new LineBorder(Color.gray, 2, true ),
                                                      new EmptyBorder( 7,7,7,7 ) ) );
               pnlBill.setMinimumSize( new Dimension( 510, 680 ) );
               pnlBill.setPreferredSize( pnlBill.getMinimumSize() );
               pnlBill.add( pnlOwnerAndPayments  , BorderLayout.NORTH  );
               pnlBill.add( pnlItemsAndNumericPad, BorderLayout.CENTER );
               pnlBill.add( pnlButtonsAndDisplay , BorderLayout.SOUTH  );

        JPanel pnlAll = new JPanel( new BorderLayout( 10, 0 ) );
               pnlAll.setBorder( new EmptyBorder( 9,9,9,9 ) );
               pnlAll.add( pnlBill    , BorderLayout.CENTER );
               pnlAll.add( pnlArticles, BorderLayout.EAST   );

        getContentPane().add( pnlAll );
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private final class ItemEditorPanel extends JPanel
    {
        ItemEditorPanel()
        {
            super( new GridLayout( 1, 4, 4, 0 ) );

            add( new Button4Item( Button4Item.CMD_DEL   ) );
            add( new Button4Item( Button4Item.CMD_EDIT  ) );
            add( new Button4Item( Button4Item.CMD_MINUS ) );
            add( new Button4Item( Button4Item.CMD_PLUS  ) );
        }

        @Override
        public void setEnabled( boolean b )
        {
            super.setEnabled( b );

            for( Component c : getComponents() )
                c.setEnabled( b );
        }
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private final class ItemPriceEditor implements ActionListener
    {
        private StringBuilder sb = new StringBuilder();

        private void startEditing()
        {
            if( AccountDialog.this.pnlItems.isRowSelected() )
            {
                AccountDialog.this.pnlPayMode.setEnabled( false );
                AccountDialog.this.pnlNumericPad.setEnabled( true );
                AccountDialog.this.pnlEditItems.setEnabled( false );
                AccountDialog.this.pnlItems.startEditingPrice();
                AccountDialog.this.pnlItems.updateEditingPrice( "0" );
                AccountDialog.this.pnlNumericPad.addActionListener( this );
            }
        }

        private void stopEditing()
        {
            AccountDialog.this.pnlPayMode.setEnabled( true );
            AccountDialog.this.pnlNumericPad.setEnabled( false );
            AccountDialog.this.pnlEditItems.setEnabled( true );
            AccountDialog.this.pnlItems.stopEditingPrice();
            AccountDialog.this.pnlDisplay.setAmount( pnlItems.getTotal() );
            AccountDialog.this.pnlNumericPad.removeActionListener( this );
        }

        @Override
        public void actionPerformed( ActionEvent ae )
        {
            char cBtn = ae.getActionCommand().charAt( 0 );

            if( cBtn == NumericPadPanel.cENTER )
            {
                stopEditing();
            }
            else
            {
                if( cBtn == Utils.cDecimalSep && notExist() ) sb.append( cBtn );
                else if( cBtn == NumericPadPanel.cCLEAR )     sb.setLength( 0 );
                else                                          sb.append( cBtn );

                AccountDialog.this.pnlItems.updateEditingPrice( sb.toString() );
            }
        }

        private boolean notExist()     // Comprueba que no existe el DecimalSeparator en sb
        {
            return (sb.toString().indexOf( Utils.cDecimalSep ) == -1);
        }
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private final class Button4Item extends JButton implements ActionListener
    {
        final static String CMD_MINUS = "item_minus";
        final static String CMD_PLUS  = "item_plus";
        final static String CMD_EDIT  = "item_edit";
        final static String CMD_DEL   = "item_del";

        Button4Item( String sActionCommand )
        {
            // Con este filtro fabrico el icono de disabled y me ahorro la mitad de los iconos
            ImageHighlightFilter ihf = new ImageHighlightFilter( true, 64 );

            ImageIcon icon  = new ImageIcon( getClass().getResource( "images/"+ sActionCommand +".png" ) );
            Image     image = createImage( new FilteredImageSource( icon.getImage().getSource(), ihf ) );
            ImageIcon dicon = new ImageIcon( image );

            setActionCommand( sActionCommand );
            setIcon( icon );
            setDisabledIcon( dicon );
            setMargin( new Insets( 4, 0, 0, 4 ) );
            setFocusPainted( false );
            addActionListener( Button4Item.this );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            String sCmd = getActionCommand();

                 if( sCmd.equals( CMD_MINUS ) )  AccountDialog.this.pnlItems.decrementQuantity();
            else if( sCmd.equals( CMD_PLUS  ) )  AccountDialog.this.pnlItems.incrementQuantity();
            else if( sCmd.equals( CMD_EDIT  ) )  (new ItemPriceEditor()).startEditing();
            else if( sCmd.equals( CMD_DEL   ) )
            {
                AccountDialog.this.pnlItems.deleteLine();
                AccountDialog.this.updateLeftPanelButtons();
            }
        }
    }
}