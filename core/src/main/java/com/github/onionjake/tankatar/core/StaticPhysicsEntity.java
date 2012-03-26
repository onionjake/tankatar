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


import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;


public abstract class StaticPhysicsEntity extends TObject implements PhysicsEntity {
  private Body body;

  public StaticPhysicsEntity(TankatarWorld tankatarWorld, World world, float x, float y, float angle) {
    super(x, y, angle);
    body = initPhysicsBody(world, x, y, angle);
    tankatarWorld.addStaticLayerBack(img);
  }

  abstract Body initPhysicsBody(World world, float x, float y, float angle);

  @Override
  public void paint(float alpha) {
  }

  @Override
  public void update(float delta) {
  }

  @Override
  public void setPos(float x, float y) {
    throw new RuntimeException("Error setting position on static entity");
  }

  @Override
  public void setAngle(float a) {
    throw new RuntimeException("Error setting angle on static entity");
  }

  @Override
  public Body getBody() {
    return body;
  }
}
