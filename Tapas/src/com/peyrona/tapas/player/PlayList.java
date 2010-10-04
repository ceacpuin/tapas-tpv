/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.peyrona.tapas.player;

import java.awt.Component;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Francisco Morero Peyrona
 */
public final class PlayList extends JTable
{
    PlayList()
    {
        this( null );
    }

    PlayList( String sBaseFolder )
    {
        setFillsViewportHeight( true );
        setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        setShowGrid( false );
        setRowHeight( 16 );
        setModel( new DataModel() );
        getColumnModel().getColumn( 0 ).setCellRenderer( new CellRenderer() );

        if( sBaseFolder != null )
            loadPlayList( new File( sBaseFolder ) );
    }

    //------------------------------------------------------------------------//

    public void loadPlayList( File fBaseFolder )
    {
        if( fBaseFolder != null && fBaseFolder.isDirectory() )    // isDirectory() comprueba que exista
        {
            SwingWorker sw = getSwingWorkerToLoadPlayList( fBaseFolder );
                        sw.execute();
        }
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private SwingWorker getSwingWorkerToLoadPlayList( final File fBaseFolder )
    {
        return new SwingWorker<Void, Void>()
        {
            private String[] asExt = { "WAV", "MP3", "OGG" };

            @Override
            public Void doInBackground()
            {
                ((DataModel) PlayList.this.getModel()).deleteAllRows();
                loadFiles( fBaseFolder );
                return null;
            }

            @Override
            public void done()
            {
                if( ! ((DataModel) PlayList.this.getModel()).isEmpty() )
                {
                    Rectangle rect = getCellRect( 0, 0, true );

                    PlayList.this.setRowSelectionInterval( 0, 0 );
                    ((JViewport) PlayList.this.getParent()).scrollRectToVisible( rect );
                }
            }

            private void loadFiles( File folder )
            {
                File[] filesAndDirs = folder.listFiles();

                for( File file : filesAndDirs )
                {
                    if ( file.isDirectory() )
                        loadFiles( file );   // Llamada recursiva
                    else if( isMusic( file ) )
                        ((DataModel) PlayList.this.getModel()).addRow( file );
                }
            }

            private boolean isMusic( File file )
            {
                String sExt = file.getName().toUpperCase();
                       sExt = sExt.substring( sExt.lastIndexOf( '.' ) + 1 ) ;

                for( String s : asExt )
                {
                    if( s.equals( sExt ) )
                        return true;
                }

                return false;
            }
        };
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private static final class DataModel extends AbstractTableModel
    {
        private List<File> Songs = new ArrayList<File>();

        public int getColumnCount()
        {
            return 1;
        }

        public String getColumnName( int nCol )
        {
            return "Canciones";
        }

        public int getRowCount()
        {
            return Songs.size();
        }

        public Object getValueAt( int nRow, int nCol )
        {
            return Songs.get( nRow );
        }

        public void setValueAt( Object value, int nRow, int nCol )
        {
            Songs.set( nRow, (File) value );
        }

        public boolean isCellEditable( int nRow, int nCol )
        {
            return false;
        }

        private void addRow( File song )
        {
            Songs.add( song );
            fireTableRowsInserted( Songs.size() - 1, Songs.size() - 1 );
        }

        private void deleteAllRows()
        {
            Songs.clear();
            fireTableDataChanged();
        }

        private boolean isEmpty()
        {
            return Songs.isEmpty();
        }
    }

    //------------------------------------------------------------------------//
    // Inner Class
    //------------------------------------------------------------------------//
    private static final class CellRenderer extends JLabel implements TableCellRenderer
    {
        private CellRenderer()
        {
            setOpaque( true );
            setFont( getFont().deriveFont( 14f ) );
        }

        @Override
        public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
        {
            File song = (File) value;

            setBackground( isSelected ? table.getSelectionBackground() : table.getBackground() );
            setForeground( isSelected ? table.getSelectionForeground() : table.getForeground() );
            setText( song.getName() );

            return this;
        }
    }
}