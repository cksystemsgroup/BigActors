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

/*
 * L.DrifterMarker is used to display quadrotor UAVs on the map.
 */
L.DrifterMarker = L.VehicleMarker.extend({

	options : {
		iconBusyUrl : 'img/DrifterBlack_32.png',
		iconIdleUrl : 'img/DrifterGreen_32.png',
	}

});

L.drifterMarker = function (latlng, options) {
    return new L.DrifterMarker(latlng, options);
};
