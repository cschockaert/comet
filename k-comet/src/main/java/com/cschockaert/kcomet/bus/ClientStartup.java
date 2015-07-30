package com.cschockaert.kcomet.bus;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.client.ClientSessionChannel.ClientSessionChannelListener;
import org.cometd.client.BayeuxClient;
import org.eclipse.jetty.client.HttpClient;

@Listener
public class ClientStartup {

	private String serverUrl;

	public ClientStartup() {
		super();
		serverUrl = "http://localhost:8080/cometd";
		try {
			run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BayeuxClient client;

	private void run() throws Exception {
		Bus.BUS.subscribe(this);
		HttpClient httpClient = new HttpClient();
		httpClient.start();

		WebSocketContainer wsClientContainer = ContainerProvider.getWebSocketContainer();
		httpClient.addBean(wsClientContainer, true);

		client = new BayeuxClient(serverUrl, new org.cometd.websocket.client.WebSocketTransport(null, null, wsClientContainer));
		client.getChannel(Channel.META_HANDSHAKE).addListener(new InitializerListener());
		client.getChannel(Channel.META_CONNECT).addListener(new ConnectionListener());

		client.handshake();
		boolean success = client.waitFor(1000, BayeuxClient.State.CONNECTED);
		if (!success) {
			System.err.printf("Could not handshake with server at %s%n", serverUrl);
			return;
		} else {
			System.err.printf("Connected to %s%n", serverUrl);
		}

	}

	private class ConnectionListener implements ClientSessionChannel.MessageListener {
		private boolean wasConnected;
		private boolean connected;

		public void onMessage(ClientSessionChannel channel, Message message) {
			if (client.isDisconnected()) {
				connected = false;
				System.err.println("connectionClosed()");
				return;
			}

			wasConnected = connected;
			connected = message.isSuccessful();
			if (!wasConnected && connected) {
				System.err.println("connectionEstablished()");
			} else if (wasConnected && !connected) {
				System.err.println("connectionBroken()");
			}
		}
	}

	private class InitializerListener implements ClientSessionChannel.MessageListener {
		public void onMessage(ClientSessionChannel channel, Message message) {
			if (message.isSuccessful()) {
				initialize();
			}
		}
	}
	
	private void initialize() {
		client.batch(new Runnable() {
			public void run() {
				ClientSessionChannel golChannel = client.getChannel("/gol/**");
				golChannel.subscribe(new GolListener());
			}
		});
	}
	
	private class GolListener implements ClientSessionChannel.MessageListener {
		@SuppressWarnings("unchecked")
		public void onMessage(ClientSessionChannel channel, Message message) {
			System.err.println("received event " + message.getData());
			Bus.BUS.publish(new ReceivedEvent((Map<Object, Object>) message.getData(), message.getChannel()));
		}
	}
	
	
	@Handler
	public void handle(PublishEvent event) {
		client.getChannel(event.getChannel()).publish(event.getValues());
	}
	
	

}
