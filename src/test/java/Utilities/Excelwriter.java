package Utilities;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excelwriter {
	public void WriteExcel(String expath,String sheetname,String givendata,int colno) throws IOException
	{
		String excelpath =expath;
		File Excelfile = new File(excelpath);
		
		 try (FileInputStream fileInputStream = new FileInputStream(Excelfile);
	             XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

	            XSSFSheet sheet = workbook.getSheet(sheetname);
	            if (sheet == null) {
	                // If the sheet doesn't exist, create a new one
	                sheet = workbook.createSheet(sheetname);
	            }

	            // Create a row and add the data to the first cell
	            //Row row = sheet.createRow(1);
	          //Cell cell = row.createCell(colno);
	            Row row =sheet.getRow(1);
 
	            Cell cell = row.getCell(colno);
	            cell.setCellValue(givendata);

	            // Write the modified workbook back to the existing Excel file
	            try (FileOutputStream fileOut = new FileOutputStream(Excelfile)) {
	                workbook.write(fileOut);
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}

	    }
  




