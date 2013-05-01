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

import at.uni_salzburg.cs.ros.Configuration;

import big_actor_msgs.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskArtificer
 */
public class TaskArtificer extends Artificer
{
    private Configuration configuration;
    
    /**
     * @param configuration configuration
     */
    public TaskArtificer(Configuration configuration)
    {
        // TODO Auto-generated constructor stub
        this.configuration = configuration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        // TODO Auto-generated method stub
        configuration.getNode().getLog().info("TaskArtificer.execute()");
        
    }
    
    /**
     * @return the current tasks
     */
    public List<Task> currentTasks()
    {
        List<Task> tasks = new ArrayList<Task>();
        // TODO Auto-generated method stub
        return tasks;
    }


}
