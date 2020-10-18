package com.aws.lambda.online;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.aws.lambda.online.data.RequestDetails;
import com.aws.lambda.online.data.ResponseDetails;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;


public class MySqlTest implements RequestHandler<RequestDetails, ResponseDetails> {

	@Override
	public ResponseDetails handleRequest(RequestDetails requestDetails, Context context) {
		
		ResponseDetails responseDetails = new ResponseDetails(); 
		
		try {
			insertDetails(requestDetails, responseDetails); 
		} catch (SQLException e) {
			responseDetails.setMessageID("999");
			responseDetails.setMessageReason("Unable to Register " + e);
		}
		
		return responseDetails; 
	}

	private void insertDetails(RequestDetails requestDetails, ResponseDetails responseDetails ) throws SQLException {
		Connection connection = getConnection(); 
		Statement statement = connection.createStatement(); 
		String query = getquery(requestDetails); 
		int responseCode = statement.executeUpdate(query); 
		if ( responseCode == 1 ) {
			responseDetails.setMessageID(String.valueOf(responseCode));
			responseDetails.setMessageReason("Successfully updated details");
		}
	}
	
	private String getquery(RequestDetails requestDetails) {
		String query = "INSERT INTO onlineshopping.employee (empid, empname ) VALUES (";
		if (requestDetails != null) {
			query = query.concat("'" + requestDetails.getEmpID() + "','" + requestDetails.getEmpName() + "')"); 
		}
		System.out.println("the query is " + query);
		System.out.println("Request details are: id is "  + requestDetails.getEmpID() + " and name " + requestDetails.getEmpName());
		return query; 
	}
	
	
	private Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://mysqltest.crkyuecboye3.us-east-1.rds.amazonaws.com:3306";
		String username = ""; 
		String password = "";
		Connection connection = DriverManager.getConnection(url, username, password); 
		return connection; 
	}

	
}
