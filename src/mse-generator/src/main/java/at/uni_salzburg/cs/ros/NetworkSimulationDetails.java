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

/**
 * NetworkSimulationDetails
 */
public class NetworkSimulationDetails
{
    private double radius;
    private double fluctuation;
    
    /**
     * @return the radius in meters
     */
    public double getRadius()
    {
        return radius;
    }
    
    /**
     * @param radius the radius in meters
     */
    public void setRadius(double radius)
    {
        this.radius = radius;
    }
    
    /**
     * @return the fluctuation in meters
     */
    public double getFluctuation()
    {
        return fluctuation;
    }
    
    /**
     * @param fluctuation the fluctuation in meters
     */
    public void setFluctuation(double fluctuation)
    {
        this.fluctuation = fluctuation;
    }
}
