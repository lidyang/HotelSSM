package com.ldy.programmer.controller.admin;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ldy.programmer.entity.admin.Authority;
import com.ldy.programmer.entity.admin.Menu;
import com.ldy.programmer.entity.admin.Role;
import com.ldy.programmer.entity.admin.User;
import com.ldy.programmer.service.admin.AuthorityService;
import com.ldy.programmer.service.admin.LogService;
import com.ldy.programmer.service.admin.MenuService;
import com.ldy.programmer.service.admin.RoleService;
import com.ldy.programmer.service.admin.UserService;
import com.ldy.programmer.util.CpachaUtil;
import com.ldy.programmer.util.MenuUtil;


@Controller
@RequestMapping("/system")
public class SystemController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private LogService logService;
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model,HttpServletRequest request) {
		List<Menu> userMenus = (List<Menu>)request.getSession().getAttribute("userMenus");
		
		model.addObject("topMenuList", MenuUtil.getAllTopMenu(userMenus));
		model.addObject("secondMenuList", MenuUtil.getAllSecondMenu(userMenus));
		model.setViewName("system/index");
		
		return model;
	}
	
	
	@RequestMapping(value="/welcome", method = RequestMethod.GET)
	public ModelAndView welcome(ModelAndView model) {
		model.setViewName("system/welcome");
		return model;
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView model) {
		model.setViewName("system/login");
		return model;
	}
	
	
	/*
	 * username:username,password:password,cpacha:cpacha
	 * 
	 */
	@RequestMapping(value="/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> loginAction(User user, String cpacha, HttpServletRequest request) {
		Map<String,String> result = new HashedMap();
		
		if(user == null) {
			result.put("type","error");
			result.put("msg","请输入用户信息！");
			return result;
		}
		if(StringUtils.isEmpty(cpacha)) {
			result.put("type","error");
			result.put("msg","请输入验证码！");
			return result;
		}
		if(StringUtils.isEmpty(user.getUsername())) {
			result.put("type","error");
			result.put("msg","请输入用户名！");
			return result;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			result.put("type","error");
			result.put("msg","请输入用户密码！");
			return result;
		}
	
		Object loginCpacha = request.getSession().getAttribute("loginCpacha");
		if(loginCpacha == null) {
			result.put("type","error");
			result.put("msg","验证码超时，请刷新页面！");
			return result;
		}
		if(!StringUtils.equalsIgnoreCase(loginCpacha.toString(), cpacha)){
			result.put("type","error");
			result.put("msg","验证码不正确，请重新输入！！");
			return result;
		}
		
		User findUser = userService.findByUsername(user.getUsername());
		if(findUser == null) {
			result.put("type","error");
			result.put("msg","用户名不存在！");
			return result;
		}
		if(!StringUtils.equals(findUser.getPassword(), user.getPassword())) {
			result.put("type","error");
			result.put("msg","用户名密码不正确！");
			return result;
		}

		Role role = roleService.find(findUser.getRoleId());
		List<Authority> authorityList = authorityService.findListByRoleId(role.getId());//根据角色获取权限列表
		String menuIds = "";
		for(Authority authority:authorityList){
			menuIds += authority.getMenuId() + ",";
		}
		
		if(!StringUtils.isEmpty(menuIds)){
			menuIds = menuIds.substring(0,menuIds.length()-1);
		}
		List<Menu> userMenus = menuService.findListByIds(menuIds);
		
		
		//把角色信息、菜单信息放到session中
		request.getSession().setAttribute("admin", findUser);
		request.getSession().setAttribute("role", role);
		request.getSession().setAttribute("userMenus", userMenus);
		result.put("type", "success");
		result.put("msg", "登录成功！");
		logService.add("用户名为{"+user.getUsername()+"}，角色为{"+role.getName()+"}的用户登录成功!");
		return result;
		
	}
	
	
	/*
	 * 
	 * get_cpacha?vl=4&w=150&h=40&type=loginCpacha
	 */
	@RequestMapping("/get_cpacha")
	public void getCpacha(
			@RequestParam(name="vl",required = false, defaultValue = "4")Integer vcodeLen,
			@RequestParam(name="w",required = false, defaultValue = "100")Integer width,
			@RequestParam(name="h",required = false, defaultValue = "30")Integer height,
			@RequestParam(name="type",required = false, defaultValue = "loginCpacha")String cpachaType,
			HttpServletRequest request,
			HttpServletResponse response
			) {
		
		CpachaUtil cpachaUtil =  new CpachaUtil(vcodeLen, width, height);
		String generatorVCode = cpachaUtil.generatorVCode();
		
		request.getSession().setAttribute(cpachaType,generatorVCode);
		
		BufferedImage generatorVCodeImage = cpachaUtil.generatorVCodeImage(generatorVCode, true);
		
		try {
			ImageIO.write(generatorVCodeImage,"gif",response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 *退出登录
	 * 
	 */
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("admin");
		session.setAttribute("admin", null);
		session.setAttribute("role", null);
		session.setAttribute("userMenus", null);
		logService.add("用户名为{"+user.getUsername()+"}的用户退出登录!");
		return "redirect:login";
	}
	
	@RequestMapping(value="/edit_password", method = RequestMethod.GET)
	public ModelAndView editPassword(ModelAndView model,HttpServletRequest request) {
		model.setViewName("/system/edit_password");
		return model;
	}
	
	/*
	 * 修改密码
	 * newpassword:$("#newPassword").val(),oldpassword:$("#oldPassword").val()	
	 */
	@ResponseBody
	@RequestMapping(value="/edit_password", method = RequestMethod.POST)
	public Map<String,String> editPasswordAction(HttpServletRequest request, String newpassword,String oldpassword) {
		
		Map<String,String> result = new HashedMap();
		
		User user = (User) request.getSession().getAttribute("admin");
		
		if(user == null) {
			result.put("type","error");
			result.put("msg","请先登录用户！");
			return result;
		}

		if(StringUtils.isEmpty(newpassword)) {
			result.put("type","error");
			result.put("msg","请输入新的密码！");
			return result;
		}
		if(!StringUtils.equals(oldpassword, user.getPassword())) {
			result.put("type","error");
			result.put("msg","用户原密码错误，请重新输入！");
			return result;
		}
		user.setPassword(newpassword);
		
		if(userService.edit(user) <= 0) {
			result.put("type","error");
			result.put("msg","修改失败，请联系管理员！");
			return result;
		}
		
		result.put("type", "success");
		result.put("msg", "密码修改成功！");
		logService.add("用户名为{"+user.getUsername()+"}的用户修改密码成功!");
		return result;
	}
	
	
}
