package com.sri.straylight.socketserver.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;
import org.bushe.swing.event.EventBus;
import org.eclipse.jetty.util.TypeUtil;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketConnectionRFC6455;

import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.socketserver.event.WebSocketConnectionStateEvent;
import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class StrayLightWebSocketHandler
	implements 	WebSocket.OnTextMessage, 
				WebSocket.OnControl,
				Iserializable {

	private static int socketCount_ = 0;
	
	boolean _verbose = true;
	
	@SuppressWarnings("unused")
	private int socketID_ = 0;
	
	private String sessionID_;
	private Connection connection_;
	private WebSocketConnectionState state_ = WebSocketConnectionState.uninitialized;
	
	private int idx_ = -1;
	private WebSocketConnectionController parentController_;
	private String messageQueItem_;
	private HttpSession httpSession_;
	
	private boolean serializeType_ = true;
	
    protected FrameConnection _connection;
    
    final static byte OP_CLOSE = 0x08;
    
    
	public StrayLightWebSocketHandler(HttpSession httpSession) {

		socketID_ = socketCount_;
		socketCount_++;
		httpSession_ = httpSession;
		sessionID_ = httpSession_.getId();
	}
	
	
	public void setIdx(int idx) {
		idx_ = idx;
	}
	
	
	public String getSessionID() {
		return httpSession_.getId();
	}
	
	public int getIdx() {
		return idx_;
	}
	
	public WebSocketConnectionState getState() {
		return state_;
	}
	

	
    /**
     * Called when a new websocket connection is accepted.
     * @param connection The Connection object to use to send messages.
     */
	public void onOpen(Connection connection) {
		
		// A client has opened a connection with us
		// Store the opened connection
		this.connection_ = connection;
		setState(WebSocketConnectionState.opened_new);
		
    	System.out.println("StrayLightWebSocketHandler.onOpen() sessionID: " + sessionID_);
    	
        if (_verbose)
            System.err.printf("%s#onOpen %s %s\n",this.getClass().getSimpleName(),connection,connection.getProtocol());
        
		return;
	}
	
//	@Override
//	public void onHandshake(FrameConnection connection) {
//		// TODO Auto-generated method stub
//		
//        if (_verbose)
//            System.err.printf("%s#onHandshake %s %s\n",this.getClass().getSimpleName(),connection,connection.getClass().getSimpleName());
//		
//        _connection = connection;
//	
//	}
	
	
    /**
     * Called when any websocket frame is received.
     * @param flags
     * @param opcode
     * @param data
     * @param offset
     * @param length
     * @return true if this call has completely handled the frame and no further processing is needed (including aggregation and/or message delivery)
     */
//    public boolean onFrame(byte flags, byte opcode, byte[] data, int offset, int length)
//    {            
//        if (_verbose)
//            System.err.printf("%s#onFrame %s|%s %s\n",this.getClass().getSimpleName(),TypeUtil.toHexString(flags),TypeUtil.toHexString(opcode),TypeUtil.toHexString(data,offset,length));
//        return false;
//    }
    

	//handle incoming message
	public void onMessage(String data) {
		
		if (null == parentController_) {
			messageQueItem_ = data;
		} else {
			parentController_.onMessageRecieved(data);
		}
		
        if (_verbose)
            System.err.printf("%s#onMessage     %s\n",this.getClass().getSimpleName(),data);
        
	}
	
    public void onMessage(byte[] data, int offset, int length)
    {
        if (_verbose)
            System.err.printf("%s#onMessage     %s\n",this.getClass().getSimpleName(),TypeUtil.toHexString(data,offset,length));
    }
	
	
	
	
    /** 
     * Called when a control message has been received.
     * @param controlCode
     * @param data
     * @param offset
     * @param length
     * @return true if this call has completely handled the control message and no further processing is needed.
     */
	public boolean onControl(byte controlCode,byte[] data, int offset, int length) {
		
		System.out.println("StrayLightWebSocketHandler.onControl() controlCode: " + controlCode);
		
		
		if ( OP_CLOSE == controlCode) {
			setState(WebSocketConnectionState.closeRequested);	
		} else {
			System.err.println("StrayLightWebSocketHandler.onControl() controlCode: " + controlCode);
		}
		
        if (_verbose)
            System.err.printf("%s#onControl  %s %s\n",this.getClass().getSimpleName(),TypeUtil.toHexString(controlCode),TypeUtil.toHexString(data,offset,length));
        
		return false;
	}
	
	
	//handle connection closc 
	
	public void onClose(int code, String message) {
		

		setState(WebSocketConnectionState.closed);
    	System.out.println("StrayLightWebSocketHandler.onClose() sessionID: " + sessionID_);
    	
        if (_verbose)
            System.err.printf("%s#onDisonnect %d %s\n",this.getClass().getSimpleName(),code,message);
	}
	
	
	private void setState(WebSocketConnectionState state) {
		state_ = state;
		WebSocketConnectionStateEvent e = new WebSocketConnectionStateEvent(this, state);
		EventBus.publish(e);
	}
	
	
	
	public void serializeAndSend(Iserializable obj) {
		
		
		if (obj instanceof XMLparsedEvent) {
			
			XMLparsedEvent event = (XMLparsedEvent) obj;
			
			XMLparsedInfo  xmlParsedInfo = event.getPayload();
			xmlParsedInfo.setSessionID(sessionID_);
		}
		  
		String json = obj.serialize();
		sendString_ (json);
	}
	
	
	public void sendString_(String str) {
		
		LogFactory.getLog(this.getClass()).debug("sendString_ " + str);
		
		
		System.out.println("StrayLightWebSocketHandler.sendString_() sessionID_: " + sessionID_);
		
		
		try {
			if (this.connection_.isOpen()) {
				this.connection_.sendMessage(str);
			}

		} catch (IOException e) {
			e.printStackTrace();
			//	this.connection_.disconnect();
		}
	}


	public void setParentController(WebSocketConnectionController webSocketConnectionController) {
		parentController_ = webSocketConnectionController;
	}


	public void processQuedItem() {
		if (null != messageQueItem_) {
			parentController_.onMessageRecieved(messageQueItem_);
		}
	}


    public String serialize() {
    	return JsonController.getInstance().serialize(this);
    }


	public boolean getSerializeType() {
		return serializeType_;
	}
	
	public void setSerializeType(boolean serializeType) {
		serializeType_ = serializeType;
	}

//
//	@Override
//	public String getProtocol() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	@Override
//	public void sendMessage(String data) throws IOException {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void sendMessage(byte[] data, int offset, int length)
//			throws IOException {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void disconnect() {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void close() {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void close(int closeCode, String message) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public boolean isOpen() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//
//	@Override
//	public void setMaxIdleTime(int ms) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void setMaxTextMessageSize(int size) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void setMaxBinaryMessageSize(int size) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public int getMaxIdleTime() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//
//	@Override
//	public int getMaxTextMessageSize() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//
//	@Override
//	public int getMaxBinaryMessageSize() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//


	
	
}