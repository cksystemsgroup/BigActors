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
import org.ros.message.Time;

import std_msgs.Header;

import java.util.List;

public class MseConverterImpl implements MseConverter
{
    @Override
    public JSONObject convertMseToJSON(MissionStateEstimate mse)
    {
        JSONObject o = new JSONObject();
        o.put("header", convertHeaderToJSON(mse.getHeader()));
        o.put("srcVehicleId", mse.getSrcVehicleId());
        o.put("connections", convertConnectionsToJSON(mse.getConnections()));
        o.put("locations", convertLocationListToJSON(mse.getLocations()));
        o.put("tasks", convertTaskListToJSON(mse.getTasks()));
        o.put("vehicles", convertVehicleListToJSON(mse.getVehicles()));
        return o;
    }

    @Override
    public JSONObject convertHeaderToJSON(Header header)
    {
        JSONObject o = new JSONObject();
        o.put("stamp", convertStampToJSON(header.getStamp()));
        return o;
    }

    @Override
    public JSONArray convertConnectionsToJSON(List<Connectivity> connections)
    {
        JSONArray a = new JSONArray();
        for (Connectivity c : connections)
        {
            a.put(convertConnectivityToJSON(c));
        }
        return a;
    }

    @Override
    public JSONObject convertConnectivityToJSON(Connectivity c)
    {
        JSONObject o = new JSONObject();
        o.put("dstVehicleId", c.getDstVehicleId());
        o.put("srcVehicleId", c.getSrcVehicleId());
        o.put("type", c.getType());
        o.put("stamp", convertStampToJSON(c.getStamp()));
        return o;
    }

    @Override
    public JSONArray convertLocationListToJSON(List<Location> locations)
    {
        JSONArray a = new JSONArray();
        for (Location l : locations)
        {
            a.put(convertLocationToJSON(l));
        }
        return a;
    }

    @Override
    public JSONObject convertLocationToJSON(Location location)
    {
        JSONObject o = new JSONObject();
        o.put("locationId", location.getLocationId());
        o.put("minimumAltitude", location.getMinimumAltitude());
        o.put("maximumAltitude", location.getMaximumAltitude());
        o.put("boundaries", convertLatLngListToJSON(location.getBoundaries()));
        o.put("stamp", convertStampToJSON(location.getStamp()));
        return o;
    }

    @Override
    public JSONArray convertTaskListToJSON(List<Task> tasks)
    {
        JSONArray a = new JSONArray();
        for (Task t : tasks)
        {
            a.put(convertTaskToJSON(t));
        }
        return a;
    }

    @Override
    public JSONObject convertTaskToJSON(Task task)
    {
        JSONObject o = new JSONObject();
        o.put("taskId", task.getTaskId());
        o.put("action", task.getAction());
        o.put("position", convertLatLngAltToJSON(task.getPosition()));
        return o;
    }

    @Override
    public JSONArray convertVehicleListToJSON(List<Vehicle> vehicles)
    {
        JSONArray a = new JSONArray();
        for (Vehicle v : vehicles)
        {
            a.put(convertVehicleToJSON(v));
        }
        return a;
    }

    @Override
    public JSONObject convertVehicleToJSON(Vehicle vehicle)
    {
        JSONObject o = new JSONObject();
        o.put("vehicleId", vehicle.getVehicleId());
        o.put("vehicleState", vehicle.getVehicleState());
        o.put("heading", vehicle.getHeading());
        o.put("position", convertLatLngAltToJSON(vehicle.getPosition()));
        o.put("taskId", vehicle.getTaskId());
        o.put("taskState", vehicle.getTaskState());
        o.put("stamp", convertStampToJSON(vehicle.getStamp()));
        return o;
    }

    @Override
    public JSONObject convertStampToJSON(Time time)
    {
        JSONObject o = new JSONObject();
        o.put("secs", time.secs);
        o.put("nsecs", time.nsecs);
        return o;
    }

    @Override
    public JSONArray convertLatLngListToJSON(List<LatLng> boundaries)
    {
        JSONArray a = new JSONArray();
        for (LatLng position : boundaries)
        {
            a.put(convertLatLngToJSON(position));
        }
        return a;
    }

    @Override
    public JSONObject convertLatLngToJSON(LatLng position)
    {
        JSONObject o = new JSONObject();
        o.put("lat", position.getLatitude());
        o.put("lon", position.getLongitude());
        return o;
    }

    @Override
    public JSONObject convertLatLngAltToJSON(LatLngAlt position)
    {
        JSONObject o = new JSONObject();
        o.put("lat", position.getLatitude());
        o.put("lon", position.getLongitude());
        o.put("alt", position.getAltitude());
        return o;
    }
}
