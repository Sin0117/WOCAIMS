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
import models.enums.AuditType;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import utils.Utils;

import com.google.gson.GsonBuilder;
import com.mongodb.util.JSON;

/** 计生报表管理 */
public class Planning extends Controller {
	public static final int PAGE_SIZE = 20;
	
	public static List<models.Department> deps;
	/** 管理入口 .*/
	public static void index(String keyword, String department, int page, int size) {
		List<models.Department> divisions = Secure.getDepartments();
		render(keyword, department, divisions);
	}
	
	/** 导出excel. */
	public static void report(String keyword,Date date, String department) {
		String titleName = "计划生育基本情况表-"+ Secure.getAdmin().department.name;
		String fileName = titleName + ".xls";	
		File f = new File("./excels/" + fileName);
		try {
			if (f.exists())
				f.createNewFile();
			WritableWorkbook wbook = Workbook.createWorkbook(f);
			WritableSheet ws = wbook.createSheet(titleName, 0);
			
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);   
			WritableCellFormat wcfFC = new WritableCellFormat(wfont); 
			wcfFC.setAlignment(Alignment.CENTRE);
			
			WritableFont wfont2 = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK);
			WritableCellFormat wcfFC2 = new WritableCellFormat(wfont2);
			wcfFC2.setAlignment(Alignment.CENTRE);
			wcfFC2.setVerticalAlignment(VerticalAlignment.CENTRE);
			int index = 0;
			Label label0_0 = new Label(0, index, titleName,wcfFC);
			ws.addCell(label0_0);
			ws.mergeCells(0, index, 14, 0); 
			index++;
			Label label0_1 = new Label(0, index, "填表单位（章）");
			ws.addCell(label0_1);
			ws.mergeCells(0, index, 1, 1); 
			Label label1_1 = new Label(2, index,
					Secure.getAdmin().department.name);
			ws.addCell(label1_1);
			Label label12_1 = new Label(13, index, "公司计划生育表");
			ws.addCell(label12_1);
			
			index++;
			Label label0_2 = new Label(0, index, "项目", wcfFC2);
			ws.addCell(label0_2);
			ws.mergeCells(0, index, 1, 3);
			Label label1_2 = new Label(2, index, "总计", wcfFC2);
			ws.addCell(label1_2);
			ws.mergeCells(2, index, 2, 3);
			Label label2_2 = new Label(3, index, "其中", wcfFC2);
			ws.addCell(label2_2);
			ws.mergeCells(3, index, 4, 2);
			Label label3_2 = new Label(3, 3, "汉族");
			ws.addCell(label3_2);
			Label label4_2 = new Label(4, 3, "少数民族");
			ws.addCell(label4_2);
			Label label5_2 = new Label(5, index, "项目", wcfFC2);
			ws.addCell(label5_2);
			ws.mergeCells(5, index, 6, 3);
			Label label6_2 = new Label(7, index, "总计", wcfFC2);
			ws.addCell(label6_2);
			ws.mergeCells(7, index, 7, 3);
			Label label7_2 = new Label(8, index, "其中", wcfFC2);
			ws.addCell(label7_2);
			ws.mergeCells(8, index, 9, 2);
			Label label8_2 = new Label(8, 3, "汉族");
			ws.addCell(label8_2);
			Label label9_2 = new Label(9, 3, "少数民族");
			ws.addCell(label9_2);
			Label label10_2 = new Label(10, index, "项目", wcfFC2);
			ws.addCell(label10_2);
			ws.mergeCells(10, index, 11, 3);
			Label label11_2 = new Label(12, index, "总计", wcfFC2);
			ws.addCell(label11_2);
			ws.mergeCells(12, index, 12, 3);
			Label label12_2 = new Label(13, index, "其中", wcfFC2);
			ws.addCell(label12_2);
			ws.mergeCells(13, index, 14, 2);
			Label label13_2 = new Label(13, 3, "汉族", wcfFC2);
			ws.addCell(label13_2);
			Label label14_2 = new Label(14, 3, "少数民族", wcfFC2);
			ws.addCell(label14_2);
			
			int cols = 2;
			int rows = 4;
			ws = method(ws, wcfFC2, cols, rows);
			ws = method2(ws, wcfFC2, cols, rows);
			ws = method3(ws, wcfFC2, cols, rows);
			
			Label label69_2 = new Label(0, 21, "单位负责人", wcfFC2);
			ws.addCell(label69_2);
			ws.mergeCells(0, 21, 1, 21);
			Label label69_2_1 = new Label(2, 21, Secure.getAdmin().admin, wcfFC2);
			ws.addCell(label69_2_1);
			
			Label label70_2 = new Label(7, 21, "填表人", wcfFC2);
			ws.addCell(label70_2);
			Label label70_2_1 = new Label(8, 21, Secure.getAdmin().admin, wcfFC2);
			ws.addCell(label70_2_1);
			
			Label label71_2 = new Label(12, 21, "报表时间", wcfFC2);
			ws.addCell(label71_2);
			Label label71_2_1 = new Label(13, 21, utils.Utils
					.formatDate(new Date()), wcfFC2);
			ws.addCell(label71_2_1);

