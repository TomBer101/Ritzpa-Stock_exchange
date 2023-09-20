package servlets;

import DTOs.StockDTO;
import DTOs.UserDataDTO;
import com.google.gson.Gson;
import enginePackage.Users;
import utils.ServletsUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

public class UserDataServlet extends HttpServlet
{
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        response.setContentType("application/json");

        Users users = ServletsUtils.getUsers(getServletContext());

        UserDataDTO userData;
        synchronized (getServletContext())
        {
            userData = users.userToDTO(SessionUtils.getUsername(request));
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(userData);

        try(PrintWriter out = response.getWriter())
        {
            out.print(jsonResponse);
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
