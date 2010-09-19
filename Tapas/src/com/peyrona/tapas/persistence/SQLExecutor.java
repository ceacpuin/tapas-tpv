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

package com.peyrona.tapas.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Procesa los comandos SQL almacenados en un fichero.
 * <p>
 * Los comandos tienen que terminar con ';'.<br>
 * Los comentarios ("--") y las líneas en blanco son ignoradas.
 *
 * @author Francisco Morero Peyrona
 */
public class SQLExecutor
{
    private final static String sSQL_Comment     = "--";
    private final static String sSQL_Termination =  ";";

    private Connection     dbConn;
    private BufferedReader reader;

    SQLExecutor( Connection conn, File file )
    {
        dbConn = conn;

        try
        {
            reader = new BufferedReader( new FileReader( file ) );
        }
        catch( FileNotFoundException ex )
        {
            reader = null;
        }
    }

    void extecute()  throws FileNotFoundException, SQLException, IOException
    {
        if( reader == null )
            throw new FileNotFoundException();

        Statement      stmt  = null;
        String         sLine = null;
        StringBuilder  sb    = new StringBuilder( 1024 );

        try
        {
            stmt = dbConn.createStatement();

            while( (sLine = reader.readLine()) != null )
            {
                if( isSQL( sLine ) )
                {
                    sb.append( sLine );

                    if( sLine.endsWith( sSQL_Termination ) )
                    {
                        sb.deleteCharAt( sb.length() - 1 );    // Borra la terminación (";")
                        stmt.execute( sb.toString() );
                        sb.setLength( 0 );
                    }
                }
            }
        }
        catch( SQLException exc )
        {
            throw exc;
        }
        catch( IOException exc )
        {
            throw exc;
        }
        finally
        {
            if( reader != null )
                try{ reader.close(); } catch( IOException se ) { /* Nothing to do */ }

            if( stmt != null )
                try{ stmt.close(); } catch( SQLException se ) { /* Nothing to do */ }
        }

        ///añadeAlgunosArtículosParaPoderHacerPruebas(); // FIXME QUITARLO
    }

    boolean isSQL( String s )
    {
        String sLine = s.trim();

        return (sLine.length() > 0) &&
               (! sLine.startsWith( sSQL_Comment ));
    }

    private static void añadeAlgunosArtículosParaPoderHacerPruebas()
    {
        Article proMahou = new Article();
                proMahou.setCaption( "Mahou 1/3" );
                proMahou.setDescription( "Mahou 1/3 - 5 estrellas" );
                proMahou.setPrice( BigDecimal.valueOf( 1.9d ) );
               /// proMahou.setIcon( new ImageIcon( "/home/peyrona/proyectos/Tapas/img/mahou.png" ) );

        Article proCruz = new Article();
                proCruz.setCaption( "Cruzcampo 1/5" );
                proCruz.setDescription( "Cruzcampo 1/5" );
                proCruz.setPrice( BigDecimal.valueOf( 1.7d ) );

        Article catBirras = new Article();
                catBirras.setCaption( "Cervezas" );
                catBirras.addToSubMenu( proCruz );
                catBirras.addToSubMenu( proMahou );

        Article proCoca = new Article();
                proCoca.setCaption( "CocaCola 1/2" );
                proCoca.setDescription( "CocaCola 1/2 litro" );
                proCoca.setPrice( BigDecimal.valueOf( 1.5d ) );

        Article proFanta = new Article();
                proFanta.setCaption( "Fanta naranja" );
                proFanta.setDescription( "Fanta naranja" );
                proFanta.setPrice( BigDecimal.valueOf( 1.4d ) );

        Article catColas = new Article();
                catColas.setCaption( "Refrescos" );
                catColas.addToSubMenu( proCoca );
                catColas.addToSubMenu( proFanta );

        List<Article> list = new ArrayList<Article>();
                      list.add( catBirras );
                      list.add( catColas );

        DataProvider.getInstance().setCategoriesAndProducts( list );
    }
}