package com.ldy.programmer.controller.admin;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;
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
import com.ldy.programmer.util.Page;


@RequestMapping("/admin/user")
@Controller
public class UserController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private LogService logService;

	/*
	 * 用户list
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		System.out.println("list model");
		Map<String,Object> queryMap = new HashMap<String, Object>();
		List<Role> roleList = roleService.findList(queryMap);
		model.setViewName("/user/list");
		model.addObject("roleList", roleList);
		return model;
	}
	
	/*
	 * 添加用户
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String,String> add(HttpServletRequest request, User user) {
		System.out.println("add menu");
		Map<String,String> result = new HashMap();
		if(user == null) {
			result.put("type","error");
			result.put("msg","请填写用户信息！");
			return result;
		}
		if(StringUtils.isEmpty(user.getUsername())) {
			result.put("type","error");
			result.put("msg","请填写用户名！");
			return result;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			result.put("type","error");
			result.put("msg","请填写用户密码！");
			return result;
		}
		if(user.getRoleId() == null ) {
			result.put("type","error");
			result.put("msg","请选择用户角色！");
			return result;
		}
		
		User findUser = userService.findByUsername(user.getUsername());
		if(findUser != null  ) {
			result.put("type","error");
			result.put("msg","该用户名已经存在，请重新输入");
			return result;
		}
		
		if(userService.add(user) <= 0) {
			result.put("type","error");
			result.put("msg","添加失败，请联系管理员！");
			return result;
		}
		
		User loginUser = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+loginUser.getUsername()+"}的用户添加用户：" + user.getUsername());
		
		result.put("type","success");
		result.put("msg","添加成功");

		return result;
	}

	/*
	 * 删除用户
	 */
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(HttpServletRequest request, @RequestParam(name = "ids",required = true)String ids) {
		
		System.out.println("/delete POST");
		Map<String,Object> result = new HashMap<String, Object>();
		
		if(ids == null  ) {
			result.put("type","error");
			result.put("msg","请选择要删除的用户！");
			return result;
		}
		
		try {
			int len = ids.length();
			if(userService.delete(ids.substring(0,len-1))<=0) {
				result.put("type","error");
				result.put("msg","删除失败，请联系管理员！");
				return result;
			}
		} catch (Exception e) {
			result.put("type","error");
			result.put("msg","删除失败，请联系管理员！");
			return result;
		}
		
		User loginUser = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+loginUser.getUsername()+"}的用户删除用户：" + ids);
		
		result.put("type","success");
		result.put("msg","用户删除成功");
		return result;
	}
	

	/*
	 * 修改用户
	 */
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public Map<String,String> edit(HttpServletRequest request, User user) {
		Map<String,String> result = new HashMap();
		if(user == null) {
			result.put("type","error");
			result.put("msg","请填写用户信息！");
			return result;
		}
		if(StringUtils.isEmpty(user.getUsername())) {
			result.put("type","error");
			result.put("msg","请填写用户名！");
			return result;
		}
		
		if(user.getRoleId() == null ) {
			result.put("type","error");
			result.put("msg","请选择用户角色！");
			return result;
		}
		
		User findUser = userService.findByUsername(user.getUsername());
		if(findUser != null && !findUser.getId().equals(user.getId()) ) {
			result.put("type","error");
			result.put("msg","该用户名已经存在，请重新输入");
			return result;
		}
		
		if(userService.edit(user) <= 0) {
			result.put("type","error");
			result.put("msg","修改失败，请联系管理员！");
			return result;
		}
		
		User loginUser = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+loginUser.getUsername()+"}的用户修改用户：" + user.getUsername());
		
		result.put("type","success");
		result.put("msg","修改成功");

		return result;
	}

	
	
	/*
	 * 获取菜单列表，有过滤条件
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> findList(Page page, 	
			@RequestParam(name="username",required=false,defaultValue="") String username,
			@RequestParam(name="roleId",required=false) Long roleId,
			@RequestParam(name="sex",required=false) Integer sex
			){
		System.out.println("/list POST");
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		
		queryMap.put("offsize",page.getOffset());
		queryMap.put("pageSize",page.getRows());
		queryMap.put("username",username);
		queryMap.put("roleId",roleId);
		queryMap.put("sex",sex);
		
		List<User> findList = userService.findList(queryMap);
		result.put("rows",findList);
		result.put("total", findList.size());
		
		return result;
		
	}
	
//	upload_photo
	@RequestMapping(value="/upload_photo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> uploadPhoto(MultipartFile photo,HttpServletRequest request){
		Map<String,Object> result = new HashMap<String, Object>();

		if(photo == null ) {
			result.put("type","error");
			result.put("msg","请选择要上传的文件！");
			return result;
		}
		
		if(photo.getSize() > 1024*1024*1024) {
			result.put("type","error");
			result.put("msg","文件大小不能超过10M ！");
			return result;
		} 
		
		String originalFilename = photo.getOriginalFilename();
		System.out.println("originalFilename: "+originalFilename);
		String suffix = originalFilename.substring(originalFilename.lastIndexOf('.')+1);
		if(! "jpg,jpeg,png,gif".contains(suffix)) {
			result.put("type","error");
			result.put("msg","请选择jpg,jpeg,png,gif格式图片 ！");
			return result;
		}
		
		String savePath = request.getServletContext().getRealPath("/") + "resources/upload/";
		
		File savePathDir = new File(savePath);
		if(!savePathDir.exists()) {
			savePathDir.mkdir();
		}
		String filename = new Date().getTime() + "." + suffix;
		File saveFile = new File(savePath + filename);
		String fileUrl = request.getServletContext().getContextPath() + "/resources/upload/" + filename;
		
		try {
			photo.transferTo(saveFile);
		} catch (Exception e) {
			// TODO: handle exception
			result.put("type","error");
			result.put("msg","保存文件异常！");
			return result;
		}
		
		result.put("type","success");
		result.put("msg","文件上传成功");
		result.put("filepath",fileUrl);

		return result;
		
	}
	
	
	
	

}
