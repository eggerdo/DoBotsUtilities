package org.dobots.comm;

public enum Move {
	NONE, // also stop
	STRAIGHT_FORWARD, FORWARD, // forward motion (w/o radius) 
	STRAIGHT_BACKWARD, BACKWARD, // backward motion (w/o radius)
	ROTATE_LEFT, ROTATE_RIGHT,  // rotation
	LEFT, RIGHT, // left / right (not rotation, e.g. parrot, or holonomic robot)
	UP, DOWN // up / down (parrot increase / lower altitude)
}
