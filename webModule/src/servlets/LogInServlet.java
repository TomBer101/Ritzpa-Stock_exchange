package servlets;

import enginePackage.User;
import enginePackage.Users;
import utils.ServletsUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LogInServlet extends HttpServlet
{
    /******************************************************************************/
    private final String RSE_ADMIN_HOME_PAGE_URL = "pages/adminHomePage/adminHomePage.html";
    private final String RSE_BROKER_HOME_PAGE_URL = "pages/brokerHomePage/brokerHomePage.html";
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        String userNameFromSession = SessionUtils.getUsername(request);
        Users users = ServletsUtils.getUsers(getServletContext());
        String userNameFromParameter = request.getParameter("username");    // gets what the user typed
        String typeFromParameter = request.getParameter("type");

        if (userNameFromSession == null)
        {
            // normalize the username value
            userNameFromParameter = userNameFromParameter.trim();

            synchronized (this)
            {
                if (users.isUserExist(userNameFromParameter))
                {
                  String errorMessage = "User name " + userNameFromParameter + " already exist. please enter a diffrent user name.";
                  response.setStatus(401);
                  response.getOutputStream().println(errorMessage);
                }
                else
                {
                    if(typeFromParameter.equals("broker"))
                    {
                        users.addUser(userNameFromParameter, User.Type.TRADER);
                        request.getSession(true).setAttribute("userName", userNameFromParameter);
                        request.getSession(true).setAttribute("type", "broker");
                        response.setStatus(200);
                        response.getOutputStream().println(RSE_BROKER_HOME_PAGE_URL);
                    }
                    else
                    {
                        users.addUser(userNameFromParameter, User.Type.ADMIN);
                        request.getSession(true).setAttribute("userName", userNameFromParameter);
                        request.getSession(true).setAttribute("type", "admin");
                        response.setStatus(200);
                        response.getOutputStream().println(RSE_ADMIN_HOME_PAGE_URL);
                    }
                }
            }
        }
        else
        {
            String typeFromSession = request.getSession().getAttribute("type").toString();

            if(!typeFromSession.equals(typeFromParameter))
            {
                String errorMessage = "Wrong role input.";
                response.setStatus(401);
                response.getOutputStream().println(errorMessage);
            }
            else
            {
                if (typeFromParameter.equals("broker"))
                    response.sendRedirect(RSE_BROKER_HOME_PAGE_URL);
                else
                    response.sendRedirect(RSE_BROKER_HOME_PAGE_URL);
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
}
