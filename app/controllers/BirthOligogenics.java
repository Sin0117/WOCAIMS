package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import utils.Utils;
/** 出生及节育措施管理. */
public class BirthOligogenics extends Controller {
	public static final int PAGE_SIZE = 20;
	/** 管理入口 .*/
	public static void index(String keyword, String department, int page, int size) {
		List<models.Department> divisions = Secure.getDepartments();
		render(keyword, department, divisions);
	}
	
	/** 导出excel. */
	public static void report(String keyword, String department) {
		String fileName = "出生及节育措施-" + Secure.getAdmin().department.name + ".xls";
		renderBinary(Utils.toExcel(fileName), fileName);
	}
	
	/** 获取数据列表. */
	public static void list(String keyword, String department, int page, int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		List<models.BirthOligogenics> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.BirthOligogenics.find();
				if (keyword != null && !"".equals(keyword))
					query.or(query.criteria("birthStatus.man").contains(keyword), query.criteria("birthStatus.woman").contains(keyword),
							query.criteria("oligogenics.man").contains(keyword), query.criteria("oligogenics.woman").contains(keyword));
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, page, rows);
			}
		}
		List<Object> datas = new ArrayList<Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (models.BirthOligogenics mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.BirthOligogenics.count());
		result.put("size", rows);
		renderJSON(result);
	}
	
	/** 获取当前操作者的全部数据. */
	private static List<models.BirthOligogenics> findAll(String keyword, int page, int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.BirthOligogenics> result = new ArrayList<models.BirthOligogenics>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.BirthOligogenics.find("byDepartment", department);
			if (keyword != null && !"".equals(keyword))
				query.or(query.criteria("birthStatus.man").contains(keyword), query.criteria("birthStatus.woman").contains(keyword),
						query.criteria("oligogenics.man").contains(keyword), query.criteria("oligogenics.woman").contains(keyword));
			List<models.BirthOligogenics> data = query.asList();
			if (size >= offset && size < end) {
				for (models.BirthOligogenics flup : data) {
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
	public static void add(String flow, String man, String woman, String nation, String birth, int size,
			String gender, Boolean plan, Boolean survival, String code, String measure,
			String measureDate, String measureLocal, String notes, String department) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderJSON(result);
		}
		Date newDate = new Date();
		models.Department dep = models.Department.findById(department);
		
		models.BirthStatus newBirth = new models.BirthStatus();
		newBirth.man = man;
		newBirth.woman = woman;
		newBirth.nation = nation;
		newBirth.birth = Utils.parseDate(birth);
		newBirth.size = size;
		newBirth.gender = gender;
		newBirth.plan = plan;
		newBirth.survival = survival;
		newBirth.code = code;
		newBirth.notes = notes;
		newBirth.createAt = newDate;
		newBirth.modifyAt = newDate;
		newBirth.department = dep;
		newBirth.save();
		
		models.Oligogenics newOlig = new models.Oligogenics();
		newOlig.man = man;
		newOlig.woman = woman;
		newOlig.nation = nation;
		newOlig.measureDate = Utils.parseDate(measureDate);
		newOlig.size = size;
		newOlig.measure = measure;
		newOlig.measureLocal = measureLocal;
		newOlig.notes = notes;
		newOlig.createAt = newDate;
		newOlig.modifyAt = newDate;
		newOlig.department = dep;
		newOlig.save();
		
		models.BirthOligogenics newData = new models.BirthOligogenics();
		newData.flow = flow;
		newData.birthStatus = newBirth;
		newData.oligogenics = newOlig;
		newData.createAt = newDate;
		newData.modifyAt = newDate;
		newData.department = dep;
		newData.notes = notes;
		newData.save();
		renderJSON(result);
	}
	
	/** 删除操作 */
	public static void del(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			models.BirthOligogenics.findById(id).delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderJSON(result);
	}
	
	/** 修改操作 */
	public static void update(String id, String flow, String man, String woman, String nation, 
			String birth, int size,
			String gender, Boolean plan, Boolean survival, String code, String measure,
			String measureDate, String measureLocal, String notes, String department) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderJSON(result);
		}
		try {
			models.Department dep = models.Department.findById(department);
			Date newDate = new Date();
			models.BirthOligogenics cur = models.BirthOligogenics.findById(id);
			models.BirthStatus newBirth = cur.birthStatus;
			newBirth.man = man;
			newBirth.woman = woman;
			newBirth.nation = nation;
			newBirth.birth = Utils.parseDate(birth);
			newBirth.size = size;
			newBirth.gender = gender;
			newBirth.plan = plan;
			newBirth.survival = survival;
			newBirth.code = code;
			newBirth.notes = notes;
			newBirth.modifyAt = newDate;
			newBirth.department = dep;
			newBirth.save();
			
			models.Oligogenics newOlig = cur.oligogenics;
			newOlig.man = man;
			newOlig.woman = woman;
			newOlig.nation = nation;
			newOlig.measureDate = Utils.parseDate(measureDate);
			newOlig.size = size;
			newOlig.measure = measure;
			newOlig.measureLocal = measureLocal;
			newOlig.notes = notes;
			newOlig.createAt = newDate;
			newOlig.modifyAt = newDate;
			newOlig.department = dep;
			newOlig.save();
			
			cur.flow = flow;
			cur.birthStatus = newBirth;
			cur.oligogenics = newOlig;
			cur.modifyAt = newDate;
			cur.department = dep;
			cur.notes = notes;
			cur.save();
			result.put("success", true);
		} catch (Exception exc) {
			exc.printStackTrace();
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderJSON(result);
	}
}
