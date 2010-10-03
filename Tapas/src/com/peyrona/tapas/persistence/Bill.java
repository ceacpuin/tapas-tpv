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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase Entidad (Entity Class) que representa elementos "Bills" de la
 * fuente de datos.
 * <p>
 * En la implementación por defecto del repositorio de datos se utiliza una
 * base de datos Derby y en este caso, esta entidad se corresponde con la tabla
 * "Ventas".
 *
 * @author Francisco Morero Peyrona
 */
public final class Bill
{
    public enum Payment { Undefined, Paid, Deferred, Invitation, NotPaid }

    public final static int NOW = -1;    // Para ser usado con setWhen(...)

    private static int nCount = 1;

    private int            nId;
    private String         sCustomer;
    private long           nWhen;
    private Payment        payment;
    private List<BillLine> lstLines;

    //------------------------------------------------------------------------//
    // PUBLIC INTERFACE

    public Bill()
    {
        setId( -1 );
        setCustomer( String.valueOf( nCount++ ) );
        setWhen( 0l );
        setPayment( Payment.Undefined );
        setLines( null );
    }

    public String getCustomer()
    {
        return sCustomer;
    }

    public void setCustomer( String sName )
    {
        if( sName != null )
            this.sCustomer = sName;
    }

    /**
     * @return the nWhen
     */
    public long getWhen()
    {
        return nWhen;
    }

    /**
     * @param nWhen the nWhen to set
     */
    public void setWhen( long nWhen )
    {
        this.nWhen = (nWhen < 0  ? System.currentTimeMillis() : nWhen);
    }

    /**
     * @return the nPayMode
     */
    public Payment getPayment()
    {
        return payment;
    }

    /**
     * @param nPayMode the nPayMode to set
     */
    public void setPayment( Payment paymode )
    {
        this.payment = paymode;
    }

    /**
     * @return the lstLines
     */
    public List<BillLine> getLines()
    {
        return lstLines;
    }

    /**
     * @param lstLines the lstLines to set
     */
    public void setLines( List<BillLine> lstLines )
    {
        if( lstLines == null )
            lstLines = new ArrayList<BillLine>();

        this.lstLines = lstLines;
    }

    public void addLine( BillLine line )
    {
        if( this.lstLines == null )
            this.lstLines = new ArrayList<BillLine>();

        this.lstLines.add( line );
    }

    public BigDecimal getTotal()
    {
        BigDecimal nTotal = new BigDecimal( 0 );

        for( BillLine line : lstLines )
        {
            BigDecimal nQuantity = new BigDecimal( line.getQuantity() );
            nTotal = nTotal.add( line.getPrice().multiply( nQuantity ) );
        }

        return nTotal;
    }

    public boolean isClosed()
    {
        return (getPayment() != null);
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
        final Bill other = (Bill) obj;
        if( this.nId != other.nId )
        {
            return false;
        }
        if( (this.sCustomer == null) ? (other.sCustomer != null) : !this.sCustomer.equals( other.sCustomer ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 61 * hash + this.nId;
        hash = 61 * hash + (this.sCustomer != null ? this.sCustomer.hashCode() : 0);
        hash = 61 * hash + (int) (this.nWhen ^ (this.nWhen >>> 32));
        hash = 61 * hash + (this.payment != null ? this.payment.hashCode() : 0);
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

    int getPayModeAsInt()
    {
        int n = -1;

        switch( getPayment() )
        {
            case Undefined : n = 1; break;
            case Deferred  : n = 2; break;
            case Invitation: n = 3; break;
            case NotPaid   : n = 4; break;
            case Paid      : n = 5; break;
        }

        return n;
    }

    void setPayModeAsInt( int nPayMode )
    {
        switch( nPayMode )
        {
            case 1: setPayment( Payment.Undefined  ); break;
            case 2: setPayment( Payment.Deferred   ); break;
            case 3: setPayment( Payment.Invitation ); break;
            case 4: setPayment( Payment.NotPaid    ); break;
            case 5: setPayment( Payment.Paid       ); break;
        }
    }
}