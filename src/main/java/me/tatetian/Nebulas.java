package me.tatetian;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Nebulas {
	public static class RandomNebulas extends Nebulas {
		private Random random = new Random();
		private int min_size, max_size, window_w, window_h;
		private StarGeneratable generator;
		
		public RandomNebulas(int min_size, int max_size, 
												 int window_w, int window_h, 
												 StarGeneratable generator) {
			this.min_size = min_size;
			this.max_size = max_size;
			this.window_w = window_w;
			this.window_h = window_h;
			this.generator = generator;
			random.setSeed(1000);
		}
		
		@Override
		public Star generate() {
			int x = random.nextInt(window_w),
					y = random.nextInt(window_h),
					size = min_size + random.nextInt(max_size - min_size);
			return generator.generate(x, y, size);
		}
	}
	
	public static class CompositeNebulas extends Nebulas {
		private List<Nebulas> members;
		private List<Float> weights;
		private float[] accum_norm_weights;
		
		private Random random = new Random();
		
		public CompositeNebulas() {
			members = new ArrayList<Nebulas>();
			weights = new ArrayList<Float>();
			random.setSeed(1000);
		}
		
		public void add(Nebulas member, float weight) {
			if(weight < 0) 
				throw new IllegalArgumentException("weight can't be negative");
			
			members.add(member);
			weights.add(weight);
		}
		
		@Override
		protected void prepare() {
			// normalize the probabilities			
			int num_members = members.size();
			float weight_sum = 0;
			accum_norm_weights = new float[num_members];
			for(int wi = 0; wi < num_members; wi++) weight_sum += weights.get(wi);
			float accum_norm_weight = 0;
			for(int wi = 0; wi < num_members; wi++) {				
				accum_norm_weight += weights.get(wi) / weight_sum;
				accum_norm_weights[wi] = accum_norm_weight;	
			}
			// prepare members
			for(Nebulas member : members)
				member.prepare();
		}
		
		@Override
		protected Star generate() {
			float p = random.nextFloat();
			int member_i = 0;
			while(p >= accum_norm_weights[member_i]) member_i ++;
			return members.get(member_i).generate();
		}
	}
	
	public Star[] generate(int num) {
		Star[] stars = new Star[num];
		prepare();
		for(int star_i = 0; star_i < num; star_i++) {
			stars[star_i] = generate();
		}
		return stars;
	}
	
	protected void prepare() {
		// no-op
	}
	
	protected abstract Star generate();
}
