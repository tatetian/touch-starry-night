package me.tatetian.stars;

import java.util.Random;

public class StarsGenerator {
	private static final Random random = new Random(1000L);
	
	public static void setSeed(long seed) {
		random.setSeed(seed);
	} 
	
	public static Star[] generate(int num, 
																float w, float h, float xy_scale, 
																float min_z, float max_z) {
		Star[] stars = new Star[num];
		float min_x = w * ( 1 - xy_scale ) / 2,
					min_y = h * ( 1 - xy_scale ) / 2,
					max_x = xy_scale * w,
					max_y = xy_scale * h;
		for(int star_i = 0; star_i < num; star_i++) {
			stars[star_i] = generate(min_x, max_x, min_y, max_y, min_z, max_z);
		}
		return stars;
	}
	
	private static Star generate(float min_x, float max_x, 
															 float min_y, float max_y, 
															 float min_z, float max_z) {	
		float rx, ry;
		do {
			rx = random.nextFloat();
			ry = random.nextFloat();
		} while( 0.38f < rx && rx < 0.62f && 0.38f < ry && ry < 0.62f);
		
		float x = min_x + rx * (max_x - min_x),
					y = min_y + ry * (max_y - min_y),
					z = min_z + random.nextFloat() * (max_z - min_z);
		return new Star(x, y, z); 
	} 
}
