package me.tatetian.scene;

import processing.core.PImage;
import me.tatetian.common.DrawableObject;
import me.tatetian.effects.Showable;

public class NebulaSceneBackground extends DrawableObject
												implements Showable {
	private PImage background;
	private float normal_z;
	
	public NebulaSceneBackground(String imgPath, float z) {
		this(imgPath, E.WINDOW_WIDTH / 2, E.WINDOW_HEIGHT / 2, z);
	}
	public NebulaSceneBackground(String imgPath, float x, float y, float z) {
		super(x, y, z, 0, 255);
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
		E.pushMatrix();
		E.translate(x, y, z);		
		E.rotateZ(angle);		
		E.tint(255, alpha);
		E.image(background, 0, 0);
		E.popMatrix();
	}
}
