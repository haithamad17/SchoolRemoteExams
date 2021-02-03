package com.example.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "checkedexam")
public class checkedExam {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "subject_id")
	private Subject subject;
	@JsonIgnore
	@ManyToMany()
	private List<Question> questions;
	private String teacherExamComments;
	private String timeString;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "student_id")

	private Student student;
	@ElementCollection
	List<String> studentAnswers;

	@ElementCollection
	List<String> teacherInfoPerQuestion;

	@ElementCollection
	List<Double> questionsGrades;
	private double grade;

	private String discreption;

	private boolean isChecked;

	private int examId;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "course_id")
	private Course course;
	@JoinColumn(name = "teacherCommentChange")
	private String teacherExp;

	public checkedExam(Teacher teacher, Subject subject, List<Question> questions, String timeString, Student student,
			double grade, String discreption) {
		this.teacher = teacher;
		this.subject = subject;
		this.questions = questions;
		this.timeString = timeString;
		this.student = student;
		this.grade = grade;
		this.discreption = discreption;
		isChecked = false;
	}

	public checkedExam() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public String getDiscreption() {
		return discreption;
	}

	public void setDiscreption(String discreption) {
		this.discreption = discreption;
	}

	public void addQuestions(Question... questionslst) {
		for (Question ga : questionslst) {
			questions.add(ga);
			ga.getCheckedExams().add(this);
		}
	}

	public List<String> getStudentAnswers() {
		return studentAnswers;
	}

	public void setStudentAnswers(List<String> studentAnswers) {
		this.studentAnswers = studentAnswers;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<String> getTeacherInfoPerQuestion() {
		return teacherInfoPerQuestion;
	}

	public void setTeacherInfoPerQuestion(List<String> teacherInfoPerQuestion) {
		this.teacherInfoPerQuestion = teacherInfoPerQuestion;
	}

	public String getTeacherExamComments() {
		return teacherExamComments;
	}

	public void setTeacherExamComments(String teacherExamComments) {
		this.teacherExamComments = teacherExamComments;
	}

	public List<Double> getQuestionsGrades() {
		return questionsGrades;
	}

	public void setQuestionsGrades(List<Double> questionsGrades) {
		this.questionsGrades = questionsGrades;
	}

	public String getTeacherExp() {
		return teacherExp;
	}

	public void setTeacherExp(String teacherExp) {
		this.teacherExp = teacherExp;
	}

	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

}
