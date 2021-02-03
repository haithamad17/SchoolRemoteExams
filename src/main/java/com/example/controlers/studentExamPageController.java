/**
 * Sample Skeleton for 'studentExamPage.fxml' Controller Class
 */

package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.operations.generalOps;
import com.example.project.startApp;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class studentExamPageController implements Initializable {

	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader
	@FXML // fx:id="toQuestions"
	private Button toQuestions; // Value injected by FXMLLoader

	@FXML // fx:id="studNotations"
	private TextArea studNotations; // Value injected by FXMLLoader

	@FXML // fx:id="submitBTN"
	private Button submitBTN; // Value injected by FXMLLoader

	@FXML // fx:id="minuts"
	private TextField minuts; // Value injected by FXMLLoader

	@FXML // fx:id="seconds"
	private TextField seconds; // Value injected by FXMLLoader
	@FXML // fx:id="courseName"
	private Text courseName; // Value injected by FXMLLoader

	@FXML // fx:id="studentName"
	private Text studentName; // Value injected by FXMLLoader

	@FXML // fx:id="studentd"
	private Text studentd; // Value injected by FXMLLoader

	@FXML // fx:id="examDur"
	private Text examDur; // Value injected by FXMLLoader

	@FXML // fx:id="tacherName"
	private Text tacherName; // Value injected by FXMLLoader

	@FXML // fx:id="backBtn"
	private Button backBtn; // Value injected by FXMLLoader
	ActionEvent myEvent;

	@FXML // fx:id="errorTXT"
	private Text errorTXT; // Value injected by FXMLLoader
	static String studName = "";
	static String studentIDString = "";
	static String userString = "";
	static String paString = "";
	static String duration = "";
	static String subName = "";
	static String cName = "";
	static String stInfo = "";
	static String teacherName = "";
	static int secondsExam = 60;
	static int mintsExam = 60;
	static String setTeacher = "";
	static boolean studentInExam = false;
	static boolean firstTime = true;
	static int lastAddition = 0;
	static String examCode = "";
	private Thread t = null;
	static boolean done = false;
	static int checks = 0;
	int addition;
	int firsDuration;
	static int minTotal = 0;

	public void showData() {
		examDur.setText(duration);
		studentName.setText(studName);
		studentd.setText(studentIDString);
		studNotations.setText(stInfo);
		courseName.setText(cName);
		tacherName.setText(teacherName);
	}

	@FXML
	void addExam(ActionEvent event) throws IOException, InterruptedException {

		String realtime = String.valueOf(firsDuration + addition - minTotal + 1);
		System.out.println("took for you " + (Integer.parseInt(duration) - mintsExam));
		Instance.sendMessage(Command.studentSubmmit.ordinal() + "@" + studentExamQuestionsController.getData()
				+ "@Real time duration: " + realtime);
		toQuestions.setVisible(true);
		errorTXT.setVisible(false);
		resetAll();
		done = true;
	}

	@FXML
	void showQuestions(ActionEvent event) throws IOException, InterruptedException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/studentExamQuestions.fxml"));
		Parent Main = loader.load();
		studentExamQuestionsController secController = loader.getController();
		examsQuestionsController.paString = examCreateController.paString;
		studentExamQuestionsController.exTimeSec = studentExamPageController.secondsExam;
		studentExamQuestionsController.exTimeMin = studentExamPageController.mintsExam;
		secController.viewQuest();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Questions list");
		Window.setScene(scene);
		Window.show();
	}

	public void initByExam(String data) throws IOException, SQLException {
		addition = 0;
		studentExamPageController.minTotal = 0;
		secondsExam = 60;
		String[] datas = data.split("@");
		studentExamPageController.userString = datas[0];
		studentExamPageController.paString = datas[1];
		studentExamPageController.duration = datas[2];
		firsDuration = Integer.parseInt(duration);
		studentExamPageController.subName = datas[3];
		studentExamPageController.cName = datas[4];
		studentExamQuestionsController.studentsInfo = new ObjectMapper().readValue(datas[5], ArrayList.class);
		studentExamQuestionsController.points = (new ObjectMapper().readValue(datas[7], ArrayList.class));
		studentExamQuestionsController.questIDs = new ObjectMapper().readValue(datas[8], ArrayList.class);
		studentExamPageController.stInfo = datas[9];
		studentExamPageController.teacherName = setTeacher;
		showData();
		if (studentInExam) {
			toQuestions.setVisible(true);
			errorTXT.setVisible(false);
			backBtn.setVisible(false);
			studentExamPageController.mintsExam = Integer.parseInt(duration) - 1;
			studentExamQuestionsController.studentInExam = true;
			examTimer myTimer = new examTimer();
			t = new Thread(myTimer);
			t.start();
		}
		studentExamQuestionsController.loadDiscriptions();
	}

	public String getTeachName() throws IOException {
		Instance.sendMessage(Command.getTechName.ordinal() + "@" + userString);
		return Instance.getClientConsole().getMessage().toString();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		done = false;
		if (studentInExam) {
			toQuestions.setVisible(true);
			errorTXT.setVisible(false);
			backBtn.setVisible(false);
		}
	}

	@FXML
	void goBack(ActionEvent event) {

	}

	public static void resetAll() {
		studName = "";
		studentIDString = "";
		userString = "";
		paString = "";
		duration = "";
		subName = "";
		cName = "";
		stInfo = "";
		teacherName = "";
		setTeacher = "";
		done = false;
		studentExamQuestionsController.resetAll();
	}

	@FXML
	void printT(ActionEvent event) {
	}

	public void back() {
		secondsExam = 60;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/StudentMainPage.fxml"));
		Scene scene;
		try {
			scene = new Scene(loader.load());
			Stage Window = startApp.stageM;

			Window.setTitle("Main page");
			Window.setScene(scene);
			Window.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class examTimer implements Runnable {
		int second;
		int mints;

		public examTimer() {
			this.second = secondsExam;
			this.mints = mintsExam;
		}

		@Override
		public void run() {
			while (true) {
				if (done) {
					break;
				}
				if (checks >= 4) {
					if (firstTime) {
						try {
							Instance.sendMessage(Command.checkExt.ordinal() + "@" + examCode);
							String respone = Instance.getClientConsole().getMessage().toString();
							if (!respone.equals("NoEx")) {
								try {
									int val = Integer.parseInt(respone);
									addition += val;
									mints += Integer.parseInt(respone);
									mintsExam = mints;
									firstTime = false;
									lastAddition = Integer.parseInt(respone);
								} catch (Exception e) {
									// TODO: handle exception
								}

							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						try {
							Instance.sendMessage(Command.checkExt.ordinal() + "@" + examCode);
							String respone = Instance.getClientConsole().getMessage().toString();
							if (!respone.equals("NoEx") && Integer.parseInt(respone) != lastAddition) {
								mints += Integer.parseInt(respone);
								addition += Integer.parseInt(respone);
								mintsExam = mints;
								lastAddition = Integer.parseInt(respone);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					checks = 0;
				} else {
					checks++;
				}
				second--;
				secondsExam = second;
				seconds.setText(Integer.toString(second));
				minuts.setText(Integer.toString(mints));

				if (second <= 0) {
					minTotal++;
					mints--;
					mintsExam = mints;
					minuts.setText(Integer.toString(mints));
					if (mints < 0) {
						seconds.setText("00");
						minuts.setText("00");
						toQuestions.setVisible(false);
						errorTXT.setVisible(true);
						break;
					}
					second = 60;

				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Platform.runLater(() ->

			{
				if (!done) {
					try {
						addExam(null);
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				back();

			});

		}

	}
}
