package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.operations.ExamOps;
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
import javafx.stage.Stage;

public class PrincipalSubjectExamFilterController implements Initializable {

	@FXML // fx:id="showExambtn"
	private Button showExambtn; // Value injected by FXMLLoader

	@FXML // fx:id="backBTN"
	private Button backBTN; // Value injected by FXMLLoader

	@FXML // fx:id="filterBtn"
	private ComboBox<String> filterBtn; // Value injected by FXMLLoader

	@FXML // fx:id="ExamsList"
	private ListView<String> ExamsList; // Value injected by FXMLLoader

	@FXML
	void back(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/PrincipalExamsList.fxml"));
		Parent Main = loader.load();
		PrincipalExamsListController secController = loader.getController();
		secController.init();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exams List");
		Window.setScene(scene);
		Window.show();

	}

	@FXML
	void showExam(ActionEvent event) throws IOException {
		if (!(ExamsList.getSelectionModel().getSelectedIndex() >= 0))
			return;
		Instance.sendMessage(Command.getExamById.ordinal() + "@"
				+ ExamsList.getSelectionModel().getSelectedItem().split("\n")[0].split(" ")[2]);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/examCreation.fxml"));
		Parent Main = loader.load();
		examCreateController secController = loader.getController();
		examCreateController.create = false;
		examCreateController.back = "/com/example/project/PrincipalSubjectExamFilter.fxml";
		secController.initByExam(Instance.getClientConsole().getMessage().toString());
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("View Exam");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void showFilter(ActionEvent event) throws IOException {

		String selection = filterBtn.getSelectionModel().getSelectedItem();

		if (selection.equals("ALL")) {
			loadAll();
		} else {

			Instance.sendMessage(Command.ExamsBySubject.ordinal() + "@" + selection);
			String json = Instance.getClientConsole().getMessage().toString();
			if (json.equals("")) {
				ExamsList.getItems().removeAll(ExamsList.getItems());
				return;
			}

			List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
			ExamsList.getItems().removeAll(ExamsList.getItems());
			ExamsList.getItems().addAll(l);

		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Instance.getClientConsole().setMessage(null);
		try {
			Instance.getClientConsole().sendToServer("" + Command.getSubjectName.ordinal());
			while (Instance.getClientConsole().getMessage() == null) {
				System.out.println("waiting for server");
			}
			String json = Instance.getClientConsole().getMessage().toString();
			System.out.println("json: " + json);
			List<String> ll = new ObjectMapper().readValue(json, ArrayList.class);
			System.out.println("ll: " + ll);
			filterBtn.getItems().removeAll(filterBtn.getItems());
			filterBtn.getItems().addAll(ll);
			filterBtn.getSelectionModel().select(0);
			loadAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void loadAll() throws IOException {
		Instance.getClientConsole().setMessage(null);
		Instance.getClientConsole().sendToServer("" + Command.getAllExams.ordinal());
		while (Instance.getClientConsole().getMessage() == null) {
			System.out.println("waiting for server");

		}

		String json = Instance.getClientConsole().getMessage().toString();
		List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
		ExamsList.getItems().removeAll(ExamsList.getItems());
		ExamsList.getItems().addAll(l);
	}

}
