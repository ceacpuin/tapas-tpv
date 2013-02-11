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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Interfaz con las operaciones de persistencia que la aplicación necesita.
 *
 * @author Francisco Morero Peyrona
 */
public interface DataProviderable
{
    /**
     * Performs the connection to data sopurce and creates (if needed) the
     * Relational Data Model (tables, foreign keys and indexes).
     *
     * @throws Exception
     */
    void connect() throws Exception;

    /**
     * Gently disconnect from data source.
     */
    void disconnect() throws Exception;

    Configuration getConfiguration() throws Exception;

    void setConfiguration( Configuration config ) throws Exception;

    List<Product> getCategoriesAndProducts() throws Exception;

    void setCategoriesAndProducts( List<Product> products ) throws Exception;

    Bill insertBill( Bill bill ) throws Exception;

    Bill updateBill( Bill bill ) throws Exception;

    void deleteBill( Bill bill ) throws Exception;

    List<Bill> findBills( Date dFrom, Date dTo, Bill.Payment[] payments, boolean bDelete ) throws Exception;

    List<Bill> findBillsByCustomer( String sCustomerPattern ) throws Exception;
}