package me.tatetian.common;

import me.tatetian.Engine;

public abstract class Drawable {
	public static Engine E;
	public static void setEngine(Engine e) {
		E = e;
	}
	
	public abstract void draw();
}
