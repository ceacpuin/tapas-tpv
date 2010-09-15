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

import com.peyrona.tapas.utils.Utils;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Este es un tipo un poco raro de Factory, de hecho y para exáctos no es un
 * factory propiamente dicho, pero se le parece bastante.
 *
 * Esta clase internamente instancia unnnn DataProviderable y hace de
 * facade contra ella, por lo que sería un híbrido entre los patrones de diseño
 * facade y factory. También implementa Sigleton (una vez puestos...)
 *
 * @author Francisco Morero Peyrona
 */
public final class DataProvider implements DataProviderable
{
    public enum DataSources { DerbyEmbedded };   // Para facilitar el añadir más tipos de almacenamiento

    private static DataSources  type     = null;
    private static DataProvider instance = null;    // Para el Singleton

    private DataProviderable provider = null;

    // FIXME: Poner la instancia Config en la caché (las imgs tardan mucho)

    //------------------------------------------------------------------------//

    @Override
    public void connect()
    {
        try
        {
            provider.connect();
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }
    }

    @Override
    public void disconnect()
    {
        try
        {
            provider.disconnect();
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }
    }

    @Override
    public Configuration getConfiguration()
    {
        Configuration conf = null;

        try
        {
            conf = provider.getConfiguration();
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }

        return conf;
    }

    @Override
    public void setConfiguration( Configuration config )
    {
        try
        {
            provider.setConfiguration( config );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }
    }

    @Override
    public List<Article> getCategoriesAndProducts()
    {
        List<Article> articles = null;

        try
        {
            articles = provider.getCategoriesAndProducts();
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }

        return articles;
    }

    @Override
    public void setCategoriesAndProducts( List<Article> articles )
    {
        try
        {
            provider.setCategoriesAndProducts( articles );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }
    }

    @Override
    public Bill insertBill( Bill bill )
    {
        Bill bill2Ret = null;

        try
        {
            bill2Ret = provider.insertBill( bill );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }

        return bill2Ret;
    }

    @Override
    public Bill updateBill( Bill bill )
    {
        Bill bill2Ret = null;

        try
        {
            bill2Ret = provider.updateBill( bill );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }

        return bill2Ret;
    }

    @Override
    public void deleteBill( Bill bill )
    {
        try
        {
            provider.deleteBill( bill );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }
    }

    @Override
    public List<Bill> findBillByCustomer( String sCustomerPattern )
    {
        List<Bill> bills = null;

        try
        {
            bills = provider.findBillByCustomer( sCustomerPattern );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }

        return bills;
    }

    @Override
    public List<Bill> findBillBetweenDates( Date from, Date to )
    {
        List<Bill> bills = null;

        try
        {
            bills = provider.findBillBetweenDates( from, to );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }

        return bills;
    }

    //------------------------------------------------------------------------//

    public static void setDataSourceType( DataSources ds )
    {
        type = ds;
    }

    public static DataProvider getInstance()
    {
        synchronized( DataProvider.class )
        {
            if( instance == null )
                instance = new DataProvider();
        }

        return instance;
    }

    //------------------------------------------------------------------------//

    private DataProvider()
    {
        if( type == null )
            type = DataSources.DerbyEmbedded;

        // Por ahora sólo hay un tipo de fuente de datos, pero de este modo se
        // pueden implementar otras fácilmente.
        switch( type )
        {
            case DerbyEmbedded: provider = new DataProvider4EmbeddedDerby();
        }
    }

    private void onFatalError( Exception exc )
    {
        Utils.showError( exc,
                         Level.SEVERE,
                         "No se ha podido acceder a la base de datos.\n"+
                         "El programa no puede continuar.",
                         Utils.nEXIT_DB_ERROR );
    }
}