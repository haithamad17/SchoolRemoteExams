
package com.example.ServerClientEntities;

public enum Command {

	accExist, teacherExams, teacherQuestions, teacherSubjects, teacherCourses, teachQuesSubj, teacherToDo, teachAddToDo,
	teachDellToDo, princToDo, princAddToDo, princDellToDo, StuToDo, StuAddToDo, StuDellToDo, getAllExams, getSubjNumber,
	getTeachers, isQuestExist, addQ, getSUBJS, getQUESTIONS, GETQUESSUBJ, getQ, DELLQ, getCourseSubject,
	TeacherExamsByUsrName, ExamsBySubject, getSubjectName, isExamExist, AddExam, getExamById, getTechName, getExamCode,
	isStudentExistById, getExamCourseByCode, isStudentExistInCourse, getExamIdBycode, setExamByExamNum,getNameByUsrName,
  getTechIdByExCode, studentSubmmit,getAllStudents, getTeacherExamGenerated, getCheckedExamById,teachAPPROVE,getALLChecked, getStudentByIDNUM,
	getSTIDNum, studentChecked, thisTeacherStudentChecked, submitHanedExam, getHandedExams, getHanedByID, logOut,
	EXTENDEX, GETREQ, DELLREQ, APPROVEXT, checkExt, ENDEXAM,TeacherExamsByCourse



}
