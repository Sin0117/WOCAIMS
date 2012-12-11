package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

import play.modules.morphia.Model;
/** 出生及节育措施花名册. */
@Entity
public class BirthOligogenics extends Model {
	// 所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 流动性质
	public String flow;
	// 禁忌
	public String taboo;
	// 出生情况
	@Reference(ignoreMissing = false)
	public BirthStatus birthStatus;
	// 节育措施
	@Reference(ignoreMissing = false)
	public Oligogenics oligogenics;
	// 备注 
	public String notes;
	// 创建时间
	public Date createAt;
	// 修改时间
	public Date modifyAt;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> birthStatus = this.birthStatus != null ? this.birthStatus.serialize() : null;
		Map<String, Object> oligogenics = this.oligogenics != null ? this.oligogenics.serialize() : null;
		
		result.put("flow", this.flow);
		result.put("taboo", this.taboo);
		result.put("birthStatus", birthStatus);
		if (birthStatus != null) {
			for (String key : birthStatus.keySet()) {
				result.put(key, birthStatus.get(key));
			}
		}
		result.put("oligogenics", oligogenics);
		if (oligogenics != null) {
			for (String key : oligogenics.keySet()) {
				result.put(key, oligogenics.get(key));
			}
		}
		result.put("id", this.getId().toString());
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("notes", this.notes);
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
