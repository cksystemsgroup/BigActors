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

public class LocationSimulationDetails
{
    private double averageSpeed;
    private double mutationRate;
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;
    
    /**
     * @return average speed in meters per second.
     */
    public double getAverageSpeed()
    {
        return averageSpeed;
    }
    
    /**
     * @param averageSpeed average speed in meters per second.
     */
    public void setAverageSpeed(double averageSpeed)
    {
        this.averageSpeed = averageSpeed;
    }
    
    /**
     * @return mutation speed of vertices in meters per second.
     */
    public double getMutationSpeed()
    {
        return mutationRate;
    }
    
    /**
     * @param mutationRate mutation speed of vertices in meters per second.
     */
    public void setMutationRate(double mutationRate)
    {
        this.mutationRate = mutationRate;
    }
    
    /**
     * @return the minimum latitude
     */
    public double getMinLatitude()
    {
        return minLatitude;
    }
    
    /**
     * @param minLatitude the minimum latitude
     */
    public void setMinLatitude(double minLatitude)
    {
        this.minLatitude = minLatitude;
    }
    
    /**
     * @return the maximum latitude
     */
    public double getMaxLatitude()
    {
        return maxLatitude;
    }
    
    /**
     * @param maxLatitude the maximum latitude
     */
    public void setMaxLatitude(double maxLatitude)
    {
        this.maxLatitude = maxLatitude;
    }
    
    /**
     * @return the minimum longitude
     */
    public double getMinLongitude()
    {
        return minLongitude;
    }
    
    /**
     * @param minLongitude the minimum longitude
     */
    public void setMinLongitude(double minLongitude)
    {
        this.minLongitude = minLongitude;
    }
    
    /**
     * @return the maximum longitude
     */
    public double getMaxLongitude()
    {
        return maxLongitude;
    }
    
    /**
     * @param maxLongitude the maximum longitude
     */
    public void setMaxLongitude(double maxLongitude)
    {
        this.maxLongitude = maxLongitude;
    }
}
