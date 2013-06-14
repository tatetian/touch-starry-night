package me.tatetian.effects;

import me.tatetian.common.Positionable;

public class LinearPositionAnimation extends PositionAnimation {
	private float from_x, to_x, from_y, to_y, from_z, to_z;
	
	public LinearPositionAnimation(Positionable object, int millis,
																 float from_x, float to_x, 
																 float from_y, float to_y,
																 float from_z, float to_z) {
		super(object, millis);
		this.from_x = from_x;
		this.to_x  	= to_x;
		this.from_y = from_y;
		this.to_y 	= to_y;
		this.from_z = from_z;
		this.to_z		= to_z;
	}
	
	@Override
	public void update() {
		float p = progress();
		object.x( from_x + p * ( to_x - from_x ));
		object.y( from_y + p * ( to_y - from_y ));
		object.z( from_z + p * ( to_z - from_z ));
	}

}
