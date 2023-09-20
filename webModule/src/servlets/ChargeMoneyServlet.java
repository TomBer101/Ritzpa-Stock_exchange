package servlets;

import enginePackage.Users;
import utils.ServletsUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChargeMoneyServlet extends HttpServlet
{
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Users users = ServletsUtils.getUsers(getServletContext());
        String userNameFromSession = SessionUtils.getUsername(request);
        int amountFromParameter = Integer.parseInt(request.getParameter("moneyAmount"));
        users.addCashToUser(userNameFromSession, amountFromParameter);
    }
    /******************************************************************************/
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }
    /******************************************************************************/
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }
    /******************************************************************************/

}
