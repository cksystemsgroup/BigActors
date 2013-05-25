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
package at.uni_salzburg.cs.ros.artificer;

import at.uni_salzburg.cs.ros.Configuration;

import big_actor_msgs.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * LocationArtificer
 */
public class LocationArtificer extends Artificer
{
    private List<LocationData> locationData;

    private Configuration configuration;
    
    /**
     * @param configuration configuration
     */
    public LocationArtificer(Configuration configuration)
    {
        this.configuration = configuration;
        init();
    }

    private void init()
    {
        locationData = new ArrayList<LocationData>();

        for (Location location : configuration.getLocations())
        {
            LocationData ld = new LocationData(configuration, location);
            locationData.add(ld);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        configuration.getNode().getLog().info("LocationArtificer.execute()");
        
        for (LocationData ld : locationData)
        {
            ld.update();
        }
    }

    /**
     * @return the current locations
     */
    public List<Location> currentLocations()
    {
        List<Location> locations = new ArrayList<Location>();
        
        for (LocationData ld : locationData)
        {
            locations.add(ld.getLocation());
        }
        
        return locations;
    }

}
