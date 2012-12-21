package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;

import com.google.code.morphia.query.CriteriaContainer;
import com.mongodb.util.JSON;

import models.enums.DepartmentLevel;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import play.mvc.Util;
import play.mvc.With;
import utils.CreateNo;

/** 部门管理 */
@With(Secure.class)
public class Department extends Controller {
	public static final int PAGE_SIZE = 20;
	/** 管理入口 .*/
	public static void index(String keyword, String department, int page, int size) {
		List<models.Department> divisions = models.Department.find().asList();
		
		List<models.Department> list = new ArrayList<models.Department>();
		
		for (models.Department department2 : divisions) {
			if(!department2.getDepartmentName().equals("科级部门")){
				if(department2.codeLength == 3){
					department2.name = "|--"+department2.name;
				}
				
				if(department2.codeLength == 6){
					department2.name = "|--|--"+department2.name;
				}
				
				if(department2.codeLength == 9){
					department2.name = "|--|--|--"+department2.name;
				}
				
				if(department2.codeLength == 12){
					department2.name = "|--|--|--|--"+department2.name;
				}
				
				if(department2.codeLength == 15){
					department2.name = "|--|--|--|--|--|--"+department2.name;
				}
				
				if(department2.codeLength == 18){
					department2.name = "|--|--|--|--|--|--|--"+department2.name;
				}
				
				list.add(department2);
			}
		}
		
		render(keyword, department, list);
	}
	
	public static List<models.Department> deptList(List<models.Department> deptList){
		
		return null;
	}
	
	/** 获取数据列表. */
	public static void list(String keyword, String code, int page, int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		MorphiaQuery query = models.Department.find();
		if (keyword != null && "".equals(keyword))
			query.filter("name", keyword);
		if (code != null && "".equals(code))
			query.filter("code", code);
		List<models.Department> lists = query.limit(rows).offset(page * rows - rows).asList();
		List<Object> datas = new ArrayList<Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (models.Department mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.Department.count());
		result.put("size", rows);
		renderJSON(result);
	}
	
	/** 添加操作 */
	public static void add(String name,String type, String parent) {
//		if(StringUtils.isBlank(code) || "自动或手动获取编号".equals(code)){
//			code = HypyUtil.cn2py(name).toUpperCase();
//		}
		MorphiaQuery query = models.Department.find();
		models.Department dept = models.Department.findById(parent);
		String code = dept.code;
		query.criteria("code").contains(code);
		query.filter("codeLength", code.length()+3);
		List<models.Department> lists = query.asList();
		List<String> l = new ArrayList<String>();
		for (models.Department department : lists) {
			l.add(department.code);
		}
		
		if(lists.size()==0){
			code += "001";
		}else{
			code = CreateNo.getNo(l, 3);
		}
		Map<String, String> result = new HashMap<String, String>();
		models.Department existDep = isExist(code, null);
		if (parent == null || "".equals(parent)) {
			result.put("error", "部门添加失败，请选择该部门所属部门。");
			renderText(JSON.serialize(result));
		}
		if (existDep != null) {
			result.put("error", "部门添加失败，" + existDep.name + "(" + existDep.code + ")部门已经存在！");
		} else {
			models.Department newDep = new models.Department();
			newDep.name = name;
			newDep.code = code;
			newDep.codeLength = code.length();
			newDep.type = DepartmentLevel.valueOf(type);
			newDep.createAt = new Date();
			if (parent != null && !"".equals(parent) && parent != "-1") {
				models.Department parentDep = models.Department.findById(parent);
				if (parentDep != null)
					newDep.parent = parentDep;
			}
			newDep.save();
		}
		renderText(JSON.serialize(result));
	}
	
	/** 删除操作 */
	public static void del(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			models.Department root = models.Department.findById(id);
			models.Department.filter("parent", root).delete();
			root.delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	/** 修改操作 */
	public static void update(String id, String name, String type, String parent) {
		MorphiaQuery query = models.Department.find();
		models.Department dept = models.Department.findById(parent);
		String code = dept.code;
		query.criteria("code").contains(code);
		query.filter("codeLength", code.length()+3);
		List<models.Department> lists = query.asList();
		List<String> l = new ArrayList<String>();
		for (models.Department department : lists) {
			l.add(department.code);
		}
		
		if(lists.size()==0){
			code += "001";
		}else{
			code = CreateNo.getNo(l, 3);
		}
		Map<String, String> result = new HashMap<String, String>();
		if (parent == null || "".equals(parent)) {
			result.put("error", "部门添加失败，请选择该部门所属部门。");
			renderText(JSON.serialize(result));
		}
		try {
			models.Department cur = models.Department.findById(id);
			models.Department existDep = isExist(code, id);
			if (existDep != null && !existDep.getId().toString().equals(cur.getId().toString())) {
				result.put("error", "部门添加失败，" + existDep.name + "(" + existDep.code + ")部门已经存在！");
			} else {
				cur.name = name;
				cur.code = code;
				cur.codeLength = code.length();
				cur.type = DepartmentLevel.valueOf(type);
				cur.parent = null;
				if (parent != null && !"".equals(parent) && parent != "-1") {
					models.Department parentDep = models.Department.findById(parent);
					if (parentDep != null)
						cur.parent = parentDep;
				}
				cur.save();
			}
		
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	@Util
	public static models.Department isExist(final String code, final String id) {
		MorphiaQuery q = models.Department.q();
		q.or(q.criteria("code").equal(code));
		if (id != null && !"".equals(id)) {
			List<models.Department> exists = q.asList();
			for (models.Department exist : exists) {
				if (!id.equals(exist.getId().toString()))
					return exist;
			}
			return null;
		} else
			return q.get();
	}
}
