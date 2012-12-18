package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

import play.modules.morphia.Model;
/** 体检项检查异常结果汇总以及治疗建议. */
@Entity
public class PhysicalTreatment extends Model {
	// 体检所属档案
	@Reference(ignoreMissing = false)
	public HealthArchives health;
	// 所属检查项
	// @Reference(ignoreMissing = false)
	// public Physical physical;
	// 单位
	@Reference(ignoreMissing = false)
	public Department department;
	// 检查异常结果汇总
	public String summary;
	// 结论
	public String conclusion;
	// 治疗意见或建议
	public String treatment;
	// 健康咨询地址
	public String address;
	// 咨询电话
	public String phone;
	// 主检医生
	public String treatmentDoctor;
	// 创建时间
	public Date createAt;
	// 修改时间
	public Date modifyAt;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("health", this.health.getId());
		// result.put("physical", this.physical.getId());
		result.put("summary", this.summary);
		result.put("conclusion", this.conclusion);
		result.put("treatment", this.treatment);
		result.put("address", this.address);
		result.put("phone", this.phone);
		result.put("treatmentDoctor", this.treatmentDoctor);
		result.put("doctor", this.treatmentDoctor);
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
