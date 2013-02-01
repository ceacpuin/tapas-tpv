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

package com.peyrona.tapas.account.bill;

import com.peyrona.tapas.Utils;
import com.peyrona.tapas.persistence.Product;
import com.peyrona.tapas.persistence.BillLine;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Este es el panel que contiene todos los artículos (bedidas y comidas) que se
 * han consumido en una cuenta.
 *
 * @author Francisco Morero Peyrona
 */
final class ItemsPanel extends JScrollPane
{
    private final static int nCOL_QUANTITY = 0;
    private final static int nCOL_ITEM     = 1;
    private final static int nCOL_PRICE    = 2;
    private final static int nCOL_AMOUNT   = 3;
    private final static int nCOL_COUNT    = 4;    // Number of columns

    private JTable tblPaper = new JTable();

    private int nRowEditing = -1;     // La fila que se está editando

    //------------------------------------------------------------------------//

    ItemsPanel( List<BillLine> lstBillLines )
    {
        initComponents();

        for( BillLine line : lstBillLines )
        {
            addLine( line.getQuantity(), line.getItem(), line.getPrice() );
        }
    }

    void add( Product product )
    {
        addLine( 1, product.getDescription(), product.getPrice() );
    }

    void addLine( int nQuantity, String sItem, BigDecimal nPrice )
    {
        ((DataModel) tblPaper.getModel()).addLine( nQuantity, sItem, nPrice );

        tblPaper.getSelectionModel().setSelectionInterval( tblPaper.getRowCount() - 1, tblPaper.getRowCount() - 1 );

        // Hace la fila seleccionada visible
        Rectangle rect = tblPaper.getCellRect( tblPaper.getRowCount() - 1, 0, true );
        getViewport().scrollRectToVisible( rect );

        // Alínea horizontalmente a la derecha (no puede hacerse hasta que haya datos)
        if( ((DataModel) tblPaper.getModel()).getRowCount() == 1 )
        {
            setDefaultRenderers();
        }
    }

    void deleteLine()
    {
        int nRow = tblPaper.getSelectedRow();

        if( nRow > -1 )
        {
            ((DataModel) tblPaper.getModel()).deleteSelectedLine();

            if( tblPaper.getRowCount() > 0 )
            {
                nRow = (nRow == 0 ? 0 : nRow -1 );
                tblPaper.getSelectionModel().setSelectionInterval( nRow, nRow );
            }
        }
    }

