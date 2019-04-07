package com.eap.framework.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class ExcelUtil {

	/**
	 * 合并单元格（行合并），合并规则：内容相同则合并
	 * 
	 * @param sheet	       合并的sheet
	 * @param colindex      合并列索引
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static HSSFSheet rowSpan(HSSFSheet sheet, int colindex, int contentBeginIndex) {
		// 总行数
		int rowNum = sheet.getLastRowNum();
		HSSFRow row = sheet.getRow(1);
		// 正文内容应该从第二行开始,第一行为表头的标题
		int startRow = contentBeginIndex;
		String startValue = "";
		for (int i = contentBeginIndex; i <= rowNum; i++) {
			row = sheet.getRow(i);
			String value = row.getCell(colindex).getRichStringCellValue().getString();
			if (i == contentBeginIndex) {
				startValue = value;
				continue;
			}
			if (StrUtil.isNotBlank(startValue) && StrUtil.isNotBlank(value) && startValue.equals(value)) {
				if (i == rowNum) 
					sheet.addMergedRegion(new CellRangeAddress(startRow, i , colindex, colindex));
				else
					continue;
			} else {
				if((i-1)>startRow)
					sheet.addMergedRegion(new CellRangeAddress(startRow, i - 1, colindex, colindex));
				startRow = i;
			}
			startValue=value;
		}
		return sheet;
	}

	public static List<List<String>> readExcel(String path) throws IOException{
		if(path.endsWith(".xls")){
			return readXls(path);
		}else{
			return readXlsx(path);
		}
	}
	public static List<List<String>> readXlsx(String path) throws IOException {
		InputStream input = new FileInputStream(path);
		return readXlsx(input);
	}

	public static List<List<String>> readXls(String path) throws IOException {
		InputStream input = new FileInputStream(path);
		return readXls(input);
	}

	public static List<List<String>> readXlsx(InputStream input) throws IOException {
		List<List<String>> result = new ArrayList<List<String>>();
		XSSFWorkbook workbook = new XSSFWorkbook(input);
		for (XSSFSheet xssfSheet : workbook) {
			if (xssfSheet == null) {
				continue;
			}
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow row = xssfSheet.getRow(rowNum);
				int minCellNum = row.getFirstCellNum();
				int maxCellNum = row.getLastCellNum();
				List<String> rowList = new ArrayList<String>();
				for (int i = minCellNum; i < maxCellNum; i++) {
					XSSFCell cell = row.getCell(i);
					if (cell == null) {
						continue;
					}
					rowList.add(cell.toString());
				}
				result.add(rowList);
			}
		}
		return result;
	}

	public static List<List<String>> readXls(InputStream input) throws IOException {
		List<List<String>> result = new ArrayList<List<String>>();
		HSSFWorkbook workbook = new HSSFWorkbook(input);
		for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet sheet = workbook.getSheetAt(numSheet);
			if (sheet == null) {
				continue;
			}
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				HSSFRow row = sheet.getRow(rowNum);
				int minCellNum = row.getFirstCellNum();
				int maxCellNum = row.getLastCellNum();
				List<String> rowList = new ArrayList<String>();
				for (int i = minCellNum; i < maxCellNum; i++) {
					HSSFCell cell = row.getCell(i);
					if (cell == null) {
						continue;
					}
					rowList.add(getStringVal(cell));
				}
				result.add(rowList);
			}
		}
		return result;
	}

	private static String getStringVal(HSSFCell cell) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			case Cell.CELL_TYPE_NUMERIC:
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			default:
				return null;
		}
	}
}
