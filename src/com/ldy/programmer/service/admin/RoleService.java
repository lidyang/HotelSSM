package com.ldy.programmer.service.admin;

import java.util.List;
import java.util.Map;

import com.ldy.programmer.entity.admin.Role;

public interface RoleService {
	
	int add(Role role);
	
	int edit(Role role);
	
	int delete(Long id);
	
	Role find(Long id);
	
	List<Role> findList(Map queryMap);
}
