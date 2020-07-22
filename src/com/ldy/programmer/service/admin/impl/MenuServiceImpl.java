package com.ldy.programmer.service.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldy.programmer.dao.admin.MenuDao;
import com.ldy.programmer.dao.admin.UserDao;
import com.ldy.programmer.entity.admin.Menu;
import com.ldy.programmer.entity.admin.User;
import com.ldy.programmer.service.admin.MenuService;
import com.ldy.programmer.service.admin.UserService;



@Service
public class MenuServiceImpl implements  MenuService {

	@Autowired
	private MenuDao menuDao;


	@Override
	public int add(Menu menu) {
		// TODO Auto-generated method stub
		
		return menuDao.add(menu);
		
	}


	@Override
	public List<Menu> findList(Map queryMap) {
		// TODO Auto-generated method stub
		return menuDao.findList(queryMap);
	}


	@Override
	public List<Menu> findTopList() {
		// TODO Auto-generated method stub
		return menuDao.findTopList();
	}


	@Override
	public int edit(Menu menu) {
		// TODO Auto-generated method stub
		return menuDao.edit(menu);
	}


	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return menuDao.delete(id);
	}


	@Override
	public List<Menu> findChildernList(Long id) {
		// TODO Auto-generated method stub
		return menuDao.findChildernList(id);
	}


	@Override
	public List<Menu> findListByIds(String menuIds) {
		// TODO Auto-generated method stub
		return menuDao.findListByIds(menuIds);
	}
	
	
	
	
	
	
}
