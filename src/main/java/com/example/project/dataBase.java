package com.example.project;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.example.entities.Answer;
import com.example.entities.Course;
import com.example.entities.Exam;
import com.example.entities.Principal;
import com.example.entities.Question;
import com.example.entities.Student;
import com.example.entities.Subject;
import com.example.entities.Teacher;
import com.example.entities.checkedExam;
import com.example.entities.handedExam;
import com.example.entities.todoItem;

public class dataBase {
	private static dataBase instance = null;
	private static Session session = null;
	private static SessionFactory sessionFactory = null;

	private static SessionFactory getSessionFactory() throws HibernateException {
		Configuration configuration = new Configuration();
		// Add ALL of your entities here. You can also try adding a whole package.
		// configuration.addAnnotatedClass(Answer.class);
		configuration.addAnnotatedClass(checkedExam.class);
		configuration.addAnnotatedClass(Course.class);
		configuration.addAnnotatedClass(Exam.class);
		configuration.addAnnotatedClass(Principal.class);
		configuration.addAnnotatedClass(Question.class);
		configuration.addAnnotatedClass(Student.class);
		configuration.addAnnotatedClass(Subject.class);
		configuration.addAnnotatedClass(Teacher.class);
		configuration.addAnnotatedClass(handedExam.class);
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		return configuration.buildSessionFactory(serviceRegistry);
	}

	private dataBase() {
		Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		sessionFactory = getSessionFactory();
	}

//get all entities from class
	public static <T> List<T> getAll(Class<T> object) {
		getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(object);
		Root<T> rootEntry = criteriaQuery.from(object);
		CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);

		TypedQuery<T> allQuery = session.createQuery(allCriteriaQuery);
		return allQuery.getResultList();
	}

	public static dataBase getInstance() {
		if (instance == null) {
			instance = new dataBase();
		}
		return instance;
	}

	public static Session getSession() {
		if (session != null && session.isOpen())
			return session;
		session = sessionFactory.openSession();
		session.beginTransaction();
		return session;
	}

	public static void closeSess() {
		if (session != null && session.isOpen())
			session.close();
	}

}