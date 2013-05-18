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
	var h = r.top - 40;
	document.getElementById('map').style.height = h + 'px';
}

function adjustMapCenter() {
	var minLat = 90;
	var maxLat = -90;
	var minLng = 180;
	var maxLng = -180;

	var loc;
	var found = 0;
	for (loc in locations) {
		var b = locations[loc].getBounds();
		var ne = b.getNorthEast();
		var sw = b.getSouthWest();
		if (sw.lat < minLat) {
			minLat = sw.lat;
		}
		if (ne.lat > maxLat) {
			maxLat = ne.lat;
		}
		if (sw.lng < minLng) {
			minLng = sw.lng;
		}
		if (ne.lng > maxLng) {
			maxLng = ne.lng;
		}
		found = 1;
	}

	if (found) {
		map.fitBounds([ [ minLat, minLng ], [ maxLat, maxLng ] ]);
	}
}

function toLatLngArray(points) {
	var latLngs = new Array();

	for ( var k = 0, l = points.length; k < l; ++k) {
		latLngs.push(new L.LatLng(points[k].lat, points[k].lon))
	}

	return latLngs;
}

var firstUpdate = 1;

function updateLocations(data) {
	if (!data) {
		return;
	}
	
	var oldLocations = {};
	for ( var id in locations) {
		oldLocations[id] = 1;
	}
	
	for ( var k = 0, l = data.length; k < l; ++k) {
		var loc = data[k];
		var id = loc.locationId;
		delete oldLocations[id];
		if (!locations[id]) {
			locations[id] = new L.Location(toLatLngArray(loc.boundaries));
			locationLayer.addLayer(locations[id]);
		}
	}
	
	for ( var id in oldLocations) {
		locationLayer.removeLayer(locations[id]);
		delete locations[id];
	}
	
	if (firstUpdate) {
		firstUpdate = 0;
		adjustMapCenter();
	}
}

var t = 0;
var taskStateMap = {
	0 : 'none',
	1 : 'todo',
	2 : 'inProgress',
	3 : 'done',
	4 : 'assigned',
	5 : 'cancelled'
};

var vehicleStateMap = {
	0 : 'none',
	1 : 'idle',
	2 : 'busy'
};

function updateVehicles(data) {
	if (!data) {
		return;
	}

	var oldVehicles = {};
	for ( var id in vehicles) {
		oldVehicles[id] = 1;
	}

	for ( var k = 0, l = data.length; k < l; ++k) {
		var vehicle = data[k];
		var id = vehicle.vehicleId;
		delete oldVehicles[id];
		var pos = [ vehicle.position.lat, vehicle.position.lon ];
		if (vehicles[id]) {
			vehicles[id].setLatLng(pos);
		} else {
			vehicles[id] = L.quadrotorMarker(pos);
//			vehicles[id] = L.boatMarker(pos);
			vehicleLayer.addLayer(vehicles[id]);
		}
		vehicles[id].setVehicleState(id, vehicleStateMap[vehicle.vehicleState], vehicle.heading);
		vehicles[id].setTaskState(vehicle.taskId, taskStateMap[vehicle.taskState]);
	}

	for ( var id in oldVehicles) {
		vehicleLayer.removeLayer(vehicles[id]);
		delete vehicles[id];
	}
}

var map;
var locationLayer;
var vehicleLayer;

function mseViewerInit() {
	adjustMapSize();

	L.Icon.Default.imagePath = 'images';
	
	var tileServerUrl1 = 'http://{s}.tile.osm.org/{z}/{x}/{y}.png';
	var tileServerUrl2 = 'http://khm1.google.com/kh/v=128&src=app&x={x}&y={y}&z={z}&s=Galileo'
	var attribution1 = '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors';
	var attribution2 = '&copy; <a href="http://www.google.com">Google</a>';
	
	var emptyLayer = L.layerGroup();
	var tileLayer1 = L.tileLayer(tileServerUrl1, {attribution : attribution1});
	var tileLayer2 = L.tileLayer(tileServerUrl2, {attribution : attribution2});
	locationLayer = L.layerGroup();
	vehicleLayer = L.layerGroup();
	
	map = L.map('map', {
		center: [47.82210, 13.04077],
		zoom: 10,
		layers: [emptyLayer, locationLayer, vehicleLayer]
	});

	var baseMaps = {
		"No Map" : emptyLayer,
	    "Minimal": tileLayer1,
	    "Satellite" : tileLayer2
	};

	var overlayMaps = {
	    "Locations": locationLayer,
	    "Vehicles" : vehicleLayer
	};
	
	L.control.layers(baseMaps, overlayMaps).addTo(map);

	$.getJSON("state/locations", updateLocations);
	$.getJSON("state/vehicles", updateVehicles);

	setInterval('$.getJSON( "state/locations", updateLocations)', 5000);
	setInterval('$.getJSON( "state/vehicles", updateVehicles)', 1000);

	$(window).resize(_.debounce(adjustMapSize, 500));
}