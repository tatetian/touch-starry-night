package me.tatetian.scene;

import processing.core.PGraphics;
import processing.core.PImage;
import me.tatetian.common.DrawableObject;
import me.tatetian.effects.Animation;
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
	
	public void fly(float from_x, float from_y, float to_x, float to_y, int millis) {
		addAnimation(new LoopMovingAnimation(this, from_x, from_y, to_x, to_y, millis));
	}
	
	private static class LoopMovingAnimation extends Animation {
		private NebulaSceneBackground bg;
		private float speed_x, speed_y;
		private float from_x, from_y;
		private float to_x, to_y;
		private int steps, step_period;
		
		public LoopMovingAnimation(NebulaSceneBackground bg, 
															 float from_x, float from_y,
															 float to_x, 	 float to_y, 
															 int millis) {
			super(-1);
			this.bg = bg;
			this.from_x = from_x; this.from_y = from_y;
			this.to_x   = to_x; 	this.to_y = to_y;
			
			step_period = millis * E.FRAME_RATE / 1000;
			speed_x = (to_x - from_x) / step_period;
			speed_y = (to_y - from_y) / step_period;
			steps = 0;
		}

		@Override
		public void update() {
			bg.x += speed_x;
			bg.y += speed_y;
			
			steps ++;
			if (steps == step_period) {
				steps = 0;
				speed_x *= -1;
				speed_y *= -1;
			}
		}
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
