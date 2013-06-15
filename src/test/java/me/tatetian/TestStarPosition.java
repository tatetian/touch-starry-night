package me.tatetian;

import me.tatetian.common.Drawable;
import me.tatetian.effects.Animation;
import me.tatetian.stars.Nebula;
import me.tatetian.stars.SpriteStarsRenderer;
import me.tatetian.stars.Star;
import me.tatetian.stars.Stars;

public class TestStarPosition extends Engine {
	private Nebula nebula;
	
	public void setup() {		
		Drawable.setEngine(this);
		Animation.setEngine(this);
		
		// init processing
		size(WINDOW_WIDTH, WINDOW_HEIGHT, P3D);
		background(0);
		smooth();
//		imageMode(CENTER);
	  frameRate(FRAME_RATE);
	  
	  float fov = PI/3;
	  float cameraZ = (height/2) / tan(fov/2);
	  assert(10 * cameraZ == WINDOW_DEPTH);
	  perspective(fov, (float)width/height, cameraZ/10, cameraZ*10); 
	  camera(width/2, height/2, cameraZ / 10, width/2, height/2, 0, 0, 1, 0); 
	  
	  background(0);
	  nebula = new TestNebula(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 0);
	}
	
	public void draw() {
		background(0);
		lights();
		
		blendMode(ADD);
		hint(DISABLE_DEPTH_TEST);
	
		pushMatrix();
		translate(WINDOW_WIDTH / 2, WINDOW_HEIGHT /2, 0);
		box(0.5f);
		popMatrix();
		
		nebula.draw();
	}

	public static class TestNebula extends Nebula {
		public TestNebula(float x, float y, float z) {
			super(x, y, z);
		}

		@Override
		public Stars[] generateStars() {
			Stars[] stars = new Stars[1];
			stars[0] = new Stars(
					new Star[] { new Star(0, 0, 0) },
					new SpriteStarsRenderer(E, "small_stars/1.png", 10) );
			
			return stars;
		}
	}
}
