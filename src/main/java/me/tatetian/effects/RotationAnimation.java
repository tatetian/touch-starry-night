package me.tatetian.effects;

import me.tatetian.common.Angleable;

public abstract class RotationAnimation extends Animation {
	protected Angleable object;
	
	public RotationAnimation(Angleable object, int duration) {
		super(duration);
		this.object = object;
	}
}
