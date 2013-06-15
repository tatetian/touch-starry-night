package me.tatetian.stars;

import me.tatetian.common.DrawableObject;
import me.tatetian.effects.Showable;

public abstract class Nebula extends DrawableObject implements Showable {
	private Stars[] stars;
	private float normal_z;

	public Nebula(float x, float y, float z) {
		super(x, y, z, 0, 255);
		normal_z = z;
		this.stars = generateStars();
	}
	
	public abstract Stars[] generateStars();
	
	public Stars[] stars() {
		return stars;
	}
	
	@Override
	public void draw() {
		E.pushMatrix();
		transform();
		E.stroke(E.color(alpha));
		for(Stars ss : stars) {
			ss.draw();
		}
		E.popMatrix();
	}
	
	public void show(int millis) {
		alpha = 0;
		z = 1.2f * normal_z;
		move(z, normal_z, millis);
		fadeIn(millis);
	}

	public void hide(int millis) {
		alpha = 255;
		//z = normal_z;
		//move(normal_z, - E.WINDOW_DEPTH, millis);
		fadeOut(millis);
	}
}
