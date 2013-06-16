package me.tatetian.effects;

import me.tatetian.scene.Scene;

public class ZoomFadeSceneTransition extends SceneTransition {
	private static final int MAX_D = 8 * E.WIN_D, MIN_D = 10;
	private float ts_from_z, ts_to_z;
	private float fs_from_z, fs_to_z;
	private int ts_from_alpha, ts_to_alpha;
	private int fs_from_alpha, fs_to_alpha;
	
	public ZoomFadeSceneTransition(int duration, 
																 Scene fromScene, Scene toScene, boolean direction) {
		super(duration, fromScene, toScene);
		// for fromScene
		fs_from_z = fromScene.z(); fs_to_z = direction ? MIN_D : MAX_D;
		fs_from_alpha = fromScene.alpha(); fs_to_alpha = 0;
		// for toScene
		ts_from_z = direction ? MAX_D : MIN_D; ts_to_z = toScene.z();
		ts_from_alpha = 0; ts_to_alpha = toScene.alpha();
	}

	@Override
	public void update() {
		float p = progress(), q = 1 - p;
		fromScene.z(p * fs_to_z + q * fs_from_z);
		fromScene.alpha((int) (p * fs_to_alpha + q * fs_from_alpha) );
		toScene.z(p * ts_to_z + q * ts_from_z);
		toScene.alpha((int) (p * ts_to_alpha + q * ts_from_alpha));
	}
	
	@Override
	public void end() {
		toScene.resetFromScene();
		fromScene.z(fs_from_z);
		fromScene.alpha(fs_from_alpha);
	}
}
