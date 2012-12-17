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

package com.peyrona.tapas;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class Utils
{
    private static NumberFormat nfCurrency  = NumberFormat.getCurrencyInstance();
    private static NumberFormat nfNoSymbol = NumberFormat.getInstance();

    public static final int nEXIT_NO_EXIT   = -1;
    public static final int nEXIT_NO_ERROR =  0;
    public static final int nEXIT_LAF_ERROR =  1;
    public static final int nEXIT_DB_ERROR  =  2;

    public static final char   cDecimalSep     = (new DecimalFormatSymbols()).getMonetaryDecimalSeparator();
    public static final String sCurrencySymbol = (new DecimalFormatSymbols()).getCurrencySymbol();

    public static final boolean DEBUGGING = true;   // ¿Estamos en modo develop o debug?    // FIXME: Apagar el flag de debug

    //------------------------------------------------------------------------//

    static
    {
        int nDigits = Currency.getInstance( Locale.getDefault() ).getDefaultFractionDigits();

        Utils.nfNoSymbol.setMinimumFractionDigits( nDigits );
        Utils.nfNoSymbol.setMaximumFractionDigits( nDigits );
    }

    //------------------------------------------------------------------------//

    /**
     * Formatea cantidades como moneda utilizando el default locale.
     *
     * @param nAmount
     * @return
     */
    public static String formatAsCurrency( BigDecimal nAmount )
    {
        return nfCurrency.format( nAmount );
    }

    /**
     * Funciona como formatAsCurrency(...) pero no incluye el símbolo de la moneda.
     *
     * @param nAmount
     * @return
     */
    public static String formatLikeCurrency( BigDecimal nAmount )
    {
        return nfNoSymbol.format( nAmount );
    }

    public static boolean isEmpty( String s )
    {
        return (s == null || s.trim().length() == 0);
    }

    public static String setMaxLength( String s, int len )
    {
        if( s != null && len >= 0 )
        {
            if( s.length() > len )
                s = s.substring( 0, len );
        }

        return s;
    }

    public static void printError( Throwable th )
    {
        printError( th, Level.WARNING, null, nEXIT_NO_EXIT );
    }

    public static void printError( Throwable th, Level level, String sMessage, int nExitCode )
    {
        if( sMessage != null )
            System.out.println( sMessage );

        if( th != null )
        {
            Logger logger = Logger.getLogger( "Tapas.Logger" );

            try
            {
                FileHandler handler = new FileHandler( "Tapas.log", true );   // true == append en el fichero
                logger.addHandler( handler );// FIXME: esto añadiría un handler cada vez que se llama a printError: corregirlo
            }
            catch( IOException ioe )
            {
                // Nada que hacer
            }

            logger.log( Level.SEVERE, sMessage, th );
            th.printStackTrace( System.err );
        }

        if( nExitCode != nEXIT_NO_EXIT )
            System.exit( nExitCode );
    }

    //------------------------------------------------------------------------//

    public static Image readImageFromBlob( ResultSet rs, String column ) throws SQLException
    {
        byte[] binImage = Utils.readFromBlob( rs, column );

        return ((binImage == null) ? null : new ImageIcon( binImage ).getImage());
    }

    public static void writeImageToBlob( int column, PreparedStatement psmt, Image image ) throws SQLException, IOException
    {
        ImageIcon icon = null;

        if( image != null )
        {
            icon = new ImageIcon( image );
        }

        writeToBlob( column, psmt, imageToBytes( icon ) );
    }

    public static byte[] readFromBlob( ResultSet rs, String column ) throws SQLException
    {
        byte[] ret  = null;
        Blob   blob = rs.getBlob( column );

        if( blob != null )
        {
            long length = blob.length();

            if( length == 0 )    // Cuando (length == 0) => returns byte[0] en lugar de null
            {
                ret = new byte[0];
            }
            else
            {
                ret = blob.getBytes( 1, (int) length );
            }

            blob.free();
        }

        return ret;
    }

    public static void writeToBlob( int index, PreparedStatement ps, byte[] data ) throws SQLException
    {
        ByteArrayInputStream bais = null;

        if( data != null )
        {
            bais = new ByteArrayInputStream( data );
        }

        ps.setBinaryStream( index, bais );
    }

    public static byte[] imageToBytes( ImageIcon icon ) throws IOException
    {
        if( icon == null )
        {
            return null;
        }

        BufferedImage         image = getBufferedImageFromImage( icon.getImage() );
        ByteArrayOutputStream baos  = new ByteArrayOutputStream();

        ImageIO.write( image, "png", baos );

        return baos.toByteArray();
    }

    private static BufferedImage getBufferedImageFromImage( Image img )
    {
        // Crea un objeto BufferedImage con el ancho y alto de la Image
        BufferedImage bufferedImage = new BufferedImage( img.getWidth( null ), img.getHeight( null ),
                                                         BufferedImage.TYPE_INT_RGB );
        Graphics g = bufferedImage.createGraphics();
                 g.drawImage( img, 0, 0, null );
                 g.dispose();

        return bufferedImage;
    }
}