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

package com.peyrona.tapas.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Francisco Morero Peyrona
 */
class ImageFilter extends FileFilter
{
    @Override
    public boolean accept( File f )
    {
        if( f.isDirectory() )
            return true;

        String sFileName = f.getName();

        return sFileName.endsWith( ".tiff" ) ||
               sFileName.endsWith( ".tif"  ) ||
               sFileName.endsWith( ".gif"  ) ||
               sFileName.endsWith( ".jpeg" ) ||
               sFileName.endsWith( ".jpg"  ) ||
               sFileName.endsWith( ".png"  ) ;
    }

    public String getDescription()
    {
        return "Imágenes";
    }
}
