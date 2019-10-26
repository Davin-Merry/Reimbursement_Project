package com.project1.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.project1.controller.RequestHandler;

/*
 * Create two servlets. One for requests, another for login.
 * 
 * This one is for requests.
 */
@SuppressWarnings("serial")
public class MasterServlet extends HttpServlet {
	private static final Logger LOGGER = org.apache.log4j.Logger.getLogger(MasterServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String request = RequestHandler.process(req);
		PrintWriter p = resp.getWriter();
		
		resp.setContentType("application/json");
		p.println(request);
		LOGGER.trace("Master Servlet finished GET request.");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String response = RequestHandler.process(req);
		PrintWriter p = resp.getWriter();
		
		p.println(response);
		LOGGER.trace("Master Servlet finished POST request.");
	}
}