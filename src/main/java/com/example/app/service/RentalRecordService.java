package com.example.app.service;

import java.util.List;

import com.example.app.domain.RentalRecord;

public interface RentalRecordService {

	// ある教材の最新の貸し出し履歴
	List<RentalRecord> getLatestRentalRecordListByMaterialId(int materialId, int num) throws Exception;
	// 教材を借りる
	void borrowMaterial(int studentId, int materialId) throws Exception;
	// 教材を返却する
	void returnMaterial(int materialId) throws Exception;
	// 生徒が教材を借りられる状態か判別する
	boolean isAbleToBorrow(int studentId, int limitation) throws Exception;
	// 本人による返却か確認する
	boolean byAuthenticatedStudent(int studentId, int materialId) throws Exception;

}
