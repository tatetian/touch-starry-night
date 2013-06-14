package me.tatetian.effects;

import me.tatetian.common.Transparentible;

public class TransparencyAnimation extends LinearAnimation {
	private Transparentible object;
	
	public TransparencyAnimation(Transparentible object, int millisDuration, 
															int from_alpha, int to_alpha) {
		super(millisDuration, from_alpha, to_alpha);
		this.object = object;
	}

	@Override
	public void update() {
		System.out.println(value());
		object.alpha( (int) value() );
	}
}