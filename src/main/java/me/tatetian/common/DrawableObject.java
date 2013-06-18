package me.tatetian.common;

import processing.core.PGraphics;
import me.tatetian.effects.Animation;
import me.tatetian.effects.LinearPositionAnimation;
import me.tatetian.effects.TransparencyAnimation;
import me.tatetian.effects.UniformRotationAnimation;

public abstract class DrawableObject extends Drawable implements Transparentible, Positionable, Angleable {
	protected float x, y, z;
	protected float angle;
	protected int alpha;
	private int delay_time;

	private float ox, oy, oz, oangle; int oalpha;
	
	protected int MIN_ALPHA = 0, MAX_ALPHA = 255;
	
	public DrawableObject(PGraphics G) {
		this(G, 0, 0, 0, 0, 255);
	}
	
	public DrawableObject(PGraphics G, float x, float y, float z, float angle, int alpha) {
		super(G);
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
		this.alpha = alpha;
		this.delay_time = 0;
		
		save();
	}
	
	public void save() {		
		ox = x; oy = y; oz = z; oangle = angle; oalpha = alpha;
	}
	
	public void reset() {
		x = ox; y = oy; z = oz; angle = oangle; alpha = oalpha;
	}
	
	public void delay(int millis) {
		delay_time += millis;
	}
	
	public Animation rotate(float speed, int millis) {
		return addAnimation(new UniformRotationAnimation(this, -1, 0, 0, speed));
	}
	
	public Animation move(float from_z, float to_z, int millis) {
		return addAnimation(new LinearPositionAnimation(this, millis, x, x, y, y, from_z, to_z));
	}
	
	public Animation fadeIn(int millis) {
		return fade(millis, MIN_ALPHA, MAX_ALPHA);
	}
	
	public Animation fadeOut(int millis) {
		return fade(millis, MAX_ALPHA, MIN_ALPHA);
	}

	public Animation fade(int millis, int from_alpha, int to_alpha) {
		if(millis > 0)
			return addAnimation(new TransparencyAnimation(this, millis, from_alpha, to_alpha));
		else {
			alpha = to_alpha;
			return null;
		}
	}
	
	protected Animation addAnimation(Animation a) {
		a.delay(delay_time);
		E.addAnimation(a);
		return a;
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
	
	public float x() { return x; }
	public float y() { return y; }
	public float z() { return z; }
	
	public void alpha(int alpha) {
		this.alpha = alpha;
	}

	public int alpha() {
		return alpha;
	}
	
	public void addAngles(float x_angle_delta, float y_angle_delta, float z_angle_delta) {
		angle += z_angle_delta;
		if(angle > E.TWO_PI) angle -= E.TWO_PI;
		else if(angle < 0) angle += E.TWO_PI;
	}
	
	public void angle(float angle) {
		this.angle = angle;
	}
	
	public float angle() { return angle; }
	
	public void transform() {		
		G.translate(x, y, z);	
		G.rotateZ(angle);
	}
}
