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

public class BootstrapStack implements JavaScriptStack
{
    private StylesheetLink[] stylesheets;

    private Asset[] javaScriptLibraries;

    public BootstrapStack(final AssetSource assetSource)
    {
        stylesheets =
            new StylesheetLink[]
            {
                //                TapestryInternalUtils.assetToStylesheetLink.map(assetSource
                //                    .getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/bootstrap.css")),
                //                TapestryInternalUtils.assetToStylesheetLink.map(assetSource
                //                    .getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/bootstrap-responsive.css")),
                TapestryInternalUtils.assetToStylesheetLink.map(assetSource
                    .getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/css/bootstrap-2.3.1.min.css")),
                TapestryInternalUtils.assetToStylesheetLink.map(assetSource
                    .getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/css/bootstrap-responsive-2.3.1.min.css"))
            };
        javaScriptLibraries =
            new Asset[]
            {
                assetSource.getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/js/jquery-1.9.1.min.js"),
                assetSource.getUnlocalizedAsset("/at/uni_salzburg/cs/ros/viewer/js/bootstrap-2.3.1.min.js")
            };
    }

    public List<String> getStacks()
    {
        return Collections.emptyList();
    }

    public List<Asset> getJavaScriptLibraries()
    {
        return Arrays.asList(javaScriptLibraries);
        //        return Collections.emptyList();
    }

    public List<StylesheetLink> getStylesheets()
    {
        return Arrays.asList(stylesheets);
    }

    public String getInitialization()
    {
        return null;
    }
}
