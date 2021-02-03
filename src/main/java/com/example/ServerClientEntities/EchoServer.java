package com.example.ServerClientEntities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.server.AbstractServer;
import com.example.server.ConnectionToClient;

public class EchoServer extends AbstractServer {
	List<String> userStrings;

	public EchoServer(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		try {
			client.sendToClient(commandRunner.run((String) msg));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		// TODO Auto-generated method stub

		System.out.println("Client Disconnected.");
		super.clientDisconnected(client);
	}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		System.out.println("Client connected: " + client.getInetAddress());
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Required argument: <port>");
		} else {
			EchoServer server = new EchoServer(Integer.parseInt(args[0]));
			server.listen();
		}
	}
}
