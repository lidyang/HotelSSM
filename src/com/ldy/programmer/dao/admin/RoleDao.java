package com.ldy.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import com.ldy.programmer.entity.admin.Role;
import com.ldy.programmer.entity.admin.User;

public interface RoleDao {
	
	
	int add(Role role);
	
	int edit(Role role);
	
	int delete(long id);
	
	Role find(Long id);
	
	List<Role> findList(Map queryMap);

}
