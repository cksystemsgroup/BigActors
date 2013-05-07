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

import big_actor_msgs.LatLngAlt;
import big_actor_msgs.Vehicle;

import org.ros.message.Time;
import org.ros.node.ConnectedNode;

/**
 * VehicleData
 */
public class VehicleData implements Updatable
{
    private Vehicle vehicle;

    private ConnectedNode node;
    private LatLngAlt startPosition;
    private LatLngAlt targetPosition;
    private double velocity;
    private long startTime;
    private long endTime;
    private boolean busy = false;
    private float heading = 0;
    private LatLngAlt currentPosition;
    private Clock clock;

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
    }

    /**
     * @return the associated vehicle.
     */
    public Vehicle getVehicle()
    {
        vehicle.setHeading(heading);
        vehicle.setPosition(currentPosition);
        vehicle.setStamp(Time.fromMillis(System.currentTimeMillis()));
        vehicle.setTaskId(0);
        vehicle.setTaskState(endTime >= clock.currentTimeMillis() ? Vehicle.TS_IN_PROGRESS : Vehicle.TS_DONE);
        vehicle.setVehicleState(busy ? Vehicle.VS_BUSY : Vehicle.VS_IDLE);
        return vehicle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update()
    {
        if (endTime >= clock.currentTimeMillis())
        {
            // TODO Auto-generated method stub
            node.getLog().info("VehicleData.update()");

        }
        else
        {
            busy = false;
        }

    }

    /**
     * @return true if vehicle is busy.
     */
    public boolean isBusy()
    {
        return busy;
    }
}
