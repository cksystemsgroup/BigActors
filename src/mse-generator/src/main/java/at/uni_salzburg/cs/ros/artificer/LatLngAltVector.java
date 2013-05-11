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

import big_actor_msgs.LatLngAlt;

/**
 * LatLngAltVector
 */
public class LatLngAltVector
{
    private double latitude;
    private double longitude;
    private double altitude;

    /**
     * @param a the vector as <code>LatLngAlt</code> instance.
     */
    public LatLngAltVector(LatLngAlt a)
    {
        latitude = a.getLatitude();
        longitude = a.getLongitude();
        altitude = a.getAltitude();
    }

    /**
     * @param latitude the latitude
     * @param longitude the longitude
     * @param altitude the altitude
     */
    public LatLngAltVector(double latitude, double longitude, double altitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
    
    /**
     * @param a a given <code>LatLngAlt</code> instance to be filled with this vector's coordinates.
     */
    public void fill(LatLngAlt a)
    {
        a.setLatitude(latitude);
        a.setLongitude(longitude);
        a.setAltitude(altitude);
    }

    /**
     * @param a other vector
     * @return this + a
     */
    public LatLngAltVector add(LatLngAltVector a)
    {
        return new LatLngAltVector(latitude + a.latitude, longitude + a.longitude, altitude + a.altitude);
    }

    /**
     * @param b the multiplier
     * @return b times the original vector
     */
    public LatLngAltVector multiply(double b)
    {
        return new LatLngAltVector(latitude * b, longitude * b, altitude * b);
    }

    /**
     * @return the length of the vector.
     */
    public double norm()
    {
        return Math.sqrt(latitude * latitude + longitude * longitude + altitude * altitude);
    }

    /**
     * @return the normalized vector
     */
    public LatLngAltVector normalize()
    {
        double length = norm();
        return new LatLngAltVector(latitude / length, longitude / length, altitude / length);
    }

}
