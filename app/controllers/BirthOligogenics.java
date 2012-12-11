package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.mongodb.util.JSON;

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
		String fileName = "出生及节育措施花名册-";
		List<models.BirthOligogenics> lists = null;
		if (Utils.checkString(department)) {
			models.Department dep = models.Department.findById(department);
			fileName += dep.name + ".xls";
			MorphiaQuery query = models.BirthOligogenics.find();
			if (keyword != null && !"".equals(keyword))
				query.or(query.criteria("birthStatus.man").contains(keyword), query.criteria("birthStatus.woman").contains(keyword),
						query.criteria("oligogenics.man").contains(keyword), query.criteria("oligogenics.woman").contains(keyword));
			query.filter("department", dep);
			lists = query.asList();
		} else {
			fileName += Secure.getAdmin().department.name + ".xls";
			lists = findAll(keyword, 0, 0);
		}
		File f = new File("./excels/" + fileName);
	    try {
	    	if (f.exists())
	    		f.createNewFile();
	        WritableWorkbook wbook = Workbook.createWorkbook(f);
	        /*
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
	        */
	        wbook.write();
	        wbook.close();
	        renderBinary(f, fileName);
	    } catch (Exception exception) {  
	        // TODO Auto-generated catch block  
	    	exception.printStackTrace();
	    	error(exception);
	    }
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
			if (rows > 0) {
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
		}
		return result;
	}
	
	/** 添加操作 */
	public static void add(String flow, String man, String woman, String nation, String birth, int size,
			String gender, Boolean plan, Boolean survival, String code, String measure, String taboo,
			String measureDate, String measureLocal, String notes, String department) {
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
	public static void update(String id, String flow, String man, String woman, String nation, 
			String birth, int size, String taboo,
			String gender, Boolean plan, Boolean survival, String code, String measure,
			String measureDate, String measureLocal, String notes, String department) {
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
