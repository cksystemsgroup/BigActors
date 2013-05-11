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

import big_actor_msgs.LatLng;
import big_actor_msgs.LatLngAlt;
import big_actor_msgs.Location;
import big_actor_msgs.Task;
import big_actor_msgs.Vehicle;

import org.apache.commons.lang.math.RandomUtils;
import org.ros.node.ConnectedNode;

import java.util.List;

/**
 * VehicleData
 */
public class VehicleData implements Updatable
{
    private static final int MAXIMUM_WAIT_TIME_FOR_NEW_TASK = 15000;
    private static volatile int taskCounter = 0;

    private Vehicle vehicle;

    private ConnectedNode node;
    private LatLngAlt startPosition;
    private LatLngAlt targetPosition;
    private double velocity = 15;
    private long startTime;
    private long endTime;
    private boolean busy = false;
    private float heading = 0;
    private LatLngAlt currentPosition;
    private Clock clock;
    private double minLat = 90;
    private double minLng = 180;
    private double maxLat = -90;
    private double maxLng = -180;
    private double minAlt = Double.MAX_VALUE;
    private double maxAlt = Double.MIN_VALUE;

    private double distance;
    private WGS84 geodeticSystem = new WGS84();

    private CartesianCoordinate distanceVector;

    private CartesianCoordinate cartesianStarPosition;
    
    private int currentTaskId = 0;
    private int newTaskId = 0;

    /**
     * @param configuration configuration
     * @param vehicle vehicle
     */
    public VehicleData(Configuration configuration, Vehicle vehicle)
    {
        this.node = configuration.getNode();
        this.clock = configuration.getClock();
        this.vehicle = vehicle;

        startPosition = node.getTopicMessageFactory().newFromType(LatLngAlt._TYPE);
        startPosition.setLatitude(vehicle.getPosition().getLatitude());
        startPosition.setLongitude(vehicle.getPosition().getLongitude());
        startPosition.setAltitude(vehicle.getPosition().getAltitude());

        targetPosition = node.getTopicMessageFactory().newFromType(LatLngAlt._TYPE);
        targetPosition.setLatitude(vehicle.getPosition().getLatitude());
        targetPosition.setLongitude(vehicle.getPosition().getLongitude());
        targetPosition.setAltitude(vehicle.getPosition().getAltitude());

        currentPosition = node.getTopicMessageFactory().newFromType(LatLngAlt._TYPE);
        currentPosition.setLatitude(vehicle.getPosition().getLatitude());
        currentPosition.setLongitude(vehicle.getPosition().getLongitude());
        currentPosition.setAltitude(vehicle.getPosition().getAltitude());

        velocity = 10;
        startTime = clock.currentTimeMillis();
        endTime = clock.currentTimeMillis();

        for (Location location : configuration.getLocations())
        {
            List<LatLng> boundaries = location.getBoundaries();
            for (LatLng position : boundaries)
            {
                if (position.getLatitude() < minLat)
                {
                    minLat = position.getLatitude();
                }

                if (position.getLatitude() > maxLat)
                {
                    maxLat = position.getLatitude();
                }

                if (position.getLongitude() < minLng)
                {
                    minLng = position.getLongitude();
                }

                if (position.getLongitude() > maxLng)
                {
                    maxLng = position.getLongitude();
                }
            }

            if (location.getMaximumAltitude() > maxAlt)
            {
                maxAlt = location.getMaximumAltitude();
            }

            if (location.getMinimumAltitude() < minAlt)
            {
                minAlt = location.getMinimumAltitude();
            }
        }
    }

    /**
     * @return the associated vehicle.
     */
    public Vehicle getVehicle()
    {
        vehicle.setHeading(heading);
        vehicle.setPosition(currentPosition);
        vehicle.setTimeStamp(System.currentTimeMillis());
        vehicle.setTaskId(currentTaskId);
        vehicle.setTaskState(busy ? Task.TS_IN_PROGRESS : Task.TS_NONE);
        vehicle.setVehicleState(busy ? Vehicle.VS_BUSY : Vehicle.VS_IDLE);
        return vehicle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update()
    {
        if (startTime >= clock.currentTimeMillis())
        {
            // do nothing, wait for new task.
            node.getLog().debug("Waiting for new task.");
            busy = false;
            
        }
        else if (endTime >= clock.currentTimeMillis())
        {
            busy = true;
            currentTaskId = newTaskId;
            
            double completion = 1.0 - (endTime - clock.currentTimeMillis()) / (double) (endTime - startTime);
            CartesianCoordinate cp =
                distanceVector.multiply(completion * completion * (3 - 2 * completion)).add(cartesianStarPosition);
            geodeticSystem.rectangularToPolarCoordinates(cp).fill(currentPosition);

            node.getLog().info(
                String.format("VehicleData.update() %f %d %d", completion, (endTime - clock.currentTimeMillis()),
                    (endTime - startTime)));
        }
        else
        {
            newTaskId = incrementTaskCounter();
            currentTaskId = 0;
            
            node.getLog().info("Generate new task. " + newTaskId);
            startPosition = currentPosition;
            startTime = clock.currentTimeMillis() + (long) (MAXIMUM_WAIT_TIME_FOR_NEW_TASK * RandomUtils.nextDouble());
            busy = false;

            double latitude = minLat + (maxLat - minLat) * RandomUtils.nextDouble();
            double longitude = minLng + (maxLng - minLng) * RandomUtils.nextDouble();
            double altitude = minAlt + (maxAlt - minAlt) * RandomUtils.nextDouble();

            targetPosition.setLatitude(latitude);
            targetPosition.setLongitude(longitude);
            targetPosition.setAltitude(altitude);

            PolarCoordinate s = new PolarCoordinate(startPosition);
            PolarCoordinate t = new PolarCoordinate(targetPosition);

            cartesianStarPosition = geodeticSystem.polarToRectangularCoordinates(s);
            CartesianCoordinate target = geodeticSystem.polarToRectangularCoordinates(t);
            distanceVector = target.subtract(cartesianStarPosition);
            distance = distanceVector.norm();
            endTime = startTime + (long) (1000 * distance / velocity);
        }
    }

    /**
     * @return true if vehicle is busy.
     */
    public boolean isBusy()
    {
        return busy;
    }
    
    private int incrementTaskCounter() {
        synchronized (VehicleData.class)
        {
            ++taskCounter;
        }
        return taskCounter;
    }
}
