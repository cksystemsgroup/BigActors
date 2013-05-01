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
import big_actor_msgs.Vehicle;

import org.ros.message.Time;
import org.ros.node.ConnectedNode;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Configuration
 */
public class ConfigurationImpl implements Configuration
{
    private ConnectedNode node;

    private Clock clock;

    private List<Location> locations;

    private List<Vehicle> vehicles;

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
    }

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

    private void parseConfigNode(NodeList nodes) throws IOException
    {
        locations = new ArrayList<Location>();
        vehicles = new ArrayList<Vehicle>();
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
        }
    }

    private void parseLocationsNode(NodeList locationNodes)
    {
        for (int k = 0, l = locationNodes.getLength(); k < l; ++k)
        {
            Node child = locationNodes.item(k);
            if ("location".equals(child.getNodeName()))
            {
                Location loc = parseOneLocationNode(child);
                locations.add(loc);
            }
        }
    }

    private Location parseOneLocationNode(Node locationNode)
    {
        Location location = node.getTopicMessageFactory().newFromType(Location._TYPE);

        NamedNodeMap attributes = locationNode.getAttributes();
        location.setLocationId(Integer.parseInt(attributes.getNamedItem("id").getNodeValue()));
        location.setStamp(Time.fromMillis(Integer.parseInt(attributes.getNamedItem("timeStamp").getNodeValue())));
        location.setMaximumAltitude(Float.parseFloat(attributes.getNamedItem("maxAltitude").getNodeValue()));
        location.setMinimumAltitude(Float.parseFloat(attributes.getNamedItem("minAltitude").getNodeValue()));

        List<LatLng> boundaries = new ArrayList<LatLng>();
        for (int k = 0, l = locationNode.getChildNodes().getLength(); k < l; ++k)
        {
            Node child = locationNode.getChildNodes().item(k);
            if ("boundaries".equals(child.getNodeName()))
            {
                List<LatLng> bnds = parseBoundariesNode(child.getChildNodes());
                boundaries.addAll(bnds);
            }
        }
        location.setBoundaries(boundaries);
        
        return location;
    }

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

    private void parseVehiclesNode(NodeList vehicleNodes)
    {
        for (int k = 0, l = vehicleNodes.getLength(); k < l; ++k)
        {
            Node child = vehicleNodes.item(k);
            if ("vehicle".equals(child.getNodeName()))
            {
                Vehicle vehicle = node.getTopicMessageFactory().newFromType(Vehicle._TYPE);
                NamedNodeMap attributes = child.getAttributes();
                vehicle.setVehicleId(Integer.parseInt(attributes.getNamedItem("id").getNodeValue()));
                LatLngAlt position = node.getTopicMessageFactory().newFromType(LatLngAlt._TYPE);
                position.setLatitude(Double.parseDouble(attributes.getNamedItem("latitude").getNodeValue()));
                position.setLongitude(Double.parseDouble(attributes.getNamedItem("longitude").getNodeValue()));
                position.setAltitude(Double.parseDouble(attributes.getNamedItem("altitude").getNodeValue()));
                vehicle.setPosition(position);
                vehicles.add(vehicle);
            }
        }
    }

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
    public List<Vehicle> getVehicles()
    {
        return vehicles;
    }
}
