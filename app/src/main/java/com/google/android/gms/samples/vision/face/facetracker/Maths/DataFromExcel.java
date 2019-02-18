package com.google.android.gms.samples.vision.face.facetracker.Maths;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public  class DataFromExcel {


    private static FileInputStream file;


    public static void readfile() throws IOException, InvalidFormatException {

        file = new FileInputStream(new File("/Users/mohammedzaman/AndroidStudioProjects/FaceTracker/app/src/main/java/com/google/android/gms/samples/vision/face/facetracker/Maths/DataSet/DataSet1.xlsx"));
        // Creating a Workbook from an Excel file (.xls or .xlsx)

        Workbook workbook = WorkbookFactory.create(file);
        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");


        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);


        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();


        for (Row row: sheet) {
            for(Cell cell: row){
               if(cell.getColumnIndex() == 0){
                  if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                   String cellValue = dataFormatter.formatCellValue(cell);
                     // y[cell.getRowIndex() - 1] = Double.parseDouble(cellValue);
                   }
                }else if(cell.getColumnIndex() == 1){
                   if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        String cellValue = dataFormatter.formatCellValue(cell);
                        System.out.print(cellValue + "\t");
                       // x[cell.getRowIndex() -1] = Double.parseDouble(cellValue);

                   }
                }
            }

        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
