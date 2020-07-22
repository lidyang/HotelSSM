package com.ldy.programmer.controller.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ldy.programmer.entity.admin.Authority;
import com.ldy.programmer.entity.admin.Menu;
import com.ldy.programmer.entity.admin.Role;
import com.ldy.programmer.entity.admin.User;
import com.ldy.programmer.service.admin.AuthorityService;
import com.ldy.programmer.service.admin.LogService;
import com.ldy.programmer.service.admin.MenuService;
import com.ldy.programmer.service.admin.RoleService;
import com.ldy.programmer.util.MenuUtil;
import com.ldy.programmer.util.Page;

@RequestMapping("/admin/menu")
@Controller
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private LogService logService;
	
	@RequestMapping("/list")
	public ModelAndView index(ModelAndView model,HttpServletRequest request) {
		List<Menu> topList = menuService.findTopList();
		model.addObject("topList",topList);
		model.setViewName("/menu/list");
		return model;
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String,String> addMenu(Menu menu,HttpServletRequest request) {
		Map<String,String> result = new HashMap();
		if(menu == null) {
			result.put("type","error");
			result.put("msg","请填写菜单信息！");
			return result;
		}
		System.out.println(menu.toString());
		if(StringUtils.isEmpty(menu.getName())) {
			result.put("type","error");
			result.put("msg","请填写菜单名称！");
			return result;
		}
		if(StringUtils.isEmpty(menu.getIcon())) {
			result.put("type","error");
			result.put("msg","请选择菜单图标！");
			return result;
		}
		if(menuService.add(menu) <= 0) {
			result.put("type","error");
			result.put("msg","添加失败，请联系管理员！");
			return result;
		}
		
		User user = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+user.getUsername()+"}的用户添加菜单：" + menu.getName());
		
		result.put("type","success");
		result.put("msg","添加成功");

		return result;
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public Map<String,String> editMenu(Menu menu, HttpServletRequest request) {
		Map<String,String> result = new HashMap();
		if(menu == null) {
			result.put("type","error");
			result.put("msg","请填写菜单信息！");
			return result;
		}
		System.out.println(menu.toString());
		if(StringUtils.isEmpty(menu.getName())) {
			result.put("type","error");
			result.put("msg","请填写菜单名称！");
			return result;
		}
		if(StringUtils.isEmpty(menu.getIcon())) {
			result.put("type","error");
			result.put("msg","请选择菜单图标！");
			return result;
		}
		if(menuService.edit(menu) <= 0) {
			result.put("type","error");
			result.put("msg","修改失败，请联系管理员！");
			return result;
		}
		
		User user = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+user.getUsername()+"}的用户修改菜单：" + menu.getName());
		
		result.put("type","success");
		result.put("msg","修改成功");
		return result;
	}
	
	/*
	 * 删除菜单
	 */
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(HttpServletRequest request,@RequestParam(name = "id",required = true) Long id) {
		Map<String,Object> result = new HashMap<String, Object>();
		
		List<Menu> childernList = menuService.findChildernList(id);
		if(childernList != null && childernList.size()>0) {
			result.put("type","error");
			result.put("msg","该分类下存在子类，不能删除");
			return result;
		}
		if(menuService.delete(id)<=0) {
			result.put("type","error");
			result.put("msg","删除失败，请联系管理员！");
			return result;
		}
		
		User user = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+user.getUsername()+"}的用户删除菜单：" + id);
		
		result.put("type","success");
		result.put("msg","删除成功");
		return result;
	}
	
	
	
	/*
	 * 获取菜单列表，有过滤条件
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> list(Page page, @RequestParam(name = "name",required = false, defaultValue = "")String name) {
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		
		queryMap.put("offsize",page.getOffset());
		queryMap.put("pageSize",page.getRows());
		queryMap.put("name",name);
		List<Menu> findList = menuService.findList(queryMap);
		result.put("rows",findList);
		result.put("total", findList.size());
		return result;
		
	}
	
	
	/*
	 * 获取顶级菜单列表
	 */
	@RequestMapping(value="/topList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> topList() {
		System.out.println("/top list get");
		Map<String,Object> result = new HashMap<String, Object>();
		List<Menu> topList = menuService.findTopList();
		result.put("topList",topList);
		result.put("total", topList.size());
		return result;
		
	}
	
	/*
	 * 获取子菜单列表
	 */
	@RequestMapping(value="/childrenList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> childrenList(@RequestParam(name="name",required = true)Long parentId) {
		System.out.println("/childrenList get");
		Map<String,Object> result = new HashMap<String, Object>();
		List<Menu> childrenList = menuService.findChildernList(parentId);
		result.put("childrenList",childrenList);
		result.put("total", childrenList.size());
		return result;
		
	}
	
	@RequestMapping(value="get_icons", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getIconList(HttpServletRequest request){
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		String realPath = request.getServletContext().getRealPath("/");
		String filePath = realPath + "resources/admin/easyui/css/icons";
		System.out.println("filePath: " + filePath);
		File file = new File(filePath);
		if(!file.exists()) {
			result.put("type","error");
			result.put("msg","图标文件不存在！");
			return result;
		}
		
		File[] listFiles = file.listFiles();
		List<String> iconList = new ArrayList<>();
		
		for (File f : listFiles) {
			System.out.println("file :" + f.getName());
			if(f != null && f.getName().contains(".png")) {
				iconList.add("icon-" + f.getName().substring(0,f.getName().indexOf(".")).replace('_', '-'));
			}
		}	
		result.put("type","success");
		result.put("content",iconList);
		return result;
		
		
	}
	
	

}
