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

import java.io.File;
import java.io.IOException;

/**
 * BigraphArchive
 */
public interface BigraphArchive
{
    /**
     * @param bigraph the new bigraph as a <code>String</code> to be archived.
     * @throws IOException thrown in case of errors.
     */
    void archive(String bigraph) throws IOException;

    /**
     * @return the current bigraph image id.
     */
    String getCurrentImageId();

    /**
     * @param imageId the image identification
     * @return the requested image or null (if not found).
     */
    File getImage(String imageId);
}
