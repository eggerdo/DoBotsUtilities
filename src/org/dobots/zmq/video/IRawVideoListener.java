package org.dobots.zmq.video;


public interface IRawVideoListener {
	
	/**
	 * callback when a frame is received. in case the frame is rotated before being sent, it has to be
	 * rotated by the given rotation before being displayed 
	 * @param bmp received frame as a byte array. has to be decoded as bitmap before displaying
	 * @param rotation rotation so that it can be displayed correctly. 0 if it doesn't have to be rotated,
	 * 				   -1 if rotation parameter can be read from rgb array, see .... TODO
	 */
	public void onFrame(byte[] rgb, int rotation);
}
