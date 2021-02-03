package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.example.ServerClientEntities.Instance;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.bytebuddy.asm.Advice.This;

public class CheckExamShowQuestionsController implements Initializable {

	@FXML // fx:id="myLabel"
	private TextField mylabel; // Value injected by FXMLLoader
	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader

	@FXML // fx:id="questionDisc"
	private TextArea questionDisc; // Value injected by FXMLLoader

	@FXML // fx:id="ans1"
	private TextArea ans1; // Value injected by FXMLLoader

	@FXML // fx:id="ans2"
	private TextArea ans2; // Value injected by FXMLLoader

	@FXML // fx:id="ans3"
	private TextArea ans3; // Value injected by FXMLLoader

	@FXML // fx:id="ans4"
	private TextArea ans4; // Value injected by FXMLLoader

	@FXML // fx:id="selc1"
	private RadioButton selc1; // Value injected by FXMLLoader

	@FXML // fx:id="tglG"
	private ToggleGroup tglG; // Value injected by FXMLLoader

	@FXML // fx:id="selc2"
	private RadioButton selc2; // Value injected by FXMLLoader

	@FXML // fx:id="selc3"
	private RadioButton selc3; // Value injected by FXMLLoader

	@FXML // fx:id="selc4"
	private RadioButton selc4; // Value injected by FXMLLoader

	@FXML // fx:id="nextBTN"
	private Button nextBTN; // Value injected by FXMLLoader

	@FXML // fx:id="backBTN"
	private Button backBTN; // Value injected by FXMLLoader

	@FXML // fx:id="studentInfo"
	private TextArea studentInfo; // Value injected by FXMLLoader

	@FXML // fx:id="teacherInfo"
	private TextArea teacherInfo; // Value injected by FXMLLoader

	@FXML // fx:id="poitns"
	private TextField poitns; // Value injected by FXMLLoader

	@FXML // fx:id="finishBTN"
	private Button finishBTN; // Value injected by FXMLLoader

	@FXML // fx:id="ErrorTXT"
	private Text ErrorTXT; // Value injected by FXMLLoader
	static List<String> discriptions = new ArrayList<>();
	static List<String> studentAnswers = new ArrayList<>();
	static List<String> rightAnswers = new ArrayList<>();
	static List<String> studentInfoPerQ = new ArrayList<>();
	static List<String> teacherInfoPerQ = new ArrayList<>();
	static int current = 0;
	static List<Double> gradesnew = new ArrayList<>();
	static List<List<String>> answersList = new ArrayList<>();
	static boolean isTeacher = true;

	@FXML
	void backToMain(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/OpenCheckedExam.fxml"));
		Parent Main = loader.load();
		OpenCheckedExamController secController = loader.getController();
		if (!isTeacher) {
			secController.hideButtons();
		}
		secController.showData();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Student Exam");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void changeCurrDowner(ActionEvent event) throws IOException {
		if (current - 1 < 0) {
			backToMain(event);
			return;
		}
		current -= 1;
		viewQuest();
		System.out.println(current);
	}

	@FXML
	void changeCurrUpper(ActionEvent event) throws IOException {
		if (current + 1 == discriptions.size()) {
			current = 0;
		} else {
			current += 1;
		}
		viewQuest();
	}

	public void setRightAns() {

		if (ans1.getText().equals(rightAnswers.get(current))) {
			selc1.setSelected(true);
			ans1.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
		}
		if (ans2.getText().equals(rightAnswers.get(current))) {
			selc2.setSelected(true);
			ans2.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
		}
		if (ans3.getText().equals(rightAnswers.get(current))) {
			selc3.setSelected(true);
			ans3.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
		}
		if (ans4.getText().equals(rightAnswers.get(current))) {
			selc4.setSelected(true);
			ans4.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
		}

		return;
	}

	public void reset() {
		ans1.setBackground(null);
		ans2.setBackground(null);
		ans3.setBackground(null);
		ans4.setBackground(null);
		selc1.setSelected(false);
		selc2.setSelected(false);
		selc3.setSelected(false);
		selc4.setSelected(false);
	}

	public void setIfFalse() {
		if (ans1.getText().equals(studentAnswers.get(current)) && !rightAnswers.get(current).equals(ans1.getText())) {
			selc1.setSelected(true);
			ans1.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		}
		if (ans2.getText().equals(studentAnswers.get(current)) && !rightAnswers.get(current).equals(ans2.getText())) {
			selc2.setSelected(true);
			ans2.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		}
		if (ans3.getText().equals(studentAnswers.get(current)) && !rightAnswers.get(current).equals(ans3.getText())) {
			selc3.setSelected(true);
			ans3.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		}
		if (ans4.getText().equals(studentAnswers.get(current)) && !rightAnswers.get(current).equals(ans4.getText())) {
			selc4.setSelected(true);
			ans4.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		}
	}

	public void viewQuest() throws IOException {

		questionDisc.setText(discriptions.get(current));

		List<String> anStrings = answersList.get(current);

		ans1.setText(anStrings.get(0));
		ans2.setText(anStrings.get(1));
		ans3.setText(anStrings.get(2));
		ans4.setText(anStrings.get(3));
		reset();
		setRightAns();
		setIfFalse();
		// teacherInfo.setText(teacherInfoPerQ.get(current));
		studentInfo.setText(studentInfoPerQ.get(current));
		System.out.println("grade is: " + gradesnew.get(current));
		poitns.setText(Double.toString(gradesnew.get(current)));

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// TODO Auto-generated method stub
		// start();
	}

	public static void resetAll() {
		CheckExamShowQuestionsController.discriptions.clear();
		CheckExamShowQuestionsController.studentAnswers.clear();
		CheckExamShowQuestionsController.rightAnswers.clear();
		CheckExamShowQuestionsController.studentInfoPerQ.clear();
		CheckExamShowQuestionsController.teacherInfoPerQ.clear();
		CheckExamShowQuestionsController.current = 0;
		CheckExamShowQuestionsController.gradesnew.clear();
		CheckExamShowQuestionsController.answersList.clear();
		CheckExamShowQuestionsController.isTeacher = true;
	}
}
