package com.example.controlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sample Skeleton for 'questionList.fxml' Controller Class
 */

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class questionListController {
	static String userString;
	static String paString;
	ObservableList<String> list = FXCollections.observableArrayList();
	@FXML // fx:id="questionsList"
	private ListView<String> questionsList; // Value injected by FXMLLoader

	@FXML // fx:id="showBTN"
	private Button showBTN; // Value injected by FXMLLoader
	@FXML // fx:id="filter"
	private ComboBox<String> filter; // Value injected by FXMLLoader
	@FXML // fx:id="errorTXT"
	private Text errorTXT; // Value injected by FXMLLoader

	@FXML // fx:id="backbutton"
	private Button backbutton; // Value injected by FXMLLoader
	@FXML // fx:id="loadbtn"
	private Button loadbtn; // Value injected by FXMLLoader
	@FXML // fx:id="addQBTN"
	private Button addQBTN; // Value injected by FXMLLoader

	@FXML
	void back_(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/teacherMainPage.fxml"));
		Scene scene = new Scene(loader.load());
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Main page");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void refresh(ActionEvent event) throws IOException {
		String selection = filter.getSelectionModel().getSelectedItem();
		if (selection.equals("All")) {
			loadQuestions(userString, paString);
			return;
		} else {
			Instance.sendMessage(Command.teachQuesSubj.ordinal() + "@" + userString + "@" + paString + "@" + selection);
			String json = Instance.getClientConsole().getMessage().toString();
			if (json.equals("")) {
				questionsList.getItems().removeAll(questionsList.getItems());
				return;
			}
			List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
			questionsList.getItems().removeAll(questionsList.getItems());
			questionsList.getItems().addAll(l);
		}

	}

	public void init(String username, String password) throws IOException {
		questionListController.userString = username;
		questionListController.paString = password;
		loadData();

	}

	public void loadData() throws IOException {

		loadQuestions(userString, paString);
		loadSubjects(userString, paString);

	}

	public void loadSubjects(String username, String password) throws IOException {
		Instance.sendMessage(Command.teacherSubjects.ordinal() + "@" + username + "@" + password);
		String json = Instance.getClientConsole().getMessage().toString();
		List<String> ll = new ObjectMapper().readValue(json, ArrayList.class);
		filter.getItems().removeAll(filter.getItems());
		filter.getItems().addAll(ll);
		filter.getSelectionModel().select(0);
	}

	public void loadQuestions(String username, String password) throws IOException {

		Instance.sendMessage(Command.teacherQuestions.ordinal() + "@" + username + "@" + password);
		String json = Instance.getClientConsole().getMessage().toString();
		if (json.isEmpty())
			return;
		List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
		questionsList.getItems().removeAll(questionsList.getItems());
		questionsList.getItems().addAll(l);
	}

	@FXML
	void addQuestion(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/addQuestion.fxml"));
		Parent Main = loader.load();
		addQuestionController secController = loader.getController();
		secController.init(userString, paString);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.getIcons().add(new Image("/com/example/project/images/uni_pic.jpg"));
		Window.setTitle("Add question page");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void loadQ(ActionEvent event) throws IOException {
		if (!(questionsList.getSelectionModel().getSelectedIndex() >= 0))
			return;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/viewORupdateQuestion.fxml"));
		Parent Main = loader.load();
		viewORUpdateQuestController secController = loader.getController();
		String id = questionsList.getSelectionModel().getSelectedItem().split("\n")[0].split(" ")[1];
		Instance.sendMessage(Command.getQ.ordinal() + "@" + id);
		secController.init(Instance.getClientConsole().getMessage().toString(), userString, paString, true);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.getIcons().add(new Image("/com/example/project/images/uni_pic.jpg"));
		Window.setTitle("View or update question");
		Window.setScene(scene);
		Window.show();
	}

}
