package com.ldy.programmer.service.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldy.programmer.dao.admin.AuthorityDao;
import com.ldy.programmer.dao.admin.RoleDao;
import com.ldy.programmer.entity.admin.Authority;
import com.ldy.programmer.entity.admin.Role;
import com.ldy.programmer.service.admin.AuthorityService;
import com.ldy.programmer.service.admin.RoleService;

@Service
public class AuthorityServiceImpl implements AuthorityService {
	
	@Autowired
	private AuthorityDao authorityDao;

	@Override
	public int add(Authority authority) {
		// TODO Auto-generated method stub
		return authorityDao.add(authority);
	}

	@Override
	public int deleteByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return authorityDao.deleteByRoleId(roleId);
	}

	@Override
	public List<Authority> findListByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return authorityDao.findListByRoleId(roleId);
	}
	
	
}
