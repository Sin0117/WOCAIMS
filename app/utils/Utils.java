package utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Utils {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final SimpleDateFormat parseDate = new SimpleDateFormat("MM/dd/yyyy");
	private static final SimpleDateFormat parseDate1 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static WritableCellFormat _wcfFC;
	/** 时间转化. */
	public static String formatDate(Date d) {
		if (d == null)
			return "未记录";
		return dateFormat.format(d);
	}
	public static String formatTime(Date d) {
		if (d == null)
			return "未记录";
		return timeFormat.format(d);
	}
	public static String formatDateTime(Date d) {
		if (d == null)
			return "未记录";
		return dateTimeFormat.format(d);
	}
	
	public static Date parseDate(String d) {
		if (!checkString(d)) return null;
		if (d.indexOf("%2F") != -1)
			d = d.replaceAll("%2F", "/");
		try {
			if (d.indexOf("-") != -1) {
				return parseDate1.parse(d);
			} else {
				return parseDate.parse(d);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 字符串拆分. */
	public static List<String> toStringArray(String str, String split) {
		List<String> result = new ArrayList<String>();
		if (str.indexOf(split) == -1) {
			result.add(str);
		} else {
			String arr[] = str.split(split);
			for (int i = 0; i < arr.length; i ++)
				result.add(arr[i]);
		}
		return result;
	}
	/** 获取excel单元格居中的样式. */
	public static WritableCellFormat reportCellCenterFormat() {
		if (_wcfFC == null) {
			try {
				WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);   
				_wcfFC = new WritableCellFormat(wfont); 
				_wcfFC.setAlignment(Alignment.CENTRE);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		return _wcfFC;
	}
	/** 检测字符串是否为空. */
	public static boolean checkString(String str) {
		if (str != null && !"".equals(str) && !"".equals(str.trim())) {
			return true;
		}
		return false;
	}
	
	public static File toExcel(String name) {
		File f = new File("./excels/" + name);
	    try {
	    	if (f.exists())
	    		f.createNewFile();
	        WritableWorkbook wbook = Workbook.createWorkbook(f);
	        WritableSheet ws = wbook.createSheet("Test Sheet 1", 0);
	        //1.添加Label对象
	        Label labelC = new Label(0, 0, "This is a Label cell");
	        ws.addCell(labelC);
	        //添加带有字型Formatting的对象
	        WritableFont wf = new WritableFont(WritableFont.TIMES, 18, WritableFont.BOLD, true);
	        WritableCellFormat wcfF = new WritableCellFormat(wf);
	        Label labelCF = new Label(1, 0, "This is a Label Cell", wcfF);
	        ws.addCell(labelCF);

	        //2.添加Number对象
	        jxl.write.Number labelN = new jxl.write.Number(0, 1, 3.1415926);
	        ws.addCell(labelN);
	        //3.添加Boolean对象
	        jxl.write.Boolean labelB = new jxl.write.Boolean(0, 2, false);
	        ws.addCell(labelB);
	        //4.添加DateTime对象
	        DateTime labelDT = new DateTime(0, 3, new java.util.Date());
	        ws.addCell(labelDT);
	        
	        wbook.write();
	        wbook.close();
	        return f;
	    } catch (Exception e) {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	    }
	    return null;
	}
}
