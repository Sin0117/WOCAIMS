package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/** 健康档案. */
@Entity
public class HealthArchives extends Model {
	// 体检编号
	public String code;
	// 姓名
	public String name;
	// 性别
	public String gender;
	// 年龄
	public int age;
	// 工作单位
	@Reference(ignoreMissing = false)
	public Department department;
	// 家庭地址
	public String home;
	// 联系电话
	public String tel;
	// 体检日期
	public Date dealthDate;
	// 报告日期
	public Date reportDate;
	// 体检地址
	public String dealthAddress;
	// 既往病史
	public String history;
	// 检查项
	@Reference(ignoreMissing = true)
	public List<Physical> physicals;
	// 创建时间
	public Date createAt;
	// 修改时间
	public Date modifyAt;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("name", this.name);
		result.put("code", this.code);
		result.put("gender", this.gender);
		result.put("age", this.age);
		result.put("home", this.home);
		result.put("tel", this.tel);
		result.put("dealthAddress", this.dealthAddress);
		result.put("history", this.history);
		List<Map<String, Object>> physicalList = new ArrayList<Map<String, Object>>();
		if (physicals != null && physicals.size() > 0) {
			for (Physical children : physicals)
				physicalList.add(children.serialize());
			result.put("physicals", physicalList);
		}
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("dealthDate", this.dealthDate == null ? "" : utils.Utils.formatDate(this.dealthDate));
		result.put("reportDate", this.reportDate == null ? "" : utils.Utils.formatDate(this.reportDate));
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
