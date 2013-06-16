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

import big_actor_msgs.Vehicle;

import org.apache.commons.lang.math.RandomUtils;
import org.ros.node.topic.Publisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generator Publisher
 */
public class GeneratorPublisher extends AbstractPublisher
{
    private Publisher<big_actor_msgs.StructureStateEstimate> ssePublisher;
    private Publisher<big_actor_msgs.MissionStateEstimate> msePublisher;
    
    private LocationArtificer locationArtificer;
    private VehicleArtificer vehicleArtificer;
    private ConnectionArtificer connectionArtificer;
    private TaskArtificer taskArtificer;
    private List<Long> vehicleIdList = new ArrayList<Long>();
    
    /**
     * @param configuration configuration
     */
    public GeneratorPublisher(Configuration configuration)
    {
        setConfiguration(configuration);
        locationArtificer = new LocationArtificer(configuration);
        vehicleArtificer = new VehicleArtificer(configuration);
        connectionArtificer = new ConnectionArtificer(configuration);
        connectionArtificer.setVehicleArtificer(vehicleArtificer);
        taskArtificer = new TaskArtificer(configuration);

        setArtificers(Arrays.asList(new Artificer[]{locationArtificer, vehicleArtificer, connectionArtificer,
            taskArtificer}));
        
        for (Vehicle v : configuration.getVehicles())
        {
            vehicleIdList.add(Long.valueOf(v.getVehicleId()));
        }
    }
    
    /**
     * @param ssePublisher ssePublisher
     */
    public void setSsePublisher(Publisher<big_actor_msgs.StructureStateEstimate> ssePublisher)
    {
        this.ssePublisher = ssePublisher;
    }
    
    /**
     * @param msePublisher msePublisher
     */
    public void setMsePublisher(Publisher<big_actor_msgs.MissionStateEstimate> msePublisher)
    {
        this.msePublisher = msePublisher;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loop() throws InterruptedException
    {
        getConfiguration().getNode().getLog().info("GeneratorPublisher.loop()");
        long vehicleId = getRandomVehicleId();
        
        if (msePublisher != null)
        {
            big_actor_msgs.MissionStateEstimate mse = msePublisher.newMessage();
            mse.setTimeStamp(getConfiguration().getClock().currentTimeMillis());
            mse.setSrcVehicleId(vehicleId);
            mse.setTasks(taskArtificer.currentTasks());
            msePublisher.publish(mse);
        }
        
        if (ssePublisher != null)
        {
            big_actor_msgs.StructureStateEstimate sse = ssePublisher.newMessage();
            sse.setTimeStamp(getConfiguration().getClock().currentTimeMillis());
            sse.setSrcVehicleId(vehicleId);
            sse.setLocations(locationArtificer.currentLocations());
            sse.setVehicles(vehicleArtificer.currentVehicles());
            sse.setConnections(connectionArtificer.currentConnections());
            ssePublisher.publish(sse);
        }
        
        Sleeper.sleep(1000);
    }
    
    /**
     * @return the randomly generated vehicle id.
     */
    private long getRandomVehicleId()
    {
        int index = (int)(vehicleIdList.size() * RandomUtils.nextDouble()) % vehicleIdList.size();
        return vehicleIdList.get(index);
    }


}
