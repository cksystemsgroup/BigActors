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

import at.uni_salzburg.cs.ckgroup.course.CartesianCoordinate;
import at.uni_salzburg.cs.ckgroup.course.IGeodeticSystem;
import at.uni_salzburg.cs.ckgroup.course.WGS84;
import at.uni_salzburg.cs.ros.Configuration;
import at.uni_salzburg.cs.ros.NetworkSimulationDetails;

import org.apache.commons.lang.math.RandomUtils;

import big_actor_msgs.Connectivity;
import big_actor_msgs.LatLngAlt;
import big_actor_msgs.Network;
import big_actor_msgs.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ConnectionArtificer
 */
public class ConnectionArtificer extends Artificer
{
    private Configuration configuration;
    
    private VehicleArtificer vehicleArtificer;
    
    private IGeodeticSystem geodeticSystem = new WGS84();
    
    private List<Connectivity> connections = new ArrayList<Connectivity>();
    
    /**
     * @param configuration 
     */
    public ConnectionArtificer(Configuration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        configuration.getNode().getLog().info("ConnectionArtificer.execute()");

        Map<Byte, Map<Integer, NetworkSimulationDetails>> simDetails = configuration.getNetworkSimulationDetails();
        List<Network> networks = configuration.getNetworks();
        List<Vehicle> vehicles = vehicleArtificer.currentVehicles();
        List<Connectivity> cons = new ArrayList<Connectivity>();

        for (Network net : networks)
        {
            List<Vehicle> possiblyConnectedVehicles = new ArrayList<Vehicle>();
            for (Vehicle v : vehicles)
            {
                for (Network vehNet : v.getNetworks())
                {
                    if (vehNet.getType() == net.getType() && vehNet.getAddress() == net.getAddress())
                    {
                        possiblyConnectedVehicles.add(v);
                    }
                }
            }

            NetworkSimulationDetails vsd = simDetails.get(net.getType()).get(net.getAddress());

            for (int k = 0, l = possiblyConnectedVehicles.size(); k < l; ++k)
            {
                Vehicle v1 = possiblyConnectedVehicles.get(k);
                if (v1.getVehicleType() == Vehicle.VT_DRIFTER || v1.getVehicleType() == Vehicle.VT_NONE)
                {
                    continue;
                }
                for (int j = 0; j < l; ++j)
                {
                    if (k == j)
                    {
                        continue;
                    }
                    Vehicle v2 = possiblyConnectedVehicles.get(j);
                    double distance = calculateDistance(v1, v2);
                    double range = vsd.getRadius() + 2.0 * (RandomUtils.nextDouble() - 0.5) * vsd.getFluctuation();
                    if (distance <= range)
                    {
                        Connectivity con =
                            configuration.getNode().getTopicMessageFactory().newFromType(Connectivity._TYPE);
                        con.setTimeStamp(configuration.getClock().currentTimeMillis());
                        con.setSrcVehicleId(v1.getVehicleId());
                        con.setDstVehicleId(v2.getVehicleId());
                        con.setNetwork(net);
                        cons.add(con);
                    }
                }
            }
        }
        
        connections = cons;
    }

    private double calculateDistance(Vehicle a, Vehicle b)
    {
        LatLngAlt posA = a.getPosition();
        LatLngAlt posB = b.getPosition();

        CartesianCoordinate recA =
            geodeticSystem.polarToRectangularCoordinates(posA.getLatitude(), posA.getLongitude(), posA.getAltitude());
        CartesianCoordinate recB =
            geodeticSystem.polarToRectangularCoordinates(posB.getLatitude(), posB.getLongitude(), posB.getAltitude());

        return recA.subtract(recB).norm();
    }

    /**
     * @return the current connections
     */
    public List<Connectivity> currentConnections()
    {
        return connections;
    }

    /**
     * @param vehicleArtificer the current vehicle artificer.
     */
    public void setVehicleArtificer(VehicleArtificer vehicleArtificer)
    {
        this.vehicleArtificer = vehicleArtificer;
    }
}
