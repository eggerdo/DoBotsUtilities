package org.dobots.zmq.video;

import org.dobots.zmq.ZmqHandler;
import org.dobots.zmq.comm.VideoMessage;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;


public class ZmqVideoSender implements IRawVideoListener {

	private ZMQ.Socket m_oVideoSocket;
	
	private byte[] robotID;
	
	public ZmqVideoSender(String i_strRobotID) {
		robotID = i_strRobotID.getBytes();

		m_oVideoSocket = ZmqHandler.getInstance().obtainVideoSendSocket();
	}

	@Override
	public void onFrame(byte[] rgb, int rotation) {

		VideoMessage oMsg = new VideoMessage(robotID, rgb, rotation);
		
		ZMsg zmsg = oMsg.toZmsg();
		try {
			zmsg.send(m_oVideoSocket);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (IllegalAccessError err) {
			err.printStackTrace();
		}
	}

}
