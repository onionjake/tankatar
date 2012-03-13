package com.github.onionjake.tankatar.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.github.onionjake.tankatar.core.Tankatar;

public class TankatarHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("tankatar/");
    PlayN.run(new Tankatar());
  }
}
