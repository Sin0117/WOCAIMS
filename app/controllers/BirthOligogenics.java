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
import jxl.write.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import utils.Utils;

import com.mongodb.util.JSON;

/** 出生及节育措施管理. */
public class BirthOligogenics extends Controller {
	public static final int PAGE_SIZE = 20;

	/** 管理入口 . */
	public static void index(String keyword, String department, int page,
			int size) {
		List<models.Department> divisions = Secure.getDepartments();
		render(keyword, department, divisions);
	}

	/** 导出excel. */
	public static void report(String keyword, String department) {
		String fileName = "出生及节育措施花名册-";
		List<models.BirthOligogenics> lists = null;
		if (Utils.checkString(department)) {
			models.Department dep = models.Department.findById(department);
			fileName += dep.name + ".xls";
			MorphiaQuery query = models.BirthOligogenics.find();
			if (keyword != null && !"".equals(keyword))
				query.or(query.criteria("birthStatus.man").contains(keyword),
					query.criteria("birthStatus.woman").contains(keyword),
					query.criteria("oligogenics.man").contains(keyword),
					query.criteria("oligogenics.woman").contains(keyword));
			query.filter("department", dep);
			lists = query.asList();
			department = dep.name;
		} else {
			department = Secure.getAdmin().department.name;
			fileName += department + ".xls";
			lists = findAll(keyword, 0, 0);
		}
		File f = new File("./excels/" + fileName);
		try {
			if (f.exists())
				f.createNewFile();
			int index = 0;
			WritableWorkbook wbook = Workbook.createWorkbook(f);
			WritableSheet ws = wbook.createSheet("出生及节育措施花名册", 0);

			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK);
			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
			wcfFC.setAlignment(Alignment.CENTRE);
			Label label0_00 = new Label(0, index, "出生及节育措施花名册", wcfFC);
			ws.addCell(label0_00);
			ws.mergeCells(0, index, 14, 0);

			WritableFont wfont2 = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK);
			WritableCellFormat wcfFC2 = new WritableCellFormat(wfont2);
			wcfFC2.setAlignment(Alignment.CENTRE);
			wcfFC2.setVerticalAlignment(VerticalAlignment.CENTRE);

			index++;
			Label label0_0 = new Label(0, index, "填报单位", wcfFC2);
			ws.addCell(label0_0);
			Label field0_0 = new Label(1, index,
					Secure.getAdmin().department.name);
			ws.addCell(field0_0);
			ws.mergeCells(1, index, 14, index);
			index++;

			Label label1_0 = new Label(0, index, "夫妇情况", wcfFC2);
			ws.addCell(label1_0);
			ws.mergeCells(0, index, 6, index);
			Label label1_1 = new Label(7, index, "婴儿情况", wcfFC2);
			ws.addCell(label1_1);
			ws.mergeCells(7, index, 14, index);
			index++;

			Label label2_0 = new Label(0, index, "流动性质", wcfFC2);
			ws.addCell(label2_0);
			Label label2_1 = new Label(1, index, "男方姓名", wcfFC2);
			ws.addCell(label2_1);
			Label label2_2 = new Label(2, index, "女方姓名", wcfFC2);
			ws.addCell(label2_2);
			Label label2_3 = new Label(3, index, "禁忌症", wcfFC2);
			ws.addCell(label2_3);
			Label label2_4 = new Label(4, index, "节育措施", wcfFC2);
			ws.addCell(label2_4);
			Label label2_5 = new Label(5, index, "采取年月日", wcfFC2);
			ws.addCell(label2_5);
			Label label2_6 = new Label(6, index, "手术地点", wcfFC2);
			ws.addCell(label2_6);
			Label label2_7 = new Label(7, index, "出生年月日", wcfFC2);
			ws.addCell(label2_7);
			Label label2_8 = new Label(8, index, "民族", wcfFC2);
			ws.addCell(label2_8);
			Label label2_9 = new Label(9, index, "孩次", wcfFC2);
			ws.addCell(label2_9);
			Label label2_10 = new Label(10, index, "性别", wcfFC2);
			ws.addCell(label2_10);
			Label label2_011 = new Label(11, index, "计划内外", wcfFC2);
			ws.addCell(label2_011);
			Label label2_12 = new Label(12, index, "是否存活", wcfFC2);
			ws.addCell(label2_12);
			Label label2_13 = new Label(13, index, "准生证号", wcfFC2);
			ws.addCell(label2_13);
			Label label2_14 = new Label(14, index, "备注", wcfFC2);
			ws.addCell(label2_14);
			index++;

