package com.example.app.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Material {

	private Integer id;

	@NotBlank
	@Size(max = 30)
	private String name;

	@Size(max = 30)
	private String publisher;

	@Size(max = 100)
	private String note;

	private MaterialType materialType;
	private LocalDateTime created;
	private String status;

	private Integer rentalId;

}
