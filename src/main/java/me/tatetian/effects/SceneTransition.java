package me.tatetian.effects;

import me.tatetian.scene.Scene;

public abstract class SceneTransition extends Animation {
	protected Scene fromScene, toScene;
	
	public SceneTransition(int duration, Scene fromScene, Scene toScene) {
		super(duration);
		this.fromScene = fromScene;
		this.toScene 	 = toScene;
	}
}
