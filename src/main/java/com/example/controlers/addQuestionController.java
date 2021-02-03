/**
 * Sample Skeleton for 'addQuestion.fxml' Controller Class
 */

package com.example.controlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class addQuestionController {
	String userString;
	String paString;
	String Snumber = "";
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

	@FXML // fx:id="filterCombo"
	private ComboBox<String> filterCombo; // Value injected by FXMLLoader

	@FXML // fx:id="backBTN"
	private Button backBTN; // Value injected by FXMLLoader

	@FXML // fx:id="submitBTN"
	private Button submitBTN; // Value injected by FXMLLoader

	@FXML // fx:id="ErrorTXT"
	private Text ErrorTXT; // Value injected by FXMLLoader

	@FXML
	void addQuestion(ActionEvent event) throws IOException {
		ErrorTXT.setText("");
		String questionString = questionDisc.getText();
		String subjNumber = Snumber;
		if (!Instance.valQuestion(questionString, subjNumber).equals("all good")) {
			ErrorTXT.setText(Instance.valQuestion(questionString, subjNumber));
			return;
		}

		String an1 = ans1.getText();
		if (an1.isBlank()) {
			ErrorTXT.setText("Must fill first answer");
			return;
		}
		String an2 = ans2.getText();
		if (an2.isBlank()) {
			ErrorTXT.setText("Must fill second answer");
			return;
		}
		String an3 = ans3.getText();
		if (an3.isBlank()) {
			ErrorTXT.setText("Must fill third answer");
			return;
		}
		String an4 = ans4.getText();
		if (an4.isBlank()) {
			ErrorTXT.setText("Must fill fourth answer");
			return;
		}
		if (tglG.getSelectedToggle() == null) {
			ErrorTXT.setText("Select the right answer!");
			return;
		}
		String rAnswer = getRAns();
		String arg = Command.addQ.ordinal() + "@" + questionString + "@" + subjNumber + "@" + an1 + "@" + an2 + "@"
				+ an3 + "@" + an4 + "@" + rAnswer;
		addQuestion(arg);
		back(event);

	}

	@FXML
	void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/questionList.fxml"));
		Scene scene = new Scene(loader.load());
		questionListController secController = loader.getController();
		secController.loadData();
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Question List");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void getSNumber(ActionEvent event) throws IOException {
		Instance.sendMessage(Command.getSubjNumber.ordinal() + "@" + filterCombo.getSelectionModel().getSelectedItem());

		Snumber = Instance.getClientConsole().getMessage().toString();
	}

	public void init(String username, String password) throws IOException {
		userString = username;
		paString = password;
		loadSubjects(username, password);
	}

	public void loadSubjects(String username, String password) throws IOException {
		Instance.sendMessage(Command.teacherSubjects.ordinal() + "@" + username + "@" + password);
		String json = Instance.getClientConsole().getMessage().toString();
		List<String> ll = new ObjectMapper().readValue(json, ArrayList.class);
		filterCombo.getItems().removeAll(filterCombo.getItems());
		filterCombo.getItems().addAll(ll);
		filterCombo.getSelectionModel().select(0);

	}

	int checkQuestion(String qNumber, String subjNumber) throws IOException {
		Instance.sendMessage(Command.isQuestExist.ordinal() + "@" + qNumber + "@" + subjNumber);
		if (Instance.getClientConsole().getMessage().equals("good")) {
			Instance.getClientConsole().setMessage(null);
			return 1;
		}
		Instance.getClientConsole().setMessage(null);
		return -1;
	}

	public String getRAns() {
		ObservableList<Toggle> ans = tglG.getToggles();
		if (ans.get(0).isSelected())
			return ans1.getText();
		if (ans.get(1).isSelected())
			return ans2.getText();
		if (ans.get(2).isSelected())
			return ans3.getText();
		if (ans.get(3).isSelected())
			return ans4.getText();
		return "";
	}

	void addQuestion(String arg) throws IOException {
		Instance.sendMessage(arg);

	}
}
