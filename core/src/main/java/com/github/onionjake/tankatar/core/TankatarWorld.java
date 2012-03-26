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
import java.util.List;
import java.util.HashMap;
import java.util.Stack;

public class TankatarWorld implements ContactListener {
	public static final int TILE_WIDTH = 100;
	public static final int TILE_HEIGHT = 80;

	// In tiles
	public static final int WORLD_WIDTH = 10;
	public static final int WORLD_HEIGHT = 10;
	public static final int WORLD_TOP_OFFSET = 50;

	private HashMap<Body, PhysicsEntity> bodyEntityLUT = new HashMap<Body, PhysicsEntity>();
	private Stack<Contact> contacts = new Stack<Contact>();

	private ArrayList<BackgroundTile> backgroundTiles = new ArrayList<BackgroundTile>();
	private ArrayList<TObject> objects = new ArrayList<TObject>();
	Tank player;
	String playerid;
	Tank otherplayer;
	String otherplayerid;

	private GroupLayer staticLayerBack;
	private GroupLayer dynamicLayer;
	private GroupLayer staticLayerFront;
	private GroupLayer worldLayer;

	// box2d object containing physics world
	protected World physicsWorld;

	public TankatarWorld(GroupLayer worldLayer) {
		playerid = "";
		otherplayerid = "";

		staticLayerBack = graphics().createGroupLayer();
		worldLayer.add(staticLayerBack);
		dynamicLayer = graphics().createGroupLayer();
		worldLayer.add(dynamicLayer);
		staticLayerFront = graphics().createGroupLayer();
		worldLayer.add(staticLayerFront);
    this.worldLayer = worldLayer;
		for(int i=0;i<WORLD_WIDTH;i++) {
			for(int j=0;j<WORLD_WIDTH;j++) {
				ImageLayer l = graphics().createImageLayer(assets().getImage("block_grass.png"));
				backgroundTiles.add(new BackgroundTile(l, new Coordinate(i,j)));
				staticLayerBack.add(l);
			}
		}
		// create the physics world
		Vec2 gravity = new Vec2(0.0f, 0.0f);
		physicsWorld = new World(gravity, true);
		physicsWorld.setWarmStarting(true);
		physicsWorld.setAutoClearForces(true);
		physicsWorld.setContactListener(this);
		Tile foo = new Tile(this, physicsWorld,200, 100 , 0);
		add(foo);
		// create the walls
		Body wallLeft = physicsWorld.createBody(new BodyDef());
		PolygonShape wallLeftShape = new PolygonShape();
		wallLeftShape.setAsEdge(new Vec2(0, 0), new Vec2(0, getHeight()));
		wallLeft.createFixture(wallLeftShape, 0.0f);
		Body wallRight = physicsWorld.createBody(new BodyDef());
		PolygonShape wallRightShape = new PolygonShape();
		wallRightShape.setAsEdge(new Vec2(getWidth(), 0), new Vec2(getWidth(), getHeight()));
		wallRight.createFixture(wallRightShape, 0.0f);
		Body wallTop = physicsWorld.createBody(new BodyDef());
		PolygonShape wallTopShape = new PolygonShape();
		wallTopShape.setAsEdge(new Vec2(0, 0), new Vec2(getWidth(),0));
		wallTop.createFixture(wallTopShape, 0.0f);
		Body wallBottom = physicsWorld.createBody(new BodyDef());
		PolygonShape wallBottomShape = new PolygonShape();
		wallBottomShape.setAsEdge(new Vec2(0,getHeight() ), new Vec2(getWidth(), getHeight()));
		wallBottom.createFixture(wallBottomShape, 0.0f);
	}

  public static int getHeight() {
    return WORLD_HEIGHT * TILE_HEIGHT;
  }
  public static int getWidth() {
    return WORLD_WIDTH * TILE_WIDTH;
  }

  public void addDynamicLayer(ImageLayer img) {
    dynamicLayer.add(img);
  }
  public void addStaticLayerBack(ImageLayer img) {
    staticLayerBack.add(img);
  }

	public Tank newPlayer(float x, float y) {
		Tank bar = new Tank(this, physicsWorld, x, y, 0);
		add(bar);
		return bar;
	}

	public Pea newPea(float x, float y) {
		Pea bar = new Pea(this, physicsWorld, x, y, 0);
		add(bar);
		return bar;
	}

	public Tank setPlayer(Tank player) {
		this.player = player;
		return player;
	}

	public void update(float delta) {
		for (TObject t:objects) {
			t.update(delta); 
		}
		// the step delta is fixed so box2d isn't affected by framerate
		physicsWorld.step(0.033f, 10, 10);
		processContacts();
		
		String pos = "{ \"posx\": " + player.x + ", \"posy\": " + player.y + "}";
    // Screen size
    worldLayer.setTranslation(-player.x + 320, -player.y + 240);
	}

	public void paint(float delta) {
		for (TObject t : objects) {
			t.paint(delta);
		}
    // Screen size
    worldLayer.setTranslation(-player.x + 320, -player.y + 240);
	}

	public void add(TObject entity) {
		objects.add(entity);
		if (entity instanceof PhysicsEntity) {
			PhysicsEntity physicsEntity = (PhysicsEntity) entity;
			bodyEntityLUT.put(physicsEntity.getBody(), physicsEntity);
		}
	}

	// handle contacts out of physics loop
	public void processContacts() {
		while (!contacts.isEmpty()) {
			Contact contact = contacts.pop();

			// handle collision
			PhysicsEntity entityA = bodyEntityLUT.get(contact.m_fixtureA.m_body);
			PhysicsEntity entityB = bodyEntityLUT.get(contact.m_fixtureB.m_body);

			if (entityA != null && entityB != null) {
				if (entityA instanceof PhysicsEntity.HasContactListener) {
					((PhysicsEntity.HasContactListener) entityA).contact(entityB);
				}
				if (entityB instanceof PhysicsEntity.HasContactListener) {
					((PhysicsEntity.HasContactListener) entityB).contact(entityA);
				}
			}
		}
	}
	@Override
	public void beginContact(Contact contact) {
		contacts.push(contact);

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
