package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
/** 本期避孕措施登记表. */
@Entity
public class Oligogenics extends Model {
	// 所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 男方姓名
	public String man;
	// 女方姓名
	public String woman;
	// 民族
	public String nation;
	// 孩次
	public int size;
	// 措施
	public String measure;
	// 手术时间
	public Date measureDate;
	// 手术地点
	public String measureLocal;
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
		result.put("measure", this.measure);
		result.put("measureLocal", this.measureLocal);
		result.put("notes", this.notes);
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("measureDate", this.measureDate == null ? "" : utils.Utils.formatDate(this.measureDate));
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
