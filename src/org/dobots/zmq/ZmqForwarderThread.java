package org.dobots.zmq;

import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class ZmqForwarderThread extends ZmqReceiveThread {
	
	protected ZMQ.Socket m_oOutSocket;

	public ZmqForwarderThread(ZMQ.Context i_oContext, ZMQ.Socket i_oInSocket, ZMQ.Socket i_oOutSocket, String i_strThreadName) {
		super(i_oContext, i_oInSocket, i_strThreadName);
		
		m_oOutSocket = i_oOutSocket;
	}

	@Override
	protected void execute() {
		ZMsg msg = ZMsg.recvMsg(m_oInSocket);
		if (msg != null) {
			msg.send(m_oOutSocket);
		}
	}
	
}