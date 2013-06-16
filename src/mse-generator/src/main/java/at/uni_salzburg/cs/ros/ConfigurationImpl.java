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

import big_actor_msgs.LatLng;
import big_actor_msgs.LatLngAlt;
import big_actor_msgs.Location;
import big_actor_msgs.Network;
import big_actor_msgs.Vehicle;

import org.ros.node.ConnectedNode;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Configuration Implementation
 */
public class ConfigurationImpl implements Configuration
{
    private ConnectedNode node;

    private Clock clock;

    private List<Location> locations;
    
    private Map<Integer, LocationSimulationDetails> locationSimulationDetails;

    private List<Vehicle> vehicles;
    
    private Map<Long, VehicleSimulationDetails> vehicleSimulationDetails;

    private List<Network> networks;
    
    private Map<Byte, Map<Integer, NetworkSimulationDetails>> networkSimulationDetails;

    /**
     * @param node connected node
     * @param configFile configuration file
     * @throws IOException thrown in case of errors
     */
    public ConfigurationImpl(ConnectedNode node, InputStream configFile) throws IOException
    {
        this.node = node;
        try
        {
            parseConfigFile(configFile);
        }
        catch (SAXException e)
        {
            throw new IOException("SAX", e);
        }
        catch (ParserConfigurationException e)
        {
            throw new IOException("ParserConfiguration", e);
        }
        catch (NumberFormatException e)
        {
            throw new IOException("NumberFormat", e);
        }
    }

    /**
     * @param xmlStream xmlStream
     * @throws SAXException thrown in case of errors
     * @throws IOException thrown in case of errors
     * @throws ParserConfigurationException thrown in case of errors
     */
    private void parseConfigFile(InputStream xmlStream) throws SAXException, IOException, ParserConfigurationException
    {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        Document document = f.newDocumentBuilder().parse(xmlStream);

        NodeList childNodes = document.getChildNodes();

        for (int k = 0, l = childNodes.getLength(); k < l; ++k)
        {
            Node child = childNodes.item(k);
            if ("config".equals(child.getNodeName()))
            {
                parseConfigNode(child.getChildNodes());
            }
        }
    }

    /**
     * @param nodes nodes
     * @throws IOException thrown in case of errors
     */
    private void parseConfigNode(NodeList nodes) throws IOException
    {
        locations = new ArrayList<Location>();
        locationSimulationDetails = new HashMap<Integer, LocationSimulationDetails>();
        vehicles = new ArrayList<Vehicle>();
        vehicleSimulationDetails = new HashMap<Long, VehicleSimulationDetails>();
        networks = new ArrayList<Network>();
        networkSimulationDetails = new HashMap<Byte, Map<Integer,NetworkSimulationDetails>>();
        clock = null;

        for (int k = 0, l = nodes.getLength(); k < l; ++k)
        {
            Node child = nodes.item(k);
            String nodeName = child.getNodeName();
            child.getNodeValue();
            child.getNodeType();
            if ("locations".equals(nodeName))
            {
                parseLocationsNode(child.getChildNodes());
            }
            else if ("vehicles".equals(nodeName))
            {
                parseVehiclesNode(child.getChildNodes());
            }
            else if ("clock".equals(nodeName))
            {
                parseClockNode(child);
            }
            else if ("networks".equals(nodeName))
            {
                List<Network> nets = parseNetworksNode(child.getChildNodes());
                networks.addAll(nets);
            }
        }
    }

    /**
     * @param locationNodes locationNodes
     */
    private void parseLocationsNode(NodeList locationNodes)
    {
        for (int k = 0, l = locationNodes.getLength(); k < l; ++k)
        {
            Node child = locationNodes.item(k);
            if ("location".equals(child.getNodeName()))
            {
                parseOneLocationNode(child);
            }
        }
    }

