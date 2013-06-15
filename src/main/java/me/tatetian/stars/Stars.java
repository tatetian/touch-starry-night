package me.tatetian.stars;

import me.tatetian.common.Drawable;

public class Stars extends Drawable {
	private StarsRenderable renderer;
	private Star[] stars; 
	
	public Stars(Star[] stars, StarsRenderable renderer) {
		this.renderer = renderer;
		this.stars 	  = stars;
	}
	
	public void draw() {
		renderer.draw(stars);
	}

	public void animate(Animator animator) {
		animator.update(stars);
	}
	
	public Star[] stars() {
		return stars;
	}
	
	@Override
	public Stars clone() {
		Star[] clonedStars = stars.clone();
		return new Stars(clonedStars, renderer);
	}
}
