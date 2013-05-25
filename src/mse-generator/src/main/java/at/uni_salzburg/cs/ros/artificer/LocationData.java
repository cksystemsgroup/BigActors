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

import at.uni_salzburg.cs.ckgroup.course.PolarCoordinate;
import at.uni_salzburg.cs.ckgroup.course.WGS84;
import at.uni_salzburg.cs.ros.Configuration;
import at.uni_salzburg.cs.ros.LocationSimulationDetails;

import org.apache.commons.lang.math.RandomUtils;

import java.util.Map;

import big_actor_msgs.LatLng;
import big_actor_msgs.Location;

public class LocationData implements Updatable
{
    private Location location;
    private Map<Integer, LocationSimulationDetails> simDetails;
    private double velocity;
    private double mutation;
    private boolean moving;
    private double headingSouth;
    private double headingEast;
    private double heading;
    
    private WGS84 geodeticSystem = new WGS84();
    private Clock clock;
    
    /**
     * @param configuration configuration
     * @param location location
     */
    public LocationData(Configuration configuration, Location location)
    {
        clock = configuration.getClock();
        this.location = location;
        simDetails = configuration.getLocationSimulationDetails();
        velocity = simDetails.get(location.getLocationId()).getAverageSpeed();
        mutation  = simDetails.get(location.getLocationId()).getMutationSpeed();
        moving = Math.abs(velocity) > 1E-4;
        heading = RandomUtils.nextDouble() * 2.0 * Math.PI;
        headingSouth = Math.sin(heading) * velocity;
        headingEast = Math.cos(heading) * velocity;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void update()
    {
        if (!moving)
        {
            return;
        }

        for (LatLng vertex : location.getBoundaries())
        {
            PolarCoordinate pos = new PolarCoordinate(vertex.getLatitude(), vertex.getLongitude(), 0);
            double x = headingSouth + RandomUtils.nextDouble() * mutation;
            double y = headingEast + RandomUtils.nextDouble() * mutation;
            
            PolarCoordinate r = geodeticSystem.walk(pos, x, y, 0);
            
            vertex.setLatitude(r.getLatitude());
            vertex.setLongitude(r.getLongitude());
        }
        
        location.setTimeStamp(clock.currentTimeMillis());
    }

    /**
     * @return location
     */
    public Location getLocation()
    {
        return location;
    }
}
