package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.enums.AuditType;
import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/** 随访记录表. */
@Entity
public class Planning extends Model {
	// 所属部门
	@Reference(ignoreMissing = false)
	public Department department;
	// 年初人口数
	public int beginPopulation;
	// 年末人口数
	public int endPopulation;
	// 男职工数
	public int maleWorker;
	// 女职工数
	public int femaleWorker;
	// 育龄妇女数
	public int childBearingAge;
	// 已婚育龄妇女数
	public int marriedChildBearingAge;
	// 已婚未育妇女数
	public int marriedNotBrood;
	// 一孩妇女数
	public int childWomens;
	// 环扎数
	public int cerclage;
	// 环扎率
	public float cerclageRate;
	// 领独生子女证人数
	public int childCard;
	// 二孩及以上妇女数
	public int childrenWomens;
	// 放环数
	public int putRing;
	// 放环率
	public float putRingRate;
	// 结扎数
	public int ligation;
	// 结扎率
	public float ligationRate;
	// 出生总数
	public int bornTotal; 
	// 一孩
	public int child;
	// 二孩及以上
	public int children;
	// 女性初婚
	public int womenFirstMarriage;
	// 23岁以上女性人数
	public int womens23years;
	// 男性初婚
	public int menFirstMarriage;
	// 25岁以上男性人数
	public int men25years;
	// 期末选用各种避孕人数
	public int finalSelection;
	// 男扎
	public int finalMaleFirm;
	// 女扎
	public int finalFemaleFirm;
	// 放环
	public int finalPutRing;
	// 皮埋
	public int finalSkinBuried;
	// 用套
	public int finalCondoms;
	// 外用
	public int finalExternal;
	// 其他
	public int finalOther;
	// 现孕
	public int now;
	// 一孩
	public int nowChild;
	// 二孩及以上
	public int nowChildren;
	// 生在今年
	public int bornThisYear;
	// 生在下年
	public int bornNextYear;
	// 本期施行计划生育手术例数
	public int operation;
	// 男扎
	public int operationMaleFirm;
	// 女扎
	public int operationFemaleFirm;
	// 放环
	public int operationPutRing;
	// 取环
	public int operationTakeRing;
	// 人工流产
	public int operationAbortion;
	// 引产
	public int operationInduced;
	// 汉族独生子女领证率
	public float nationality;
	// 综合节育率
	public float comprehensive; 
	// 晚婚率
	public float lastMarriage;
	// 晚育率
	public float lastPregnant;
	// 单位负责人
	public String charge;
	// 填表人
	public Administrator preparer;
	// 创建时间
	public Date createAt;
	// 修改时间
	public Date modifyAt;
	// 审核状态
	public AuditType status;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("beginPopulation", this.beginPopulation + "");
		result.put("endPopulation", this.endPopulation + "");
		result.put("maleWorker", this.maleWorker + "");
		result.put("femaleWorker", this.maleWorker + "");
		result.put("childBearingAge", this.childBearingAge + "");
		result.put("marriedChildBearingAge", this.marriedChildBearingAge + "");
		result.put("marriedNotBrood", this.marriedNotBrood + "");
		result.put("childWomens", this.childWomens + "");
		result.put("cerclage", this.cerclage + "");
		result.put("cerclageRate", this.cerclageRate + "");
		result.put("childCard", this.childCard + "");
		result.put("childrenWomens", this.childrenWomens + "");
		result.put("putRing", this.putRing + "");
		result.put("putRingRate", this.putRingRate + "");
		result.put("ligation", this.ligation + "");
		result.put("ligationRate", this.ligationRate + "");
		result.put("bornTotal", this.bornTotal + ""); 
		result.put("child", this.child + "");
		result.put("children", this.children + "");
		result.put("womenFirstMarriage", this.womenFirstMarriage + "");
		result.put("womens23years", this.womens23years + "");
		result.put("menFirstMarriage", this.menFirstMarriage + "");
		result.put("men25years", this.men25years + "");
		result.put("finalSelection", this.finalSelection + "");
		result.put("finalMaleFirm", this.finalMaleFirm + "");
		result.put("finalFemaleFirm", this.finalFemaleFirm + "");
		result.put("finalPutRing", this.finalPutRing + "");
		result.put("finalSkinBuried", this.finalSkinBuried + "");
		result.put("finalCondoms", this.finalCondoms + "");
		result.put("finalExternal", this.finalExternal + "");
		result.put("finalOther", this.finalOther + "");
		result.put("now", this.now + "");
		result.put("nowChild", this.nowChild + "");
		result.put("nowChildren", this.nowChildren + "");
		result.put("bornThisYear", this.bornThisYear + "");
		result.put("bornNextYear", this.bornNextYear + "");
		result.put("operation", this.operation + "");
		result.put("operationMaleFirm", this.operationMaleFirm + "");
		result.put("operationFemaleFirm", this.operationFemaleFirm + "");
		result.put("operationPutRing", this.operationPutRing + "");
		result.put("operationTakeRing", this.operationTakeRing + "");
		result.put("operationAbortion", this.operationAbortion + "");
		result.put("operationInduced", this.operationInduced + "");
		result.put("nationality", this.nationality + "");
		result.put("comprehensive", this.comprehensive + ""); 
		result.put("lastMarriage", this.lastMarriage + "");
		result.put("lastPregnant", this.lastMarriage + "");
		result.put("charge", this.charge + "");
		result.put("status", this.status);
		result.put("statusName", this.status == AuditType.AUDIT ? "审核中" : this.status == AuditType.PASS ? "审核通过" : "未通过");
		result.put("preparer", this.preparer.serialize());
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
