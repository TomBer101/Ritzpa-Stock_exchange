package servlets;

import chatPackage.ChatManager;
import utils.ServletsUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendChatServlet extends HttpServlet
{
    /******************************************************************************/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        ChatManager chatManager = ServletsUtils.getChatManager(getServletContext());
        String username = SessionUtils.getUsername(request);

        if (username == null)
        {
            // TODO : we need to do something here?
        }

        String userChatString = request.getParameter("chatString");

        if (userChatString != null && !userChatString.isEmpty())
        {
            synchronized (getServletContext())
            {
                chatManager.addChatString(userChatString, username);
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
            throws ServletException, IOException
    {
        processRequest(request, response);
    }
    /******************************************************************************/
}
