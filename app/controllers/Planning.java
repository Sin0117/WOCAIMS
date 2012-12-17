package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import utils.Utils;

import com.mongodb.util.JSON;

/** 随访记录管理 */
public class Planning extends Controller {
	public static final int PAGE_SIZE = 20;
	/** 管理入口 .*/
	public static void index(String keyword, String department, int page, int size) {
		List<models.Department> divisions = Secure.getDepartments();
		render(keyword, department, divisions);
	}
	
	/** 导出excel. */
	public static void report(String keyword,Date date, String department) {
		int rows = 100000;
		int page = 1;
		List<models.Flup> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, date, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.Flup.find();
				if (keyword != null && !"".equals(keyword))
					query.filter("name", keyword);
				if (date != null)
					query.filter("date", date);
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, date, page, rows);
			}
		}

		String fileName = "计划生育基本情况表-" + Secure.getAdmin().department.name + ".xls";	
		File f = new File("./excels/" + fileName);
		try {
			if (f.exists())
				f.createNewFile();
			WritableWorkbook wbook = Workbook.createWorkbook(f);
			WritableSheet ws = wbook.createSheet("人口和计划生育工作随访服务记录", 0);
			
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);   
			WritableCellFormat wcfFC = new WritableCellFormat(wfont); 
			wcfFC.setAlignment(Alignment.CENTRE);
			int index = 0;
			Label label0_0 = new Label(0, index, "人口和计划生育工作随访服务记录",wcfFC);
			ws.addCell(label0_0);
			ws.mergeCells(0, index, 5, 0); 
			index++;
			Label label0_1 = new Label(0, index, "单位");
			ws.addCell(label0_1);
			Label label1_1 = new Label(1, index,
					Secure.getAdmin().department.name);
			ws.addCell(label1_1);
			index++;
			Label label0_2 = new Label(0, index, "随访职工姓名");
			ws.addCell(label0_2);
			Label label1_2 = new Label(1, index, "随访事由");
			ws.addCell(label1_2);
			Label label2_2 = new Label(2, index, "随访内容");
			ws.addCell(label2_2);
			Label label3_2 = new Label(3, index, "随访日期");
			ws.addCell(label3_2);
			Label label4_2 = new Label(4, index, "随访人员");
			ws.addCell(label4_2);
			Label label5_2 = new Label(5, index, "备注");
			ws.addCell(label5_2);

			if (lists != null) {
				for (models.Flup flup : lists) {
					index++;
					Label label0_2_r = new Label(0, index, flup.name);
					ws.addCell(label0_2_r);
					Label label1_2_r = new Label(1, index, flup.reason);
					ws.addCell(label1_2_r);
					Label label2_2_r = new Label(2, index, flup.content);
					ws.addCell(label2_2_r);
					Label label3_2_r = new Label(3, index, utils.Utils
							.formatDate(flup.date));
					ws.addCell(label3_2_r);
					Label label4_2_r = new Label(4, index, flup.staff);
					ws.addCell(label4_2_r);
					Label label5_2_r = new Label(5, index, flup.notes);
					ws.addCell(label5_2_r);
				}
			}

			wbook.write();
			wbook.close();
			renderBinary(f, fileName);
		} catch (Exception exception) {
			exception.printStackTrace();
			error(exception);
		}
	}
	
	/** 获取数据列表. */
	public static void list(String keyword, Date date, String department, int page, int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		List<models.Flup> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, date, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.Flup.find();
				if (keyword != null && !"".equals(keyword))
					query.filter("name", keyword);
				if (date != null)
					query.filter("date", date);
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, date, page, rows);
			}
		}
		List<Object> datas = new ArrayList<Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (models.Flup mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.Flup.count());
		result.put("size", rows);
		renderJSON(result);
	}
	
	/** 获取当前操作者的全部数据. */
	private static List<models.Flup> findAll(String keyword, Date date, int page, int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.Flup> result = new ArrayList<models.Flup>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.Flup.find("byDepartment", department);
			if (keyword != null && !"".equals(keyword))
				query.filter("name", keyword);
			if (date != null)
				query.filter("date", date);
			List<models.Flup> data = query.asList();
			if (size >= offset && size < end) {
				for (models.Flup flup : data) {
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
	public static void add(String name, String reason, String content, String date,
			String staff, String notes, String department) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "随访记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		models.Flup newData = new models.Flup();
		Date newDate = new Date();
		newData.name = name;
		newData.reason = reason;
		newData.content = content;
		newData.date = Utils.parseDate(date);
		newData.staff = staff;
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
			models.Flup.findById(id).delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	/** 修改操作 */
	public static void update(String id, String name, String reason, String content, String date,
			String staff, String notes, String department) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "随访记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		try {
			models.Flup cur = models.Flup.findById(id);
			cur.name = name;
			cur.reason = reason;
			cur.content = content;
			cur.date = Utils.parseDate(date);
			cur.staff = staff;
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
