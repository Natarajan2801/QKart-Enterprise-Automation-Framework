package com.qkart.utils;

import com.qkart.constants.FrameworkConstants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {

    // Synchronized to prevent file access collisions
    public synchronized static Iterator<Object[]> getSheetData(String sheetName) {
        List<Object[]> data = new ArrayList<>();

        // Try-with-resources ensures the file is CLOSED immediately after reading
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.EXCEL_DATA_FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                // If sheet doesn't exist, return empty list instead of crashing
                System.err.println("Sheet not found: " + sheetName);
                return data.iterator();
            }

            Iterator<Row> rowIterator = sheet.rowIterator();
            if (rowIterator.hasNext()) rowIterator.next(); // Skip Header

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int lastCellNum = row.getLastCellNum();
                if (lastCellNum <= 0) continue;

                String[] rowData = new String[lastCellNum - 1];

                // Skip Index Column (0)
                for (int i = 1; i < lastCellNum; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[i - 1] = getCellValue(cell);
                }
                data.add(rowData);
            }
        } catch (Exception e) {
            // Log error but don't crash the whole suite
            System.err.println("Error reading Excel sheet " + sheetName + ": " + e.getMessage());
        }
        return data.iterator();
    }

    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) return cell.getDateCellValue().toString();
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }
}