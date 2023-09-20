package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "StockInfoServlet", urlPatterns = {"/StockInfoServlet"})
public class StockInfoServlet extends HttpServlet
{
    /******************************************************************************/
    private final String RSE_ADMIN_STOCK_PAGE_URL = "../adminStockPage/adminStockPage.html";
    private final String RSE_BROKER_STOCK_PAGE_URL = "../brokerStockPage/brokerStockPage.html";
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();

        if (session == null )
        {
            response.setStatus(401);
            response.getOutputStream().println("For some reason we got there...");
        }
        else
        {
            session.setAttribute("symbol", request.getParameter("symbol"));
            if(session.getAttribute("type").toString().equals("broker"))
            {
                response.setStatus(200);
                response.getOutputStream().println(RSE_BROKER_STOCK_PAGE_URL);
            }
            else
            {
                response.setStatus(200);
                response.getOutputStream().println(RSE_ADMIN_STOCK_PAGE_URL);
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
    /******************************************************************************/
}
