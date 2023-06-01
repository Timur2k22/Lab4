package ru.timur.lab4.manipulationWithData;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.timur.lab4.calculation.StatCalculations.getCalculations;
import static ru.timur.lab4.calculation.StatCalculations.getCovMatrix;

public class DataManipulation {

    private final Map<String, double[]> valuesMap = new HashMap<>();
    private final Map<String, Map<String, Double>> resultsMap = new HashMap<>();
    private double[][] covMatrix;

    public void loadData(String path) throws IOException, InvalidFormatException {
        Provider provider = new Provider(path);
        provider.openFile();
        valuesMap.put("x", provider.getValues("Вариант 3", 0));
        valuesMap.put("y", provider.getValues("Вариант 3", 1));
        valuesMap.put("z", provider.getValues("Вариант 3", 2));
        provider.close();
        loadResults();
    }

    public void exportData(String path) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Calculations");
        Row row = sheet.createRow(0);
        int numberOfNames = resultsMap.keySet().size();
        String[] names = resultsMap.keySet().toArray(new String[numberOfNames]);
        int numberOfParameters = resultsMap.get(names[0]).keySet().size();
        String[] parameterNames = resultsMap.get(names[0]).keySet().toArray(new String[numberOfParameters]);
        for (int i = 0; i < numberOfParameters; i++) {
            row.createCell(i).setCellValue(parameterNames[i]);
        }
        for (int i = 0; i < numberOfNames; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < numberOfParameters; j++) {
                Cell cell = row.createCell(j);
                double value = resultsMap.get(names[i]).get(parameterNames[i]);
                cell.setCellValue(value);
            }
        }
        sheet = workbook.createSheet("Covariance matrix");
        for (int i = 0; i < covMatrix.length; i++) {
            row = sheet.createRow(i);
            for (int j = 0; j < covMatrix[i].length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(covMatrix[i][j]);
            }
        }
        workbook.write(new FileOutputStream(path));
        workbook.close();
    }


    private void loadResults() {
        resultsMap.put("x", getCalculations(valuesMap.get("x")));
        resultsMap.put("y", getCalculations(valuesMap.get("y")));
        resultsMap.put("z", getCalculations(valuesMap.get("z")));
        List<double[]> list = new ArrayList<>();
        list.add(valuesMap.get("x"));
        list.add(valuesMap.get("y"));
        list.add(valuesMap.get("z"));
        covMatrix = getCovMatrix(list);
    }
}
