package com.tfbank.longkong.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExcelUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	/**
	 * 读取Excel文件
	 * 
	 * @param is 文件的输入流
	 * @param filePath 文件路径
	 * @return
	 * @throws Exception
	 */
	public static Map<String, List<Map<String, String>>> importExcle(String filePath)
			throws Exception {
		
		
		Workbook wb = null;
		Map<String, List<Map<String, String>>> result = new HashMap<String, List<Map<String, String>>>();
		File file = new File(filePath);
		InputStream is = new FileInputStream(file);
		try {
			if (filePath.endsWith(".xls")) {
				wb = new HSSFWorkbook(is);
			} else if (filePath.endsWith(".xlsx")) {
				wb = new XSSFWorkbook(is);
			} else {
				logger.error("The Wrong file type！");
			}
			
			int sheetSize = wb.getNumberOfSheets();
			for (int i = 0; i < sheetSize; i++) {
				Sheet sheet = wb.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				List<Map<String, String>> sheetList = new ArrayList<Map<String, String>>();// 对应sheet页
				List<String> titles = new ArrayList<String>();

				int rowSize = sheet.getLastRowNum() + 1;
				for (int j = 0; j < rowSize; j++) {// 遍历行
					Row row = sheet.getRow(j);
					if (row == null) {// 略过空行
						continue;
					}
					int cellSize = row.getLastCellNum();// 行中有多少个单元格，也就是有多少列
					if (j == 0) {// 第一行是标题行
						for (int k = 0; k < cellSize; k++) {
							Cell cell = row.getCell(k);
							titles.add(cell.toString().trim());
						}
					} else {// 其他行是数据行
						Map<String, String> rowMap = new HashMap<String, String>();// 对应一个数据行
						for (int k = 0; k < titles.size(); k++) {
							
							Cell cell = row.getCell(k);
							//cell.setCellType(Cell.CELL_TYPE_STRING);
							String key = titles.get(k);
							String value = null;
							if (cell != null)  value = cell.toString().trim();
							if (value != null && value.length() > 0)
								rowMap.put(key, value);
						}
						if (rowMap != null && rowMap.size() > 0)
							sheetList.add(rowMap);
					}
				}
				result.put(sheetName, sheetList);
			}
		} finally {
			if (is != null) is.close();
		}
		return result;
	}
	
	/**
	 * 根据sheet页名称读取Excel文件
	 * 
	 * @param filePath
	 * @param funName
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> importExcleBySheetName(String filePath, String sheetName) throws Exception {
		List<Map<String, String>> ls = new ArrayList<Map<String, String>>();
		Map<String, List<Map<String, String>>> map = importExcle(filePath);
		if (map != null && map.size() > 0) {
			if (map.containsKey(sheetName)) {
				ls = map.get(sheetName);
			} else {
				logger.info("The sheet name \"" + sheetName +"\" is not exist!");
			}
		}
		return ls;
	}

}
