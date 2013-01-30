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

import com.peyrona.tapas.Utils;
import com.peyrona.tapas.persistence.Bill.Payment;
import com.peyrona.tapas.swing.SwingUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    // Como Configuration se utiliza con mucha frecuencia, para no tener que estar
    // leyéndola cada vez de la DB, la instancia Configuration la ponemos en una
    // variable que la usamos a modo de caché.
    private Configuration config = null;

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
            {
                instance = new DataProvider();
            }
        }

        return instance;
    }

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
            // No es bueno, pero no hay nada que hacer.
        }
    }

    @Override
    public Configuration getConfiguration()
    {
        if( config == null )
        {
            try
            {
                config = provider.getConfiguration();
            }
            catch( Exception ex )
            {
                onFatalError( ex );
            }
        }

        return config;
    }

    @Override
    public void setConfiguration( Configuration config )
    {
        try
        {   // Estos son los tamaños max de las cadenas para estos datos
            config.setEmail( Utils.setMaxLength( config.getEmail(), 48 ) );
            config.setPassword( Utils.setMaxLength( config.getPassword(), 32 ) );
            config.setTicketHeader( Utils.setMaxLength( config.getTicketHeader(), 999 ) );
            config.setTicketFooter( Utils.setMaxLength( config.getTicketFooter(), 999 ) );

            provider.setConfiguration( config );
            this.config = config;
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }
    }

    @Override
    public HashMap<String,String> getConfiguration4Plugin( String sPluginName ) throws Exception
    {
        HashMap<String,String> ret = null;

        try
        {
            ret = provider.getConfiguration4Plugin( sPluginName );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }

        return ret;
    }

    @Override
    public void setConfiguration4Plugin( String sPluginName, HashMap<String,String> conf ) throws Exception
    {
        try
        {
            provider.setConfiguration4Plugin( sPluginName, conf );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }
    }

    @Override
    public List<Product> getCategoriesAndProducts()
    {
        List<Product> products = null;

        try
        {
            products = provider.getCategoriesAndProducts();
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }

        return products;
    }

    @Override
    public void setCategoriesAndProducts( List<Product> products )
    {
        try
        {
            // Estos son los tamaños max de las cadenas para estos los artículos
            for( Product product : products )
            {
                product.setCaption( Utils.setMaxLength( product.getCaption(), 16 ) );
                product.setDescription( Utils.setMaxLength( product.getDescription(), 32 ) );
            }

            provider.setCategoriesAndProducts( products );
        }
        catch( Exception ex )
        {
            ex.printStackTrace( System.err );
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
    public List<Bill> findBillsByCustomer( String sCustomerPattern )
    {
        List<Bill> bills = new ArrayList<Bill>();

        try
        {
            bills = provider.findBillsByCustomer( sCustomerPattern );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }

        return bills;
    }

    @Override
    public List<Bill> findBills( Date dFrom, Date dTo, Payment[] payments, boolean bDelete ) throws Exception
    {
        List<Bill> bills = new ArrayList<Bill>();

        try
        {
            bills = provider.findBills( dFrom, dTo, payments, bDelete );
        }
        catch( Exception ex )
        {
            onFatalError( ex );
        }

        return bills;
    }

    //------------------------------------------------------------------------//

    private DataProvider()
    {
        if( type == null )
        {
            type = DataSources.DerbyEmbedded;
        }
        
        // Por ahora sólo hay un tipo de fuente de datos, pero de este modo se
        // pueden implementar otras fácilmente.
        switch( type )
        {
            case DerbyEmbedded: provider = new DataProvider4EmbeddedDerby();
        }
    }

    private void onFatalError( Exception exc )
    {
        SwingUtils.showError( exc,
                              Level.SEVERE,
                              "No se ha podido acceder a la base de datos.\n"+
                              "El programa no puede continuar.",
                              Utils.nEXIT_DB_ERROR );
    }
}