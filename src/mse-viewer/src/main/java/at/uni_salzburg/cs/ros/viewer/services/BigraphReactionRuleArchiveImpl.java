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
public class BigraphReactionRuleArchiveImpl implements BigraphReactionRuleArchive
{
    private static final Logger LOG = LoggerFactory.getLogger(BigraphReactionRuleArchiveImpl.class);

    private static final String BRR_ARCHIVE_CLEANUP_PROP = "bigraph.archive.cleanup";
    private static final String BRR_ARCHIVE_PROP = "bigraph.archive.dir";
    private static final String BRR_ARCHIVE_DEFAULT_DIR = "bgmArchive";
    private static final String BRR_REDEX_IMAGE_NAME_FORMAT = "bgm-%d-redex.png";
    private static final String BRR_REDEX_IMAGE_NAME_PATTERN = "bgm-\\d+-redex.png";
    private static final String BRR_REACTUM_IMAGE_NAME_FORMAT = "bgm-%d-reactum.png";
    private static final String BRR_REACTUM_IMAGE_NAME_PATTERN = "bgm-\\d+-reactum.png";
    
    private BigraphImageRenderer imageRenderer;

    private File brrArchive;

    private String brrString = "";

    private File currentRedexImage;
    
    private File currentReactumImage;

    /**
     * @param imageRenderer the BigraphImageRenderer instance.
     * @throws IOException
     */
    public BigraphReactionRuleArchiveImpl(BigraphImageRenderer imageRenderer) throws IOException
    {
        this.imageRenderer = imageRenderer;
        String brrArchiveDirName = System.getProperty(BRR_ARCHIVE_PROP, BRR_ARCHIVE_DEFAULT_DIR);
        brrArchive = new File(brrArchiveDirName);
        FileUtils.forceMkdir(brrArchive);
        LOG.info("Using archive directory {}", brrArchive.getAbsolutePath());

        String cleanUp = System.getProperty(BRR_ARCHIVE_CLEANUP_PROP, "true");
        if ("true".equalsIgnoreCase(cleanUp))
        {
            LOG.info("Cleaning up bigraph image archive folder '{}'", brrArchive.getAbsolutePath());
            FileUtils.cleanDirectory(brrArchive);
        }
        else
        {
            LOG.info("Cleaning up bigraph image archive folder '{}' declined.", brrArchive.getAbsolutePath());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void archive(String newBrrString) throws IOException
    {
        if (newBrrString == null || brrString.equals(newBrrString))
        {
            // LOG.debug("Ignoring empty or unchanged BRR string.");
            return;
        }

        LOG.debug("New BRR received: {}", newBrrString);
        
        String[] rr = newBrrString.split("\\s*->\\s*");
        
        if (rr.length != 2)
        {
            currentRedexImage = null;
            currentReactumImage = null;
            brrString = "";
            return;
        }
        
        long now = System.currentTimeMillis();
        
        String redexImageName = String.format(BRR_REDEX_IMAGE_NAME_FORMAT, now);
        currentRedexImage = renderImage(redexImageName, rr[0]);

        String reactumImageName = String.format(BRR_REACTUM_IMAGE_NAME_FORMAT, now);
        currentReactumImage = renderImage(reactumImageName, rr[1]);

        brrString = newBrrString;
    }
    
    /**
     * @param imageName the image file name
     * @param bgm the bigraph
     * @return the file name of the rendered image.
     * @throws IOException thrown in case of errors.
     */
    private File renderImage(String imageName, String bgm) throws IOException
    {
        File imageFile = new File(brrArchive, imageName);

        FileOutputStream fos = new FileOutputStream(imageFile);
        byte[] image = imageRenderer.convertBgmToPng(bgm);
        fos.write(image);
        fos.close();
        
        return imageFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentRedexImageId()
    {
        return currentRedexImage != null ? currentRedexImage.getName() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentReactumImageId()
    {
        return currentReactumImage != null ? currentReactumImage.getName() : null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public File getImage(String imageId)
    {
        if (imageId.matches(BRR_REDEX_IMAGE_NAME_PATTERN) || imageId.matches(BRR_REACTUM_IMAGE_NAME_PATTERN))
        {
            File image = new File(brrArchive, imageId);
            if (image.exists() && image.isFile())
            {
                return image;
            }
        }
        
        return null;
    }
}
