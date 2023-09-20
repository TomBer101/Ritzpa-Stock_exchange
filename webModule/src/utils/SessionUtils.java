package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {
    /******************************************************************************/
    public static String getUsername(HttpServletRequest request) {
        // get session, if not exist does not create a new one.
        HttpSession session = request.getSession(false);
        Object sessionAtribute = session != null ? session.getAttribute("userName") : null;
        return sessionAtribute != null ? sessionAtribute.toString() : null;
    }

    /******************************************************************************/
    public static String getChosenStock(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAtribute = session != null ? session.getAttribute("symbol") : null;
        return sessionAtribute != null ? sessionAtribute.toString() : null;
    }

    /******************************************************************************/
    public static void clearSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }
    /******************************************************************************/
}
