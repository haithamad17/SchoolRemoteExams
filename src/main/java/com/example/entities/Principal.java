package com.example.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Principal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String privelage;

	private String username;
	private String password;
	
	private String firstName;
	private String lastName;
	@ElementCollection
	private List<String> todoList;
	
	@ElementCollection
	private List<String> examExtends;

	public Principal(String privelage, String username, String password,String firstName, String lastName) {
		this.privelage = privelage;
		this.username = username;
		this.todoList = new ArrayList<String>();
		this.examExtends=new ArrayList<String>();
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getTodoList() {
		return todoList;
	}

	public void setTodoList(List<String> todoList) {
		this.todoList = todoList;
	}

	public List<String> getExamExtends() {
		return examExtends;
	}

	public void setExamExtends(List<String> examExtends) {
		this.examExtends = examExtends;
	}

	public void addTodoItem(String item) {
		todoList.add(item);
	}

	public Principal() {

	}

}
