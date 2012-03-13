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

    ding.play();
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
