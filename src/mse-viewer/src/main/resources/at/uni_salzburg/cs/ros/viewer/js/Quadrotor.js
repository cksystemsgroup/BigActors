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
 * Quadrotor icon
 */
L.QuadrotorIcon = L.Icon.extend({

	options : {
		iconUrl : 'img/QuadrotorGreen_32.png',
		shadowUrl : null,
		iconSize : new L.Point(32, 32),
		iconAnchor : new L.Point(16, 16),
		popupAnchor : new L.Point(1, -16),
		className : 'quadrotor-icon'
	},

	taskId : '',
	taskState : '',
	vehicleId : '',
	vehicleState : '',

	createIcon : function() {
		var div = document.createElement('div');
		var img = this._createImg(this.options['iconUrl']);
		this.taskDiv = document.createElement('div');
		this.taskDiv.setAttribute("class", "task");
		this.vehicleDiv = document.createElement('div');
		this.vehicleDiv.setAttribute("class", "vehicle");
		this.setTaskState('0','none');
		this.setVehicleState('0','none');
		div.appendChild(img);
		div.appendChild(this.taskDiv);
		div.appendChild(this.vehicleDiv);
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
			this.taskDiv.innerHTML = 't: ' + (this.taskId || '??');
			this.taskDiv.className = 'task_info_' + this.taskState;
		}
	},
	
	setVehicleState : function(vehicleId, vehicleState) {
		if (this.vehicleId != vehicleId || this.vehicleState != vehicleState) {
			this.vehicleId = vehicleId;
			this.vehicleState = vehicleState;
			this.vehicleDiv.innerHTML = 'v: ' + (this.vehicleId || '??');
			this.vehicleDiv.className = 'vehicle_info_' + this.vehicleState;
		}
	}
});


/*
 * L.QuadrotorMarker is used to display quadrotor UAVs on the map.
 */
L.QuadrotorMarker = L.Marker.extend({

	options : {
		icon : new L.QuadrotorIcon(),
		clickable : false,
		className : 'quadrotor-marker-icon'
	},
	
	vehicleId : '',
	vehicleState : '',

	setVehicleState : function(id, state) {
		if (this.vehicleId != id || this.vehicleState != state) {
			this.vehicleId = id;
			this.vehicleState = state;
			if (state == 'idle') {
				this.setIcon(new L.QuadrotorIcon({iconUrl : 'img/QuadrotorGreen_32.png'}));
			} else {
				this.setIcon(new L.QuadrotorIcon({iconUrl : 'img/QuadrotorBlack_32.png'}));
			}
			this.options.icon.setVehicleState(id, state);
		}		
		
		return this;
	},

	setTaskState : function(task, state) {
		this.options.icon.setTaskState(task, state)
		return this;
	},

});


L.quadrotorMarker = function (latlng, options) {
    return new L.QuadrotorMarker(latlng, options);
};
