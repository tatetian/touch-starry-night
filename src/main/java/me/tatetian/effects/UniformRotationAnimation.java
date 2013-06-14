package me.tatetian.effects;

import me.tatetian.common.Angleable;

public class UniformRotationAnimation extends RotationAnimation {
	private float speed_x, speed_y, speed_z;
	
	public UniformRotationAnimation(Angleable object, int millis,
															    float speed_x, float speed_y, float speed_z) {
		super(object, millis);
		this.speed_x = speed_x;
		this.speed_y = speed_y;
		this.speed_z = speed_z;
	}

	@Override
	public void update() {
		object.addAngles(speed_x, speed_y, speed_z);
	}
}
