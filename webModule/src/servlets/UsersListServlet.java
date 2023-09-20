package servlets;

import com.google.gson.Gson;
import enginePackage.Users;
import javafx.util.Pair;
import utils.ServletsUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public class UsersListServlet extends HttpServlet
{
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter())
        {
            Gson gson = new Gson();
            Users users = ServletsUtils.getUsers(getServletContext());
            String json = gson.toJson(users.getUsersOnline());
            out.println(json);
            out.flush();
        }
    }
    /******************************************************************************/
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }
    /******************************************************************************/
}
