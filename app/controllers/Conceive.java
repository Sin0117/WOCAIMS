package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Department;

import com.google.code.morphia.annotations.Reference;

import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import utils.Utils;
/** 现孕登记管理 */
public class Conceive extends Controller {
	public static final int PAGE_SIZE = 20;
	/** 管理入口 .*/
	public static void index(String keyword, String department, int page, int size) {
		List<models.Department> divisions = Secure.getDepartments();
		render(keyword, department, divisions);
	}
	
	/** 导出excel. */
	public static void report(String keyword, String department) {
		String fileName = "现孕登记-" + Secure.getAdmin().department.name + ".xls";
		renderBinary(Utils.toExcel(fileName), fileName);
	}
	
	/** 获取数据列表. */
	public static void list(String keyword, String department, int page, int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		List<models.Conceive> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.Conceive.find();
				if (keyword != null && !"".equals(keyword))
					query.or(query.criteria("man").contains(keyword), query.criteria("woman").contains(keyword));
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, page, rows);
			}
		}
		List<Object> datas = new ArrayList<Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (models.Conceive mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.Conceive.count());
		result.put("size", rows);
		renderJSON(result);
	}
	
	/** 获取当前操作者的全部数据. */
	private static List<models.Conceive> findAll(String keyword, int page, int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.Conceive> result = new ArrayList<models.Conceive>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.Conceive.find("byDepartment", department);
			if (keyword != null && !"".equals(keyword))
				query.or(query.criteria("man").contains(keyword), query.criteria("woman").contains(keyword));
			List<models.Conceive> data = query.asList();
			if (size >= offset && size < end) {
				for (models.Conceive flup : data) {
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
	public static void add(String man, String woman, String nation, String birth, 
			String conceiveDate, int size, boolean thisYear, boolean plan, String code, 
			String notes, String department) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderJSON(result);
		}
		models.Conceive newData = new models.Conceive();
		Date newDate = new Date();
		newData.man = man;
		newData.woman = woman;
		newData.nation = nation;
		newData.birth = Utils.parseDate(birth);
		newData.conceiveDate = Utils.parseDate(conceiveDate);
		newData.size = size;
		newData.thisYear = thisYear;
		newData.plan = plan;
		newData.code = code;
		newData.notes = notes;
		newData.createAt = newDate;
		newData.modifyAt = newDate;
		newData.department = models.Department.findById(department);
		newData.save();
		renderJSON(result);
	}
	
	/** 删除操作 */
	public static void del(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			models.Conceive.findById(id).delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderJSON(result);
	}
	
	/** 修改操作 */
	public static void update(String id, String man, String woman, String nation, String birth, 
			String conceiveDate, int size, boolean thisYear, boolean plan, String code, 
			String notes, String department) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderJSON(result);
		}
		try {
			models.Conceive cur = models.Conceive.findById(id);
			cur.man = man;
			cur.woman = woman;
			cur.nation = nation;
			cur.birth = Utils.parseDate(birth);
			cur.conceiveDate = Utils.parseDate(conceiveDate);
			cur.size = size;
			cur.thisYear = thisYear;
			cur.plan = plan;
			cur.code = code;
			cur.notes = notes;
			cur.modifyAt = new Date();
			cur.department = models.Department.findById(department);
			cur.save();
			result.put("success", true);
		} catch (Exception exc) {
			exc.printStackTrace();
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderJSON(result);
	}
}
