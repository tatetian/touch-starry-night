package me.tatetian.common;

import processing.core.PGraphics;
import me.tatetian.Engine;

public abstract class Drawable {
	public static Engine E;
	public static void setEngine(Engine e) {
		E = e;
	}

	protected PGraphics G;
	public Drawable(PGraphics G) {
		this.G = G;
	}
	
	public abstract void draw();
}
