package com.example.controlers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.entities.checkedExam;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OpenCheckedExamController {

	@FXML // fx:id="toQuestions"
	private Button toQuestions; // Value injected by FXMLLoader

	@FXML // fx:id="studNotations"
	private TextArea studNotations; // Value injected by FXMLLoader

	@FXML // fx:id="submitBTN"
	private Button submitBTN; // Value injected by FXMLLoader

	@FXML // fx:id="studentName"
	private Text studentName; // Value injected by FXMLLoader

	@FXML // fx:id="examDur"
	private Text examDur; // Value injected by FXMLLoader

	@FXML // fx:id="tacherName"
	private Text tacherName; // Value injected by FXMLLoader

	@FXML // fx:id="systemGrade"
	private Text systemGrade; // Value injected by FXMLLoader

	@FXML // fx:id="teacherComments"
	private TextArea teacherComments; // Value injected by FXMLLoader

	@FXML // fx:id="lastGrade"
	private TextField lastGrade; // Value injected by FXMLLoader
	@FXML // fx:id="errorTXT"
	private Text errorTXT; // Value injected by FXMLLoader
	@FXML // fx:id="lastGF"
	private Text lastGF; // Value injected by FXMLLoader
	@FXML // fx:id="back"
	private Button back; // Value injected by FXMLLoader
	static String teacherName = "";
	static String studtName = "";
	static String time = "";
	static String grade = "";
	static String Lgrade = "";
	static String teacherCommentsExam = "";
	static String exId;
	static String dicriptionsTech = "";
	static boolean isTeacher = true;
	static String back_ = "/com/example/project/CheckExam.fxml";

	@FXML
	void goBack(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(back_));
		Parent Main = loader.load();

		if (back_.equals("/com/example/project/CheckExam.fxml")) {
			CheckExamController secController = loader.getController();
			secController.init(CheckExamController.username, CheckExamController.password, false);
		}
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Check Exams list");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void addExam(ActionEvent event) throws IOException {
		if (lastGrade.getText().isEmpty()) {
			errorTXT.setText("Must set you're grade");
			return;
		}
		if (!Instance.containCH(lastGrade.getText())) {
			errorTXT.setText("Grade shoulde be only digits");
			return;
		}
		if (Double.valueOf(lastGrade.getText()) > 100) {
			errorTXT.setText("Grade should be 0-100!");
			return;
		}
		if (studNotations.getText().isBlank()) {
			errorTXT.setText("Fill you're comments!");
			return;
		}
		Instance.sendMessage(Command.teachAPPROVE.ordinal() + "@" + exId + "@" + lastGrade.getText() + "@"
				+ studNotations.getText());
		resetAll();
		goBack(event);
	}

	@FXML
	void showQuestions(ActionEvent event) throws IOException {
		Lgrade = lastGrade.getText();
		dicriptionsTech = studNotations.getText();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/CheckExamShowQuestions.fxml"));
		Parent Main = loader.load();
		CheckExamShowQuestionsController secController = loader.getController();
		if (!isTeacher)
			CheckExamShowQuestionsController.isTeacher = false;
		secController.viewQuest();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Create exam main page");
		Window.setScene(scene);
		Window.show();

	}

	public void initByExam(String data) throws IOException, SQLException {
		if (!isTeacher)
			hideButtons();
		String[] datas = data.split("@");
		OpenCheckedExamController.teacherName = datas[0];
		OpenCheckedExamController.studtName = datas[1];
		OpenCheckedExamController.time = datas[2];
		OpenCheckedExamController.grade = datas[4];
		OpenCheckedExamController.exId = datas[13];
		OpenCheckedExamController.teacherCommentsExam = datas[6];
		teacherComments.setText(datas[6]);
		CheckExamShowQuestionsController.discriptions = new ObjectMapper().readValue(datas[9], ArrayList.class);
		CheckExamShowQuestionsController.studentAnswers = new ObjectMapper().readValue(datas[7], ArrayList.class);
		CheckExamShowQuestionsController.rightAnswers = new ObjectMapper().readValue(datas[8], ArrayList.class);
		CheckExamShowQuestionsController.studentInfoPerQ = new ObjectMapper().readValue(datas[3], ArrayList.class);
		// CheckExamShowQuestionsController.teacherInfoPerQ = new
		// ObjectMapper().readValue(datas[10], ArrayList.class);
		CheckExamShowQuestionsController.gradesnew = new ObjectMapper().readValue(datas[11], ArrayList.class);
		CheckExamShowQuestionsController.answersList = new ObjectMapper().readValue(datas[12], ArrayList.class);

		showData();
	}

	public void hideButtons() {

		submitBTN.setVisible(false);
		lastGrade.setVisible(false);
		lastGF.setVisible(false);
		studNotations.setEditable(false);

	}

	public void showData() {
		examDur.setText(time);
		tacherName.setText(teacherName);
		studentName.setText(studtName);
		systemGrade.setText(grade);
		teacherComments.setText(teacherCommentsExam);
		lastGrade.setText(Lgrade);
		studNotations.setText(dicriptionsTech);
		if (isTeacher == false) {
			teacherComments.setVisible(false);
			studNotations.setText("Teacher comment:\n" + teacherCommentsExam);
		}
	}

	public static void resetAll() {
		OpenCheckedExamController.teacherName = "";
		OpenCheckedExamController.studtName = "";
		OpenCheckedExamController.time = "";
		OpenCheckedExamController.grade = "";
		OpenCheckedExamController.Lgrade = "";
		OpenCheckedExamController.teacherCommentsExam = "";
		OpenCheckedExamController.exId = "";
		OpenCheckedExamController.dicriptionsTech = "";
		OpenCheckedExamController.isTeacher = true;
		CheckExamShowQuestionsController.resetAll();
	}
}
