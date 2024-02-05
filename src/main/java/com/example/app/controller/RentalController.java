package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.login.LoginStatus;
import com.example.app.service.MaterialService;
import com.example.app.service.RentalRecordService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RentalController {

	private static final int NUM_PER_PAGE = 5;
	private static final int BORROWABLE_LIMIT = 3;

	private final MaterialService materialService;
	private final RentalRecordService rentalRecordService;
	private final HttpSession session;

	@GetMapping({ "/", "/rental" })
	public String rental(
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			Model model) throws Exception {
		// セッション(生徒IDを含んでいる)を取得
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");

		// ページ番号の保持
		session.setAttribute("page", page);

		// 全体のページ数
		int totalPages = materialService.getTotalBorrowableMaterialPages(NUM_PER_PAGE);
		model.addAttribute("totalPages", totalPages);
		// 現在のページ番号
		model.addAttribute("currentPage", page);
		// 現在、借りている教材のリスト
		model.addAttribute("borrowingList", materialService.getBorrowingMaterialList(loginStatus.getId()));
		// 貸し出し可能な教材のリスト
		model.addAttribute("materialList", materialService.getBorrowableMaterialListPerPage(page, NUM_PER_PAGE));
		return "list-rental";
	}

	// 「借りる」ボタン
	@GetMapping("/rental/borrow/{materialId}")
	public String borrowMaterial(
			@PathVariable("materialId") Integer materialId,
			RedirectAttributes redirectAttributes) throws Exception {
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");

		int previousPage = (int) session.getAttribute("page");

		// 借りている教材数が最大値を超えていないか確認
		if (!rentalRecordService.isAbleToBorrow(loginStatus.getId(), BORROWABLE_LIMIT)) {
			redirectAttributes.addFlashAttribute("message", "送信したままの数は" + BORROWABLE_LIMIT + "個までです");
			return "redirect:/rental?page=" + previousPage;
		}

		// 借りようとしている教材が貸し出されていないか確認
		if (!materialService.isBorrowable(materialId)) {
			redirectAttributes.addFlashAttribute("message", "投稿は送受信中、または削除済みです");
			return "redirect:/rental?page=" + previousPage;
		}

		// 問題がなければ、「借りる」処理を実行
		rentalRecordService.borrowMaterial(loginStatus.getId(), materialId);

		// 借りた後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
		int totalPages = materialService.getTotalBorrowableMaterialPages(NUM_PER_PAGE);
		int page = previousPage <= totalPages ? previousPage : totalPages;
		return "redirect:/rental?page=" + page;
	}

	// 「返却」ボタン
	@GetMapping("/rental/return/{materialId}")
	public String returnMaterial(
			@PathVariable("materialId") Integer materialId) throws Exception {
		// 本人による返却か確認
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		if (!rentalRecordService.byAuthenticatedStudent(loginStatus.getId(), materialId)) {
			System.out.println("他のメンバーによる返却");
		} else {
			rentalRecordService.returnMaterial(materialId);
		}

		// 返却後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/rental?page=" + previousPage;
	}

}
