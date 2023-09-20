package servlets;

import chatPackage.ChatManager;
import chatPackage.SingleChatEntry;
import com.google.gson.Gson;
import utils.ServletsUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ChatServlet extends HttpServlet
{
    /******************************************************************************/
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json");

        ChatManager chatManager = ServletsUtils.getChatManager(getServletContext());

        String username = SessionUtils.getUsername(request);

        if (username == null)
        {
            // TODO : we need to do something??
        }

        List<SingleChatEntry> chatEntries;

        synchronized (getServletContext())
        {
            chatEntries = chatManager.getChatDataList();
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(chatEntries);
        try (PrintWriter out = response.getWriter())
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }
    /******************************************************************************/
}
