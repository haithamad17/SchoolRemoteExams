package com.example.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "student")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String idNum;
	private String privelage;
	@JsonIgnore
	@ManyToMany()
	private List<Course> courses;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
	private List<checkedExam> grades;
	private String username;
	private String password;
	@ElementCollection
	private List<String> todoList;

	@ElementCollection
	private List<String> handedExamsIds;

	private String firstName;
	private String lastName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrivelage() {
		return privelage;
	}

	public void setPrivelage(String privelage) {
		this.privelage = privelage;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public List<checkedExam> getGrades() {
		return grades;
	}

	public void setGrades(List<checkedExam> grades) {
		this.grades = grades;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public List<String> getTodoList() {
		return todoList;
	}

	public void setTodoList(List<String> todoList) {
		this.todoList = todoList;
	}

	public void addTodoItem(String item) {
		todoList.add(item);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void addCourses(Course... courses_) {
		for (Course course : courses_) {
			this.courses.add(course);
			course.getStudents().add(this);
		}
	}

	public List<String> getHandedExamsIds() {
		return handedExamsIds;
	}

	public void setHandedExamsIds(List<String> handedExamsIds) {
		this.handedExamsIds = handedExamsIds;
	}

	public Student(String idNum, String privelage, String username, String password, String firstName,
			String lastName) {
		super();
		// this.id = id;
		this.idNum = idNum;
		this.privelage = privelage;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.todoList = new ArrayList<String>();
		this.courses = new ArrayList<Course>();
		this.handedExamsIds = new ArrayList<String>();
	}

	public Student() {

	}

}
