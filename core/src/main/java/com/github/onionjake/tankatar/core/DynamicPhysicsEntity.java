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


import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;


public abstract class DynamicPhysicsEntity extends TObject implements PhysicsEntity {
  // for calculating interpolation
  public float prevX, prevY, prevA;
  private Body body;
	public double ax,ay;
  public static final float PHYSICS_SCALE = 0.9f;

  public DynamicPhysicsEntity(TankatarWorld tankatarWorld, World world, float x, float y, float angle) {
    super(x, y, angle);
    body = initPhysicsBody(world, x, y, angle);
    setPos(x, y);
    setAngle(angle);
    ax=0;
    ay=0;
    tankatarWorld.addDynamicLayer(img);
  }

  abstract Body initPhysicsBody(World world, float x, float y, float angle);

  @Override
  public void paint(float alpha) {
    // interpolate based on previous state
    // TODO: not sure if this is the best place to set the TObject x and y
    x = (body.getPosition().x * alpha) + (prevX * (1f - alpha))/PHYSICS_SCALE;
    y = (body.getPosition().y * alpha) + (prevY * (1f - alpha))/PHYSICS_SCALE;
    float a = (body.getAngle() * alpha) + (prevA * (1f - alpha))/PHYSICS_SCALE;
    img.setTranslation(x, y);
    img.setRotation(a);
  }

  @Override
  public void update(float delta) {
    // store state for interpolation in paint()
    x = body.getPosition().x/PHYSICS_SCALE;
    y = body.getPosition().y/PHYSICS_SCALE;
    prevX = body.getPosition().x/PHYSICS_SCALE;
    prevY = body.getPosition().y/PHYSICS_SCALE;
    prevA = body.getAngle()/PHYSICS_SCALE;
  }

  public void setLinearVelocity(float x, float y) {
    body.setLinearVelocity(new Vec2(x*PHYSICS_SCALE, y*PHYSICS_SCALE));
  }

  public void setAngularVelocity(float w) {
    body.setAngularVelocity(w*PHYSICS_SCALE);
  }

  @Override
  public void setPos(float x, float y) {
    super.setPos(x, y);
    getBody().setTransform(new Vec2(x*PHYSICS_SCALE, y*PHYSICS_SCALE), getBody().getAngle());
    prevX = x;
    prevY = y;
  }

  @Override
  public void setAngle(float a) {
    super.setAngle(a);
    getBody().setTransform(getBody().getPosition(), a*PHYSICS_SCALE);
    prevA = a;
  }

  @Override
  public Body getBody() {
    return body;
  }
}
