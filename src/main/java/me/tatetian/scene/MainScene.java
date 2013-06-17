package me.tatetian.scene;

import java.util.LinkedList;

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
	private Foreground foreground;
	private Sky sky;
	private Controller controller;
	
	private LinkedList<Animation> animMemory;
	
	@Override
	protected void setup() {
		foreground = new Foreground(G);
		sky 			 = new Sky(G);
		
		controller = new Controller(this);
		animMemory = new LinkedList<Animation>();
		// TODO: init animations
		// ...
	}

	@Override
	public void show() {
		for(Animation a : animMemory)
			a.resume();
		E.getAnimations().addAll(this.animMemory);
	}
	
	@Override
	public void hide() {
		// save animations for next time when this scene is shown
		animMemory.clear();
		animMemory.addAll(E.getAnimations());
		for(Animation a : animMemory)
			a.pause();
	}
	
	@Override
	protected void beforeDraw() {
//		G.blendMode(G.BLEND);
		super.beforeDraw();
		sky.prepare();
	}
	
	@Override
	protected void drawGraphics() {
//		G.blendMode(G.BLEND);
//		G.hint(G.ENABLE_DEPTH_TEST);
		G.background(120);
		sky.draw();
		foreground.draw();		
//		G.blendMode(G.ADD);
//		G.hint(G.DISABLE_DEPTH_TEST);
//		for(BigStar star : stars) 
//			star.draw();
	}
	
	@Override
	public void press(char key) {
		controller.press(key);
	}

	@Override
	public void transit(Scene fromScene) {
		if( fromScene == null // if no fromScene, no effect
		 || this.fromScene != null) // if in transition, no effect
			return;
		
		this.fromScene = fromScene;
		if(fromScene instanceof NebulaScene) {
			SceneTransition trans = new ZoomFadeSceneTransition(1000, fromScene, this, false);
			E.addAnimation(trans);
		}
	}
	
	private static PImage loadImage(String fileName) {
		return E.loadImage(E.BASE_PATH + "main_scene/" + fileName);
	}
	
	private static class Sky extends Drawable {
		private PImage skyImg;
		private BigStar[] stars;
		private PGraphics sky;
		
		public Sky(PGraphics G) {
			super(G);
				
			this.skyImg	= loadImage("sky.png");
			this.sky		= E.createGraphics(E.WIN_W, E.WIN_H, E.P2D);
			this.stars  = BigStar.getStars(sky);
		}

		/*
		 * this is a workaround for limitation on concurrent PGraphics
		 * must be called before draw()
		 * */
		public void prepare() {
			sky.beginDraw();
			sky.blendMode(sky.BLEND);
			sky.smooth(4);
			sky.background(0);
			sky.imageMode(sky.CORNER);
			sky.image(skyImg, 0, 0, sky.width, sky.height);
			sky.imageMode(sky.CENTER);
			for(BigStar bs : stars) {
				bs.draw();
			}
			sky.endDraw();
		}
		
		@Override
		public void draw() {
			G.imageMode(G.CORNER);
			G.image(sky, 0, 0, E.WIN_W, E.WIN_H);
		}
		
		public BigStar[] stars() { return stars; }
	}
	
	private static class Foreground extends Drawable {
		private PImage trees, hills, mountain, grass;
		
		public Foreground(PGraphics G) {
			super(G);
			
			mountain = loadImage("mountain.png");
			trees = loadImage("trees.png");
			hills = loadImage("hills.png");
			grass = loadImage("grass.png");
		}
		
		@Override
		public void draw() {		
			G.pushMatrix();
			showImage(mountain);
			showImage(hills);
			showImage(trees);
			showImage(grass);
			G.popMatrix();
		}
		
		private void showImage(PImage img) {
			G.imageMode(G.CORNER);
			G.image(img, 0, 0, E.WIN_W, E.WIN_H);
		}
	}
	
	private static class BigStar extends DrawableObject {
		private PImage starImage;
		private int w, h;
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
			this.w = (int) (SCALE * starImage.width);
			this.h = (int) (SCALE * starImage.height);
			starImage.resize(w, h);
		}
		
		@Override
		public void draw() {
			if(alpha > 0) {
				G.pushMatrix();
				G.tint(E.color(255, alpha));
				// adjust alpha of image
//				starImage.loadPixels();
//				int[] pixels = starImage.pixels;
//				int w = starImage.width, h = starImage.height;
//				int i;
//				for(int px = 0; px < w; px++)
//					for(int py = 0; py < h; py++) {
//						i = px + py * w;
//						pixels[i] = G.color(pixels[i], alpha);
//					}
//				starImage.updatePixels();
				//G.imageMode(G.CENTER);
//				G.blendMode(G.BLEND);
				G.image(starImage, x, y, w, h);
//				G.tint(5);
//				G.blend(starImage, 0, 0, w, h, (int)(x-w/2), (int)(y-h/2), w, h, G.BLEND);
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
			BigStar[] stars = parent.sky.stars();
			starControllers = new BigStarController[stars.length];
			for(int si = 0; si < stars.length; si++) {
				starControllers[si] = new BigStarController(stars[si], this);
			}
		}
		
		public void press(char key) {
			// click on stars
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
