package me.tatetian.effects;

import java.util.Random;

import me.tatetian.stars.Star;
import me.tatetian.stars.Stars;

public class StarFlyingAnimation extends StarsAnimation {
	private float min_z, max_z, speed;
	private Random random = new Random();
	
	public StarFlyingAnimation(Stars[] movingStars, float min_z, float max_z, float speed) {
		super(movingStars, -1);
		
		this.min_z = min_z;
		this.max_z = max_z;
		this.speed = speed;
	}

	@Override
	public void update() {
		for(Stars ss : movingStars) {
			for(Star s : ss.stars()) {
				s.z += speed;
				if(s.z > max_z) s.z = min_z;
			}
		}
	}
}
