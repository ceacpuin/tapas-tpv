package com.peyrona.tapas.persistence;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class TestBlobs
{
    private Connection dbConn;

    public static void main( String[] args )
    {
        try
        {
            TestBlobs t = new TestBlobs();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public TestBlobs() throws ClassNotFoundException, SQLException, IOException
    {
        String    sAppDir  = System.getProperty( "user.dir", "." );
        String    sDbPath  = sAppDir + "/db";
        ImageIcon icon2DB  = new ImageIcon( getClass().getResource( "../logo.png" ) );

        JFrame frame2DB = new JFrame("To DB");
               frame2DB.getContentPane().setLayout( new BorderLayout() );
               frame2DB.add( new JButton( icon2DB ), BorderLayout.CENTER );
               frame2DB.pack();
               frame2DB.setVisible(true);

        System.setProperty( "derby.system.home", sDbPath );
        Class.forName( "org.apache.derby.jdbc.EmbeddedDriver" );
        dbConn = DriverManager.getConnection( "jdbc:derby:tapas;user=admin;password=admin;create=true" );

        dbConn.createStatement().executeUpdate( "DELETE FROM App.categorias WHERE nombre='Categoría'" );

        PreparedStatement psCategories = dbConn.prepareStatement(
                "INSERT INTO App.categorias (nombre, icono) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS );

        // Se graba en la DB
        Blob blobIcon = iconToBlob( icon2DB );

        psCategories.setString( 1, "Categoría" );
        psCategories.setBlob( 2, blobIcon );
        psCategories.executeUpdate();
        psCategories.close();

        blobIcon.free();

        // Se lee de la DB
        ImageIcon iconFromDB = null;

        ResultSet rs = dbConn.createStatement().executeQuery(
                                      "SELECT icono FROM App.Categorias WHERE nombre='Categoría'" );
        if( rs.next() )
            iconFromDB = iconFromBlob( rs.getBlob( "icono" ) );

        // Se pinta en pantalla
        JFrame frameFromDB = new JFrame("From DB");
               frameFromDB.getContentPane().setLayout( new BorderLayout() );
               frameFromDB.add( new JButton( iconFromDB ), BorderLayout.CENTER );
               frameFromDB.pack();
               frameFromDB.setVisible(true);

       psCategories.close();
       rs.close();
       dbConn.close();
    }

    private Blob imageToBlob( Image image ) throws SQLException
    {
        if( image == null )
            return iconToBlob( null );

        return iconToBlob( new ImageIcon( image) );
    }

    private Blob iconToBlob( ImageIcon icon ) throws SQLException
    {
        Blob blob = null;

        if( icon != null )
        {
            blob = dbConn.createBlob();

            try
            {
                ObjectOutputStream oos;
                                   oos = new ObjectOutputStream( blob.setBinaryStream( 1 ) );
                                   oos.writeObject( icon );
                                   oos.close();
            }
            catch( Exception exc )
            {
                if( exc instanceof SQLException )
                    throw (SQLException) exc;
                else
                    throw new SQLException( exc.getMessage() );
            }
        }

        return blob;
    }

    private Image imageFromBlob( Blob blob ) throws SQLException
    {
        ImageIcon icon = iconFromBlob( blob );

        return (icon == null ? null : icon.getImage());
    }

    private ImageIcon iconFromBlob( Blob blob ) throws SQLException
    {
        ImageIcon icon = null;

        if( blob != null )
        {
            if( blob.length() > 0 )
            {
                try
                {
                    ObjectInputStream ois = new ObjectInputStream( blob.getBinaryStream() );

                    icon = (ImageIcon) ois.readObject();
                    ois.close();
                }
                catch( Exception exc )
                {
                    if( exc instanceof SQLException )
                        throw (SQLException) exc;
                    else
                        throw new SQLException( exc.getMessage() );
                }
            }

            blob.free();
        }

        return icon;
    }
}