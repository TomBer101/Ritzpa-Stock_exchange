package servlets;

import DTOs.DataToAlertDTO;
import com.google.gson.Gson;
import enginePackage.User;
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

public class CheckForAlert extends HttpServlet
{
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json");

        String userNameFromSession = SessionUtils.getUsername(request);

        Users users = ServletsUtils.getUsers(getServletContext());

        User user = users.getUser(userNameFromSession);

        List<DataToAlertDTO.StockAlert> alerts = user.getDataToAlertDTO();

        if (alerts == null)
        {
            response.setStatus(401);
        }
        else
        {
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(alerts);

            try(PrintWriter out = response.getWriter())
            {
                out.print(jsonResponse);
                out.flush();
            }
        }

    }
    /******************************************************************************/
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }
    /******************************************************************************/
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    /******************************************************************************/
}
