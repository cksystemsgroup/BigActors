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

import big_actor_msgs.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * VehicleArtificer
 */
public class VehicleArtificer extends Artificer
{
    private List<VehicleData> vehicleData;
    private Configuration configuration;

    /**
     * @param configuration configuration
     */
    public VehicleArtificer(Configuration configuration)
    {
        this.configuration = configuration;
        init();
    }

    private void init()
    {
        vehicleData = new ArrayList<VehicleData>();

        for (Vehicle vehicle : configuration.getVehicles())
        {
            VehicleData vd = new VehicleData(configuration, vehicle);
            vehicleData.add(vd);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        configuration.getNode().getLog().info("VehicleArtificer.execute()");

        for (VehicleData vd : vehicleData)
        {
            vd.update();
        }
    }

    /**
     * @return the current vehicles
     */
    public List<Vehicle> currentVehicles()
    {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        
        for (VehicleData vd : vehicleData)
        {
            vehicles.add(vd.getVehicle());
        }
        
        return vehicles;
    }

}
