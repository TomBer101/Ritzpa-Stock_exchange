package servlets;

import DTOs.StockDTO;
import com.google.gson.Gson;
import enginePackage.Stock;
import enginePackage.Stocks;
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

public class StockInfoUpdateBrokerServlet extends HttpServlet
{
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Users users = ServletsUtils.getUsers(getServletContext()); // get the current user - need to know if and how many shares he holds.
        Stocks stocks = ServletsUtils.getStocks(getServletContext());
        StockDTO stockDTO;

        String userNameFromSession = SessionUtils.getUsername(request);
        int amount = users.getAmountOfStock(userNameFromSession, SessionUtils.getChosenStock(request));

        synchronized (getServletContext())
        {
            stockDTO = stocks.stockToDTO(SessionUtils.getChosenStock(request));
        }

        StockData4Broker res = new StockData4Broker(stockDTO, amount);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(res);

        try(PrintWriter out = response.getWriter())
        {
            out.print(jsonResponse);
            out.flush();
        }


    }
    /******************************************************************************/
    private  class StockData4Broker
    {
        final private StockDTO stockDTO;
        final private int amount;

        public StockData4Broker(StockDTO src, int amount)
        {
            this.stockDTO = src;
            this.amount = amount;
        }

    }
    /******************************************************************************/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
    /******************************************************************************/
}
