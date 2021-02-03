/**
 * Sample Skeleton for 'princQuestionList.fxml' Controller Class
 */

package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
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
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class princQuestionListController implements Initializable {

	@FXML // fx:id="questionsList"
	private ListView<String> questionsList; // Value injected by FXMLLoader

	@FXML // fx:id="showBTN"
	private Button showBTN; // Value injected by FXMLLoader

	@FXML // fx:id="filter"
	private ComboBox<String> filter; // Value injected by FXMLLoader

	@FXML // fx:id="errorTXT"
	private Text errorTXT; // Value injected by FXMLLoader

	@FXML // fx:id="backbutton1"
	private Button backbutton1; // Value injected by FXMLLoader

	@FXML
	void addQuestion(ActionEvent event) {

	}

	@FXML
	void back_(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/PrincipalMainPage.fxml"));
		Parent Main = loader.load();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Main page");
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
		secController.init(Instance.getClientConsole().getMessage().toString(), "", "", false);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.getIcons().add(new Image("/com/example/project/images/uni_pic.jpg"));
		Window.setTitle("View or update question");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void refresh(ActionEvent event) throws IOException {
		String selection = filter.getSelectionModel().getSelectedItem();
		if (selection.equals("ALL")) {
			loadQuestions();
			return;
		}
		Instance.sendMessage(Command.getSubjNumber.ordinal() + "@" + selection);
		selection = Instance.getClientConsole().getMessage().toString();

		Instance.sendMessage("" + Command.GETQUESSUBJ.ordinal() + "@" + selection);
		String json = Instance.getClientConsole().getMessage().toString();
		if (json.equals("")) {
			questionsList.getItems().removeAll(questionsList.getItems());
			return;
		}
		List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
		questionsList.getItems().removeAll(questionsList.getItems());
		questionsList.getItems().addAll(l);
	}

	public void loadData() throws IOException {
		loadQuestions();
		loadSubjects();

	}

	public void loadSubjects() throws IOException {
		Instance.sendMessage("" + Command.getSUBJS.ordinal());
		String json = Instance.getClientConsole().getMessage().toString();
		List<String> ll = new ArrayList<String>();
		ll.add("ALL");
		ll.addAll(new ObjectMapper().readValue(json, ArrayList.class));

		filter.getItems().removeAll(filter.getItems());
		filter.getItems().addAll(ll);
		filter.getSelectionModel().select(0);
	}

	public void loadQuestions() throws IOException {
		Instance.sendMessage("" + Command.getQUESTIONS.ordinal());
		String json = Instance.getClientConsole().getMessage().toString();
		List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
		questionsList.getItems().removeAll(questionsList.getItems());
		questionsList.getItems().addAll(l);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			// addQBTN.setVisible(false);
			loadData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
