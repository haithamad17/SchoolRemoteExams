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
import javax.persistence.Lob;
import javax.persistence.LockModeType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "handedexam")
public class handedExam {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String stID;
	private String examIn;
	private String duration;
	@Column(name = "DATA", unique = false, nullable = false, length = 100000)
	private String exJson;
	@ElementCollection
	private List<String> examLines;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStID() {
		return stID;
	}

	public void setStID(String stID) {
		this.stID = stID;
	}

	public String getExamIn() {
		return examIn;
	}

	public void setExamIn(String examIn) {
		this.examIn = examIn;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public List<String> getLines() {
		return examLines;
	}

	public void setLines(List<String> lines) {
		this.examLines = lines;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getExJson() {
		return exJson;
	}

	public void setExJson(String exJson) {
		this.exJson = exJson;
	}

	public List<String> getExamLines() {
		return examLines;
	}

	public void setExamLines(List<String> examLines) {
		this.examLines = examLines;
	}

	public handedExam(String stID, String examIn, String duration, Teacher teacher) {
		this.stID = "Student ID: " + stID;
		this.examIn = "Exam in: " + examIn;
		this.duration = "Duration: " + duration;
		this.teacher = teacher;
		this.examLines = new ArrayList<>();
	}

	public handedExam() {

	}

}
