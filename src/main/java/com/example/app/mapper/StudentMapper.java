package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Student;

@Mapper
public interface StudentMapper {

	List<Student> selectAll() throws Exception;
	Student selectById(Integer id) throws Exception;
	Student selectByLoginId(String loginId) throws Exception;
	void setDeleteById(Integer id) throws Exception;
	void insert(Student student) throws Exception;
	void update(Student student) throws Exception;
	List<Student> selectLimited(@Param("offset") int offset, @Param("num") int num) throws Exception;
    long countActive() throws Exception;

}
