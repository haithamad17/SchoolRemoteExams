package com.example.operations;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.example.ServerClientEntities.commandRunner;
import com.example.entities.Principal;
import com.example.entities.Student;
import com.example.entities.Teacher;
import com.example.project.dataBase;

public class accOps {
	public static String logIn(String user, String paString, String tyString) {
		if (commandRunner.userStrings.contains(user))
			return "You already loged in";
		dataBase.getInstance();
		Session session = dataBase.getSession();
		System.out.println("from " + tyString + " where username = :username and password = :password");
		Query query = session.createQuery("from " + tyString + " where username = :username and password = :password");
		query.setParameter("username", user);
		query.setParameter("password", paString);
		List list = query.list();

		if (list.size() != 0) {
			commandRunner.userStrings.add(user);
			if (tyString.equals("Student")) {
				Student student = (Student) query.getSingleResult();

				session.close();
				if (student != null)
					return "Student@" + student.getUsername() + "@" + student.getPassword();
			}
			if (tyString.equals("Teacher")) {
				Teacher teacher = (Teacher) query.getSingleResult();
				session.close();
				if (teacher != null)
					return "Teacher@" + teacher.getUsername() + "@" + teacher.getPassword();
			}
			if (tyString.equals("Principal")) {
				Principal principal = (Principal) query.getSingleResult();
				session.close();
				if (principal != null)
					return "Principal@" + principal.getUsername() + "@" + principal.getPassword();
			}
			session.close();
			return "";
		}
		session.close();
		return "";
	}
	public static String logOut(String userName) {
		if(commandRunner.userStrings.contains(userName))
			commandRunner.userStrings.remove(userName);
		
		return "";
	}
}
