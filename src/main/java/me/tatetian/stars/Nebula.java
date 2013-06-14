package me.tatetian.stars;

import me.tatetian.common.DrawableObject;

public abstract class Nebula extends DrawableObject {
	private Stars[] stars;

	public Nebula(float x, float y, float z) {
		super(x, y, z, 0, 255);
		this.stars = generateStars();
	}
	
	public abstract Stars[] generateStars();
	
	public Stars[] getStars() {
		return stars;
	}
	
	@Override
	public void draw() {
		E.pushMatrix();
		transform();
		for(Stars ss : stars) {
			ss.draw();
		}
		E.popMatrix();
	}
}
