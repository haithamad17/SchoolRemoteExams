package com.example.operations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import org.hibernate.Query;
import org.hibernate.Session;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.ServerClientEntities.commandRunner;
import com.example.entities.Course;
import com.example.entities.Exam;
import com.example.entities.Question;
import com.example.entities.Student;
import com.example.entities.Subject;
import com.example.entities.Teacher;
import com.example.entities.checkedExam;
import com.example.project.dataBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.stage.FileChooser;

public class ExamOps {

	public static String getExamsList() throws JsonProcessingException {

		dataBase.getInstance();
		ObjectMapper mapper = new ObjectMapper();
		List<String> examsdisc = new ArrayList<String>();
		for (Exam exam : dataBase.getAll(Exam.class)) {

			String discString = "Exam id: " + exam.getId() + "\nExam in " + exam.getSubject().getName() + " writen by "
					+ exam.getTeacher().getUsername() + "\nDuration: " + exam.getTimeString() + " minutes\nCourse: "
					+ exam.getCourseName();
			examsdisc.add(discString);
		}
		dataBase.closeSess();
		return mapper.writeValueAsString(examsdisc);
	}

	public static String getSubjectName() throws JsonProcessingException {
		dataBase.getInstance();
		ObjectMapper mapper = new ObjectMapper();
		List<String> subjectsNames = new ArrayList<String>();
		subjectsNames.add("ALL");
		for (Subject t : dataBase.getAll(Subject.class)) {

			String discString = "" + t.getName();
			subjectsNames.add(discString);
		}
		System.out.println("Subjects: " + subjectsNames);
		dataBase.closeSess();
		return mapper.writeValueAsString(subjectsNames);
	}

