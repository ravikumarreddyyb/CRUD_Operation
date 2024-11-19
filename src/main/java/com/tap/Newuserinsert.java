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

@WebServlet("/callnewuser")
public class Newuserinsert extends HttpServlet {

    String url = "jdbc:mysql://localhost:3306/jdbcclasses";
    String username = "root";
    String password = "7093805291";

    String checkSql = "SELECT COUNT(*) FROM details WHERE Password = ?";
    String insertSql = "INSERT INTO details (`UserName`, `Password`) VALUES (?, ?)";
    private static Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String un = req.getParameter("username");
        String pw = req.getParameter("password");
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        // CSS Styles for the response
        String css = "<style>" +
                     "body { font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 20px; }" +
                     "div { margin: 20px; padding: 20px; border-radius: 5px; }" +
                     "h2 { color: #333; }" +
                     "button { background-color: #007bff; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; }" +
                     "button:hover { background-color: #0056b3; }" +
                     "</style>";

        try {
            // Check if the password already exists
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, pw);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Password already exists
                out.println("<html><head><title>Result</title>" + css + "</head><body>");
                out.println("<div>");
                out.println("<h2>Password already exists. Please choose a different one.</h2>");
                out.println("<form action='newuserform.html' method='get'>");
                out.println("<button type='submit'>New User</button>");
                out.println("</form>");
                out.println("</div>");
                out.println("</body></html>");
                
            } else {
                // Insert the new user
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setString(1, un);
                insertStmt.setString(2, pw);

                int result = insertStmt.executeUpdate();

                out.println("<html><head><title>Result</title>" + css + "</head><body>");
                if (result > 0) {
                    out.println("<div>");
                    out.println("<h2>Insert Successfully</h2>");
                    out.println("<form action='options.html' method='get'>");
                    out.println("<button type='submit'>Go to Options</button>");
                    out.println("</form>");
                    out.println("</div>");
                } else {
                    out.println("<div>");
                    out.println("<h2>Insert Failed</h2>");
                    out.println("</div>");
                }
                out.println("</body></html>");
            }
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
