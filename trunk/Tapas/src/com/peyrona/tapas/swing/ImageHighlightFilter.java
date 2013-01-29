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

package com.peyrona.tapas.swing;

import java.awt.image.RGBImageFilter;

/**
 *
 * @author Francisco Morero Peyrona
 */
public final class ImageHighlightFilter extends RGBImageFilter
{
    private boolean brighter;
    private int     amount;

    //------------------------------------------------------------------------//

    /**
     * Filter to apply to images.
     *
     * @param brighter  true to highlight and false to de-highlight.
     * @param percent   Percent to highlight or de-highlight.
     */
    public ImageHighlightFilter( boolean brighter, int percent )
    {
        if( percent > 100 ) percent = 100;
        if( percent <   0 ) percent =   0;

        this.brighter = brighter;
        this.amount   = 100 - percent;

        canFilterIndexColorModel = true;
    }

    @Override
    public int filterRGB( int x, int y, int rgb )
    {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >>  8) & 0xff;
        int b = (rgb >>  0) & 0xff;

        if( brighter )
        {
            r = 255 - (((255 - r) * amount) / 100);
            g = 255 - (((255 - g) * amount) / 100);
            b = 255 - (((255 - b) * amount) / 100);
        }
        else
        {
            r = (r * amount) / 100;
            g = (g * amount) / 100;
            b = (b * amount) / 100;
        }

        if(      r <   0 )  r = 0;
        else if( r > 255 )  r = 255;

        if(      g <   0 )  g = 0;
        else if( g > 255 )  g = 255;

        if(      b <   0 )  b = 0;
        else if( b > 255 )  b = 255;

        return (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
    }
}