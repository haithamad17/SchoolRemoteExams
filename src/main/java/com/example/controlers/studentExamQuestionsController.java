/**
 * Sample Skeleton for 'studentExamQuestions.fxml' Controller Class
 */

package com.example.controlers;

import java.awt.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.controlers.studentExamPageController.examTimer;
import com.example.operations.generalOps;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.bytebuddy.asm.Advice.This;

public class studentExamQuestionsController implements Initializable {

	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader
	@FXML // fx:id="myLabel"
	private TextField mylabel; // Value injected by FXMLLoader
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

	@FXML // fx:id="finishBTN"
	private Button finishBTN; // Value injected by FXMLLoader
	@FXML // fx:id="Qpoints"
	private Text Qpoints; // Value injected by FXMLLoader
	@FXML // fx:id="ErrorTXT"
	private Text ErrorTXT; // Value injected by FXMLLoader
	@FXML // fx:id="minuts"
	private Text minuts; // Value injected by FXMLLoader
	@FXML // fx:id="seconds"
	private Text seconds; // Value injected by FXMLLoader
	static List<String> questDiscriptions = new ArrayList<String>();
	static List<String> studentsInfo = new ArrayList<String>();
	static List<String> questIDs = new ArrayList<String>();
	static List<Double> points = new ArrayList<Double>();
	static List<Boolean> isAnswerd = new ArrayList<Boolean>();
	static List<String> studentAnswers = new ArrayList<String>();
	static List<ArrayList<String>> answers = new ArrayList<ArrayList<String>>();
	static int Current = 0;
	static int exTimeSec = 0;
	static int exTimeMin = 0;
	static String teacherID = "";
	static boolean studentInExam = false;
	static Thread t = null;
	@FXML // fx:id="errorTXT"
	private Text errorTXT; // Value injected by FXMLLoader

	@FXML
	void backToMain(ActionEvent event) throws IOException, InterruptedException {
		setView();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/studentExamPage.fxml"));
		Parent Main = loader.load();
		studentExamPageController secController = loader.getController();
		secController.showData();
		Scene scene = new Scene(Main);
		Stage Window = event != null ? (Stage) ((Node) event.getSource()).getScene().getWindow()
				: (Stage) mainPane.getScene().getWindow();
		Window.setTitle("Exam Main Page");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void changeCurrDowner(ActionEvent event) throws IOException, InterruptedException {
		if (Current - 1 < 0) {
			backToMain(event);
			return;
		}
		Current -= 1;
		viewQuest();
		System.out.println(Current);
	}

	@FXML
	void changeCurrUpper(ActionEvent event) {
		if (Current + 1 == questDiscriptions.size()) {
			Current = 0;
		} else {
			Current += 1;
		}
		viewQuest();
	}

	@FXML
	void delete(ActionEvent event) {

	}

	@FXML
	void insert(ActionEvent event) {

	}

	@FXML
	void update(ActionEvent event) {

	}

	@FXML
	void updateAnswers(ActionEvent event) {

		ObservableList<Toggle> ans = tglG.getToggles();
		if (ans.get(0).isSelected()) {
			isAnswerd.set(Current, true);
			studentAnswers.set(Current, ans1.getText());
			System.out.println(studentAnswers.toString());
			return;
		}
		if (ans.get(1).isSelected()) {
			isAnswerd.set(Current, true);
			studentAnswers.set(Current, ans2.getText());
			System.out.println(studentAnswers.toString());
			return;
		}
		if (ans.get(2).isSelected()) {
			isAnswerd.set(Current, true);
			studentAnswers.set(Current, ans3.getText());
			System.out.println(studentAnswers.toString());
			return;
		}
		if (ans.get(3).isSelected()) {
			isAnswerd.set(Current, true);
			studentAnswers.set(Current, ans4.getText());
			System.out.println(studentAnswers.toString());
			return;
		}

	}

	public void viewQuest() {
		questionDisc.setText(questDiscriptions.get(Current));

		List<String> anStrings = answers.get(Current);
		ans1.setText(anStrings.get(0));
		ans2.setText(anStrings.get(1));
		ans3.setText(anStrings.get(2));
		ans4.setText(anStrings.get(3));
		if (isAnswerd.get(Current) == true) {
			setAnswer();
		} else {
			selc1.setSelected(false);
			selc2.setSelected(false);
			selc3.setSelected(false);
			selc4.setSelected(false);
		}
		studentInfo.setText(studentsInfo.get(Current));
		Qpoints.setText(Double.toString(points.get(Current)));
	}

	public void setAnswer() {
		if (ans1.getText().equals(studentAnswers.get(Current)))
			selc1.setSelected(true);
		if (ans2.getText().equals(studentAnswers.get(Current)))
			selc2.setSelected(true);
		if (ans3.getText().equals(studentAnswers.get(Current)))
			selc3.setSelected(true);
		if (ans4.getText().equals(studentAnswers.get(Current)))
			selc4.setSelected(true);
	}

	public static void loadDiscriptions() throws IOException {
		for (String id : questIDs) {
			Instance.sendMessage(Command.getQ.ordinal() + "@" + id);
			List<String> l = new ObjectMapper().readValue(Instance.getClientConsole().getMessage().toString(),
					ArrayList.class);
			System.out.println("id is " + id);
			List<String> ans = new ArrayList<>();
			questDiscriptions.add(l.get(0));
			isAnswerd.add(false);
			studentAnswers.add("");
			ans.add(l.get(3));
			ans.add(l.get(4));
			ans.add(l.get(5));
			ans.add(l.get(6));
			answers.add((ArrayList<String>) ans);
		}
	}

	public static String getData() throws JsonProcessingException {

		String toRetString = "";
		toRetString += studentExamPageController.studentIDString + "@"
				+ generalOps.getJsonString(studentExamQuestionsController.studentAnswers) + "@"
				+ StudentStartExamPageController.examC + "@" + teacherID;

		return toRetString;
	}

	public static void resetAll() {
		questDiscriptions.clear();
		studentsInfo.clear();
		questIDs.clear();
		points.clear();
		isAnswerd.clear();
		studentAnswers.clear();
		answers.clear();
		Current = 0;
		teacherID = "";
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		if (studentInExam) {
			exTimeSec = studentExamPageController.secondsExam;
			exTimeMin = studentExamPageController.mintsExam;
			examTimer myTimer = new examTimer();
			t = new Thread(myTimer);
			t.start();
		}

	}

	public void setView() {
		minuts.setText("00");
		seconds.setText("00");
		errorTXT.setVisible(true);
		nextBTN.setVisible(true);
		backBTN.setVisible(true);
		selc1.setDisable(false);
		selc2.setDisable(false);
		selc3.setDisable(false);
		selc4.setDisable(false);
	}

	public class examTimer implements Runnable {
		int second;
		int mints;

		public examTimer() {
			this.second = studentExamPageController.secondsExam;
			this.mints = studentExamPageController.mintsExam;
		}

		@Override
		public void run() {
			while (true) {
				seconds.setText(Integer.toString(studentExamPageController.secondsExam));
				minuts.setText(Integer.toString(studentExamPageController.mintsExam));
				if (studentExamPageController.mintsExam < 0 && studentExamPageController.secondsExam <= 0) {
					minuts.setText("00");
					seconds.setText("00");
					errorTXT.setVisible(true);
					nextBTN.setVisible(false);
					backBTN.setVisible(false);
					selc1.setDisable(true);
					selc2.setDisable(true);
					selc3.setDisable(true);
					selc4.setDisable(true);
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

}
