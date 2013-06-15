package me.tatetian.scene;

import me.tatetian.common.Drawable;
import me.tatetian.common.DrawableObject;
import me.tatetian.common.Positionable;
import me.tatetian.effects.Animation;
import me.tatetian.effects.TransparencyAnimation;
import processing.core.PGraphics;
import processing.core.PImage;
import me.tatetian.scene.NebulaScene.Name;

public class MainScene extends Scene {
	private Background background;
	private BigStar[] stars;
	private Controller controller;
	
	@Override
	protected void setup() {
		background = new Background(E.WINDOW_WIDTH / 2, E.WINDOW_HEIGHT / 2, 
																E.WINDOW_DEPTH);
		stars			 = BigStar.getStars();
		controller = new Controller(this);
	}
	
	@Override
	public void draw() {		
//		E.blendMode(E.ADD);
		background.draw();
		for(BigStar star : stars) 
			star.draw();
	}
	
	@Override
	public void press(char key) {
		controller.press(key);
	}

	@Override
	public void transit(Scene fromScene) {
		
	}
	
	private static PImage loadImage(String fileName) {
		return E.loadImage(E.BASE_PATH + "main_scene/" + fileName);
	}
	
	private static class Background extends Drawable implements Positionable {
		private PImage trees, hills, mountain, sky, grass;
		private PGraphics canvas;
		private float x, y, z;
		
		public Background(float x, float y, float z) {
			this.x = x; this.y = y; this.z = z;
			
			sky		= loadImage("sky.png");
			mountain = loadImage("mountain.png");
			trees = loadImage("trees.png");
			hills = loadImage("hills.png");
			grass = loadImage("grass.png");
			
			canvas = E.createGraphics(E.WINDOW_WIDTH, E.WINDOW_HEIGHT, E.P2D);
			canvas.smooth(4);
		}
		
		@Override
		public void draw() {		
			// update canvas
			canvas.beginDraw();
			showImage(sky);
			showImage(mountain);
			showImage(hills);
			showImage(trees);
			showImage(grass);
			canvas.endDraw();

			// draw on window
			E.pushMatrix();
			E.translate(x, y, z);
			E.image(canvas, 0, 0, E.WINDOW_WIDTH, E.WINDOW_HEIGHT);
			E.popMatrix();
		}
		
		private void showImage(PImage img) {
			canvas.image(img, 0, 0, E.WINDOW_WIDTH, E.WINDOW_HEIGHT);
		}

		public void x(float x) {
			this.x = x;
		}

		public void y(float y) {
			this.y = y;
		}

		public void z(float z) {
			this.z = z;
		}
	}
	
	private static class BigStar extends DrawableObject {
		private PImage starImage;
		private float w, h;
		private Name name;
		// only useful when debugging
		private static final float SCALE = (float) E.WINDOW_WIDTH / 1920;
		
		public static BigStar[] getStars() {
			int numBigStars = 13;
			BigStar[] stars = new BigStar[numBigStars];
			stars[0] = new BigStar(Name.MOON, "moon.png", 1544, 263, E.WINDOW_DEPTH);
			stars[1] = new BigStar(Name.M51, "M51.png", 993, 444,  E.WINDOW_DEPTH);
			stars[2] = new BigStar(Name.CHE2, "che2.png", 267, 546,  E.WINDOW_DEPTH);
			stars[3] = new BigStar(Name.VENUS, "venus.png", 827, 683,  E.WINDOW_DEPTH);
			stars[4] = new BigStar(Name.HIP_13454, "hip 13454.png", 715, 443,  E.WINDOW_DEPTH);			
			stars[5] = new BigStar(Name.CHE3, "che3.png", 198, 546,  E.WINDOW_DEPTH);
			stars[6] = new BigStar(Name.LOU1, "lou1.png", 1325, 354,  E.WINDOW_DEPTH);
			stars[7] = new BigStar(Name.DA5, "da5.png", 554, 291,  E.WINDOW_DEPTH);
			stars[8] = new BigStar(Name.CHUAN3, "chuan3.png", 253, 214,  E.WINDOW_DEPTH);
			stars[9] = new BigStar(Name.TIAN1, "tian1.png", 540, 81,  E.WINDOW_DEPTH);
			stars[10] = new BigStar(Name.HIP_10064, "hip 10064.png", 784, 139,  E.WINDOW_DEPTH);
			stars[11] = new BigStar(Name.HIP_10670, "hip 10670.png", 865, 195,  E.WINDOW_DEPTH);
			stars[12] = new BigStar(Name.LOU3, "lou3.png", 1155, 196,  E.WINDOW_DEPTH);			
			return stars;
		}
		
