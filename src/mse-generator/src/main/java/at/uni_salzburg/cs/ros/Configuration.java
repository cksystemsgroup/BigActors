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

import at.uni_salzburg.cs.ros.artificer.Clock;

import org.ros.node.ConnectedNode;

import big_actor_msgs.Location;
import big_actor_msgs.Network;
import big_actor_msgs.Vehicle;

import java.util.List;
import java.util.Map;

/**
 * Configuration
 */
public interface Configuration
{
    /**
     * @return the clock implementation.
     */
    Clock getClock();
    
    /**
     * @return the connected node.
     */
    ConnectedNode getNode();
    
    /**
     * @return the configured locations.
     */
    List<Location> getLocations();

    /**
     * @return location simulation details.
     */
    Map<Integer, LocationSimulationDetails> getLocationSimulationDetails();

    /**
     * @return the configured vehicles.
     */
    List<Vehicle> getVehicles();

    /**
     * @return vehicle simulation details.
     */
    Map<Long, VehicleSimulationDetails> getVehicleSimulationDetails();

    /**
     * @return the configured networks
     */
    List<Network> getNetworks();

    /**
     * @return network simulation details
     */
    Map<Byte, Map<Integer, NetworkSimulationDetails>> getNetworkSimulationDetails();

}
