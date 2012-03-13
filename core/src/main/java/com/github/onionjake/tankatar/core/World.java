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
import playn.core.Image;
import playn.core.ImageLayer;

import java.util.ArrayList;

public class World {
  public static final int TILE_WIDTH = 100;
  public static final int TILE_HEIGHT = 80;

  // In tiles
  public static final int WORLD_WIDTH = 20;
  public static final int WORLD_HEIGHT = 20;
  public static final int WORLD_TOP_OFFSET = 50;

  private ArrayList<Tile> world = new ArrayList<Tile>();
  private ArrayList<TObject> objects = new ArrayList<TObject>();
  Tank player;

  private GroupLayer worldLayer;

  public World(GroupLayer worldLayer) {
    this.worldLayer = worldLayer;
    for(int i=0;i<WORLD_WIDTH;i++) {
      for(int j=0;j<WORLD_WIDTH;j++) {
        ImageLayer l = graphics().createImageLayer(assets().getImage("block_grass.png"));
        world.add(new Tile(l, new Coordinate(i,j)));
        worldLayer.add(l);
      }
    }
  }

  public Tank newPlayer() {
    ImageLayer foo = graphics().createImageLayer(assets().getImage("redtank.png")); 
    worldLayer.add(foo);
    Tank bar = new Tank(foo,new Coordinate(10,10,0));
    objects.add(bar);
    player = bar;
    return bar;
  }

  public void update(float delta) {
    worldLayer.setTranslation(-(float)player.x+320, -(float)player.y + 240 );
    for (TObject t:objects) {
      t.update(delta);
    }
  }
}