			for (models.BirthOligogenics data : lists) {
				Label field3_0 = new Label(0, index, data.flow);
				ws.addCell(field3_0);

				if (data.oligogenics != null) {
					Label field3_1 = new Label(1, index, data.oligogenics.woman);
					ws.addCell(field3_1);
				} else {
					if (data.birthStatus != null) {
						Label field3_1 = new Label(1, index,
								data.birthStatus.man);
						ws.addCell(field3_1);
					}
				}

				if (data.oligogenics != null) {
					Label field3_2 = new Label(2, index, data.oligogenics.woman);
					ws.addCell(field3_2);
				} else {
					if (data.birthStatus != null) {
						Label field3_2 = new Label(2, index,
								data.birthStatus.woman);
						ws.addCell(field3_2);
					}
				}

				Label field3_3 = new Label(3, index, data.taboo);
				ws.addCell(field3_3);

				if (data.oligogenics != null) {
					Label field3_4 = new Label(4, index,
							data.oligogenics.measure);
					ws.addCell(field3_4);
					Label field3_5 = new Label(5, index, utils.Utils
							.formatDate(data.oligogenics.measureDate));
					ws.addCell(field3_5);
					Label field3_6 = new Label(6, index,
							data.oligogenics.measureLocal);
					ws.addCell(field3_6);
				}

				if (data.birthStatus != null) {
					Label field3_7 = new Label(7, index, utils.Utils
							.formatDate(data.birthStatus.birth));
					ws.addCell(field3_7);
					Label field3_8 = new Label(8, index,
							data.birthStatus.nation);
					ws.addCell(field3_8);
					jxl.write.Number field3_9 = new jxl.write.Number(9, index,
							data.birthStatus.size);
					ws.addCell(field3_9);
					Label field3_10 = new Label(10, index,
							data.birthStatus.gender);
					ws.addCell(field3_10);
					Label field3_11 = new Label(11, index,
							data.birthStatus.plan ? "计划内" : "计划外");
					ws.addCell(field3_11);
					Label field3_12 = new Label(12, index,
							data.birthStatus.survival ? "存活" : "死亡");
					ws.addCell(field3_12);
					Label field3_13 = new Label(13, index,
							data.birthStatus.code);
					ws.addCell(field3_13);
				}
				Label field3_14 = new Label(14, index, data.notes);
				ws.addCell(field3_14);
				index++;
			}

			Label label4_0 = new Label(0, index, "填表人", wcfFC2);
			ws.addCell(label4_0);
			ws.mergeCells(0, index, 1, index);
			Label field4_0 = new Label(2, index, Secure.getAdmin().name);
			ws.addCell(field4_0);
			ws.mergeCells(2, index, 9, index);
			Label label4_1 = new Label(10, index, "填表日期", wcfFC2);
			ws.addCell(label4_1);
			ws.mergeCells(10, index, 11, index);
			Label field4_1 = new Label(12, index, utils.Utils
					.formatDate(new Date()));
			ws.addCell(field4_1);
			ws.mergeCells(12, index, 14, index);
			wbook.write();
			wbook.close();
			renderBinary(f, fileName);
		} catch (Exception exception) {
			exception.printStackTrace();
			error(exception);
		}
	}

	/** 获取数据列表. */
	public static void list(String keyword, String department, int page,
			int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		List<models.BirthOligogenics> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.BirthOligogenics.find();
				if (keyword != null && !"".equals(keyword))
					query.or(query.criteria("birthStatus.man")
							.contains(keyword), query.criteria(
							"birthStatus.woman").contains(keyword), query
							.criteria("oligogenics.man").contains(keyword),
							query.criteria("oligogenics.woman").contains(
									keyword));
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
	private static List<models.BirthOligogenics> findAll(String keyword,
			int page, int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.BirthOligogenics> result = new ArrayList<models.BirthOligogenics>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.BirthOligogenics.find("byDepartment",
					department);
			if (keyword != null && !"".equals(keyword))
				query.or(query.criteria("birthStatus.man").contains(keyword),
						query.criteria("birthStatus.woman").contains(keyword),
						query.criteria("oligogenics.man").contains(keyword),
						query.criteria("oligogenics.woman").contains(keyword));
			List<models.BirthOligogenics> data = query.asList();
			if (rows > 0) {
				if (size >= offset && size < end) {
					for (models.BirthOligogenics flup : data) {
						result.add(flup);
						size++;
					}
				} else {
					size += data.size();
				}
				if (size >= end) {
					break;
				}
			} else {
				result.addAll(data);
			}
		}
		return result;
	}

	/** 添加操作 */
	public static void add(String flow, String man, String woman,
			String nation, String birth, int size, String gender, Boolean plan,
			Boolean survival, String code, String measure, String taboo,
			String measureDate, String measureLocal, String notes,
			String department) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
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
		newData.taboo = taboo;
		newData.birthStatus = newBirth;
		newData.oligogenics = newOlig;
		newData.createAt = newDate;
		newData.modifyAt = newDate;
		newData.department = dep;
		newData.notes = notes;
		newData.save();
		renderText(JSON.serialize(result));
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
		renderText(JSON.serialize(result));
	}

	/** 修改操作 */
	public static void update(String id, String flow, String man, String woman,
			String nation, String birth, int size, String taboo, String gender,
			Boolean plan, Boolean survival, String code, String measure,
			String measureDate, String measureLocal, String notes,
			String department) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
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
			cur.taboo = taboo;
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
		renderText(JSON.serialize(result));
	}
}
