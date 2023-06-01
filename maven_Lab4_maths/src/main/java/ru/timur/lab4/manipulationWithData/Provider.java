package ru.timur.lab4.manipulationWithData;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

public class Provider {
    private final File file;
    private XSSFWorkbook wb;

    public Provider(String path) {
        this.file = new File(path);
    }

    public void openFile() throws IOException, InvalidFormatException {
            wb = new XSSFWorkbook(file);
    }

    public double[] getValues(String sheetName, int numberOfColumn) {
        XSSFSheet sheet = this.wb.getSheet(sheetName);
        double[] values = new double[sheet.getLastRowNum()];
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            values[i-1] = row.getCell(numberOfColumn).getNumericCellValue();
        }
        return values;
    }

    public void close() throws IOException {
        this.wb.close();

    }
}
