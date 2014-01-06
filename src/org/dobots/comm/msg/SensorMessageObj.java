package org.dobots.comm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class SensorMessageObj extends JSONObject implements ISensorMessageData {
	
	private String mRobotID;
//	private JSONObject mValues;
	
	private SensorMessageObj(String robotID) {
		super();
		mRobotID = robotID;
	}
	
	private SensorMessageObj(String robotID, String json) throws JSONException {
		super(json);
		mRobotID = robotID;
	}
	
	public static SensorMessageObj decodeJSON(String robotID, String jsonItem) {

//		SensorMessageObj data = new SensorMessageObj(robotID);
		SensorMessageObj data;
		try {
			data = new SensorMessageObj(robotID, jsonItem);
			data.mRobotID = robotID;
		} catch (JSONException e) {
			e.printStackTrace();
			
			data = new SensorMessageObj(robotID);
		}
		
		return data;
	}
	
	public String getRobotID() {
		return mRobotID;
	}
	
	public String toJSONString() {
//		JSONObject data = new JSONObject();
//		try {
//			data.put("data", this);
			return toString();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		return null;
	}

}
