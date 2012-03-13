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

import playn.core.Image;
import playn.core.Surface;

public class TObject {

  public Image img;
  public double oldx, oldy, oldz;
  public double x, y, z;
  public double vx, vy, vz;
  public double ax, ay, az;
  public double r;

  int lastUpdated;
  boolean resting;

  public TObject(Image  img) {
    this.img = img;
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

  public void paint(Surface surf, float alpha) {
    surf.drawImage(img,(int)x,(int)y);
  }
}
