package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.entities.Course;
import com.example.entities.Exam;
import com.example.entities.Teacher;
import com.example.operations.generalOps;
import com.example.project.dataBase;

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

public class ExamCodeGenerateController implements Initializable {

	String usrName = "";
	String password = "";
	String examCode = "";
	String course = "";
	@FXML // fx:id="codeBtn"
	private Button codeBtn; // Value injected by FXMLLoader
	@FXML // fx:id="codeIdText"
	private Text codeIdText; // Value injected by FXMLLoader
	@FXML // fx:id="backBtn"
	private Button backBtn; // Value injected by FXMLLoader
	@FXML // fx:id="onHandBTN"
	private Button onHandBTN; // Value injected by FXMLLoader
	@FXML // fx:id="extendBTN"
	private Button extendBTN; // Value injected by FXMLLoader

	@FXML // fx:id="timeExtentios"
	private TextField timeExtentios; // Value injected by FXMLLoader

	@FXML // fx:id="Explain"
	private TextArea Explain; // Value injected by FXMLLoader
	@FXML // fx:id="errorTXT"
	private Text errorTXT; // Value injected by FXMLLoader

	@FXML // fx:id="endBTN"
	private Button endBTN; // Value injected by FXMLLoader

	@FXML
	void endExam(ActionEvent event) throws IOException {
		String examNum = "" + examCode.charAt(2) + "" + examCode.charAt(3);
		Instance.sendMessage(Command.ENDEXAM.ordinal() + "@" + examNum + "@" + examCode + "@" + course);
		String respone = Instance.getClientConsole().getMessage().toString();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/showMessage.fxml"));
		Parent Main = loader.load();
		setUNvisible();
		showMessageController secController = loader.getController();
		secController.init(respone);
		Stage stage = new Stage();
		stage.setScene(new Scene(Main));
		stage.show();
	}

	@FXML
	void examExtension(ActionEvent event) throws IOException {
		if (timeExtentios.getText().isBlank()) {
			errorTXT.setText("Enter needed time");
			return;
		}
		if (Explain.getText().isBlank()) {
			errorTXT.setText("Enter Explaination");
			return;
		}
		String query = "Exam code number:" + examCode + "\nTeacher: " + usrName + "\nTime: " + timeExtentios.getText()
				+ "\nExplain:\n" + Explain.getText();
		Instance.sendMessage(Command.EXTENDEX.ordinal() + "@" + query);
		timeExtentios.setText("");
		Explain.setText("");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/showMessage.fxml"));
		Parent Main = loader.load();
		showMessageController secController = loader.getController();
		secController.init("You're request have been sent");
		Stage stage = new Stage();
		stage.setScene(new Scene(Main));
		stage.show();

	}

	@FXML
	void goBack(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/teacherExamsList.fxml"));
		Parent Main = loader.load();
		teacherExamList secController = loader.getController();

		secController.init(usrName, password);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exams list");
		Window.setScene(scene);
		Window.show();

	}

	public void setvisible() {
		timeExtentios.setVisible(true);
		Explain.setVisible(true);
		extendBTN.setVisible(true);
	}

	public void setUNvisible() {
		codeIdText.setText("");
		timeExtentios.setText("");
		Explain.setText("");
		timeExtentios.setVisible(false);
		Explain.setVisible(false);
		extendBTN.setVisible(false);
	}

	@FXML
	void generateCode(ActionEvent event) throws IOException {
		setvisible();
		codeIdText.setText(examCode);
		String examNum = "" + examCode.charAt(2) + "" + examCode.charAt(3);
		Instance.sendMessage(
				Command.setExamByExamNum.ordinal() + "@" + examNum + "@" + examCode + "@" + usrName + "@" + course);

	}

	@FXML
	void generateCodeMan(ActionEvent event) throws IOException {
		setvisible();
		codeIdText.setText(examCode);
		String examNum = "" + examCode.charAt(2) + "" + examCode.charAt(3);
		Instance.sendMessage(Command.setExamByExamNum.ordinal() + "@" + examNum + "@" + examCode + "@" + usrName
				+ "@onhand@" + course);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void init(String examCode, String usrName, String password, String course) {

		this.usrName = usrName;
		this.password = password;
		System.out.println("code : " + examCode);
		this.examCode = examCode;
		this.course = course;

	}

}
