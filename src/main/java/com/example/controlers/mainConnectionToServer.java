/**
 * Sample Skeleton for 'mainConnectionToServer.fxml' Controller Class
 */

package com.example.controlers;

import java.io.IOException;

import com.example.ServerClientEntities.Instance;
import com.example.ServerClientEntities.SimpleChatClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class mainConnectionToServer {

	@FXML // fx:id="IPADDRESS"
	private TextField IPADDRESS; // Value injected by FXMLLoader

	@FXML // fx:id="PORT"
	private TextField PORT; // Value injected by FXMLLoader

	@FXML // fx:id="ErrorTXT"
	private Text ErrorTXT; // Value injected by FXMLLoader

	@FXML // fx:id="ConnectionButton"
	private Button ConnectionButton; // Value injected by FXMLLoader

	@FXML
	void Connect(ActionEvent event) throws IOException {
		ErrorTXT.setText("");
		String IP = IPADDRESS.getText();
		String Port = PORT.getText();

		if (IP.equals("")) {
			ErrorTXT.setText("Please Insert IP!");
		}
		if (Port.equals("")) {
			ErrorTXT.setText("Please Insert Port!");
		}

		try {
			SimpleChatClient chatClient = new SimpleChatClient(IP, Integer.valueOf(Port));
			Instance.clientConsole = chatClient;
			chatClient.openConnection();
			Parent Main = FXMLLoader.load(getClass().getResource("/com/example/project/logIn.fxml"));
			Scene scene = new Scene(Main);
			Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Window.setTitle("MainScreen");
			Window.setScene(scene);
			Window.show();
		} catch (IOException e) {
			ErrorTXT.setText("Connection Failed, Please Check you Inserts!");
			e.printStackTrace();
		}
		;

	}

}
