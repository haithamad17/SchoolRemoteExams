/**
 * Sample Skeleton for 'examRequestList.fxml' Controller Class
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
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class examRequestController implements Initializable {

	@FXML // fx:id="examsList"
	private ListView<String> examsList; // Value injected by FXMLLoader

	@FXML // fx:id="backBtn"
	private Button backBtn; // Value injected by FXMLLoader
	@FXML // fx:id="rejectBTN"
	private Button rejectBTN; // Value injected by FXMLLoader

	@FXML // fx:id="acceptBTN"
	private Button acceptBTN; // Value injected by FXMLLoader

	@FXML
	void AcceptReq(ActionEvent event) throws IOException {
		if (!(examsList.getSelectionModel().getSelectedIndex() >= 0))
			return;
		String query = examsList.getItems().get(examsList.getSelectionModel().getSelectedIndex());
		String examCode = query.split("\n")[0].split(":")[1];
		String examTimeEx = query.split("\n")[2].split(" ")[1];
		Instance.sendMessage(Command.DELLREQ.ordinal() + "@"
				+ examsList.getItems().get(examsList.getSelectionModel().getSelectedIndex()));
		examsList.getItems().remove(examsList.getSelectionModel().getSelectedIndex());
		Instance.sendMessage(Command.APPROVEXT.ordinal() + "@" + examCode + "@" + examTimeEx);
	}

	@FXML
	void RejectReq(ActionEvent event) throws IOException {
		if (!(examsList.getSelectionModel().getSelectedIndex() >= 0))
			return;
		Instance.sendMessage(Command.DELLREQ.ordinal() + "@"
				+ examsList.getItems().get(examsList.getSelectionModel().getSelectedIndex()));
		examsList.getItems().remove(examsList.getSelectionModel().getSelectedIndex());
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Instance.sendMessage(Command.GETREQ.ordinal() + "");
			List<String> l = new ObjectMapper().readValue(Instance.getClientConsole().getMessage().toString(),
					ArrayList.class);
			examsList.getItems().removeAll(examsList.getItems());
			examsList.getItems().addAll(l);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
