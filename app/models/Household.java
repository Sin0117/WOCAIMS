package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
/** 户基本信息卡. */
@Entity
public class Household extends Model {
	// 单位
	@Reference(ignoreMissing = false)
	public Department department;
	// 地区属性
	public String live;
	// 建卡日期
	public Date createAt;
	// 修改日期
	public Date modifyAt;
	// 户编码
	public String code;
	// 户主姓名
	public String user;
	// 户籍
	public String register;
	// 本户人数
	public int peoples;
	// 育龄人数
	public int pregnant;
	// 少数民族人数
	public int minority;
	// 流入人口数
	public int into;
	// 流出人口数
	public int out;
	// 少数民族流入人口数
	public int minorityInto;
	// 少数民族流出人口数
	public int minorityOut;
	// 记事
	public String notes;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("live", this.live);
		result.put("code", this.code);
		result.put("user", this.user);
		result.put("register", this.register);
		result.put("peoples", this.peoples);
		result.put("pregnant", this.pregnant);
		result.put("minority", this.minority);
		result.put("into", this.into);
		result.put("out", this.out);
		result.put("minorityInto", this.minorityInto);
		result.put("minorityOut", this.minorityOut);
		result.put("notes", this.notes);
		result.put("department", this.department != null ? this.department.serialize() : null);
		result.put("departmentName", this.department != null ? this.department.name : null);
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
