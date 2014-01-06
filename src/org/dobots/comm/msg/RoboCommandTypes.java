package org.dobots.comm.msg;


public class RoboCommandTypes {

	public static final int DRIVE_COMMAND 	= 0;
	public static final int CAMERA_COMMAND	= 1;
	public static final int CONTROL_COMMAND = 2;
	
//	public static final int CAMERA_ROBOT 	= 0;
//	public static final int CAMERA_PHONE	= 1;
//	public static final int CAMERA_ROOM		= 2;
	
	/*
	 * ht_small:
	 * 		the header is only an id defining the type of the command
	 * 		{"id":..., "data":...}
	 * ht_ext:
	 * 		the header is a json object with the fields: id, tid (transaction id / message nr),
	 * 			timestamp, robot_id and version (json protocol version)
	 * 		{"header":{"id":...,"tid":...,"timestamp":..., "robot_id":..., "version":...}, "data":...}
	 */
	public enum HeaderType {
		ht_small, ht_ext
	}
	
	
	// JSON field names
	// S_ are names defining a section
	// F_ are names defining a field
	
	public static final String VERSION = "0.1";
	
	/// HEADER
	public static final String S_HEADER 	= "header";
	public static final String F_HEADER_ID	= "id";
	public static final String F_TID		= "tid";
	public static final String F_TIMESTAMP	= "timestamp";
	public static final String F_ROBOT_ID 	= "robot_id";
	public static final String F_VERSION	= "version";
	
	public static final String F_ROTATION	= "rotation";
	
	/// DATA
	public static final String S_DATA		= "data";
	
	/// drive command data fields
	public static final String F_MOVE		= "move";
	public static final String F_SPEED 		= "speed";
	public static final String F_RADIUS		= "radius";

	/// camera command data fields
	public static final String F_TYPE	 	= "type";
	
	// control command data fields
	public static final String F_COMMAND	= "command";
	public static final String F_PARAMETER	= "parameter";
	
}
