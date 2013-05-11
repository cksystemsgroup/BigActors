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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

/**
 * MapStack
 */
public class MapStack implements JavaScriptStack
{
    private StylesheetLink[] stylesheets;

    private Asset[] javaScriptLibraries;

    /**
     * @param assetSource the asset source
     */
    public MapStack(final AssetSource assetSource)
    {
        stylesheets =
            new StylesheetLink[]
            {
                TapestryInternalUtils.assetToStylesheetLink.map(assetSource
                    .getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/css/leaflet-0.5.1.css")),
                TapestryInternalUtils.assetToStylesheetLink.map(assetSource
                    .getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/css/Quadrotor.css")),   
            };
        javaScriptLibraries =
            new Asset[]
            {
//                assetSource.getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/js/kinetic-v4.4.3.min.js"),
                assetSource.getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/js/leaflet-0.5.1.min.js"),
                assetSource.getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/js/underscore-1.4.4.min.js"),
//                assetSource.getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/js/backbone-1.0.0.min.js"),
                assetSource.getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/js/Location.js"),
                assetSource.getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/js/Quadrotor.js"),
                assetSource.getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/js/mse-viewer.js"),
            };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getStacks()
    {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Asset> getJavaScriptLibraries()
    {
        return Arrays.asList(javaScriptLibraries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StylesheetLink> getStylesheets()
    {
        return Arrays.asList(stylesheets);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInitialization()
    {
        return null;
    }
}
