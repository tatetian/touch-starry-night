package me.tatetian.scene;

import me.tatetian.common.DrawableObject;

public abstract class Scene extends DrawableObject {
	public Scene() {
		super(null, E.WIN_W / 2, E.WIN_H / 2, E.WIN_D, 0, 255);
		initGraphics();
		setup();
	}
	
	private void initGraphics() {		
		final int w = E.WIN_W, h = E.WIN_H, d = E.WIN_D;
		G = E.createGraphics(w, h, E.P3D);
		G.perspective(E.FOV, (float)w/h, 1, 10 * d); 
	  G.camera(w/2, h/2, 1, w/2, h/2, 0, 0, 1, 0); 
	}
	
	protected abstract void setup();
	
	@Override
	public final void draw() {
		beforeDraw();
		G.beginDraw();
		drawGraphics();
		G.endDraw();
		afterDraw();		
		
		E.pushMatrix();
		transform();
		E.image(G, 0, 0);
		E.popMatrix();
	}
	
	@Override
	public void transform() {
		E.translate(x, y, z);	
		E.rotateZ(angle);
	}
	
	protected void beforeDraw() {
	  G.imageMode(G.CENTER);
	  G.smooth(4);
		G.lights();
	}
	
	protected void afterDraw() {
		// noop
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
