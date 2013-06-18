package me.tatetian.scene;

import processing.core.PGraphics;
import processing.core.PImage;
import me.tatetian.common.DrawableObject;
import me.tatetian.effects.Showable;

public class NebulaSceneBackground extends DrawableObject
												implements Showable {
	private PImage background;
	private float normal_z;
	
	public NebulaSceneBackground(PGraphics G, float x, float y, String imgPath, float z) {
		this(G, imgPath, x, y, z);
	}
	public NebulaSceneBackground(PGraphics G, String imgPath, float x, float y, float z) {
		super(G, x, y, z, 0, 255);
		this.background = E.loadImage(E.BASE_PATH + imgPath);
		this.normal_z = z;
//		z = 1.2f * z;
		this.MIN_ALPHA = 100;
	}
	
	public void show(int millis) {
		alpha = MIN_ALPHA;
//		z = 1.2f * normal_z;
//		move(z, normal_z, millis);
		fadeIn(millis);
	}

	public void hide(int millis) {
		alpha = MAX_ALPHA;
//		z = 1.2f * normal_z;
//		move(normal_z, z, millis);
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
