package com.example.controlers;

import java.io.IOException;
//import java.awt.Button;
//import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.operations.ExamOps;
import com.example.operations.PrincipalOps;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PrincipalMainPageController implements Initializable {

	static String password = "";
	static String username = "";
	@FXML // fx:id="examsBtn"
	private Button examsBtn; // Value injected by FXMLLoader

	@FXML // fx:id="questionsBtn"
	private Button questionsBtn; // Value injected by FXMLLoader

	@FXML // fx:id="usrNameBtn"
	private Text usrNameBtn; // Value injected by FXMLLoader
	@FXML // fx:id="logoutBtn"
	private Button logoutBtn; // Value injected by FXMLLoader
	@FXML // fx:id="todoBTN"
	private Button todoBTN; // Value injected by FXMLLoader
	@FXML // fx:id="studentsBTN"
	private Button studentsBTN; // Value injected by FXMLLoader
	@FXML // fx:id="examExtends"
	private Button examExtends; // Value injected by FXMLLoader

	@FXML
	void examExtendsReq(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/examRequestList.fxml"));
		Parent Main = loader.load();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Requests");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void logOut(ActionEvent event) throws IOException {
		Instance.signOut(username);
	}

	@FXML
	void showExams(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/PrincipalExamsList.fxml"));
		Parent Main = loader.load();
		PrincipalExamsListController secController = loader.getController();
		secController.init();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exams list");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void showQuestions(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/princQuestionList.fxml"));
		Parent Main = loader.load();
		viewORUpdateQuestController.backTo = "";
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Questions list");
		Window.setScene(scene);
		Window.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		usrNameBtn.setText(username);

	}

	public void init(String disc, String Password) {
		PrincipalMainPageController.username = disc;
		PrincipalMainPageController.password = Password;
		usrNameBtn.setText(disc);
	}

	@FXML
	void todoList(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/todoList.fxml"));
		Parent Main = loader.load();
		todoListController secController = loader.getController();

		secController.init(username, password, "Principal");
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("ToDo list");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void studentsList(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/principalMainStudents.fxml"));
		principalMainStudentsController.backTo = "/com/example/project/PrincipalMainPage.fxml";
		Parent Main = loader.load();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Students informations");
		Window.setScene(scene);
		Window.show();

	}
}
