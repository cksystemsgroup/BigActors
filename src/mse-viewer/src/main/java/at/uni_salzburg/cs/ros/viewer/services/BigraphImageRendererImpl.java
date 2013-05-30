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
package at.uni_salzburg.cs.ros.viewer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.berkeley.eloi.bigvis.BgBigraph;
import edu.berkeley.eloi.bigvis.BgContext;
import edu.berkeley.eloi.bigvis.BgParseException;
import edu.berkeley.eloi.bigvis.BgParser;
import edu.berkeley.eloi.bigvis.BgRegion;
import edu.berkeley.eloi.bigvis.BgShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BigraphImageRendererImpl implements BigraphImageRenderer
{
    private final Logger LOG = LoggerFactory.getLogger(BigraphImageRendererImpl.class);

    @Override
    public byte[] convertBgmToPng(String bgstring) throws IOException
    {
        if (bgstring == null)
        {
            return null;
        }

        BgBigraph bigraph = parseBigraph(bgstring);

        BufferedImage bgImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bgImage.createGraphics();
        bigraph.layout(g2d);
        
        int width = bigraph.getWidth() + 70;
        int height = bigraph.getHeight() + 170;
        
        bgImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2d = bgImage.createGraphics();
        g2d.setPaint(Color.white);
        g2d.fillRect(0, 0, width, height);
        bigraph.draw(g2d, 25D, 100D);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bgImage, "PNG", bos);

        return bos.toByteArray();
    }

    
    private BgBigraph parseBigraph(String bgstring)
    {
        int regionsCounter = 0;
        BgContext context = new BgContext();
        BgBigraph bigraph = new BgBigraph(context);

        for (String s : bgstring.split("\\|\\|"))
        {
            BgRegion region = new BgRegion(context, regionsCounter++);

            try
            {
                BgParser bgp = new BgParser(context, s);
                for (BgShape shape : bgp.parse())
                {
                    region.addChild(shape);
                }
            }
            catch (BgParseException e)
            {
                LOG.error("Can not parse bigraph '" + bgstring + "'", e);
            }

            bigraph.addChild(region);
        }
        
        return bigraph;
    }

}
