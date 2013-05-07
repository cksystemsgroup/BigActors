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

import big_actor_msgs.MissionStateEstimate;

import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MseListenerImpl
 */
@EagerLoad
public class MseListenerImpl extends AbstractNodeMain implements MseListener
{
    private final Logger LOG = LoggerFactory.getLogger(MseListenerImpl.class);
    
    private big_actor_msgs.MissionStateEstimate message;

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphName getDefaultNodeName()
    {
        return GraphName.of("mse_viewer");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        LOG.info("MseListenerImpl.onStart()");

        Subscriber<big_actor_msgs.MissionStateEstimate> subscriber =
            connectedNode.newSubscriber("mse", MissionStateEstimate._TYPE);

        subscriber.addMessageListener(new MessageListener<MissionStateEstimate>()
        {
            @Override
            public void onNewMessage(MissionStateEstimate newMessage)
            {
                message = newMessage;
                LOG.info("MseListenerImpl.onNewMessage() MSE received");
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionStateEstimate getMessage()
    {
        return message;
    }
}
