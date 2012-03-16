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

	public TObject(final TankatarWorld tankatarWorld, float px, float py, float pangle) {
		this.x = px;
		this.y = py;
		this.angle = pangle;
		img = graphics().createImageLayer(getImage());
		initPreLoad(tankatarWorld);
		getImage().addCallback(new ResourceCallback<Image>() {
			@Override
			public void done(Image image) {
				// since the image is loaded, we can use its width and height
				img.setWidth(image.width());
				img.setHeight(image.height());
				img.setOrigin(image.width() / 2f, image.height() / 2f);
				img.setScale(getWidth() / image.width(), getHeight() / image.height());
				img.setTranslation(x, y);
				img.setRotation(angle);
				initPostLoad(tankatarWorld);
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
	//	resting = true;
		updateLayer();
	}

	private void updateLayer()
	{
		img.setTranslation((float)x,(float)y);
	}


	/**
	 * Perform pre-image load initialization (e.g., attaching to PeaWorld layers).
	 *
	 * @param peaWorld
	 */
	public abstract void initPreLoad(final TankatarWorld tankatarWorld);

	/**
	 * Perform post-image load initialization (e.g., attaching to PeaWorld layers).
	 *
	 * @param peaWorld
	 */
	public abstract void initPostLoad(final TankatarWorld tankatarWorld);

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

/*
public abstract class TObject {

  public ImageLayer img;
  public double oldx, oldy, oldz;
  public double x, y, z, angle;
  public double vx, vy, vz;
  public double ax, ay, az;
  public double r;

  int lastUpdated;
  boolean resting;

  public void zero() {
    x = 0;
    y = 0;
    z = 0;
    ax = 0;
    ay = 0;
    az = 0;
    vx = 0;
    vy = 0;
    vz = 0;
  }

  public TObject(final TankatarWorld tankatarWorld, float px, float py, float pangle) {
	    this.x = px;
	    this.y = py;
	    this.angle = pangle;
	    img = graphics().createImageLayer(getImage());
	    initPreLoad(tankatarWorld);
	    getImage().addCallback(new ResourceCallback<Image>() {
	      @Override
	      public void done(Image image) {
	        // since the image is loaded, we can use its width and height
	        img.setWidth(image.width());
	        img.setHeight(image.height());
	        img.setOrigin(image.width() / 2f, image.height() / 2f);
	        img.setScale(getWidth() / image.width(), getHeight() / image.height());
	  //      img.setTranslation(x, y);
	  //      img.setRotation(angle);
	        initPostLoad(tankatarWorld);
	      }

	      @Override
	      public void error(Throwable err) {
	        PlayN.log().error("Error loading image: " + err.getMessage());
	      }
	    });
	  }

  public TObject(ImageLayer img) {
    this.img = img;
    resting = true;
    zero();
  }

  public TObject(ImageLayer img,Coordinate c) {
    zero();
    this.img = img;
    this.x   = c.x;
    this.y   = c.y;
    this.z   = c.z;
    resting = true;
    updateLayer();
  }

  private void updateLayer()
  {
    img.setTranslation((float)x,(float)y);
  }

  public boolean isResting() {
    return resting;
  }

  public void setAcceleration(double ax, double ay, double az) {
    this.ax = ax;
    this.ay = ay;
    this.az = az;
  }

  public void setPos(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
    updateLayer();
  }

  public void setVelocity(double vx, double vy, double vz) {
    this.vx = vx;
    this.vy = vy;
    this.vz = vz;
  }

  public void saveOldPos() {
    this.oldx = x;
    this.oldy = y;
    this.oldz = z;
  }

  public double x(double alpha) {
    return x * alpha + oldx * (1.0f - alpha);
  }

  public double y(double alpha) {
    return y * alpha + oldy * (1.0f - alpha);
  }

  public double z(double alpha) {
    return z * alpha + oldz * (1.0f - alpha);
  }

 /* public void update(float delta) {
    // Friction on TObject
   //   vx -= vx * this.getFriction() * delta;
   //   vy -= vy * this.getFriction() * delta;
   if(this instanceof Tank) {
      vx -= vx * 1.0 * delta;
      vy -= vy * 1.0 * delta;
   }
   else if(this instanceof Bullet) {
      vx -= vx * 0.000000005 * delta;
      vy -= vy * 0.000000005 * delta;
   }
      if (vz < 0) {
        vz = 0;
      }
    vx += ax * delta;
    vy += ay * delta;
    vz += az * delta;

    x += vx * delta;
    y += vy * delta;
    z += vz * delta;

    // Don't let them go off the edge
    if (x > TankatarWorld.TILE_WIDTH*TankatarWorld.WORLD_WIDTH){
      x = TankatarWorld.TILE_WIDTH*TankatarWorld.WORLD_WIDTH;
    }
    if (y > TankatarWorld.TILE_HEIGHT*TankatarWorld.WORLD_HEIGHT + TankatarWorld.WORLD_TOP_OFFSET) {
      y = TankatarWorld.TILE_HEIGHT*TankatarWorld.WORLD_HEIGHT+ TankatarWorld.WORLD_TOP_OFFSET;
    }
    if (x < 0) {
      x = 0;
    }
    if (y < TankatarWorld.WORLD_TOP_OFFSET) {
      y = TankatarWorld.WORLD_TOP_OFFSET;
    }

    updateLayer();
  }
  /**
 * Perform pre-image load initialization (e.g., attaching to PeaWorld layers).
 *
 * @param peaWorld
 */
/*public abstract void initPreLoad(final TankatarWorld tankatarWorld);

  /**
 * Perform post-image load initialization (e.g., attaching to PeaWorld layers).
 *
 * @param peaWorld
 */
/*public abstract void initPostLoad(final TankatarWorld tankatarWorld);

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
}*/
