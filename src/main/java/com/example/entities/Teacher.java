package com.example.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
//import com.example.entities.Course;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "teacher")
public class Teacher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String privelage;

	@JsonIgnore
	@ManyToMany()
	private List<Course> courses;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "teacher")
	private List<Exam> exams;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "teacher")
	private List<checkedExam> chExams;

	@JsonIgnore
	@ManyToMany()
	private List<Subject> subjects;

	@ElementCollection
	private List<String> todoList;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "teacher")
	private List<handedExam> handExams;

	private String username;
	private String password;

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

	public void setCourses(Course courses) {
		this.courses.add(courses);
	}

	public void addCourses(Course... courses_) {
		for (Course course : courses_) {
			this.courses.add(course);
			course.getTeachers().add(this);
		}
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public void setSubjects(Subject subject) {
		this.subjects.add(subject);
	}

	public void addSubjects(Subject... subjects) {
		for (Subject subject : subjects) {
			this.subjects.add(subject);
			subject.getTeachers().add(this);
		}
	}

	public List<Exam> getExams() {
		return exams;
	}

	public void setExams(List<Exam> exams) {

		this.exams = exams;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
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

	public Teacher(String privelage, String username, String password, String firstName, String lastName) {

		this.privelage = privelage;
		this.username = username;
		this.password = password;

		this.firstName = firstName;
		this.lastName = lastName;

		this.courses = new ArrayList<Course>();
		this.subjects = new ArrayList<Subject>();
		this.todoList = new ArrayList<String>();
		this.chExams = new ArrayList<checkedExam>();
		this.handExams = new ArrayList<handedExam>();
	}

	public List<checkedExam> getChExams() {
		return chExams;
	}

	public void setChExams(List<checkedExam> chExams) {
		this.chExams = chExams;
	}

	public List<handedExam> getHandExams() {
		return handExams;
	}

	public void setHandExams(List<handedExam> handExams) {
		this.handExams = handExams;
	}

	public Teacher() {

	}

}
