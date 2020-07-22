package com.ldy.programmer.interceptor.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ldy.programmer.entity.admin.Menu;
import com.ldy.programmer.util.MenuUtil;
/**
 * 后台登录拦截器
 * @author llq
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		// TODO Auto-generated method stub
		String requestURI = request.getRequestURI();
		Object admin = request.getSession().getAttribute("admin");
		Object role = request.getSession().getAttribute("role");
		//获取菜单id
		String mid = request.getParameter("_mid");
		System.out.println("链接："+requestURI+"   进入拦截器！");
		if(admin == null && role == null){
			//表示未登录或者登录失效
			System.out.println("无用户登录 admin 和 role 都为空");
			String header = request.getHeader("X-Requested-With");
			//判断是否是ajax请求
			if("XMLHttpRequest".equals(header)){
				//表示是ajax请求
				Map<String, String> ret = new HashMap<String, String>();
				ret.put("type", "error");
				ret.put("msg", "登录会话超时或还未登录，请重新登录!");
				response.getWriter().write(JSONObject.fromObject(ret).toString());
				return false;
			}
			//表示是普通链接跳转，直接重定向到登录页面
			response.sendRedirect(request.getServletContext().getContextPath() + "/system/login");
			return false;
		}
		System.out.println("_mId: " + mid);
		if(!StringUtils.isEmpty(mid)){
			List<Menu> userMenus = (List<Menu>) request.getSession().getAttribute("userMenus");
			System.out.println("userMenus: " + userMenus);
			List<Menu> allThirdMenu = MenuUtil.getAllThirdMenu(userMenus, Long.valueOf(mid));
			System.out.println("allThirdMenu:  " + allThirdMenu);
			request.setAttribute("thirdMenuList", allThirdMenu);
		}
		return true;
	}

}

