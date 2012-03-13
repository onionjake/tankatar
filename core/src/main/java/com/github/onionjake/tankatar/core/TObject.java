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

public class TObject {

  public ImageLayer img;
  public double oldx, oldy, oldz;
  public double x, y, z;
  public double vx, vy, vz;
  public double ax, ay, az;
  public double r;
  private static double FRICTION = 1.0;

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

  public void update(float delta) {
      vx -= vx * this.FRICTION * delta;
      //if (vx < 0) vx = 0;

      vy -= vy * this.FRICTION * delta;
     // if (vy < 0) vy = 0;

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
    if (x > World.TILE_WIDTH*World.WORLD_WIDTH) x = World.TILE_WIDTH*World.WORLD_WIDTH;
    if (y > World.TILE_HEIGHT*World.WORLD_HEIGHT + World.WORLD_TOP_OFFSET) y = World.TILE_HEIGHT*World.WORLD_HEIGHT+ World.WORLD_TOP_OFFSET;
    if (x < 0) x = 0;
    if (y < World.WORLD_TOP_OFFSET) y = World.WORLD_TOP_OFFSET;



    updateLayer();
  }
}
