import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class UserServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String url = "jdbc:postgresql://10.0.1.10:5432/myapp";
        String user = "postgres";
        String password = "123";

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            out.println("<html><body><h1>Список пользователей:</h1><ul>");
            while (rs.next()) {
                out.println("<li>" + rs.getString("name") + "</li>");
            }
            out.println("</ul></body></html>");

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            out.println("<p>Ошибка: " + e.getMessage() + "</p>");
        }
    }
}
