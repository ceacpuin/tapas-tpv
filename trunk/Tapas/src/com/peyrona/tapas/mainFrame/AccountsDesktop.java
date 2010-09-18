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

package com.peyrona.tapas.mainFrame;

import com.peyrona.tapas.persistence.Bill;
import com.peyrona.tapas.persistence.DataProvider;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.DesktopManager;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.border.LineBorder;

/**
 *
 * @author Francisco Morero Peyrona
 */
final class AccountsDesktop extends JDesktopPane
{
    AccountsDesktop()
    {
        setMinimumSize( new Dimension( 580, 380 ) );
        setBorder( new LineBorder( Color.black, 2, true ));
    }

    void createAccount()
    {
        openAccount( null );
    }

    void openAccount( Bill bill )
    {
        final AccountInternalFrame iframe = new AccountInternalFrame( bill );
                             iframe.setVisible( true );

        add( iframe );
        iframe.setSelected( true );

       if( DataProvider.getInstance().getConfiguration().isAutoAlignSelected() )
           mosaic();
    }

    boolean isEmpty()
    {
        return (getAllFrames().length == 0);
    }

    void mosaic()
    {
        DesktopManager   manager = getDesktopManager();
        JInternalFrame[] aFrame  = getAllFrames();

        if( manager != null && aFrame.length > 0 )  // Tiene que haber desktop manager y ventanas
        {
            int nDeskCols  = getSize().width / aFrame[0].getWidth();  // Nº de iFrames que caben a lo ancho
            int nFrmWidth  = aFrame[0].getWidth();     // Anchura de la iFrame
            int nFrmHeight = aFrame[0].getHeight();    // Altura de la iFrame
            int nColIndex  = -1;                       // Columna en la que se situa la iFrame
            int nRowIndex  = 0;                        // Fila en la que se situa la iFrame

            for( int n = aFrame.length - 1; n >= 0 ; n-- )
            {
                if( aFrame[n].isVisible() )
                {
                    nColIndex++;

                    if( nColIndex == nDeskCols )
                    {
                        nColIndex = 0;
                        nRowIndex++;
                    }

                    manager.dragFrame( aFrame[n], nColIndex * nFrmWidth + nColIndex,
                                                  nRowIndex * nFrmHeight + nRowIndex );
                }
            }
        }
    } // Fin método
} // Fin clase