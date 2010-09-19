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

import java.awt.Image;
import java.util.Arrays;

/**
 * Clase Entidad (Entity Class) que representa elementos "Configuration" de la
 * fuente de datos.
 * <p>
 * En la implementación por defecto del repositorio de datos se utiliza una
 * base de datos Derby y en este caso, esta entidad se corresponde con la tabla
 * "Configuracion".
 * <p>
 * Todos los métodos de get son públicos, pero los de set son package para sólo
 * puedan ser invocados por el DataSourceProvider.
 *
 * @author Francisco Morero Peyrona
 */
public class Configuration
{
    private char[]  acPass      = new char[0];
    private String  sEmail      = null;
    private boolean bFullScreen = false;
    private boolean bAutoAlign  = false;
    private Image   imgHeader   = null;
    private String  sHeader     = null;
    private String  sFooter     = null;

    //------------------------------------------------------------------------//

    /**
     *
     *
     * @return
     */
    // Las buenas práctias aconsejan que en el caso de las contraseñas, se
    // devuelva un char[] en lugar de una String. Así lo hace el componente de
    // Swing JPasswordField.
    public boolean isValidPassword( char[] acPass )
    {
       return Arrays.equals( this.acPass, acPass );
    }

    public String getPassword()
    {
        return (acPass.length == 0 ? null : String.valueOf( acPass ));
    }

    public void setPassword( char[] acPass )
    {
        if( acPass == null || String.valueOf( acPass ).trim().length() == 0 )
            acPass = new char[0];

        this.acPass = Arrays.copyOf( acPass, acPass.length );   // Copia defensiva
    }

    // Package: sólo utilizable por los DataSource
    void setPassword( String sPass )
    {
        if( sPass == null || sPass.trim().length() == 0 )
            acPass = new char[0];
        else
            acPass = sPass.toCharArray();
    }

    /**
     * @return the sEmail
     */
    public String getEmail()
    {
        return sEmail;
    }

    /**
     * @param sEmail the sEmail to set
     */
    public void setEmail( String sEmail )
    {
        if( sEmail != null && sEmail.trim().length() == 0 )
            sEmail = null;

        this.sEmail = sEmail;
    }

    public boolean isFullScreenSelected()
    {
        return bFullScreen;
    }

    public void setFullScreenMode( boolean bFullScreen )
    {
        this.bFullScreen = bFullScreen;
    }

    public boolean isAutoAlignSelected()
    {
        return bAutoAlign;
    }

    public void setAutoAlignMode( boolean bAutoAlign )
    {
        this.bAutoAlign = bAutoAlign;
    }

    /**
     * @return the imgHeader
     */
    public Image getTicketHeaderImage()
    {
        return imgHeader;
    }

    /**
     * @param imgHeader the imgHeader to set
     */
    public void setTicketHeaderImage( Image imgHeader )
    {
        this.imgHeader = imgHeader;
    }

    /**
     * @return the sHeader
     */
    public String getTicketHeader()
    {
        return sHeader;
    }

    /**
     * @param sHeader the sHeader to set
     */
    public void setTicketHeader( String sHeader )
    {
        if( sHeader != null && sHeader.trim().length() == 0 )
            sHeader = null;

        this.sHeader = sHeader;
    }

    /**
     * @return the sFooter
     */
    public String getTicketFooter()
    {
        return sFooter;
    }

    /**
     * @param sFooter the sFooter to set
     */
    public void setTicketFooter( String sFooter )
    {
        if( sFooter != null && sFooter.trim().length() == 0 )
            sFooter = null;

        this.sFooter = sFooter;
    }
}