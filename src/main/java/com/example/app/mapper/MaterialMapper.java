package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Material;

@Mapper
public interface MaterialMapper {

	List<Material> selectAll() throws Exception;
	Material selectById(Integer id) throws Exception;
	Material selectByName(String name) throws Exception;
	void setDeleteById(Integer id) throws Exception;
	void insert(Material material) throws Exception;
	void update(Material material) throws Exception;
	List<Material> selectLimited(@Param("offset") int offset, @Param("num") int num) throws Exception;
    long countActive() throws Exception;
    
    // ある生徒の貸し出し中のリストを取得
    List<Material> selectBorrowingByStudentId(int studentId) throws Exception;
    // 貸し出し可能な教材のリストを取得(LIMIT句あり)
    List<Material> selectBorrowableWithOffset(@Param("offset") int offset, @Param("num") int num) throws Exception;
    // 貸し出し可能な教材の数を取得(ページ番号用)
    long countBorrowable() throws Exception;
    // 教材に「貸し出し中」を記録
    void addBorrowedRecord(@Param("id") int materialId, @Param("rentalId") int rentalId) throws Exception;
    // 教材に「返却済み」を記録
    void addReturnedRecord(int materialId) throws Exception;

}
