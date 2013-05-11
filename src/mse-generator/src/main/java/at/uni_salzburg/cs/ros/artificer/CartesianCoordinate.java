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
 * This class implements a 3-tuple to describe a position in a orthogonal coordinate system.
 */
public class CartesianCoordinate
{
    /**
     * The x value of this vector.
     */
    private double x;

    /**
     * The y value of this vector.
     */
    private double y;

    /**
     * The z value of this vector.
     */
    private double z;

    /**
     * Construct a null position vector, i.e. x, y and z are zero.
     */
    public CartesianCoordinate()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Construct a position vector as a copy of another position vector.
     * 
     * @param p the position vector to be copied.
     */
    public CartesianCoordinate(CartesianCoordinate p)
    {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    /**
     * Construct a position vector with the given values for x, y and z.
     * 
     * @param x the x value.
     * @param y the y value.
     * @param z the z value.
     */
    public CartesianCoordinate(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Return the x value.
     * 
     * @return the x value.
     */
    public double getX()
    {
        return x;
    }

    /**
     * Set the x value.
     * 
     * @param x the new x value
     */
    public void setX(double x)
    {
        this.x = x;
    }

    /**
     * Return the y value.
     * 
     * @return the y value.
     */
    public double getY()
    {
        return y;
    }

    /**
     * Set the y value.
     * 
     * @param y the new y value
     */
    public void setY(double y)
    {
        this.y = y;
    }

    /**
     * Return the z value.
     * 
     * @return the z value.
     */
    public double getZ()
    {
        return z;
    }

    /**
     * Set the z value.
     * 
     * @param z the new z value
     */
    public void setZ(double z)
    {
        this.z = z;
    }

    /**
     * Copy the values of another CartesianCoordinate vector to this vector.
     * 
     * @param p the other CartesianCoordinate vector.
     */
    public void set(CartesianCoordinate p)
    {
        x = p.x;
        y = p.y;
        z = p.z;
    }

    /**
     * Return the norm of the position vector.
     * 
     * @return the norm of the vector.
     */
    public double norm()
    {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Add another position vector to this vector.
     * 
     * @param p another vector to be added to this vector.
     * @return the resulting vector
     */
    public CartesianCoordinate add(CartesianCoordinate p)
    {
        return new CartesianCoordinate(x + p.x, y + p.y, z + p.z);
    }

    /**
     * Subtract another position vector from this vector.
     * 
     * @param p another vector to be subtracted of this vector.
     * @return the resulting vector
     */
    public CartesianCoordinate subtract(CartesianCoordinate p)
    {
        return new CartesianCoordinate(x - p.x, y - p.y, z - p.z);
    }

    /**
     * Multiply this position vector by a scalar.
     * 
     * @param scalar the scalar this position is multiplied by.
     * @return the resulting vector
     */
    public CartesianCoordinate multiply(double scalar)
    {
        return new CartesianCoordinate(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Perform a scalar multiplication with another vector.
     * 
     * @param p the other vector
     * @return the result
     */
    public double multiply(CartesianCoordinate p)
    {
        return x * p.x + y * p.y + z * p.z;
    }

    /**
     * Perform a cross multiplication with another vector.
     * 
     * @param p the other vector
     * @return the result
     */
    public CartesianCoordinate crossProduct(CartesianCoordinate p)
    {
        return new CartesianCoordinate(y * p.z - z * p.y, z * p.x - x * p.z, x * p.y - y * p.x);
    }

    /**
     * Return the normalized current vector, i.e. a vector in the same direction having length one.
     * 
     * @return the normalized current vector.
     */
    public CartesianCoordinate normalize()
    {
        double n = norm();

        if (n == 0)
        {
            return new CartesianCoordinate(1, 0, 0);
        }
        return multiply(1 / n);
    }

}
