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

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Procesa los comandos SQL almacenados en un fichero.
 * <p>
 * Los comandos tienen que terminar con ';'.<br>
 * Los comentarios ("--") y las líneas en blanco son ignoradas.
 *
 * @author Francisco Morero Peyrona
 */
public final class SQLExecutor
{
    private final static String sSQL_Comment     = "--";
    private final static String sSQL_Termination =  ";";

    private Connection     dbConn;
    private BufferedReader reader;

    SQLExecutor( Connection conn, InputStream is )
    {
        dbConn = conn;
        reader = new BufferedReader( new InputStreamReader( is ) );
    }

    void extecute() throws SQLException, IOException
    {
        Statement      stmt  = null;
        StringBuilder  sb    = new StringBuilder( 1024 );
        String         sLine;

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
    }

    boolean isSQL( String s )
    {
        String sLine = s.trim();

        return (sLine.length() > 0) &&
               (! sLine.startsWith( sSQL_Comment ));
    }
}