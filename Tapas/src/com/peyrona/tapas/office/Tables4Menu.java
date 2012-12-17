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

package com.peyrona.tapas.office;

import com.peyrona.tapas.Utils;
import com.peyrona.tapas.persistence.Article;
import com.peyrona.tapas.swing.SwingUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Esta es simplemente una clase instrumental, un repositorio de clases estáticas
 * que son utilizadas desde la clase Menu.
 * <p>
 * Las he puesto en un fichero separado para poder afinar más con la encapsulación:
 * si las clases de este fichero estuvieran dentro del fichero Menu.java, entonces
 * todos los métodos y datos de estas clases serían accesibles desde la clase Menu,
 * aunque fuesen private, lo que no ocurre al estar en otro fichero.
 * <p>
 * Son static porque de este modo funcionan casi como si cada una estuviera en un
 * fichero diferente (ya que no hay que instanciar Tables4Menu para poder crear
 * instancias de estas clases).
 *
 * @author Francisco Morero Peyrona
 */
final class Tables4Menu
{
    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    /**
     * De esta clase heredan TableCategories y TableProducts, pero como esta clase
     * no está pensada para ser utilizada por sí misma, la hacemos private.
     * <p>
     * Podría conseguirs lo mismo haciéndola anstract, pero de este modo encaja
     * mejor con la teoría de la POO.
     */
    private static class JCommonTable extends JTable
    {
        private JCommonTable()
        {
            setFillsViewportHeight( true );
            setMinimumSize( new Dimension( 100, 160 ) );
            setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
            setShowGrid( false );
            setIntercellSpacing( new Dimension( 2, 2 ) );
        }

        void addRow( Article article )
        {
            ((DataModel) getModel()).addRow( article );
            getSelectionModel().setSelectionInterval( getRowCount() - 1, getRowCount() - 1 );
            makeHighlightedRowVisible();
        }

        void deleteHighlightedRow()
        {
            int nRow = getSelectedRow();

            if( nRow > -1 )
            {
                ((DataModel) getModel()).deleteRow( getSelectedRow() );

                if( getRowCount() > 0 )
                {
                    nRow = (nRow == 0 ? 0 : nRow -1 );
                    getSelectionModel().setSelectionInterval( nRow, nRow );
                    makeHighlightedRowVisible();
                }
            }
        }

        void shiftUpHighlightedRow()
        {
            shiftHighlightedRow( -1 );
        }

        void shiftDownHighlightedRow()
        {
            shiftHighlightedRow( 1 );
        }

        List<Article> getData()
        {
            return ((DataModel) getModel()).getData();
        }

        void setData( List<Article> data )
        {
            ((DataModel) getModel()).setData( data );
            
            if( data.size() > 0 )
                getSelectionModel().setSelectionInterval( 0, 0 );
        }

        private void shiftHighlightedRow( int nDirection )
        {
            int nSelect = getSelectedRow();
            int nNewRow = ((DataModel) getModel()).shiftRow( nSelect, nDirection );

            if( nNewRow > -1 )
            {
                getSelectionModel().setSelectionInterval( nNewRow, nNewRow );
                makeHighlightedRowVisible();
            }
        }

