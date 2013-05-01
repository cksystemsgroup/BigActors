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

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;

import java.io.IOException;
import java.io.InputStream;

/**
 * MSE Generator ROS node
 */
public class MseGeneratorNode extends AbstractNodeMain
{
    private static final String CONFIGURATION_FILE = "mse-generator-config.xml";

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphName getDefaultNodeName()
    {
        return GraphName.of("mse_generator");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(final ConnectedNode connectedNode)
    {
        InputStream confStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIGURATION_FILE);
        final Configuration configuration;
        try
        {
            configuration = new ConfigurationImpl(connectedNode, confStream);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Can not load configuration " + CONFIGURATION_FILE, e);
        }

        final Publisher<big_actor_msgs.MissionStateEstimate> publisher =
            connectedNode.newPublisher("mse", big_actor_msgs.MissionStateEstimate._TYPE);

        MsePublisher msePublisher = new MsePublisher(publisher, configuration);

        connectedNode.getLog().info("MseGeneratorNode.onStart(): Buggerit!");

        connectedNode.executeCancellableLoop(msePublisher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShutdown(Node node)
    {
        super.onShutdown(node);
        node.getLog().info("MseGeneratorNode.onShutdown()");
    }
}
