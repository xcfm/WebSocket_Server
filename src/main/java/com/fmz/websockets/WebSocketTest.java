package com.fmz.websockets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.ExtendedSSLSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocketTest {
	private static HashMap<String, Session> map = new HashMap<String, Session>();
	private Session session;

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		map.put(session.getId(), session);
		System.out.println("Client connected--------" + session.getId());
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException, InterruptedException {
		// Print the client message for testing purposes
		System.out.println("Received: " + message + "  From:" + session.getId());
		for (String key : map.keySet()) {
			System.out.println("key= " + key + " and value= " + map.get(key));
		}
		// Send the ID to the client if it is register;
		if (message.equals("reg")) {
			session.getBasicRemote().sendText("reg" + session.getId());
		} else if (message.startsWith("login")) {
			String[] a = message.split("-");
			if (a.length != 2) {
				return;
			}
			Session loginSess = map.get(a[1]);
			if (loginSess == null)
				return;
			loginSess.getBasicRemote().sendText("loginSuccess");
			session.getBasicRemote().sendText("loginSuccess");
		} else {
			session.getBasicRemote().sendText("二维码已过期");
		}

	}

	@OnError
	public void error(Session session, Throwable t) {
		if (session != null)
			onClose(session);
		System.out.println(t);
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("Connection closed");
		map.remove(session.getId());
	}
}
