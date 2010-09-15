/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.peyrona.tapas.persistence;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class TestStreams
{
    public static void main( String[] args )
    {
        try
        {
            TestStreams t = new TestStreams();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public TestStreams() throws ClassNotFoundException, SQLException, IOException
    {
        String    sAppDir  = System.getProperty( "user.dir", "." );
        String    sDbPath  = sAppDir + "/db";
        ImageIcon icon2DB  = new ImageIcon( getClass().getResource( "../logo.png" ) );

        JFrame frame2DB = new JFrame("To DB");
               frame2DB.getContentPane().setLayout( new BorderLayout() );
               frame2DB.add( new JButton( icon2DB ), BorderLayout.CENTER );
               frame2DB.pack();
               frame2DB.setVisible(true);

//        System.setProperty( "derby.system.home", sDbPath );
//        Class.forName( "org.apache.derby.jdbc.EmbeddedDriver" );
//        Connection dbConn = DriverManager.getConnection( "jdbc:derby:tapas;user=admin;password=admin;create=true" );
//
//        Statement stmt = dbConn.createStatement();
//                  stmt.executeUpdate( "DELETE FROM App.categorias WHERE nombre='Categoría'" );
//                  stmt.close();
//
//        PreparedStatement psCategories = dbConn.prepareStatement(
//                "INSERT INTO App.categorias (nombre, icono) VALUES (?, ?)",
//                Statement.RETURN_GENERATED_KEYS );
//
//        // Se graba en la DB
//        psCategories.setString( 1, "Categoría" );
//        psCategories.setBinaryStream( 2, iconToStream( icon2DB ) );
//        psCategories.executeUpdate();
//        psCategories.close();

        // Se lee de la DB
        ImageIcon iconFromDB = null;

//        ResultSet rs = dbConn.createStatement().executeQuery(
//                                      "SELECT icono FROM App.Categorias WHERE nombre='Categoría'" );
//        if( rs.next() )
//            iconFromDB = streamToIcon( rs.getBinaryStream( "icono" ) );

        iconFromDB = streamToIcon( iconToStream( icon2DB ) );

        // Se pinta en pantalla
        JFrame frameFromDB = new JFrame("From DB");
               frameFromDB.getContentPane().setLayout( new BorderLayout() );
               frameFromDB.add( new JButton( iconFromDB ), BorderLayout.CENTER );
               frameFromDB.pack();
               frameFromDB.setVisible(true);

//       dbConn.close();
    }

    private InputStream iconToStream( ImageIcon icon )
    {
        BufferedInputStream is = new BufferedInputStream(
                                     new ByteArrayInputStream(
                                         imageToByteArray( icon.getImage() ) ) );

        return is;
    }

    private ImageIcon streamToIcon( InputStream is )
    {
        ImageIcon           icon = null;
        BufferedInputStream bis  = new BufferedInputStream( is );

        try
        {
            icon = new ImageIcon( ImageIO.read( bis ) );
        }
        catch( IOException ex )
        {
            Logger.getLogger( TestStreams.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return icon;
    }

    public static byte[] imageToByteArray( Image image )
    {
        byte[] abRet = null;

        if( image != null )
        {
            BufferedImage bi = new BufferedImage( image.getWidth( null ),
                                                  image.getHeight( null ),
                                                  BufferedImage.TYPE_INT_RGB );

            abRet = bufferedImageToByteArray( bi );
        }

        return abRet;
    }

    public static byte[] bufferedImageToByteArray( BufferedImage bimage )
    {
        byte[] abRet = null;

        if( bimage != null )
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try
            {
                ImageIO.write( bimage, "png", baos );
                abRet = baos.toByteArray();
            }
            catch( IOException ie )
            {
                //printError( ie, Level.WARNING, "No se ha podido procesar la imagen", nEXIT_NO_EXIT );
            }
        }

        return abRet;
    }
}
