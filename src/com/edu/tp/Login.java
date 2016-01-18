package com.edu.tp;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login extends HttpServlet {

	private AtomicLong count = new AtomicLong();

	public Login() {
		super();

		System.out.println("new.......");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// com.edu.tp.Login

		for (Cookie cookie : request.getCookies()) {
			System.out.println("Cookie : " + cookie.getName() + " / " + cookie.getValue());
		}
		HttpSession session = request.getSession();
		if (session.getAttribute("name") == null) {
			session.setAttribute("name", "buskingplay" + new Date().toString());
		}

		response.getWriter().write("Login.servlet " + count.getAndIncrement());
		response.getWriter().write("<br>");
		response.getWriter().write("name : " + session.getAttribute("name"));

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST------------------------------------");
		doGet(request, response);
	}

}
