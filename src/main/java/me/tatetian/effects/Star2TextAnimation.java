package me.tatetian.effects;

import me.tatetian.stars.StarText;
import me.tatetian.stars.Stars;

public class Star2TextAnimation extends Animation {
	private StarText text;
	private Stars[] movingStars;
	
	public Star2TextAnimation(StarText text, Stars[] stars, int duration) {
		super(duration);
		this.text = text;
		this.movingStars = stars;
	}

	@Override
	public void update() {
		
	}
}
