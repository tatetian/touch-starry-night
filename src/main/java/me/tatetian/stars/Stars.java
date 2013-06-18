package me.tatetian.stars;

public class Stars {
	private StarsRenderable renderer;
	private Star[] stars; 
	private Star[] savedStars;
	
	public Stars(Star[] stars, StarsRenderable renderer) {
		this.renderer = renderer;
		this.stars 	  = stars;
		this.savedStars = null;
	}
	
	public void save() {
		savedStars = new Star[stars.length];
		for(int si = 0; si < stars.length; si++)
			savedStars[si] = new Star(stars[si]);
	}
	
	public void reset() {
		if(savedStars != null && savedStars.length == stars.length) {
			for(int si = 0; si < stars.length; si++)
				stars[si].set(savedStars[si]);
		}
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
