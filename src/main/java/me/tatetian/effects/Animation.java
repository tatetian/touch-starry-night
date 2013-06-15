package me.tatetian.effects;

import me.tatetian.Engine;
import me.tatetian.common.Drawable;

public abstract class Animation {
	protected static Engine E;
	public static void setEngine(Engine e) { E = e; }
	
	private int startTime = -1;
	private int duration = -1;
	private float progress = 0;
	private int delayTime = 0;
	private int realStartTime = -1;
	private boolean running = false;
	
	/**
	 * @param duration is in millisecond
	 * */
	public Animation(int duration) {
		this.duration  = duration;
	}
	
	public void start() {		
		running = true;
		if(startTime < 0) {
			startTime = E.millis();
			realStartTime = startTime + delayTime;
		}
	}

	public void pause() {
		running = false;
	}
	
	public void delay(int millis) {
		this.delayTime = millis;
	}
	
	public void step() {
		if(running) {
			int now = E.millis();
			if(now > realStartTime) {
				// calculate progress if time is limited
				if( duration >= 0 ) {
					float ellapsedTime = now - realStartTime;
					progress = ellapsedTime < duration ? ellapsedTime / duration : 1.0f;
				}
				// update state
				update();
			}
		} 
	}
	
	public float progress() {
		return progress;
	}
	
	public boolean finished() {
		return progress == 1;
	}
	
	public boolean forever() {
		return duration < 0;
	}
		
	public abstract void update(); 
}
