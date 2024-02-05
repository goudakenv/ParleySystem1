package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.RentalRecord;
import com.example.app.mapper.MaterialMapper;
import com.example.app.mapper.RentalRecordMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalRecordServiceImpl implements RentalRecordService {

	private final RentalRecordMapper rentalRecordMapper;
	private final MaterialMapper rentalMapper;

	@Override
	public List<RentalRecord> getLatestRentalRecordListByMaterialId(int materialId, int num) throws Exception {
		return rentalRecordMapper.selectLatestByMaterialId(materialId, num);
	}

	@Override
	public void borrowMaterial(int studentId, int materialId) throws Exception {
		RentalRecord rentalRecord = new RentalRecord();
		rentalRecord.setStudentId(studentId);
		rentalRecord.setMaterialId(materialId);
		rentalRecordMapper.addBorrowedRecord(rentalRecord);
		rentalMapper.addBorrowedRecord(materialId, rentalRecord.getId());
	}

	@Override
	public void returnMaterial(int materialId) throws Exception {
		rentalRecordMapper.addReturnedRecord(materialId);
		rentalMapper.addReturnedRecord(materialId);
	}

	@Override
	public boolean isAbleToBorrow(int studentId, int limitation) throws Exception {
		return rentalRecordMapper.countBorrowingByStudentId(studentId) < limitation;
	}

	@Override
	public boolean byAuthenticatedStudent(int studentId, int materialId) throws Exception {
		RentalRecord record = rentalRecordMapper.selectBorrowingRecordByMaterialId(materialId);
		if(record == null) {
			return false;
		}

		if(!record.getStudentId().equals(studentId)) {
			return false;
		}

		return true;
	}

}
