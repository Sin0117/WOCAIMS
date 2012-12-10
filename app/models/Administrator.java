package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
/** 部门管理员员表. */
@Entity
public class Administrator extends Model {
	// 管理员所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 部门管理员登录名
	public String admin;
	// 部门管理员显示名称
	public String name;
	// 部门管理员密码
	public String password;
	// 是否有操作权限, 无权限为审核员
	public boolean isAdmin;
	// 角色创建时间
	public Date createAt;
	// 最后登录时间
	public Date lastLoginAt;

	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("name", this.name);
		result.put("login", this.admin);
		result.put("password", this.password);
		result.put("admin", this.isAdmin);
		result.put("type", this.isAdmin ? "干事" : "审核");
		result.put("department", this.department != null ? this.department.serialize() : null);
		result.put("departmentName", this.department != null ? this.department.name : null);
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		if (this.lastLoginAt != null)
			result.put("lastLoginAt", utils.Utils.formatDate(this.lastLoginAt));
		else
			result.put("lastLoginAt", "从未登录");
		return result;
	}
}
