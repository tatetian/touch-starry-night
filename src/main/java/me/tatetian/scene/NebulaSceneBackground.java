package me.tatetian.scene;

import processing.core.PGraphics;
import processing.core.PImage;
import me.tatetian.common.DrawableObject;
import me.tatetian.effects.Showable;

public class NebulaSceneBackground extends DrawableObject
												implements Showable {
	private PImage background;
	private float normal_z;
	
	public NebulaSceneBackground(PGraphics G, String imgPath, float z) {
		this(G, imgPath, E.WIN_W / 2, E.WIN_H / 2, z);
	}
	public NebulaSceneBackground(PGraphics G, String imgPath, float x, float y, float z) {
		super(G, x, y, z, 0, 255);
		this.background = E.loadImage(E.BASE_PATH + imgPath);
		this.normal_z = z;
	}
	
	public void show(int millis) {
		alpha = 0;
		z = 1.2f * normal_z;
		move(z, normal_z, millis);
		fadeIn(millis);
	}

	public void hide(int millis) {
		alpha = 255;
		//z = normal_z;
		//move(normal_z, - E.WINDOW_DEPTH, millis);
		fadeOut(millis);
	}
	
	public void loop() {}

	@Override
	public void draw() {
		G.pushMatrix();
		G.translate(x, y, z);		
		G.rotateZ(angle);		
		G.tint(255, alpha);
		G.image(background, 0, 0);
		G.popMatrix();
	}
}
