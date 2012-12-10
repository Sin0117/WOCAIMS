package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import play.mvc.Util;
import utils.Utils;
/** 健康档案管理 */
public class HealthArchives extends Controller {
	public static final int PAGE_SIZE = 20;
	/** 管理入口 .*/
	public static void index(String keyword, String department, int page, int size) {
		List<models.Department> divisions = Secure.getDepartments();
		render(keyword, department, divisions);
	}
	
	/** 导出excel. */
	public static void report(String keyword, String department) {
		String fileName = "健康档案-" + Secure.getAdmin().department.name + ".xls";
		renderBinary(Utils.toExcel(fileName), fileName);
	}
	
	/** 获取数据列表. */
	public static void list(String keyword, String code, String department, int page, int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		List<models.HealthArchives> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, code, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.HealthArchives.find();
				if (keyword != null && !"".equals(keyword))
					query.filter("name", keyword);
				if (code != null)
					query.filter("code", code);
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, code, page, rows);
			}
		}
		List<Object> datas = new ArrayList<Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (models.HealthArchives mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.HealthArchives.count());
		result.put("size", rows);
		renderText(result);
	}
	
	/** 获取当前操作者的全部数据. */
	private static List<models.HealthArchives> findAll(String keyword, String code, int page, int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.HealthArchives> result = new ArrayList<models.HealthArchives>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.HealthArchives.find("byDepartment", department);
			if (keyword != null && !"".equals(keyword))
				query.filter("name", keyword);
			if (code != null)
				query.filter("code", code);
			List<models.HealthArchives> data = query.asList();
			if (size >= offset && size < end) {
				for (models.HealthArchives flup : data) {
					result.add(flup);
					size ++;
				}
			} else {
				size += data.size();
			}
			if (size >= end) {
				break;
			}
		}
		return result;
	}
	
	/** 添加操作 */
	public static void add(String name, String code, String gender, int age,
			String home, String dealthAddress, String history, String department, String tel,
			String dealthDate, String reportDate, String physicals) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(result);
		}
		
		models.HealthArchives existHealth = isExist(code, department, null);
		if (existHealth != null) {
			result.put("error", "人员添加失败，" + existHealth.name + "(" + existHealth.code + ")已经存在！");
		} else {
			models.HealthArchives newData = new models.HealthArchives();
			Date newDate = new Date();
			models.Department curDep = models.Department.findById(department);
			
			newData.name = name;
			newData.code = code;
			newData.gender = gender;
			newData.age = age;
			newData.dealthDate = Utils.parseDate(dealthDate);
			newData.reportDate = Utils.parseDate(reportDate);
			newData.home = home;
			newData.dealthAddress = dealthAddress;
			newData.history = history;
			newData.tel = tel;
			newData.createAt = newDate;
			newData.modifyAt = newDate;
			newData.department = curDep;
			newData = newData.save();
			
			if (physicals != null && !"".equals(physicals)) {
				List<String> physicalArr = Utils.toStringArray(physicals, "#S#");
				List<models.Physical> childrenList = new ArrayList<models.Physical>();
				for (int i = 0; i < physicalArr.size(); i ++) {
					// data format: name,content,result,reference,doctor,summary,conclusion,treatment,address,tel,treatmentDoctor,id|... 
					String[] contentAt = physicalArr.get(i).split("#,#", 12);
					models.Physical newPhy = new models.Physical();
					if (contentAt.length > 0)
						newPhy.name = contentAt[0];
					newPhy.health = newData;
					newPhy.createAt = newDate;
					newPhy.modifyAt = newDate;
					newPhy = newPhy.save();
					
					// 添加检查项内容
					if (contentAt.length > 1 && Utils.checkString(contentAt[1])) {
						List<models.PhysicalInfo> infoList = new ArrayList<models.PhysicalInfo>();
						List<String> infoArr = Utils.toStringArray(contentAt[1], "#T#");
						for (int j = 0; j < infoArr.size(); j ++) {
							models.PhysicalInfo newInfo = new models.PhysicalInfo();
							newInfo.content = infoArr.get(j);
							newInfo.physical = newPhy;
							newInfo.createAt = newDate;
							newInfo.modifyAt = newDate;
							newInfo = newInfo.save();
							infoList.add(newInfo);
						}
						newPhy.physicalInfo = infoList;
					}
					// 检查结果
					if ((contentAt.length > 2 && Utils.checkString(contentAt[2]))
							|| (contentAt.length > 4 && Utils.checkString(contentAt[4]))) {
						models.PhysicalResult newResult = new models.PhysicalResult();
						newResult.health = newData;
						newResult.physical = newPhy;
						if (contentAt.length > 2)
							newResult.result = contentAt[2];
						if (contentAt.length > 3)
							newResult.reference = contentAt[3];
						if (contentAt.length > 4)
							newResult.doctor = contentAt[4];
						newResult.department = curDep;
						newResult.createAt = newDate;
						newResult.modifyAt = newDate;
						newResult = newResult.save();
						newPhy.results = newResult;
					}
					// 结论以及建议
					if ((contentAt.length > 5 && Utils.checkString(contentAt[5]))
							|| (contentAt.length > 6 && Utils.checkString(contentAt[6]))
							|| (contentAt.length > 7 && Utils.checkString(contentAt[7]))
							|| (contentAt.length > 8 && Utils.checkString(contentAt[8]))
							|| (contentAt.length > 9 && Utils.checkString(contentAt[9]))
							|| (contentAt.length > 10 && Utils.checkString(contentAt[10]))) {
						models.PhysicalTreatment newTra = new models.PhysicalTreatment();
						if (contentAt.length > 5)
							newTra.summary = contentAt[5];
						if (contentAt.length > 6)
							newTra.conclusion = contentAt[6];
						if (contentAt.length > 7)
							newTra.treatment = contentAt[7];
						if (contentAt.length > 8)
							newTra.address = contentAt[8];
						if (contentAt.length > 9)
							newTra.tel = contentAt[9];
						if (contentAt.length > 10)
							newTra.treatmentDoctor = contentAt[10];
						newTra.department = curDep;
						newTra.createAt = newDate;
						newTra.modifyAt = newDate;
						newTra.physical = newPhy;
						newTra.health = newData;
						newTra = newTra.save();
						newPhy.treatment = newTra;
					}
					newPhy = newPhy.save();
					childrenList.add(newPhy);
				}
				newData.physicals = childrenList;
				newData.save();
			}
		}
		renderText(result);
	}
	
	/** 删除操作 */
	public static void del(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			models.HealthArchives.findById(id).delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(result);
	}
	
	/** 修改操作 */
	public static void update(String id, String name, String code, String gender, int age,
			String home, String dealthAddress, String history, String department, String tel,
			String dealthDate, String reportDate, String physicals) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(result);
		}
		try {
			models.HealthArchives cur = models.HealthArchives.findById(id);
			models.Department curDep = models.Department.findById(department);
			Date modifyDate = new Date();
			cur.name = name;
			cur.code = code;
			cur.gender = gender;
			cur.age = age;
			cur.dealthDate = Utils.parseDate(dealthDate);
			cur.reportDate = Utils.parseDate(reportDate);
			cur.home = home;
			cur.dealthAddress = dealthAddress;
			cur.history = history;
			cur.tel = tel;
			cur.modifyAt = modifyDate;
			cur.department = curDep;
			
			if (physicals != null && !"".equals(physicals)) {
				List<String> physicalArr = Utils.toStringArray(physicals, "#S#");
				List<models.Physical> childrenList = new ArrayList<models.Physical>();
				for (int i = 0; i < physicalArr.size(); i ++) {
					// data format: name,content,result,reference,doctor,summary,conclusion,treatment,address,tel,treatmentDoctor,id|... 
					String[] contentAt = physicalArr.get(i).split("#,#", 12);
					models.Physical newPhy = null;
					if (contentAt.length > 11 && Utils.checkString(contentAt[11])) {
						newPhy = models.Physical.findById(contentAt[11]);
					} else {
						newPhy = new models.Physical();
						newPhy.createAt = modifyDate;
					}
					if (contentAt.length > 0)
						newPhy.name = contentAt[0];
					newPhy.health = cur;
					newPhy.modifyAt = modifyDate;
					newPhy = newPhy.save();
					
					// 清除之前的检查详情
					if (newPhy.physicalInfo != null && newPhy.physicalInfo.size() > 0) {
						for (models.PhysicalInfo delInfo : newPhy.physicalInfo)
							delInfo.delete();
						newPhy.physicalInfo = null;
					}
					
					// 添加检查项内容
					if (contentAt.length > 1 && Utils.checkString(contentAt[1])) {
						List<models.PhysicalInfo> infoList = new ArrayList<models.PhysicalInfo>();
						List<String> infoArr = Utils.toStringArray(contentAt[1], "#T#");
						for (int j = 0; j < infoArr.size(); j ++) {
							models.PhysicalInfo newInfo = new models.PhysicalInfo();
							newInfo.content = infoArr.get(j);
							newInfo.physical = newPhy;
							newInfo.createAt = modifyDate;
							newInfo.modifyAt = modifyDate;
							newInfo = newInfo.save();
							infoList.add(newInfo);
						}
						newPhy.physicalInfo = infoList;
					}
					// 检查结果
					if ((contentAt.length > 2 && Utils.checkString(contentAt[2]) ) || 
							(contentAt.length > 4 && Utils.checkString(contentAt[4]))) {
						models.PhysicalResult newResult = null;
						if (newPhy.results != null) {
							newResult = newPhy.results;
						} else {
							newResult = new models.PhysicalResult();
							newResult.createAt = modifyDate;
						}
						newResult.health = cur;
						newResult.physical = newPhy;
						if (contentAt.length > 2)
							newResult.result = contentAt[2];
						if (contentAt.length > 3)
							newResult.reference = contentAt[3];
						if (contentAt.length > 4)
							newResult.doctor = contentAt[4];
						newResult.department = curDep;
						newResult.modifyAt = modifyDate;
						newResult = newResult.save();
						newPhy.results = newResult;
					}
					// 结论以及建议
					if ((contentAt.length > 5 && Utils.checkString(contentAt[5]))
							|| (contentAt.length > 6 && Utils.checkString(contentAt[6]))
							|| (contentAt.length > 7 && Utils.checkString(contentAt[7]))
							|| (contentAt.length > 8 && Utils.checkString(contentAt[8]))
							|| (contentAt.length > 9 && Utils.checkString(contentAt[9]))
							|| (contentAt.length > 10 && Utils.checkString(contentAt[10]))) {
						models.PhysicalTreatment newTra = null;
						if (newPhy.treatment != null) {
							newTra = newPhy.treatment;
						} else {
							newTra = new models.PhysicalTreatment();
							newTra.createAt = modifyDate;
						}
						if (contentAt.length > 5)
							newTra.summary = contentAt[5];
						if (contentAt.length > 6)
							newTra.conclusion = contentAt[6];
						if (contentAt.length > 7)
							newTra.treatment = contentAt[7];
						if (contentAt.length > 8)
							newTra.address = contentAt[8];
						if (contentAt.length > 9)
							newTra.tel = contentAt[9];
						if (contentAt.length > 10)
							newTra.treatmentDoctor = contentAt[10];
						newTra.department = curDep;
						newTra.modifyAt = modifyDate;
						newTra.physical = newPhy;
						newTra.health = cur;
						newTra = newTra.save();
						newPhy.treatment = newTra;
					}
					newPhy = newPhy.save();
					childrenList.add(newPhy);
				}
				cur.physicals = childrenList;
			} else {
				if (cur.physicals != null) {
					for (models.Physical phy: cur.physicals)
						phy.delete();
				}
				cur.physicals = null;
			}
			cur.save();
			result.put("success", true);
		} catch (Exception exc) {
			exc.printStackTrace();
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(result);
	}
	@Util
	public static models.HealthArchives isExist(final String code, final String department, final String id) {
		models.Department dep = models.Department.findById(department);
		MorphiaQuery q = models.HealthArchives.find("byDepartment", dep).filter("code", code);
		if (id != null && !"".equals(id)) {
			List<models.HealthArchives> exists = q.asList();
			for (models.HealthArchives exist : exists) {
				if (!id.equals(exist.getId().toString()))
					return exist;
			}
			return null;
		} else
			return q.get();
	}
}
