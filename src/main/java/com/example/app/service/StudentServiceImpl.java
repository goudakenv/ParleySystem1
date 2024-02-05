package com.example.app.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Student;
import com.example.app.mapper.StudentMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final StudentMapper studentMapper;

	@Override
	public List<Student> getStudentList() throws Exception {
		return studentMapper.selectAll();
	}

	@Override
	public Student getStudentById(Integer id) throws Exception {
		return studentMapper.selectById(id);
	}
	
	@Override
	public Student getStudentByLoginId(String logingId) throws Exception {
		return studentMapper.selectByLoginId(logingId);
	}

	@Override
	public void deleteStudentById(Integer id) throws Exception {
		studentMapper.setDeleteById(id);
	}

	@Override
	public void addStudent(Student student) throws Exception {
		String password = student.getLoginPass();
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		student.setLoginPass(hashedPassword);
		studentMapper.insert(student);
	}

	@Override
	public void editStudent(Student student) throws Exception {
		String password = student.getLoginPass();
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		student.setLoginPass(hashedPassword);
		studentMapper.update(student);
	}

	@Override
	public boolean isExsitingStudent(String loginId) throws Exception {
		Student student = studentMapper.selectByLoginId(loginId);
		if(student != null) {
			return true;
		}

		return false;
	}

	@Override
	public List<Student> getStudentListPerPage(int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		return studentMapper.selectLimited(offset, numPerPage);
	}

	@Override
	public int getTotalPages(int numPerPage) throws Exception {
		long count = studentMapper.countActive();
		return (int) Math.ceil((double) count / numPerPage);
	}

}