    /**
     * @param locationNode locationNode
     * @return location
     */
    private void parseOneLocationNode(Node locationNode)
    {
        Location location = node.getTopicMessageFactory().newFromType(Location._TYPE);

        NamedNodeMap attributes = locationNode.getAttributes();
        location.setLocationId(Integer.parseInt(attributes.getNamedItem("id").getNodeValue()));
        location.setName(attributes.getNamedItem("name").getNodeValue());
        location.setTimeStamp(Integer.parseInt(attributes.getNamedItem("timeStamp").getNodeValue()));
        location.setMaximumAltitude(Float.parseFloat(attributes.getNamedItem("maxAltitude").getNodeValue()));
        location.setMinimumAltitude(Float.parseFloat(attributes.getNamedItem("minAltitude").getNodeValue()));
        location.setLocationType(Byte.parseByte(attributes.getNamedItem("type").getNodeValue()));

        LocationSimulationDetails simDetail = new LocationSimulationDetails();
        simDetail.setAverageSpeed(Double.parseDouble(attributes.getNamedItem("avgSpeed").getNodeValue()));
        simDetail.setMutationRate(Double.parseDouble(attributes.getNamedItem("mutRate").getNodeValue()));
        locationSimulationDetails.put(Integer.valueOf(location.getLocationId()), simDetail);

        List<LatLng> boundaries = new ArrayList<LatLng>();
        for (int k = 0, l = locationNode.getChildNodes().getLength(); k < l; ++k)
        {
            Node child = locationNode.getChildNodes().item(k);
            if ("boundaries".equals(child.getNodeName()))
            {
                List<LatLng> bnds = parseBoundariesNode(child.getChildNodes());
                boundaries.addAll(bnds);
            }
            else if ("limits".equals(child.getNodeName()))
            {
                parseLimitsNode(simDetail, child.getChildNodes());
            }
        }
        location.setBoundaries(boundaries);

        locations.add(location);
    }

    /**
     * @param simDetail the location simulation details
     * @param nodes the nodes to be parsed
     */
    private void parseLimitsNode(LocationSimulationDetails simDetail, NodeList nodes)
    {
        for (int k = 0, l = nodes.getLength(); k < l; ++k)
        {
            Node child = nodes.item(k);
            if ("latitude".equals(child.getNodeName()))
            {
                String mn = child.getAttributes().getNamedItem("min").getNodeValue();
                String mx = child.getAttributes().getNamedItem("max").getNodeValue();
                simDetail.setMinLatitude(Double.parseDouble(mn));
                simDetail.setMaxLatitude(Double.parseDouble(mx));
            }
            else if ("longitude".equals(child.getNodeName()))
            {
                String mn = child.getAttributes().getNamedItem("min").getNodeValue();
                String mx = child.getAttributes().getNamedItem("max").getNodeValue();
                simDetail.setMinLongitude(Double.parseDouble(mn));
                simDetail.setMaxLongitude(Double.parseDouble(mx));
            }
        }
    }

    /**
     * @param pointNodes pointNodes
     * @return boundaries
     */
    private List<LatLng> parseBoundariesNode(NodeList pointNodes)
    {
        List<LatLng> boundaries = new ArrayList<LatLng>();

        for (int k = 0, l = pointNodes.getLength(); k < l; ++k)
        {
            Node child = pointNodes.item(k);
            if ("point".equals(child.getNodeName()))
            {
                LatLng position = node.getTopicMessageFactory().newFromType(LatLng._TYPE);
                NamedNodeMap attributes = child.getAttributes();
                position.setLatitude(Double.parseDouble(attributes.getNamedItem("latitude").getNodeValue()));
                position.setLongitude(Double.parseDouble(attributes.getNamedItem("longitude").getNodeValue()));
                boundaries.add(position);
            }
        }

        return boundaries;
    }

    /**
     * @param vehicleNodes vehicleNodes
     */
    private void parseVehiclesNode(NodeList vehicleNodes)
    {
        for (int k = 0, l = vehicleNodes.getLength(); k < l; ++k)
        {
            Node child = vehicleNodes.item(k);
            if ("vehicle".equals(child.getNodeName()))
            {
                parseOneVehicle(child);
            }
        }
    }

    /**
     * @param vehicleNode vehicleNode
     */
    private void parseOneVehicle(Node vehicleNode)
    {
        Vehicle vehicle = node.getTopicMessageFactory().newFromType(Vehicle._TYPE);
        NamedNodeMap attributes = vehicleNode.getAttributes();
        vehicle.setVehicleId(Integer.parseInt(attributes.getNamedItem("id").getNodeValue()));
        vehicle.setName(attributes.getNamedItem("name").getNodeValue());
        LatLngAlt position = node.getTopicMessageFactory().newFromType(LatLngAlt._TYPE);
        position.setLatitude(Double.parseDouble(attributes.getNamedItem("latitude").getNodeValue()));
        position.setLongitude(Double.parseDouble(attributes.getNamedItem("longitude").getNodeValue()));
        position.setAltitude(Double.parseDouble(attributes.getNamedItem("altitude").getNodeValue()));
        vehicle.setPosition(position);
        vehicle.setVehicleType(Byte.parseByte(attributes.getNamedItem("type").getNodeValue()));
        vehicles.add(vehicle);

        VehicleSimulationDetails simDetail = new VehicleSimulationDetails();
        simDetail.setAverageSpeed(Double.parseDouble(attributes.getNamedItem("avgSpeed").getNodeValue()));
        vehicleSimulationDetails.put(Long.valueOf(vehicle.getVehicleId()), simDetail);
        
        
        NodeList childs = vehicleNode.getChildNodes();
        List<Network> vehicleNetworks = new ArrayList<Network>();
        for (int k=0, l = childs.getLength(); k < l; ++k)
        {
            Node child = childs.item(k);
            if ("networks".equals(child.getNodeName()))
            {
                List<Network> nets = parseNetworksNode(child.getChildNodes());
                vehicleNetworks.addAll(nets);
            }
        }
        vehicle.setNetworks(vehicleNetworks);
    }

