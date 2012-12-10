package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.enums.DepartmentLevel;
import play.modules.morphia.Model;
import play.mvc.Util;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/** 部门表. */
@Entity
public class Department extends Model {
	// 部门名称
	public String name;
	// 部门编码
	public String code;
	// 部门类型
	public DepartmentLevel type;
	// 部门所属(上级)
	@Reference(ignoreMissing = true)
	public Department parent;
	// 创建时间
	public Date createAt;
	
	@Util
	public String getDepartmentName() {
		if (this.type == DepartmentLevel.SECTION)
			return "科级部门";
		if (this.type == DepartmentLevel.UNION)
			return "总工会";
		if (this.type == DepartmentLevel.DIVISION)
			return "处级部门";
		if (this.type == DepartmentLevel.DIRECTOR)
			return "机关办事";
		return null;
	}
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("name", this.name);
		result.put("code", this.code);
		result.put("type", this.type.toString());
		result.put("typeName", getDepartmentName());
		result.put("parent", this.parent != null ? this.parent.serialize() : null);
		result.put("parentName", this.parent != null ? this.parent.name : null);
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		return result;
	}
}
