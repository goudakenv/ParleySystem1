package com.example.app.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Material;
import com.example.app.service.MaterialService;
import com.example.app.service.RentalRecordService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/material")
@RequiredArgsConstructor
public class MaterialController {

	private static final int NUM_PER_PAGE = 5;
	private static final int RECORD_NUM = 3;

	private final MaterialService service;
	private final RentalRecordService rentalRecordService;
	private final HttpSession session;

	@GetMapping("/list")
	public String list(
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			Model model) throws Exception {
		// 詳細・追加・編集ページから戻る際に利用
		session.setAttribute("page", page);

		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("materialList", service.getMaterialListPerPage(page, NUM_PER_PAGE));
		return "admin/list-material";
	}

	@GetMapping("/show/{id}")
	public String show(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("material", service.getMaterialById(id));
		model.addAttribute("recordList", rentalRecordService.getLatestRentalRecordListByMaterialId(id, RECORD_NUM));
		return "admin/show-material";
	}

	@GetMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("material", new Material());
		model.addAttribute("materialTypeList", service.getMaterialTypeList());
		model.addAttribute("heading", "投稿の追加");
		return "admin/save-material";
	}

	@PostMapping("/add")
	public String add(
			@Valid Material material,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		if (!material.getName().isBlank()) {
			if (service.isExsitingMaterial(material.getName())) {
				errors.rejectValue("name", "error.existing_material_name");
			}
		}

		if (errors.hasErrors()) {
			model.addAttribute("materialTypeList", service.getMaterialTypeList());
			model.addAttribute("heading", "投稿の追加");
			return "admin/save-material";
		}

		service.addMaterial(material);
		redirectAttributes.addFlashAttribute("message", "投稿を追加しました。");

		// 追加後に戻るページ(⇒最終ページ)
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		return "redirect:/admin/material/list?page=" + totalPages;
	}

	@GetMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("material", service.getMaterialById(id));
		model.addAttribute("materialTypeList", service.getMaterialTypeList());
		model.addAttribute("heading", "投稿の編集");
		return "admin/save-material";
	}

	@PostMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			@Valid Material material,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		String originalMaterialName = service.getMaterialById(id).getName();

		if (!material.getName().isBlank()) {
			if (!originalMaterialName.equals(material.getName()) && service.isExsitingMaterial(material.getName())) {
				errors.rejectValue("name", "error.existing_material_name");
			}
		}

		if (errors.hasErrors()) {
			model.addAttribute("materialTypeList", service.getMaterialTypeList());
			model.addAttribute("heading", "投稿の編集");
			return "admin/save-material";
		}

		service.editMaterial(material);
		redirectAttributes.addFlashAttribute("message", "投稿を編集しました。");

		// 編集後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/admin/material/list?page=" + previousPage;
	}

	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Integer id,
			RedirectAttributes redirectAttributes) throws Exception {
		service.deleteMaterialById(id);
		redirectAttributes.addFlashAttribute("message", "投稿を削除しました。");

		// 削除後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
		int previousPage = (int) session.getAttribute("page");
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		int page = previousPage <= totalPages ? previousPage : totalPages;
		return "redirect:/admin/material/list?page=" + page;
	}

}
