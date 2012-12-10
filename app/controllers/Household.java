package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import models.enums.DepartmentLevel;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import play.mvc.Util;
import play.mvc.With;
import utils.Utils;
/** 户基本信息管理 */
@With(Secure.class)
public class Household extends Controller {
	public static final int PAGE_SIZE = 20;
	/** 管理入口 .*/
	public static void index(String keyword, String department, int page, int size) {
		List<models.Department> divisions = Secure.getDepartments();
		render(keyword, department, divisions);
	}
	
	/** 导出excel. */
	public static void report(String keyword, String department, String id) {
		String fileName = "户基本信息-" + Secure.getAdmin().department.name + ".xls";
		File f = new File("./excels/" + fileName);
		models.Household data = models.Household.findById(id);
	    try {
	    	if (f.exists())
	    		f.createNewFile();
	        WritableWorkbook wbook = Workbook.createWorkbook(f);
	        WritableSheet ws = wbook.createSheet("户基本信息卡", 0);
	        Label label0_0 = new Label(0, 0, "单位");
	        ws.addCell(label0_0);
	        Label field0_0 = new Label(1, 0, data.department.name);
	        ws.addCell(field0_0);
	        
	        Label label0_1 = new Label(2, 0, "单位编码");
	        ws.addCell(label0_1);
	        Label field0_1 = new Label(3, 0, data.department.code);
	        ws.addCell(field0_1);
	        
	        Label label1_0 = new Label(0, 1, "地区属性");
	        ws.addCell(label1_0);
	        Label field1_0 = new Label(1, 1, data.live);
	        ws.addCell(field1_0);
	        
	        Label label1_1 = new Label(2, 1, "建卡日期");
	        ws.addCell(label1_1);
	        Label field1_1 = new Label(3, 1, utils.Utils.formatDate(data.createAt));
	        ws.addCell(field1_1);
	        
	        Label label2_0 = new Label(0, 2, "户编码");
	        ws.addCell(label2_0);
	        Label field2_0 = new Label(1, 2, data.code);
	        ws.addCell(field2_0);
	        
	        Label label2_1 = new Label(2, 2, "户主姓名");
	        ws.addCell(label2_1);
	        Label field2_1 = new Label(3, 2, data.user);
	        ws.addCell(field2_1);
	        
	        Label label3_0 = new Label(0, 3, "户籍地址");
	        ws.addCell(label3_0);
	        Label field3_0 = new Label(1, 3, data.register);
	        ws.addCell(field3_0);
	        
	        Label label3_1 = new Label(2, 3, "本户人数");
	        ws.addCell(label3_1);
	        jxl.write.Number field3_1 = new jxl.write.Number(3, 3, data.peoples);
	        ws.addCell(field3_1);
	        
	        Label label4_0 = new Label(0, 4, "本户育龄妇女人数");
	        ws.addCell(label4_0);
	        jxl.write.Number field4_0 = new jxl.write.Number(1, 4, data.pregnant);
	        ws.addCell(field4_0);
	        
	        Label label4_1 = new Label(2, 4, "本户少数民族人数");
	        ws.addCell(label4_1);
	        jxl.write.Number field4_1 = new jxl.write.Number(3, 4, data.minority);
	        ws.addCell(field4_1);
	        
	        Label label5_0 = new Label(0, 5, "流入人数");
	        ws.addCell(label5_0);
	        jxl.write.Number field5_0 = new jxl.write.Number(1, 5, data.into);
	        ws.addCell(field5_0);
	        
	        Label label5_1 = new Label(2, 5, "少数民族流入人数");
	        ws.addCell(label5_1);
	        jxl.write.Number field5_1 = new jxl.write.Number(3, 5, data.minorityInto);
	        ws.addCell(field5_1);
	        
	        Label label6_0 = new Label(0, 6, "流出人数");
	        ws.addCell(label6_0);
	        jxl.write.Number field6_0 = new jxl.write.Number(1, 6, data.out);
	        ws.addCell(field6_0);
	        
	        Label label6_1 = new Label(2, 6, "少数民族流出人数");
	        ws.addCell(label6_1);
	        jxl.write.Number field6_1 = new jxl.write.Number(3, 6, data.minorityOut);
	        ws.addCell(field6_1);
	        
	        Label label7_0 = new Label(0, 7, "记事栏");
	        ws.addCell(label7_0);
	        Label field7_0 = new Label(1, 7, data.notes);
	        ws.addCell(field7_0);
	        
	        wbook.write();
	        wbook.close();
	        renderBinary(f, fileName);
	    } catch (Exception exception) {  
	        // TODO Auto-generated catch block  
	    	exception.printStackTrace();
	        render("/errors/500.html", exception);
	    }
	}
	
