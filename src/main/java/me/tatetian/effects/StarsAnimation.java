package me.tatetian.effects;

import me.tatetian.stars.Stars;

public abstract class StarsAnimation extends Animation {
	private Stars[] movingStars;

	public StarsAnimation(Stars[] movingStars, int millis) {
		super(millis);
	}
	
	public Stars[] getStars() {
		return movingStars;
	}
}
