package me.tatetian.effects;

import java.util.Random;

import me.tatetian.stars.Star;
import me.tatetian.stars.Stars;

public class StarFlyingAnimation extends Animation {
	private float min_z, max_z, speed;
	private Random random = new Random();
	private Stars movingStars;
	
	public StarFlyingAnimation(Stars movingStars, float min_z, float max_z, float speed) {
		super(-1);
		this.movingStars = movingStars;
		
		this.min_z = min_z;
		this.max_z = max_z;
		this.speed = speed;
	}

	@Override
	public void update() {
		for(Star s : movingStars.stars()) {
			s.z += speed;
			if(s.z > max_z) s.z = min_z;
		}
	}
}
