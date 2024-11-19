package com.tap;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Check extends HttpServlet{
	
	
	String url="jdbc:mysql://localhost:3306/jdbcclasses";
	String username="root";
	String password="7093805291";

	String sql="Select UserName,Password from details where UserName=? and Password=?";
	private static Connection connection;
	
	
	@Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url,username,password);
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String un=req.getParameter("username");
		String pw=req.getParameter("password");
		resp.setContentType("text/html");
		
		PrintWriter out=resp.getWriter();
		
        try {
			PreparedStatement statement=connection.prepareStatement(sql);
			statement.setString(1, un);
			statement.setString(2, pw);
			
			ResultSet result=statement.executeQuery();
			
			if(result.next()) {
				RequestDispatcher rd=req.getRequestDispatcher("options.html");
				rd.forward(req, resp);
			}
			else {
				out.println("Invalid Details :(");
			}
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
        
	}

}
