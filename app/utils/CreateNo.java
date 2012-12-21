package utils;

import java.util.Collections;
import java.util.List;

public class CreateNo {

	/**
	 * 根据传入的编号集合获取新编号
	 * 
	 * @param l
	 *            所有编号集合
	 * @param num
	 *            编号的起始位数
	 * @return 返回一个编号
	 */
	public static String getNo(List<String> l, Integer num) {
		// 对所有的编号进行排序
		Collections.sort(l);
		try {
			if (l.size() != 0) {
				// 得到最后一个元素
				String tmp = l.get(l.size() - 1);
				// 得到最后一个元素的倒数索引
				int endIndex = tmp.length() - num;
				String start = tmp.substring(0, endIndex);
				String end = tmp.substring(endIndex);
				Integer endS = Integer.parseInt(end);
				String iNum = ",";
				for (String string : l) {
					Integer ii = Integer.parseInt(string.substring(endIndex));
					iNum += ii + ",";
				}
				for (int i = 1; i <= endS; i++) {
					if (iNum.indexOf("," + i + ",") == -1) {
						int endL = end.length() - (i + "").length();
						String o = "";
						for (int j = 0; j < endL; j++) {
							o += "0";
						}
						if (o.length() == 0) {
							end = i + "";
						} else {
							i = i - 1;
							end = o + i;
						}
						break;
					}
				}
				int endValue = Integer.parseInt(end);
				int endLength = (endValue + "").length();
				String endPrefix = "";
				String endPrefixNo = "";
				String[] arrNo = new String[num - 1];
				for (int i = 0; i < num - 1; i++) {
					endPrefixNo += "0";
					arrNo[i] = "1" + endPrefixNo;
				}
				for (int i = 0; i < arrNo.length; i++) {
					if (endValue + 1 == Integer.parseInt(arrNo[i])) {
						endLength = arrNo[i].length();
					}
				}
				for (int j = 0; j < num - endLength; j++) {
					endPrefix += "0";
				}
				String result = start + endPrefix + (endValue + 1);
				if (result != null || "".equals(result)) {
					return result;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public static String getNo2(List<String> l, Integer num) {
		Collections.sort(l);
		try {
			if (l.size() != 0) {
				if (num == 3 || num == 4) {
					String tmp = l.get(l.size() - 1);
					int endIndex = tmp.length() - num;

					String start = tmp.substring(0, endIndex);
					String end = tmp.substring(endIndex);

					int endValue = Integer.parseInt(end);
					int endLength = (endValue + "").length();
					String endPrefix = "";

					if (num == 3) {
						if (endLength == 1) {
							int a = endValue + 1;
							if (a == 10) {
								endPrefix = "0";
							} else {
								endPrefix = "00";
							}
						} else if (endLength == 2) {
							int a = endValue + 1;
							if (a != 100) {
								endPrefix = "0";
							}
						}
					} else {
						if (endLength == 1) {
							int a = endValue + 1;
							if (a == 10) {
								endPrefix = "00";
							} else {
								endPrefix = "000";
							}
						} else if (endLength == 2) {
							int a = endValue + 1;
							if (a == 100) {
								endPrefix = "0";
							} else {
								endPrefix = "00";
							}
						} else if (endLength == 3) {
							int a = endValue + 1;
							if (a != 1000) {
								endPrefix = "0";
							}
						}
					}

					String result = start + endPrefix + (endValue + 1);
					if (result != null || "".equals(result)) {
						return result;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
