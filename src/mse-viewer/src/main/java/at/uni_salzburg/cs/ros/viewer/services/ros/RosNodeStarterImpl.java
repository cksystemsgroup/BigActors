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

import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.ros.exception.RosRuntimeException;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import java.util.Arrays;

/**
 * RosNodeStarterImpl
 */
@EagerLoad
public class RosNodeStarterImpl implements RosNodeStarter
{
    private static final Logger LOG = LoggerFactory.getLogger(RosNodeStarterImpl.class);

    private NodeConfiguration nodeConfiguration;

    private NodeMainExecutor nodeMainExecutor;

    private NodeMain nodeMain;

    private String[] nodeNames = {
        MseListenerImpl.class.getName()
    };

    /**
     * @throws RosRuntimeException thrown in case of errors.
     */
    public RosNodeStarterImpl() throws RosRuntimeException
    {
        LOG.info("Initializing RosNodeStarterImpl.");

        CommandLineLoader loader = new CommandLineLoader(Arrays.asList(nodeNames));
        String nodeClassName = loader.getNodeClassName();
        LOG.info("Loading node class: %s", loader.getNodeClassName());
        nodeConfiguration = loader.build();

        try
        {
            nodeMain = loader.loadClass(nodeClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new RosRuntimeException("Unable to locate node: " + nodeClassName, e);
        }
        catch (InstantiationException e)
        {
            throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName, e);
        }
        catch (IllegalAccessException e)
        {
            throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName, e);
        }

        Preconditions.checkState(nodeMain != null);
        nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
        nodeMainExecutor.execute(nodeMain, nodeConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown()
    {
        nodeMainExecutor.shutdown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MseListener getMseListener()
    {
        if (nodeMain instanceof MseListener)
        {
            return (MseListener) nodeMain;
        }

        return null;
    }
}
