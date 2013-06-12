package me.tatetian.scene;

import me.tatetian.Engine;

public abstract class Scene {
	public Scene() {
		setup();
	}
	
	protected abstract void setup();
	
	public abstract void draw();
	public abstract void transit(Scene fromScene);
}
