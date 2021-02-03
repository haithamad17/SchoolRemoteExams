package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentStartExamPageController implements Initializable {

	private String usrName = "";
	private String password = "";
	@FXML // fx:id="idNum"
	private TextField idNum; // Value injected by FXMLLoader

	@FXML // fx:id="examCode"
	private TextField examCode; // Value injected by FXMLLoader

	@FXML // fx:id="enterExamBtn"
	private Button enterExamBtn; // Value injected by FXMLLoader

	@FXML // fx:id="backBtn"
	private Button backBtn; // Value injected by FXMLLoader
	@FXML // fx:id="errorTxt"
	private Text errorTxt; // Value injected by FXMLLoader
	static String examC = "";
	static String teacherID = "";

	@FXML
	void enterExam(ActionEvent event) throws IOException, SQLException {

		if (examCode.getText().toString().length() != 4) {
			errorTxt.setText("Exam code should be 4 digits!");
			return;
		}
		if (!Instance.containCH(examCode.getText())) {
			errorTxt.setText("Exam code should only contain digits");
			return;
		}
		Instance.sendMessage(Command.isStudentExistById.ordinal() + "@" + idNum.getText() + "@" + usrName + "@"
				+ password + "@" + examCode.getText());
		if (Instance.getClientConsole().getMessage().toString().equals("doneIt")) {
			errorTxt.setText("You've already submited you're exam!");
			return;
		}
		if (!(Instance.getClientConsole().getMessage().toString().equals("exist"))) {

			errorTxt.setText("Invalid ID or exam code");
			return;
		}

		else {
			Instance.sendMessage(Command.getExamCourseByCode.ordinal() + "@" + examCode.getText());
			String course = Instance.getClientConsole().getMessage().toString();
			if (course.isEmpty()) {
				errorTxt.setText("Invalid ID or exam code");
				return;
			}
			Instance.sendMessage(Command.isStudentExistInCourse.ordinal() + "@" + idNum.getText() + "@" + course);
			if (!Instance.getClientConsole().getMessage().toString().equals("exist")) {

				errorTxt.setText("You don't have access to this exam!");
				return;
			}
		}
		studentExamPageController.examCode = examCode.getText();
		StudentStartExamPageController.examC = examCode.getText();
		Instance.sendMessage(Command.getTechIdByExCode.ordinal() + "@" + examCode.getText());
		StudentStartExamPageController.teacherID = Instance.getClientConsole().getMessage().toString();
		studentExamQuestionsController.teacherID = teacherID;
		studentExamPageController.setTeacher = teacherID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/studentExamPage.fxml"));
		Parent Main = loader.load();
		studentExamPageController secController = loader.getController();
		studentExamPageController.studentInExam = true;
		Instance.sendMessage(Command.getExamIdBycode.ordinal() + "@" + examCode.getText() + "@onapp");
		String respone = Instance.getClientConsole().getMessage().toString();
		if (respone.equals("exam not available")) {
			errorTxt.setText("exam not available");
			return;
		}
		Instance.sendMessage(Command.getExamById.ordinal() + "@" + respone);
		String dataString = Instance.getClientConsole().getMessage().toString();
		studentExamPageController.studentIDString = idNum.getText();
		Instance.sendMessage(Command.getNameByUsrName.ordinal() + "@" + usrName);
		studentExamPageController.studName = Instance.getClientConsole().getMessage().toString();
		secController.initByExam(dataString);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Create exam main page");
		Window.setScene(scene);
		Window.show();

	}

	@FXML
	void goBack(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/StudentMainPage.fxml"));
		Scene scene = new Scene(loader.load());
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exams list");
		Window.setScene(scene);
		Window.show();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void init(String usrName, String password) {

		this.usrName = usrName;
		this.password = password;

	}
}
