package com.ldy.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import com.ldy.programmer.entity.admin.Authority;
import com.ldy.programmer.entity.admin.Role;
import com.ldy.programmer.entity.admin.User;

public interface AuthorityDao {
	
	
	int add(Authority authority);
	
	
	int deleteByRoleId(Long roleId);
	
	List<Authority> findListByRoleId(Long roleId);

	
	

}
