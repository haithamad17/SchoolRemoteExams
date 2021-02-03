package com.example.operations;

import com.example.entities.Exam;
import com.example.project.dataBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrincipalOps {
	
	public String getExamsList() throws JsonProcessingException{
		
		dataBase.getInstance();
		ObjectMapper mapper = new ObjectMapper();

		//String json = mapper.writeValueAsString(examsdisc);
		//System.out.println("JSON = " + json);
		dataBase.closeSess();
		//return json;
		return mapper.writeValueAsString(dataBase.getAll(Exam.class));
	}

}
