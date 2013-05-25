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

L.Location = L.Polygon.extend({

	options : {
		stroke: true,
		color: '#ffff00',
		weight: 3,
		opacity: 0.15,
		fill: true,
		fillColor: '#ffff00',
		fillOpacity: 0.10,
		dashArray: null,
		clickable: false,
	},
	
	timeStamp: 1,
});


L.LocSurface = L.Location.extend({
	options : {
		color: '#8d5b2a',
		opacity: 0.15,
		fillColor: '#8d5b2a',
		fillOpacity: 0.10,
	},
});

L.LocAirSpace = L.Location.extend({
	options : {
		color: '#00ffff',
		opacity: 0.15,
		fillColor: '#00ffff',
		fillOpacity: 0.10,
	},
});

L.LocUnderWater = L.Location.extend({
	options : {
		color: '#0000ff',
		opacity: 0.15,
		fillColor: '#0000ff',
		fillOpacity: 0.10,
	},
});

L.LocOilSpill = L.Location.extend({
	options : {
		color: '#000000',
		opacity: 0.3,
		fillColor: '#000000',
		fillOpacity: 0.3,
	},
});

L.createLocation = function (type, boundaries) {
	
	if (type == "surface") {
		return new L.LocSurface(boundaries);
	}
	
	if (type == "airSpace") {
		return new L.LocAirSpace(boundaries);
	}
	
	if (type == "underWater") {
		return new L.LocUnderWater(boundaries);
	}
	
	if (type == "oilSpill") {
		return new L.LocOilSpill(boundaries);
	}

	return new L.Location(boundaries);
}
