package com.example.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String anString;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "question_id")
	private Question question;
	private boolean is_right;

	public Answer(String anString, Question question, boolean is_right) {
		this.anString = anString;
		this.question = question;
		this.is_right = is_right;
	}

	public Answer(String anString, boolean is_right,Question q) {
		super();
		this.anString = anString;
		this.is_right = is_right;
	//	setQuestion(q);
	}

	public Answer() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAnString() {
		return anString;
	}

	public void setAnString(String anString) {
		this.anString = anString;
	}

	public Question getQuestion() {
		return question;
	}

/*	public void setQuestion(Question question) {
		this.question = question;
		this.question.getAnswers().add(this);
	}*/

	public boolean isIs_right() {
		return is_right;
	}

	public void setIs_right(boolean is_right) {
		this.is_right = is_right;
	}

}
