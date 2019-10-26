package com.project1.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.project1.model.User;
import com.project1.service.UserService;

public class LoginController {
	private static final Logger LOGGER = Logger.getLogger(LoginController.class);
	//TODO Possibly remove. There is really no use for this method.
	public static String getAllUsers(HttpServletRequest req) {
		if (req.getMethod().toLowerCase().equals("get")) {
			return new UserService().fetchAllUsers().toString();
		}
		return null;
	}
	
	public static String validateUser(HttpServletRequest req) {
		if (req.getMethod().toLowerCase().equals("post")) {
			User u = new UserService().getUserByLogin(req.getParameter("user"), req.getParameter("pass"));
			if (u == null) {
				LOGGER.info("A failed login attempt made by " + req.getParameter("user") + ".");
				return "/main/login.html";
			} else if (u.getRole_id() == 1 || u.getRole_id() == 2) {
				LOGGER.info("Successful manager login by username " + u.getUsername() + ".");
				return "/menu/manager.html";
			} else if (u.getRole_id() == 0) {
				LOGGER.info("Successful employee login by username " + u.getUsername() + ".");
				return "/menu/employee.html";
			}
		}
		return "/main/login.html";
	}
}
