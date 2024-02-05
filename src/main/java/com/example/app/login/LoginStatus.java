package com.example.app.login;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginStatus {

	private Integer id;
	private String name;
	private String loginId;
	private int authority;

}
