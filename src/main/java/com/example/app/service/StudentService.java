package com.example.app.service;

import java.util.List;

import com.example.app.domain.Student;

public interface StudentService {

	List<Student> getStudentList() throws Exception;
	Student getStudentById(Integer id) throws Exception;
	Student getStudentByLoginId(String logingId) throws Exception;
	void deleteStudentById(Integer id) throws Exception;
	void addStudent(Student student) throws Exception;
	void editStudent(Student student) throws Exception;
	boolean isExsitingStudent(String loginId) throws Exception;
	List<Student> getStudentListPerPage(int page, int numPerPage) throws Exception;
    int getTotalPages(int numPerPage) throws Exception;

}
