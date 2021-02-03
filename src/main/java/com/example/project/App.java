package com.example.project;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.ServerClientEntities.commandRunner;
import com.example.controlers.examCreateController;
import com.example.controlers.studentExamPageController;
import com.example.entities.Answer;
import com.example.entities.Course;
import com.example.entities.Exam;
import com.example.entities.Principal;
import com.example.entities.Question;
import com.example.entities.Student;
import com.example.entities.Subject;
import com.example.entities.Teacher;
import com.example.entities.checkedExam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App {
	private static Session session;

	private static SessionFactory getSessionFactory() throws HibernateException {
		Configuration configuration = new Configuration();
// Add ALL of your entities here. You can also try adding a whole package.
		configuration.addAnnotatedClass(Answer.class);
		configuration.addAnnotatedClass(checkedExam.class);
		configuration.addAnnotatedClass(Course.class);
		configuration.addAnnotatedClass(Exam.class);
		configuration.addAnnotatedClass(Principal.class);
		configuration.addAnnotatedClass(Question.class);
		configuration.addAnnotatedClass(Student.class);
		configuration.addAnnotatedClass(Subject.class);
		configuration.addAnnotatedClass(Teacher.class);
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		return configuration.buildSessionFactory(serviceRegistry);
	}

	private static void initializeData() throws Exception {

		Subject math = new Subject("math", "00");
		session.save(math);

		Subject cs = new Subject("CS", "01");
		session.save(cs);
		Subject hebrew = new Subject("hebrew", "02");
		session.save(hebrew);
		Subject english = new Subject("english", "03");
		session.save(english);

		Course calculations = new Course("calculations", "00", math);
		session.save(calculations);

		Course probapilities = new Course("probapilities", "01", math);
		session.save(probapilities);
		Course ds = new Course("Data Structures", "02", cs);
		session.save(ds);
		Course intro = new Course("introduction to cs", "03", cs);
		session.save(intro);
		Course algo = new Course("algorithmes", "04", cs);
		session.save(algo);

		Course lashon = new Course("lashon", "05", hebrew);
		session.save(lashon);
		Course tanakh = new Course("tanakh", "06", hebrew);
		session.save(tanakh);

		Course grammar = new Course("grammar", "07", english);
		session.save(grammar);
		Course litreture = new Course("litreture", "08", english);
		session.save(litreture);

		Teacher malki = new Teacher("teacher", "malkigr", "123", "malki", "gr");
		malki.addSubjects(math, cs);
		malki.addCourses(calculations, intro);
		session.save(malki);

		Teacher oren = new Teacher("teacher", "orends", "123", "oren", "wieman");
		oren.addSubjects(math, cs);
		oren.addCourses(probapilities, ds);
		session.save(oren);

		Teacher noga = new Teacher("teacher", "nogaron", "123", "noga", "rontzve");
		noga.addSubjects(hebrew, cs);
		noga.addCourses(algo, tanakh);
		session.save(noga);

		Teacher liel = new Teacher("teacher", "lielf", "123", "liel", "fridman");
		liel.addSubjects(hebrew, english);
		liel.addCourses(tanakh, litreture);
		session.save(liel);

		Teacher mohammed = new Teacher("teacher", "aboseh", "123", "mohammed", "seh");
		mohammed.addSubjects(hebrew, english);
		mohammed.addCourses(lashon, grammar);
		session.save(mohammed);

		Student haitham = new Student("316379510", "student", "haitham17ad", "123", "haitham", "adudahesh");
		haitham.addCourses(ds, algo, lashon, tanakh);
		session.save(haitham);

		Student azmi = new Student("316022334", "student", "azmi_abu", "123", "azmi", "abuahmad");
		azmi.addCourses(ds, probapilities);
		session.save(azmi);

		Student ahmad = new Student("632510265", "student", "ahmadW", "123", "ahmad", "wahbi");
		ahmad.addCourses(grammar, litreture, tanakh);
		session.save(ahmad);

		Student rola = new Student("203174698", "student", "rolama", "123", "rola", "marie");
		rola.addCourses(intro, ds, calculations);
		session.save(rola);

		Student shadi = new Student("485126957", "student", "shadiCR7", "123", "shadi", "haloun");
		shadi.addCourses(probapilities, intro, ds, calculations);
		session.save(shadi);

		Principal ahmadM = new Principal("principal", "ahmedmassalha", "123", "ahmad", "massalha");
		session.save(ahmadM);

		session.getTransaction().commit();

	}

	public static <T> List<T> getAll(Class<T> object) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(object);
		Root<T> rootEntry = criteriaQuery.from(object);
		CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);

		TypedQuery<T> allQuery = session.createQuery(allCriteriaQuery);
		return allQuery.getResultList();
	}

	private static void printData() throws Exception {
		List<Exam> exams = getAll(Exam.class);

		List<Student> students = getAll(Student.class);
		// List<Course> courses = getAll(Course.class);
		for (Student student : students) {
			System.out.println("Student username: " + student.getUsername());
			System.out.println("Courses taken: ");
			// int i = 0;
			for (Course course : student.getCourses()) {
				System.out.println("" + course.getName());
			}
			System.out.println("");
		}
		System.out.println("");
		for (Exam exam : exams) {

			System.out.println("Exam writen by: " + exam.getTeacher().getUsername());
			System.out.println("time for exam: " + exam.getTimeString());
			System.out.println("\n\nQuestions");
			int i = 0;

			for (Question question : exam.getQuestions()) {
				// System.out.println("")
				System.out.print(i + ")" + question.getDiscription() + "\n");
				for (String answer : question.getAnswers()) {
					System.out.println("    " + answer);
				}
				i++;
			}
			System.out.format("\n\n");
			System.out.println("Done!");
			System.out.format("\n\n");

		}

	}

	public static void main(String[] args) throws Exception {

		dataBase.getInstance();
		session = dataBase.getSession();
		initializeData();
		printData();
		dataBase.closeSess();

		/*
		 * Course c1 = new Course("122","1212"); Subject s1 = new Subject("math", "00");
		 * Question question = new Question("1 + 1 = ?", "000", s1); Answer a1 = new
		 * Answer("1 + 1 = 2", false, question); Answer a2 = new Answer("1 + 1 = 3",
		 * false, question); Answer a3 = new Answer("1 + 1 = 4", false, question);
		 * Answer a4 = new Answer("1 + 1 = 5", true, question);
		 * 
		 * ObjectMapper mapper = new ObjectMapper(); List<Answer>
		 * l=question.getAnswers(); try { String json =
		 * mapper.writeValueAsString(question); System.out.println("JSON = " + json);
		 * Question question1 = new ObjectMapper().readValue(json, Question.class); }
		 * catch (JsonProcessingException e) { e.printStackTrace(); }
		 */
		/*
		 * dataBase.getInstance(); session = dataBase.getSession();
		 */
		/*
		 * Query query = session .createQuery("from " +
		 * "Teacher where username = :username" + " where password = :password");
		 * query.setParameter("username", "malki gr"); query.setParameter("password",
		 * "123"); List list = query.list(); System.out.println(list.size());
		 */
		/*
		 * Principal principal = new Principal("Principal", "Ahmad Massalha", "1234");
		 * session.save(principal); dataBase.closeSess();
		 */
		/*
		 * session = dataBase.getInstance().getSession(); initializeData(); printData();
		 * session.close();
		 */

		/*
		 * Query query = session.
		 * createQuery("from Teacher where username = :username and password = :password"
		 * ); query.setParameter("username", "sholy weinter");
		 * query.setParameter("password", "123"); List list = query.list(); if
		 * (list.size() != 0) { Teacher teacher = (Teacher) query.getSingleResult();
		 * List<Exam> l = teacher.getExams(); for (Exam exam : l) {
		 * System.out.println(exam.getTimeString()); } ObjectMapper mapper = new
		 * ObjectMapper();
		 * 
		 * String json = mapper.writeValueAsString(l); System.out.println("JSON = " +
		 * json); }
		 */
	}
}
