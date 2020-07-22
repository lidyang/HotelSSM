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
import org.springframework.web.servlet.ModelAndView;

import com.ldy.programmer.entity.admin.Authority;
import com.ldy.programmer.entity.admin.Log;
import com.ldy.programmer.entity.admin.Menu;
import com.ldy.programmer.entity.admin.Role;
import com.ldy.programmer.entity.admin.User;
import com.ldy.programmer.service.admin.AuthorityService;
import com.ldy.programmer.service.admin.LogService;
import com.ldy.programmer.service.admin.MenuService;
import com.ldy.programmer.service.admin.RoleService;
import com.ldy.programmer.util.Page;


@RequestMapping("/admin/log")
@Controller
public class LogController {
	
	@Autowired
	private LogService logService;
	

	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("/log/list");
		return model;
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String,String> add(HttpServletRequest request,Log log) {
		Map<String,String> result = new HashMap();
		if(log == null) {
			result.put("type","error");
			result.put("msg","请填写日志信息！");
			return result;
		}
		if(StringUtils.isEmpty(log.getContent())) {
			result.put("type","error");
			result.put("msg","请填写日志内容！");
			return result;
		}
		log.setCreateTime(new Date());
		if(logService.add(log) <= 0) {
			result.put("type","error");
			result.put("msg","添加失败，请联系管理员！");
			return result;
		}
		result.put("type","success");
		result.put("msg","添加成功");

		User user = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+user.getUsername()+"}的用户添加日志：" + log.getContent());
		return result;
	}
	
	/*
	 * 删除日志
	 */
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(HttpServletRequest request, @RequestParam(name = "ids",required = true) String ids) {
		Map<String,Object> result = new HashMap<String, Object>();
		
		if(ids == null  ) {
			result.put("type","error");
			result.put("msg","请选择要删除的日志！");
			return result;
		}
		int len = ids.length();
		try {
			if(logService.delete(ids.substring(0, len-1))<=0) {
				result.put("type","error");
				result.put("msg","删除失败，请联系管理员！");
				return result;
			}
		} catch (Exception e) {
			result.put("type","error");
			result.put("msg","日志删除失败！");
			return result;
		}
		
		User user = (User) request.getSession().getAttribute("admin");
		logService.add("用户名为{"+user.getUsername()+"}的用户删除日志：" + ids);
		
		result.put("type","success");
		result.put("msg","日志删除成功");
		return result;
	}
	

	
	/*
	 * 获取菜单列表，有过滤条件
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public List<Log> findList(Page page, @RequestParam(name = "content",required = false, defaultValue = "")String content) {
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		
		queryMap.put("offsize",page.getOffset());
		queryMap.put("pageSize",page.getRows());
		queryMap.put("content",content);
		
		List<Log> findList = logService.findList(queryMap);
		
		return findList;
		
	}
	

	
	

}
