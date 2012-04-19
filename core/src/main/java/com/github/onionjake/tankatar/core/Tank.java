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
import playn.core.GroupLayer;
import pythagoras.f.AbstractPoint;
import playn.core.Layer.Util;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 * Tanks represent players in the game
 * Properties: health, score
 */
public class Tank extends DynamicPhysicsEntity {

	public static double FRICTION = 1.0;
	private double health;
	private double score;
	private GroupLayer worldLayer;
	private ArrayList<TObject> objects = new ArrayList<TObject>();
	public Coordinate c;
  public TankatarWorld tankatarWorld;
  public World world;
  public int bulletDelay = 0;
	/**
	 *
	 * Tank Constuctor
	 */
	public Tank(TankatarWorld tankatarWorld, World world, float x, float y, float angle) {
		super(tankatarWorld, world, x, y, angle);
		ax =0;
		ay = 0;
    this.world = world;
    this.tankatarWorld = tankatarWorld;
	}

	@Override
	Body initPhysicsBody(World world, float x, float y, float angle) {
		this.x = x;
		this.y = y;
		z = 0;
		c = new Coordinate((double)x,(double)y,(double)z);
		FixtureDef fixtureDef = new FixtureDef();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position = new Vec2(0, 0);
		Body body = world.createBody(bodyDef);

		CircleShape circleShape = new CircleShape();
		circleShape.m_radius = getRadius();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0.4f;
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0.35f;
		circleShape.m_p.set(0, 0);
		body.createFixture(fixtureDef);
		body.setLinearDamping(0.2f);
		body.setTransform(new Vec2(x*PHYSICS_SCALE, y*PHYSICS_SCALE), angle*PHYSICS_SCALE);
		return body;
	}


	
public TObject shoot(Coordinate dest) {
    Bullet  b = new Bullet(tankatarWorld,world,getBody().getPosition().x+35,getBody().getPosition().y+35,0,dest);
    return b;
  }  
	 
	public static double getFriction() {
		return FRICTION;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (this == null)return;
		float vx = getBody().getLinearVelocity().x/PHYSICS_SCALE;
		float vy = getBody().getLinearVelocity().y/PHYSICS_SCALE;
		// Friction on TObject
		vx -= vx * this.getFriction() * delta;
		vy -= vy * this.getFriction() * delta;
		//  if(this instanceof Tank) {
		//     vx -= vx * 1.0 * delta;
		//      vy -= vy * 1.0 * delta;
		//   }



		vx += ax *2* delta;
		vy += ay *2* delta;
		Vec2 v = new Vec2(vx*PHYSICS_SCALE,vy*PHYSICS_SCALE);
		this.getBody().setLinearVelocity(v);

		x += vx * delta;
		y += vy * delta;
		c = new Coordinate(x,y,z);
    System.out.println(x + " " + y);
    System.out.println(getBody().getPosition().x + " " + getBody().getPosition().y);
		updateLayer();
	}

	private void updateLayer()
	{
		img.setTranslation((float)x,(float)y);
}


	@Override
	float getWidth() {
		return 2 * getRadius();
	}

	@Override
	float getHeight() {
		return 2 * getRadius();
	}

	private float getRadius() {
		return 10f;
	}

	@Override
	public Image getImage() {
		return image;
	}
	private static Image image = loadImage("redtank.png");
}

