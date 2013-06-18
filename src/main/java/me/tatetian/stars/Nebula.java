package me.tatetian.stars;

import processing.core.PGraphics;
import me.tatetian.common.DrawableObject;
import me.tatetian.effects.Showable;

public class Nebula extends DrawableObject implements Showable {
	private Stars[] stars;
	private float normal_z;

	private float rot_x, rot_y;
	
	public Nebula(PGraphics G, Stars[] stars, float x, float y, float z) {
		super(G, x, y, z, 0, 255);
		this.normal_z = z;
		this.stars = stars;
//		this.rot_x = this.rot_y = 0;
		
		for(Stars ss : stars)
			ss.save();
	}
	
	@Override
	public void reset() {
		super.reset();
		for(Stars ss : stars)
			ss.reset();
	}
	
	public Stars[] stars() {
		return stars;
	}
	
//	public void setRotationCenter(float rot_x, float rot_y) {
//		this.rot_x = rot_x;
//		this.rot_y = rot_y;
//	}
	
	@Override
	public void draw() {
		G.pushMatrix();
		transform();
		G.stroke(G.color(alpha));
		for(Stars ss : stars) {
			ss.draw();
		}
		G.popMatrix();
	}
	
//	@Override
//	public void transform() {
//		G.translate(x + rot_x, y + rot_y, z);	
//		G.rotateZ(angle);
//		G.translate(- rot_x, - rot_y);
//	}
	
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
