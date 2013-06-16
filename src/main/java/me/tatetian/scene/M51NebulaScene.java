package me.tatetian.scene;

import processing.core.PGraphics;
import me.tatetian.effects.Animation;
import me.tatetian.effects.Star2TextAnimationMaker;
import me.tatetian.effects.Star2TextAnimationMaker.Stars2TextAnimation;
import me.tatetian.stars.Nebula;
import me.tatetian.stars.SpriteStarsRenderer;
import me.tatetian.stars.Star;
import me.tatetian.stars.StarText;
import me.tatetian.stars.Stars;
import me.tatetian.stars.StarsGenerator;

public class M51NebulaScene extends NebulaScene {
	private Nebula nebula;
	private NebulaSceneBackground background;
	private StarText starText;
	
	private Star2TextAnimationMaker stars2TextAnimator;
	
	private int lastClickTime = -1;
	private Animation nebulaRotation;
	private boolean textShown = false;
	
	@Override
	protected void setup() {
		// init background
		background = new NebulaSceneBackground(G, "max.png", E.WIN_D / 3);
		// init nubulas
		nebula	 = new M51Nebula(G, E.WIN_W / 2, E.WIN_H / 2, E.WIN_D);
		// init text
		starText = new StarText(G, "small_stars/4.png", 
														"For my part I know nothing with any certainty,\n" +
														" but the sight of the stars makes me dream.", 120,
														E.WIN_W / 2, E.WIN_H / 2, E.WIN_D );
		starText.hide(0);
	}

	@Override
	public void show() {		
		background.show(4000);
		background.rotate(0.0004f, -1);
		nebula.reset();		
		nebula.show(4000);
		nebulaRotation = nebula.rotate(0.0004f, -1);
		starText.reset();		
		stars2TextAnimator = new Star2TextAnimationMaker(starText, nebula.stars(), nebula);
		
		lastClickTime = E.millis();
	}
	
	@Override
	protected void drawGraphics() {
		G.background(0);
		// draw nebula background
		background.draw();
		// draw stars
		nebula.draw();
		// draw text
		starText.draw();
	}

	@Override
	public void click(int clientX, int clientY) {
		if(E.millis() - lastClickTime > 4000) {
			lastClickTime = E.millis();
			if(!textShown) {
				nebulaRotation.pause();
				background.hide(2000);
				starText.delay(1000);
				starText.show(1500);
			//	starText.show(0);
				// Moving star to form the shape of text
				Stars2TextAnimation anim = stars2TextAnimator.toText(2000);
				//anim.delay(1000);
				E.addAnimation(anim);
				
				textShown = true;
			}
			else {	
				// Moving star back to their origin positions 
				Stars2TextAnimation anim = stars2TextAnimator.backStars(2000);
				E.addAnimation(anim);
	
				starText.hide(1000);
				background.delay(500);
				background.show(1500);
				nebulaRotation.resume();
				
				textShown = false;
			}
		}
	}
	
	public static class M51Nebula extends Nebula {
		public M51Nebula(PGraphics G, float x, float y, float z) {
			super(G, x, y, z);
		}

		@Override
		public Stars[] generateStars() {
			Stars[] stars = new Stars[7];
			stars[0] = new Stars(
					StarsGenerator.generate(5, E.width, E.height, 1.5f, -200, 0), 
					new SpriteStarsRenderer(G, "small_stars/1.png", 50) );
			stars[1] = new Stars(
					StarsGenerator.generate(10, E.width , E.height, 1.5f, -200, 0), 
					new SpriteStarsRenderer(G, "small_stars/2.png", 50) );
			stars[2] = new Stars(
					StarsGenerator.generate(2, E.width, E.height, 1.5f, 0, 200), 
					new SpriteStarsRenderer(G, "small_stars/3.png", 50) );
			stars[3] = new Stars(
					StarsGenerator.generate(25, E.width, E.height, 1.75f, -100, 0), 
					new SpriteStarsRenderer(G, "small_stars/4.png", 50) );
			stars[4] = new Stars(
					StarsGenerator.generate(10, E.width, E.height, 1.5f, -200, -100), 
					new SpriteStarsRenderer(G, "small_stars/5.png", 50) );
			stars[5] = new Stars(
					StarsGenerator.generate(6, E.width, E.height, 1.5f, -1200, -200), 
					new SpriteStarsRenderer(G, "small_stars/6.png", 20) );
			stars[6] = new Stars(
					StarsGenerator.generate(10, E.width, E.height, 2f, -200, 0), 
						new SpriteStarsRenderer(G, "small_stars/7.png", 50) );
			
//
//			Stars[] stars = new Stars[1];
//			stars[0] = new Stars(
//					new Star[] { new Star(0, 0, 0) },
//					new SpriteStarsRenderer(E, "small_stars/1.png", 100) );
//			
			return stars;
		}
	}
}
