/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.peyrona.tapas.account;

import com.peyrona.tapas.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * This class acts as a calculator display.
 * <p>
 * It has seberal methdos to perform needed actions: clear(), getAmount(), ...
 *
 * @author Francisco Morero Peyrona
 * @version 1.0
 */
final class TotalDisplayPanel extends JPanel
{
	private JTextField txt = new JTextField();

	/**
	 * Zero argumento constructor.
	 */
	TotalDisplayPanel()
	{
        initComponents();
	}

	/**
	 * Clears display and set a new amount using passed parameter.
	 *
	 * @param nAmount New amount to be displayed.
	 */
	void setAmount( BigDecimal nAmount )
	{
		txt.setText( Utils.formatAsCurrency( nAmount ) );
	}

    //------------------------------------------------------------------------//
    
	private void initComponents()
	{
        Color clrPaper = Color.black;
        Color clrInk   = Color.yellow;

        JLabel lblTotal = new JLabel( "Total:" );
               lblTotal.setFont( getFont().deriveFont( Font.BOLD, 24f ) );
               lblTotal.setForeground( clrInk );

		txt.setOpaque( false );
        txt.setFont( new java.awt.Font( "Monospaced", Font.BOLD, 24 ) );
        txt.setForeground( clrInk );
        txt.setBackground( clrPaper );
        txt.setBorder( null );
        txt.setDisabledTextColor( clrInk );
        txt.setEditable( false );
        txt.setMargin( new Insets( 3, 3, 3, 3 ) );
        txt.setHorizontalAlignment( SwingConstants.RIGHT );

        setBackground( clrPaper );
        setBorder( new EmptyBorder( 9,9,9,9 ) );
        setLayout( new BorderLayout( 9, 0 ) );
        add( lblTotal, BorderLayout.WEST   );
        add( txt     , BorderLayout.CENTER );

        setAmount( new BigDecimal( 0 ) );
	}
}