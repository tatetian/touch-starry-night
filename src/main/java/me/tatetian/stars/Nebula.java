package me.tatetian.stars;

public class Nebula {
	private StarsRenderable renderer;
	private Star[] stars; 
	
	public Nebula(Star[] stars, StarsRenderable renderer) {
		this.renderer = renderer;
		this.stars 	  = stars;
	}
	
	public void draw() {
		renderer.draw(stars);
	}
	
	public void animate(Animator animator) {
		animator.update(stars);
	}
}
