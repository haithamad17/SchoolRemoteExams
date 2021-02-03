package com.example.ServerClientEntities;

import java.io.IOException;
import java.util.Random;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class Instance {
	public static SimpleChatClient clientConsole;

	public static SimpleChatClient getClientConsole() {
		return clientConsole;
	}

	public static void signOut(String username) throws IOException {
		sendMessage(Command.logOut.ordinal() + "@" + username);
		clientConsole.closeConnection();
	}

	public static void setClientConsole(SimpleChatClient clientConsole) {
		Instance.clientConsole = clientConsole;
	}

	public static String valQuestion(String disc, String sNumber) {
		if (disc.isBlank()) {
			return ("Fill question discription.");

		}
		if (sNumber.isBlank()) {
			return ("Select subject");
		}
		return "all good";
	}

	public static boolean containCH(String s) {
		for (char c : s.toCharArray()) {
			if (!(c >= '0' && c <= '9'))
				return false;
		}
		return true;
	}

	public static void sendMessage(String msg) throws IOException {
		clientConsole.setMessage(null);
		while (clientConsole.getMessage() != null) {
			System.out.println("waiting for server");
		}
		clientConsole.sendToServer(msg);
		while (clientConsole.getMessage() == null)
			System.out.println("waiting for server");
	}

	public static String getQN(int num) {
		Random rand = new Random();
		int rand_int1 = rand.nextInt(9);
		int rand_int2 = rand.nextInt(9);
		int rand_int3 = rand.nextInt(9);
		if (num == 2) {
			return "" + rand_int1 + rand_int2;
		}
		return "" + rand_int1 + rand_int2 + rand_int3;

	}

	public static void show(String string) {
		// TODO Auto-generated method stub
		System.out.println("in instance");
	}

}
