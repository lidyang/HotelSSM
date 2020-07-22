package com.ldy.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import com.ldy.programmer.entity.admin.User;

public interface UserDao {
	
	User findByUsername(String username);
	
	int  add(User user);
	
	int edit(User user);
	
	int editPassword(User user);
	
	int delete(String ids);
	
	
	List<User> findList(Map<String,Object> queryMap);
	
	int getTotal(Map<String,Object> queryMap);

}
