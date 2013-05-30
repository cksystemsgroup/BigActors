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

import org.apache.commons.io.FileUtils;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * BigraphArchiveImpl
 */
@EagerLoad
public class BigraphArchiveImpl implements BigraphArchive
{
    private static final Logger LOG = LoggerFactory.getLogger(BigraphArchiveImpl.class);

    private static final String BGM_ARCHIVE_CLEANUP_PROP = "bigraph.archive.cleanup";
    private static final String BGM_ARCHIVE_PROP = "bigraph.archive.dir";
    private static final String BGM_ARCHIVE_DEFAULT_DIR = "bgmArchive";
    private static final String BGM_IMAGE_NAME_FORMAT = "bgm-%d.png";
    private static final String BGM_IMAGE_NAME_PATTERN = "bgm-\\d+.png";

    private BigraphImageRenderer imageRenderer;

    private File bgmArchive;

    private String bgmString = "";

    private File currentImage;

    /**
     * @param imageRenderer the BigraphImageRenderer instance.
     * @throws IOException
     */
    public BigraphArchiveImpl(BigraphImageRenderer imageRenderer) throws IOException
    {
        this.imageRenderer = imageRenderer;
        String bgmArchiveDirName = System.getProperty(BGM_ARCHIVE_PROP, BGM_ARCHIVE_DEFAULT_DIR);
        bgmArchive = new File(bgmArchiveDirName);
        FileUtils.forceMkdir(bgmArchive);
        LOG.info("Using archive directory {}", bgmArchive.getAbsolutePath());

        String cleanUp = System.getProperty(BGM_ARCHIVE_CLEANUP_PROP, "true");
        if ("true".equalsIgnoreCase(cleanUp))
        {
            LOG.info("Cleaning up bigraph image archive folder '{}'", bgmArchive.getAbsolutePath());
            FileUtils.cleanDirectory(bgmArchive);
        }
        else
        {
            LOG.info("Cleaning up bigraph image archive folder '{}' declined.", bgmArchive.getAbsolutePath());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void archive(String newBgmString) throws IOException
    {
        if (newBgmString == null || bgmString.equals(newBgmString))
        {
            // LOG.debug("Ignoring empty or unchanged BGM string.");
            return;
        }

        LOG.debug("New BGM received: {}", newBgmString);

        String bgmImageName = String.format(BGM_IMAGE_NAME_FORMAT, System.currentTimeMillis());
        currentImage = new File(bgmArchive, bgmImageName);

        FileOutputStream fos = new FileOutputStream(currentImage);
        byte[] image = imageRenderer.convertBgmToPng(newBgmString);
        fos.write(image);
        fos.close();

        bgmString = newBgmString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentImageId()
    {
        return currentImage != null ? currentImage.getName() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getImage(String imageId)
    {
        if (imageId.matches(BGM_IMAGE_NAME_PATTERN))
        {
            File image = new File(bgmArchive, imageId);
            if (image.exists() && image.isFile())
            {
                return image;
            }
        }

        return null;
    }

}
