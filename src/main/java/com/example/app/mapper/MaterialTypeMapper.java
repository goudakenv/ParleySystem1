package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.MaterialType;

@Mapper
public interface MaterialTypeMapper {

	List<MaterialType> selectAll() throws Exception;

}
