package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Alignment;
import jxl.write.Colour;
import jxl.write.Label;
import jxl.write.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import play.mvc.With;
import utils.Utils;

import com.mongodb.util.JSON;

/** 男职工登记情况管理 */
@With(Secure.class)
public class Workers extends Controller {
	public static final int PAGE_SIZE = 20;

	/** 管理入口 . */
	public static void index(String keyword, String department, int page,
			int size) {
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

			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK);
			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
			wcfFC.setAlignment(Alignment.CENTRE);
			int index = 0;
			Label label0_0 = new Label(0, index, "男职工登记花名册", wcfFC);
			ws.addCell(label0_0);
			ws.mergeCells(0, index, 17, 0);
			index++;
			Label label0_1 = new Label(0, index, "单位");
			ws.addCell(label0_1);
			Label label1_1 = new Label(1, index,
					Secure.getAdmin().department.name);
			ws.addCell(label1_1);
			index++;
			WritableFont wfont2 = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK);
			WritableCellFormat wcfFC2 = new WritableCellFormat(wfont2);
			wcfFC2.setAlignment(Alignment.CENTRE);
			wcfFC2.setVerticalAlignment(VerticalAlignment.CENTRE);
			Label label0_2 = new Label(0, index, "户主", wcfFC2);
			ws.addCell(label0_2);
			ws.mergeCells(0, index, 0, 3);

			Label label1_2 = new Label(1, index, "人口数", wcfFC2);
			ws.addCell(label1_2);
			ws.mergeCells(1, index, 2, 2);
			Label label2_2 = new Label(1, 3, "汉族", wcfFC2);
			ws.addCell(label2_2);
			Label label3_2 = new Label(2, 3, "少数民族", wcfFC2);
			ws.addCell(label3_2);
			Label label4_2 = new Label(3, index, "已婚育龄妇女	", wcfFC2);
			ws.addCell(label4_2);
			ws.mergeCells(3, index, 4, 2);
			Label label5_2 = new Label(3, 3, "姓名", wcfFC2);
			ws.addCell(label5_2);
			Label label6_2 = new Label(4, 3, "出生年月", wcfFC2);
			ws.addCell(label6_2);
			Label label7_2 = new Label(5, index, "结婚时间", wcfFC2);
			ws.addCell(label7_2);
			ws.mergeCells(5, index, 5, 3);
			Label label8_2 = new Label(6, index, "工作单位", wcfFC2);
			ws.addCell(label8_2);
			ws.mergeCells(6, index, 6, 3);
			Label label9_2 = new Label(7, index, "户口性质", wcfFC2);
			ws.addCell(label9_2);
			ws.mergeCells(7, index, 7, 3);
			Label label10_2 = new Label(8, index, "现有子女", wcfFC2);
			ws.addCell(label10_2);
			ws.mergeCells(8, index, 9, 2);
			Label label11_2 = new Label(8, 3, "男", wcfFC2);
			ws.addCell(label11_2);
			Label label12_2 = new Label(9, 3, "女", wcfFC2);
			ws.addCell(label12_2);
			Label label13_2 = new Label(10, index, "小孩出生年月日", wcfFC2);
			ws.addCell(label13_2);
			ws.mergeCells(10, index, 11, 2);
			Label label14_2 = new Label(10, 3, "最大", wcfFC2);
			ws.addCell(label14_2);
			Label label15_2 = new Label(11, 3, "最小", wcfFC2);
			ws.addCell(label15_2);
			Label label16_2 = new Label(12, index, "禁忌证", wcfFC2);
			ws.addCell(label16_2);
			ws.mergeCells(12, index, 12, 3);
			Label label17_2 = new Label(13, index, "节育情况", wcfFC2);
			ws.addCell(label17_2);
			ws.mergeCells(13, index, 13, 3);
			Label label18_2 = new Label(14, index, "采取时间", wcfFC2);
			ws.addCell(label18_2);
			ws.mergeCells(14, index, 14, 3);
			Label label19_2 = new Label(16, index, "家庭住址", wcfFC2);
			ws.addCell(label19_2);
			ws.mergeCells(16, index, 16, 3);
			Label label20_2 = new Label(17, index, "备注", wcfFC2);
			ws.addCell(label20_2);
			ws.mergeCells(17, index, 17, 3);

			index = 3;
			if (lists != null) {
				for (models.Workers workers : lists) {
					index++;
					Label label0_2_r = new Label(0, index, workers.name);
					ws.addCell(label0_2_r);
					jxl.write.Number label1_2_r = new jxl.write.Number(1,
							index, workers.han);
					ws.addCell(label1_2_r);
					jxl.write.Number label2_2_r = new jxl.write.Number(2,
							index, workers.minority);
					ws.addCell(label2_2_r);
					Label label3_2_r = new Label(3, index, workers.woman);
					ws.addCell(label3_2_r);
					Label label4_2_r = new Label(4, index, utils.Utils
							.formatDate(workers.womanDate));
					ws.addCell(label4_2_r);
					Label label5_2_r = new Label(5, index, utils.Utils
							.formatDate(workers.weddingTime));
					ws.addCell(label5_2_r);
					Label label6_2_r = new Label(6, index,
							workers.department.name);
					ws.addCell(label6_2_r);
					Label label7_2_r = new Label(7, index, workers.type);
					ws.addCell(label7_2_r);
					jxl.write.Number label8_2_r = new jxl.write.Number(8,
							index, workers.boy);
					ws.addCell(label8_2_r);
					jxl.write.Number label9_2_r = new jxl.write.Number(9,
							index, workers.girl);
					ws.addCell(label9_2_r);
					Label label10_2_r = new Label(10, index, utils.Utils
							.formatDate(workers.childrenMaxDate));
					ws.addCell(label10_2_r);
					Label label11_2_r = new Label(11, index, utils.Utils
							.formatDate(workers.childrenMinDate));
					ws.addCell(label11_2_r);
					Label label12_2_r = new Label(12, index,
							workers.contraindication);
					ws.addCell(label12_2_r);
					Label label13_2_r = new Label(13, index, workers.measure);
					ws.addCell(label13_2_r);
					Label label14_2_r = new Label(14, index, utils.Utils
							.formatDate(workers.measureDate));
					ws.addCell(label14_2_r);
					Label label15_2_r = new Label(15, index, workers.live);
					ws.addCell(label15_2_r);
					Label label16_2_r = new Label(16, index, workers.notes);
					ws.addCell(label16_2_r);
					Label label17_2_r = new Label(17, index, workers.notes);
					ws.addCell(label17_2_r);
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
	public static void list(String keyword, String department, int page,
			int rows) {
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
	private static List<models.Workers> findAll(String keyword, int page,
			int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.Workers> result = new ArrayList<models.Workers>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.Workers
					.find("byDepartment", department);
			if (keyword != null && !"".equals(keyword))
				query.filter("name", keyword);
			List<models.Workers> data = query.asList();
			if (size >= offset && size < end) {
				for (models.Workers worker : data) {
					result.add(worker);
					size++;
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
	public static void add(String name, int han, int minority, String woman,
			String womanDate, String weddingTime, String type, int boy,
			int girl, String childrenMinDate, String childrenMaxDate,
			String contraindication, String measure, String measureDate,
			String live, String notes, String department) {
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
	public static void update(String id, String name, int han, int minority,
			String woman, String womanDate, String weddingTime, String type,
			int boy, int girl, String childrenMinDate, String childrenMaxDate,
			String contraindication, String measure, String measureDate,
			String live, String notes, String department) {
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
