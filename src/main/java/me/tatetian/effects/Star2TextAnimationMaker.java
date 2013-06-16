package me.tatetian.effects;

import java.util.Random;

import me.tatetian.stars.Star;
import me.tatetian.stars.StarText;
import me.tatetian.stars.Stars;

public class Star2TextAnimationMaker {
	private StarText text;
	private Star[] movingStars;
	private Star[] originStars;
	
	public Star2TextAnimationMaker(StarText text, Stars[] stars) {
		this.text = text;

		int numMovingStars = 0;
		for(Stars ss : stars) {
			numMovingStars += ss.stars().length;
		}
		movingStars = new Star[numMovingStars];
		numMovingStars = 0;
		for(Stars ss: stars) {
			Star[] s = ss.stars();
			System.arraycopy(s, 0, movingStars, numMovingStars, s.length);
			numMovingStars += s.length;
		}
		// clone origins
		originStars = new Star[movingStars.length];
		for(int si = 0; si < originStars.length; si++ )
			originStars[si] = new Star(movingStars[si]);
	}

	public Stars2TextAnimation toText(int millis) {
		return new ToTextAnimation(this, millis);
	}
	
	public Stars2TextAnimation backStars(int millis) {
		return new BackStarsAnimation(this, millis);
	} 
	
	public static abstract class Stars2TextAnimation extends Animation {
		protected Star2TextAnimationMaker parent;	
		public Stars2TextAnimation(Star2TextAnimationMaker parent, int millis) {
			super(millis);
			this.parent = parent;
		}
	}
	
	private static class ToTextAnimation extends Stars2TextAnimation {
		private Star[] targetStars;
		private Random random = new Random();
		
		public ToTextAnimation(Star2TextAnimationMaker parent, int millis) {
			super(parent, millis);
			Star[] textStars = parent.text.stars();
			Star[] movingStars = parent.movingStars;
			targetStars = new Star[movingStars.length];
			for(int ms_i = 0; ms_i < movingStars.length; ms_i ++) {
				int ts_j = random.nextInt(textStars.length);
				targetStars[ms_i] = textStars[ts_j];
			}
		}

		@Override
		public void update() {
			Star[] movingStars = parent.movingStars;
			Star[] originStars = parent.originStars;
			Star os, ms, ts;
			float p = progress(), q = 1 - p;
			for(int s_i = 0; s_i < movingStars.length; s_i ++) {
				os = originStars[s_i];
				ms = movingStars[s_i];
				ts = targetStars[s_i];
				ms.x = q * os.x + p * ts.x ;
				ms.y = q * os.y + p * ts.y ;
				ms.z = q * os.z + p * ts.z ;
			}
		}
	}

	private static class BackStarsAnimation extends Stars2TextAnimation {
		public BackStarsAnimation(Star2TextAnimationMaker parent, int millis) {
			super(parent, millis);
		}

		@Override
		public void update() {			
		}
	}
}
