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

var vehicles = {};
var locations = {};

function getRect(o) {
	var r = {
		top : 0,
		left : 0,
		width : 0,
		height : 0
	};

	if (!o) {
		return r;
	} else if (typeof o == 'string') {
		o = document.getElementById(o);
	}
	if (typeof o != 'object') {
		return r;
	}
	if (typeof o.offsetTop != 'undefined') {
		r.height = o.offsetHeight;
		r.width = o.offsetWidth;
		r.left = r.top = 0;
		while (o && o.tagName != 'BODY') {
			r.top += parseInt(o.offsetTop);
			r.left += parseInt(o.offsetLeft);
			o = o.offsetParent;
		}
	}
	return r;
}

function adjustMapSize() {
	var r = getRect('footer');
	// alert(' t='+ r.top + ' l=' + r.left + ' h=' + r.height + ' w=' +
	// r.width);
	var h = r.top - 40;
	document.getElementById('map').style.height = h + 'px';
	// document.getElementById('map').style.width = '100%';
}

var firstUpdate = 1;

function adjustMapCenter() {
	var minLat = 90;
	var maxLat = -90;
	var minLon = 180;
	var maxLon = -180;

	var loc;
	var found = 0;
	for (loc in locations) {
		var b = locations[loc].boundaries;
		for ( var k = 0, l = b.length; k < l; ++k) {
			if (b[k].lat < minLat) {
				minLat = b[k].lat;
			}
			if (b[k].lat > maxLat) {
				maxLat = b[k].lat;
			}
			if (b[k].lon < minLon) {
				minLon = b[k].lon;
			}
			if (b[k].lon > maxLon) {
				maxLon = b[k].lon;
			}
			found = 1;
		}
	}
	
	if (found) {
		map.setView([ (maxLat+minLat)/2, (maxLon+minLon)/2 ], 13);
	}
}

function updateLocations(data) {
	if (!data) {
		return;
	}
	console.log("success");

	for ( var k = 0, l = data.length; k < l; ++k) {
		var loc = data[k];
		if (locations[data[k].locationId]) {
			locations[data[k].locationId] = data[k];
		} else {
			locations[data[k].locationId] = data[k];
		}
		
		
		
	}
	
	
	
	
	

	if (firstUpdate) {
		firstUpdate = 0;
		adjustMapCenter();
	}
}

var t = 0;
function updateVehicles(data) {
	if (!data) {
		return;
	}
	console.log("success");

	if (t == 0) {
		vehicles['01'].setVehicleState('none');
		// vehicles['01'].setLatLng([ 51.5, 0.0 ]);
		t = 1;
	} else {
		vehicles['01'].setVehicleState('other');
		// vehicles['01'].setLatLng([ 51.5, -0.09 ]);
		t = 0;
	}
	vehicles['01'] = L.quadrotorMarker([ 51.5, 0.0 ]).addTo(map);
	
	
	
	
	
}

var map;

function mseViewerInit() {
	adjustMapSize();

	L.Icon.Default.imagePath = 'images';

	// create a map in the "map" div, set the view to a given place and zoom
	map = L.map('map').setView([ 51.505, -0.09 ], 13);

	// add an OpenStreetMap tile layer
	L
			.tileLayer(
					'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
					{
						attribution : '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
					}).addTo(map);

	// add a marker in the given location, attach some popup content to it and
	// open the popup
	// L.marker([ 51.5, 0.0 ]).addTo(map).bindPopup('A pretty CSS3 popup. <br>
	// Easily customizable.').openPopup();
	// L.marker([ 51.5, 0.0 ]).addTo(map);

//	vehicles['01'] = L.quadrotorMarker([ 51.5, 0.0 ]).addTo(map);

	setInterval(
			'$.getJSON( "mse/locations", function(json) {updateLocations(json)})',
			5000);
	setInterval(
			'$.getJSON( "mse/vehicles", function(json) {updateVehicles(json)})',
			1000);
}