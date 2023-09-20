package servlets;

import DTOs.StockDTO;
import com.google.gson.Gson;
import enginePackage.Stocks;
import enginePackage.Users;
import utils.ServletsUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

public class StocksDataServlets extends HttpServlet
{
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        response.setContentType("application/json");
        Stocks stocks = ServletsUtils.getStocks(getServletContext());

        List<StockDTO> stocksEntries;

        synchronized (getServletContext())
        {
            stocksEntries = stocks.getOnlineStocks();
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(stocksEntries);

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
