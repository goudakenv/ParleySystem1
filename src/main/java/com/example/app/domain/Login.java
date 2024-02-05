package com.example.app.domain;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Login {

	@NotBlank
	private String loginId;

	@NotBlank
	private String loginPass;

	public boolean isCorrectPassword(String hashedPassword) {
		return BCrypt.checkpw(loginPass, hashedPassword);
	}

}
