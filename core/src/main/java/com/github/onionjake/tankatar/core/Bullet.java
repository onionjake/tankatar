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

import playn.core.ImageLayer;
import java.lang.Math;
/**
 * Bullets
 */
public class Bullet extends TObject {
  public static double FRICTION = 0.0;
  private static double BULLET_VELOCITY = 100;

  /**
   * Create a bullet for tank with starting position x,y,z
   * @param Tank, x, y, z
   */
  public Bullet(ImageLayer b, Tank t, Coordinate dest) {
    super(b, new Coordinate(t.x,t.y,t.z));

    // calc velocity components from position tank and dest positions.
    double xPos = t.x - dest.x, yPos = t.y - dest.y;
    double ratio = Math.abs(yPos / xPos);
    double calcX = BULLET_VELOCITY / (1 + ratio); 
    Math.sqrt(Math.abs(calcX));
    double calcY = calcX * ratio;
    if (xPos >= 0) calcX *= -1; 
    if (yPos >= 0) calcY *= -1; 
    setVelocity(calcX,calcY,0);
  }
    
  public double getFriction() {
    return FRICTION;
  }
}
