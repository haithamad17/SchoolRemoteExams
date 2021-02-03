package com.example.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "course")
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String Cnumber;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
	private List<Exam> exams;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
	private List<checkedExam> checked_Exams;
	@JsonIgnore
	@ManyToMany(
			cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			targetEntity = Student.class
		)
	@JoinTable(
		name="courses_students",
		joinColumns = @JoinColumn(name = "course_id"),
		inverseJoinColumns = @JoinColumn(name = "student_id")
			)
	private List<Student> students;
	@JsonIgnore
	@ManyToMany(
			cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			targetEntity = Teacher.class
		)
	@JoinTable(
		name="courses_teachers",
		joinColumns = @JoinColumn(name = "course_id"),
		inverseJoinColumns = @JoinColumn(name = "teacher_id")
			)
	private List<Teacher> teachers;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "subject_id")
	private Subject subject;

	public Course(String name, String cnumber,Subject subject) {
		this.name = name;
		Cnumber = cnumber;
		this.subject = subject;
		this.exams = new ArrayList<Exam>();
		this.checked_Exams = new ArrayList<checkedExam>();
		this.students = new ArrayList<Student>();
		this.teachers = new ArrayList<Teacher>();
	}

	public Course() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCnumber() {
		return Cnumber;
	}

	public void setCnumber(String cnumber) {
		Cnumber = cnumber;
	}

	public List<Exam> getExams() {
		return exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public List<checkedExam> getChecked_Exams() {
		return checked_Exams;
	}

	public void setChecked_Exams(List<checkedExam> checked_Exams) {
		this.checked_Exams = checked_Exams;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}


}