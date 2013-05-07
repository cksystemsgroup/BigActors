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
package at.uni_salzburg.cs.ros.viewer.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import com.trsvax.bootstrap.annotations.Exclude;

import java.util.Arrays;
import java.util.Collection;

/**
 * Layout component for pages of application mse-viewer.
 */
@Exclude(stylesheet = {"core"})
//@Import(stack = "bootstrap", stylesheet = "layout.css")
@Import(stylesheet = {
    "classpath:/com/trsvax/bootstrap/assets/bootstrap/css/bootstrap.css",
    "classpath:/com/trsvax/bootstrap/assets/bootstrap/css/bootstrap-responsive.css",
    "layout.css"
},
    library = {
        "classpath:/com/trsvax/bootstrap/assets/bootstrap/js/bootstrap.js"
    })
public class Layout
{
    /**
     * The page title, for the <title> element and the <h1>element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    private String pageName;

    @Inject
    private Messages messages;

    @Inject
    private ComponentResources resources;

    @Inject
    @Symbol(SymbolConstants.APPLICATION_VERSION)
    private String appVersion;

    String defaultTitle()
    {
        return messages.get("pagetitle." + resources.getPageName());
    }

    public String getClassForPageName()
    {
        return resources.getPageName().equals(pageName) ? "active" : null;
    }

    public Collection<String> getPageNames()
    {
        return Arrays.asList("about", "contact");
    }

    public String getPageLabel()
    {
        return messages.get("pagetitle." + pageName);
    }

}
