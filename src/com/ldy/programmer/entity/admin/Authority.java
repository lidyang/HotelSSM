package com.ldy.programmer.entity.admin;

import java.io.Serializable;

import org.springframework.stereotype.Component;

/**
 * Ȩ��ʵ��
 * @author llq
 *
 */
@Component
public class Authority implements Serializable{
	private Long id;
	
	private Long roleId;//��ɫid
	
	private Long menuId;//�˵�id

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	
	
}