package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.RentalRecord;

@Mapper
public interface RentalRecordMapper {

	// ある教材の最新の貸し出し履歴
	List<RentalRecord> selectLatestByMaterialId(@Param("materialId") int materialId, @Param("num") int num) throws Exception;
	// 「借りる」に対応する処理：生徒ID, 教材ID, 貸出日を記録
	void addBorrowedRecord(RentalRecord rentalRecord) throws Exception;
	// 「返却」に対応する処理：返却日を記録
	void addReturnedRecord(int materialId) throws Exception;
	// ある生徒の未返却の教材数
	int countBorrowingByStudentId(int studentId) throws Exception;
	// 教材IDから現在の貸し出し状況(どの生徒が借りているか)を取得
	RentalRecord selectBorrowingRecordByMaterialId(int materialId) throws Exception;

}
