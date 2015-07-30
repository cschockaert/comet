package com.cschockaert.kcomet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.eclipse.jetty.client.HttpClient;

import com.cschockaert.kcomet.bus.Bus;
import com.cschockaert.kcomet.bus.TextInputEvent;
import com.cschockaert.kcomet.chatui.ChatWindow;

@Listener
public class ChatConsoleStartup {

	public static void main(String[] args) throws Exception {
		ChatConsoleStartup client = new ChatConsoleStartup();
		client.run();
	}

	String nickname = "anonymous";
	BayeuxClient client;

	private final ChatListener chatListener = new ChatListener();
	private final MembersListener membersListener = new MembersListener();
	ChatWindow chat = new ChatWindow();

	private void run() throws Exception {
		Bus.BUS.subscribe(this);
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		String defaultURL = "http://localhost:8080/cometd";
		System.err.printf("Enter Bayeux Server URL [%s]: ", defaultURL);

		String url = input.readLine();
		if (url == null)
			return;
		if (url.trim().length() == 0)
			url = defaultURL;

		System.err.printf("Enter nickname: ");
		nickname = input.readLine();
		if (nickname == null)
			return;

		HttpClient httpClient = new HttpClient();
		httpClient.start();

		WebSocketContainer wsClientContainer = ContainerProvider.getWebSocketContainer();
		httpClient.addBean(wsClientContainer, true);

		client = new BayeuxClient(url, new org.cometd.websocket.client.WebSocketTransport(null, null, wsClientContainer));
		client.getChannel(Channel.META_HANDSHAKE).addListener(new InitializerListener());
		client.getChannel(Channel.META_CONNECT).addListener(new ConnectionListener());

		client.handshake();
		boolean success = client.waitFor(1000, BayeuxClient.State.CONNECTED);
		if (!success) {
			System.err.printf("Could not handshake with server at %s%n", url);
			return;
		} else {
			System.err.println("Connected !");
		}

		while (true) {

			String text = input.readLine();
			if (text != null && text.length() > 0) {
				Map<String, Object> data = new HashMap<>();
				data.put("user", nickname);
				data.put("chat", text);
				client.getChannel("/chat/demo").publish(data);
			}
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
				ClientSessionChannel chatChannel = client.getChannel("/chat/demo");
				chatChannel.subscribe(chatListener);

				ClientSessionChannel membersChannel = client.getChannel("/members/demo");
				membersChannel.subscribe(membersListener);

				Map<String, Object> data = new HashMap<>();
				data.put("user", nickname);
				data.put("membership", "join");
				data.put("chat", nickname + " hello");
				chatChannel.publish(data);
			}
		});
	}

	private class ChatListener implements ClientSessionChannel.MessageListener {
		public void onMessage(ClientSessionChannel channel, Message message) {
			Map<String, Object> data = message.getDataAsMap();
			String fromUser = (String) data.get("user");
			String text = (String) data.get("chat");
			System.err.printf(">> %s: %s%n", fromUser, text);
			chat.addExternalMessage("Received message from " + fromUser + ":" + text);
		}
	}

	private class MembersListener implements ClientSessionChannel.MessageListener {
		public void onMessage(ClientSessionChannel channel, Message message) {
			Object data = message.getData();
			Object[] members = data instanceof List ? ((List) data).toArray() : (Object[]) data;
			System.err.printf("Members: %s%n", Arrays.asList(members));
		}
	}

	@Handler
	public void handle(TextInputEvent event) {
		String text = event.getData();
		if (text != null && text.length() > 0) {
			Map<String, Object> data = new HashMap<>();
			data.put("user", nickname);
			data.put("chat", text);
			client.getChannel("/chat/demo").publish(data);
		}
	}
}
