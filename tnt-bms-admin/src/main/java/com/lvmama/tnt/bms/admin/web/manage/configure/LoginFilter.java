package com.lvmama.tnt.bms.admin.web.manage.configure;

import com.lvmama.tnt.bms.admin.web.manage.constant.SessionUtil;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserDTO;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public class LoginFilter implements Filter {

    private static final String LOGIN_URL = "/tnt-bms-admin/login.html";
    private static final String[] ignoreType = {"/login.html","/asyncLogin",".json",".js",".css",".ico",".jpg",".png",".map"};


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        boolean ignore = false;
        for (int i = 0; i < ignoreType.length; i++) {
            if (uri.endsWith(ignoreType[i])) {
                ignore = true;
                break;
            }
        }
        if (ignore) {
            chain.doFilter(request, response);
        } else {
            if (!isLogin(req, resp)) {
                resp.sendRedirect(LOGIN_URL);
                return;
            }
            chain.doFilter(request, response);
        }

    }

    private boolean isLogin(HttpServletRequest req, HttpServletResponse resp) {
        UserDTO sessionUser = SessionUtil.getUser(req.getSession());
        if (sessionUser == null || (sessionUser != null && sessionUser.getUserName() == null)) {
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {

    }
}
