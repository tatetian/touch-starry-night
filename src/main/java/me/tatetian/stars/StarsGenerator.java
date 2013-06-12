package me.tatetian.stars;

import java.util.Random;

public class StarsGenerator {
	private static final Random random = new Random(1000L);
	
	public static void setSeed(long seed) {
		random.setSeed(seed);
	} 
	
	public static Star[] generate(int num, 
																float max_x, float max_y, 
																float min_z, float max_z) {
		Star[] stars = new Star[num];
		for(int star_i = 0; star_i < num; star_i++) {
			stars[star_i] = generate(max_x, max_y, min_z, max_z);
		}
		return stars;
	}
	
	private static Star generate(float max_x, float max_y, 
															 float min_z, float max_z) {		
		float x = random.nextFloat() * max_x,
					y = random.nextFloat() * max_y,
					z = min_z + random.nextFloat() * (max_z - min_z);
		return new Star(x, y, z); 
	} 
}
