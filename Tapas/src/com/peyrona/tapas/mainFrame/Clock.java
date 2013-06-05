/*
 * Copyright (C) 2010 Francisco José Morero Peyrona. All Rights Reserved.
 *
 */

package com.peyrona.tapas.mainFrame;

import com.peyrona.tapas.Utils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * Esta clase muestra un reloj en una JLabel, el cual se actualiza cada 60 segs.
 * aproximadamente. En el peor de los casos puede haber un error de hasta 1 min.,
 * pero a quién le importa 1 min. arriba o abajo...<br>
 * Como contrapartida a esta inexactitud, consume muy pocos recursos.
 *
 * @author Francisco Morero Peyrona
 */
final class Clock extends JLabel
{
    private static final Color COLOR_TINTA = Color.cyan.darker();

    // Si el micro tiene más de un core nos podemos permitir un reloj más complejo.
    private static boolean bShowSecs = (Utils.getCores() > 1);

    private int nSecs = 0;

    //------------------------------------------------------------------------//

    Clock()
    {
        initComponents();

        setMaximumSize( new Dimension( 120, 48 ) );
        setPreferredSize( getMaximumSize() );
        setOpaque( true );
        setBackground( Color.black );
        setBorder( new EmptyBorder( 4,4,4,4 ) );
    }

    //----------------------------------------------------------------------------//

    private void initComponents()
    {
        setHorizontalAlignment( JLabel.CENTER );
        setForeground( COLOR_TINTA );
        setFont( new Font( "Courier New", Font.BOLD, 34 ) );

        Timer timer = new Timer( (bShowSecs ? 1 : 60) * 1000, new UpdateTask() );
              timer.start();
    }

    @Override
    protected void paintComponent( Graphics g )
    {
	    super.paintComponent( g );

        if( bShowSecs )
        {
            double nUnit = getWidth() / (double) 60;   // double para evitar el redondeo de las divisiones a enteros (por defecto)

            Graphics2D g2d = (Graphics2D) g;
                       g2d.setColor( getForeground() );
                       g2d.fillRect( 0, getHeight()-4, (int) (nSecs*nUnit), 4 );
        }
    }

    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    private final class UpdateTask implements ActionListener
    {
        private SimpleDateFormat dateFormat;

        private UpdateTask()
        {
            // Siempre que se pueda es mejor usar DateFormat y sus constantes
            // SHORT, LONG, ... porque entonces es la clase DateFormat la que
            // lidia con todos los pormenores de la internacionalización.
            // En este caso no lo he usado porque por defecto DateFormat no
            // rellena a ceros y odio una hora asï: "8:36", me gusta así "08:36"
            dateFormat = new SimpleDateFormat( "HH:mm" );

            updateHour();
        }

        @Override
        public void actionPerformed( ActionEvent ae )
        {
            if( Clock.this.nSecs == 0 )
            {
                updateHour();
            }

            // Pasamos de System.currentTimeMillis() a secs transcurridos en el minuto actual
            Clock.this.nSecs = ((int) (System.currentTimeMillis() / 1000)) % 60;

            if( Clock.this.nSecs == 0 )
            {
                updateHour();
            }

            if( Clock.bShowSecs )
            {
                // Repinta sólo el área de la barra de los segundos y no la hora (mejora el rendimiento)
                Clock.this.repaint( 0, getHeight()-4, getWidth(), 4 );
            }
        }

        private void updateHour()
        {
            Clock.this.setText( dateFormat.format( new Date() ) );
        }
    }
}