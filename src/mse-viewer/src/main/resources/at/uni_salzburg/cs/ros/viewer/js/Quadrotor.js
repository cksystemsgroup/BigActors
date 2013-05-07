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
		task : '',
		taskState : 'none',
		shadowUrl : null,
		iconSize : new L.Point(32, 32),
		iconAnchor : new L.Point(16, 16),
		popupAnchor : new L.Point(1, -16),
		className : 'quadrotor-icon'
	},

	createIcon : function() {
		var div = document.createElement('div');
		var img = this._createImg(this.options['iconUrl']);
		var taskdiv = document.createElement('div');
		taskdiv.setAttribute("class", "task");
		taskdiv.innerHTML = this.options['task'] || '';
		taskdiv.className = 'task_info_' + this.options['taskState'];
		div.appendChild(img);
		div.appendChild(taskdiv);
		this._setIconStyles(div, 'icon');
		return div;
	},

	createShadow : function() {
		return null;
	}
});


/*
 * L.QuadrotorMarker is used to display quadrotor UAVs on the map.
 */
L.QuadrotorMarker = L.Marker.extend({

	options : {
		icon : new L.QuadrotorIcon(),
		clickable : false,
		vehicleState : 'none',
		taskState : 'none',
		className : 'quadrotor-marker-icon'
	},

	setVehicleState : function(state) {
		
		if (this.options.vehicleState != state) {
			this.options.vehicleState = state;
			if (state != 'none') {
				this.setIcon(new L.QuadrotorIcon({iconUrl : 'img/QuadrotorRed_32.png'}));
			} else {
				this.setIcon(new L.QuadrotorIcon({iconUrl : 'img/QuadrotorBlack_32.png'}));
			}
		}		
		
		return this;
	},

	setTaskState : function(state) {
		this.options.taskState = state;

		return this;
	},

});


L.quadrotorMarker = function (latlng, options) {
    return new L.QuadrotorMarker(latlng, options);
};