    void startEditingPrice()
    {
        nRowEditing = tblPaper.getSelectedRow();

        TableColumn column = tblPaper.getColumnModel().getColumn( nCOL_PRICE );
                    column.setCellRenderer( new CellRendererToEditPrice() );

        // Ponemos el evento en la cola para que se procese primero el mostrado del teclado numérico
        EventQueue.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                tblPaper.scrollRectToVisible( tblPaper.getCellRect( nRowEditing, nCOL_PRICE, true ) );
            }
        } );
    }

    void updateEditingPrice( String sNewPrice )
    {
        if( Utils.isEmpty( sNewPrice ) )
        {
            sNewPrice = "0";
        }

        if( sNewPrice.indexOf( Utils.cDecimalSep ) > -1 )
        {
            sNewPrice = sNewPrice.replace( Utils.cDecimalSep, '.' );
        }

        try
        {
            BigDecimal bd = new BigDecimal( sNewPrice );
            ((DataModel) tblPaper.getModel()).setPriceInRow( bd, nRowEditing );
        }
        catch( NumberFormatException nfe )
        {
            // Nada que hacer
        }
    }

    void stopEditingPrice()
    {
        nRowEditing = -1;      // No es necesario pero es elgante
        setDefaultRenderers();
    }

    void incrementQuantity()
    {
        ((DataModel) tblPaper.getModel()).changeQuantityInSelectedLine( 1 );
    }

    void decrementQuantity()
    {
        ((DataModel) tblPaper.getModel()).changeQuantityInSelectedLine( -1 );
    }

    List<BillLine> getBillLines()
    {
        return ((DataModel) tblPaper.getModel()).vLines;
    }

    BigDecimal getTotal()
    {
        BigDecimal nTotal = new BigDecimal( 0 );

        for( BillLine line : getBillLines() )
        {
            BigDecimal nQuantity = new BigDecimal( line.getQuantity() );
            nTotal = nTotal.add( line.getPrice().multiply( nQuantity ) );
        }

        return nTotal;
    }

    boolean isRowSelected()
    {
        return tblPaper.getSelectedRow() > -1;
    }

    //------------------------------------------------------------------------//

    private void setDefaultRenderers()
    {
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
                                 cr.setHorizontalAlignment( JLabel.RIGHT );

        tblPaper.getColumnModel().getColumn( nCOL_QUANTITY ).setCellRenderer( cr );
        tblPaper.getColumnModel().getColumn( nCOL_PRICE    ).setCellRenderer( cr );
        tblPaper.getColumnModel().getColumn( nCOL_AMOUNT   ).setCellRenderer( cr );

        tblPaper.repaint();
    }

    private void initComponents()
    {
        tblPaper.setModel( new DataModel() );
        tblPaper.setTableHeader( null );
        tblPaper.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        tblPaper.setBackground( new Color( 255, 255, 235 ) );
        tblPaper.setFont( getFont().deriveFont( Font.PLAIN, 14f ) );
        tblPaper.setFillsViewportHeight( true );
        tblPaper.setIntercellSpacing( new Dimension( 2,2 ) );
        tblPaper.setRowHeight( 32 );

        tblPaper.getColumnModel().getColumn( nCOL_QUANTITY ).setPreferredWidth(  32 );
        tblPaper.getColumnModel().getColumn( nCOL_ITEM     ).setPreferredWidth( 170 );
        tblPaper.getColumnModel().getColumn( nCOL_PRICE    ).setPreferredWidth(  60 );
        tblPaper.getColumnModel().getColumn( nCOL_AMOUNT   ).setPreferredWidth(  72 );

        tblPaper.getColumnModel().getColumn( nCOL_QUANTITY ).setMaxWidth(  36 );
        tblPaper.getColumnModel().getColumn( nCOL_ITEM     ).setMaxWidth( 999 );
        tblPaper.getColumnModel().getColumn( nCOL_PRICE    ).setMaxWidth(  64 );
        tblPaper.getColumnModel().getColumn( nCOL_AMOUNT   ).setMaxWidth(  76 );

        getViewport().setBackground( tblPaper.getBackground() );
        getViewport().add( tblPaper );

        setMinimumSize( new Dimension( 160, 110 ) );
        setPreferredSize( new Dimension( 480, 480 ) );
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private final class CellRendererToEditPrice implements TableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected,
                                                        boolean hasFocus, int row, int column )
        {
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                                     renderer.setHorizontalAlignment( JLabel.RIGHT );
                                     renderer = (DefaultTableCellRenderer)
                                                 renderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );

            if( isSelected && column == nCOL_PRICE )
            {
                renderer.setBackground( Color.cyan.brighter() );
                renderer.setForeground( Color.black );
                renderer.setFont( renderer.getFont().deriveFont( Font.BOLD ) );
            }

            return renderer;
        }
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private final class DataModel extends AbstractTableModel
    {
        private List<BillLine> vLines = new ArrayList<BillLine>();

        @Override
        public int getColumnCount()
        {
            return nCOL_COUNT;
        }

        @Override
        public int getRowCount()
        {
            return vLines.size();
        }

        @Override
        public Object getValueAt( int nRow, int nCol )
        {
            Object ret = null;

            switch( nCol )
            {
                case nCOL_QUANTITY: ret = vLines.get( nRow ).getQuantity();                           break;
                case nCOL_ITEM    : ret = vLines.get( nRow ).getItem();                               break;
                case nCOL_PRICE   : ret = Utils.formatLikeCurrency( vLines.get( nRow ).getPrice() );  break;
                case nCOL_AMOUNT  : BillLine line = vLines.get( nRow );
                                    BigDecimal nAmount = line.getPrice().multiply( new BigDecimal( line.getQuantity() ) );
                                    ret = Utils.formatLikeCurrency( nAmount );                        break;
            }

            return ret;
        }

        @Override
        public boolean isCellEditable( int nRow, int nCol )
        {
            return false;
        }

        private void addLine( int nQuantity, String sItem, BigDecimal nPrice )
        {
            vLines.add( new BillLine( nQuantity, sItem, nPrice ) );
            fireTableRowsInserted( vLines.size() - 1, vLines.size() - 1 );
        }

        private void deleteSelectedLine()
        {
            int nRow = tblPaper.getSelectedRow();

            if( nRow > -1 )
            {
                vLines.remove( nRow );
                fireTableRowsDeleted( nRow, nRow );
            }
        }

        private void changeQuantityInSelectedLine( int nQuantity )
        {
            int nRow = tblPaper.getSelectedRow();

            if( nRow > -1 )
            {
                BillLine line = vLines.get( nRow );

                if( nQuantity > 0 || line.getQuantity() > 1 )
                {
                    line.setQuantity( line.getQuantity() + nQuantity );
                    fireTableRowsUpdated( nRow, nRow );
                }
            }
        }

        private void setPriceInRow( BigDecimal nPrice, int nRow )
        {
            BillLine line = vLines.get( nRow );
                     line.setPrice( nPrice );
            fireTableRowsUpdated( nRow, nRow );
        }
    }
}