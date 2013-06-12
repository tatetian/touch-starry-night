package me.tatetian.scene;

import java.util.HashMap;
import java.util.Map;

import me.tatetian.Engine;

public abstract class NebulaScene extends Scene {
	/* =======================================================================
	 * Nebulas Cache
	 * =======================================================================*/
	public static enum Name {
		M51
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
				throw new RuntimeException("Can't get scene by name " + name);
			}
			scene_cache.put(name, scene);
		}
		return scene;
	}
	
	/**
	 * All nebulas scenes have the same transition
	 * */
	@Override
	public void transit(Scene fromScene) {
		
	}
	
	@Override
	public void draw() {		
		final Engine E = Engine.INSTANCE;
		E.hint(E.DISABLE_DEPTH_TEST);
		E.blendMode(E.ADD);
		_draw();
	}
	
	protected abstract void _draw(); 
}
