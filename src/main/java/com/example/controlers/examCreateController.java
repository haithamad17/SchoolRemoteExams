
package com.example.controlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.entities.checkedExam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import jdk.jfr.Description;

public class examCreateController implements Initializable {
	static String userString;
	static String paString;
	static String selection;
	@FXML // fx:id="coursesFilt"
	private ComboBox<String> coursesFilt; // Value injected by FXMLLoader
	static String subName = "";
	static String subNum = "";
	@FXML // fx:id="durationTXT"
	private TextField durationTXT; // Value injected by FXMLLoader
	@FXML // fx:id="errorTXT"
	private Text errorTXT; // Value injected by FXMLLoader
	@FXML // fx:id="insertQ"
	private Button insertQ; // Value injected by FXMLLoader
	@FXML // fx:id="techNotations"
	private TextArea techNotations; // Value injected by FXMLLoader

	@FXML // fx:id="studNotations"
	private TextArea studNotations; // Value injected by FXMLLoader
	@FXML // fx:id="cancelBTN"
	private Button cancelBTN; // Value injected by FXMLLoader
	@FXML // fx:id="subjName"
	private Text subjName; // Value injected by FXMLLoader

	@FXML // fx:id="finishBTN"
	private Button finishBTN; // Value injected by FXMLLoader
	@FXML // fx:id="submitBTN"
	private Button submitBTN; // Value injected by FXMLLoader
	static String Duration = "";
	static String stInfo = "";
	static String techInfo = "";
	static boolean create = true;
	static String back = "/com/example/project/teacherExamsList.fxml";

	@FXML
	void cancel(ActionEvent event) throws IOException {
		reset();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(back));
		Parent Main = loader.load();
		initLoader(loader);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Questions list");
		Window.setScene(scene);
		Window.show();
	}

	public void initLoader(FXMLLoader loader) throws IOException {
		if (back.equals("/com/example/project/PrincipalTeacherExamFilter.fxml")
				|| back.equals("/com/example/project/PrincipalSubjectExamFilter.fxml")) {
			return;
		}
		if ((back.equals("/com/example/project/PrincipalExamsList.fxml"))) {
			PrincipalExamsListController secController = loader.getController();
			secController.init();
		} else {
			teacherExamList secController = loader.getController();
			secController.init(teacherExamList.useString, teacherExamList.passString);

		}
	}

	@FXML
	void getCourseInfo(ActionEvent event) throws IOException {
		selection = coursesFilt.getSelectionModel().getSelectedItem();
		getSubjectName();
	}

	public void getSubjectName() throws IOException {
		Instance.sendMessage(Command.getCourseSubject.ordinal() + "@" + selection);
		String[] argStrings = Instance.getClientConsole().getMessage().toString().split("@");
		subName = argStrings[0];
		subNum = argStrings[1];
		subjName.setText("Exam in: " + subName);
	}

	@FXML
	void insertQuestions(ActionEvent event) throws IOException {
		if (!check()) {
			return;
		}
		Duration = durationTXT.getText();
		techInfo = techNotations.getText();
		stInfo = studNotations.getText();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/examQuestions.fxml"));
		Parent Main = loader.load();
		examsQuestionsController secController = loader.getController();
		examsQuestionsController.paString = examCreateController.paString;
		examsQuestionsController.userString = examCreateController.userString;
		examsQuestionsController.create = create == true ? true : false;
		secController.init(subName, subNum);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Questions list");
		Window.setScene(scene);
		Window.show();
	}

	public void setvisibilty() {
		if (create == false) {
			coursesFilt.setDisable(true);
			durationTXT.setEditable(false);
			studNotations.setEditable(false);
			techNotations.setEditable(false);
			submitBTN.setVisible(false);
		}
	}

	public void initByExam(String data) throws IOException {
		setvisibilty();
		String[] datas = data.split("@");
		examCreateController.userString = datas[0];
		examCreateController.paString = datas[1];
		examCreateController.Duration = datas[2];
		examCreateController.subName = datas[3];
		selection = datas[4];
		examsQuestionsController.studentsInfo = new ObjectMapper().readValue(datas[5], ArrayList.class);
		examsQuestionsController.sName = datas[3];
		examsQuestionsController.teachersInfo = new ObjectMapper().readValue(datas[6], ArrayList.class);
		examsQuestionsController.points = (new ObjectMapper().readValue(datas[7], ArrayList.class));
		examsQuestionsController.questIDs = new ObjectMapper().readValue(datas[8], ArrayList.class);
		examCreateController.stInfo = datas[9];
		examCreateController.techInfo = datas[10];
		filFilter(userString, paString);
		coursesFilt.getSelectionModel().select(datas[4]);

	}

	public void init(String teacherName, String teacherPass) throws IOException {
		examCreateController.paString = teacherPass;
		examCreateController.userString = teacherName;
		if (!Duration.equals(""))
			durationTXT.setText(Duration);
		filFilter(teacherName, teacherPass);
	}

	public void filFilter(String teacherName, String teacherPass) throws IOException {
		Instance.sendMessage(Command.teacherCourses.ordinal() + "@" + teacherName + "@" + teacherPass);
		String json = Instance.getClientConsole().getMessage().toString();
		List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
		coursesFilt.getItems().addAll(l);

		durationTXT.setText(Duration);
		techNotations.setText(techInfo);
		studNotations.setText(stInfo);
		setvisibilty();

	}

	public boolean check() {
		if (!(coursesFilt.getSelectionModel().getSelectedIndex() >= 0)) {
			errorTXT.setText("Select course");
			System.out.println("empty");
			return false;
		}

		return true;
	}

	@FXML
	void addExam(ActionEvent event) throws JsonProcessingException, IOException {
		if (examsQuestionsController.questDiscriptions.size() == 0) {
			errorTXT.setText("Exam is empty");
			return;
		}
		if (durationTXT.getText().isEmpty()) {
			errorTXT.setText("Fill exam duration");
			return;
		}
		if (!Instance.containCH(durationTXT.getText())) {
			errorTXT.setText("Exam duration only in minutes");
			return;
		}
		/*
		 * if (techNotations.getText().isEmpty()) {
		 * errorTXT.setText("Enter information for teachers"); return; } if
		 * (studNotations.getText().isEmpty()) {
		 * errorTXT.setText("Enter information for students"); return; }
		 */
		Duration = durationTXT.getText();
		stInfo = studNotations.getText().isBlank() ? " " : studNotations.getText();
		techInfo = techNotations.getText().isBlank() ? " " : techNotations.getText();
		Instance.sendMessage(Command.AddExam.ordinal() + "@" + examsQuestionsController.getData());
		cancel(event);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void setSelection(String select) {
		selection = select;
		coursesFilt.getSelectionModel().select(select);
		subjName.setText("Exam in: " + subName);
	}

	public static void reset() {
		userString = "";
		paString = "";
		Duration = "";
		stInfo = "";
		techInfo = "";
		create = true;
		examsQuestionsController.cancelAll();
	}

	@FXML
	void setDuration(ActionEvent event) {
		examCreateController.Duration = durationTXT.getText();
	}
}
