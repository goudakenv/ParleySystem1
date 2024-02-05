package com.example.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Login;
import com.example.app.domain.Student;
import com.example.app.login.LoginAuthority;
import com.example.app.login.LoginStatus;
import com.example.app.service.StudentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StudentLoginController {

	private final StudentService studentService;
	private final HttpSession session;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute(new Login());
		return "login-student";
	}

	@PostMapping("/login")
	public String login(
			@Valid Login login,
			Errors errors) throws Exception {
		if(errors.hasErrors()) {
			return "login-student";
		}

		// 正しいIDとパスワードの組み合わせか確認
		Student student = studentService.getStudentByLoginId(login.getLoginId());
		if(student == null || !login.isCorrectPassword(student.getLoginPass())) {
			errors.rejectValue("loginId", "error.incorrect_id_or_password");
			return "login-student";
		}

		// セッションに認証情報を格納
		LoginStatus loginStatus = LoginStatus.builder()
				.id(student.getId())
				.name(student.getName())
				.loginId(student.getLoginId())
				.authority(LoginAuthority.STUDENT)
				.build();
		session.setAttribute("loginStatus", loginStatus);
		return "redirect:/rental";
	}

	@GetMapping("/logout")
	public String logout(
			RedirectAttributes redirectAttributes) {
		session.removeAttribute("loginStatus");
		redirectAttributes.addFlashAttribute("message", "ログアウトしました。");
		return "redirect:/login";
	}

}