			wbook.write();
			wbook.close();
			renderBinary(f, fileName);
		} catch (Exception exception) {
			exception.printStackTrace();
			error(exception);
		}
	}

	private static WritableSheet method(WritableSheet ws,WritableCellFormat wcfFC2,int cols,int rows){
		try{
			Label label15_2 = new Label(0, 4, "年初人口数", wcfFC2);
			ws.addCell(label15_2);
			ws.mergeCells(0, 4, 1, 4);
			jxl.write.Number label15_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label15_2_1);
			jxl.write.Number label15_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label15_2_2);
			jxl.write.Number label15_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label15_2_3);
			Label label16_2 = new Label(0, 5, "年末人口数", wcfFC2);
			ws.addCell(label16_2);
			ws.mergeCells(0, 5, 1, 5);
			cols = 2;
			jxl.write.Number label16_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label16_2_1);
			jxl.write.Number label16_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label16_2_2);
			jxl.write.Number label16_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label16_2_3);
			Label label17_2 = new Label(0, 6, "男职工数", wcfFC2);
			ws.addCell(label17_2);
			ws.mergeCells(0, 6, 1, 6);
			cols = 2;
			jxl.write.Number label17_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label17_2_1);
			jxl.write.Number label17_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label17_2_2);
			jxl.write.Number label17_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label17_2_3);
			Label label18_2 = new Label(0, 7, "女职工数", wcfFC2);
			ws.addCell(label18_2);
			ws.mergeCells(0, 7, 1, 7);
			cols = 2;
			jxl.write.Number label18_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label18_2_1);
			jxl.write.Number label18_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label18_2_2);
			jxl.write.Number label18_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label18_2_3);
			Label label19_2 = new Label(0, 8, "育龄妇女数", wcfFC2);
			ws.addCell(label19_2);
			ws.mergeCells(0, 8, 1, 8);
			cols = 2;
			jxl.write.Number label19_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label19_2_1);
			jxl.write.Number label19_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label19_2_2);
			jxl.write.Number label19_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label19_2_3);
			Label label20_2 = new Label(0, 9, "已婚育龄妇女数", wcfFC2);
			ws.addCell(label20_2);
			ws.mergeCells(0, 9, 1, 9);
			cols = 2;
			jxl.write.Number label20_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label20_2_1);
			jxl.write.Number label20_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label20_2_2);
			jxl.write.Number label20_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label20_2_3);
			Label label21_2 = new Label(0, 10, "已婚未育妇女数", wcfFC2);
			ws.addCell(label21_2);
			ws.mergeCells(0, 10, 1, 10);
			cols = 2;
			jxl.write.Number label21_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label21_2_1);
			jxl.write.Number label21_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label21_2_2);
			jxl.write.Number label21_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label21_2_3);
			Label label22_2 = new Label(0, 11, "一孩妇女数", wcfFC2);
			ws.addCell(label22_2);
			ws.mergeCells(0, 11, 1, 11);
			cols = 2;
			jxl.write.Number label22_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label22_2_1);
			jxl.write.Number label22_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label22_2_2);
			jxl.write.Number label22_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label22_2_3);
			Label label23_2 = new Label(0, 12, "其中", wcfFC2);
			ws.addCell(label23_2);
			ws.mergeCells(0, 12, 0, 13);
			Label label24_2 = new Label(1, 12, "环扎数", wcfFC2);
			ws.addCell(label24_2);
			cols = 2;
			jxl.write.Number label24_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label24_2_1);
			jxl.write.Number label24_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label24_2_2);
			jxl.write.Number label24_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label24_2_3);
			Label label25_2 = new Label(1, 13, "环扎率", wcfFC2);
			ws.addCell(label25_2);
			cols = 2;
			jxl.write.Number label25_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label25_2_1);
			jxl.write.Number label25_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label25_2_2);
			jxl.write.Number label25_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label25_2_3);
			Label label26_2 = new Label(0, 14, "领独生子女证人数", wcfFC2);
			ws.addCell(label26_2);
			ws.mergeCells(0, 14, 1, 14);
			cols = 2;
			jxl.write.Number label26_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label26_2_1);
			jxl.write.Number label26_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label26_2_2);
			jxl.write.Number label26_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label26_2_3);
			Label label27_2 = new Label(0, 15, "二孩及以上妇女数", wcfFC2);
			ws.addCell(label27_2);
			ws.mergeCells(0, 15, 1, 15);
			cols = 2;
			jxl.write.Number label27_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label27_2_1);
			jxl.write.Number label27_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label27_2_2);
			jxl.write.Number label27_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label27_2_3);
			Label label28_2 = new Label(0, 16, "其中", wcfFC2);
			ws.addCell(label28_2);
			ws.mergeCells(0, 16, 0, 19);
			Label label29_2 = new Label(1, 16, "放环数", wcfFC2);
			ws.addCell(label29_2);
			cols = 2;
			jxl.write.Number label29_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label29_2_1);
			jxl.write.Number label29_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label29_2_2);
			jxl.write.Number label29_2_3 = new jxl.write.Number(cols++, rows++, 30);
			ws.addCell(label29_2_3);
			Label label30_2 = new Label(1, 17, "放环率", wcfFC2);
			ws.addCell(label30_2);
			cols = 2;
			jxl.write.Number label30_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label30_2_1);
			jxl.write.Number label30_2_2 = new jxl.write.Number(cols++, rows, 20);
			ws.addCell(label30_2_2);
			jxl.write.Number label30_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label30_2_3);
			Label label31_2 = new Label(1, 18, "结扎数", wcfFC2);
			ws.addCell(label31_2);
			cols = 2;
			jxl.write.Number label31_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label31_2_1);
			jxl.write.Number label31_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label31_2_2);
			jxl.write.Number label31_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label31_2_3);
			Label label32_2 = new Label(1, 19, "结扎率", wcfFC2);
			ws.addCell(label32_2);
			cols = 2;
			jxl.write.Number label32_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label32_2_1);
			jxl.write.Number label32_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label32_2_2);
			jxl.write.Number label32_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label32_2_3);
		} catch (Exception exception) {
			exception.printStackTrace();
			error(exception);
		}
		return ws;
	}

	private static WritableSheet method2(WritableSheet ws,WritableCellFormat wcfFC2,int cols,int rows){
		try{
			Label label33_2 = new Label(5, 4, "出生", wcfFC2);
			ws.addCell(label33_2);
			ws.mergeCells(5, 4, 6, 4);
			cols = 7;
			jxl.write.Number label33_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label33_2_1);
			jxl.write.Number label33_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label33_2_2);
			jxl.write.Number label33_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label33_2_3);
			Label label34_2 = new Label(5, 5, "其中", wcfFC2);
			ws.addCell(label34_2);
			ws.mergeCells(5, 5, 5, 6);
			Label label35_2 = new Label(6, 5, "一孩", wcfFC2);
			ws.addCell(label35_2);
			cols = 7;
			jxl.write.Number label35_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label35_2_1);
			jxl.write.Number label35_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label35_2_2);
			jxl.write.Number label35_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label35_2_3);
			Label label36_2 = new Label(6, 6, "二孩", wcfFC2);
			ws.addCell(label36_2);
			cols = 7;
			jxl.write.Number label36_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label36_2_1);
			jxl.write.Number label36_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label36_2_2);
			jxl.write.Number label36_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label36_2_3);
			Label label37_2 = new Label(5, 7, "女性初婚", wcfFC2);
			ws.addCell(label37_2);
			ws.mergeCells(5, 7, 6, 7);
			cols = 7;
			jxl.write.Number label37_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label37_2_1);
			jxl.write.Number label37_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label37_2_2);
			jxl.write.Number label37_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label37_2_3);
			Label label38_2 = new Label(5, 8, "23岁以上人数", wcfFC2);
			ws.addCell(label38_2);
			ws.mergeCells(5, 8, 6, 8);
			cols = 7;
			jxl.write.Number label38_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label38_2_1);
			jxl.write.Number label38_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label38_2_2);
			jxl.write.Number label38_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label38_2_3);
			Label label39_2 = new Label(5, 9, "男性初婚", wcfFC2);
			ws.addCell(label39_2);
			ws.mergeCells(5, 9, 6, 9);
			cols = 7;
			jxl.write.Number label39_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label39_2_1);
			jxl.write.Number label39_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label39_2_2);
			jxl.write.Number label39_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label39_2_3);
			Label label40_2 = new Label(5, 10, "25岁以上人数", wcfFC2);
			ws.addCell(label40_2);
			ws.mergeCells(5, 10, 6, 10);
			cols = 7;
			jxl.write.Number label40_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label40_2_1);
			jxl.write.Number label40_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label40_2_2);
			jxl.write.Number label40_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label40_2_3);
			Label label41_2 = new Label(5, 11, "期末选用各种避孕人数", wcfFC2);
			ws.addCell(label41_2);
			ws.mergeCells(5, 11, 6, 11);
			cols = 7;
			jxl.write.Number label41_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label41_2_1);
			jxl.write.Number label41_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label41_2_2);
			jxl.write.Number label41_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label41_2_3);
			Label label42_2 = new Label(5, 12, "其中", wcfFC2);
			ws.addCell(label42_2);
			ws.mergeCells(5, 12, 5, 19);
			Label label43_2 = new Label(6, 12, "男扎", wcfFC2);
			ws.addCell(label43_2);
			cols = 7;
			jxl.write.Number label43_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label43_2_1);
			jxl.write.Number label43_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label43_2_2);
			jxl.write.Number label43_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label43_2_3);
			Label label44_2 = new Label(6, 13, "女扎", wcfFC2);
			ws.addCell(label44_2);
			cols = 7;
			jxl.write.Number label44_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label44_2_1);
			jxl.write.Number label44_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label44_2_2);
			jxl.write.Number label44_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label44_2_3);
			Label label45_2 = new Label(6, 14, "放环", wcfFC2);
			ws.addCell(label45_2);
			cols = 7;
			jxl.write.Number label45_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label45_2_1);
			jxl.write.Number label45_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label45_2_2);
			jxl.write.Number label45_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label45_2_3);
			Label label46_2 = new Label(6, 15, "皮埋", wcfFC2);
			ws.addCell(label46_2);
			cols = 7;
			jxl.write.Number label46_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label46_2_1);
			jxl.write.Number label46_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label46_2_2);
			jxl.write.Number label46_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label46_2_3);
			Label label47_2 = new Label(6, 16, "用套", wcfFC2);
			ws.addCell(label47_2);
			cols = 7;
			jxl.write.Number label47_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label47_2_1);
			jxl.write.Number label47_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label47_2_2);
			jxl.write.Number label47_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label47_2_3);
			Label label48_2 = new Label(6, 17, "外用", wcfFC2);
			ws.addCell(label48_2);
			cols = 7;
			jxl.write.Number label48_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label48_2_1);
			jxl.write.Number label48_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label48_2_2);
			jxl.write.Number label48_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label48_2_3);
			Label label49_2 = new Label(6, 18, "其它", wcfFC2);
			ws.addCell(label49_2);
			Label label50_2 = new Label(6, 19, "", wcfFC2);
			ws.addCell(label50_2);
		} catch (Exception exception) {
			exception.printStackTrace();
			error(exception);
		}
		return ws;
	}

	private static WritableSheet method3(WritableSheet ws,WritableCellFormat wcfFC2,int cols,int rows){
		try{
			Label label51_2 = new Label(10, 4, "现孕", wcfFC2);
			ws.addCell(label51_2);
			cols = 12;
			jxl.write.Number label51_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label51_2_1);
			jxl.write.Number label51_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label51_2_2);
			jxl.write.Number label51_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label51_2_3);
			Label label52_2 = new Label(10, 5, "其中", wcfFC2);
			ws.addCell(label52_2);
			ws.mergeCells(10, 5, 10, 8);
			Label label53_2 = new Label(11, 5, "一孩", wcfFC2);
			ws.addCell(label53_2);
			cols = 12;
			jxl.write.Number label53_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label53_2_1);
			jxl.write.Number label53_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label53_2_2);
			jxl.write.Number label53_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label53_2_3);
			Label label54_2 = new Label(11, 6, "二孩", wcfFC2);
			ws.addCell(label54_2);
			cols = 12;
			jxl.write.Number label54_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label54_2_1);
			jxl.write.Number label54_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label54_2_2);
			jxl.write.Number label54_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label54_2_3);
			Label label55_2 = new Label(11, 7, "生在今年", wcfFC2);
			ws.addCell(label55_2);
			cols = 12;
			jxl.write.Number label55_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label55_2_1);
			jxl.write.Number label55_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label55_2_2);
			jxl.write.Number label55_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label55_2_3);
			Label label56_2 = new Label(11, 8, "生在下年", wcfFC2);
			ws.addCell(label56_2);
			cols = 12;
			jxl.write.Number label56_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label56_2_1);
			jxl.write.Number label56_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label56_2_2);
			jxl.write.Number label56_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label56_2_3);
			Label label57_2 = new Label(10, 9, "本期施行计划生育手术例数", wcfFC2);
			ws.addCell(label57_2);
			ws.mergeCells(10, 9, 11, 9);
			cols = 12;
			jxl.write.Number label57_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label57_2_1);
			jxl.write.Number label57_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label57_2_2);
			jxl.write.Number label57_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label57_2_3);
			Label label58_2 = new Label(10, 10, "其中", wcfFC2);
			ws.addCell(label58_2);
			ws.mergeCells(10, 10, 10, 19);
			Label label59_2 = new Label(11, 10, "男扎", wcfFC2);
			ws.addCell(label59_2);
			cols = 12;
			jxl.write.Number label59_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label59_2_1);
			jxl.write.Number label59_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label59_2_2);
			jxl.write.Number label59_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label59_2_3);
			Label label60_2 = new Label(11, 11, "女扎", wcfFC2);
			ws.addCell(label60_2);
			cols = 12;
			jxl.write.Number label60_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label60_2_1);
			jxl.write.Number label60_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label60_2_2);
			jxl.write.Number label60_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label60_2_3);
			Label label61_2 = new Label(11, 12, "放环", wcfFC2);
			ws.addCell(label61_2);
			cols = 12;
			jxl.write.Number label61_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label61_2_1);
			jxl.write.Number label61_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label61_2_2);
			jxl.write.Number label61_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label61_2_3);
			Label label62_2 = new Label(11, 13, "取环", wcfFC2);
			ws.addCell(label62_2);
			cols = 12;
			jxl.write.Number label62_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label62_2_1);
			jxl.write.Number label62_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label62_2_2);
			jxl.write.Number label62_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label62_2_3);
			Label label63_2 = new Label(11, 14, "人工流产", wcfFC2);
			ws.addCell(label63_2);
			cols = 12;
			jxl.write.Number label63_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label63_2_1);
			jxl.write.Number label63_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label63_2_2);
			jxl.write.Number label63_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label63_2_3);
			Label label64_2 = new Label(11, 15, "引产", wcfFC2);
			ws.addCell(label64_2);
			cols = 12;
			jxl.write.Number label64_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label64_2_1);
			jxl.write.Number label64_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label64_2_2);
			jxl.write.Number label64_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label64_2_3);
			Label label65_2 = new Label(11, 16, "汉族独生子女领证率", wcfFC2);
			ws.addCell(label65_2);
			cols = 12;
			jxl.write.Number label65_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label65_2_1);
			jxl.write.Number label65_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label65_2_2);
			jxl.write.Number label65_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label65_2_3);
			Label label66_2 = new Label(11, 17, "综合节育率", wcfFC2);
			ws.addCell(label66_2);
			cols = 12;
			jxl.write.Number label66_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label66_2_1);
			jxl.write.Number label66_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label66_2_2);
			jxl.write.Number label66_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label66_2_3);
			Label label67_2 = new Label(11, 18, "晚婚率", wcfFC2);
			ws.addCell(label67_2);
			cols = 12;
			jxl.write.Number label67_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label67_2_1);
			jxl.write.Number label67_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label67_2_2);
			jxl.write.Number label67_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label67_2_3);
			Label label68_2 = new Label(11, 19, "晚育率", wcfFC2);
			ws.addCell(label68_2);
			cols = 12;
			jxl.write.Number label68_2_1 = new jxl.write.Number(cols++, rows, 1);
			ws.addCell(label68_2_1);
			jxl.write.Number label68_2_2 = new jxl.write.Number(cols++, rows, 2);
			ws.addCell(label68_2_2);
			jxl.write.Number label68_2_3 = new jxl.write.Number(cols++, rows++, 3);
			ws.addCell(label68_2_3);
		} catch (Exception exception) {
			exception.printStackTrace();
			error(exception);
		}
		return ws;
	}
	
	/** 获取全部的部门. */
	public static List<models.Department> departments() {
		if (deps == null) {
			String adminId = session.get("admin");
			models.Administrator admin = models.Administrator.findById(adminId);
			deps = new ArrayList<models.Department>();
			deps.add(admin.department);
			deps.addAll(Secure.eachDepartments(admin.department));
		}
		return deps;
	}

	/** 获取数据列表. */
	public static void list(String keyword, String department, int page, int rows) {
		rows = rows != 0 ? rows : PAGE_SIZE;
		List<models.Planning> lists = null;
		models.Administrator admin = models.Administrator.findById(session.get("admin"));
		if (department == null || "".equals(department)) {
			lists = findAll(keyword, page, rows);
		} else {
			models.Department dep = models.Department.findById(department);
			if (dep != null) {
				MorphiaQuery query = models.Planning.find();
				query.filter("auditDep", admin.department);
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
		for (models.Planning mode : lists) {
			datas.add(mode.serialize());
		}
		result.put("page", page);
		result.put("rows", datas);
		result.put("total", models.Planning.count());
		result.put("size", rows);
		renderJSON(result);
	}
	
	/** 获取当前操作者的全部数据. */
	private static List<models.Planning> findAll(String keyword, int page, int rows) {
		models.Administrator admin = models.Administrator.findById(session.get("admin"));
		List<models.Department> departments = Secure.getDepartments();
		List<models.Planning> result = new ArrayList<models.Planning>();
		int size = 0, offset = page * rows - rows, end = offset + rows;
		for (models.Department department : departments) {
			MorphiaQuery query = models.Planning.find("byDepartment", department);
			query.filter("auditDep", admin.department);
			if (keyword != null && !"".equals(keyword))
				query.filter("name", keyword);
			List<models.Planning> data = query.asList();
			if (size >= offset && size < end) {
				for (models.Planning flup : data) {
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
	public static void add() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String adminId = session.get("admin");
			models.Administrator admin = models.Administrator.findById(adminId);
			models.Planning.find("byDepartment", admin.department).delete();
			models.Planning newData = new models.Planning();
			Date newDate = new Date();
			departments();
			newData.department = admin.department;
			newData.beginPopulation = getBeginPopulation();
			newData.endPopulation = getEndPopulation();
			newData.maleWorker = getMaleWorker();
			newData.femaleWorker = getFemaleWorker();
			newData.childBearingAge = getChildBearingAge();
			newData.marriedChildBearingAge = getMarriedChildBearingAge();
			newData.marriedNotBrood = getMarriedNotBrood();
			newData.childWomens = getChildWomens();
			newData.cerclage = getCerclage();
			newData.cerclageRate = getCerclageRate();
			newData.childCard = getChildCard();
			newData.childrenWomens = getChildrenWomens();
			newData.putRing = getPutRing();
			newData.putRingRate = getPutRingRate();
			newData.ligation = getLigation();
			newData.ligationRate = getLigationRate();
			newData.bornTotal = getBornTotal(); 
			newData.child = getChild();
			newData.children = getChildren();
			newData.womenFirstMarriage = getWomenFirstMarriage();
			newData.womens23years = getWomens23years();
			newData.menFirstMarriage = getMenFirstMarriage();
			newData.men25years = getMen25years();
			newData.finalSelection = getFinalSelection();
			newData.finalMaleFirm = getFinalMaleFirm();
			newData.finalFemaleFirm = getFinalFemaleFirm();
			newData.finalPutRing = getFinalPutRing();
			newData.finalSkinBuried = getFinalSkinBuried();
			newData.finalCondoms = getFinalCondoms();
			newData.finalExternal = getFinalExternal();
			newData.finalOther = getFinalOther();
			newData.now = getNow();
			newData.nowChild = getNowChild();
			newData.nowChildren = getNowChildren();
			newData.bornThisYear = getBornThisYear();
			newData.bornNextYear = getBornNextYear();
			newData.operation = getOperation();
			newData.operationMaleFirm = getOperationMaleFirm();
			newData.operationFemaleFirm = getOperationFemaleFirm();
			newData.operationPutRing = getOperationPutRing();
			newData.operationTakeRing = getOperationTakeRing();
			newData.operationAbortion = getOperationAbortion();
			newData.operationInduced = getOperationInduced();
			newData.nationality = getNationality();
			newData.comprehensive = getComprehensive(); 
			newData.lastMarriage = getLastMarriage();
			newData.lastPregnant = getLastPregnant();
			newData.charge = admin.name;
			newData.preparer = admin;
			newData.status = AuditType.AUDIT;
			newData.createAt = newDate;
			newData.modifyAt = newDate;
			newData.auditDep = admin.department;
			newData.save();
			result.put("success", true);
		} catch (Exception exc) {
			exc.printStackTrace();
			result.put("error", "统计失败，请稍候再试！");
			renderText(JSON.serialize(result));
		}
		renderText(JSON.serialize(result));
	}
	// 年初人口数
	private static int getBeginPopulation() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.Workers.find("byDepartment", dep).count() + models.WomenCard.find("byDepartment", dep).count();
		}
		return count;
	}
	// 年末人口数
	private static int getEndPopulation() {
		int count = 0;
		for (models.Department dep : deps) {
			int begin = getBeginPopulation(), out = 0, into = 0;
			List<models.Household> list = models.Household.find("byDepartment", dep).asList();
			for (models.Household data : list) {
				out += data.out;
			}
			list = models.Household.find("byDepartment", dep).asList();
			for (models.Household data : list) {
				into += data.into;
			}
			count += begin - out + into;
		}
		return count;
	}
	// 获取男职工数
	private static int getMaleWorker() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.Workers.find("byDepartment", dep).count();
		}
		return count;
	}
	// 获取女职工数
	private static int getFemaleWorker() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.WomenCard.find("byDepartment", dep).count();
		}
		return count;
	}
	// 育龄妇女数
	private static int getChildBearingAge() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.WomenCard.find("byDepartment", dep).count();
		}
		return count;
	}
	// 已婚育龄妇女数
	private static int getMarriedChildBearingAge() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.WomenCard.find("byDepartment", dep).filter("womanFirstMarriage exists", true).count();
		}
		return count;
	}
	// 已婚未育妇女数
	private static int getMarriedNotBrood() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.WomenCard.find("byDepartment", dep).filter("womanFirstMarriage exists", false).count();
		}
		return count;
	}
	// 一孩妇女数
	private static int getChildWomens() {
		int count = 0;
		for (models.Department dep : deps) {
			List<models.WomenCard> list = models.WomenCard.find("byDepartment", dep).asList();
			int size = 0;
			for (models.WomenCard card : list) {
				if (card.childrens.size() > 0 && card.childrens.size() < 2)
					size ++;
			}
			count += size;
		}
		return count;
	}
	// 环扎数
	private static int getCerclage() {
		int count = 0;
		for (models.Department dep : deps) {
			List<models.Household> list = models.Household.find("byDepartment", dep).asList();
			int size = 0;
			for (models.Household house : list) {
				models.WomenCard women = models.WomenCard.find("byHouseCode", dep).get();
				models.Workers man = models.Workers.find("byHouseCode", dep).get();
				if (women.oligogenics.measureDate != null || man.measureDate != null)
					size ++;
			}
			count += size;
		}
		return count;
	}
	// 环扎率
	private static String getCerclageRate() {
		double count = 0, num = getCerclage();
		for (models.Department dep : deps)
			count += models.Household.find("byDepartment", dep).count();
		return Utils.toPercentage(num / count);
	}
	// 领独生子女证人数
	private static int getChildCard() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.OnlyChildren.find("byDepartment", dep).count();
		}
		return count;
	}
	// 二孩及以上妇女数
	private static int getChildrenWomens() {
		int count = 0;
		for (models.Department dep : deps) {
			List<models.WomenCard> list = models.WomenCard.find("byDepartment", dep).asList();
			int size = 0;
			for (models.WomenCard card : list) {
				if (card.childrens.size() > 1)
					size ++;
			}
			count += size;
		}
		return count;
	}
	// 放环数
	private static int getPutRing() {
		int count = 0;
		for (models.Department dep : deps) {
			List<models.WomenCard> list = models.WomenCard.find("byDepartment", dep).asList();
			int size = 0;
			for (models.WomenCard card : list) {
				if (card.oligogenics != null && card.oligogenics.measureDate != null)
					size ++;
			}
			count += size;
		}
		return count;
	}
	// 放环率
	private static String getPutRingRate() {
		double count = 0;
		for (models.Department dep : deps) {
			count += models.WomenCard.find("byDepartment", dep).count();
		}
		return Utils.toPercentage(getPutRing() / count);
	}
	// 结扎数
	private static int getLigation() {
		int count = 0;
		for (models.Department dep : deps) {
			List<models.Workers> list = models.Workers.find("byDepartment", dep).asList();
			int size = 0;
			for (models.Workers er : list) {
				if (er.measureDate != null)
					size ++;
			}
			count += size;
		}
		return count;
	}
	// 结扎率
	private static String getLigationRate() {
		double count = 0;
		for (models.Department dep : deps) {
			count += models.Workers.find("byDepartment", dep).count();
		}
		return Utils.toPercentage(getLigation() / count);
	}
	// 出生总数
	private static int getBornTotal() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.BirthStatus.find("byDepartment", dep).count();
		}
		return count;
	}
	// 一孩
	private static int getChild() {
		return getChildWomens();
	}
	// 二孩
	private static int getChildren() {
		return getChildrenWomens();
	}
	// 女性初婚
	private static int getWomenFirstMarriage() {
		int count = 0;
		for (models.Department dep : deps) {
			List<models.WomenCard> list = models.WomenCard.find("byDepartment", dep).asList();
			int size = 0;
			for (models.WomenCard card : list) {
				if (card.womanFirstMarriage.getYear() == card.womanCurrentMarriage.getYear())
					size ++;
			}
			count += size;
		}
		return count;
	}
	// 23岁以上女性人数
	private static int getWomens23years() {
		int count = 0;
		for (models.Department dep : deps) {
			List<models.WomenCard> list = models.WomenCard.find("byDepartment", dep).asList();
			int year = new Date().getYear();
			int size = 0;
			for (models.WomenCard card : list) {
				if (year - card.womanBirth.getYear() > 23)
					size ++;
			}
			count += size;
		}
		return count;
	}
	// 男性初婚
	private static int getMenFirstMarriage() {
		int count = 0;
		for (models.Department dep : deps) {
			List<models.WomenCard> list = models.WomenCard.find("byDepartment", dep).asList();
			int size = 0;
			for (models.WomenCard card : list) {
				if (card.manFirstMarriage != null && card.manCurrentMarriage != null &&
						card.manFirstMarriage.getYear() == card.manCurrentMarriage.getYear())
					size ++;
			}
			count += size;
		}
		return count;
	}
	// 25岁以上男性人数
	private static int getMen25years() {
		int count = 0;
		for (models.Department dep : deps) {
			List<models.WomenCard> list = models.WomenCard.find("byDepartment", dep).asList();
			int year = new Date().getYear(), size = 0;
			for (models.WomenCard card : list) {
				if (card.manBirth != null && year - card.manBirth.getYear() > 25)
					size ++;
			}
			count += size;
		}
		return count;
	}
	// 期末选用各种避孕人数
	private static int getFinalSelection() {
		return getLigation() + getPutRing();
	}
	// 男扎
	private static int getFinalMaleFirm() {
		return getLigation();
	}
	// 女扎
	private static int getFinalFemaleFirm() {
		return getPutRing();
	}
	// 放环
	private static int getFinalPutRing() {
		return 0;
	}
	// 皮埋
	private static int getFinalSkinBuried() {
		return 0;
	}
	// 用套
	private static int getFinalCondoms() {
		return 0;
	}
	// 外用
	private static int getFinalExternal() {
		return 0;
	}
	// 其他
	private static int getFinalOther() {
		return 0;
	}
	// 现孕
	private static int getNow() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.Conceive.find("byDepartment", dep).count();
		}
		return count;
	}
	// 一孩
	private static int getNowChild() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.Conceive.find("byDepartment", dep).filter("size", 1).count();
		}
		return count;
	}
	// 二孩
	private static int getNowChildren() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.Conceive.find("byDepartment", dep).filter("size > ", 1).count();
		}
		return count;
	}
	// 生在今年
	private static int getBornThisYear() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.Conceive.find("byDepartment", dep).filter("thisYear", true).count();
		}
		return count;
	}
	// 生在下年
	private static int getBornNextYear() {
		int count = 0;
		for (models.Department dep : deps) {
			count += models.Conceive.find("byDepartment", dep).filter("thisYear", false).count();
		}
		return count;
	}
	// 本期施行计划生育手术例数
	private static int getOperation() {
		return getFinalSelection();
	}
	// 女扎
	private static int getOperationFemaleFirm() {
		return getFinalFemaleFirm();
	}
	// 男扎
	private static int getOperationMaleFirm() {
		return getFinalMaleFirm();
	}
	// 放环
	private static int getOperationPutRing() {
		return 0;
	}
	// 取环
	private static int getOperationTakeRing() {
		return 0;
	}
	// 人工流产
	private static int getOperationAbortion() {
		return 0;
	}
	// 引产
	private static int getOperationInduced() {
		return 0;
	}
	// 汉族独生子女领证率
	private static String getNationality() {
		/*
		double count = 0, size = 0;
		for (models.Department dep : deps) {
			count += models.BirthStatus.find("byDepartment", dep).filter("nation", "汉").filter("size", 1).count() +
					models.BirthStatus.find("byDepartment", dep).filter("nation", "汉族").filter("size", 1).count();
			size += models.OnlyChildren.find("byDepartment", dep).filter("nation", "汉").count() +
					models.OnlyChildren.find("byDepartment", dep).filter("nation", "汉族").count();
		}
		return Utils.toPercentage(size / count);
		*/
		return "100%";
	}
	// 综合节育率
	private static String getComprehensive() {
		return getLigationRate();
	}
	// 晚婚率
	private static String getLastMarriage() {
		double count = 0, size = 0;
		for (models.Department dep : deps) {
			MorphiaQuery q = models.WomenCard.find("byDepartment", dep);
			count += q.count();
			List<models.WomenCard> list = q.asList();
			for (models.WomenCard card : list) {
				if (card.womanFirstMarriage.getYear() > 22)
					size ++;
			}
		}
		return Utils.toPercentage(size / count);
	}
	// 晚育率
	private static String getLastPregnant() {
		double count = 0, size = 0;
		for (models.Department dep : deps) {
			MorphiaQuery q = models.BirthStatus.find("byDepartment", dep).filter("size", 1);
			count += q.count();
			List<models.BirthStatus> list = q.asList();
			int year = new Date().getYear();
			for (models.BirthStatus card : list) {
				if (year - card.birth.getYear() > 23)
					size ++;
			}
		}
		return Utils.toPercentage(size / count);
	}
	
	/** 删除操作 */
	public static void back(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			models.Administrator admin = models.Administrator.findById(session.get("admin"));
			if (admin.getId().toString().equals(id)) {
				models.Planning.findById(id).delete();
			} else {
				models.Planning delData = models.Planning.findById(id);
				delData.auditDep = delData.department;
				delData.status = AuditType.BACK;
				delData.save();
			}
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	/** 审核通过. */
	public static void pass(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			models.Administrator admin = models.Administrator.findById(session.get("admin"));
			models.Planning passData = models.Planning.findById(id);
			if (admin.department.parent != null) {
				passData.auditDep = admin.department.parent;
			} else {
				passData.status = AuditType.PASS;
			}
			passData.save();
			result.put("success", true);
		} catch (Exception exc) {
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
	
	/** 修改操作 */
	public static void update(String id, int beginPopulation, int endPopulation, int maleWorker, int femaleWorker, 
			int childBearingAge, int marriedChildBearingAge, int marriedNotBrood, int childWomens, int cerclage, 
			String cerclageRate, int childCard, int childrenWomens, int putRing, String putRingRate, int ligation, String ligationRate, 
			int bornTotal, int child, int children, int womenFirstMarriage, int womens23years, int menFirstMarriage, 
			int men25years, int finalSelection, int finalMaleFirm, int finalFemaleFirm, int finalPutRing, int finalSkinBuried, 
			int finalCondoms, int finalExternal, int finalOther, int now, int nowChild, int nowChildren, int bornThisYear, 
			int bornNextYear, int operation, int operationMaleFirm, int operationFemaleFirm, int operationPutRing, 
			int operationTakeRing, int operationAbortion, int operationInduced, String nationality, String comprehensive, 
			String lastMarriage, String lastPregnant, String charge) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String adminId = session.get("admin");
			models.Administrator admin = models.Administrator.findById(adminId);
			models.Planning cur = models.Planning.findById(id);
			cur.beginPopulation = beginPopulation;
			cur.endPopulation = endPopulation;
			cur.maleWorker = maleWorker;
			cur.femaleWorker = femaleWorker;
			cur.childBearingAge = childBearingAge;
			cur.marriedChildBearingAge = marriedChildBearingAge;
			cur.marriedNotBrood = marriedNotBrood;
			cur.childWomens = childWomens;
			cur.cerclage = cerclage;
			cur.cerclageRate = cerclageRate;
			cur.childCard = childCard;
			cur.childrenWomens = childrenWomens;
			cur.putRing = putRing;
			cur.putRingRate = putRingRate;
			cur.ligation = ligation;
			cur.ligationRate = ligationRate;
			cur.bornTotal = bornTotal; 
			cur.child = child;
			cur.children = children;
			cur.womenFirstMarriage = womenFirstMarriage;
			cur.womens23years = womens23years;
			cur.menFirstMarriage = menFirstMarriage;
			cur.men25years = men25years;
			cur.finalSelection = finalSelection;
			cur.finalMaleFirm = finalMaleFirm;
			cur.finalFemaleFirm = finalFemaleFirm;
			cur.finalPutRing = finalPutRing;
			cur.finalSkinBuried = finalSkinBuried;
			cur.finalCondoms = finalCondoms;
			cur.finalExternal = finalExternal;
			cur.finalOther = finalOther;
			cur.now = now;
			cur.nowChild = nowChild;
			cur.nowChildren = nowChildren;
			cur.bornThisYear = bornThisYear;
			cur.bornNextYear = bornNextYear;
			cur.operation = operation;
			cur.operationMaleFirm = operationMaleFirm;
			cur.operationFemaleFirm = operationFemaleFirm;
			cur.operationPutRing = operationPutRing;
			cur.operationTakeRing = operationTakeRing;
			cur.operationAbortion = operationAbortion;
			cur.operationInduced = operationInduced;
			cur.nationality = nationality;
			cur.comprehensive = comprehensive; 
			cur.lastMarriage = lastMarriage;
			cur.lastPregnant = lastPregnant;
			cur.charge = charge;
			cur.preparer = admin;
			cur.modifyAt = new Date();
			cur.save();
			result.put("success", true);
		} catch (Exception exc) {
			exc.printStackTrace();
			result.put("error", "数据库异常，可能其他人正在操作，请刷新后重试。");
		}
		renderText(JSON.serialize(result));
	}
}
