package com.example.app.domain;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Student {

	private Integer id;

	@NotBlank
	@Size(max = 30)
	private String name;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;

	@NotBlank
	@Size(max = 30)
	private String loginId;

	@NotBlank
	private String loginPass;

	private String status;

}
