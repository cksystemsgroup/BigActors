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

import at.uni_salzburg.cs.ros.viewer.services.BigraphArchive;

import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.ros.address.InetAddressFactory;
import org.ros.exception.RosRuntimeException;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * RosNodeStarterImpl
 */
@EagerLoad
public class RosNodeStarterImpl implements RosNodeStarter
{
    private static final Logger LOG = LoggerFactory.getLogger(RosNodeStarterImpl.class);

    private NodeMainExecutor nodeMainExecutor;

    private MseListenerImpl mseListener;

    private SseListenerImpl sseListener;
    
    private BgmListenerImpl bgmListener;


    /**
     * @throws RosRuntimeException thrown in case of errors.
     */
    public RosNodeStarterImpl(BigraphArchive archive) throws RosRuntimeException
    {
        LOG.info("Initializing RosNodeStarterImpl.");
        
        NodeConfiguration nodeConfiguration =
            NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostName(), getMasterUri());
        
        mseListener = new MseListenerImpl();
        sseListener = new SseListenerImpl();
        bgmListener = new BgmListenerImpl(archive);
        
        DefaultNodeMainExecutor.newDefault().execute(mseListener, nodeConfiguration);
        DefaultNodeMainExecutor.newDefault().execute(sseListener, nodeConfiguration);
        DefaultNodeMainExecutor.newDefault().execute(bgmListener, nodeConfiguration);
    }

    /**
     * @return master URI
     */
    private URI getMasterUri()
    {
        return NodeConfiguration.DEFAULT_MASTER_URI;
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
        return mseListener;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SseListener getSseListener()
    {
        return sseListener;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BgmListener getBgmListener()
    {
        return bgmListener;
    }
}
