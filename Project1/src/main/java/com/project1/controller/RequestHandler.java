package com.project1.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/*
 * TODO This should be responsible for getting/sending information in JSON format
 */
public class RequestHandler {
	private static final Logger LOGGER = Logger.getLogger(RequestHandler.class);
	
	public static String process(HttpServletRequest req) {
		String s = null;
		String uri = req.getRequestURI();
		String data = "";
		System.out.println(uri);
		
		if (uri.contains("info/user/")) {
			data = uri.split("/")[4];
			s = EmployeeController.getUserByUsername(req, data);
			System.out.println(s);
		} else if (uri.contains("info/reimbursement/")) {
			data = uri.split("/")[4];
			if (data.equals("all")) {
				s = ManagerController.getAllReimbursements(req);
			} else {
				s = EmployeeController.getReimbursementsById(req, data);
			}
		} else if (uri.contains("/submit/reimbursement")) {
			if (req.getMethod().toLowerCase().contains("post")) {
				EmployeeController.createReimbursement(req);
				s = "{\"data\":true}";
			} else {
				LOGGER.debug("A GET attempt was made for retrieving reimbursement information.");
				s = "{\"data\":false}";
			}
				
		} else if (uri.contains("/submit/changes")) {
			if (req.getMethod().toLowerCase().contains("post")) {
				ManagerController.updateReimbursementStatusList(req);
				s = "{\"sent\":true}";
			} else {
				LOGGER.debug("A GET attempt was made for changing reimbursements.");
				s = "{\"sent\":false}";
			}
		}
		
		return s;
	}
}
