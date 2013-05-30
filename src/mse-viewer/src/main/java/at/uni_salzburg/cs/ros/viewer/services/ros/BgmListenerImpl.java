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
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * BgmListenerImpl
 */
@EagerLoad
public class BgmListenerImpl extends AbstractNodeMain implements BgmListener
{
    private static final Logger LOG = LoggerFactory.getLogger(BgmListenerImpl.class);
    
    private std_msgs.String message;

    private BigraphArchive archive;
    
    public BgmListenerImpl(BigraphArchive archive)
    {
        this.archive = archive;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public GraphName getDefaultNodeName()
    {
        return GraphName.of("bgm_listener");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        LOG.info("onStart()");

        Subscriber<std_msgs.String> subscriber =
            connectedNode.newSubscriber("bgm", std_msgs.String._TYPE);

        subscriber.addMessageListener(new MessageListener<std_msgs.String>()
        {
            @Override
            public void onNewMessage(std_msgs.String newMessage)
            {
                message = newMessage;
                LOG.info("onNewMessage() BGM received");
                try
                {
                    archive.archive(message.getData());
                }
                catch (IOException e)
                {
                    LOG.error("Can not archive BGM.", e);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBigraphString()
    {
        return message != null ? message.getData() : null;
    }
}
