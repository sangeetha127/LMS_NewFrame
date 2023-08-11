package Utilities;
import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Excelreader {
	public LinkedHashMap<String, String> readexcelsheet(String expath,String sheetname,int rowno) throws IOException {
		String excelpath =expath;
		File Excelfile = new File(excelpath);
		FileInputStream Fist= new FileInputStream(Excelfile);
		XSSFWorkbook wbook = new XSSFWorkbook(Fist);  // create workbook instance from given excelpath
		XSSFSheet sheet = wbook.getSheet(sheetname);  //get the given sheet

		LinkedHashMap<String, String> eachcolumnMapdata = new LinkedHashMap<String, String>();
		List<String> columnnames = new ArrayList<String>();
		int temp;
		int headerRow = sheet.getFirstRowNum();
		Row headrerow = sheet.getRow(headerRow);
		
		if (sheet.getRow(rowno)!=null) {
			//Save headercolumnnames into list
			Iterator<Cell> headerrowcell=headrerow.cellIterator();
			while (headerrowcell.hasNext()) {
				Cell HeaderCell = headerrowcell.next();
				columnnames.add(HeaderCell.getStringCellValue());
			}
			
			Iterator<Row> row = sheet.rowIterator();//Iterate through each rows one by one
			while (row.hasNext()) {
				Row currRow = row.next();//assigning next existing row
				temp=currRow.getRowNum();  // for checking headerrowno and given rowno
				if (temp!=headerRow) {
					if (temp==rowno+1) {
						Iterator<Cell> cell = currRow.cellIterator();  //iterate thru all columns in each row
						int headerindex=0;  //header column names index
						int maxcell = currRow.getLastCellNum();
						for (int i =0;i<maxcell;i++) {
						//while (cell.hasNext()) {
							//Cell currCell = cell.next();
							Cell currCell = currRow.getCell(i,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
							
							if (currCell.getCellType() == CellType.STRING) {
							//eachcolumnMapdata.put(columnnames.get(headerindex),format.formatCellValue(currCell));
							eachcolumnMapdata.put(columnnames.get(headerindex),currCell.getStringCellValue());
							}
							else if (currCell.getCellType() == CellType.NUMERIC) {
								eachcolumnMapdata.put(columnnames.get(headerindex),NumberToTextConverter.toText(currCell.getNumericCellValue()));	
							}
							else if (currCell.getCellType() == CellType.BOOLEAN) {
								eachcolumnMapdata.put(columnnames.get(headerindex),Boolean.toString(currCell.getBooleanCellValue()));	
							}	
							else if (currCell.getCellType() == CellType.BLANK) {
								eachcolumnMapdata.put(columnnames.get(headerindex),"");	
							}
							//System.out.println(eachcolumnMapdata);
							headerindex++;
							
						}
					}
				}
			}
		}
		Fist.close();
		wbook.close();
		return eachcolumnMapdata;
	}
}