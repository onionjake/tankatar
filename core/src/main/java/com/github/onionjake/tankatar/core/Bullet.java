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
import playn.core.Image;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
/**
 * Bullets
 */
public class Bullet extends DynamicPhysicsEntity {
  public static double FRICTION = 0.0;
  private static double BULLET_VELOCITY = 1000000;
	public double ax,ay;
	public Coordinate c;
  private Vec2 velocity;

  /**
   * Create a bullet for tank with starting position x,y,z
   * @param Tank, x, y, z
   */
	public Bullet(TankatarWorld tankatarWorld, World world, float x, float y, float angle, Coordinate dest) {
		super(tankatarWorld, world, x, y, angle);
		calcVelocity(x,y,dest);
//		setLinearVelocity(velocity);
  }
    
  public void calcVelocity(float x, float y, Coordinate dest) {
    // calc velocity components from position tank and dest positions.
    double xPos = x - dest.x, yPos = y - dest.y;
    double ratio = Math.abs(yPos / xPos);
    double calcX = BULLET_VELOCITY / (1 + ratio); 
    Math.sqrt(Math.abs(calcX));
    double calcY = calcX * ratio;
    if (xPos >= 0) calcX *= -1; 
    if (yPos >= 0) calcY *= -1; 
    setLinearVelocity((float)calcX,(float)calcY);
//		velocity = new Vec2((float)calcX,(float)calcY);
//		this.getBody().setLinearVelocity(velocity);
  } 
	@Override
	Body initPhysicsBody(World world, float x, float y, float angle) {
		this.x = x;
		this.y = y;
		z = 0;
		FixtureDef fixtureDef = new FixtureDef();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position = new Vec2(0, 0);
		Body body = world.createBody(bodyDef);

		CircleShape circleShape = new CircleShape();
		circleShape.m_radius = getRadius();
		fixtureDef.shape = circleShape;
	//	fixtureDef.density = 0.1f;
	//	fixtureDef.friction = 0.0f;
//		fixtureDef.restitution = 0.0f;
		circleShape.m_p.set(0, 0);
		body.createFixture(fixtureDef);
	//	body.setLinearDamping(0.0f);
		body.setTransform(new Vec2(x*PHYSICS_SCALE, y*PHYSICS_SCALE), angle);
		return body;
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		if (this == null)return;
		float vx = getBody().getLinearVelocity().x;
		float vy = getBody().getLinearVelocity().y;
		vx += ax *2* delta;
		vy += ay *2* delta;
    setLinearVelocity(vx,vy);
	//	getBody().setLinearVelocity(velocity);


  }

  public static double getFriction() {
    return FRICTION;
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
		return 5f;
	}

	@Override
	public Image getImage() {
		return image;
	}
	private static Image image = loadImage("bullet.png");
}


