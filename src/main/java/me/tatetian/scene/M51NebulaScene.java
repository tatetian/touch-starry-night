package me.tatetian.scene;

import me.tatetian.Engine;
import me.tatetian.stars.Nebula;
import me.tatetian.stars.SpriteStarsRenderer;
import me.tatetian.stars.StarsGenerator;
import processing.core.PImage;
import processing.opengl.PShader;

public class M51NebulaScene extends NebulaScene {
	private Nebula[] nebulas;
	private PImage background;
	
	@Override
	protected void setup() {
		Engine E = Engine.INSTANCE;
		// init background
		background = E.loadImage(E.BASE_PATH + "max.png");
		// init nubulas
		nebulas = new Nebula[1];
		nebulas[0] = new Nebula(
				StarsGenerator.generate(300, E.width, E.height, 1000, 2000), 
				new SpriteStarsRenderer(E.BASE_PATH + "small_stars/1.png", 100) );
	}

	@Override
	protected void _draw() {		
		// draw background
		//e.blendMode(e.REPLACE);		
		Engine E = Engine.INSTANCE;
		E.pushMatrix();
		E.translate(0, 0, - E.WINDOW_DEPTH / 10);
		E.image(background, E.WINDOW_WIDTH / 2, E.WINDOW_HEIGHT / 2);
		E.popMatrix();
		// draw nebulas
		for(Nebula nb : nebulas) {
			nb.draw();
		}
	}
}
