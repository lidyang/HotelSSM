package com.ldy.programmer.service.admin.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldy.programmer.dao.admin.AuthorityDao;
import com.ldy.programmer.dao.admin.LogDao;
import com.ldy.programmer.dao.admin.RoleDao;
import com.ldy.programmer.entity.admin.Authority;
import com.ldy.programmer.entity.admin.Log;
import com.ldy.programmer.entity.admin.Role;
import com.ldy.programmer.service.admin.AuthorityService;
import com.ldy.programmer.service.admin.LogService;
import com.ldy.programmer.service.admin.RoleService;
import com.ldy.programmer.util.DateUtil;

@Service
public class LogServiceImpl implements LogService {
	
	@Autowired
	private LogDao logDao;

	@Override
	public int add(Log log) {
		// TODO Auto-generated method stub
		return logDao.add(log);
	}
	
	@Override
	public void add(String content) {
		// TODO Auto-generated method stub
		
		Log log = new Log();
		Date date = new Date();
		
		log.setCreateTime(date);
		log.setContent(DateUtil.dateToStr(date) + "  " + content);
		
		logDao.add(log);
	}


	@Override
	public int delete(String ids) {
		// TODO Auto-generated method stub
		return logDao.delete(ids);
	}

	@Override
	public List<Log> findList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return logDao.findList(queryMap);
	}

	@Override
	public int getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
