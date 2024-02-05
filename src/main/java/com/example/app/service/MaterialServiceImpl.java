package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Material;
import com.example.app.domain.MaterialType;
import com.example.app.mapper.MaterialMapper;
import com.example.app.mapper.MaterialTypeMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

	private final MaterialMapper materialMapper;
	private final MaterialTypeMapper materialTypeMapper;

	@Override
	public List<Material> getMaterialList() throws Exception {
		return materialMapper.selectAll();
	}

	@Override
	public Material getMaterialById(Integer id) throws Exception {
		return materialMapper.selectById(id);
	}

	@Override
	public void deleteMaterialById(Integer id) throws Exception {
		materialMapper.setDeleteById(id);
	}

	@Override
	public void addMaterial(Material material) throws Exception {
		materialMapper.insert(material);
	}

	@Override
	public void editMaterial(Material material) throws Exception {
		materialMapper.update(material);
	}
	
	@Override
	public boolean isExsitingMaterial(String name) throws Exception {
		Material material = materialMapper.selectByName(name);
		if(material != null) {
			return true;
		}

		return false;
	}

	@Override
	public List<Material> getMaterialListPerPage(int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		return materialMapper.selectLimited(offset, numPerPage);
	}

	@Override
	public int getTotalPages(int numPerPage) throws Exception {
		long count = materialMapper.countActive();
		return (int) Math.ceil((double) count / numPerPage);
	}
	
	@Override
	public List<Material> getBorrowingMaterialList(int studentId) throws Exception {
		return materialMapper.selectBorrowingByStudentId(studentId);
	}

	@Override
	public List<Material> getBorrowableMaterialListPerPage(int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		return materialMapper.selectBorrowableWithOffset(offset, numPerPage);
	}

	@Override
	public int getTotalBorrowableMaterialPages(int numPerPage) throws Exception {
		long count = materialMapper.countBorrowable();
		return (int) Math.ceil((double) count / numPerPage);
	}

	@Override
	public boolean isBorrowable(Integer materialId) throws Exception {
		Material material = materialMapper.selectById(materialId);

		if(material == null) {
			return false;
		}
		else if(material.getRentalId() != null) {
			return false;
		}

		return true;
	}

	@Override
	public List<MaterialType> getMaterialTypeList() throws Exception {
		return materialTypeMapper.selectAll();
	}

}
