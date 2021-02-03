/**
 * Sample Skeleton for 'showMessage.fxml' Controller Class
 */

package com.example.controlers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class showMessageController {

	@FXML // fx:id="text"
	private Text text; // Value injected by FXMLLoader

	public void init(String string) {
		text.setText(string);
	}
}
