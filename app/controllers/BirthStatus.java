package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Department;

import com.google.code.morphia.annotations.Reference;
import com.mongodb.util.JSON;

import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import utils.Utils;
/** 出生情况管理 */
public class BirthStatus extends Controller {
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
		List<models.BirthStatus> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.BirthStatus.find();
				if (keyword != null && !"".equals(keyword))
					query.or(query.criteria("man").contains(keyword), query
							.criteria("woman").contains(keyword));
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, page, rows);
			}
		}

		String fileName = "出生情况-" + Secure.getAdmin().department.name + ".xls";
		File f = new File("./excels/" + fileName);
		try {
			if (f.exists())
				f.createNewFile();
			WritableWorkbook wbook = Workbook.createWorkbook(f);
			WritableSheet ws = wbook.createSheet("出生情况花名册", 0);
			
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);   
			WritableCellFormat wcfFC = new WritableCellFormat(wfont); 
			wcfFC.setAlignment(Alignment.CENTRE);
			int index = 0;
			Label label0_0 = new Label(0, index, "出生情况花名册",wcfFC);
			ws.addCell(label0_0);
			ws.mergeCells(0, index, 9, 0);
			index++;
			Label label0_1 = new Label(0, index, "单位");
			ws.addCell(label0_1);
			Label label1_1 = new Label(1, index,
					Secure.getAdmin().department.name);
			ws.addCell(label1_1);
			index++;
			Label label0_2 = new Label(0, index, "男方姓名");
			ws.addCell(label0_2);
			Label label1_2 = new Label(1, index, "女方姓名");
			ws.addCell(label1_2);
			Label label2_2 = new Label(2, index, "民族");
			ws.addCell(label2_2);
			Label label3_2 = new Label(3, index, "婴儿出生年月日");
			ws.addCell(label3_2);
			Label label4_2 = new Label(4, index, "孩次");
			ws.addCell(label4_2);
			Label label5_2 = new Label(5, index, "性别");
			ws.addCell(label5_2);
			Label label6_2 = new Label(6, index, "计划内外");
			ws.addCell(label6_2);
			Label label7_2 = new Label(7, index, "是否存活");
			ws.addCell(label7_2);
			Label label8_2 = new Label(8, index, "准生证号");
			ws.addCell(label8_2);
			Label label9_2 = new Label(9, index, "备注");
			ws.addCell(label9_2);

			if (lists != null) {
				for (models.BirthStatus birthStatus : lists) {
					index++;
					Label label0_2_r = new Label(0, index, birthStatus.man);
					ws.addCell(label0_2_r);
					Label label1_2_r = new Label(1, index, birthStatus.woman);
					ws.addCell(label1_2_r);
					Label label2_2_r = new Label(2, index, birthStatus.nation);
					ws.addCell(label2_2_r);
					Label label3_2_r = new Label(3, index, utils.Utils
							.formatDate(birthStatus.birth));
					ws.addCell(label3_2_r);
					jxl.write.Number label4_2_r = new jxl.write.Number(4,
							index, birthStatus.size);
					ws.addCell(label4_2_r);
					Label label5_2_r = new Label(5, index, birthStatus.gender);
					ws.addCell(label5_2_r);
					Label label6_2_r = new Label(6, index,
							birthStatus.plan ? "计划内" : "计划外");
					ws.addCell(label6_2_r);
					Label label7_2_r = new Label(7, index,
							birthStatus.survival ? "存活" : "死亡");
					ws.addCell(label7_2_r);
					Label label8_2_r = new Label(8, index, birthStatus.code);
					ws.addCell(label8_2_r);
					Label label9_2_r = new Label(9, index, birthStatus.notes);
					ws.addCell(label9_2_r);
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
	public static void list(String keyword, String department, int page, int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		List<models.BirthStatus> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.BirthStatus.find();
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
		for (models.BirthStatus mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.BirthStatus.count());
		result.put("size", rows);
		renderJSON(result);
	}
	
	/** 获取当前操作者的全部数据. */
	private static List<models.BirthStatus> findAll(String keyword, int page, int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.BirthStatus> result = new ArrayList<models.BirthStatus>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.BirthStatus.find("byDepartment", department);
			if (keyword != null && !"".equals(keyword))
				query.or(query.criteria("man").contains(keyword), query.criteria("woman").contains(keyword));
			List<models.BirthStatus> data = query.asList();
			if (size >= offset && size < end) {
				for (models.BirthStatus flup : data) {
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
	public static void add(String man, String woman, String name, String nation, String birth, int size,
			String gender, Boolean plan, Boolean survival, String code, String notes, String department) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		models.BirthStatus newData = new models.BirthStatus();
		Date newDate = new Date();
		newData.man = man;
		newData.woman = woman;
		newData.name = name;
		newData.nation = nation;
		newData.birth = Utils.parseDate(birth);
		newData.size = size;
		newData.gender = gender;
		newData.plan = plan;
		newData.survival = survival;
		newData.code = code;
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
			models.BirthStatus.findById(id).delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	/** 修改操作 */
	public static void update(String id, String man, String woman, String name, String nation, String birth, 
			int size, String gender, Boolean plan, Boolean survival, String code, String notes, 
			String department) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		try {
			models.BirthStatus cur = models.BirthStatus.findById(id);
			cur.man = man;
			cur.woman = woman;
			cur.name = name;
			cur.nation = nation;
			cur.birth = Utils.parseDate(birth);
			cur.size = size;
			cur.gender = gender;
			cur.plan = plan;
			cur.survival = survival;
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
		renderText(JSON.serialize(result));
	}
}
