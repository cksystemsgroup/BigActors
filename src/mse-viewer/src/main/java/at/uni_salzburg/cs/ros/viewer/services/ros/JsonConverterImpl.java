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
import big_actor_msgs.StructureStateEstimate;
import big_actor_msgs.Task;
import big_actor_msgs.Vehicle;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MseConverterImpl
 */
public class JsonConverterImpl implements JsonConverter
{
    @SuppressWarnings("serial")
    private static final Map<Byte, String> TASK_STATE_MAP = new HashMap<Byte, String>()
    {
        {
            put(big_actor_msgs.Task.TS_NONE, "none");
            put(big_actor_msgs.Task.TS_TODO, "todo");
            put(big_actor_msgs.Task.TS_IN_PROGRESS, "inProgress");
            put(big_actor_msgs.Task.TS_DONE, "done");
            put(big_actor_msgs.Task.TS_ASSIGNED, "assigned");
            put(big_actor_msgs.Task.TS_CANCELED, "canceled");
        }
    };

    @SuppressWarnings("serial")
    private static final Map<Byte, String> VEHICLE_STATE_MAP = new HashMap<Byte, String>()
    {
        {
            put(big_actor_msgs.Vehicle.VS_NONE, "none");
            put(big_actor_msgs.Vehicle.VS_IDLE, "idle");
            put(big_actor_msgs.Vehicle.VS_BUSY, "busy");
        }
    };

    @SuppressWarnings("serial")
    private static final Map<Byte, String> VEHICLE_TYPE_MAP = new HashMap<Byte, String>()
    {
        {
            put(big_actor_msgs.Vehicle.VT_NONE, "none");
            put(big_actor_msgs.Vehicle.VT_QUADROTOR, "quadrotor");
            put(big_actor_msgs.Vehicle.VT_VESSEL, "vessel");
            put(big_actor_msgs.Vehicle.VT_DRIFTER, "drifter");
            put(big_actor_msgs.Vehicle.VT_MODEL_AIRPLANE, "modelAirPlane");
        }
    };

    @SuppressWarnings("serial")
    private static final Map<Byte, String> LOCATION_TYPE_MAP = new HashMap<Byte, String>()
    {
        {
            put(big_actor_msgs.Location.LT_NONE, "none");
            put(big_actor_msgs.Location.LT_SURFACE, "surface");
            put(big_actor_msgs.Location.LT_AIR_SPACE, "airSpace");
            put(big_actor_msgs.Location.LT_UNDERWATER, "underWater");
            put(big_actor_msgs.Location.LT_OIL_SPILL, "oilSpill");
        }
    };
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject convertMseAndSseToJSON(MissionStateEstimate mse, StructureStateEstimate sse)
    {
        JSONObject o = new JSONObject();
        o.put("timeStamp", mse.getTimeStamp());
        o.put("srcVehicleId", mse.getSrcVehicleId());
        o.put("connections", convertConnectionsToJSON(sse.getConnections()));
        o.put("locations", convertLocationListToJSON(sse.getLocations()));
        o.put("tasks", convertTaskListToJSON(mse.getTasks()));
        o.put("vehicles", convertVehicleListToJSON(sse.getVehicles()));
        return o;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject convertConnectivityToJSON(Connectivity c)
    {
        JSONObject o = new JSONObject();
        o.put("dstVehicleId", c.getDstVehicleId());
        o.put("srcVehicleId", c.getSrcVehicleId());
        o.put("type", c.getType());
        o.put("timeStamp", c.getTimeStamp());
        return o;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject convertLocationToJSON(Location location)
    {
        JSONObject o = new JSONObject();
        o.put("locationId", location.getLocationId());
        o.put("name", location.getName());
        o.put("locationType", LOCATION_TYPE_MAP.get(location.getLocationType()));
        o.put("minimumAltitude", location.getMinimumAltitude());
        o.put("maximumAltitude", location.getMaximumAltitude());
        o.put("boundaries", convertLatLngListToJSON(location.getBoundaries()));
        o.put("timeStamp", location.getTimeStamp());
        return o;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject convertTaskToJSON(Task task)
    {
        JSONObject o = new JSONObject();
        o.put("taskId", task.getTaskId());
        o.put("taskType", task.getTaskType());
        o.put("status", TASK_STATE_MAP.get(task.getStatus()));
        o.put("creationStamp", task.getCreationStamp());
        o.put("taskStamp", task.getTaskStamp());
        o.put("vehicleId", task.getVehicleId());
        o.put("parameters", task.getParameters());
        return o;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject convertVehicleToJSON(Vehicle vehicle)
    {
        JSONObject o = new JSONObject();
        o.put("vehicleId", vehicle.getVehicleId());
        o.put("name", vehicle.getName());
        o.put("vehicleState", VEHICLE_STATE_MAP.get(vehicle.getVehicleState()));
        o.put("vehicleType", VEHICLE_TYPE_MAP.get(vehicle.getVehicleType()));
        o.put("heading", vehicle.getHeading());
        o.put("position", convertLatLngAltToJSON(vehicle.getPosition()));
        o.put("taskId", vehicle.getTaskId());
        o.put("taskState", vehicle.getTaskState());
        o.put("timeStamp", vehicle.getTimeStamp());
        return o;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject convertLatLngToJSON(LatLng position)
    {
        JSONObject o = new JSONObject();
        o.put("lat", position.getLatitude());
        o.put("lon", position.getLongitude());
        return o;
    }

    /**
     * {@inheritDoc}
     */
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
