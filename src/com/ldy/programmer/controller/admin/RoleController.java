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
import com.ldy.programmer.util.Page;


@RequestMapping("/admin/role")
@Controller
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private LogService logService;

	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("/role/list");
		return model;
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String,String> add(HttpServletRequest request, Role role) {
		Map<String,String> result = new HashMap();
		if(role == null) {
			result.put("type","error");
			result.put("msg","请填写角色信息！");
			return result;
		}
		if(StringUtils.isEmpty(role.getName())) {
			result.put("type","error");
			result.put("msg","请填写角色名称！");
			return result;
		}
		if(roleService.add(role) <= 0) {
			result.put("type","error");
			result.put("msg","添加失败，请联系管理员！");
			return result;
		}
		
		User user = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+user.getUsername()+"}的用户添加角色：" + role.getName());
		
		result.put("type","success");
		result.put("msg","添加成功");
		return result;
	}
	
	/*
	 * 删除菜单
	 */
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@RequestParam(name = "id",required = true) Long id, HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String, Object>();
		
		if(id == null  ) {
			result.put("type","error");
			result.put("msg","请选择要删除的角色！");
			return result;
		}
		
		try {
			if(roleService.delete(id)<=0) {
				result.put("type","error");
				result.put("msg","删除失败，请联系管理员！");
				return result;
			}
		} catch (Exception e) {
			result.put("type","error");
			result.put("msg","删除失败，角色存在用户信息！");
			return result;
		}
		
		User user = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+user.getUsername()+"}的用户删除角色：" + id);
		
		result.put("type","success");
		result.put("msg","角色删除成功");
		return result;
	}
	

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public Map<String,String> edit(Role role, HttpServletRequest request) {
		Map<String,String> result = new HashMap();
		if(role == null) {
			result.put("type","error");
			result.put("msg","请填写角色信息！");
			return result;
		}
		if(StringUtils.isEmpty(role.getName())) {
			result.put("type","error");
			result.put("msg","请填写角色名称！");
			return result;
		}
		if(roleService.edit(role) <= 0) {
			result.put("type","error");
			result.put("msg","修改失败，请联系管理员！");
			return result;
		}
		User user = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+user.getUsername()+"}的用户修改角色：" + role.getName());
		
		result.put("type","success");
		result.put("msg","修改成功");
		return result;
	}
	
	
	
	/*
	 * 获取菜单列表，有过滤条件
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> findList(Page page, @RequestParam(name = "name",required = false, defaultValue = "")String name) {
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		
		queryMap.put("offsize",page.getOffset());
		queryMap.put("pageSize",page.getRows());
		queryMap.put("name",name);
		
		List<Role> findList = roleService.findList(queryMap);
		
		result.put("rows",findList);
		result.put("total", findList.size());
		
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
	
	
	/*
	 * 添加对应角色的权限
	 */
	@RequestMapping(value="/add_authority", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addAuthority(HttpServletRequest request,
			@RequestParam(name = "ids",required = false, defaultValue = "")String ids,
			@RequestParam(name = "roleId",required = true)Long roleId) {

		Map<String,Object> result = new HashMap<String, Object>();
		
		if(StringUtils.isEmpty(ids)){
			result.put("type", "error");
			result.put("msg", "请选择相应的权限！");
			return result;
		}
		if(roleId == null){
			result.put("type", "error");
			result.put("msg", "请选择相应的角色！");
			return result;
		}
		System.out.println("ids: " + ids);
		if(ids.contains(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		String[] idArr = ids.split(",");
		if(idArr.length > 0){
			authorityService.deleteByRoleId(roleId);
		}
		for(String id:idArr){
			Authority authority = new Authority();
			authority.setMenuId(Long.valueOf(id));
			authority.setRoleId(roleId);
			authorityService.add(authority);
		}
		updateMenu(request);
		
		result.put("type", "success");
		result.put("msg", "权限编辑成功！");
		return result;
		
	}
	
	private void updateMenu(HttpServletRequest request){
		User sessionUser = (User) request.getSession().getAttribute("admin");
		if(sessionUser != null) {
			Role role = roleService.find(sessionUser.getRoleId());
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
			request.getSession().setAttribute("userMenus", userMenus);
			
		}
		
	}
	
	
	/*
	 * 获取角色对应的权限列表
	 */
	@RequestMapping(value="/get_role_authority", method = RequestMethod.POST)
	@ResponseBody
	public List<Authority> getRoleAuthority(@RequestParam(name = "roleId",required = true)Long roleId) {
		
		System.out.println("getRoleAuthority");
		Map<String,Object> result = new HashMap<String, Object>();
		
		
		List<Authority> findList = authorityService.findListByRoleId(roleId);
		
		
		return findList;
		
	}
	
	/*
	 * 获取所有菜单信息
	 */
	@RequestMapping(value="/get_all_menu", method = RequestMethod.POST)
	@ResponseBody
	public List<Menu> getAllMenu() {
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("offsize",0);
		queryMap.put("pageSize",9999);
		return menuService.findList(queryMap);
		
	}

	

}
