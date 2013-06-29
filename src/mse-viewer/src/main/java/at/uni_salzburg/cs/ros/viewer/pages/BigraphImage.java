/*
 * This code is part of the BigActor project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.ros.viewer.pages;

import at.uni_salzburg.cs.ros.viewer.services.BigraphImageRenderer;
import at.uni_salzburg.cs.ros.viewer.services.PngImageStreamResponse;
import at.uni_salzburg.cs.ros.viewer.services.ros.RosNodeStarter;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * BigraphImage
 */
public class BigraphImage
{
    private final Logger LOG = LoggerFactory.getLogger(BigraphImage.class);
    
    @Inject
    private RosNodeStarter rosNodeStarter;

    @Inject
    private BigraphImageRenderer imageRenderer;

    
    /**
     * @return the currently available bigraph as PNG image.
     */
    public StreamResponse onActivate()
    {
        String bigraphString = rosNodeStarter.getBgmListener().getBigraphString();

        byte[] image = null;

        try
        {
            image = imageRenderer.convertBgmToPng(bigraphString);
        }
        catch (IOException e)
        {
            LOG.error("Can not render bigraph image.", e);
        }

        return new PngImageStreamResponse(image);
    }

}
