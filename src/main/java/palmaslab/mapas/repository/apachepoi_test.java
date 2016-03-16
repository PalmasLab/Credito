package palmaslab.mapas.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class apachepoi_test {

	
	
	private String excelDir_ = "/srv/PalmasServer/locals/";
	private String excel_name_ = "test.xlsx";
	private Path targetDir_ = Paths.get(excelDir_+excel_name_);
	
	public apachepoi_test()
	{
		
	}
	public void readFile()
	{
		try {
	        
			//..
			FileInputStream file = new FileInputStream(new File(excelDir_+excel_name_));
	         
	        //Get the workbook instance for XLS file 
	        XSSFWorkbook workbook = new XSSFWorkbook(file);
	
	        //Get first sheet from the workbook
	        XSSFSheet sheet = workbook.getSheetAt(0);
	         
	        //Iterate through each rows from first sheet
	        Iterator<Row> rowIterator = sheet.iterator();
	        while(rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	             
	            //For each row, iterate through each columns
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while(cellIterator.hasNext()) {
	                 
	                Cell cell = cellIterator.next();
	                 
	                switch(cell.getCellType()) {
	                    case Cell.CELL_TYPE_BOOLEAN:
	                        System.out.print(cell.getBooleanCellValue() + "\t\t");
	                        break;
	                    case Cell.CELL_TYPE_NUMERIC:
	                        System.out.print(cell.getNumericCellValue() + "\t\t");
	                        break;
	                    case Cell.CELL_TYPE_STRING:
	                        System.out.print(cell.getStringCellValue() + "\t\t");
	                        break;
	                }
	            }
	            System.out.println("");
	        }
	        file.close();
	        
	         
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException ei) {
	        ei.printStackTrace();
	    }
	
	}
}
