package me.tatetian.scene;

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
	
	@Override
	protected void setup() {
		// init background
		background = new NebulaSceneBackground("max.png", E.WINDOW_DEPTH);
		//background.show(4000);
		background.rotate(0.0004f, -1);
		// init nubulas
		nebula	 = new M51Nebula(E.WINDOW_WIDTH / 2, E.WINDOW_HEIGHT / 2, E.WINDOW_DEPTH);
		//nebula.show(4000);
		//nebula.rotate(0.0004f, -1);
		// init text
		starText = new StarText("small_stars/4.png", 
														"For my part I know nothing with any certainty,\n" +
														" but the sight of the stars makes me dream.", 100,
														E.WINDOW_WIDTH / 2, E.WINDOW_HEIGHT / 2, E.WINDOW_DEPTH );
		starText.hide(0);
		
		stars2TextAnimator = new Star2TextAnimationMaker(starText, nebula.stars());
	}

	@Override
	protected void _draw() {
		E.pushMatrix();
		E.translate(E.WINDOW_WIDTH / 2, E.WINDOW_HEIGHT / 2);
		E.popMatrix();
		// draw nebula background
		background.draw();
		// draw stars
		nebula.draw();
		// draw text
		starText.draw();
	}

	@Override
	public void click(int clientX, int clientY) {
		if(lastClickTime < 0) {
			lastClickTime = E.millis();
			
			background.hide(2000);
			starText.delay(2000);
			starText.show(3000);
		//	starText.show(0);
			// Moving star to form the shape of text
			Stars2TextAnimation anim = stars2TextAnimator.toText(2000);
			//anim.delay(1000);
			E.addAnimation(anim);
		}
		else if(E.millis() - lastClickTime > 5000) {
			lastClickTime = -1;
			
			starText.hide(4000);
			background.delay(2000);
			background.show(2000);
			// Moving star back to their origin positions 
			Stars2TextAnimation anim = stars2TextAnimator.backStars(4000);
			anim.delay(1000);
			E.addAnimation(anim);
		}
	}
	
	public static class M51Nebula extends Nebula {
		public M51Nebula(float x, float y, float z) {
			super(x, y, z);
		}

		@Override
		public Stars[] generateStars() {
			Stars[] stars = new Stars[7];
			stars[0] = new Stars(
					StarsGenerator.generate(5, E.width, E.height, 1.5f, -200, 0), 
					new SpriteStarsRenderer(E, "small_stars/1.png", 50) );
			stars[1] = new Stars(
					StarsGenerator.generate(10, E.width , E.height, 1.5f, -200, 0), 
					new SpriteStarsRenderer(E, "small_stars/2.png", 50) );
			stars[2] = new Stars(
					StarsGenerator.generate(2, E.width, E.height, 1.5f, 0, 200), 
					new SpriteStarsRenderer(E, "small_stars/3.png", 50) );
			stars[3] = new Stars(
					StarsGenerator.generate(25, E.width, E.height, 1.75f, -100, 0), 
					new SpriteStarsRenderer(E, "small_stars/4.png", 50) );
			stars[4] = new Stars(
					StarsGenerator.generate(10, E.width, E.height, 1.5f, -200, -100), 
					new SpriteStarsRenderer(E, "small_stars/5.png", 50) );
			stars[5] = new Stars(
					StarsGenerator.generate(6, E.width, E.height, 1.5f, -1200, -200), 
					new SpriteStarsRenderer(E, "small_stars/6.png", 20) );
			stars[6] = new Stars(
					StarsGenerator.generate(10, E.width, E.height, 2f, -200, 0), 
						new SpriteStarsRenderer(E, "small_stars/7.png", 50) );
			
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
