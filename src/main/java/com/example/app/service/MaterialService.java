package com.example.app.service;

import java.util.List;

import com.example.app.domain.Material;
import com.example.app.domain.MaterialType;

public interface MaterialService {

	List<Material> getMaterialList() throws Exception;
	Material getMaterialById(Integer id) throws Exception;
	void deleteMaterialById(Integer id) throws Exception;
	void addMaterial(Material material) throws Exception;
	void editMaterial(Material material) throws Exception;
	boolean isExsitingMaterial(String name) throws Exception;
	List<Material> getMaterialListPerPage(int page, int numPerPage) throws Exception;
    int getTotalPages(int numPerPage) throws Exception;
    
    // ある生徒が現在借りている教材の取得
    List<Material> getBorrowingMaterialList(int studentId) throws Exception;
    // 貸し出し可能な教材のリスト(ページごと)
    List<Material> getBorrowableMaterialListPerPage(int page, int numPerPage) throws Exception;
    // 教材が貸し出し可能な教材のページ数
    int getTotalBorrowableMaterialPages(int numPerPage) throws Exception;
    // 教材が貸し出し可能か否か判別
    boolean isBorrowable(Integer materialId) throws Exception;

	List<MaterialType> getMaterialTypeList() throws Exception;

}
