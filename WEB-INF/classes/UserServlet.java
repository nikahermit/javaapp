import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class UserServlet extends HttpServlet {

    private final String url = "jdbc:postgresql://10.0.1.10:5432/myapp";
    private final String user = "postgres";
    private final String password = "123";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            out.println("<html><body><h1>Список пользователей:</h1><ul>");

            while (rs.next()) {
                out.println("<li>" + rs.getString("name") + "</li>");
            }

            out.println("</ul>");

            // Форма добавления пользователя
            out.println("<h2>Добавить пользователя:</h2>");
            out.println("<form method='post' action='users'>");
            out.println("Имя: <input type='text' name='username' required>");
            out.println("<input type='submit' value='Добавить'>");
            out.println("</form>");

            out.println("</body></html>");

        } catch (Exception e) {
            out.println("<p>Ошибка: " + e.getMessage() + "</p>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES(?)")) {

            stmt.setString(1, username);
            stmt.executeUpdate();

        } catch (Exception e) {
            response.getWriter().println("<p>Ошибка при добавлении: " + e.getMessage() + "</p>");
            return;
        }

        // Перенаправление на GET после POST
        response.sendRedirect("users");
    }
}
