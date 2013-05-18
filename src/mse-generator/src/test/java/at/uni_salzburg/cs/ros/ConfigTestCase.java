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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import at.uni_salzburg.cs.ros.artificer.VehicleData;

import big_actor_msgs.LatLng;
import big_actor_msgs.Location;
import big_actor_msgs.Vehicle;

import org.junit.Before;
import org.junit.Test;
import org.ros.internal.message.definition.MessageDefinitionReflectionProvider;
import org.ros.internal.message.topic.TopicMessageFactory;
import org.ros.message.MessageDefinitionProvider;
import org.ros.message.MessageFactory;
import org.ros.node.ConnectedNode;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 * ConfigTestCase
 */
public class ConfigTestCase
{

    private static final String MSE_GENERATOR_CONFIG_1_OK =
        "configTest/mse-generator-config-1.xml";
    private static final String MSE_GENERATOR_CONFIG_2_INVALID_CLOCK =
        "configTest/mse-generator-config-2-invalid-clock.xml";
    private static final String MSE_GENERATOR_CONFIG_3_INVALID_XML =
        "configTest/mse-generator-config-3-invalid-xml.xml";
    private static final String MSE_GENERATOR_CONFIG_4_BUGGY_CLOCK =
        "configTest/mse-generator-config-4-buggy-clock.xml";
    private static final String MSE_GENERATOR_CONFIG_5_BUGGY_CLOCK =
        "configTest/mse-generator-config-5-buggy-clock.xml";
    
    ConnectedNode node;
    MessageFactory topicMessageFactory;
    VehicleData vehicleData;

    @Before
    public void setUp()
    {
        MessageDefinitionProvider messageDefinitionProvider = new MessageDefinitionReflectionProvider();
        topicMessageFactory = new TopicMessageFactory(messageDefinitionProvider);

        node = mock(ConnectedNode.class);
        when(node.getTopicMessageFactory()).thenReturn(topicMessageFactory);
    }

    @Test
    public void shouldLoadCompleteConfigFile() throws IOException, SAXException, ParserConfigurationException
    {
        InputStream xmlStream = ConfigTestCase.class.getResourceAsStream(MSE_GENERATOR_CONFIG_1_OK);
        assertNotNull("Problems opening " + MSE_GENERATOR_CONFIG_1_OK, xmlStream);

        ConfigurationImpl config = new ConfigurationImpl(node, xmlStream);
        assertNotNull(config);

        assertEquals("Number of locations", 7, config.getLocations().size());
        Location location = config.getLocations().get(0);
        assertEquals("location ID", 1, location.getLocationId());
        assertEquals("time stamp milliseconds", 1367162195, location.getTimeStamp());
        assertEquals("maximum altitude", 1000.0, location.getMaximumAltitude(), 1E-3);
        assertEquals("minimum altitude", 0.0, location.getMinimumAltitude(), 1E-3);
        assertEquals("location type", 3, location.getLocationType());
        Map<Integer, LocationSimulationDetails> locSimDetails = config.getLocationSimulationDetails();
        LocationSimulationDetails lsDetail = locSimDetails.get(Integer.valueOf(location.getLocationId()));
        assertNotNull("LocationSimulationDetails is null", lsDetail);
        assertEquals("AverageSpeed", 0.1, lsDetail.getAverageSpeed(),1E-7);
        
        List<LatLng> boundaries = location.getBoundaries();
        assertEquals("number of boundary points", 7, boundaries.size());
        assertEquals("boundary 0 latitude", 47.69243237, boundaries.get(0).getLatitude(), 1E-8);
        assertEquals("boundary 0 longitude", 13.38507593, boundaries.get(0).getLongitude(), 1E-8);

        List<Vehicle> vehicles = config.getVehicles();
        assertEquals("Nubmer of vehicles", 5, vehicles.size());
        Vehicle vehicle = vehicles.get(0);

        assertEquals("vehicle 0 id", 1, vehicle.getVehicleId());
        assertEquals("vehicle 0 latitude", 47.69243237, vehicle.getPosition().getLatitude(), 1E-8);
        assertEquals("vehicle 0 longitude", 13.38692824, vehicle.getPosition().getLongitude(), 1E-8);
        assertEquals("vehicle 0 altitude", 20.0, vehicle.getPosition().getAltitude(), 1E-8);
        
        Map<Long, VehicleSimulationDetails> vehSimDetails = config.getVehicleSimulationDetails();
        VehicleSimulationDetails vsDetail = vehSimDetails.get(Long.valueOf(vehicle.getVehicleId()));
        assertNotNull("VehicleSimulationDetails is null", vsDetail);
        assertEquals("AverageSpeed", 10.0, vsDetail.getAverageSpeed(),1E-7);
    }

