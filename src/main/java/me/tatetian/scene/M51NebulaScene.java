package me.tatetian.scene;

import me.tatetian.Engine;
import me.tatetian.stars.Nebula;
import me.tatetian.stars.SpriteStarsRenderer;
import me.tatetian.stars.Star;
import me.tatetian.stars.StarsGenerator;
import processing.core.PImage;
import processing.opengl.PShader;

public class M51NebulaScene extends NebulaScene {
	private Nebula[] nebulas;
	private PImage background;
	private float angle;
	private float z_adjust;
	private float z_step;
	
	@Override
	protected void setup() {
		Engine E = Engine.INSTANCE;
		// init background
		background = E.loadImage(E.BASE_PATH + "max.png");
		// init nubulas
		nebulas = new Nebula[7];
		nebulas[0] = new Nebula(
				StarsGenerator.generate(5, E.width, E.height, 1.5f, -1600, -800), 
				new SpriteStarsRenderer(E.BASE_PATH + "small_stars/1.png", 100) );
		nebulas[1] = new Nebula(
				StarsGenerator.generate(10, E.width , E.height, 1.5f, -1600, -800), 
				new SpriteStarsRenderer(E.BASE_PATH + "small_stars/2.png", 100) );
		nebulas[2] = new Nebula(
				StarsGenerator.generate(2, E.width, E.height, 1.5f, -800, -600), 
				new SpriteStarsRenderer(E.BASE_PATH + "small_stars/3.png", 100) );
		nebulas[3] = new Nebula(
				StarsGenerator.generate(25, E.width, E.height, 1.75f, -1200, -800), 
				new SpriteStarsRenderer(E.BASE_PATH + "small_stars/4.png", 100) );
		nebulas[4] = new Nebula(
				StarsGenerator.generate(10, E.width, E.height, 1.5f, -1600, -1200), 
				new SpriteStarsRenderer(E.BASE_PATH + "small_stars/5.png", 100) );
		nebulas[5] = new Nebula(
				StarsGenerator.generate(6, E.width, E.height, 1.5f, -3000, -2000), 
				new SpriteStarsRenderer(E.BASE_PATH + "small_stars/6.png", 100) );
		nebulas[6] = new Nebula(
				StarsGenerator.generate(10, E.width, E.height, 2f, -2000, -800), 
				new SpriteStarsRenderer(E.BASE_PATH + "small_stars/7.png", 100) );
		
		angle 		= 0.0f;
		z_adjust  = 0.0f;
		z_step 		= 0.25f;
	}

	@Override
	protected void _draw() {
		Engine E = Engine.INSTANCE;	
		// draw nebula background
		E.pushMatrix();
		E.translate(0, 0, - E.WINDOW_DEPTH / 10);		
		E.translate(E.WINDOW_WIDTH/2, E.WINDOW_HEIGHT/2, 0);		
		E.rotateZ(angle * 0.75f);		
		E.translate(-E.WINDOW_WIDTH/2, -E.WINDOW_HEIGHT/2, z_adjust);
		E.image(background, E.WINDOW_WIDTH / 2, E.WINDOW_HEIGHT / 2);
		E.popMatrix();
		// draw stars
		E.translate(E.WINDOW_WIDTH/2, E.WINDOW_HEIGHT/2, 0);		
		E.rotateZ(angle);		
		E.translate(-E.WINDOW_WIDTH/2, -E.WINDOW_HEIGHT/2, z_adjust);
		for(Nebula nb : nebulas) {
			nb.draw();
		}
		// animate
		angle += 0.0004;
		if(angle > 2 * E.PI) angle -= 2 * E.PI;
		z_adjust += z_step;
		if(z_adjust > 600) { z_adjust = 600; z_step *= -1; }
		else if(z_adjust < 0) { z_adjust = 0; z_step *= -1; }
	}
}
