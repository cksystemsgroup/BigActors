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

function BigraphUpdater(bigraphId) {
	this.bigraphId = bigraphId;
	this.div = document.getElementById(bigraphId);
	this.img = document.createElement('img');
	this.div.appendChild(this.img);
	this.currentImgName = '';
}

BigraphUpdater.prototype.refresh = function(data) {
	if (this.currentImgName == data.img) {
		return;
	}
	
	this.currentImgName = data.img;
	this.img.src = 'bigraph/img/' + this.currentImgName;
}