    private List<Network> parseNetworksNode(NodeList networkNodes)
    {
        List<Network> netwrks = new ArrayList<Network>();
        for (int k = 0, l = networkNodes.getLength(); k < l; ++k)
        {
            Node child = networkNodes.item(k);
            if ("network".equals(child.getNodeName()))
            {
                Network net = parseOneNetworkNode(child);
                netwrks.add(net);
            }
        }

        return netwrks;
    }

    private Network parseOneNetworkNode(Node networkNode)
    {
        Network network = node.getTopicMessageFactory().newFromType(Network._TYPE);
        NamedNodeMap attributes = networkNode.getAttributes();
        
        Byte networkType = Byte.valueOf(attributes.getNamedItem("type").getNodeValue());
        network.setType(networkType);
        
        int networkAddress = parseInetAddress(attributes.getNamedItem("address"));
        network.setAddress(networkAddress);
        network.setMask(parseInetAddress(attributes.getNamedItem("mask")));
        
        Node radiusAttr = attributes.getNamedItem("radius");
        Node fluctuationAttr = attributes.getNamedItem("fluctuation");
        
        if (radiusAttr != null || fluctuationAttr != null)
        {
            double radius = radiusAttr != null ? Double.parseDouble(radiusAttr.getNodeValue()) : 0.0;
            double fluctuation = fluctuationAttr != null ? Double.parseDouble(fluctuationAttr.getNodeValue()) : 0.0;
            
            NetworkSimulationDetails simDetails = new NetworkSimulationDetails();
            simDetails.setRadius(radius);
            simDetails.setFluctuation(fluctuation);
            
            Map<Integer, NetworkSimulationDetails> entry = networkSimulationDetails.get(networkType);
            if (entry == null)
            {
                entry = new HashMap<Integer, NetworkSimulationDetails>();
                networkSimulationDetails.put(networkType, entry);
            }
            
            entry.put(networkAddress, simDetails);
        }
        
        return network;
    }
    
    private int parseInetAddress(Node attribute)
    {
        if (attribute == null || attribute.getNodeValue().matches("\\s*"))
        {
            return 0;
        }
        
        String value = attribute.getNodeValue();
        if (value.matches("\\d+"))
        {
            return (int)(0xFFFFFFFF & Long.parseLong(value));
        }
        
        if (value.matches("\\d+\\.\\d+\\.\\d+\\.\\d+"))
        {
            String[] v = value.split("\\.");
            long r = Long.parseLong(v[0]) << 24
                | Long.parseLong(v[1]) << 16
                | Long.parseLong(v[2]) << 8
                | Long.parseLong(v[3]);
            
            return (int)(0xFFFFFFFF & r);
        }

        throw new NumberFormatException("Can not parse address " + value);
    }

    /**
     * @param clockNode clockNode
     * @throws IOException thrown in case of errors
     */
    private void parseClockNode(Node clockNode) throws IOException
    {
        String className = clockNode.getAttributes().getNamedItem("className").getNodeValue();

        try
        {
            Class<?> clazz = Class.forName(className);
            clock = (Clock) clazz.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            throw new IOException("Can not find class " + className, e);
        }
        catch (InstantiationException e)
        {
            throw new IOException("Can not instantiate class " + className, e);
        }
        catch (IllegalAccessException e)
        {
            throw new IOException("Can not addess class " + className, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clock getClock()
    {
        return clock;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectedNode getNode()
    {
        return node;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Location> getLocations()
    {
        return locations;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, LocationSimulationDetails> getLocationSimulationDetails()
    {
        return locationSimulationDetails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vehicle> getVehicles()
    {
        return vehicles;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Long, VehicleSimulationDetails> getVehicleSimulationDetails()
    {
        return vehicleSimulationDetails;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Network> getNetworks()
    {
        return networks;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Byte, Map<Integer, NetworkSimulationDetails>> getNetworkSimulationDetails()
    {
        return networkSimulationDetails;
    }
}