        private void makeHighlightedRowVisible()
        {
            Rectangle rect = getCellRect( getSelectedRow(), 0, true );
            ((JViewport) getParent()).scrollRectToVisible( rect );
        }
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    static final class TableCategories extends JCommonTable
    {
        private final static int nCOL_CAPTION = 0;
        private final static int nCOL_ICON    = 1;

        TableCategories()
        {
            setPreferredSize( new Dimension( 140, 190 ) );
            setMaximumSize( getPreferredSize() );
            setRowHeight( 64 );

            setModel( new DataModel( new String[] { "Nombre", "Icono" }  ) );
            getColumnModel().getColumn( nCOL_CAPTION ).setPreferredWidth( 64 );
            getColumnModel().getColumn( nCOL_ICON    ).setPreferredWidth( 64 );
            getColumnModel().getColumn( nCOL_ICON    ).setCellRenderer( new CellRenderer4Icon() );
            getColumnModel().getColumn( nCOL_ICON    ).setCellEditor( new CellEditor4Icon() );
        }
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    static final class TableProducts extends JCommonTable
    {
        private final static int nCOL_CAPTION     = 0;
        private final static int nCOL_DESCRIPTION = 1;
        private final static int nCOL_PRICE       = 2;
        private final static int nCOL_ICON        = 3;

        TableProducts()
        {
            setPreferredSize( new Dimension( 220, 270 ) );
            setMaximumSize( getPreferredSize() );
            setRowHeight( 64 );
            setModel( new DataModel( new String[] { "Nombre", "Descripción", "Precio", "Icono" }  ) );

            getColumnModel().getColumn( nCOL_CAPTION     ).setPreferredWidth( 48 );
            getColumnModel().getColumn( nCOL_DESCRIPTION ).setPreferredWidth( 96 );
            getColumnModel().getColumn( nCOL_PRICE       ).setPreferredWidth( 54 );
            getColumnModel().getColumn( nCOL_ICON        ).setPreferredWidth( 64 );
            getColumnModel().getColumn( nCOL_ICON        ).setCellRenderer( new CellRenderer4Icon() );
            getColumnModel().getColumn( nCOL_ICON        ).setCellEditor( new CellEditor4Icon() );
        }
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private static final class DataModel extends AbstractTableModel
    {
        private List<Article> vLines     = new ArrayList<Article>();
        private String[]      asColNames = null;

        private DataModel( String[] asColNames )
        {
            this.asColNames = asColNames;
        }

        @Override
        public int getColumnCount()
        {
            return asColNames.length;
        }

        @Override
        public String getColumnName( int nCol )
        {
            return asColNames[nCol];
        }

        @Override
        public int getRowCount()
        {
            return vLines.size();
        }

        @Override
        public Object getValueAt( int nRow, int nCol )
        {
            Article article = vLines.get( nRow );
            Object  ret     = null;

            if( asColNames.length == 2 )  // Categories
            {
                switch( nCol )
                {
                    case TableCategories.nCOL_CAPTION: ret = article.getCaption(); break;
                    case TableCategories.nCOL_ICON   : ret = article.getIcon();    break;
                }
            }
            else                         // Products
            {
                switch( nCol )
                {
                    case TableProducts.nCOL_CAPTION    : ret = article.getCaption();                        break;
                    case TableProducts.nCOL_DESCRIPTION: ret = article.getDescription();                     break;
                    case TableProducts.nCOL_PRICE      : ret = Utils.formatLikeCurrency( article.getPrice() );  break;
                    case TableProducts.nCOL_ICON       : ret = article.getIcon();                            break;
                }
            }

            return ret;
        }

        @Override
        public void setValueAt( Object value, int nRow, int nCol )
        {
            Article article = vLines.get( nRow );

            if( asColNames.length == 2 )  // Categories
            {
                switch( nCol )
                {
                    case TableCategories.nCOL_CAPTION: article.setCaption( (String)    value); break;
                    case TableCategories.nCOL_ICON   : article.setIcon(    (ImageIcon) value); break;
                }
            }
            else                         // Products
            {
                switch( nCol )
                {
                    case TableProducts.nCOL_CAPTION    : article.setCaption(     (String)    value); break;
                    case TableProducts.nCOL_DESCRIPTION: article.setDescription( (String)    value); break;
                    case TableProducts.nCOL_PRICE      : article.setPrice(       Str2Price(value) ); break;
                    case TableProducts.nCOL_ICON       : article.setIcon(        (ImageIcon) value); break;
                }
            }
        }

        @Override
        public boolean isCellEditable( int nRow, int nCol )
        {
            return true;
        }

        private List<Article> getData()
        {
            return vLines;
        }

        private void setData( List<Article> data )
        {
            vLines = data;
            fireTableDataChanged();
        }

        private void addRow( Article article )
        {
            vLines.add( article );
            fireTableRowsInserted( vLines.size() - 1, vLines.size() - 1 );
        }

        private void deleteRow( int nRow )
        {
            if( nRow > -1 )
            {
                vLines.remove( nRow );
                fireTableRowsDeleted( nRow, nRow );
            }
        }

        private int shiftRow( int nRow, int nDirection )
        {
            int NewRow = -1;

            if( (nRow > -1 && nRow < getRowCount()) &&           // nRow es un valor válido
                ((nDirection == 1 && nRow < getRowCount() - 1)   // Quiere bajar y no es ya el último
                ||
                 (nDirection == -1 && nRow > 0)) )               // Quiere subir y no es ya el primero
            {
                NewRow = (nDirection == 1 ? nRow+1: nRow-1 );
                vLines.add( NewRow, vLines.remove( nRow ) );
                fireTableDataChanged();
            }

            return NewRow;
        }

        // Esto no es alta tecnología, pero si el usuario es sensato funciona
        private BigDecimal Str2Price( Object value )
        {
            String sPrice = (String) value;
                   sPrice = sPrice.replace( Utils.cDecimalSep, '.' );

            return new BigDecimal( sPrice );
        }
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private static final class CellRenderer4Icon extends JLabel implements TableCellRenderer
    {
        private Border selected;
        private Border unselected;

        private CellRenderer4Icon()
        {
            selected   = new LineBorder( (new JTable()).getSelectionBackground(), 2 );
            unselected = new EmptyBorder( 2,2,2,2 );
        }

        @Override
        public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
        {
            setBorder( (isSelected ? selected : unselected) );
            setIcon( (ImageIcon) value );
            return this;
        }
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private static final class CellEditor4Icon extends AbstractCellEditor implements TableCellEditor, MouseListener
    {
        private ImageIcon icon;
        private JLabel    label;

        private CellEditor4Icon()
        {
            label = new JLabel();
            label.addMouseListener( CellEditor4Icon.this );
        }

        @Override
        public Object getCellEditorValue()
        {
            return icon;
        }

        @Override
        public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int column )
        {
            icon = (ImageIcon) value;
            label.setIcon( icon );

            return label;
        }

        @Override
        public void mouseClicked( MouseEvent me )
        {
            BufferedImage bimage = SwingUtils.ImageChooser();

            if( bimage != null )
                icon = new ImageIcon( bimage );

            label.setIcon( icon );
            fireEditingStopped();            // Para que el renderer haga su trabajo
        }

        @Override
        public void mousePressed(  MouseEvent e )  { }
        @Override
        public void mouseReleased( MouseEvent e )  { }
        @Override
        public void mouseEntered(  MouseEvent e )  { }
        @Override
        public void mouseExited(   MouseEvent e )  { }
    }
}