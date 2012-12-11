package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.util.JSON;

import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import utils.Utils;
/** 出租房屋登记管理 */
public class RentalHouse extends Controller {
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
		List<models.RentalHouse> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.RentalHouse.find();
				if (keyword != null && !"".equals(keyword))
					query.or(query.criteria("name").equal(keyword), 
							query.criteria("user").equal(keyword));
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, page, rows);
			}
		}

		String fileName = "出租房屋登记-" + Secure.getAdmin().department.name + ".xls";
		File f = new File("./excels/" + fileName);
		try {
			if (f.exists())
				f.createNewFile();
			WritableWorkbook wbook = Workbook.createWorkbook(f);
			WritableSheet ws = wbook.createSheet("出租房屋登记", 0);
			
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);   
			WritableCellFormat wcfFC = new WritableCellFormat(wfont); 
			wcfFC.setAlignment(Alignment.CENTRE);
			int index = 0;
			Label label0_0 = new Label(0, index, "出租房屋登记",wcfFC);
			ws.addCell(label0_0);
			ws.mergeCells(0, index, 9, 0); 
			index++;
			Label label0_1 = new Label(0, index, "单位");
			ws.addCell(label0_1);
			Label label1_1 = new Label(1, index,
					Secure.getAdmin().department.name);
			ws.addCell(label1_1);
			index++;
			Label label0_2 = new Label(0, index, "单位");
			ws.addCell(label0_2);
			Label label1_2 = new Label(1, index, "职工姓名");
			ws.addCell(label1_2);
			Label label2_2 = new Label(2, index, "出租房屋住址");
			ws.addCell(label2_2);
			Label label3_2 = new Label(3, index, "租房人姓名");
			ws.addCell(label3_2);
			Label label4_2 = new Label(4, index, "家庭人口");
			ws.addCell(label4_2);
			Label label5_2 = new Label(5, index, "孩  次");
			ws.addCell(label5_2);
			Label label6_2 = new Label(6, index, "婚育证明");
			ws.addCell(label6_2);
			Label label7_2 = new Label(7, index, "户籍地");
			ws.addCell(label7_2);
			Label label8_2 = new Label(8, index, "备注");
			ws.addCell(label8_2);

			if (lists != null) {
				for (models.RentalHouse rentalHouse : lists) {
					index++;
					Label label0_2_r = new Label(0, index, rentalHouse.department.name);
					ws.addCell(label0_2_r);
					Label label1_2_r = new Label(1, index, rentalHouse.name);
					ws.addCell(label1_2_r);
					Label label2_2_r = new Label(2, index, rentalHouse.address);
					ws.addCell(label2_2_r);
					Label label3_2_r = new Label(3, index, rentalHouse.user);
					ws.addCell(label3_2_r);
					jxl.write.Number label4_2_r = new jxl.write.Number(4,
							index, rentalHouse.size);
					ws.addCell(label4_2_r);
					jxl.write.Number label5_2_r = new jxl.write.Number(5,
							index, rentalHouse.childrenSize);
					ws.addCell(label5_2_r);
					Label label6_2_r = new Label(6, index, rentalHouse.childbirth);
					ws.addCell(label6_2_r);
					Label label7_2_r = new Label(7, index, rentalHouse.register);
					ws.addCell(label7_2_r);
					Label label8_2_r = new Label(8, index, rentalHouse.notes);
					ws.addCell(label8_2_r);
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
		List<models.RentalHouse> lists = null;
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.RentalHouse.find();
				if (keyword != null && !"".equals(keyword))
					query.or(query.criteria("name").equal(keyword), 
							query.criteria("user").equal(keyword));
				query.filter("department", dep);
				lists = query.limit(rows).offset(page * rows - rows).asList();
			} else {
				lists = findAll(keyword, page, rows);
			}
		}
		List<Object> datas = new ArrayList<Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (models.RentalHouse mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.RentalHouse.count());
		result.put("size", rows);
		renderJSON(result);
	}
	
	/** 获取当前操作者的全部数据. */
	private static List<models.RentalHouse> findAll(String keyword, int page, int rows) {
		List<models.Department> departments = Secure.getDepartments();
		List<models.RentalHouse> result = new ArrayList<models.RentalHouse>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.RentalHouse.find("byDepartment", department);
			if (keyword != null && !"".equals(keyword))
				query.or(query.criteria("name").equal(keyword), 
					query.criteria("user").equal(keyword));
			List<models.RentalHouse> data = query.asList();
			if (size >= offset && size < end) {
				for (models.RentalHouse house : data) {
					result.add(house);
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
	public static void add(String name, String address, String user, int size, int childrenSize, 
			String childbirth, String register, String notes, String department) {
		Map<String, String> result = new HashMap<String, String>();
		if (department == null || "".equals(department)) {
			result.put("error", "出租房屋登记添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		models.RentalHouse newData = new models.RentalHouse();
		Date newDate = new Date();
		newData.name = name;
		newData.size = size;
		newData.childrenSize = childrenSize;
		newData.childbirth = childbirth;
		newData.address = address;
		newData.user = user;
		newData.register = register;
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
			models.RentalHouse.findById(id).delete();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	/** 修改操作 */
	public static void update(String id, String name, String address, String user, int size,  
			int childrenSize, String childbirth, String register, String notes, String department) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (department == null || "".equals(department)) {
			result.put("error", "出租房屋登记添加失败，请选择该所属部门。<br>如果还未创建部门，请先创建部门后进行添加");
			renderText(JSON.serialize(result));
		}
		try {
			models.RentalHouse cur = models.RentalHouse.findById(id);
			cur.name = name;
			cur.size = size;
			cur.childrenSize = childrenSize;
			cur.childbirth = childbirth;
			cur.address = address;
			cur.user = user;
			cur.register = register;
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
