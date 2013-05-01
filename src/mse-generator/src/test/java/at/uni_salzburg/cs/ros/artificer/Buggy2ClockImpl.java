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

import at.uni_salzburg.cs.ros.artificer.Clock;

/**
 * BuggyClock
 */
public class Buggy2ClockImpl implements Clock
{
    /**
     * @throws IllegalAccessException in case of instantiation
     */
    public Buggy2ClockImpl() throws IllegalAccessException
    {
        throw new IllegalAccessException("Thrown on purpose!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long currentTimeMillis()
    {
        return 0;
    }

}
