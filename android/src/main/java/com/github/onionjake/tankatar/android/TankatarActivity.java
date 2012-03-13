package com.github.onionjake.tankatar.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.github.onionjake.tankatar.core.Tankatar;

public class TankatarActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("com/github/onionjake/tankatar/resources");
    PlayN.run(new Tankatar());
  }
}
