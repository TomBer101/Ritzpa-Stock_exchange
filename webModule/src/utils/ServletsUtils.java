package utils;

import chatPackage.ChatManager;
import enginePackage.Stocks;
import enginePackage.Users;

import javax.servlet.ServletContext;

public class ServletsUtils
{
    /******************************************************************************/
    private static final String USER_MANAGER_ATTRIBUTE = "userManager";
    private static final String STOCK_MANAGER_ATTRIBUTE = "stockManager";
    private static final String CHAT_MANAGER_ATTRIBUTE = "chatManager";
    /******************************************************************************/
    private static final Object userManagerLock = new Object();
    private static final Object stockManagerLock = new Object();
    private static final Object chatManagerLock = new Object();
    /******************************************************************************/
    public static Users getUsers(ServletContext servletContext)
    {
        synchronized (userManagerLock)
        {
            if(servletContext.getAttribute(USER_MANAGER_ATTRIBUTE) == null)
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE, new Users());
        }
        return (Users)servletContext.getAttribute(USER_MANAGER_ATTRIBUTE);
    }
    /******************************************************************************/
    public static Stocks getStocks(ServletContext servletContext)
    {
        synchronized (stockManagerLock)
        {
            if(servletContext.getAttribute(STOCK_MANAGER_ATTRIBUTE) == null)
                servletContext.setAttribute(STOCK_MANAGER_ATTRIBUTE, new Stocks());
        }
        return (Stocks)servletContext.getAttribute(STOCK_MANAGER_ATTRIBUTE);
    }
    /******************************************************************************/
    public static ChatManager getChatManager(ServletContext servletContext) {
        synchronized (chatManagerLock) {
            if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE) == null) {
                servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE, new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE);
    }
    /******************************************************************************/
}