	/** 获取数据列表. */
	public static void list(String keyword, String department, int page, int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		List<models.Household> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.Household.find();
				if (keyword != null && !"".equals(keyword))
					query.filter("user", keyword);
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, page, rows);
			}
		}
		List<Object> datas = new ArrayList<Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (models.Household mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.Household.count());
		result.put("size", rows);
		renderJSON(result);
	}
	/** 获取当前操作者的全部数据. */
	private static List<models.Household> findAll(String keyword, int page, int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.Household> result = new ArrayList<models.Household>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.Household.find("byDepartment", department);
			if (keyword != null && !"".equals(keyword))
				query.filter("user", keyword);
			List<models.Household> data = query.asList();
			if (size >= offset && size < end) {
				for (models.Household household : data) {
					result.add(household);
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
	public static void add(String user, String live, String code, String register, 
			String department, int peoples, int pregnant, int minority, int into, int out, 
			int minorityInto, int minorityOut, String notes) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "户信息添加失败，请选择该人员所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(result);
		}
		models.Household exist = isExist(code, department, null);
		if (exist != null) {
			result.put("error", "户信息添加失败，" + exist.user + "(" + exist.code + ")已经存在！");
		} else {
			models.Household newData = new models.Household();
			Date newDate = new Date();
			newData.user = user;
			newData.live = live;
			newData.code = code;
			newData.register = register;
			newData.peoples = peoples;
			newData.pregnant = pregnant;
			newData.minority = minority;
			newData.into = into;
			newData.out = out;
			newData.minorityInto = minorityInto;
			newData.minorityOut = minorityOut;
			newData.notes = notes;
			newData.createAt = newDate;
			newData.modifyAt = newDate;
			newData.department = models.Department.findById(department);
			newData.save();
		}
		renderText(result);
	}
	
	/** 删除操作 */
	public static void del(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			models.Household.findById(id).delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(result);
	}
	
	/** 修改操作 */
	public static void update(String id, String user, String live, String code, String register, 
			String department, int peoples, int pregnant, int minority, int into, int out, 
			int minorityInto, int minorityOut, String notes) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "户信息添加失败，请选择该人员所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(result);
		}
		try {
			models.Household cur = models.Household.findById(id);
			models.Household exist = isExist(code, department, id);
			if (exist != null && !exist.getId().toString().equals(cur.getId().toString())) {
				result.put("error", "户信息添加失败，" + exist.user + "(" + exist.code + ")已经存在！");
			} else {
				cur.user = user;
				cur.live = live;
				cur.code = code;
				cur.register = register;
				cur.peoples = peoples;
				cur.pregnant = pregnant;
				cur.minority = minority;
				cur.into = into;
				cur.out = out;
				cur.minorityInto = minorityInto;
				cur.minorityOut = minorityOut;
				cur.notes = notes;
				cur.modifyAt = new Date();;
				cur.department = models.Department.findById(department);
				cur.save();
				result.put("success", true);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(result);
	}
	
	@Util
	public static models.Household isExist(final String code, final String department, final String id) {
		models.Department dep = models.Department.findById(department);
		MorphiaQuery q = models.Household.find("byDepartment", dep).filter("code", code);
		if (id != null && !"".equals(id)) {
			List<models.Household> exists = q.asList();
			for (models.Household exist : exists) {
				if (!id.equals(exist.getId().toString()))
					return exist;
			}
			return null;
		} else
			return q.get();
	}
}
