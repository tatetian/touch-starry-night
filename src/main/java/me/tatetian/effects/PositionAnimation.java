package me.tatetian.effects;

import me.tatetian.common.Positionable;

public abstract class PositionAnimation extends Animation {
	protected Positionable object;
	
	public PositionAnimation(Positionable object, int duration) {
		super(duration);
		this.object = object;
	}
}
