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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * Clase Entidad (Entity Class) que representa elementos "Articles" de la
 * fuente de datos.
 * <p>
 * En la implementación por defecto del repositorio de datos se utiliza una
 * base de datos Derby y en este caso, esta entidad se corresponde con las tablas
 * "Catgorias" y "Productos".
 * <p>
 * Nota: Esta clase se utiliza tanto para las entidades "Categorías" como
 * "Productos", que son (en el caso de Derby) dos tablas separadas, pero por
 * simplicidad se utiliza una sola Clase Entidad (total, por desperdiciar un par
 * de nulls no se va a morir ninguna JVM).
 * 
 * @author Francisco Morero Peyrona
 */
public final class Article
{
    private int           nId;
    private String        sCaption;
    private String        sDescription;
    private BigDecimal    nPrice;
    private ImageIcon     icon;
    private List<Article> lstSubMenu;

    //------------------------------------------------------------------------//
    // PUBLIC INTERFACE

    public Article()
    {
        setId( -1 );
        setCaption( "Sin nombre" );
        setDescription( "Sin descripción" );
        setPrice( BigDecimal.ZERO );
        setSubMenu( null );
    }

    /**
     * @return the sCaption
     */
    public String getCaption()
    {
        return sCaption;
    }

    /**
     * @param sCaption the sCaption to set
     */
    public void setCaption( String sCaption )
    {
        this.sCaption = sCaption;
    }

    /**
     * @return the sDescription
     */
    public String getDescription()
    {
        return sDescription;
    }

    /**
     * @param sDescription the sDescription to set
     */
    public void setDescription( String sDescription )
    {
        this.sDescription = sDescription;
    }

    /**
     * @return the icon
     */
    public ImageIcon getIcon()
    {
        if( this.icon == null )
            return null;
        else
            return new ImageIcon( icon.getImage() );    // Casi "copia defensiva"
    }

    public void setIcon( ImageIcon icon )
    {
        if( icon == null )
            setIcon( (Image) null );
        else
            setIcon( icon.getImage() );
    }

    public Image getImage()
    {
        if( this.icon == null )
            return null;
        else
            return icon.getImage();
    }

    public void setIcon( Image image )
    {
        if( image == null )
            this.icon = null;
        else
            this.icon = new ImageIcon( image );
    }

    /**
     * @return the nPrice
     */
    public BigDecimal getPrice()
    {
        return nPrice;
    }

    public void setPrice( BigDecimal price )
    {
        nPrice = price;
    }

    /**
     * @return the lstSubMenu
     */
    public List<Article> getSubMenu()
    {
        return lstSubMenu;
    }

    /**
     * @param lstSubMenu the lstSubMenu to set
     */
    public void setSubMenu( List<Article> lstSubMenu )
    {
        if( lstSubMenu == null )
            lstSubMenu = new ArrayList<Article>();

        this.lstSubMenu = lstSubMenu;
    }

    /**
     * Añade un artículo nuevo al subemnú.
     * 
     * @param article
     */
    public void addToSubMenu( Article article )
    {
        lstSubMenu.add( article );
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        if( getClass() != obj.getClass() )
        {
            return false;
        }
        final Article other = (Article) obj;
        if( this.nId != other.nId )
        {
            return false;
        }
        if( (this.sCaption == null) ? (other.sCaption != null) : !this.sCaption.equals( other.sCaption ) )
        {
            return false;
        }
        if( (this.sDescription == null) ? (other.sDescription != null) : !this.sDescription.equals( other.sDescription ) )
        {
            return false;
        }
        if( this.nPrice != other.nPrice && (this.nPrice == null || !this.nPrice.equals( other.nPrice )) )
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + this.nId;
        hash = 97 * hash + (this.sCaption != null ? this.sCaption.hashCode() : 0);
        hash = 97 * hash + (this.sDescription != null ? this.sDescription.hashCode() : 0);
        hash = 97 * hash + (this.nPrice != null ? this.nPrice.hashCode() : 0);
        return hash;
    }

    //------------------------------------------------------------------------//
    // PACKAGE INTERFACE

    int getId()
    {
        return nId;
    }

    void setId( int nId )
    {
        this.nId = nId;
    }
}