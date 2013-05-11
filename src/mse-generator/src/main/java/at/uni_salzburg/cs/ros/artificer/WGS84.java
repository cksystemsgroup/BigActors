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

/**
 * This class implements the Department of Defense World Geodetic System 1984 (WGS84)
 * 
 * @see http://earth-info.nga.mil/GandG/publications/tr8350.2/tr8350_2.html NGA: DoD World Geodetic System 1984, Its
 *      Definition and Relationships with Local Geodetic Systems
 */
public class WGS84
{
    private static final double EQUATORIAL_AXIS = 6378137;
    private static final double POLAR_AXIS = 6356752.3142;
    private static final double ANGULAR_ECCENTRICITY = Math.acos(POLAR_AXIS / EQUATORIAL_AXIS);
    private static final double FIRST_ECCENTRICITY = 8.1819190842622E-2;
    private static final double PI180TH = Math.PI / 180;

    /**
     * @param coordinates coordinates
     * @return a CartesianCoordinate
     */
    public CartesianCoordinate polarToRectangularCoordinates(PolarCoordinate coordinates)
    {
        return polarToRectangularCoordinates(coordinates.getLatitude(), coordinates.getLongitude(),
            coordinates.getAltitude());
    }

    /**
     * @param latitude latitude
     * @param longitude longitude
     * @param altitude altitude
     * @return a CartesianCoordinate
     */
    public CartesianCoordinate polarToRectangularCoordinates(double latitude, double longitude, double altitude)
    {

        double u = Math.sin(latitude * PI180TH) * FIRST_ECCENTRICITY;
        double N = EQUATORIAL_AXIS / Math.sqrt(1 - u * u);

        double x = (N + altitude) * Math.cos(latitude * PI180TH) * Math.cos(longitude * PI180TH);
        double y = (N + altitude) * Math.cos(latitude * PI180TH) * Math.sin(longitude * PI180TH);
        double v = POLAR_AXIS / EQUATORIAL_AXIS;
        double z = (v * v * N + altitude) * Math.sin(latitude * PI180TH);

        return new CartesianCoordinate(x, y, z);
    }

    /**
     * @param coordinates coordinates
     * @return a PolarCoordinate
     */
    public PolarCoordinate rectangularToPolarCoordinates(CartesianCoordinate coordinates)
    {
        return rectangularToPolarCoordinates(coordinates.getX(), coordinates.getY(), coordinates.getZ());
    }

    /**
     * @param x x
     * @param y y
     * @param z z
     * @return a PolarCoordinate
     */
    public PolarCoordinate rectangularToPolarCoordinates(double x, double y, double z)
    {

        double newLatitude = 90;
        double latitude = 0;
        double u, v, w, N = 0;
        double sin2AE = Math.sin(2 * ANGULAR_ECCENTRICITY);
        double sinAE = Math.sin(ANGULAR_ECCENTRICITY);

        while (Math.abs(latitude - newLatitude) > 1E-13)
        {
            latitude = newLatitude;

            u = Math.sin(latitude) * Math.sin(ANGULAR_ECCENTRICITY);
            N = EQUATORIAL_AXIS / Math.sqrt(1 - u * u);

            v = N * Math.sin(latitude);
            w = N * Math.cos(latitude);

            double numerator = EQUATORIAL_AXIS * EQUATORIAL_AXIS * z + v * v * v * sin2AE * sin2AE / 4;
            double denominator =
                EQUATORIAL_AXIS * EQUATORIAL_AXIS * Math.sqrt(x * x + y * y) - w * w * w * sinAE * sinAE;
            newLatitude = Math.atan(numerator / denominator);
        }

        double cosNLat = Math.cos(newLatitude);
        double sinNLat = Math.sin(newLatitude);

        double altitude = cosNLat * Math.sqrt(x * x + y * y) + sinNLat * (z + sinAE * sinAE * N * sinNLat) - N;

        double longitude = Math.asin(y / ((N + altitude) * cosNLat));

        return new PolarCoordinate(newLatitude / PI180TH, longitude / PI180TH, altitude);
    }

}
