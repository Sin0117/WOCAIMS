package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.util.JSON;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import play.mvc.Util;
import utils.Utils;
/** 育龄妇女基本信息卡管理 */
public class WomenCard extends Controller {
	public static final int PAGE_SIZE = 20;
	/** 管理入口 .*/
	public static void index(String keyword, String department, int page, int size) {
		List<models.Department> divisions = Secure.getDepartments();
		render(keyword, department, divisions);
	}
	
	/** 导出excel. */
	public static void report(String keyword, String department, String id) {
		String fileName = "育龄妇女基本信息-" + Secure.getAdmin().department.name + ".xls";
		File f = new File("./excels/" + fileName);
		int index = 0;
		models.WomenCard data = models.WomenCard.findById(id);
	    try {
	    	if (f.exists())
	    		f.createNewFile();
	        WritableWorkbook wbook = Workbook.createWorkbook(f);
	        WritableSheet ws = wbook.createSheet("育龄妇女基础信息卡", 0);
	        Label label0_0 = new Label(0, index, "单位");
	        ws.addCell(label0_0);
	        Label field0_0 = new Label(2, index, data.department.name);
	        ws.addCell(field0_0);
	        Label label0_1 = new Label(4, index, "单位编码");
	        ws.addCell(label0_1);
	        Label field0_1 = new Label(5, index, data.department.code);
	        ws.addCell(field0_1);
	        index += 2;
	        
	        Label label1_0 = new Label(0, index, "育龄妇女编码");
	        ws.addCell(label1_0);
	        Label field1_0 = new Label(2, index, data.womanCode);
	        ws.addCell(field1_0);
	        Label label1_1 = new Label(4, index, "户编码");
	        ws.addCell(label1_1);
	        Label field1_1 = new Label(5, index, utils.Utils.formatDate(data.createAt));
	        ws.addCell(field1_1);
	        Label label1_2 = new Label(8, index, "建卡日期");
	        ws.addCell(label1_2);
	        Label field1_2 = new Label(9, index, utils.Utils.formatDate(data.createAt));
	        ws.addCell(field1_2);
	        index += 2;
	        
	        Label label2_0 = new Label(0, index, "姓名");
	        ws.addCell(label2_0);
	        Label label2_1 = new Label(2, index, "出生日期");
	        ws.addCell(label2_1);
	        Label label2_2 = new Label(3, index, "婚姻状况");
	        ws.addCell(label2_2);
	        Label label2_3 = new Label(4, index, "初婚日期");
	        ws.addCell(label2_3);
	        Label label2_4 = new Label(5, index, "现婚日期");
	        ws.addCell(label2_4);
	        Label label2_5 = new Label(6, index, "职业");
	        ws.addCell(label2_5);
	        Label label2_6 = new Label(7, index, "用工性质");
	        ws.addCell(label2_6);
	        Label label2_7 = new Label(8, index, "民族");
	        ws.addCell(label2_7);
	        Label label2_8 = new Label(9, index, "文化程度");
	        ws.addCell(label2_8);
	        Label label2_9 = new Label(10, index, "户口性质");
	        ws.addCell(label2_9);
	        Label label2_10 = new Label(11, index, "户籍地");
	        ws.addCell(label2_10);
	        index ++;
	        
	        Label label3_0 = new Label(0, index, "妻子");
	        ws.addCell(label3_0);
	        Label field3_0 = new Label(1, index, data.woman);
	        ws.addCell(field3_0);
	        Label field3_1 = new Label(2, index, utils.Utils.formatDate(data.womanBirth));
	        ws.addCell(field3_1);
	        Label field3_2 = new Label(3, index, data.womanMarriage);
	        ws.addCell(field3_2);
	        Label field3_3 = new Label(4, index, utils.Utils.formatDate(data.womanFirstMarriage));
	        ws.addCell(field3_3);
	        Label field3_4 = new Label(5, index, utils.Utils.formatDate(data.womanCurrentMarriage));
	        ws.addCell(field3_4);
	        Label field3_5 = new Label(6, index, data.womanProfession);
	        ws.addCell(field3_5);
	        Label field3_6 = new Label(7, index, data.womanNature);
	        ws.addCell(field3_6);
	        Label field3_7 = new Label(8, index, data.womanNation);
	        ws.addCell(field3_7);
	        Label field3_8 = new Label(9, index, data.womanCulture);
	        ws.addCell(field3_8);
	        Label field3_9 = new Label(10, index, data.womanProperties);
	        ws.addCell(field3_9);
	        Label field3_10 = new Label(11, index, data.womanRegister);
	        ws.addCell(field3_10);
	        index ++;
	        
	        Label label4_0 = new Label(0, index, "丈夫");
	        ws.addCell(label4_0);
	        Label field4_0 = new Label(1, index, data.man);
	        ws.addCell(field4_0);
	        Label field4_1 = new Label(2, index, utils.Utils.formatDate(data.manBirth));
	        ws.addCell(field4_1);
	        Label field4_2 = new Label(3, index, data.manMarriage);
	        ws.addCell(field4_2);
	        Label field4_3 = new Label(4, index, utils.Utils.formatDate(data.manFirstMarriage));
	        ws.addCell(field4_3);
	        Label field4_4 = new Label(5, index, utils.Utils.formatDate(data.manCurrentMarriage));
	        ws.addCell(field4_4);
	        Label field4_5 = new Label(6, index, data.manProfession);
	        ws.addCell(field4_5);
	        Label field4_6 = new Label(7, index, data.manNature);
	        ws.addCell(field4_6);
	        Label field4_7 = new Label(8, index, data.manNation);
	        ws.addCell(field4_7);
	        Label field4_8 = new Label(9, index, data.manCulture);
	        ws.addCell(field4_8);
	        Label field4_9 = new Label(10, index, data.manProperties);
	        ws.addCell(field4_9);
	        Label field4_10 = new Label(11, index, data.manRegister);
	        ws.addCell(field4_10);
	        index ++;
	        
	        Label label5_0 = new Label(0, index, "居住地址");
	        ws.addCell(label5_0);
	        Label label5_1 = new Label(2, index, "居住性质");
	        ws.addCell(label5_1);
	        Label label5_2 = new Label(3, index, "流动日期");
	        ws.addCell(label5_2);
	        Label label5_3 = new Label(4, index, "流出地址");
	        ws.addCell(label5_3);
	        Label label5_4 = new Label(5, index, "工作单位");
	        ws.addCell(label5_4);
	        Label label5_5 = new Label(8, index, "联系电话");
	        ws.addCell(label5_5);
	        Label label5_6 = new Label(10, index, "身份证号码");
	        ws.addCell(label5_6);
	        index ++;
	        
	        Label label6_0 = new Label(0, index, data.womanLive);
	        ws.addCell(label6_0);
	        Label label6_1 = new Label(2, index, data.womanLiveNature);
	        ws.addCell(label6_1);
	        Label label6_2 = new Label(3, index, utils.Utils.formatDate(data.womanFlowDate));
	        ws.addCell(label6_2);
	        Label label6_3 = new Label(4, index, data.womanFlowAddress);
	        ws.addCell(label6_3);
	        Label label6_4 = new Label(5, index, data.department.name);
	        ws.addCell(label6_4);
	        Label label6_5 = new Label(8, index, data.womanTel);
	        ws.addCell(label6_5);
	        Label label6_6 = new Label(10, index, data.womanIdentification);
	        ws.addCell(label6_6);
	        index ++;
	        
	        Label label7_0 = new Label(0, index, data.manLive);
	        ws.addCell(label7_0);
	        Label label7_1 = new Label(2, index, data.manLiveNature);
	        ws.addCell(label7_1);
	        Label label7_2 = new Label(3, index, utils.Utils.formatDate(data.manFlowDate));
	        ws.addCell(label7_2);
	        Label label7_3 = new Label(4, index, data.manFlowAddress);
	        ws.addCell(label7_3);
	        Label label7_4 = new Label(5, index, data.manWork);
	        ws.addCell(label7_4);
	        Label label7_5 = new Label(8, index, data.manTel);
	        ws.addCell(label7_5);
	        Label label7_6 = new Label(10, index, data.manIdentification);
	        ws.addCell(label7_6);
	        index ++;
	        
	        Label label8_0 = new Label(0, index, "现有子女");
	        ws.addCell(label8_0);
	        index ++;
	        
	        Label label9_0 = new Label(0, index, "姓名");
	        ws.addCell(label9_0);
	        Label label9_1 = new Label(2, index, "生育孩次");
	        ws.addCell(label9_1);
	        Label label9_2 = new Label(3, index, "出生日期");
	        ws.addCell(label9_2);
	        Label label9_3 = new Label(4, index, "子女性别");
	        ws.addCell(label9_3);
	        Label label9_4 = new Label(5, index, "生育状况");
	        ws.addCell(label9_4);
	        Label label9_5 = new Label(6, index, "计划内外");
	        ws.addCell(label9_5);
	        Label label9_6 = new Label(7, index, "计外原因");
	        ws.addCell(label9_6);
	        Label label9_7 = new Label(9, index, "是否处罚");
	        ws.addCell(label9_7);
	        Label label9_8 = new Label(10, index, "是否晚育");
	        ws.addCell(label9_8);
	        Label label9_9 = new Label(11, index, "户口性质");
	        ws.addCell(label9_9);
	        index ++;
	        
	        if (data.childrens != null && data.childrens.size() > 0) {
        		for (models.BirthStatus children : data.childrens) {
	        		Label children_0 = new Label(0, index, children.name);
	    	        ws.addCell(children_0);
	    	        jxl.write.Number children_1 = new jxl.write.Number(2, index, children.size);
	    	        ws.addCell(children_1);
	    	        Label children_2 = new Label(3, index, utils.Utils.formatDate(children.birth));
	    	        ws.addCell(children_2);
	    	        Label children_3 = new Label(4, index, children.gender);
	    	        ws.addCell(children_3);
	    	        Label children_4 = new Label(5, index, children.status);
	    	        ws.addCell(children_4);
	    	        Label children_5 = new Label(6, index, children.plan ? "计划内" : "计划外");
	    	        ws.addCell(children_5);
	    	        Label children_6 = new Label(7, index, children.planInfo);
	    	        ws.addCell(children_6);
	    	        Label children_7 = new Label(9, index, children.punish ? "处罚" : "未处罚");
	    	        ws.addCell(children_7);
	    	        Label children_8 = new Label(10, index, children.late ? "晚育" : "未晚育");
	    	        ws.addCell(children_8);
	    	        Label children_9 = new Label(11, index, children.properties);
	    	        ws.addCell(children_9);
	    	        index ++;
	        	}
	        }
	        index ++;
	        
	        Label label10_0 = new Label(0, index, "避孕节育信息状况");
	        ws.addCell(label10_0);
	        Label label10_1 = new Label(2, index, "开始日期");
	        ws.addCell(label10_1);
	        Label label10_2 = new Label(5, index, "领取独生子女证时间");
	        ws.addCell(label10_2);
	        Label label10_3 = new Label(7, index, "独生子女证编号");
	        ws.addCell(label10_3);
	        Label label10_4 = new Label(9, index, "备注");
	        ws.addCell(label10_4);
	        index ++;
	        
	        if (data.oligogenics != null) {
		        Label field11_0 = new Label(0, index, data.oligogenics.measure);
		        ws.addCell(field11_0);
		        Label field11_1 = new Label(2, index, utils.Utils.formatDate(data.oligogenics.measureDate));
		        ws.addCell(field11_1);
	        }
	        if (data.onlyChildren != null) {
		        Label field11_2 = new Label(5, index, utils.Utils.formatDate(data.onlyChildren.date));
		        ws.addCell(field11_2);
		        Label field11_3 = new Label(7, index, data.onlyChildren.onlyChildrenCode);
		        ws.addCell(field11_3);
	        }
	        Label field11_4 = new Label(9, index, data.notes);
	        ws.addCell(field11_4);
	        
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
		List<models.WomenCard> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.WomenCard.find();
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
		for (models.WomenCard mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.WomenCard.count());
		result.put("size", rows);
		renderJSON(result);
	}
	
	/** 获取当前操作者的全部数据. */
	private static List<models.WomenCard> findAll(String keyword, int page, int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.WomenCard> result = new ArrayList<models.WomenCard>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.WomenCard.find("byDepartment", department);
			if (keyword != null && !"".equals(keyword))
				query.or(query.criteria("birthStatus.man").contains(keyword), query.criteria("birthStatus.woman").contains(keyword),
						query.criteria("oligogenics.man").contains(keyword), query.criteria("oligogenics.woman").contains(keyword));
			List<models.WomenCard> data = query.asList();
			if (size >= offset && size < end) {
				for (models.WomenCard flup : data) {
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
	public static void add(String woman, String womanCode, String womanBirth, String womanNation, String womanMarriage,
			String womanFirstMarriage, String womanCurrentMarriage, String womanProfession, 
			String womanNature, String womanCulture, String womanProperties, String womanRegister,
			String womanLive, String womanLiveNature, String womanFlowNature, String womanFlowDate, String manFlowDate,
			String womanFlowAddress, String department, String womanTel, String womanIdentification,
			String man, String manBirth, String manNation, String manMarriage, String manFirstMarriage,
			String manCurrentMarriage, String manProfession, String manNature, String manCulture, 
			String manProperties, String manRegister, String manLive, String manLiveNature,
			String manFlowNature, String manFlowAddress, String manWork, String manTel, String manIdentification, 
			String childrens, String measure, String measureDate, String date, String onlyChildrenCode,
			String cash, String houseCode, String notes) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		models.WomenCard existCard = isExist(womanCode, department, null);
		if (existCard != null) {
			result.put("error", "人员添加失败，" + existCard.woman + "(" + existCard.womanCode + ")已经存在！");
		} else {
			Date newDate = new Date();
			models.Department dep = models.Department.findById(department);
			models.WomenCard newData = new models.WomenCard();
			if (childrens != null && !"".equals(childrens)) {
				List<String> childrenArr = Utils.toStringArray(childrens, "#S#");
				List<models.BirthStatus> childrenList = new ArrayList<models.BirthStatus>();
				for (int i = 0; i < childrenArr.size(); i ++) {
					// data format: name,size,nation,code,birth,gender,survival,plan,id|... 
					String[] childrenAt = childrenArr.get(i).split("#,#", 9);
					models.BirthStatus newBirth = new models.BirthStatus();
					newBirth.man = man;
					newBirth.woman = woman;
					if (childrenAt.length > 0)
						newBirth.name = childrenAt[0];
					if (childrenAt.length > 2)
						newBirth.nation = childrenAt[2];
					if (childrenAt.length > 4)
						newBirth.birth = Utils.parseDate(childrenAt[4]);
					if (childrenAt.length > 1)
						newBirth.size = Integer.valueOf(childrenAt[1].length() > 0 ? childrenAt[1] : "1");
					if (childrenAt.length > 5)
						newBirth.gender = childrenAt[5];
					if (childrenAt.length > 7)
						newBirth.plan = Boolean.valueOf(childrenAt[7]);
					if (childrenAt.length > 6)
						newBirth.survival = Boolean.valueOf(childrenAt[6]);
					if (childrenAt.length > 3)
						newBirth.code = childrenAt[3];
					newBirth.createAt = newDate;
					newBirth.modifyAt = newDate;
					newBirth.department = dep;
					newBirth.save();
					childrenList.add(newBirth);
					
					if(i == 0 && childrenArr.size() == 1) {
						models.OnlyChildren newOnly = new models.OnlyChildren();
						newOnly.man = man;
						newOnly.woman = woman;
						if (childrenAt.length > 2)
							newOnly.nation = childrenAt[2];
						if (childrenAt.length > 4)
							newOnly.birth = Utils.parseDate(childrenAt[4]);
						newOnly.date = Utils.parseDate(date);
						newOnly.onlyChildrenCode = onlyChildrenCode;
						newOnly.measure = measure;
						newOnly.cash = cash;
						newOnly.createAt = newDate;
						newOnly.modifyAt = newDate;
						newOnly.department = models.Department.findById(department);
						newData.onlyChildren = newOnly.save();
					}
				}
				newData.childrens = childrenList;
			}
			
			if (Utils.checkString(measure) && Utils.checkString(measureDate)) {
				models.Oligogenics newOlig = new models.Oligogenics();
				newOlig.man = man;
				newOlig.woman = woman;
				newOlig.nation = womanNation;
				newOlig.measureDate = Utils.parseDate(measureDate);
				newOlig.measure = measure;
				newOlig.createAt = newDate;
				newOlig.modifyAt = newDate;
				newOlig.department = dep;
				newData.oligogenics = newOlig.save();
			}
			
			newData.womanCode = womanCode;
			newData.houseCode = houseCode;
			newData.woman = woman;
			newData.man = man;
			newData.womanBirth = Utils.parseDate(womanBirth);
			newData.manBirth = Utils.parseDate(manBirth);
			newData.womanMarriage = womanMarriage;
			newData.manMarriage = manMarriage;
			newData.womanFirstMarriage = Utils.parseDate(womanFirstMarriage);
			newData.manFirstMarriage = Utils.parseDate(manFirstMarriage);
			newData.womanCurrentMarriage = Utils.parseDate(womanCurrentMarriage);
			newData.manCurrentMarriage = Utils.parseDate(manCurrentMarriage);
			newData.womanProfession = womanProfession;
			newData.manProfession = manProfession;
			newData.womanNature = womanNature;
			newData.manNature = manNature;
			newData.womanNation = womanNation;
			newData.manNation = manNation;
			newData.womanCulture = womanCulture;
			newData.manCulture = manCulture;
			newData.womanProperties = womanProperties;
			newData.manProperties = manProperties;
			newData.womanRegister = womanRegister;
			newData.manRegister = manRegister;
			newData.womanLive = womanLive;
			newData.manLive = manLive;
			newData.womanLiveNature = womanLiveNature;
			newData.manLiveNature = manLiveNature;
			newData.manFlowNature = manFlowNature;
			newData.womanFlowDate = Utils.parseDate(womanFlowDate);
			newData.manFlowDate = Utils.parseDate(manFlowDate);
			newData.womanFlowAddress = womanFlowAddress;
			newData.manFlowAddress = manFlowAddress;
			newData.manWork = manWork;
			newData.womanTel = womanTel;
			newData.manTel = manTel;
			newData.womanIdentification = womanIdentification;
			newData.manIdentification = manIdentification;
			
			newData.createAt = newDate;
			newData.modifyAt = newDate;
			newData.department = dep;
			newData.notes = notes;
			newData.save();
		}
		renderText(JSON.serialize(result));
	}
	
	/** 删除操作 */
	public static void del(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			models.WomenCard.findById(id).delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	/** 修改操作 */
	public static void update(String id, String woman, String womanCode, String womanBirth, String womanNation, String womanMarriage,
			String womanFirstMarriage, String womanCurrentMarriage, String womanProfession, 
			String womanNature, String womanCulture, String womanProperties, String womanRegister,
			String womanLive, String womanLiveNature, String womanFlowNature, String womanFlowDate, String manFlowDate,
			String womanFlowAddress, String department, String womanTel, String womanIdentification,
			String man, String manBirth, String manNation, String manMarriage, String manFirstMarriage,
			String manCurrentMarriage, String manProfession, String manNature, String manCulture, 
			String manProperties, String manRegister, String manLive, String manLiveNature,
			String manFlowNature, String manFlowAddress, String manWork, String manTel, String manIdentification, 
			String childrens, String measure, String measureDate, String date, String onlyChildrenCode,
			String cash, String houseCode, String notes) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "记录添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		try {
			models.Department dep = models.Department.findById(department);
			Date newDate = new Date();
			models.WomenCard cur = models.WomenCard.findById(id);
			
			if (childrens != null && !"".equals(childrens)) {
				List<String> childrenArr = Utils.toStringArray(childrens, "#S#");
				List<models.BirthStatus> childrenList = new ArrayList<models.BirthStatus>();
				for (int i = 0; i < childrenArr.size(); i ++) {
					// data format: name,size,nation,code,birth,gender,survival,plan|... 
					String[] childrenAt = childrenArr.get(i).split("#,#", 9);
					models.BirthStatus newBirth = new models.BirthStatus();
					newBirth.man = man;
					newBirth.woman = woman;
					if (childrenAt.length > 0)
						newBirth.name = childrenAt[0];
					if (childrenAt.length > 2)
						newBirth.nation = childrenAt[2];
					if (childrenAt.length > 4)
						newBirth.birth = Utils.parseDate(childrenAt[4]);
					if (childrenAt.length > 1)
						newBirth.size = Integer.valueOf(childrenAt[1].length() > 0 ? childrenAt[1] : "1");
					if (childrenAt.length > 5)
						newBirth.gender = childrenAt[5];
					if (childrenAt.length > 7)
						newBirth.plan = Boolean.valueOf(childrenAt[7]);
					if (childrenAt.length > 6)
						newBirth.survival = Boolean.valueOf(childrenAt[6]);
					newBirth.code = childrenAt[3];
					newBirth.createAt = newDate;
					newBirth.modifyAt = newDate;
					newBirth.department = dep;
					newBirth.save();
					childrenList.add(newBirth);
					
					if(i == 0 && childrenArr.size() == 1) {
						models.OnlyChildren newOnly = null;
						if (cur.onlyChildren == null) {
							newOnly = new models.OnlyChildren();
							newOnly.createAt = newDate;
						} else {
							newOnly = cur.onlyChildren;
						}
						newOnly.man = man;
						newOnly.woman = woman;
						if (childrenAt.length > 2)
							newOnly.nation = childrenAt[2];
						if (childrenAt.length > 4)
							newOnly.birth = Utils.parseDate(childrenAt[4]);
						newOnly.date = Utils.parseDate(date);
						newOnly.onlyChildrenCode = onlyChildrenCode;
						newOnly.measure = measure;
						newOnly.cash = cash;
						newOnly.modifyAt = newDate;
						newOnly.department = models.Department.findById(department);
						cur.onlyChildren = newOnly.save();
					} else {
						if (cur.onlyChildren != null) {
							cur.onlyChildren.delete();
							cur.onlyChildren = null;
						}
					}
				}
				cur.childrens = childrenList;
			}
			
			models.Oligogenics newOlig = null;
			if (cur.oligogenics == null) {
				newOlig = new models.Oligogenics();
				newOlig.createAt = newDate;
			} else {
				newOlig = cur.oligogenics;
			}
			newOlig.man = man;
			newOlig.woman = woman;
			newOlig.nation = womanNation;
			newOlig.measureDate = Utils.parseDate(measureDate);
			newOlig.measure = measure;
			newOlig.modifyAt = newDate;
			newOlig.department = dep;
			newOlig = newOlig.save();
			
			cur.womanCode = womanCode;
			cur.houseCode = houseCode;
			cur.woman = woman;
			cur.man = man;
			cur.womanBirth = Utils.parseDate(womanBirth);
			cur.manBirth = Utils.parseDate(manBirth);
			cur.womanMarriage = womanMarriage;
			cur.manMarriage = manMarriage;
			cur.womanFirstMarriage = Utils.parseDate(womanFirstMarriage);
			cur.manFirstMarriage = Utils.parseDate(manFirstMarriage);
			cur.womanCurrentMarriage = Utils.parseDate(womanCurrentMarriage);
			cur.manCurrentMarriage = Utils.parseDate(manCurrentMarriage);
			cur.womanProfession = womanProfession;
			cur.manProfession = manProfession;
			cur.womanNature = womanNature;
			cur.manNature = manNature;
			cur.womanNation = womanNation;
			cur.manNation = manNation;
			cur.womanCulture = womanCulture;
			cur.manCulture = manCulture;
			cur.womanProperties = womanProperties;
			cur.manProperties = manProperties;
			cur.womanRegister = womanRegister;
			cur.manRegister = manRegister;
			cur.womanLive = womanLive;
			cur.manLive = manLive;
			cur.womanLiveNature = womanLiveNature;
			cur.manLiveNature = manLiveNature;
			cur.manFlowNature = manFlowNature;
			cur.womanFlowDate = Utils.parseDate(womanFlowDate);
			cur.manFlowDate = Utils.parseDate(manFlowDate);
			cur.womanFlowAddress = womanFlowAddress;
			cur.manFlowAddress = manFlowAddress;
			cur.manWork = manWork;
			cur.womanTel = womanTel;
			cur.manTel = manTel;
			cur.womanIdentification = womanIdentification;
			cur.manIdentification = manIdentification;
			
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
	
	@Util
	public static models.WomenCard isExist(final String code, final String department, final String id) {
		models.Department dep = models.Department.findById(department);
		MorphiaQuery q = models.WomenCard.find("byDepartment", dep).filter("womanCode", code);
		if (id != null && !"".equals(id)) {
			List<models.WomenCard> exists = q.asList();
			for (models.WomenCard exist : exists) {
				if (!id.equals(exist.getId().toString()))
					return exist;
			}
			return null;
		} else
			return q.get();
	}
}
