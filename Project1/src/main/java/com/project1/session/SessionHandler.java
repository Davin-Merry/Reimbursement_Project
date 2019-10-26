package com.project1.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.project1.controller.LoginController;

public class SessionHandler {
	public static String process(HttpServletRequest req) {
		switch(req.getRequestURI()) {
		case "/Project1/user/login":
			String str1 = LoginController.validateUser(req);
			
			if (!str1.equals("/main/login.html")) {
				//The session is simply created and stored here. It's not really used in any way.
				//See "SessionServlet.java" for more info.
				@SuppressWarnings("unused")
				HttpSession s = req.getSession();
			}
			return str1;
		case "/Project1/user/validity":
			//TODO Find a way to perform this.
			if (req.getSession(false) != null) {
				return "{\"valid\":1}";
			}
			return null;
		case "/Project1/user/logout":
			req.getSession().invalidate();
			return "/main/login.html";
		default:
			return "/main/login.html";
		}
	}
}
