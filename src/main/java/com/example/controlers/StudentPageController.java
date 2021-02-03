/**
 * Sample Skeleton for 'StudentMainPage.fxml' Controller Class
 */

package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFileChooser;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.entities.checkedExam;
import com.example.project.App;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentPageController implements Initializable {

	static String password = "";
	static String username = "";
	@FXML // fx:id="gradesBtn"
	private Button gradesBtn; // Value injected by FXMLLoader

	@FXML // fx:id="enterExamBtn"
	private Button enterExamBtn; // Value injected by FXMLLoader

	@FXML // fx:id="backBtn"
	private Button backBtn; // Value injected by FXMLLoader

	@FXML // fx:id="feedbackBtn"
	private Button feedbackBtn; // Value injected by FXMLLoader

	@FXML // fx:id="usernameTXT"
	private Text usernameTXT; // Value injected by FXMLLoader

	@FXML // fx:id="quotesBtn"
	private Button quotesBtn; // Value injected by FXMLLoader
	@FXML // fx:id="todoBTN"
	private Button todoBTN; // Value injected by FXMLLoader
	@FXML // fx:id="mylabel"
	private TextField mylabel; // Value injected by FXMLLoader

	@FXML
	void enterExam(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/onlineOnHandExamEnter.fxml"));
		Parent Main = loader.load();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exam navigator");
		Window.setScene(scene);
		Window.show();

	}

	@FXML
	void goBack(ActionEvent event) throws IOException {
		Instance.signOut(username);
	}

	@FXML
	void showFeedback(ActionEvent event) {

	}

	@FXML
	void showGrades(ActionEvent event) throws IOException {
		principalStudentInfoContoller.thisPageFXML = "/com/example/project/principalStudentInfo.fxml";
		Instance.sendMessage(Command.getSTIDNum.ordinal() + "@" + username + "@" + password);
		Instance.sendMessage(
				Command.getStudentByIDNUM.ordinal() + "@" + Instance.getClientConsole().getMessage().toString());
		String[] datas = Instance.getClientConsole().getMessage().toString().split("@");
		principalStudentInfoContoller.name = datas[0];
		principalStudentInfoContoller.stId = datas[1];
		principalStudentInfoContoller.courses = new ObjectMapper().readValue(datas[3], ArrayList.class);
		principalStudentInfoContoller.exams = new ObjectMapper().readValue(datas[2],
				new TypeReference<List<checkedExam>>() {
				});
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/principalStudentInfo.fxml"));
		Parent Main = loader.load();
		principalStudentInfoContoller.backTo = "/com/example/project/StudentMainPage.fxml";
		System.out.println("dd: " + datas[2]);
		principalStudentInfoContoller secController = loader.getController();
		secController.setData();

		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Student Info");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void showQuotes(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/StudentMotivationPage.fxml"));
		Parent Main = loader.load();
		// teacherExamList secController = loader.getController();
		// secController.init(username, password);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Daily Motivation");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void todoList(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/todoList.fxml"));
		Parent Main = loader.load();
		todoListController secController = loader.getController();

		secController.init(username, password, "Student");
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("ToDo list");
		Window.setScene(scene);
		Window.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		usernameTXT.setText(username);
		// start();
	}

	public void init(String disc, String Password) {
		StudentPageController.username = disc;
		StudentPageController.password = Password;
		usernameTXT.setText(disc);
	}

}
