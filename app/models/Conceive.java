package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
/** 现孕登记花名册. */
@Entity
public class Conceive extends Model {
	// 男方姓名
	public String man;
	// 女方姓名
	public String woman;
	// 所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 民族
	public String nation;
	// 女方出生日期
	public Date birth;
	// 怀孕时间
	public Date conceiveDate;
	// 孩次
	public int size;
	// 是否当年出生
	public boolean thisYear;
	// 计划内外
	public boolean plan;
	// 育龄妇女编码
	public String code;
	// 备注
	public String notes;
	// 创建时间
	public Date createAt;
	// 修改时间
	public Date modifyAt;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("man", this.man);
		result.put("woman", this.woman);
		result.put("nation", this.nation);
		result.put("size", this.size);
		result.put("thisYear", this.thisYear);
		result.put("thisYearTxt", this.thisYear ? "当年" : "次年");
		result.put("plan", this.plan);
		result.put("planTxt", this.plan ? "计划内" : "计划外");
		result.put("code", this.code);
		result.put("notes", this.notes);
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("birth", this.birth == null ? "" : utils.Utils.formatDate(this.birth));
		result.put("conceiveDate", this.conceiveDate == null ? "" : utils.Utils.formatDate(this.conceiveDate));
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
