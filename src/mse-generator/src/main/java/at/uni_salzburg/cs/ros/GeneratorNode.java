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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Generator ROS node
 */
public class GeneratorNode extends AbstractNodeMain
{
    private static final String PROP_CONFIG_FILE_NAME = "mse.generator.config.file";
    
    private static final String CONFIGURATION_FILE = "mse-generator-config.xml";

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphName getDefaultNodeName()
    {
        return GraphName.of("generator");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(final ConnectedNode connectedNode)
    {
        InputStream confStream = null;
        String configFileName = System.getProperty(PROP_CONFIG_FILE_NAME);
        if (configFileName != null)
        {
            File configFile = new File(configFileName);
            connectedNode.getLog().info("Opening configuration file " + configFileName);
            try
            {
                confStream = new FileInputStream(configFile);
            }
            catch (FileNotFoundException e)
            {
                connectedNode.getLog().error("Can not read configuration file '" + configFileName +
                    "', switching to default configuration.");
            }
        }

        if (confStream == null)
        {
            confStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIGURATION_FILE);
        }

        final Configuration configuration;
        try
        {
            configuration = new ConfigurationImpl(connectedNode, confStream);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Can not load configuration " + CONFIGURATION_FILE, e);
        }

        final Publisher<big_actor_msgs.MissionStateEstimate> msePublisher =
            connectedNode.newPublisher("mse", big_actor_msgs.MissionStateEstimate._TYPE);

        final Publisher<big_actor_msgs.StructureStateEstimate> ssePublisher =
            connectedNode.newPublisher("sse", big_actor_msgs.StructureStateEstimate._TYPE);

        GeneratorPublisher publisher = new GeneratorPublisher(configuration);
        publisher.setMsePublisher(msePublisher);
        publisher.setSsePublisher(ssePublisher);

        connectedNode.getLog().info("GeneratorNode.onStart(): Buggerit!");

        connectedNode.executeCancellableLoop(publisher);
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
