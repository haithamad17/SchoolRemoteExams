
package com.example.ServerClientEntities;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.Case;

import com.example.operations.ExamOps;
import com.example.operations.accOps;
import com.example.operations.generalOps;
import com.example.operations.princOps;
import com.example.operations.stuOps;
import com.example.operations.teacherOps;
import com.example.project.dataBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class commandRunner {
	public static List<String> userStrings = new ArrayList<>();

	public static String run(String command) throws SQLException, IOException {
		dataBase.getInstance();
		System.out.println(command);
		String[] commandArr = command.split("@");
		Command command_ = Command.values()[Integer.valueOf(commandArr[0])];

		switch (command_) {
		case accExist:
			return accOps.logIn(commandArr[1], commandArr[2], commandArr[4]);
		case logOut:
			return accOps.logOut(commandArr[1]);

		case teacherExams:
			return teacherOps.getExams(commandArr[1], commandArr[2]);

		case teacherQuestions:
			return teacherOps.getQuestions(commandArr[1], commandArr[2]);
		case teachQuesSubj:
			return teacherOps.getQuestionsSubject(commandArr[1], commandArr[2], commandArr[3]);
		case teacherSubjects:
			return teacherOps.getTeacherSubjects(commandArr[1], commandArr[2]);
		case teacherCourses:
			return teacherOps.getTeacherCourses(commandArr[1], commandArr[2]);
		case teacherToDo:
			return teacherOps.getToDo(commandArr[1], commandArr[2]);
		case teachAddToDo:
			return teacherOps.addToDo(commandArr[1], commandArr[2], commandArr[3]);
		case teachDellToDo:
			return teacherOps.DellToDo(commandArr[1], commandArr[2], commandArr[3]);
		case princToDo:
			return princOps.getToDo(commandArr[1], commandArr[2]);
		case princAddToDo:
			return princOps.addToDo(commandArr[1], commandArr[2], commandArr[3]);
		case princDellToDo:
			return princOps.DellToDo(commandArr[1], commandArr[2], commandArr[3]);
		case StuToDo:
			return stuOps.getToDo(commandArr[1], commandArr[2]);
		case StuAddToDo:
			return stuOps.addToDo(commandArr[1], commandArr[2], commandArr[3]);
		case StuDellToDo:
			return stuOps.DellToDo(commandArr[1], commandArr[2], commandArr[3]);
		case getAllExams:
			return ExamOps.getExamsList();
		case getSubjNumber:
			return teacherOps.getSubNumber(commandArr[1]);
		case getTeachers:
			return teacherOps.getTeachers();
		case isQuestExist:
			return teacherOps.questionExist(commandArr[1], commandArr[2]);
		// case isExamExist:

		case addQ:
			return teacherOps.addQuestion(commandArr[1], commandArr[2], commandArr[3], commandArr[4], commandArr[5],
					commandArr[6], commandArr[7]);
		case getQUESTIONS:
			return princOps.getQuestions();
		case getSUBJS:
			return princOps.getSubjects();
		case GETQUESSUBJ:
			return princOps.getQuestSubjs(commandArr[1]);
		case getQ:
			return generalOps.getQuestion(commandArr[1]);

		case TeacherExamsByUsrName:
			return teacherOps.getExamsByUsrName(commandArr[1]);
		case ExamsBySubject:
			return ExamOps.getExamsBySubject(commandArr[1]);
		case getSubjectName:
			return ExamOps.getSubjectName();

		case DELLQ:
			return generalOps.deleteQuestion(commandArr[1], commandArr[2]);
		case getCourseSubject:
			return generalOps.getSubjectByCourse(commandArr[1]);
		case AddExam:

			List<String> ll = new ObjectMapper().readValue(commandArr[6], ArrayList.class);
			List<String> ll2 = new ObjectMapper().readValue(commandArr[7], ArrayList.class);
			List<Double> ll3 = new ObjectMapper().readValue(commandArr[8], ArrayList.class);
			List<String> ll4 = new ObjectMapper().readValue(commandArr[9], ArrayList.class);
			return ExamOps.addExam(commandArr[1], commandArr[2], commandArr[3], commandArr[4], commandArr[5], ll, ll2,
					ll3, ll4, commandArr[10], commandArr[11]);
		case isExamExist:
			return ExamOps.examExist(commandArr[1], commandArr[2], commandArr[3]);
		case getExamById:
			if (commandArr.length == 3) {
				return ExamOps.getExamById(commandArr[1], "onhand");
			}
			return ExamOps.getExamById(commandArr[1], "");

		case getExamCode:
			return ExamOps.getExamCodetById(commandArr[1]);
		case isStudentExistById:
			return ExamOps.isStudentExistById(commandArr[1], commandArr[2], commandArr[3], commandArr[4]);
		case getExamCourseByCode:
			return ExamOps.getExamCourseByCode(commandArr[1]);
		case isStudentExistInCourse:
			return ExamOps.isStudentExistInCourse(commandArr[1], commandArr[2]);

		case getTechName:
			return generalOps.getTechName(commandArr[1]);
		case getExamIdBycode:
			return ExamOps.getExamIdBycode(commandArr[1], commandArr[2]);

		case setExamByExamNum:
			if (commandArr.length == 6) {
				return ExamOps.setExamByExamNum(commandArr[1], commandArr[2], commandArr[3], "onhand", commandArr[5]);
			}
			return ExamOps.setExamByExamNum(commandArr[1], commandArr[2], commandArr[3], "", commandArr[4]);
		case getNameByUsrName:
			return ExamOps.getIdByUsrName(commandArr[1]);
		case getTechIdByExCode:
			return ExamOps.getTechIdByExCode(commandArr[1]);
		case studentSubmmit:
			List<String> answers = new ObjectMapper().readValue(commandArr[2], ArrayList.class);
			return ExamOps.examSubmmited(commandArr[1], answers, commandArr[3], commandArr[4], commandArr[5]);

		case getAllStudents:
			return generalOps.getAllStudents();

		case getTeacherExamGenerated:
			return teacherOps.getTeacherExamGenerated(commandArr[1], commandArr[2]);
		case getCheckedExamById:
			return ExamOps.getCheckedExamById(commandArr[1]);
		case teachAPPROVE:
			return ExamOps.teachAPPROVE(commandArr[1], commandArr[2], commandArr[3]);
		case getALLChecked:
			return ExamOps.getALLChecked();
		case getStudentByIDNUM:
			return generalOps.getStudentByIDNUM(commandArr[1]);
		case getSTIDNum:
			return generalOps.getSTIDNum(commandArr[1], commandArr[2]);
		case studentChecked:
			return generalOps.studentChecked(commandArr[1]);
		case thisTeacherStudentChecked:
			return generalOps.thisTeacherStudentChecked(commandArr[1]);
		case submitHanedExam:
			return teacherOps.submitHanedExam(commandArr[1], commandArr[2], commandArr[3], commandArr[4],
					commandArr[5]);
		case getHandedExams:
			return teacherOps.getHandedExams(commandArr[1], commandArr[2]);
		case getHanedByID:
			return teacherOps.getHanedByID(commandArr[1]);

		case TeacherExamsByCourse:
			return teacherOps.TeacherExamsByCourse(commandArr[1], commandArr[2]);
		case EXTENDEX:
			return princOps.EXTENDEX(commandArr[1]);
		case GETREQ:
			return princOps.GETREQ();
		case DELLREQ:
			return princOps.DELLREQ(commandArr[1]);
		case APPROVEXT:
			return princOps.APPROVEXT(commandArr[1], commandArr[2]);
		case checkExt:
			return generalOps.checkExt(commandArr[1]);
		case ENDEXAM:
			return ExamOps.ENDEXAM(commandArr[1], commandArr[2],commandArr[3]);

		}

		return command;

	}

}