		public BigStar(Name name, String imagePath, float x, float y, float z) {
			super(SCALE * x, SCALE * y, z + 1, 0, 0); // hidden by default
			this.name = name;
			starImage = loadImage("stars/" + imagePath);
			this.w = SCALE * starImage.width;
			this.h = SCALE * starImage.height;
		}
		
		@Override
		public void draw() {
			if(alpha > 0) {
				E.pushMatrix();
				transform();
				E.tint(E.color(255, alpha));
				E.image(starImage, 0, 0, w, h);
				E.tint(255);
				E.popMatrix();
			}
		}
	}
	
	private static class Controller {
		private MainScene parent;
		private BigStarController[] starControllers;
		
		public Controller(MainScene parent) {
			this.parent = parent;
			BigStar[] stars = parent.stars;
			starControllers = new BigStarController[stars.length];
			for(int si = 0; si < stars.length; si++) {
				starControllers[si] = new BigStarController(stars[si], this);
			}
		}
		
		public void press(char key) {
			if(Character.isDigit(key) || ( 'a' <= key && key <= 'f')) {
				int starId = Integer.parseInt("" + key, 16);
				if(starId < starControllers.length) {
					starControllers[starId].clicked();
				}
			}
		}
	}
	
	private static class BigStarController {
		private Controller controller;
		private BigStar star;
		private State state = State.HIDDEN;
		
		private static final int FADE_IN_TIME = 2000;
		private static final int AUTO_FADE_OUT_TIME = 5000;
		private static final int FADE_OUT_TIME = 4000;
		
		private BigStarWaitAnimation waitBeforeFadeOut;
		private BigStarFadingAnimation fadeAnim;
		
		private static enum State {
			HIDDEN, FADING_IN, VISIBLE, FADING_OUT;
		}
		
		public BigStarController(BigStar star, Controller controller) {
			this.controller = controller;
			this.star = star;
		}
		
		public void clicked() {
			if(state == State.HIDDEN) {
				fadeIn();
			}
			else if(state == State.VISIBLE) {
				visible();
				NebulaScene scene = NebulaScene.get(star.name);
				if(scene != null)
					E.switchScene(scene);
			}
			else if(state == State.FADING_OUT) {
				// stop fadeout and do fadein
				fadeAnim.stop();
				fadeIn();
			}
		}
		
		private void fadeIn() {
			state = State.FADING_IN;										
			fadeAnim = new BigStarFadingAnimation(
					this, FADE_IN_TIME, star.alpha(), 255);
			E.addAnimation(fadeAnim);
		}
	
		private void fadeOut() {
			state = State.FADING_OUT;										
			fadeAnim = new BigStarFadingAnimation(
					this, FADE_OUT_TIME, star.alpha(), 0);
			E.addAnimation(fadeAnim);
		}
		
		private void visible() {
			state = State.VISIBLE;
			star.alpha(255);
			if(waitBeforeFadeOut == null) {	// fade out if no click within a period of time
				waitBeforeFadeOut = new BigStarWaitAnimation(this, AUTO_FADE_OUT_TIME);
				E.addAnimation(waitBeforeFadeOut);
			}
			else { // restart the time counter 
				waitBeforeFadeOut.restart();
			}
		}
		
		private void hidden() {
			state = State.HIDDEN;
			star.alpha(0);
		}
	}
	
	private static class BigStarWaitAnimation extends Animation {
		private BigStarController controller;
		
		public BigStarWaitAnimation(BigStarController controller, int wait) {
			super(-1);
			this.controller = controller;
			delay(wait);
		}

		@Override
		public void update() {
			stop();		
		}
		
		@Override
		public void end() {
			controller.waitBeforeFadeOut = null;
			controller.fadeOut();
		}
	}
	
	private static class BigStarFadingAnimation extends TransparencyAnimation {
		private BigStarController controller;
		
		public BigStarFadingAnimation(BigStarController controller, int millisDuration,
				int from_alpha, int to_alpha) {
			super(controller.star, millisDuration, from_alpha, to_alpha);
			this.controller = controller;
		}
		
		@Override
		public void end() {
			boolean is_fade_in = from < to;
			if(is_fade_in) // fade in
				controller.visible();
			else // fade out
				controller.hidden();
		}
	}
}
