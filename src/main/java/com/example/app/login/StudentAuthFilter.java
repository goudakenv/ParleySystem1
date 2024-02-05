package com.example.app.login;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class StudentAuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		if(session.getAttribute("loginStatus") == null) {
			res.sendRedirect("/login");
			return;
		}

		LoginStatus status = (LoginStatus) session.getAttribute("loginStatus");
		if(status.getAuthority() != LoginAuthority.STUDENT) {
			res.sendRedirect("/login");
			return;
		}

		chain.doFilter(request, response);
	}

}
