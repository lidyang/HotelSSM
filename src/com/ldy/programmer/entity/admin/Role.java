package com.ldy.programmer.entity.admin;

import java.io.Serializable;

import org.springframework.stereotype.Component;

/**
 * 角色role实体
 * @author llq
 *
 */
@Component
public class Role implements Serializable{
	
	private Long id;
	
	private String name;
	
	private String remark;//角色备注

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", remark=" + remark + "]";
	}
	
	
}
