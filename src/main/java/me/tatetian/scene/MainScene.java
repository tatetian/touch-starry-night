package me.tatetian.scene;

import me.tatetian.common.Drawable;
import me.tatetian.common.DrawableObject;
import me.tatetian.common.Positionable;
import me.tatetian.effects.Animation;
import me.tatetian.effects.SceneTransition;
import me.tatetian.effects.TransparencyAnimation;
import me.tatetian.effects.ZoomFadeSceneTransition;
import processing.core.PGraphics;
import processing.core.PImage;
import me.tatetian.scene.NebulaScene.Name;

public class MainScene extends Scene {
	private Background background;
	private BigStar[] stars;
	private Controller controller;
	
	@Override
	protected void setup() {
		background = new Background(G);
		stars			 = BigStar.getStars(G);
		controller = new Controller(this);
	}
	
	@Override
	protected void drawGraphics() {
		G.background(120);
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
		// if in transition, no effect
		if(this.fromScene != null) return;
		System.out.println("main <= nebula");
		
		this.fromScene = fromScene;
		if(fromScene instanceof NebulaScene) {
			SceneTransition trans = new ZoomFadeSceneTransition(1000, fromScene, this, false);
			E.addAnimation(trans);
		}
	}
	
	private static PImage loadImage(String fileName) {
		return E.loadImage(E.BASE_PATH + "main_scene/" + fileName);
	}
	
	private static class Background extends Drawable {
		private PImage trees, hills, mountain, sky, grass;
		
		public Background(PGraphics G) {
			super(G);
			
			sky		= loadImage("sky.png");
			mountain = loadImage("mountain.png");
			trees = loadImage("trees.png");
			hills = loadImage("hills.png");
			grass = loadImage("grass.png");
		}
		
		@Override
		public void draw() {		
			G.pushMatrix();
			showImage(sky);
			showImage(mountain);
			showImage(hills);
			showImage(trees);
			showImage(grass);
			G.popMatrix();
		}
		
		private void showImage(PImage img) {
			G.imageMode(G.CORNER);
			G.image(img, 0, 0, E.WIN_W, E.WIN_H);
			G.imageMode(G.CENTER);
		}
	}
	
	private static class BigStar extends DrawableObject {
		private PImage starImage;
		private float w, h;
		private Name name;
		// only useful when debugging
		private static final float SCALE = (float) E.WIN_W / 1920;
		
		public static BigStar[] getStars(PGraphics G) {
			int numBigStars = 13;
			BigStar[] stars = new BigStar[numBigStars];
			stars[0] = new BigStar(G, Name.MOON, "moon.png", 1544, 263, 0);
			stars[1] = new BigStar(G, Name.M51, "M51.png", 993, 444,  0);
			stars[2] = new BigStar(G, Name.CHE2, "che2.png", 267, 546,  0);
			stars[3] = new BigStar(G, Name.VENUS, "venus.png", 827, 683,  0);
			stars[4] = new BigStar(G, Name.HIP_13454, "hip 13454.png", 715, 443,  0);			
			stars[5] = new BigStar(G, Name.CHE3, "che3.png", 198, 546,  0);
			stars[6] = new BigStar(G, Name.LOU1, "lou1.png", 1325, 354,  0);
			stars[7] = new BigStar(G, Name.DA5, "da5.png", 554, 291,  0);
			stars[8] = new BigStar(G, Name.CHUAN3, "chuan3.png", 253, 214,  0);
			stars[9] = new BigStar(G, Name.TIAN1, "tian1.png", 540, 81,  0);
			stars[10] = new BigStar(G, Name.HIP_10064, "hip 10064.png", 784, 139,  0);
			stars[11] = new BigStar(G, Name.HIP_10670, "hip 10670.png", 865, 195,  0);
			stars[12] = new BigStar(G, Name.LOU3, "lou3.png", 1155, 196,  0);			
			return stars;
		}
		
		public BigStar(PGraphics G, Name name, String imagePath, float x, float y, float z) {
			super(G, SCALE * x, SCALE * y, z, 0, 0); // hidden by default
			this.name = name;
			starImage = loadImage("stars/" + imagePath);
			this.w = SCALE * starImage.width;
			this.h = SCALE * starImage.height;
		}
		
		@Override
		public void draw() {
			if(alpha > 0) {
				G.pushMatrix();
				transform();
				G.tint(E.color(255, alpha));
				G.image(starImage, 0, 0, w, h);
				G.tint(255);
				G.popMatrix();
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
