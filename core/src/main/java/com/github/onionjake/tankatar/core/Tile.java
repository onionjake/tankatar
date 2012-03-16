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

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import playn.core.Image;
import playn.core.ImageLayer;

public class Tile extends StaticPhysicsEntity {
	public static final int TILE_HEIGHT = 100;
	public static final int TILE_WIDTH = 80;
	
	public Tile(final TankatarWorld tankatarWorld, World world, float x, float y, float angle) {
		super(tankatarWorld, world, x, y, angle);
	}


	@Override
	Body initPhysicsBody(World world, float x, float y, float angle) {
		FixtureDef fixtureDef = new FixtureDef();
	    BodyDef bodyDef = new BodyDef();
	    bodyDef.type = BodyType.STATIC;
	    bodyDef.position = new Vec2(0, 0);
	    Body body = world.createBody(bodyDef);

	    PolygonShape polygonShape = new PolygonShape();
	    Vec2[] polygon = new Vec2[4];
	    polygon[0] = new Vec2(-getWidth()/2f, -getHeight()/2f + getTopOffset());
	    polygon[1] = new Vec2(getWidth()/2f, -getHeight()/2f + getTopOffset());
	    polygon[2] = new Vec2(getWidth()/2f, getHeight()/2f);
	    polygon[3] = new Vec2(-getWidth()/2f, getHeight()/2f);
	    polygonShape.set(polygon, polygon.length);
	    fixtureDef.shape = polygonShape;
	    fixtureDef.friction = 0.1f;
	    fixtureDef.restitution = 0.8f;
	    body.createFixture(fixtureDef);
	    body.setTransform(new Vec2(x, y), angle);
	    return body;
	}

	@Override
	float getWidth() {
		return TILE_WIDTH;
	}

	@Override
	float getHeight() {
		return TILE_HEIGHT;
	}
	
	/**
	   * Return the size of the offset where the block is slightly lower than where
	   * the image is drawn for a depth effect
	   */
	  public float getTopOffset() {
	    return 35.0f ;
	  }

	@Override
	public Image getImage() {
		return image;
	}
	private static Image image = loadImage("block_grass.png");
}
