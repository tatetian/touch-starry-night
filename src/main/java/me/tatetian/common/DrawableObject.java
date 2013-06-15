package me.tatetian.common;

import me.tatetian.effects.Animation;
import me.tatetian.effects.LinearPositionAnimation;
import me.tatetian.effects.TransparencyAnimation;
import me.tatetian.effects.UniformRotationAnimation;

public abstract class DrawableObject extends Drawable implements Transparentible, Positionable, Angleable {
	protected float x, y, z;
	protected float angle;
	protected int alpha;
	private int delay_time;

	protected int MIN_ALPHA = 0, MAX_ALPHA = 255;
	
	public DrawableObject() {
		this(0, 0, 0, 0, 255);
	}
	
	public DrawableObject(float x, float y, float z, float angle, int alpha) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
		this.alpha = alpha;
		this.delay_time = 0;
	}
	
	public void delay(int millis) {
		delay_time += millis;
	}
	
	public void rotate(float speed, int millis) {
		addAnimation(new UniformRotationAnimation(this, -1, 0, 0, speed));
	}
	
	public void move(float from_z, float to_z, int millis) {
		addAnimation(new LinearPositionAnimation(this, millis, x, x, y, y, from_z, to_z));
	}
	
	public void fadeIn(int millis) {
		fade(millis, MIN_ALPHA, MAX_ALPHA);
	}
	
	public void fadeOut(int millis) {
		fade(millis, MAX_ALPHA, MIN_ALPHA);
	}

	public void fade(int millis, int from_alpha, int to_alpha) {
		if(millis > 0)
			addAnimation(new TransparencyAnimation(this, millis, from_alpha, to_alpha));
		else
			alpha = to_alpha;
	}
	
	protected void addAnimation(Animation a) {
		a.delay(delay_time);
		E.addAnimation(a);
	}
	
	public void x(float x) {
		this.x = x;
	}
	
	public void y(float y) {
		this.y = y;
	}
	
	public void z(float z) {
		this.z = z;
	}
	
	public void alpha(int alpha) {
		this.alpha = alpha;
	}
	
	public void addAngles(float x_angle_delta, float y_angle_delta, float z_angle_delta) {
		angle += z_angle_delta;
		if(angle > E.TWO_PI) angle -= E.TWO_PI;
		else if(angle < 0) angle += E.TWO_PI;
	}
	
	public void transform() {		
		E.translate(x, y, z);	
		E.rotateZ(angle);
	}
}
