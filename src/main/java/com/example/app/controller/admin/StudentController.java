package com.example.app.controller.admin;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Student;
import com.example.app.service.StudentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/student")
@RequiredArgsConstructor
public class StudentController {

	private static final int NUM_PER_PAGE = 5;

	private final StudentService service;
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
		model.addAttribute("studentList", service.getStudentListPerPage(page, NUM_PER_PAGE));
		return "admin/list-student";
	}

	@GetMapping("/add")
	public String add(Model model) {
		Student student = new Student();
		student.setBirthday(LocalDate.of(2000, 1, 1)); // 生年月日初期値
		model.addAttribute("student", student);
		model.addAttribute("heading", "メンバーの追加");
		return "admin/save-student";
	}

	@PostMapping("/add")
	public String add(
			@Valid Student student,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		if (!student.getLoginId().isBlank()) {
			if (service.isExsitingStudent(student.getLoginId())) {
				errors.rejectValue("loginId", "error.existing_student_loginId");
			}
		}

		if (errors.hasErrors()) {
			model.addAttribute("heading", "メンバーの追加");
			return "admin/save-student";
		}

		service.addStudent(student);
		redirectAttributes.addFlashAttribute("message", "メンバーを追加しました。");

		// 追加後に戻るページ(⇒最終ページ)
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		return "redirect:/admin/student/list?page=" + totalPages;
	}

	@GetMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("student", service.getStudentById(id));
		model.addAttribute("heading", "メンバーの追加");
		return "admin/save-student";
	}

	@PostMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			@Valid Student student,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		String originalLoginId = service.getStudentById(id).getLoginId();

		if (!student.getLoginId().isBlank()) {
			if (!originalLoginId.equals(student.getLoginId()) && service.isExsitingStudent(student.getLoginId())) {
				errors.rejectValue("loginId", "error.existing_student_loginId");
			}
		}

		if (errors.hasErrors()) {
			model.addAttribute("heading", "メンバーの編集");
			return "admin/save-student";
		}

		service.editStudent(student);
		redirectAttributes.addFlashAttribute("message", "メンバーを編集しました。");

		// 編集後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/admin/student/list?page=" + previousPage;
	}

	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Integer id,
			RedirectAttributes redirectAttributes) throws Exception {
		service.deleteStudentById(id);
		redirectAttributes.addFlashAttribute("message", "メンバーを削除しました。");

		// 削除後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
		int previousPage = (int) session.getAttribute("page");
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		int page = previousPage <= service.getTotalPages(NUM_PER_PAGE) ? previousPage : totalPages;
		return "redirect:/admin/student/list?page=" + page;
	}

}
