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
import at.uni_salzburg.cs.ros.artificer.TaskArtificer;
import at.uni_salzburg.cs.ros.artificer.VehicleArtificer;

import org.ros.concurrent.CancellableLoop;
import org.ros.node.topic.Publisher;

import java.util.Timer;

/**
 * MSE Publisher
 */
public class MsePublisher extends CancellableLoop
{
    private static final int ARTIFICER_WAIT_PERIOD = 1000;

    private static final long SCHEDULE_SETUP_DELAY = 0;
    
    private static final long SCHEDULE_CYCLE = 1000;

    private Publisher<big_actor_msgs.MissionStateEstimate> publisher;

    private ConnectionArtificer connectionArtificer;
    private LocationArtificer locationArtificer;
    private TaskArtificer taskArtificer;
    private VehicleArtificer vehicleArtificer;
    private Timer timer = new Timer();

    private Configuration configuration;

    /**
     * @param publisher publisher
     * @param configuration configuration
     */
    public MsePublisher(Publisher<big_actor_msgs.MissionStateEstimate> publisher, Configuration configuration)
    {
        this.publisher = publisher;
        this.configuration = configuration;
        connectionArtificer = new ConnectionArtificer(configuration);
        locationArtificer = new LocationArtificer(configuration);
        taskArtificer = new TaskArtificer(configuration);
        vehicleArtificer = new VehicleArtificer(configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setup()
    {
        configuration.getNode().getLog().info("MsePublisher.setup()");
        timer.schedule(connectionArtificer, SCHEDULE_SETUP_DELAY, SCHEDULE_CYCLE);
        timer.schedule(locationArtificer, SCHEDULE_SETUP_DELAY, SCHEDULE_CYCLE);
        timer.schedule(taskArtificer, SCHEDULE_SETUP_DELAY, SCHEDULE_CYCLE);
        timer.schedule(vehicleArtificer, SCHEDULE_SETUP_DELAY, SCHEDULE_CYCLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loop() throws InterruptedException
    {
        configuration.getNode().getLog().info("MsePublisher.loop()");
        big_actor_msgs.MissionStateEstimate mse = publisher.newMessage();
        mse.setTimeStamp(System.currentTimeMillis());
        mse.setConnections(connectionArtificer.currentConnections());
        mse.setLocations(locationArtificer.currentLocations());
        mse.setTasks(taskArtificer.currentTasks());
        mse.setVehicles(vehicleArtificer.currentVehicles());
        mse.setSrcVehicleId(3);
        publisher.publish(mse);
        Thread.sleep(1000);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel()
    {
        configuration.getNode().getLog().info("MsePublisher.cancel()");
        super.cancel();

        timer.cancel();

        boolean runningArtificers;
        do
        {
            runningArtificers = false;
            for (Artificer artificer : new Artificer[]{connectionArtificer, locationArtificer, taskArtificer,
                vehicleArtificer})
            {
                if (artificer.isActive())
                {
                    runningArtificers = true;
                    Sleeper.sleep(ARTIFICER_WAIT_PERIOD);
                    break;
                }
            }
        } while (runningArtificers);
    }

}
