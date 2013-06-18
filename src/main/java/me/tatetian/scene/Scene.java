package me.tatetian.scene;

import me.tatetian.common.DrawableObject;

public abstract class Scene extends DrawableObject {
	public static enum Name {
		// main scene
		MAIN,	
		// nebula scenes
		MOON, M51, CHE2, VENUS, HIP_13454, 
		CHE3, LOU1, DA5, CHUAN3, TIAN1, HIP_10064,
		HIP_10670, LOU3 
	}
	
	protected Name name;
	
	public Scene(Name name) {
		super(null, E.WIN_W / 2, E.WIN_H / 2, E.WIN_D, 0, 255);
		this.name = name;
		initGraphics();
		setup();
	}
	
	private void initGraphics() {		
		final int w = E.WIN_W, h = E.WIN_H, d = E.WIN_D;
		G = E.createGraphics(w, h, E.P3D);
		G.perspective(E.FOV, (float)w/h, 1, 10 * d); 
	  G.camera(w/2, h/2, 1, w/2, h/2, 0, 0, 1, 0);
	}
	
	public abstract void show();
	public void hide() {}
	
	protected abstract void setup();
	
	protected Scene fromScene = null;
	
	@Override
	public final void draw() {
//		final int w = E.WIN_W, h = E.WIN_H, d = E.WIN_D;
//		G.perspective(E.FOV, (float)w/h, 1, 10 * d); 
//	  G.camera(w/2, h/2, 1, w/2, h/2, 0, 0, 1, 0);

		beforeDraw();
		
		G.beginDraw();
		drawGraphics();
		G.endDraw();
		
		E.pushMatrix();
		transform();
		E.tint(E.color(255, alpha));
		E.image(G, 0, 0);
		E.popMatrix();
				
		afterDraw();		
	}
	
	@Override
	public void transform() {
		E.translate(x, y, z);	
		E.rotateZ(angle);
	}
	
	protected void beforeDraw() {
	  G.imageMode(G.CENTER);
		G.lights();
	}
	
	protected void afterDraw() {
		if(fromScene != null)
			fromScene.draw();
	}
	
	public void resetFromScene() {
		fromScene = null;
	}
	
	protected abstract void drawGraphics(); 

	public abstract void transit(Scene fromScene);
	
	public void click(int mouseX, int mouseY) {
		// no op
	}
	
	public void press(char key) {
		// no op
	}
}
