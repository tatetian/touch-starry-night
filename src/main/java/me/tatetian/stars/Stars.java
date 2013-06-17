package me.tatetian.stars;

public class Stars {
	private StarsRenderable renderer;
	private Star[] stars; 
	
	public Stars(Star[] stars, StarsRenderable renderer) {
		this.renderer = renderer;
		this.stars 	  = stars;
	}
	
	public void draw() {
		renderer.draw(stars);
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
