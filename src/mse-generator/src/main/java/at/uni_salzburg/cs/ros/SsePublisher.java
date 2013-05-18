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
package at.uni_salzburg.cs.ros;

import at.uni_salzburg.cs.ros.artificer.Artificer;
import at.uni_salzburg.cs.ros.artificer.ConnectionArtificer;
import at.uni_salzburg.cs.ros.artificer.LocationArtificer;
import at.uni_salzburg.cs.ros.artificer.VehicleArtificer;

import org.ros.node.topic.Publisher;

import java.util.Arrays;

/**
 * SSE Publisher
 */
public class SsePublisher extends AbstractPublisher
{
    private Publisher<big_actor_msgs.StructureStateEstimate> publisher;

    private ConnectionArtificer connectionArtificer;
    private LocationArtificer locationArtificer;
    private VehicleArtificer vehicleArtificer;

    /**
     * @param publisher publisher
     * @param configuration configuration
     */
    public SsePublisher(Publisher<big_actor_msgs.StructureStateEstimate> publisher, Configuration configuration)
    {
        this.publisher = publisher;
        connectionArtificer = new ConnectionArtificer(configuration);
        locationArtificer = new LocationArtificer(configuration);
        vehicleArtificer = new VehicleArtificer(configuration);
        setArtificers(Arrays.asList(new Artificer[] {connectionArtificer, locationArtificer, vehicleArtificer}));
        setConfiguration(configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loop() throws InterruptedException
    {
        getConfiguration().getNode().getLog().info("SsePublisher.loop()");
        big_actor_msgs.StructureStateEstimate mse = publisher.newMessage();
        mse.setTimeStamp(System.currentTimeMillis());
        mse.setConnections(connectionArtificer.currentConnections());
        mse.setLocations(locationArtificer.currentLocations());
        mse.setVehicles(vehicleArtificer.currentVehicles());
        mse.setSrcVehicleId(3);
        publisher.publish(mse);
        Thread.sleep(1000);
    }


}
