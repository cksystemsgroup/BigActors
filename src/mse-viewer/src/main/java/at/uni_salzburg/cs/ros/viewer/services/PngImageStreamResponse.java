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

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * PngImageStreamResponse
 */
public class PngImageStreamResponse implements StreamResponse
{
    private static final byte[] ONE_PIXEL_EMPTY_PNG = {
        (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a, 0x00, 0x00, 0x00, 0x0d, 0x49, 0x48, 0x44, 0x52,
        0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x08, 0x06, 0x00, 0x00, 0x00, 0x1f, 0x15, (byte) 0xc4,
        (byte) 0x89, 0x00, 0x00, 0x00, 0x01, 0x73, 0x52, 0x47, 0x42, 0x00, (byte) 0xae, (byte) 0xce, 0x1c,
        (byte) 0xe9,
        0x00, 0x00, 0x00, 0x06, 0x62, 0x4b, 0x47, 0x44, 0x00, (byte) 0xff, 0x00, (byte) 0xff, 0x00, (byte) 0xff,
        (byte) 0xa0, (byte) 0xbd, (byte) 0xa7, (byte) 0x93, 0x00, 0x00, 0x00, 0x09, 0x70, 0x48, 0x59, 0x73, 0x00,
        0x00,
        0x0b, 0x13, 0x00, 0x00, 0x0b, 0x13, 0x01, 0x00, (byte) 0x9a, (byte) 0x9c, 0x18, 0x00, 0x00, 0x00, 0x07,
        0x74,
        0x49, 0x4d, 0x45, 0x07, (byte) 0xdd, 0x05, 0x1e, 0x00, 0x02, 0x35, 0x7e, (byte) 0xd7, (byte) 0xac, 0x2c,
        0x00,
        0x00, 0x00, 0x1d, 0x69, 0x54, 0x58, 0x74, 0x43, 0x6f, 0x6d, 0x6d, 0x65, 0x6e, 0x74, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x43, 0x72, 0x65, 0x61, 0x74, 0x65, 0x64, 0x20, 0x77, 0x69, 0x74, 0x68, 0x20, 0x47, 0x49, 0x4d, 0x50,
        0x64, 0x2e, 0x65, 0x07, 0x00, 0x00, 0x00, 0x0d, 0x49, 0x44, 0x41, 0x54, 0x08, (byte) 0xd7, 0x63,
        (byte) 0xf8,
        (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x7f, 0x00, 0x09, (byte) 0xfb, 0x03, (byte) 0xfd, (byte) 0xd1,
        (byte) 0x83,
        (byte) 0x8c, (byte) 0xf0, 0x00, 0x00, 0x00, 0x00, 0x49, 0x45, 0x4e, 0x44, (byte) 0xae, 0x42, 0x60,
        (byte) 0x82
    };

    private File image = null;
    
    private byte[] imageData = null;

    /**
     * @param image the bigraph image as file.
     */
    public PngImageStreamResponse(File image)
    {
        this.image = image;
    }
    
    /**
     * @param image the bigraph image as array of bytes.
     */
    public PngImageStreamResponse(byte[] imageData)
    {
        this.imageData = imageData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentType()
    {
        return "image/png";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getStream() throws IOException
    {
        if (image != null)
        {
            return new FileInputStream(image);
        }
        
        if (imageData != null && imageData.length > 0)
        {
            return new ByteArrayInputStream(imageData);
        }

        return new ByteArrayInputStream(ONE_PIXEL_EMPTY_PNG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareResponse(Response response)
    {
        // Intentionally empty.
    }
}
