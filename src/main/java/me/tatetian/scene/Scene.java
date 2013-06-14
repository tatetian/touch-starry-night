package me.tatetian.scene;

import me.tatetian.Engine;
import me.tatetian.common.Drawable;

public abstract class Scene extends Drawable {
	public Scene() {
		setup();
	}
	
	protected abstract void setup();
	
	public abstract void draw();
	public abstract void transit(Scene fromScene);
}
