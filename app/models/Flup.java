package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/** 随访记录表. */
@Entity
public class Flup extends Model {
	// 所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 随访职工姓名
	public String name;
	// 随访事由
	public String reason;
	// 随访内容
	public String content;
	// 随访日期
	public Date date;
	// 随访人员
	public String staff;
	// 创建时间
	public Date createAt;
	// 修改时间
	public Date modifyAt;
	//备注
	public String notes;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("name", this.name);
		result.put("reason", this.reason);
		result.put("content", this.content);
		result.put("staff", this.staff);
		result.put("notes", this.notes);
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("date", this.date == null ? "" : utils.Utils.formatDate(this.date));
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
