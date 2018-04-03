package org.raymon.xyz.blogplus.security;

import org.raymon.xyz.blogplus.common.constant.CommonConstant;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by lilm on 18-3-25.
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		if (session.getAttribute(CommonConstant.SESSION_KEY) != null) {
			return true;
		}
		
		// 跳转登录
		String url = "/home";
		response.sendRedirect(url);
		return false;
	}
}
