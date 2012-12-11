package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.util.JSON;

import models.Department;

import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import play.mvc.Util;
import play.mvc.With;
import utils.Utils;
/** 男职工登记情况管理 */
@With(Secure.class)
public class Workers extends Controller {
	public static final int PAGE_SIZE = 20;
	/** 管理入口 .*/
	public static void index(String keyword, String department, int page, int size) {
		List<models.Department> divisions = Secure.getDepartments();
		render(keyword, department, divisions);
	}
	
	/** 导出excel. */
	public static void report(String keyword, String department) {
		int rows = 100000;
		int page = 1;
		List<models.Workers> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.Workers.find();
				if (keyword != null && !"".equals(keyword))
					query.filter("name", keyword);
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, page, rows);
			}
		}

		String fileName = "男职工登记-" + Secure.getAdmin().department.name + ".xls";
		File f = new File("./excels/" + fileName);
		try {
			if (f.exists())
				f.createNewFile();
			WritableWorkbook wbook = Workbook.createWorkbook(f);
			WritableSheet ws = wbook.createSheet("男职工登记花名册", 0);
			
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);   
			WritableCellFormat wcfFC = new WritableCellFormat(wfont); 
			wcfFC.setAlignment(Alignment.CENTRE);
			int index = 0;
			Label label0_0 = new Label(0, index, "男职工登记花名册",wcfFC);
			ws.addCell(label0_0);
			ws.mergeCells(0, index, 17, 0); 
			index++;
			Label label0_1 = new Label(0, index, "单位");
			ws.addCell(label0_1);
			Label label1_1 = new Label(1, index,
					Secure.getAdmin().department.name);
			ws.addCell(label1_1);
			index++;
			Label label0_2 = new Label(0, index, "户主");
			ws.addCell(label0_2);
			ws.mergeCells(0, index, 2, 0); 

			wbook.write();
			wbook.close();
			renderBinary(f, fileName);
		} catch (Exception exception) {
			exception.printStackTrace();
			error(exception);
		}
	}
	
	/** 获取数据列表. */
	public static void list(String keyword, String department, int page, int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		List<models.Workers> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.Workers.find();
				if (keyword != null && !"".equals(keyword))
					query.filter("name", keyword);
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, page, rows);
			}
		}
		List<Object> datas = new ArrayList<Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (models.Workers mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.Workers.count());
		result.put("size", rows);
		renderJSON(result);
	}
	/** 获取当前操作者的全部数据. */
	private static List<models.Workers> findAll(String keyword, int page, int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.Workers> result = new ArrayList<models.Workers>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.Workers.find("byDepartment", department);
			if (keyword != null && !"".equals(keyword))
				query.filter("name", keyword);
			List<models.Workers> data = query.asList();
			if (size >= offset && size < end) {
				for (models.Workers worker : data) {
					result.add(worker);
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
	public static void add(String name, int han, int minority, String woman, String womanDate, 
			String weddingTime, String type, int boy, int girl, String childrenMinDate,
			String childrenMaxDate, String contraindication, String measure,
			String measureDate, String live, String notes, String department) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "职工添加失败，请选择该人员所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		models.Workers newData = new models.Workers();
		Date newDate = new Date();
		newData.name = name;
		newData.han = han;
		newData.minority = minority;
		newData.woman = woman;
		newData.womanDate = Utils.parseDate(womanDate);
		newData.weddingTime = Utils.parseDate(weddingTime);
		newData.type = type;
		newData.boy = boy;
		newData.girl = girl;
		newData.childrenMinDate = Utils.parseDate(childrenMinDate);
		newData.childrenMaxDate = Utils.parseDate(childrenMaxDate);
		newData.contraindication = contraindication;
		newData.measure = measure;
		newData.measureDate = Utils.parseDate(measureDate);
		newData.live = live;
		newData.notes = notes;
		newData.createAt = newDate;
		newData.modifyAt = newDate;
		newData.department = models.Department.findById(department);
		newData.save();
		renderText(JSON.serialize(result));
	}
	
	/** 删除操作 */
	public static void del(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			models.Workers.findById(id).delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	/** 修改操作 */
	public static void update(String id, String name, int han, int minority, String woman, 
			String womanDate, String weddingTime, String type, int boy, int girl, 
			String childrenMinDate, String childrenMaxDate, String contraindication, String measure,
			String measureDate, String live, String notes, String department) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "职工添加失败，请选择该人员所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		try {
			models.Workers cur = models.Workers.findById(id);
			cur.name = name;
			cur.han = han;
			cur.minority = minority;
			cur.woman = woman;
			cur.womanDate = Utils.parseDate(womanDate);
			cur.weddingTime = Utils.parseDate(weddingTime);
			cur.type = type;
			cur.boy = boy;
			cur.girl = girl;
			cur.childrenMinDate = Utils.parseDate(childrenMinDate);
			cur.childrenMaxDate = Utils.parseDate(childrenMaxDate);
			cur.contraindication = contraindication;
			cur.measure = measure;
			cur.measureDate = Utils.parseDate(measureDate);
			cur.live = live;
			cur.notes = notes;
			cur.modifyAt = new Date();
			cur.department = models.Department.findById(department);
			cur.save();
			result.put("success", true);
		} catch (Exception exc) {
			exc.printStackTrace();
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
}
