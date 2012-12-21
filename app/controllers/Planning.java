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

/** 计生报表管理 */
public class Planning extends Controller {
	public static final int PAGE_SIZE = 20;
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
	
	private static WritableSheet  method(WritableSheet ws,WritableCellFormat wcfFC2,int cols,int rows){
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
	
	private static WritableSheet  method2(WritableSheet ws,WritableCellFormat wcfFC2,int cols,int rows){
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
	private static WritableSheet  method3(WritableSheet ws,WritableCellFormat wcfFC2,int cols,int rows){
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
