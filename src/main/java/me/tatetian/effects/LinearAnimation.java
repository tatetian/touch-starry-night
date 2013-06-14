package me.tatetian.effects;

public abstract class LinearAnimation extends Animation {
	private float from, to;
	
	public LinearAnimation(int millisDuration, 
												 int from, int to) {
		super(millisDuration);
		this.from= from;
		this.to= to;
	}
		
	public int value() {
		return (int) (from + progress() * (to - from));
	}
}
