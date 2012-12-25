package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/** 出生情况花名册. */
@Entity
public class BirthStatus extends Model {
	// 男方姓名
	public String man;
	// 女方姓名
	public String woman;
	// 所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 姓名
	public String name;
	// 民族
	public String nation;
	// 出生日期
	public Date birth;
	// 孩次
	public int size;
	// 性别
	public String gender;
	// 生育状况
	public String status;
	// 计划内外
	public boolean plan;
	// 计外原因
	public String planInfo;
	// 是否处罚
	public boolean punish;
	// 是否晚育
	public boolean late;
	// 是否存活
	public boolean survival;
	// 准生证号
	public String code;
	// 户口性质
	public String properties;
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
		result.put("name", this.name);
		result.put("nation", this.nation);
		result.put("size", this.size);
		result.put("gender", this.gender);
		result.put("survival", this.survival);
		result.put("survivalTxt", this.survival ? "存活" : "死亡");
		result.put("plan", this.plan);
		result.put("planTxt", this.plan ? "计划内" : "计划外");
		result.put("code", this.code);
		result.put("planInfo", this.planInfo);
		result.put("punish", this.punish);
		result.put("punishTxt", this.punish ? "处罚" : "未处罚");
		result.put("late", this.late);
		result.put("lateTxt", this.late ? "晚育" : "未晚育");
		result.put("properties", this.properties);
		result.put("notes", this.notes);
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("birth", this.birth == null ? "" : utils.Utils.formatDate(this.birth));
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
