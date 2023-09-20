package servlets;

import enginePackage.Stocks;
import enginePackage.Users;
import utils.ServletsUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class IssueStockServlet extends HttpServlet
{
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
       //response.setContentType("text/html;charset=UTF-8");
       response.setContentType("application/javascript");
        Stocks stocks = ServletsUtils.getStocks(getServletContext());

        String symbolFromParameter = request.getParameter("symbol");
        String companyNameFromParameter = request.getParameter("companyName");

        boolean res = stocks.isStockExists(symbolFromParameter, companyNameFromParameter);

        if(res)
        {
            String errorMessage = "Symbol name " + symbolFromParameter + " already exist. Or company name " +
                   companyNameFromParameter + " already exists";
            response.setStatus(401);
            response.getOutputStream().println(errorMessage);
        }
        else
        {
            int amountOfStock =  Integer.parseInt(request.getParameter("stocksAmount"));
            int companyVal =  Integer.parseInt(request.getParameter("companyValue"));
            synchronized (getServletContext())
            {
                stocks.addStock(symbolFromParameter, companyNameFromParameter,
                        amountOfStock, companyVal);

                String userNameFromSession = SessionUtils.getUsername(request);
                Users users = ServletsUtils.getUsers(getServletContext());

                users.addStock2User(userNameFromSession, symbolFromParameter, amountOfStock, companyVal );
            }

        }

    }
    /******************************************************************************/
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    /******************************************************************************/
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    /******************************************************************************/
}
