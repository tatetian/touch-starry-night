package me.tatetian.stars;

public class Star implements Comparable<Star> {
	public float x, y, z;
	
	public Star() {
		this(0, 0, 0);
	}
	
	public Star(Star another) {
		this(another.x, another.y, another.z);
	}
	
	public Star(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(Star star) {
		x = star.x; y = star.y; z = star.z;
	}
	
	@Override
	public String toString() {
		return "<" + x + ", " + y + ", " + z  + ">";
	}
	
	public int compareTo(Star o) {
		return x < o.x && y < o.y ? -1 : 
					 x > o.x && y > o.y ? 1 : 0;
	}
}