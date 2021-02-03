/**
 * Sample Skeleton for 'todoList.fxml' Controller Class
 */

package com.example.controlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.entities.todoItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class todoListController {
	String userString;
	String paString;
	String role;
	@FXML // fx:id="addBTN"
	private Button addBTN; // Value injected by FXMLLoader

	@FXML // fx:id="textItem"
	private TextField textItem; // Value injected by FXMLLoader
	@FXML // fx:id="doneBTN"
	private Button doneBTN; // Value injected by FXMLLoader
	@FXML // fx:id="backBTN"
	private Button backBTN; // Value injected by FXMLLoader
	@FXML // fx:id="myTodoItems"
	private ListView<String> myTodoItems; // Value injected by FXMLLoader

	@FXML
	void addToDoItem(ActionEvent event) throws IOException {
		if (textItem.getText().isBlank())
			return;
		addItemToList(userString, paString);
	}

	public void init(String username, String password, String Role) throws IOException {
		userString = username;
		paString = password;
		this.role = Role;
		loadList(username, password);
	}

	public int getCommandToDo() {
		if (role.equals("Teacher"))
			return Command.teacherToDo.ordinal();
		if (role.equals("Principal"))
			return Command.princToDo.ordinal();
		if (role.equals("Student"))
			return Command.StuToDo.ordinal();

		return -1;
	}

	public int getCommandDellToDo() {
		if (role.equals("Teacher"))
			return Command.teachDellToDo.ordinal();
		if (role.equals("Principal"))
			return Command.princDellToDo.ordinal();
		if (role.equals("Student"))
			return Command.StuDellToDo.ordinal();

		return -1;
	}

	public int getCommandADDToDo() {
		if (role.equals("Teacher"))
			return Command.teachAddToDo.ordinal();
		if (role.equals("Principal"))
			return Command.princAddToDo.ordinal();
		if (role.equals("Student"))
			return Command.StuAddToDo.ordinal();

		return -1;
	}

	public void loadList(String username, String password) throws IOException {
		int command = getCommandToDo();
		Instance.sendMessage(command + "@" + username + "@" + password);
		String json = Instance.getClientConsole().getMessage().toString();
		List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
		myTodoItems.getItems().removeAll(myTodoItems.getItems());
		myTodoItems.getItems().addAll(l);
	}

	public void addItemToList(String username, String password) throws IOException {
		int command = getCommandADDToDo();
		Instance.sendMessage(command + "@" + username + "@" + password + "@" + textItem.getText());
		textItem.setText("");
		loadList(username, password);
	}

	@FXML
	void doneToDoItem(ActionEvent event) throws IOException {
		int command = getCommandDellToDo();
		String selected = myTodoItems.getSelectionModel().getSelectedItem();
		Instance.sendMessage(command + "@" + userString + "@" + paString + "@" + selected);
		loadList(userString, paString);
	}

	@FXML
	void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(getFxml()));
		Scene scene = new Scene(loader.load());
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Main page");
		Window.setScene(scene);
		Window.show();
	}

	public String getFxml() {
		if (role.equals("Teacher"))
			return "/com/example/project/teacherMainPage.fxml";
		if (role.equals("Principal"))
			return "/com/example/project/PrincipalMainPage.fxml";
		if (role.equals("Student"))
			return "/com/example/project/StudentMainPage.fxml";

		return "";
	}
}
