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
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Keyboard;
import playn.core.Pointer;
import playn.core.Sound;

public class Tankatar implements Game, Keyboard.Listener {

  private Sound ding;
  private float touchVectorX, touchVectorY;

  @Override
  public void init() {
    // create and add background image layer
    Image bgImage = assets().getImage("images/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);

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
    // the background automatically paints itself, so no need to do anything here!
  }

  @Override
  public void update(float delta) {
  }

  @Override
  public void onKeyDown(Keyboard.Event event) {
    ding.play();
  }

  @Override
  public void onKeyTyped(Keyboard.TypedEvent event) {
  }

  @Override
  public void onKeyUp(Keyboard.Event event) {
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
