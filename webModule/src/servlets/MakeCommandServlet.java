package servlets;

import DTOs.CommandDTO;
import com.google.gson.Gson;
import enginePackage.Command;
import enginePackage.Stocks;
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
import java.util.Set;

public class MakeCommandServlet extends HttpServlet
{
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Stocks stocks = ServletsUtils.getStocks(getServletContext());
        User user = ServletsUtils.getUsers(getServletContext()).getUser(SessionUtils.getUsername(request));
        Command.Way way = Command.Way.valueOf(request.getParameter("way"));
        Command.Type type = Command.Type.valueOf(request.getParameter("type"));

        int priceLimit = type.toString().equals(Command.Type.MKT.toString()) ? (-1) : Integer.parseInt(request.getParameter("priceLimit"));
        int amount = Integer.parseInt(request.getParameter("stockAmount"));

        CommandDTO res = stocks.addCommand(SessionUtils.getChosenStock(request),user,type, way, amount, priceLimit);

        try (PrintWriter out = response.getWriter())
        {
            Gson gson = new Gson();
            String json = gson.toJson(res);
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }
}
