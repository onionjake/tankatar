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

import playn.core.Game;
import playn.core.Surface;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.ImageLayer;
import playn.core.Keyboard;
import playn.core.Pointer;
import playn.core.Sound;

import java.util.ArrayList;

public class Tankatar implements Game, Keyboard.Listener {

  private Sound ding;
  private float frameAlpha;
  private float touchVectorX, touchVectorY;
  private ImmediateLayer gameLayer;
  private World world;

  private boolean controlLeft, controlRight, controlUp, controlDown;

  private ArrayList<Tank> players = new ArrayList<Tank>();

  @Override
  public void init() {

    world = new World();
    players.add(world.newPlayer());
    // create and add background image layer
    gameLayer = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
      public void render(Surface surface) {
        surface.clear();
        world.paint(surface, frameAlpha);
      }
    });
    graphics().rootLayer().add(gameLayer);

    keyboard().setListener(this);
    pointer().setListener(new Pointer.Listener() {
      @Override
      public void onPointerEnd(Pointer.Event event) {
        touchVectorX = touchVectorY = 0;
      }
      @Override
      public void onPointerDrag(Pointer.Event event) {
        touchMove(event.x(), event.y());
      }
      @Override
      public void onPointerStart(Pointer.Event event) {
        touchMove(event.x(), event.y());
      }
    });

    ding = assets().getSound("ding");

  }

  @Override
  public void paint(float alpha) {
  }

  @Override
  public void update(float delta) {
    for (Tank t:players) {

      if (t.isResting()) {
        // Keyboard control.
        if (controlLeft) {
          t.ax = -1.0;
        }
        if (controlRight) {
          t.ax = 1.0;
        }
        if (controlUp) {
          t.ay = -1.0;
        }
        if (controlDown) {
          t.ay = 1.0;
        }

        // Mouse Control.
        t.ax += touchVectorX;
        t.ay += touchVectorY;
      }
    }
    world.update(delta/100);
  }

  @Override
  public void onKeyDown(Keyboard.Event event) {
    System.out.println("Key Down");
    switch (event.key()) {
      case SPACE:
        players.get(0).x = 100;
        break;
      case LEFT:
        controlLeft = true;
        break;
      case UP:
        controlUp = true;
        break;
      case RIGHT:
        controlRight = true;
        break;
      case DOWN:
        controlDown = true;
        break;
    }
  }

  @Override
  public void onKeyTyped(Keyboard.TypedEvent event) {
  }

  @Override
  public void onKeyUp(Keyboard.Event event) {
    switch (event.key()) {
      case LEFT:
        controlLeft = false;
        break;
      case UP:
        controlUp = false;
        break;
      case RIGHT:
        controlRight = false;
        break;
      case DOWN:
        controlDown = false;
        break;
    }
  }

  private void touchMove(float x, float y) {
    float cx = graphics().screenWidth() / 2;
    float cy = graphics().screenHeight() / 2;

    touchVectorX = (x - cx) * 1.0f / cx;
    touchVectorY = (y - cy) * 1.0f / cy;
  }

  @Override
  public int updateRate() {
    return 25;
  }
}
