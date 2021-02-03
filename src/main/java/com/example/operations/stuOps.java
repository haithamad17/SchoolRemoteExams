package com.example.operations;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.example.entities.Principal;
import com.example.entities.Student;
import com.example.project.dataBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class stuOps {
	public static Student getStudent(String user, String paString) {
		dataBase.getInstance();
		Session session = dataBase.getSession();
		Query query = session.createQuery("from Student where username = :username and password = :password");
		query.setParameter("username", user);
		query.setParameter("password", paString);
		List list = query.list();

		if (list.size() != 0) {
			Student student = (Student) query.getSingleResult();
			return student;
		}
		return null;
	}

	public static String getToDo(String user, String paString) throws JsonProcessingException {
		Student student = getStudent(user, paString);
		List<String> items = student.getTodoList();

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(items);
		dataBase.closeSess();
		System.out.println("JSON = " + json);
		return json;
	}

	public static String addToDo(String user, String paString, String item) {
		Student student = getStudent(user, paString);
		Session session = dataBase.getSession();
		student.addTodoItem(item);
		session.update(student);
		session.getTransaction().commit();
		session.close();

		return "added";
	}

	public static String DellToDo(String user, String paString, String item) throws JsonProcessingException {
		Student student = getStudent(user, paString);
		Session session = dataBase.getSession();
		int i = 0;
		for (String it : student.getTodoList()) {
			if (it.equals(item)) {
				student.getTodoList().remove(i);
				break;
			}
			i++;
		}
		session.update(student);
		session.getTransaction().commit();
		session.close();

		return "removed";
	}
}
