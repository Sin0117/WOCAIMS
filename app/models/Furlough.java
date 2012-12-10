package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

import play.modules.morphia.Model;
/** 休长假职工情况登记表. */
@Entity
public class Furlough extends Model {
	// 所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 职工姓名
	public String name;
	// 性别
	public String gender;
	// 民族
	public String nation;
	// 出生年月
	public Date birth;
	// 节育情况
	public String measure;
	// 住址
	public String live;
	// 联系电话
	public String tel;
	// 创建时间
	public Date createAt;
	// 修改时间
	public Date modifyAt;
	// 备注
	public String notes;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("name", this.name);
		result.put("gender", this.gender);
		result.put("nation", this.nation);
		result.put("measure", this.measure);
		result.put("live", this.live);
		result.put("tel", this.tel);
		result.put("notes", this.notes);
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("birth", this.birth == null ? "" : utils.Utils.formatDate(this.birth));
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
