package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/** 独生子女领证及奖励情况花名册. */
@Entity
public class OnlyChildren extends Model {
	// 所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 男方姓名
	public String man;
	// 女方姓名
	public String woman;
	// 民族
	public String nation;
	// age of woman;
	public int age;
	// 出生日期
	public Date birth;
	// 措施
	public String measure;
	// 领证时间
	public Date date;
	// 领证号码
	public String onlyChildrenCode;
	// 奖励兑现情况
	public String cash;
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
		result.put("age", this.age);
		result.put("onlyChildrenCode", this.onlyChildrenCode);
		result.put("measure", this.measure);
		result.put("cash", this.cash);
		result.put("notes", this.notes);
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("birth", this.birth == null ? "" : utils.Utils.formatDate(this.birth));
		result.put("date", this.date == null ? "" : utils.Utils.formatDate(this.date));
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
