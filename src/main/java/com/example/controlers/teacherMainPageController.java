package com.example.controlers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.ServerClientEntities.Instance;

import javafx.fxml.Initializable;

public class teacherMainPageController implements Initializable {
	static String password = "";
	static String username = "";
	@FXML // fx:id="usernameTXT"
	private Text usernameTXT; // Value injected by FXMLLoader

	@FXML // fx:id="myExamsBTN"
	private Button myExamsBTN; // Value injected by FXMLLoader
	@FXML // fx:id="showQues"
	private Button showQues; // Value injected by FXMLLoader
	@FXML // fx:id="todoBtn"
	private Button todoBtn; // Value injected by FXMLLoader
	@FXML // fx:id="signoutBTN"
	private Button signoutBTN; // Value injected by FXMLLoader

	@FXML
	void showExamsList(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/teacherExamsList.fxml"));
		Parent Main = loader.load();
		teacherExamList secController = loader.getController();

		secController.init(username, password);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exams list");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void showQuestions(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/questionList.fxml"));
		Parent Main = loader.load();
		questionListController secController = loader.getController();

		secController.init(username, password);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Questions list");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void signOut(ActionEvent event) throws IOException {
		Instance.signOut(username);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		usernameTXT.setText(username);
	}

	public void init(String disc, String password) {
		teacherMainPageController.username = disc;
		teacherMainPageController.password = password;
		usernameTXT.setText(disc);
	}

	@FXML
	void showToDo(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/todoList.fxml"));
		Parent Main = loader.load();
		todoListController secController = loader.getController();

		secController.init(username, password, "Teacher");
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("ToDo list");
		Window.setScene(scene);
		Window.show();
	}

}