	public static String getExamsBySubject(String subject) throws JsonProcessingException {

		System.out.println("Subject: " + subject);
		dataBase.getInstance();
		Session session = dataBase.getSession();
		Query query = session.createQuery("from Exam where subjectName = :subjectName");
		query.setParameter("subjectName", subject);
		List<Exam> exams = query.getResultList();
		List<String> examsdisc = new ArrayList<String>();
		List list = query.list();
		if (list.size() != 0) {
			System.out.println("Entered exam subject");
			for (Exam exam : exams) {

				String discString = "Exam id: " + exam.getId() + "\nExam in " + exam.getSubject().getName()
						+ " writen by " + exam.getTeacher().getUsername() + "\nDuration: " + exam.getTimeString()
						+ " minutes";
				examsdisc.add(discString);
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(examsdisc);
			System.out.println("JSON = " + json);
			session.close();
			return json;
		}

		session.close();
		return "";
	}

	public static String addExam(String teacherName, String teacherPass, String examDuration, String subName,
			String courseName, List<String> infoPerQStudent, List<String> infoPerQTeacher,
			List<Double> gradePerQuestion, List<String> questionsIds, String infoExamStudent, String infoExamTeacher)
			throws SQLException, IOException {
		dataBase.getInstance();
		Session session = dataBase.getSession();
		String examNum = Instance.getQN(2);
		while ((commandRunner.run(Command.isExamExist.ordinal() + "@" + examNum + "@" + subName + "@" + courseName))
				.equals("exist")) {
			examNum = Instance.getQN(2);
			System.out.println("Waiting for server");
		}

		List<Question> questionsList = new ArrayList<Question>();
		session = dataBase.getSession();
		for (String s : questionsIds) {
			System.out.println(s);

			Question q = session.get(Question.class, Integer.valueOf(s));
			questionsList.add(q);

		}
		Teacher teacher = teacherOps.getTeacher(teacherName, teacherPass);
		Subject subject = generalOps.getSubjectByName(subName);
		Course course = generalOps.getCourseByName(courseName);
		Exam exam = new Exam(teacher, subject, questionsList, examDuration, course);

		exam.setExamNumber(examNum);
		exam.setQuestions(questionsList);
		exam.setCourse(course);
		exam.setTeacher(teacher);
		exam.setSubject(subject);
		exam.setGradesPerQuestion(gradePerQuestion);
		exam.setStudentExamComments(infoExamStudent);
		exam.setStudentInfoPerQuestion(infoPerQStudent);
		exam.setTeacherExamComments(infoExamTeacher);
		exam.setTeacherInfoPerQuestion(infoPerQTeacher);
		session.save(exam);
		session.getTransaction().commit();
		session.close();
		dataBase.closeSess();
		return "";
	}

	public static String examExist(String examNum, String sName, String cName) {
		dataBase.getInstance();
		Session session = dataBase.getSession();
		Query query2 = session.createQuery("from Subject where subject_name = :subject_name");
		query2.setParameter("subject_name", sName);
		Subject s = (Subject) query2.getSingleResult();
		int subId = s.getId();
		Query query3 = session.createQuery("from Course where name = :name");
		query3.setParameter("name", cName);
		Course c = (Course) query3.getSingleResult();
		int courseId = c.getId();
		Query query = session.createQuery("from Exam where exam_num = :exam_num and subject_name = :subject_name "
				+ "and course_id = :course_id");
		query.setParameter("exam_num", examNum);
		query.setParameter("subject_name", sName);
		query.setParameter("course_id", courseId);
		List list = query.list();

		if (list.size() != 0) {
			dataBase.closeSess();
			return "exist";
		}
		dataBase.closeSess();
		return "good";
	}

	public static String getWholeExam(String examNum, int id) throws IOException {

		dataBase.getInstance();
		Session session = dataBase.getSession();
		List<String> examsdisc = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		Query query = session.createQuery("from Exam where exam_num = :exam_num and id = :id");
		query.setParameter("exam_num", examNum);
		query.setParameter("id", id);
		Exam exam = (Exam) query.getSingleResult();
		String StudentInfoPerQuestion = mapper.writeValueAsString(exam.getStudentInfoPerQuestion());
		String TeachertInfoPerQuestion = mapper.writeValueAsString(exam.getTeacherInfoPerQuestion());
		String GradesPerQuestion = mapper.writeValueAsString(exam.getGradesPerQuestion());
		List<String> questionsId = new ArrayList<String>();
		for (Question q : exam.getQuestions()) {

			questionsId.add(String.valueOf(q.getId()));
		}
		String QuestionIds = mapper.writeValueAsString(questionsId);

		String examString = "" + exam.getTeacher().getUsername() + "@" + exam.getTeacher().getPassword() + "@"
				+ exam.getTimeString() + "@" + exam.getSubjectName() + "@" + exam.getCourseName() + "@"
				+ StudentInfoPerQuestion + "@" + TeachertInfoPerQuestion + "@" + GradesPerQuestion + "@" + QuestionIds
				+ "@" + exam.getStudentExamComments() + "@" + exam.getTeacherExamComments() + "@"
				+ exam.getTeacherGeneratedExam();

		return examString + "@" + examToWord(examNum, id);

	}

	public static String getWholeCheckedExam(int i) throws JsonProcessingException {

		System.out.println(i);
		dataBase.getInstance();
		Session session = dataBase.getSession();
		List<String> examsdisc = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		Query query = session.createQuery("from checkedExam where id = :id");
		query.setParameter("id", i);
		checkedExam exam = (checkedExam) query.getSingleResult();
		String StudentInfoPerQuestion = mapper.writeValueAsString(exam.getTeacherInfoPerQuestion());
		// String TeachertInfoPerQuestion =
		// mapper.writeValueAsString(exam.getTeacherInfoPerQuestion());
		String Grade = mapper.writeValueAsString(exam.getGrade());
		List<String> questionsId = new ArrayList<String>();
		for (Question q : exam.getQuestions()) {

			questionsId.add(String.valueOf(q.getId()));
		}
		String QuestionIds = mapper.writeValueAsString(questionsId);
		String studentAnswers = mapper.writeValueAsString(exam.getStudentAnswers());
		List<String> rightAnswers = new ArrayList<>();
		List<String> discriptions = new ArrayList<>();
		List<List<String>> answersList = new ArrayList<>();
		for (Question question : exam.getQuestions()) {
			List<String> ans = new ArrayList<>();
			for (String an : question.getAnswers())
				ans.add(an);
			answersList.add(ans);
			rightAnswers.add(question.getRightAnswer());
			discriptions.add(question.getDiscription());
		}

		String examString = "" + exam.getTeacher().getUsername() + "@" + exam.getStudent().getFirstName() + " "
				+ exam.getStudent().getLastName() + "@" + exam.getTimeString() + "@" + StudentInfoPerQuestion + "@"
				+ Grade + "@" + QuestionIds + "@" + exam.getTeacherExp() + "@" + studentAnswers + "@"
				+ mapper.writeValueAsString(rightAnswers) + "@" + mapper.writeValueAsString(discriptions) + "@"
				+ mapper.writeValueAsString(exam.getTeacherInfoPerQuestion()) + "@"
				+ mapper.writeValueAsString(exam.getQuestionsGrades()) + "@" + mapper.writeValueAsString(answersList)
				+ "@" + exam.getId();

		System.out.println(examString);
		return examString;

	}

	public static String getExamCodetById(String id) {

		dataBase.getInstance();
		Session session = dataBase.getSession();
		Query query = session.createQuery("from Exam where id = :id");
		System.out.println("Exam number: " + id);
		int i = Integer.parseInt(id);
		query.setParameter("id", i);
		Exam exam = (Exam) query.getSingleResult();
		Query query2 = session.createQuery("from Course where id = :id");
		String courseId = Integer.toString(exam.getCourse().getId());
		query2.setParameter("id", exam.getCourse().getId());
		Course course = (Course) query2.getSingleResult();
		session.close();
		return "" + course.getCnumber() + "" + exam.getExamNumber();
	}

	public static String getExamById(String id, String isOnHand) throws IOException {
		dataBase.getInstance();
		Session session = dataBase.getSession();
		Exam q = session.get(Exam.class, Integer.valueOf(id));
		if (q != null) {
			String args = getWholeExam(q.getExamNumber(), Integer.valueOf(id));
			if (!isOnHand.equals("")) {
				String ret = examToWord(q.getExamNumber(), Integer.valueOf(id)) + "@" + args;
				session.close();
				return ret;
			}
			session.close();
			return args;
		}
		session.close();
		return "";
	}

	public static String isStudentExistById(String id, String usrName, String password, String courseCode) {

		dataBase.getInstance();
		Session session = dataBase.getSession();
		Query query = session.createQuery(
				"from Student where idNum = :idNum and " + "username = :username and password = :password");
		query.setParameter("idNum", id);
		query.setParameter("username", usrName);
		query.setParameter("password", password);
		List list = query.list();
		if (list.size() != 0) {
			Student student = (Student) query.getSingleResult();
			Query query2 = session.createQuery("from Exam where exam_code = :exam_code");
			query2.setParameter("exam_code", courseCode);
			if (query2.list().size() == 0) {
				session.close();
				return "";
			}

			int nameOFCourse = ((Exam) query2.getSingleResult()).getId();
			System.out.println(student.getGrades().size());
			for (checkedExam exam : student.getGrades()) {

				if (exam.getCourse() != null && exam.getExamId() == nameOFCourse) {
					session.close();
					return "doneIt";
				}

			}
			for (String exam : student.getHandedExamsIds()) {
				System.out.println(exam + "      " + nameOFCourse);
				if (Integer.parseInt(exam) == nameOFCourse) {
					session.close();
					return "doneIt";
				}

			}
			dataBase.closeSess();
			return "exist";
		}
		return "";
	}

	public static String getExamCourseByCode(String examCode) {

		dataBase.getInstance();
		Session session = dataBase.getSession();
		Query query = session.createQuery("from Exam where exam_code = :exam_code");
		query.setParameter("exam_code", examCode);
		List list = query.list();
		if (list.size() != 0) {
			Exam exam = (Exam) query.getSingleResult();
			String course = exam.getCourseName();
			return course;
		}
		return "";
	}

	public static String isStudentExistInCourse(String id, String courseName) {

		dataBase.getInstance();
		Session session = dataBase.getSession();
		Query query = session.createQuery("from Student where idNum = :idNum");
		query.setParameter("idNum", id);
		Query query2 = session.createQuery("from Course where name = :name");
		query2.setParameter("name", courseName);
		List list = query.list();
		if (list.size() != 0) {
			Student student = (Student) query.getSingleResult();
			// List<Student> students = course.getStudents();
			Course course = (Course) query2.getSingleResult();
			if (course.getStudents().contains(student)) {
				session.close();
				dataBase.closeSess();
				return "exist";
			}

		}
		session.close();
		dataBase.closeSess();
		return "";
	}

	public static String getExamIdBycode(String code, String type) {
		dataBase.getInstance();
		Session session = dataBase.getSession();
		System.out.println("searching id for code: " + code);
		Query query = session.createQuery("from Exam where exam_code = :exam_code");
		query.setParameter("exam_code", code);
		List list = query.list();
		if (list.size() != 0) {
			Exam exam = (Exam) query.getSingleResult();
			if (type.equals("onhand") && !exam.isOnHand()) {
				session.close();
				return "exam not available";
			}
			if (type.equals("onapp") && !exam.isOnAPP()) {
				session.close();
				return "exam not available";
			}
			String id = Integer.toString(exam.getId());
			System.out.println("id is: " + id);
			session.close();
			return id;
		}
		session.close();
		return "";
	}

	public static String setExamByExamNum(String examNum, String examCode, String teacherName, String onHand,
			String course) {
		dataBase.getInstance();

		Session session = dataBase.getSession();
		System.out.println("Exam num = " + examNum);
		Query query = session.createQuery("from Exam where exam_num = :exam_num and course_name = :course_name");
		query.setParameter("exam_num", examNum);
		query.setParameter("course_name", course);
		List list = query.list();

		if (list.size() != 0) {
			Query query2 = session.createQuery("from Teacher where username = :username");
			query2.setParameter("username", teacherName);
			Exam exam = (Exam) query.getSingleResult();
			Teacher teacher = (Teacher) query2.getSingleResult();
			exam.setExamCode(examCode);
			exam.setTeacherGeneratedExam(Integer.toString(teacher.getId()));
			if (!onHand.equals("")) {
				exam.setOnHand(true);
			} else {
				exam.setOnAPP(true);
			}
			session.update(exam);
			session.getTransaction().commit();
			session.close();
			System.out.println("Exam code = " + exam.getExamCode());
			System.out.println("Teacher: " + exam.getTeacherGeneratedExam());

		}

		return "";
	}

	public static String getIdByUsrName(String usrName) {

		dataBase.getInstance();

		Session session = dataBase.getSession();
		Query query = session.createQuery("from Student where username = :username");
		query.setParameter("username", usrName);
		List list = query.list();
		if (list.size() != 0) {
			Student student = (Student) query.getSingleResult();
			String toRetString = "" + student.getFirstName() + " " + student.getLastName();
			session.close();
			return toRetString;

		}
		session.close();
		return "";
	}

	public static String getTechIdByExCode(String string) {
		dataBase.getInstance();

		Session session = dataBase.getSession();
		Query query = session.createQuery("from Exam where exam_code = :exam_code");
		query.setParameter("exam_code", string);
		List list = query.list();
		if (list.size() != 0) {
			Exam exam = (Exam) query.getSingleResult();
			String toRetString = exam.getTeacherGeneratedExam();
			session.close();
			return toRetString;

		}
		session.close();
		return "";
	}

	public static String evaluateExam(checkedExam examToCheck, Exam exam) {
		Session session = dataBase.getSession();
		List<String> studentAnswers = examToCheck.getStudentAnswers();
		List<Question> questions = examToCheck.getQuestions();
		List<Double> grades = exam.getGradesPerQuestion();
		double total = 0;
		double res = 0;
		for (int i = 0; i < studentAnswers.size(); i++) {
			if (studentAnswers.get(i).equals(questions.get(i).getRightAnswer()))
				res += grades.get(i);
			total += grades.get(i);
		}
		examToCheck.setTeacherExp(exam.getStudentExamComments());
		examToCheck.setGrade((res / total) * 100);
		session.save(examToCheck);
		session.getTransaction().commit();
		session.close();
		return "";
	}

	public static String examSubmmited(String studentId, List<String> answersList, String examCode, String teacherId,
			String time2) {

		dataBase.getInstance();
		Session session = dataBase.getSession();
		Query query = session.createQuery("from Student where idNum = :idNum");
		query.setParameter("idNum", studentId);
		Student student = (Student) query.getSingleResult();
		Query query2 = session.createQuery("from Exam where exam_code = :exam_code");
		query2.setParameter("exam_code", examCode);
		Exam exam = (Exam) query2.getSingleResult();
		Query query3 = session.createQuery("from Teacher where id = :id");
		query3.setParameter("id", Integer.valueOf(teacherId));
		Teacher teacher = (Teacher) query3.getSingleResult();
		checkedExam chExam = new checkedExam(teacher, exam.getSubject(), null, exam.getTimeString() + " " + time2,
				student, 0.0, "");
		chExam.setStudentAnswers(answersList);
		chExam.setStudent(student);
		chExam.setTeacher(teacher);
		chExam.setQuestions(new ArrayList<Question>());
		chExam.setExamId(exam.getId());
		for (Question q : exam.getQuestions()) {
			chExam.getQuestions().add(q);
			q.getCheckedExams().add(chExam);
		}
		chExam.setTeacherInfoPerQuestion(new ArrayList<>());
		for (String s : exam.getStudentInfoPerQuestion())
			chExam.getTeacherInfoPerQuestion().add(s);

		chExam.setQuestionsGrades(new ArrayList<>());
		for (double s : exam.getGradesPerQuestion())
			chExam.getQuestionsGrades().add(s);

		teacher.getChExams().add(chExam);
		session.update(teacher);
		chExam.setCourse(session.get(Course.class, exam.getCourse().getId()));
		chExam.setTeacherExamComments(exam.getStudentExamComments());
		ExamOps.evaluateExam(chExam, exam);
		return "";
	}

	public static String getCheckedExamById(String id) throws JsonProcessingException {

		dataBase.getInstance();
		Session session = dataBase.getSession();
		checkedExam q = session.get(checkedExam.class, Integer.valueOf(id));
		if (q != null) {
			String args = getWholeCheckedExam(q.getId());
			session.close();
			return args;
		}
		session.close();
		return "";

	}

	public static String teachAPPROVE(String idString, String newGrade, String teacherExplain) {
		// TODO Auto-generated method stub
		dataBase.getInstance();
		Session session = dataBase.getSession();
		checkedExam exam = session.get(checkedExam.class, Integer.valueOf(idString));
		exam.setGrade(Double.valueOf(newGrade));
		exam.setTeacherExp(teacherExplain);
		exam.setChecked(true);
		session.save(exam);
		session.getTransaction().commit();
		session.close();
		return "";
	}

	public static String getALLChecked() throws JsonProcessingException {
		dataBase.getInstance();

		List<checkedExam> exams = dataBase.getAll(checkedExam.class);
		List<String> examsdisc = new ArrayList<>();
		for (checkedExam exam : exams) {
			String discString = "Exam id: " + exam.getId() + "\nName: " + exam.getStudent().getFirstName() + " "
					+ exam.getStudent().getLastName() + "\nGrade: " + exam.getGrade() + "\nDuration: "
					+ exam.getTimeString() + " minutes";
			if (exam.isChecked())
				examsdisc.add(discString);
		}
		dataBase.closeSess();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(examsdisc);
		System.out.println(json);
		return json;
	}

	public static String examToWord(String examNum, int id) throws IOException {

		dataBase.getInstance();
		Session session = dataBase.getSession();
		List<String> examsdisc = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		Query query = session.createQuery("from Exam where exam_num = :exam_num and id = :id");
		query.setParameter("exam_num", examNum);
		query.setParameter("id", id);
		Exam exam = (Exam) query.getSingleResult();
		String examString = "";
		List<Question> questions = exam.getQuestions();
		List<String> infoperQ = exam.getStudentInfoPerQuestion();
		List<Double> points = exam.getGradesPerQuestion();
		examString += "\t\t\t\tCourse: " + exam.getCourse().getName() + "\n";
		examString += "\t\t\t\tDuration: " + exam.getTimeString() + "\n\n\n";
		for (int i = 0; i < questions.size(); i++) {
			examString += "\nQuestion: " + String.valueOf(i + 1) + "\t-----------  PTS: " + points.get(i) + "\n";
			examString += "\tInstructions: " + infoperQ.get(i) + "\n";
			examString += questions.get(i).getDiscription() + "\n";
			examString += "\tAnswers:\n";
			for (String string : questions.get(i).getAnswers()) {
				examString += "\t\t-) " + string + "\n";
			}
			examString += "Write you're answer here: _____________________________________________\n\n";
		}
		return examString;

	}

	public static String ENDEXAM(String examNum, String examCode, String course) {
		dataBase.getInstance();

		Session session = dataBase.getSession();
		System.out.println("Exam num = " + examNum);
		Query query = session.createQuery(
				"from Exam where exam_num = :exam_num and course_name = :course_name and exam_code = :exam_code");
		query.setParameter("exam_num", examNum);
		query.setParameter("course_name", course);
		query.setParameter("exam_code", examCode);
		List list = query.list();
		if (list.size() != 0) {
			Exam exam = (Exam) query.getSingleResult();
			exam.setExamCode(null);
			exam.setExamExt(null);
			session.update(exam);
			session.getTransaction().commit();
			session.close();
			return "closed";
		}
		session.close();
		return "Exam not started, if you wish, you can start it";
	}
}
