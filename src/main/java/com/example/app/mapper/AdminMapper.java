package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Admin;

@Mapper
public interface AdminMapper {

	Admin selectByLoginId(String loginId) throws Exception;

}
