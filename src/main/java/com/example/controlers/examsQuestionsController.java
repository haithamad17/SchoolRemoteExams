/**
 * Sample Skeleton for 'examQuestions.fxml' Controller Class
 */

package com.example.controlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.entities.Question;
import com.example.operations.generalOps;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Duration;
import com.mysql.cj.x.protobuf.MysqlxSession.Reset;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class examsQuestionsController {

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
	@FXML // fx:id="inserBTN"
	private Button inserBTN; // Value injected by FXMLLoader
	@FXML // fx:id="questionsFilt"
	private ComboBox<String> questionsFilt; // Value injected by FXMLLoader
	@FXML // fx:id="studentInfo"
	private TextArea studentInfo; // Value injected by FXMLLoader

	@FXML // fx:id="teacherInfo"
	private TextArea teacherInfo; // Value injected by FXMLLoader
	@FXML // fx:id="poitns"
	private TextField poitns; // Value injected by FXMLLoader
	@FXML // fx:id="updatebtn"
	private Button updatebtn; // Value injected by FXMLLoader

	@FXML // fx:id="deletebtn"
	private Button deletebtn; // Value injected by FXMLLoader
	@FXML // fx:id="ErrorTXT"
	private Text ErrorTXT; // Value injected by FXMLLoader
	@FXML // fx:id="ErrorTXT2"
	private Text ErrorTXT2; // Value injected by FXMLLoader
	@FXML // fx:id="finishBTN"
	private Button finishBTN; // Value injected by FXMLLoader
	static int Current = 0;
	static List<String> questDiscriptions = new ArrayList<String>();
	static List<String> studentsInfo = new ArrayList<String>();
	static List<String> teachersInfo = new ArrayList<String>();
	static List<String> questIDs = new ArrayList<String>();
	static List<Double> points = new ArrayList<Double>();
	static List<String> rightAnswers = new ArrayList<String>();
	static List<ArrayList<String>> answers = new ArrayList<ArrayList<String>>();
	static String sName = "";
	static String sNumber = "";
	static String userString;
	static String paString;
	static String Id = "";
	static boolean create = true;
	static String back = "/com/example/project/showMessage.fxml";

	@FXML
	void changeCurrUpper(ActionEvent event) throws IOException {
		if (Current + 1 > questDiscriptions.size())
			return;
		if (Current + 1 == questDiscriptions.size()) {
			Current += 1;
			questionsFilt.setDisable(false);
			deletebtn.setVisible(false);
			inserBTN.setVisible(true);
			updatebtn.setVisible(false);
			setvisibilty();

			reset();
			return;
		}
		Current += 1;
		viewQuest();
		System.out.println(Current);
	}

	@FXML
	void changeCurrDowner(ActionEvent event) throws IOException {
		if (Current - 1 < 0) {
			backToMain(event);
			return;
		}
		Current -= 1;
		viewQuest();
		System.out.println(Current);
	}

	public void setvisibilty() {
		if (create == false) {
			studentInfo.setEditable(false);
			teacherInfo.setEditable(false);
			deletebtn.setVisible(false);
			updatebtn.setVisible(false);
			inserBTN.setVisible(false);
			poitns.setEditable(false);
			questionsFilt.setDisable(true);
		}
	}

	public void init(String subName, String SubNumber) throws IOException {
		tglG.getToggles().setAll(selc1, selc2, selc3, selc4);
		setvisibilty();
		questionsFilt.setDisable(false);
		System.out.println("heeeeeeeeeeeeeeeeere");
		if (!examsQuestionsController.sName.equals(subName)) {
			questDiscriptions.clear();
			questIDs.clear();
			answers.clear();
			teachersInfo.clear();
			studentsInfo.clear();
			rightAnswers.clear();
			points.clear();
			sName = subName;
			sNumber = SubNumber;
		}
		loadQuestionList();
		if (questDiscriptions.size() > 0) {
			questionsFilt.setDisable(true);
			Current = 0;
			viewQuest();
		}

	}

	public void loadQuestionList() throws IOException {

		Instance.sendMessage(Command.teachQuesSubj.ordinal() + "@" + userString + "@" + paString + "@" + sName);
		String json = Instance.getClientConsole().getMessage().toString();
		List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
		if (questIDs.size() > 0 && questDiscriptions.size() == 0) {
			for (String id : questIDs) {
				Instance.sendMessage(Command.getQ.ordinal() + "@" + id);
				List<String> q = new ObjectMapper().readValue(Instance.getClientConsole().getMessage().toString(),
						ArrayList.class);
				questDiscriptions.add(q.get(0));
				List<String> ans = new ArrayList<>();
				questionDisc.setText(l.get(0));
				ans.add(q.get(3));
				ans.add(q.get(4));
				ans.add(q.get(5));
				ans.add(q.get(6));
				answers.add((ArrayList<String>) ans);
			}
		}
		for (int i = 0; i < questIDs.size(); i++) {
			l.remove("Id: " + questIDs.get(i) + "\n" + questDiscriptions.get(i));
		}
		questionsFilt.getItems().removeAll(questionsFilt.getItems());
		questionsFilt.getItems().addAll(l);
		updateQuestionList();
	}

	public void updateQuestionList() {
		System.out.println(questIDs.size());
		System.out.println(points.size());
		for (String id : questIDs) {
			System.out.println(id);
		}
	}

	@FXML
	void getQuestion(ActionEvent event) throws IOException {
		if (questionsFilt.getItems().isEmpty())
			return;
		ErrorTXT.setVisible(false);
		ErrorTXT2.setVisible(false);
		inserBTN.setVisible(true);
		updatebtn.setVisible(false);
		int index = 0;
		if (!(questionsFilt.getSelectionModel().getSelectedIndex() >= 0))
			index = 0;
		else {
			index = questionsFilt.getSelectionModel().getSelectedIndex();
		}
		String dscpString = (questionsFilt.getItems().get(index));
		String id = dscpString.split("\n")[0].split(" ")[1];
		Id = id;
		Instance.sendMessage(Command.getQ.ordinal() + "@" + id);
		List<String> l = new ObjectMapper().readValue(Instance.getClientConsole().getMessage().toString(),
				ArrayList.class);
		System.out.println("id is " + Id);
		reset();
		questionDisc.setText(l.get(0));
		ans1.setText(l.get(3));
		ans2.setText(l.get(4));
		ans3.setText(l.get(5));
		ans4.setText(l.get(6));
		setRightAns(l.get(7));

	}

	public String getAns(String id) throws IOException {
		Instance.sendMessage(Command.getQ.ordinal() + "@" + id);
		List<String> l = new ObjectMapper().readValue(Instance.getClientConsole().getMessage().toString(),
				ArrayList.class);
		System.out.println(l.get(7));
		return l.get(7);
	}

	public void setRightAns(String answer) {
		System.out.println("right answer: " + answer);

		if (ans1.getText().equals(answer))
			selc1.setSelected(true);
		if (ans2.getText().equals(answer))
			selc2.setSelected(true);
		if (ans3.getText().equals(answer))
			selc3.setSelected(true);
		if (ans4.getText().equals(answer))
			selc4.setSelected(true);
		return;
	}

	public String getRightAnswer() {
		if (selc1.isSelected())
			return ans1.getText();
		if (selc2.isSelected())
			return ans2.getText();
		if (selc3.isSelected())
			return ans3.getText();
		if (selc4.isSelected())
			return ans4.getText();

		return "";
	}

	public void reset() {
		ans1.setText("");
		ans2.setText("");
		ans3.setText("");
		ans4.setText("");
		questionDisc.setText("");
		selc1.setSelected(false);
		selc2.setSelected(false);
		selc3.setSelected(false);
		selc4.setSelected(false);
		teacherInfo.setText("");
		studentInfo.setText("");
		poitns.setText("");
	}

	@FXML
	void insert(ActionEvent event) throws IOException {
		if (!(questionsFilt.getSelectionModel().getSelectedIndex() >= 0)) {
			ErrorTXT.setVisible(true);
			return;
		}
		if (poitns.getText().isBlank()) {
			ErrorTXT2.setVisible(true);
			return;
		}

		try {
			Double num = Double.parseDouble(poitns.getText());
		} catch (NumberFormatException e) {
			ErrorTXT2.setVisible(true);
			ErrorTXT2.setText("Points only digits");
			return;
		}
		points.add(Current, Double.parseDouble(poitns.getText()));
		System.out.println("here");
		questDiscriptions.add(Current, questionDisc.getText());
		questIDs.add(Current, Id);
		List<String> anStrings = new ArrayList<String>();
		anStrings.add(ans1.getText());
		anStrings.add(ans2.getText());
		anStrings.add(ans3.getText());
		anStrings.add(ans4.getText());
		answers.add(Current, (ArrayList<String>) anStrings);
		// rightAnswers.add(Current, getRightAnswer());
		teachersInfo.add(Current, teacherInfo.getText());
		studentsInfo.add(Current, studentInfo.getText());
		questionsFilt.getItems().remove("Id: " + questIDs.get(Current) + "\n" + questionDisc.getText());
		questionsFilt.getSelectionModel().clearSelection();
		changeCurrUpper(null);
	}

	public void viewQuest() throws IOException {

		ErrorTXT.setVisible(false);
		questionsFilt.setDisable(true);
		inserBTN.setVisible(false);
		deletebtn.setVisible(true);
		updatebtn.setVisible(true);
		setvisibilty();
		questionDisc.setText(questDiscriptions.get(Current));
		Id = questIDs.get(Current);
		System.out.println("Id is : " + Id);
		List<String> anStrings = answers.get(Current);

		ans1.setText(anStrings.get(0));
		ans2.setText(anStrings.get(1));
		ans3.setText(anStrings.get(2));
		ans4.setText(anStrings.get(3));
		setRightAns(getAns(Id));
		teacherInfo.setText(teachersInfo.get(Current));
		studentInfo.setText(studentsInfo.get(Current));
		poitns.setText(Double.toString(points.get(Current)));

	}

	@FXML
	void update(ActionEvent event) throws IOException {
		List<String> anStrings = new ArrayList<String>();
		anStrings.add(ans1.getText());
		anStrings.add(ans2.getText());
		anStrings.add(ans3.getText());
		anStrings.add(ans4.getText());
		answers.get(Current).clear();
		answers.get(Current).addAll((ArrayList<String>) anStrings);
		teachersInfo.add(Current, teacherInfo.getText());
		teachersInfo.remove(Current + 1);
		studentsInfo.add(Current, studentInfo.getText());
		studentsInfo.remove(Current + 1);
		FXMLLoader loader = new FXMLLoader(getClass().getResource(back));
		Parent Main = loader.load();
		showMessageController secController = loader.getController();
		secController.init("Question updated");
		Stage stage = new Stage();
		stage.setScene(new Scene(Main));
		stage.show();
	}

	@FXML
	void delete(ActionEvent event) throws IOException {
		questionsFilt.getItems().add("Id: " + questIDs.get(Current) + "\n" + questionDisc.getText());
		questDiscriptions.remove(Current);
		questIDs.remove(Current);
		answers.remove(Current);
		teachersInfo.remove(Current);
		studentsInfo.remove(Current);
		points.remove(Current);
		// rightAnswers.remove(Current);
		reset();
		Current--;
		if (Current < 0) {
			questionsFilt.setDisable(false);
			Current = 0;
			return;
		}
		viewQuest();
	}

	public static void cancelAll() {
		Current = 0;
		questDiscriptions.clear();
		studentsInfo.clear();
		teachersInfo.clear();
		questIDs.clear();
		answers.clear();
		points.clear();
		rightAnswers.clear();
		sName = "";
		sNumber = "";
		userString = "";
		paString = "";
		Id = "";

	}

	@FXML
	void backToMain(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/examCreation.fxml"));
		Parent Main = loader.load();
		examCreateController secController = loader.getController();
		secController.filFilter(examCreateController.userString, examCreateController.paString);
		secController.setSelection(examCreateController.selection);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Create exam main page");
		Window.setScene(scene);
		Window.show();
	}

	public static String getData() throws JsonProcessingException {

		String toRetString = "";
		toRetString += teacherExamList.useString + "@" + teacherExamList.passString + "@"
				+ examCreateController.Duration + "@" + sName + "@" + examCreateController.selection + "@"
				+ generalOps.getJsonString(studentsInfo) + "@" + generalOps.getJsonString(teachersInfo) + "@"
				+ generalOps.getJsonString(points) + "@" + generalOps.getJsonString(questIDs) + "@"
				+ examCreateController.stInfo + "@" + examCreateController.techInfo;

		return toRetString;
	}
}
