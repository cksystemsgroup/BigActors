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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import at.uni_salzburg.cs.ros.Configuration;

import big_actor_msgs.Vehicle;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.ros.internal.message.definition.MessageDefinitionReflectionProvider;
import org.ros.internal.message.topic.TopicMessageFactory;
import org.ros.message.MessageDefinitionProvider;
import org.ros.message.MessageFactory;
import org.ros.node.ConnectedNode;

/**
 * VehicleDataTestCase
 */
public class VehicleDataTestCase
{
    private Configuration configuration;
    private ConnectedNode node;
    private MessageFactory topicMessageFactory;
    private VehicleData vehicleData;
    private Clock clock;
    private Long currentTime;
    private Vehicle vehicle;
    private Log logger;

    @Before
    public void setUp()
    {
        logger = mock(Log.class);
        
        MessageDefinitionProvider messageDefinitionProvider = new MessageDefinitionReflectionProvider();
        topicMessageFactory = new TopicMessageFactory(messageDefinitionProvider);

        node = mock(ConnectedNode.class);
        when(node.getTopicMessageFactory()).thenReturn(topicMessageFactory);
        when(node.getLog()).then(new Answer<Log>()
        {
            @Override
            public Log answer(InvocationOnMock invocation) throws Throwable
            {
                return logger;
            }
        });

        clock = mock(Clock.class);
        when(clock.currentTimeMillis()).then(new Answer<Long>()
        {
            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable
            {
                return currentTime;
            }
        });
        
        configuration = mock(Configuration.class);
        when(configuration.getNode()).thenReturn(node);
        when(configuration.getClock()).thenReturn(clock);
        
        vehicle = node.getTopicMessageFactory().newFromType(Vehicle._TYPE);
        vehicleData = new VehicleData(configuration, vehicle);
    }

    @Test
    public void testMockClock()
    {
        currentTime = Long.valueOf(System.currentTimeMillis());
        assertEquals("Clock 1", currentTime.longValue(), clock.currentTimeMillis());
        
        currentTime += 12345;
        assertEquals("Clock 2", currentTime.longValue(), clock.currentTimeMillis());
    }

    @Test
    public void shouldHaveCreatedVehicleDataIncludingVehicle()
    {
        assertNotNull("Vehicle data object", vehicleData);
        assertNotNull("Vehicle object", vehicleData.getVehicle());
        assertFalse("Initial state is not busy", vehicleData.isBusy());
        vehicleData.update();
        verify(logger).info("VehicleData.update()");
        assertFalse("Initial state is not busy", vehicleData.isBusy());
    }

    @Test
    public void testGetVehicle()
    {
        Vehicle vehicle = vehicleData.getVehicle();
        assertNotNull(vehicle);
    }

}
