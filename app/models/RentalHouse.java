package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

import play.modules.morphia.Model;
/** 出租房屋登记表. */
@Entity
public class RentalHouse extends Model {
	// 所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 职工姓名
	public String name;
	// 出租房地址
	public String address;
	// 租房人姓名
	public String user;
	// 家庭人口
	public int size;
	// 孩次
	public int childrenSize;
	// 婚育证明
	public String childbirth;
	// 户籍地
	public String register;
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
		result.put("address", this.address);
		result.put("user", this.user);
		result.put("size", this.size);
		
		result.put("childrenSize", this.childrenSize);
		result.put("childbirth", this.childbirth);
		result.put("register", this.register);
		
		result.put("notes", this.notes);
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
