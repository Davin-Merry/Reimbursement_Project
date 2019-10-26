package com.project1.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project1.model.Reimbursement;
import com.project1.model.User;
import com.project1.service.ReimbursementService;
import com.project1.service.UserService;

public class EmployeeController {
	public static String getReimbursementsById(HttpServletRequest req, String data) {
		if (req.getMethod().toLowerCase().equals("get")) {
			try {
				int i = Integer.parseInt(data);
				List<Reimbursement> r = new ReimbursementService().getReimbursementsByUserID(i);
				return new ObjectMapper().writeValueAsString(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String getUserByUsername(HttpServletRequest req, String data) {
		try {
			if (req.getMethod().toLowerCase().equals("get")) {
				User u = new UserService().getUserByUsername(data);
				if (u != null) {
					return new ObjectMapper().writeValueAsString(u);
				}
			}
		} catch (Exception e) {
			//Do nothing
		}
		return null;
	}
	
	public static void createReimbursement(HttpServletRequest req) {
		String json = null;
		
		try {
			BufferedReader reader = req.getReader();
			json = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(json);
		if (json != null) {
			new ReimbursementService().createReimbursement(json);
		}
	}
}
