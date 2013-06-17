package me.tatetian.effects;

import me.tatetian.stars.Stars;

public abstract class StarsAnimation extends Animation {
	protected Stars[] movingStars;

	public StarsAnimation(Stars[] movingStars, int millis) {
		super(millis);
		this.movingStars = movingStars;
	}
	
	public Stars[] getStars() {
		return movingStars;
	}
}
