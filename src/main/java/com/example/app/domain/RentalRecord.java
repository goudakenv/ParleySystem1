package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RentalRecord {

	private Integer id;
	private Integer studentId;
	private Integer materialId;
	private LocalDateTime borrowedAt;
	private LocalDateTime returnedAt;

	// 貸し出し記録表示用
	private String studentName;

}
