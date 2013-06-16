package me.tatetian.scene;

import java.util.HashMap;
import java.util.Map;

import me.tatetian.Engine;

public abstract class NebulaScene extends Scene {
	/* =======================================================================
	 * Nebulas Cache
	 * =======================================================================*/
	public static enum Name {
		MOON, M51, CHE2, VENUS, HIP_13454, 
		CHE3, LOU1, DA5, CHUAN3, TIAN1, HIP_10064,
		HIP_10670, LOU3 
	}
	public static Map<Name, NebulaScene> scene_cache;
	public static NebulaScene get(Name name) {
		if(scene_cache == null) 
			scene_cache = new HashMap<Name, NebulaScene>();
		NebulaScene scene = scene_cache.get(name);
		if(scene == null) {
			switch(name) {
			case M51:
				scene = new M51NebulaScene();
				break;
			default:
				// no implemented yet!
				return null;
			}
			scene_cache.put(name, scene);
		}
		return scene;
	}
	
	@Override
	protected void beforeDraw() {
		G.imageMode(G.CENTER);
		G.blendMode(G.ADD);
		G.hint(G.DISABLE_DEPTH_TEST);
		G.lights();
	}
	
	/**
	 * All nebulas scenes have the same transition
	 * */
	@Override
	public void transit(Scene fromScene) {
		
	}
}
