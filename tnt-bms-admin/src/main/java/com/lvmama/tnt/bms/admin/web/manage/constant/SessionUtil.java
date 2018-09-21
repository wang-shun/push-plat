package com.lvmama.tnt.bms.admin.web.manage.constant;

import com.lvmama.tnt.bms.admin.web.manage.domain.UserDTO;

import javax.servlet.http.HttpSession;

/**
 *
 */
public class SessionUtil {
    private static final String SESSION_KEY_USER = "SESSION_AUTH_LOGIN_USER";

    private SessionUtil() {
    }

    public static UserDTO getUser(HttpSession session) {
        return (UserDTO)session.getAttribute(SESSION_KEY_USER);
    }

    public static void setUser(UserDTO user, HttpSession session) {
        session.setAttribute(SESSION_KEY_USER, user);
    }

    public static void removeUser(HttpSession session) {
        session.removeAttribute(SESSION_KEY_USER);
    }

    public static void putSession(HttpSession session, String key, Object value) {
        session.setAttribute(key, value);
    }
}
