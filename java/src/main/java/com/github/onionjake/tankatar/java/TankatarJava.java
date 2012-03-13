package com.github.onionjake.tankatar.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.github.onionjake.tankatar.core.Tankatar;

public class TankatarJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("com/github/onionjake/tankatar/resources");
    PlayN.run(new Tankatar());
  }
}
