package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
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
import javafx.stage.Stage;

public class PrincipalExamsListController implements Initializable {

	@FXML // fx:id="examsList"
	private ListView<String> examsList; // Value injected by FXMLLoader

	@FXML // fx:id="backBtn"
	private Button backBtn; // Value injected by FXMLLoader

	@FXML // fx:id="examShowBtn"
	private Button examShowBtn; // Value injected by FXMLLoader

	@FXML
	private ComboBox<String> filterBtn;

	@FXML
	void filterFunc(ActionEvent event) throws IOException {
		String selection = filterBtn.getSelectionModel().getSelectedItem();
		if (selection.equals("TEACHER")) {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/com/example/project/PrincipalTeacherExamFilter.fxml"));
			Scene scene = new Scene(loader.load());
			Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Window.setTitle("Teacher Filter");
			Window.setScene(scene);
			Window.show();
		}
		if (selection.equals("SUBJECT")) {

			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/com/example/project/PrincipalSubjectExamFilter.fxml"));
			Scene scene = new Scene(loader.load());
			Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Window.setTitle("Subject Filter");
			Window.setScene(scene);
			Window.show();
		}

	}

	@FXML
	void goBack(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/PrincipalMainPage.fxml"));
		Parent Main = loader.load();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Main Page");
		Window.setScene(scene);
		Window.show();

	}

	@FXML
	void showExam(ActionEvent event) throws IOException {
		if (!(examsList.getSelectionModel().getSelectedIndex() >= 0))
			return;
		Instance.sendMessage(Command.getExamById.ordinal() + "@"
				+ examsList.getSelectionModel().getSelectedItem().split("\n")[0].split(" ")[2]);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/examCreation.fxml"));
		Parent Main = loader.load();
		examCreateController secController = loader.getController();
		examCreateController.create = false;
		examCreateController.back = "/com/example/project/PrincipalExamsList.fxml";
		secController.initByExam(Instance.getClientConsole().getMessage().toString());
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Create exam main page");
		Window.setScene(scene);
		Window.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		List<String> ll = new ArrayList<String>();
		ll.add("ALL");
		ll.add("TEACHER");
		ll.add("SUBJECT");
		filterBtn.getItems().removeAll(filterBtn.getItems());
		filterBtn.getItems().addAll(ll);
		filterBtn.getSelectionModel().select(0);

	}

	public void init() throws IOException {

		Instance.getClientConsole().setMessage(null);
		Instance.getClientConsole().sendToServer("" + Command.getAllExams.ordinal());
		while (Instance.getClientConsole().getMessage() == null) {
			System.out.println("waiting for server");

		}

		String json = Instance.getClientConsole().getMessage().toString();
		List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
		examsList.getItems().addAll(l);

	}

}
