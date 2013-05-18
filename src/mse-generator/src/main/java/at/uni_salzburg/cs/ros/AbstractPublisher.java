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

import at.uni_salzburg.cs.ros.artificer.Artificer;

import org.ros.concurrent.CancellableLoop;

import java.util.List;
import java.util.Timer;

/**
 * Abstract Publisher
 */
public abstract class AbstractPublisher extends CancellableLoop
{
    private static final int ARTIFICER_WAIT_PERIOD = 1000;

    private static final long SCHEDULE_SETUP_DELAY = 0;
    
    private static final long SCHEDULE_CYCLE = 1000;

    private List<Artificer> artificers;
    private Timer timer = new Timer();

    private Configuration configuration;
    
    /**
     * @param configuration configuration
     */
    public void setConfiguration(Configuration configuration)
    {
        this.configuration = configuration;
    }
    
    /**
     * @return the configuration
     */
    public Configuration getConfiguration()
    {
        return configuration;
    }
    
    /**
     * @param artificers artificers
     */
    public void setArtificers(List<Artificer> artificers)
    {
        this.artificers = artificers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setup()
    {
        if (artificers == null)
        {
            configuration.getNode().getLog().error("No Artificers configured, terminating work.");
            return;
        }
        
        configuration.getNode().getLog().info("AbstractPublisher.setup()");
        for (Artificer artificer : artificers)
        {
            timer.schedule(artificer, SCHEDULE_SETUP_DELAY, SCHEDULE_CYCLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel()
    {
        configuration.getNode().getLog().info("AbstractPublisher.cancel()");
        super.cancel();

        timer.cancel();

        boolean runningArtificers;
        do
        {
            runningArtificers = false;
            for (Artificer artificer : artificers)
            {
                if (artificer.isActive())
                {
                    runningArtificers = true;
                    Sleeper.sleep(ARTIFICER_WAIT_PERIOD);
                    break;
                }
            }
        } while (runningArtificers);
    }

}
