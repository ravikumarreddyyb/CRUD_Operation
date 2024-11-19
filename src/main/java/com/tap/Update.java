package com.tap;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/update")
public class Update extends HttpServlet {
    String url = "jdbc:mysql://localhost:3306/jdbcclasses";
    String username = "root";
    String password = "7093805291";
    String sql = "UPDATE Students SET StudentName=?, Email=?, Department=? WHERE id=?";
    
    public static Connection connection;
    
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        super.init();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String studentname = req.getParameter("studentname");
        String email = req.getParameter("email");
        String department = req.getParameter("department");
        
        resp.setContentType("text/html");
        
        PrintWriter out = resp.getWriter();
        
        // CSS styles for the response
        String css = "<style>" +
                     "body { font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 20px; }" +
                     "div { margin: 20px; padding: 20px; border-radius: 5px; background-color: #fff; }" +
                     "table { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
                     "table, th, td { border: 1px solid #ddd; }" +
                     "th, td { padding: 12px; text-align: left; }" +
                     "th { background-color: #f2f2f2; }" +
                     "button { background-color: #007bff; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; }" +
                     "button:hover { background-color: #0056b3; }" +
                     "</style>";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, studentname);
            statement.setString(2, email);
            statement.setString(3, department);
            statement.setInt(4, Integer.parseInt(id));
            
            int result = statement.executeUpdate();
            
            out.println("<html><head><title>Update Result</title>" + css + "</head><body>");
            
            if (result > 0) {
                out.println("<div>");
                out.println("<h2>Update Successfully</h2>");
            } else {
                out.println("<div>");
                out.println("<h2>Update Failed</h2>");
            }
            
            String query = "SELECT * FROM Students";
            java.sql.Statement stmt = connection.createStatement();
            ResultSet resu = stmt.executeQuery(query);
            
            out.println("<table>" +
                        "<tr>" +
                        "<th>ID</th>" +
                        "<th>StudentName</th>" +
                        "<th>Email</th>" +
                        "<th>Department</th>" +
                        "</tr>");
            
            while (resu.next()) {
                int id1 = resu.getInt(1);
                String name1 = resu.getString(2);
                String mail1 = resu.getString(3);
                String dept1 = resu.getString(4);
                
                out.println("<tr>" +
                            "<td>" + id1 + "</td>" +
                            "<td>" + name1 + "</td>" +
                            "<td>" + mail1 + "</td>" +
                            "<td>" + dept1 + "</td>" +
                            "</tr>");
            }
            out.println("</table>");
            out.println("</div>");
            
            out.println("<div style='margin-top: 20px;'>");
            out.println("<form action='options.html' method='get'>");
            out.println("<button type='submit'>Go to Options</button>");
            out.println("</form>");
            out.println("</div>");
            
            out.println("</body></html>");
            
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<html><head><title>Error</title>" + css + "</head><body>");
            out.println("<div>");
            out.println("<h2>An error occurred: " + e.getMessage() + "</h2>");
            out.println("</div>");
            out.println("</body></html>");
        }
    }
}
