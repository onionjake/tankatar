/** Copyright (c) 2012 Jake Willoughby, Dan Willoughby

    This file is part of tankatar.

tankatar is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
any later version.

tankatar is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with tankatar.  See gpl3.txt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.onionjake.tankatar.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public abstract class TObject {
	final ImageLayer img;
	float x, y, z,angle;

	public TObject(float px, float py, float pangle) {
		this.x = px;
		this.y = py;
		this.angle = pangle;
		img = graphics().createImageLayer(getImage());
		getImage().addCallback(new ResourceCallback<Image>() {
			@Override
			public void done(Image image) {
				// since the image is loaded, we can use its width and height
				img.setWidth(image.width());
				img.setHeight(image.height());
				img.setOrigin(image.width() / 2f, image.height() / 2f);
				img.setTranslation(x, y);
				img.setRotation(angle);
			}

			@Override
			public void error(Throwable err) {
				PlayN.log().error("Error loading image: " + err.getMessage());
			}
		});
	}

	public void zero() {
		x = 50;
		y = 50;
		z = 0;
	}

	public TObject(ImageLayer img,Coordinate c) {
		zero();
		this.img = img;
		this.x   = (float) c.x;
		this.y   = (float) c.y;
		this.z   = (float) c.z;
		updateLayer();
	}

	private void updateLayer()
	{
		img.setTranslation(x,y);
	}


	public void paint(float alpha) {
	}

	public void update(float delta) {
	}

	public void setPos(float x, float y) {
		img.setTranslation(x, y);
	}

	public void setAngle(float a) {
		img.setRotation(a);
	}

	abstract float getWidth();

	abstract float getHeight();

	public abstract Image getImage();

	protected static Image loadImage(String name) {
		return assets().getImage("" + name);
	}
}
