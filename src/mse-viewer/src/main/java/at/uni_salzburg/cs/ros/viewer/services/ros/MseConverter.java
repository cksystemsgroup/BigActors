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
package at.uni_salzburg.cs.ros.viewer.services.ros;

import big_actor_msgs.Connectivity;
import big_actor_msgs.LatLng;
import big_actor_msgs.LatLngAlt;
import big_actor_msgs.Location;
import big_actor_msgs.MissionStateEstimate;
import big_actor_msgs.Task;
import big_actor_msgs.Vehicle;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import java.util.List;

/**
 * MseConverter
 */
public interface MseConverter
{
    /**
     * @param mse the mission state estimate
     * @return the created JSON object
     */
    JSONObject convertMseToJSON(MissionStateEstimate mse);

    /**
     * @param connections the connection list
     * @return the created JSON object
     */
    JSONArray convertConnectionsToJSON(List<Connectivity> connections);
    
    /**
     * @param c the connectivity
     * @return the created JSON object
     */
    JSONObject convertConnectivityToJSON(Connectivity c);
    
    /**
     * @param locations the location list
     * @return the created JSON object
     */
    JSONArray convertLocationListToJSON(List<Location> locations);

    /**
     * @param location the location
     * @return the created JSON object
     */
    JSONObject convertLocationToJSON(Location location);

    /**
     * @param tasks the task list
     * @return the created JSON object
     */
    JSONArray convertTaskListToJSON(List<Task> tasks);

    /**
     * @param task the task
     * @return the created JSON object
     */
    JSONObject convertTaskToJSON(Task task);

    /**
     * @param vehicles the vehicle list
     * @return the created JSON object
     */
    JSONArray convertVehicleListToJSON(List<Vehicle> vehicles);

    /**
     * @param vehicle the vehicle
     * @return the created JSON object
     */
    JSONObject convertVehicleToJSON(Vehicle vehicle);

    /**
     * @param boundaries the boundary coordinates
     * @return  the created JSON object
     */
    JSONArray convertLatLngListToJSON(List<LatLng> boundaries);

    /**
     * @param position the position
     * @return the created JSON object
     */
    JSONObject convertLatLngToJSON(LatLng position);

    /**
     * @param position the position
     * @return the created JSON object 
     */
    JSONObject convertLatLngAltToJSON(LatLngAlt position);
}
