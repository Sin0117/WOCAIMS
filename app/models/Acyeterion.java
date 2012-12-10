package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/** 避孕药品发放登记表. */
@Entity
public class Acyeterion extends Model {
	// 所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 领取时间
	public Date date;
	// 领取数量
	public long size;
	// 领取人
	public String user;
	// 创建时间
	public Date createAt;
	// 修改时间
	public Date modifyAt;

	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("size", this.size);
		result.put("user", this.user);
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("date", this.date == null ? "" : utils.Utils.formatDate(this.date));
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
