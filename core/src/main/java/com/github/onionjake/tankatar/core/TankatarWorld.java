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
import playn.core.Json;
import playn.core.util.Callback;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.ArrayList;

public class TankatarWorld implements ContactListener {
  public static final int TILE_WIDTH = 100;
  public static final int TILE_HEIGHT = 80;

  // In tiles
  public static final int WORLD_WIDTH = 10;
  public static final int WORLD_HEIGHT = 10;
  public static final int WORLD_TOP_OFFSET = 50;

  private ArrayList<Tile> world = new ArrayList<Tile>();
  private ArrayList<TObject> objects = new ArrayList<TObject>();
  Tank player;
  String playerid;
  Tank otherplayer;
  String otherplayerid;

  private GroupLayer worldLayer;

  // box2d object containing physics world
  protected World physicsWorld;

  public TankatarWorld(GroupLayer worldLayer) {
    playerid = "";
    otherplayerid = "";
    this.worldLayer = worldLayer;
    for(int i=0;i<WORLD_WIDTH;i++) {
      for(int j=0;j<WORLD_WIDTH;j++) {
        ImageLayer l = graphics().createImageLayer(assets().getImage("block_grass.png"));
        world.add(new Tile(l, new Coordinate(i,j)));
        worldLayer.add(l);
      }
    }
    // create the physics world
    Vec2 gravity = new Vec2(0.0f, 10.0f);
    physicsWorld = new World(gravity, true);
    physicsWorld.setWarmStarting(true);
    physicsWorld.setAutoClearForces(true);
    physicsWorld.setContactListener(this);
  }

  public Tank newPlayer() {
    net().get("http://localhost:4567/newplayer", new Callback<String>() {
      @Override
      public void onSuccess(String json) {
        System.out.println(json);
        Json.Object d = json().parse(json);
        ImageLayer foo = graphics().createImageLayer(assets().getImage(d.getString("color"))); 
        worldLayer.add(foo);
        Tank bar = new Tank(worldLayer,objects,foo,new Coordinate(10,10,0));
        objects.add(bar);
        player = bar;
        playerid = d.getString("player");
      }

      @Override
      public void onFailure(Throwable error) {
        System.err.println("Could not connect to server!  Run in single mode.");
        ImageLayer foo = graphics().createImageLayer(assets().getImage("redtank.png")); 
        worldLayer.add(foo);
        Tank bar = new Tank(worldLayer,objects,foo,new Coordinate(10,10,0));
        objects.add(bar);
        player = bar;
      }
    });
    return player;
  }

  public void update(float delta) {
    worldLayer.setTranslation(-(float)player.x+320, -(float)player.y + 240 );

    for (TObject t:objects) {
      t.update(delta);
    }
    String pos = "{ \"posx\": " + player.x + ", \"posy\": " + player.y + "}";

    net().post("http://localhost:4567/update/" + playerid, pos, new Callback<String>() {
      @Override
      public void onSuccess(String json) {
        System.out.println(json);
        Json.Array d = json().parseArray(json);
        if (d.length() == 0) return;
        Json.Object p;
        if (otherplayerid == "") {
          if (playerid == "player1") {
            otherplayerid = "player2";
          }
          else {
            otherplayerid = "player1";
          }
          p = d.getObject(0);
          ImageLayer foo = graphics().createImageLayer(assets().getImage(p.getString("color"))); 
          worldLayer.add(foo);
          otherplayer = new Tank(worldLayer,objects,foo,new Coordinate(0,0,0));
          objects.add(otherplayer);
        }
        p = d.getObject(0);
        otherplayer.setPos(p.getNumber("posx"),p.getNumber("posy"),0);
      }

      @Override
      public void onFailure(Throwable error) {
        System.err.println("bad request");
      }
    });
  }

@Override
public void beginContact(Contact contact) {
	// TODO Auto-generated method stub
	
}

@Override
public void endContact(Contact contact) {
	// TODO Auto-generated method stub
	
}

@Override
public void preSolve(Contact contact, Manifold oldManifold) {
	// TODO Auto-generated method stub
	
}

@Override
public void postSolve(Contact contact, ContactImpulse impulse) {
	// TODO Auto-generated method stub
	
}
}
