package com.project1.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.project1.session.SessionHandler;

/*
 * Create two servlets. One for login, another for after login.
 * 
 * This one is for the session before and during login. When logging out, we want to invalidate this session afterwards.
 */
@SuppressWarnings("serial")
public class SessionServlet extends HttpServlet {
	private static final Logger LOGGER = org.apache.log4j.Logger.getLogger(SessionServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Attach the following headers to ensure that you cannot backspace to the previous page.
		//This essentially *forces* the browser to not keep track of the forward.
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		req.getRequestDispatcher(SessionHandler.process(req)).forward(req, resp);
		LOGGER.trace("Session Servlet finished POST forward.");
	}
}
