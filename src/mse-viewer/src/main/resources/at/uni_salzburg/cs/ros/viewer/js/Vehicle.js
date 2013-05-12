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
 * Vehicle icon
 */
L.VehicleIcon = L.Icon.extend({

	options : {
		shadowUrl : null,
		iconSize : new L.Point(32, 32),
		iconAnchor : new L.Point(16, 16),
		popupAnchor : new L.Point(1, -16),
		className : 'vehicle-icon'
	},

	taskId : '',
	taskState : '',
	vehicleId : '',
	vehicleState : '',
	vehicleHeading : 0,
	
	createIcon : function() {
		var div = document.createElement('div');
		this.img = this._createImg(this.options['iconUrl']);
		this.vehicleDiv = document.createElement('div');
		this.taskDiv = document.createElement('div');
		this.setTaskState('0','none');
		this.setVehicleState('0','none');
		div.appendChild(this.img);
		div.appendChild(this.vehicleDiv);
		div.appendChild(this.taskDiv);
		this._setIconStyles(div, 'icon');
		return div;
	},

	createShadow : function() {
		return null;
	},
	
	setTaskState : function(taskId, taskState) {
		if (this.taskId != taskId || this.taskState != taskState) {
			this.taskId = taskId;
			this.taskState = taskState;
			this.taskDiv.innerHTML = this.taskId > 0 ? 't: ' + this.taskId : '';
			this.taskDiv.className = 'task_info ' + this.taskState;
		}
	},
	
	setVehicleState : function(vehicleId, vehicleState, vehicleHeading) {
		if (this.vehicleId != vehicleId || this.vehicleState != vehicleState || this.vehicleHeading != vehicleHeading) {
			this.vehicleId = vehicleId;
			this.vehicleState = vehicleState;
			this.vehicleHeading = vehicleHeading;
			this.vehicleDiv.innerHTML = 'v: ' + (vehicleId || '??') + ' h: ' + Number(vehicleHeading).toFixed();
			this.vehicleDiv.className = 'vehicle_info ' + vehicleState;
			this.img.style.transform = 'rotate('+(270-vehicleHeading)+'deg)';
			this.img.style['-ms-transform'] = 'rotate('+(270-vehicleHeading)+'deg)';
			this.img.style['-webkit-transform'] = 'rotate('+(270-vehicleHeading)+'deg)';
		}
	}
});


/*
 * L.VehicleMarker is used to display quadrotor UAVs on the map.
 */
L.VehicleMarker = L.Marker.extend({

	options : {
		icon : new L.VehicleIcon('img/VehicleGreen_32.png'),
		iconBusyUrl : 'img/VehicleBlack_32.png',
		iconIdleUrl : 'img/VehicleGreen_32.png',
		clickable : false,
		className : 'vehicle-marker-icon'
	},
	
	vehicleId : '',
	vehicleState : '',

	setVehicleState : function(id, state, heading) {
		if (this.vehicleId != id || this.vehicleState != state) {
			this.vehicleId = id;
			this.vehicleState = state;
			if (state == 'idle') {
				this.setIcon(new L.VehicleIcon({iconUrl : this.options.iconIdleUrl}));
			} else {
				this.setIcon(new L.VehicleIcon({iconUrl : this.options.iconBusyUrl}));
			}
			this.options.icon.setVehicleState(id, state, heading);
		}		
		
		return this;
	},

	setTaskState : function(task, state) {
		this.options.icon.setTaskState(task, state)
		return this;
	},

});


L.quadrotorMarker = function (latlng, options) {
    return new L.VehicleMarker(latlng, options);
};
