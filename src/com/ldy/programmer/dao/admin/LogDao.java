package com.ldy.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import com.ldy.programmer.entity.admin.Authority;
import com.ldy.programmer.entity.admin.Log;
import com.ldy.programmer.entity.admin.Role;
import com.ldy.programmer.entity.admin.User;

public interface LogDao {
	
	
	int add(Log log);
	
	
	int delete(String ids);
	
	List<Log> findList(Map<String,Object> queryMap);

	int getTotal(Map<String,Object> queryMap);
	

}
