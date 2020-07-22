package com.ldy.programmer.service.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldy.programmer.dao.admin.RoleDao;
import com.ldy.programmer.entity.admin.Role;
import com.ldy.programmer.service.admin.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public int add(Role role) {
		// TODO Auto-generated method stub
		return roleDao.add(role);
	}

	@Override
	public int edit(Role role) {
		// TODO Auto-generated method stub
		return roleDao.edit(role);
	}

	@Override
	public int delete(Long id) {
		// TODO Auto-generated method stub
		return roleDao.delete(id);
	}

	@Override
	public List<Role> findList(Map queryMap) {
		// TODO Auto-generated method stub
		return roleDao.findList(queryMap);
	}

	@Override
	public Role find(Long id) {
		// TODO Auto-generated method stub
		return roleDao.find(id);
	}

}
