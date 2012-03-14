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

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.GroupLayer;
import java.util.ArrayList;

/**
 * Tanks represent players in the game
 * Properties: health, score
 */
public class Tank extends TObject {
  
  public static double FRICTION = 1.0;
  private double health;
  private double score;
  private GroupLayer worldLayer;
  private ArrayList<TObject> objects = new ArrayList<TObject>();

  /**
   *
   * Tank Constuctor
   */
  public Tank(GroupLayer worldLayer,ArrayList<TObject> objects,ImageLayer tank, Coordinate c) {
    super(tank,c);
    this.worldLayer = worldLayer;
    this.objects = objects;
  }

  public TObject shoot(Coordinate dest) {
    ImageLayer image = graphics().createImageLayer(assets().getImage("bullet.png")); 
    worldLayer.add(image);
    Bullet b = new Bullet(image,this,dest);
    objects.add(b);
    return b;
  }  

  public static double getFriction() {
    return FRICTION;
  }
}

