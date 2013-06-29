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

import at.uni_salzburg.cs.ros.viewer.services.BigraphReactionRuleArchive;
import at.uni_salzburg.cs.ros.viewer.services.JSONStreamResponse;
import at.uni_salzburg.cs.ros.viewer.services.PngImageStreamResponse;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.io.File;

public class BigraphReactionRule
{
    @Inject
    private BigraphReactionRuleArchive archive;

    @Inject
    private JavaScriptSupport js;

    /**
     * import the Bigraph JavaScript stack.
     */
    @SetupRender
    public void importStack()
    {
        js.importStack("bigraph");
        js.addScript("redex = new BigraphUpdater('bigraphReactionRule','redex');" +
            "var cmdRedex = '$.getJSON(\"bigraphReactionRule/redex\", function(data){redex.refresh(data)});';" +
            "eval(cmdRedex);" +
            "setInterval(cmdRedex, 1000);" +
            "reactum = new BigraphUpdater('bigraphReactionRule','reactum');" +
            "var cmdReactum = '$.getJSON(\"bigraphReactionRule/reactum\", function(data){reactum.refresh(data)});';" +
            "eval(cmdReactum);" +
            "setInterval(cmdReactum, 1000);");
    }

    /**
     * @param command the command
     * @return the currently available bigraph names as JSON object.
     */
    public StreamResponse onActivate(String command)
    {
        if ("redex".equals(command))
        {
            JSONObject result = new JSONObject().put("img", archive.getCurrentRedexImageId());
            return new JSONStreamResponse(result);
        }

        if ("reactum".equals(command))
        {
            JSONObject result = new JSONObject().put("img", archive.getCurrentReactumImageId());
            return new JSONStreamResponse(result);
        }

        return null;
    }

    /**
     * @param command the command
     * @param imageId the image identification
     * @return the currently available bigraph as PNG image.
     */
    public StreamResponse onActivate(String command, String imageId)
    {
        if ("img".equals(command))
        {
            File image = archive.getImage(imageId);
            return new PngImageStreamResponse(image);
        }

        return new PngImageStreamResponse(new byte[0]);
    }

}
