package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.util.JSON;

import models.enums.DepartmentLevel;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import play.mvc.Util;
import play.mvc.With;

/** 用户管理. */
@With(Secure.class)
public class Administrator extends Controller {
	public static final int PAGE_SIZE = 20;
	/** 管理入口 .*/
	public static void index(String keyword, String department, int page, int size) {
		List<models.Department> divisions = Secure.getDepartments();
		render(keyword, department, divisions);
	}
	
	/** 获取数据列表. */
	public static void list(String keyword, String department, int page, int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		MorphiaQuery query = models.Administrator.find();
		if (keyword != null && "".equals(keyword))
			query.filter("name", keyword);
		if (department != null && "".equals(department)) {
			models.Department dep = models.Department.findById(department);
			if (dep != null)
				query.filter("department", dep);
		}
		List<models.Administrator> lists = query.limit(rows).offset(page * rows - rows).asList();
		List<Object> datas = new ArrayList<Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (models.Administrator mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.Administrator.count());
		result.put("size", rows);
		renderJSON(result);
	}
	
	/** 添加操作 */
	public static void add(String name, String login, String password, boolean admin, String department) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "人员添加失败，请选择该人员所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		models.Administrator existAdmin = isExist(login, department, null);
		if (existAdmin != null) {
			result.put("error", "人员添加失败，" + existAdmin.name + "(" + existAdmin.admin + ")已经存在！");
		} else {
			models.Administrator newAmin = new models.Administrator();
			newAmin.admin = login;
			newAmin.name = name;
			newAmin.password = password;
			newAmin.isAdmin = admin;
			newAmin.createAt = new Date();
			newAmin.department = models.Department.findById(department);
			newAmin.save();
		}
		renderText(JSON.serialize(result));
	}
	
	/** 删除操作 */
	public static void del(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			models.Administrator.findById(id).delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	/** 修改操作 */
	public static void update(String id, String login, String name, String password, boolean admin, String department) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "人员添加失败，请选择该人员所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		try {
			models.Administrator cur = models.Administrator.findById(id);
			cur.admin = login;
			cur.name = name;
			cur.password = password;
			cur.isAdmin = admin;
			cur.department = models.Department.findById(department);
			cur.save();
			result.put("success", true);
		} catch (Exception exc) {
			exc.printStackTrace();
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	/** 修改密码. */
	public static void changePassword(final String id, final String old, final String pwd) {
		String message = null;
		models.Administrator admin = Secure.getAdmin();
		models.Administrator cur = models.Administrator.findById(id);
		try {
			if (!admin.getId().toString().equals(id)) {
				message = "用户信息异常！";
			} else {
				if (cur.password.equals(old)) {
					cur.password = pwd;
					cur.save();
					message = "密码修改成功！";
				} else {
					message = "旧密码错误！";
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			message = "数据库异常，可能其他人正在操作，请刷新后重试。";
		}
		render("/Administrator/modify.html", admin, message);
	}
	
	/** 修改密码. */
	public static void modify() {
		models.Administrator admin = Secure.getAdmin();
		render(admin);
	}
	
	@Util
	public static models.Administrator isExist(final String name, final String department, final String id) {
		models.Department dep = models.Department.findById(department);
		MorphiaQuery q = models.Administrator.find("byDepartment", dep).filter("admin", name);
		if (id != null && !"".equals(id)) {
			List<models.Administrator> exists = q.asList();
			for (models.Administrator exist : exists) {
				if (!id.equals(exist.getId().toString()))
					return exist;
			}
			return null;
		} else
			return q.get();
	}
}
