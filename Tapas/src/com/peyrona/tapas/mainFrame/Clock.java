/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.peyrona.tapas.mainFrame;

import com.peyrona.tapas.Utils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
final class Clock extends JPanel
{
    private static final Color COLOR_TINTA = Color.cyan.darker();

    private JLabel  lblClock;
    private boolean bShowSecs;       // Mostrar los segundos?
    private int     nSecs;

    //------------------------------------------------------------------------//

    Clock()
    {
        initComponents();

        setMaximumSize( new Dimension( 120, 48 ) );
        setPreferredSize( getMaximumSize() );
        setBackground( Color.black );
        setBorder( new EmptyBorder( 4,4,4,4 ) );
        setLayout( new GridLayout( 1, 1 ) );
        add( lblClock );
    }

    private void initComponents()
    {
        nSecs = 0;

        lblClock = new JLabel();
        lblClock.setHorizontalAlignment( JLabel.CENTER );
        lblClock.setForeground( COLOR_TINTA );
        lblClock.setFont( new Font( "Courier New", Font.BOLD, 34 ) );

        // Si el micro tiene más de un core nos podemos permitir un reloj más complejo.
        bShowSecs = (Utils.getCores() > 1);

        Timer timer = new Timer( (bShowSecs ? 1 : 60) * 1000, new UpdateTask() );
              timer.start();
    }

    @Override
    protected void paintComponent( Graphics g )
    {
	    super.paintComponent( g );

        if( bShowSecs )
        {
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor( COLOR_TINTA );
            g2d.fillRect( 0, getHeight()-4, nSecs*2+2, 4 );
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
            if( nSecs == 0 )
            {
                updateHour();
            }

            // Lo apropiado sería pasar de System.currentTimeMillis() a secs transcurridos
            // desde medianoche, pero esto es más simple y también vale.
            nSecs = (nSecs == 58 ? 0 : nSecs+1);

            if( Clock.this.bShowSecs )
            {
                // Mejora mucho el rendimiento porque sólo pinta el área especificada
                Clock.this.repaint( 0, Clock.this.getHeight()-4, Clock.this.getWidth(), 4 );
            }
        }

        private void updateHour()
        {
            Clock.this.lblClock.setText( dateFormat.format( new Date() ) );
        }
    }
}