    @Test
    public void shouldHaveSystemClock() throws IOException
    {
        InputStream xmlStream = ConfigTestCase.class.getResourceAsStream(MSE_GENERATOR_CONFIG_1_OK);
        assertNotNull("Problems opening " + MSE_GENERATOR_CONFIG_1_OK, xmlStream);

        ConfigurationImpl config = new ConfigurationImpl(node, xmlStream);
        assertNotNull(config);

        long systemTime = System.currentTimeMillis();
        long clockTime = config.getClock().currentTimeMillis();

        assertTrue("System time <= clock time", systemTime <= clockTime);
    }

    @Test
    public void shouldHaveMessageFactory() throws IOException
    {
        InputStream xmlStream = ConfigTestCase.class.getResourceAsStream(MSE_GENERATOR_CONFIG_1_OK);
        assertNotNull("Problems opening " + MSE_GENERATOR_CONFIG_1_OK, xmlStream);

        ConfigurationImpl config = new ConfigurationImpl(node, xmlStream);
        assertNotNull(config);

        Vehicle vehicle = config.getNode().getTopicMessageFactory().newFromType(Vehicle._TYPE);
        assertNotNull(vehicle);
    }

    @Test(expected = IOException.class)
    public void shouldDetectInvalidClock() throws IOException
    {
        InputStream xmlStream = ConfigTestCase.class.getResourceAsStream(MSE_GENERATOR_CONFIG_2_INVALID_CLOCK);
        assertNotNull("Problems opening " + MSE_GENERATOR_CONFIG_2_INVALID_CLOCK, xmlStream);

        new ConfigurationImpl(node, xmlStream);
    }

    @Test(expected = IOException.class)
    public void shouldDetectInvalidXML() throws IOException
    {
        InputStream xmlStream = ConfigTestCase.class.getResourceAsStream(MSE_GENERATOR_CONFIG_3_INVALID_XML);
        assertNotNull("Problems opening " + MSE_GENERATOR_CONFIG_3_INVALID_XML, xmlStream);

        new ConfigurationImpl(node, xmlStream);
    }

    @Test(expected = IOException.class)
    public void shouldDetectBuggyClockOne() throws IOException
    {
        InputStream xmlStream = ConfigTestCase.class.getResourceAsStream(MSE_GENERATOR_CONFIG_4_BUGGY_CLOCK);
        assertNotNull("Problems opening " + MSE_GENERATOR_CONFIG_4_BUGGY_CLOCK, xmlStream);

        new ConfigurationImpl(node, xmlStream);
    }

    @Test(expected = IOException.class)
    public void shouldDetectBuggyClockTwo() throws IOException
    {
        InputStream xmlStream = ConfigTestCase.class.getResourceAsStream(MSE_GENERATOR_CONFIG_5_BUGGY_CLOCK);
        assertNotNull("Problems opening " + MSE_GENERATOR_CONFIG_5_BUGGY_CLOCK, xmlStream);

        new ConfigurationImpl(node, xmlStream);
    }
    
    @Test
    public void buggerit()
    {
        Integer one = new Integer (1);
        Integer anotherOne = new Integer(1);
        
        Map<Integer,String> numbers = new HashMap<Integer, String>();
        numbers.put(one,"1");
        assertEquals("1", numbers.get(one));
        assertEquals("1", numbers.get(anotherOne));
        assertEquals("1", numbers.get(Integer.valueOf(1)));
    }
}
