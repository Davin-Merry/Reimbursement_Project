package com.project1.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project1.model.Reimbursement;
import com.project1.service.ReimbursementService;

public class ManagerController {
	private static final Logger LOGGER = Logger.getLogger(ManagerController.class);
	
	public static String getAllReimbursements(HttpServletRequest req) {
		if (req.getMethod().toLowerCase().equals("get")) {
			try {
				List<Reimbursement> r = new ReimbursementService().fetchAllReimbursements();
				return new ObjectMapper().writeValueAsString(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void updateReimbursementStatusList(HttpServletRequest req) {
		try {
			JsonNode json = new ObjectMapper().readTree(req.getReader().readLine());
			//Get initial sizes of each array in the JsonNode
			System.out.println(json);
			int s1 = json.get(0).size();
			int s2 = json.get(1).size();
			//Get the resolver's ID.
			int r = json.get(2).asInt();
			
			//Create a new int array
			int[][] list = new int[2][(s1 > s2) ? s1 : s2];
			System.out.println("Size for our array: " + list.length + ", " + list[0].length);
			for (int i = 0; i < list.length; i++) {
				System.out.println("i = " + i);
				for (int j = 0; j < list[i].length; j++) {
					//By default, we want to fill our array with -1, unless something is found in our JSON
					list[i][j] = -1;
					System.out.println("j = " + j);
					JsonNode t = json.get(i).get(j);
					if (t != null) {
						list[i][j] = json.get(i).get(j).asInt(-1);
					}
					System.out.println("Value of current selection: " + list[i][j]);
				}
			}
			LOGGER.info("JSON successfully parsed from client. Ready for reimbursement changes.");
			new ReimbursementService().updateReimbursements(list, r);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
