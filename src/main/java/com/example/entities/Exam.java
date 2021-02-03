package com.example.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "exam")
public class Exam {
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

	private String timeString;
	@JoinColumn(name = "onHand")
	private boolean onHand;
	@JoinColumn(name = "onAPP")
	private boolean onAPP;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "course_id")
	private Course course;

	private String studentExamComments;

	private String teacherExamComments;
	@ElementCollection
	private List<String> teacherInfoPerQuestion;
	@ElementCollection
	private List<String> studentInfoPerQuestion;
	@ElementCollection
	private List<Double> gradesPerQuestion;
	// private String teacherName;
	@Column(name = "subject_name")
	private String subjectName;

	@Column(name = "course_name")
	private String courseName;

	@Column(name = "exam_num")
	private String examNumber;

	@Column(name = "exam_code")
	private String examCode;

	@Column(name = "teacher_exam_generator")
	private String teacherGeneratedExam;

	@Column(name = "exam_extension")
	private String examExt;

	public Exam(Teacher teacher, Subject subject, List<Question> questions, String timeString, Course course_) {
		this.teacher = teacher;
		this.subject = subject;
		this.questions = questions;
		this.timeString = timeString;
		setCourse(course_);
		// setTeacherName(teacher.getUsername());
		setSubjectName(subject.getName());
		setCourseName(course.getName());
		this.gradesPerQuestion = new ArrayList<Double>();
		this.studentInfoPerQuestion = new ArrayList<String>();
		this.teacherInfoPerQuestion = new ArrayList<String>();
		// this.examNumber = examNumber;

	}

	public String getTeacherGeneratedExam() {
		return teacherGeneratedExam;
	}

	public void setTeacherGeneratedExam(String teacherGeneratedExam) {
		this.teacherGeneratedExam = teacherGeneratedExam;
	}

	public String getExamCode() {
		return examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public String getExamNumber() {
		return examNumber;
	}

	public void setExamNumber(String examNumber) {
		this.examNumber = examNumber;
	}

	public String getStudentExamComments() {
		return studentExamComments;
	}

	public void setStudentExamComments(String studentExamComments) {
		this.studentExamComments = studentExamComments;
	}

	public String getTeacherExamComments() {
		return teacherExamComments;
	}

	public void setTeacherExamComments(String teacherExamComments) {
		this.teacherExamComments = teacherExamComments;
	}

	public List<String> getTeacherInfoPerQuestion() {
		return teacherInfoPerQuestion;
	}

	public void setTeacherInfoPerQuestion(List<String> teacherInfoPerQuestion) {
		this.teacherInfoPerQuestion = teacherInfoPerQuestion;
	}

	public void addTeacherInfoPerQuestion(String teacherInfoPerQuestion) {
		this.teacherInfoPerQuestion.add(teacherInfoPerQuestion);
	}

	public List<String> getStudentInfoPerQuestion() {
		return studentInfoPerQuestion;
	}

	public void setStudentInfoPerQuestion(List<String> studentInfoPerQuestion) {
		this.studentInfoPerQuestion = studentInfoPerQuestion;
	}

	public void addStudentInfoPerQuestion(String studentInfoPerQuestion) {
		this.studentInfoPerQuestion.add(studentInfoPerQuestion);
	}

	public List<Double> getGradesPerQuestion() {
		return gradesPerQuestion;
	}

	public void setGradesPerQuestion(List<Double> gradesPerQuestion) {
		this.gradesPerQuestion = gradesPerQuestion;
	}

	public void addGradesPerQuestion(Double gradesPerQuestion) {
		this.gradesPerQuestion.add(gradesPerQuestion);
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Exam() {

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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
		this.course.getExams().add(this);
	}

	public void addQuestions(Question... questionslst) {
		for (Question ga : questionslst) {
			questions.add(ga);
			ga.getExams().add(this);
		}
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subject.getName();
	}

	public boolean isOnHand() {
		return onHand;
	}

	public void setOnHand(boolean onHand) {
		this.onHand = onHand;
	}

	public boolean isOnAPP() {
		return onAPP;
	}

	public void setOnAPP(boolean onAPP) {
		this.onAPP = onAPP;
	}

	public String getExamExt() {
		return examExt;
	}

	public void setExamExt(String examExt) {
		this.examExt = examExt;
	}

}
