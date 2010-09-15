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

import java.math.BigDecimal;

/**
 * Clase Entidad (Entity Class) que representa elementos "BillLines" de la
 * fuente de datos.
 * <p>
 * En la implementación por defecto del repositorio de datos se utiliza una
 * base de datos Derby y en este caso, esta entidad se corresponde con la tabla
 * "Ventas_detalle".

 * @author Francisco Morero Peyrona
 */
public class BillLine
{
    private int        nId;
    private int        nQuantity;
    private String     sItem;
    private BigDecimal nPrice;

    public BillLine( int nQuantity, String sItem, BigDecimal nPrice )
    {
        this.nId       = -1;
        this.nQuantity = nQuantity;
        this.sItem     = sItem;
        this.nPrice    = nPrice;
    }

    //------------------------------------------------------------------------//
    // PUBLIC INTERFACE

    /**
     * @return the nQuantity
     */
    public int getQuantity()
    {
        return nQuantity;
    }

    /**
     * @param nQuantity the nQuantity to set
     */
    public void setQuantity( int nQuantity )
    {
        this.nQuantity = nQuantity;
    }

    /**
     * @return the sItem
     */
    public String getItem()
    {
        return sItem;
    }

    /**
     * @param sItem the sItem to set
     */
    public void setItem( String sItem )
    {
        this.sItem = sItem;
    }

    /**
     * @return the nPrice
     */
    public BigDecimal getPrice()
    {
        return nPrice;
    }

    /**
     * @param nPrice the nPrice to set
     */
    public void setPrice( BigDecimal nPrice )
    {
        this.nPrice = nPrice;
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
        final BillLine other = (BillLine) obj;
        if( this.nId != other.nId )
        {
            return false;
        }
        if( this.nQuantity != other.nQuantity )
        {
            return false;
        }
        if( (this.sItem == null) ? (other.sItem != null) : !this.sItem.equals( other.sItem ) )
        {
            return false;
        }
        if( this.nPrice.compareTo( other.nPrice ) != 0 )
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 97 * hash + this.nId;
        hash = 97 * hash + this.nQuantity;
        hash = 97 * hash + (this.sItem != null ? this.sItem.hashCode() : 0);
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