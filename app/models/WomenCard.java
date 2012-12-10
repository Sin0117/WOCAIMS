package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
/** 育龄妇女基本信息卡. */
@Entity
public class WomenCard extends Model {
	// 育龄妇女编码
	public String womanCode;
	// 户编码
	public String houseCode;
	// 妻子姓名
	public String woman;
	// 丈夫姓名
	public String man;
	// 出生日期
	public Date womanBirth;
	// 丈夫出生日期
	public Date manBirth;
	// 婚姻状况
	public String womanMarriage;
	// 男方婚姻状况
	public String manMarriage;
	// 初婚日期
	public Date womanFirstMarriage;
	// 男方初婚日期
	public Date manFirstMarriage;
	// 现婚日期
	public Date womanCurrentMarriage;
	// 男方现婚日期
	public Date manCurrentMarriage;
	// 职业
	public String womanProfession;
	// 男方职业
	public String manProfession;
	// 用工性质
	public String womanNature;
	// 男方用工性质
	public String manNature;
	// 民族
	public String womanNation;
	// 男方民族
	public String manNation;
	// 文化程度
	public String womanCulture;
	// 男方文化程度
	public String manCulture;
	// 户口性质
	public String womanProperties;
	// 男方户口性质
	public String manProperties;
	// 户籍
	public String womanRegister;
	// 男方户籍
	public String manRegister;
	// 居住地址
	public String womanLive;
	// 男方居住地址
	public String manLive;
	// 居住性质
	public String womanLiveNature;
	// 男方居住性质
	public String manLiveNature;
	// 流动性质
	public String womanFlowNature;
	// 男方流动性质
	public String manFlowNature;
	// 流动日期
	public Date womanFlowDate;
	// 男方流动日期
	public Date manFlowDate;
	// 流出地址
	public String womanFlowAddress;
	// 男方流出地址
	public String manFlowAddress;
	// 单位
	@Reference(ignoreMissing = false)
	public Department department;
	// 男方工作单位
	public String manWork;
	// 电话
	public String womanTel;
	// 男方电话
	public String manTel;
	// 身份证号
	public String womanIdentification;
	// 男方身份证号
	public String manIdentification;
	
	// 子女
	@Reference(ignoreMissing = true)
	public List<BirthStatus> childrens;
	// 节育措施
	@Reference(ignoreMissing = true)
	public Oligogenics oligogenics;
	// 独生子女
	@Reference(ignoreMissing = true)
	public OnlyChildren onlyChildren;
	
	// 创建日期 
	public Date createAt;
	// 修改时间
	public Date modifyAt;
	// 备注
	public String notes;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> onlyChildren = this.onlyChildren != null ? this.onlyChildren.serialize() : null;
		Map<String, Object> oligogenics = this.oligogenics != null ? this.oligogenics.serialize() : null;
		
		result.put("oligogenics", oligogenics);
		if (oligogenics != null) {
			for (String key : oligogenics.keySet()) {
				result.put(key, oligogenics.get(key));
			}
		}
		result.put("onlyChildren", onlyChildren);
		if (onlyChildren != null) {
			for (String key : onlyChildren.keySet()) {
				result.put(key, onlyChildren.get(key));
			}
		}
		List<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
		if (childrens != null && childrens.size() > 0) {
			for (BirthStatus children : childrens)
				childrenList.add(children.serialize());
			result.put("childrens", childrenList);
		}
		result.put("childrens", childrenList);
		result.put("id", this.getId().toString());
		result.put("womanLive", this.womanLive);
		result.put("manLive", this.manLive);
		result.put("woman", this.woman);
		result.put("man", this.man);
		result.put("womanProfession", this.womanProfession);
		result.put("manProfession", this.manProfession);
		result.put("womanNation", this.womanNation);
		result.put("manNation", this.manNation);
		result.put("womanFlowAddress", this.womanFlowAddress);
		result.put("manFlowAddress", this.manFlowAddress);
		result.put("manWork", this.manWork);
		result.put("womanWork", this.womanFlowNature);
		result.put("manFlowNature", this.manFlowNature);
		result.put("womanLiveNature", this.womanLiveNature);
		result.put("manLiveNature", this.manLiveNature);
		result.put("womanRegister", this.womanRegister);
		result.put("manRegister", this.manRegister);
		result.put("womanProperties", this.womanProperties);
		result.put("manProperties", this.manProperties);
		result.put("womanCulture", this.womanCulture);
		result.put("manCulture", this.manCulture);
		result.put("womanNature", this.womanNature);
		result.put("manNature", this.manNature);
		result.put("womanMarriage", this.womanMarriage);
		result.put("manMarriage", this.manMarriage);
		result.put("houseCode", this.houseCode);
		result.put("womanCode", this.womanCode);
		
		result.put("womanTel", this.womanTel);
		result.put("manTel", this.manTel);
		result.put("womanIdentification", this.womanIdentification);
		result.put("manIdentification", this.manIdentification);
		result.put("notes", this.notes);
		result.put("department", this.department != null ? this.department.serialize() : "");
		result.put("departmentName", this.department != null ? this.department.name : "");
		result.put("departmentCode", this.department != null ? this.department.code : "");
		
		if (this.oligogenics != null) {
			result.put("measure", this.oligogenics.measure != null ? this.oligogenics.measure : "");
			result.put("measureDate", this.oligogenics.measureDate != null ? utils.Utils.formatDate(this.oligogenics.measureDate) : "");
		} else {
			result.put("measure", "");
			result.put("measureDate", "");
		}
		result.put("hoursCode", this.houseCode);
		
		result.put("womanFlowDate", this.womanFlowDate == null ? "" : utils.Utils.formatDate(this.womanFlowDate));
		result.put("manFlowDate", this.manFlowDate == null ? null : utils.Utils.formatDate(this.manFlowDate));
		result.put("womanBirth", this.womanBirth == null ? "" : utils.Utils.formatDate(this.womanBirth));
		result.put("manBirth", this.manBirth == null ? null : utils.Utils.formatDate(this.manBirth));
		result.put("womanFirstMarriage", this.womanFirstMarriage == null ? "" : utils.Utils.formatDate(this.womanFirstMarriage));
		result.put("manFirstMarriage", this.manFirstMarriage == null ? "" : utils.Utils.formatDate(this.manFirstMarriage));
		result.put("womanCurrentMarriage", this.womanCurrentMarriage == null ? "" : utils.Utils.formatDate(this.womanCurrentMarriage));
		result.put("manCurrentMarriage", this.manCurrentMarriage == null ? "" : utils.Utils.formatDate(this.manCurrentMarriage));
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
