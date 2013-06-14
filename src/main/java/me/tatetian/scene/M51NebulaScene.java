package me.tatetian.scene;

import me.tatetian.stars.Nebula;
import me.tatetian.stars.SpriteStarsRenderer;
import me.tatetian.stars.StarText;
import me.tatetian.stars.Stars;
import me.tatetian.stars.StarsGenerator;

public class M51NebulaScene extends NebulaScene {
	private Nebula nebula;
	private Background background;
	private float z_adjust;
	private float z_step;
	private StarText starText;
	
	@Override
	protected void setup() {
		// init background
		background = new Background("max.png", - E.WINDOW_DEPTH / 10);
		background.show(4000);
		background.rotate(0.0004f, -1);
		// init nubulas
		nebula	 = new M51Nebula(E.WINDOW_WIDTH / 2, E.WINDOW_HEIGHT / 2, - E.WINDOW_DEPTH / 10);
		nebula.rotate(0.0004f, -1);
		// init text
		starText = new StarText("small_stars/4.png", 
														"Starry Night\nis Beautiful!", 200,
														E.WINDOW_WIDTH / 2, E.WINDOW_HEIGHT / 2, -2000 );
		z_adjust  = 0.0f;
		z_step 		= 0.25f;
	}

	@Override
	protected void _draw() {
		// draw nebula background
		background.draw();
		// draw stars
		nebula.draw();
		// draw text
		starText.draw();
		// animate
		z_adjust += z_step;
		if(z_adjust > 600) { z_adjust = 600; z_step *= -1; }
		else if(z_adjust < 0) { z_adjust = 0; z_step *= -1; }
	}

	@Override
	public void click(int clientX, int clientY) {
		starText.show(4000);
	}
	
	public static class M51Nebula extends Nebula {
		public M51Nebula(float x, float y, float z) {
			super(x, y, z);
		}

		@Override
		public Stars[] generateStars() {
			Stars[] stars = new Stars[7];
			stars[0] = new Stars(
					StarsGenerator.generate(5, E.width, E.height, 1.5f, -800, 0), 
					new SpriteStarsRenderer(E, "small_stars/1.png", 100) );
			stars[1] = new Stars(
					StarsGenerator.generate(10, E.width , E.height, 1.5f, -800, 0), 
					new SpriteStarsRenderer(E, "small_stars/2.png", 100) );
			stars[2] = new Stars(
					StarsGenerator.generate(2, E.width, E.height, 1.5f, 0, 200), 
					new SpriteStarsRenderer(E, "small_stars/3.png", 100) );
			stars[3] = new Stars(
					StarsGenerator.generate(25, E.width, E.height, 1.75f, -400, 0), 
					new SpriteStarsRenderer(E, "small_stars/4.png", 100) );
			stars[4] = new Stars(
					StarsGenerator.generate(10, E.width, E.height, 1.5f, -800, -400), 
					new SpriteStarsRenderer(E, "small_stars/5.png", 100) );
			stars[5] = new Stars(
					StarsGenerator.generate(6, E.width, E.height, 1.5f, -2200, -1200), 
					new SpriteStarsRenderer(E, "small_stars/6.png", 100) );
			stars[6] = new Stars(
					StarsGenerator.generate(10, E.width, E.height, 2f, -1200, 0), 
						new SpriteStarsRenderer(E, "small_stars/7.png", 100) );
			return stars;
		}
	}
}
