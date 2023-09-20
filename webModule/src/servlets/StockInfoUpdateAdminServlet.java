package servlets;

import DTOs.StockDTO;
import com.google.gson.Gson;
import enginePackage.Stock;
import enginePackage.Stocks;
import utils.ServletsUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StockInfoUpdateAdminServlet extends HttpServlet
{
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        response.setContentType("application/json");

        Stocks stocks = ServletsUtils.getStocks(getServletContext());

        StockDTO stockDTO;

        synchronized (getServletContext())
        {
            stockDTO = stocks.stockToDTO(SessionUtils.getChosenStock(request));
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(stockDTO);

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
