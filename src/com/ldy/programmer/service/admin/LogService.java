package com.ldy.programmer.service.admin;

import java.util.List;
import java.util.Map;

import com.ldy.programmer.entity.admin.Authority;
import com.ldy.programmer.entity.admin.Log;
import com.ldy.programmer.entity.admin.Role;

public interface LogService {
	
	int add(Log log);
	
	void add(String content);


	int delete(String ids);

	List<Log> findList(Map<String,Object> queryMap);

	int getTotal(Map<String,Object> queryMap);

	




}

