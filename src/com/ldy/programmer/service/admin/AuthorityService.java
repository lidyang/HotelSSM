package com.ldy.programmer.service.admin;

import java.util.List;
import java.util.Map;

import com.ldy.programmer.entity.admin.Authority;
import com.ldy.programmer.entity.admin.Role;

public interface AuthorityService {
	
	int add(Authority authority);
	
	
	int deleteByRoleId(Long roleId);
	
	List<Authority> findListByRoleId(Long roleId);

}

