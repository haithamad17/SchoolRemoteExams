package com.example.ServerClientEntities;

import com.example.client.AbstractClient;
import com.example.controlers.showMessageController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class SimpleChatClient extends AbstractClient {
	private static final Logger LOGGER = Logger.getLogger(SimpleChatClient.class.getName());
	private Object message;
	private ChatClientCLI chatClientCLI;
	static double extension = -1;
	static String course = null;

	public SimpleChatClient(String host, int port) {
		super(host, port);
		this.chatClientCLI = new ChatClientCLI(this);
	}

	@Override
	protected void connectionEstablished() {
		// TODO Auto-generated method stub
		super.connectionEstablished();
		LOGGER.info("Connected to server.");
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		setMessage(msg);
		chatClientCLI.displayMessage(msg);
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	@Override
	protected void connectionClosed() {
		// TODO Auto-generated method stub
		super.connectionClosed();
		chatClientCLI.closeConnection();
	}
}
