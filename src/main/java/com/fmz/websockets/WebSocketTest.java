package com.fmz.websockets;

import java.io.IOException;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocketTest {
	private HashMap<String,Session> map =new HashMap<String,Session>();
	@OnMessage
    public void onMessage(String message, Session session) 
    	throws IOException, InterruptedException {
		String id =session.getId();
		if(id==null ||id.equals("")){
			id ="12345";
		}
		// Print the client message for testing purposes
		System.out.println("Received: " + message +"  From:"+id ); 
		if(!map.containsKey(session.getId())){
			map.put(session.getId(), session);
		} 
		// Send the ID to the client if it is register;
		if(message.equals("reg")){ 
			session.getBasicRemote().sendText("reg"+session.getId()); 
		}else if(message.startsWith("login")){
			String[] a =message.split("-");
			if(a.length!=2){
				return; 
			}
			Session sess=	map.get(a[1]);
			if(sess==null)
				return;
			sess.getBasicRemote().sendText("loginSuccess");
		}
	
		
    }
	@OnError
	public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
		map.remove(session.getId());
    }
	@OnOpen
    public void onOpen (  ) {
        System.out.println("Client connected"); 
    }

    @OnClose
    public void onClose () {
    	System.out.println("Connection closed");
    }
}
