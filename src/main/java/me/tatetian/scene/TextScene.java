package me.tatetian.scene;

import me.tatetian.Engine;
import me.tatetian.stars.Stars;
import me.tatetian.stars.SpriteStarsRenderer;
import me.tatetian.stars.Star;
import me.tatetian.stars.StarText;
import me.tatetian.stars.StarsGenerator;
import me.tatetian.stars.StarsRenderable;
import geomerative.*;

public class TextScene extends Scene {
	private StarText starText;
	
	@Override
	protected void setup() {		
		starText = new StarText("small_stars/4.png", 
														"Starry Night\nis Beautiful!", 200,
														E.WINDOW_WIDTH / 2, E.WINDOW_HEIGHT / 2, -2000 );
	}

	@Override
	public void draw() {
		starText.draw();
	}

	@Override
	public void transit(Scene fromScene) {
	}
}
