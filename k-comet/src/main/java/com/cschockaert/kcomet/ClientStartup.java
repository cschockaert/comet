package com.cschockaert.kcomet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.eclipse.jetty.client.HttpClient;


public class ClientStartup {

	
	public static void main(String[] args) throws Exception {
		ClientStartup client = new ClientStartup();
	        client.run();
	}
	
	String nickname = "anonymous";
	BayeuxClient client;

	private void run() throws Exception {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        String defaultURL = "http://localhost:8080/cometd";
        System.err.printf("Enter Bayeux Server URL [%s]: ", defaultURL);

        String url = input.readLine();
        if (url == null)
            return;
        if (url.trim().length() == 0)
            url = defaultURL;

        while (nickname.trim().length() == 0)
        {
            System.err.printf("Enter nickname: ");
            nickname = input.readLine();
            if (nickname == null)
                return;
        }

        HttpClient httpClient = new HttpClient();
        httpClient.start();

        WebSocketContainer wsClientContainer = ContainerProvider.getWebSocketContainer();
        httpClient.addBean(wsClientContainer, true);
        
        
        client = new BayeuxClient(url, new org.cometd.websocket.client.WebSocketTransport(null, null, wsClientContainer));
//        client.getChannel(Channel.META_HANDSHAKE).addListener(new InitializerListener());
        client.getChannel(Channel.META_CONNECT).addListener(new ConnectionListener());

        client.handshake();
        boolean success = client.waitFor(1000, BayeuxClient.State.CONNECTED);
        if (!success)
        {
            System.err.printf("Could not handshake with server at %s%n", url);
            return;
        } else {
        	System.err.println("Connected !");
        }
        
	}
	
	 private class ConnectionListener implements ClientSessionChannel.MessageListener
	    {
	        private boolean wasConnected;
	        private boolean connected;

	        public void onMessage(ClientSessionChannel channel, Message message)
	        {
	            if (client.isDisconnected())
	            {
	                connected = false;
	                System.err.println("connectionClosed()");
	                return;
	            }

	            wasConnected = connected;
	            connected = message.isSuccessful();
	            if (!wasConnected && connected)
	            {
	            	 System.err.println("connectionEstablished()");
	            }
	            else if (wasConnected && !connected)
	            {
	            	 System.err.println("connectionBroken()");
	            }
	        }
	    }
	 
}
