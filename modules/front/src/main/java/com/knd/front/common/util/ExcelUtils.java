package com.knd.front.common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel文件工具类
 *
 * @author amx
 */
public class ExcelUtils {

    static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * 写入多sheet的Excel(带标题栏) List每一项为一个sheet
     * list对应excel文件，第二层Iterable对应sheet页，第三层map对应sheet页中一行
     *
     * @param excelResult   写入数据List
     * @param finalXlsxPath excel文件路径
     */
    public static void writeExcel(List<Iterable<Map<String, Object>>> excelResult, String finalXlsxPath) {
        OutputStream out = null;
        try {
            File file = new File(finalXlsxPath);
            // 手动创建还是自动创建？
            if (!file.exists()) {
                FileOutputStream fileOut = new FileOutputStream(finalXlsxPath);
                if (finalXlsxPath.endsWith(EXCEL_XLS)) {
                    HSSFWorkbook wb1 = new HSSFWorkbook();
                    wb1.write(fileOut);
                    fileOut.close();
                    wb1.close();
                } else if (finalXlsxPath.endsWith(EXCEL_XLSX)) {
                    XSSFWorkbook wb2 = new XSSFWorkbook();
                    wb2.write(fileOut);
                    fileOut.close();
                    wb2.close();
                }
            }
            File finalXlsxFile = new File(finalXlsxPath);
            Workbook workBook = getWorkbook(finalXlsxFile);
            for (int i = 0; i < excelResult.size(); i++) {
                Sheet sheet = workBook.createSheet();
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) excelResult.get(i);
                Map<String, Object> dataMap = dataList.get(0);
                Object[] titleArray = dataMap.keySet().toArray();

                for (int j = 0; j < dataList.size(); j++) {
                    // 第一行是标题行
                    if (j == 0) {
                        Row row = sheet.createRow(j);
                        for (int k = 0; k < titleArray.length; k++) {
                            Cell cell = row.createCell(k);
                            if (titleArray[k] != null) {
                                cell.setCellValue(titleArray[k].toString());
                            }
                        }
                    }
                    Row row = sheet.createRow(j + 1);
                    dataMap = dataList.get(j);
                    for (int k = 0; k < titleArray.length; k++) {
                        String value = null;
                        if (titleArray[k] != null) {
                            value = dataMap.get(titleArray[k]) == null ? null : dataMap.get(titleArray[k]).toString();
                        }
                        Cell cell = row.createCell(k);
                        cell.setCellValue(value);
                    }
                }
            }
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功");
    }

    /**
     * 写入多sheet的Excel(带标题栏、表头名称、自定义sheet名称)  List每一项为一个sheet
     * list对应excel文件，第二层ArrayList对应sheet页，第三层map对应sheet页中一行
     *
     * @param excelResult
     * @param finalXlsxPath excel文件路径
     * @param headers       sheet中表头名称列表
     * @param sheetName     sheet名称列表
     */
    public static void writeExcel(List<ArrayList<Map<String, Object>>> excelResult, String finalXlsxPath, List<String> headers, List<String> sheetName) {
        OutputStream out = null;
        try {
            // 设置输出的excel文件
            File file = new File(finalXlsxPath);
            if (file.exists()) {
                file.delete();
            }
            // 手动创建还是自动创建？
            if (!file.exists()) {
                FileOutputStream fileOut = new FileOutputStream(finalXlsxPath);
                if (finalXlsxPath.endsWith(EXCEL_XLS)) {
                    HSSFWorkbook wb1 = new HSSFWorkbook();
                    wb1.write(fileOut);
                    fileOut.close();
                    wb1.close();
                } else if (finalXlsxPath.endsWith(EXCEL_XLSX)) {
                    XSSFWorkbook wb2 = new XSSFWorkbook();
                    wb2.write(fileOut);
                    fileOut.close();
                    wb2.close();
                }
            }
            // 读取Excel文档
            File finalXlsxFile = new File(finalXlsxPath);
            Workbook workBook = getWorkbook(finalXlsxFile);

            // 表头样式
            CellStyle headStyle = workBook.createCellStyle();
            //水平居中
            headStyle.setAlignment(HorizontalAlignment.CENTER);
            //垂直居中
            headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //设置字体大小
            Font titleFont = workBook.createFont();
            titleFont.setFontHeightInPoints((short) 20);
            //斜体为false
            titleFont.setItalic(false);
            //设置字体
            titleFont.setFontName("黑体");
            headStyle.setFont(titleFont);

            //标题栏样式
            CellStyle titleStyle = workBook.createCellStyle();
            //水平居中
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            //垂直居中
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //设置边框
            titleStyle.setBorderTop(BorderStyle.THIN);
            titleStyle.setBorderRight(BorderStyle.THIN);
            titleStyle.setBorderBottom(BorderStyle.THIN);
            titleStyle.setBorderLeft(BorderStyle.THIN);
            //设置字体
            Font headerFont = workBook.createFont();
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setItalic(false);
            headerFont.setFontName("宋体");
//			headerFont.setBold(true);
            titleStyle.setFont(headerFont);

            // 单元格样式
            CellStyle cellStyle = workBook.createCellStyle();
            //水平居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //垂直居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //设置边框
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            Font cellFont = workBook.createFont();
            cellFont.setFontHeightInPoints((short) 11);
            cellFont.setItalic(false);
            cellFont.setFontName("宋体");
//			headerFont.setBold(true);
            cellStyle.setFont(cellFont);

            Sheet sheet = null;
            int numberOfSheets = workBook.getNumberOfSheets();
            for (int i = 0; i < excelResult.size(); i++) {
                //此处默认为一个新的excel，注释掉的代码为考虑文件已经有存在的sheet
                // sheet 对应一个工作页
                List<Map<String, Object>> dataList = excelResult.get(i);
//				if (numberOfSheets >= (sheetIndex + 1)) {
                // 当前的表格个数>=(指定的索引+1),直接获取
//					sheet = workBook.getSheetAt(sheetIndex);
//				} else {
                // 当前表格个数不足，创建一个空表
                sheet = workBook.createSheet();
//				}
                workBook.setSheetName(i, sheetName.get(i));
                Map<String, Object> dataMap = dataList.get(0);
                Object[] titleArray = dataMap.keySet().toArray();

                //第一列为表头
                Row nameRow = sheet.createRow(0);
                //设置行高
                nameRow.setHeight((short) 750);
                Cell nameCell = nameRow.createCell(0);
                //设置单元格值
                nameCell.setCellValue(headers.get(i));
                //设置单元格样式
                nameCell.setCellStyle(headStyle);
                //合并第一行的前几列为表头
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titleArray.length - 1));
                for (int j = 0; j < dataList.size(); j++) {
                    // 创建并写入标题栏
                    if (j == 0) {
                        // 第二行是标题行
                        Row row = sheet.createRow(j + 1);
                        row.setHeight((short) 500);
                        for (int k = 0; k < titleArray.length; k++) {
                            Cell cell = row.createCell(k);
                            if (titleArray[k] != null) {
                                cell.setCellValue(titleArray[k].toString());
                                cell.setCellStyle(titleStyle);
                            }
                        }
                    }
                    Row row = sheet.createRow(j + 2);
                    //设置行高
                    row.setHeight((short) 500);
                    dataMap = dataList.get(j);
                    for (int k = 0; k < titleArray.length; k++) {
                        String value = null;
                        if (titleArray[k] != null) {
                            value = dataMap.get(titleArray[k]) == null ? null : dataMap.get(titleArray[k]).toString();
                        }
                        Cell cell = row.createCell(k);
                        cell.setCellValue(value);
                        cell.setCellStyle(cellStyle);
                    }
                }
                // 设置每一列为根据内容自动调整列宽
                for (int k = 0; k < titleArray.length; k++) {
                    sheet.autoSizeColumn(k, true);
                }
            }
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功");
    }

    /**
     * 判断Excel的版本,获取到对应的Workbook
     *
     * @param file 文件
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbook(File file) throws IOException {
        Workbook wb = null;
        InputStream fis = new FileInputStream(file);
        if (file.getName().endsWith(EXCEL_XLS)) {
            // Excel 2003
            wb = new HSSFWorkbook(fis);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {
            // Excel 2007/2010
            wb = new XSSFWorkbook(fis);
        }
        if (fis != null) {
            fis.close();
        }
        return wb;
    }

    /**
     * 读取excel文件中的全部表格 适用于没有单元格合并的excel，并且第一行是title（标题栏）的情况
     * 每一行构成一个map，key值是列标题，value是列值。没有值的单元格其value值为null
     * 返回结果最外层list对应excel文件，第二层Iterable对应sheet页，第三层map对应sheet页中一行
     *
     * @param filePath 文件路径
     * @return
     * @throws Exception
     */
    public static List<Iterable<Map<String, Object>>> readExcelWithTitle(String filePath) throws Exception {
        Workbook wb = null;
        // List<String> sheetNames = new ArrayList<String>();
        try {
            // 对应excel文件
            List<Iterable<Map<String, Object>>> result = new ArrayList<Iterable<Map<String, Object>>>();
            if (filePath.endsWith(EXCEL_XLS) || filePath.endsWith(EXCEL_XLSX)) {
                // 读取Excel文档
                File file = new File(filePath);
                wb = getWorkbook(file);
                int sheetSize = wb.getNumberOfSheets();
                // 遍历sheet页
                for (int i = 0; i < sheetSize; i++) {
                    Sheet sheet = wb.getSheetAt(i);
                    Iterable<Map<String, Object>> readExcelWithTitle = readExcelWithTitle(wb, sheet);
                    result.add(readExcelWithTitle);
                }
            } else {
                logger.error("读取的不是excel格式文件");
            }
            return result;

        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            if (wb != null) {
                wb.close();
            }
        }
    }

    /**
     * 读取给定sheet的内容
     * 读取excel文件中的指定名称的表格 用于没有单元格合并的表格，且第一行是标题行，
     * 每一行构成一个map(key值是列标题，value是列值)。没有值的单元格其value值为null
     * 返回结果最外层的list对应一个sheet页，第二层的map对应sheet页中的一行
     *
     * @param wb
     * @param sheet
     * @return
     */
    private static Iterable<Map<String, Object>> readExcelWithTitle(Workbook wb, Sheet sheet) {
        Iterable<Map<String, Object>> sheetList = null;
        try {
            sheetList = new ArrayList<Map<String, Object>>();
            List<String> titles = new ArrayList<String>();
            int rowSize = sheet.getLastRowNum() + 1;
            for (int j = 0; j < rowSize; j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    // 略过空行
                    continue;
                }
                int cellSize = row.getLastCellNum();
                if (j == 0) {
                    // 第一行是标题行
                    for (int k = 0; k < cellSize; k++) {
                        Cell cell = row.getCell(k);
                        titles.add(cell.toString());
                    }
                } else {
                    // 对应一个数据行
                    Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
                    for (int k = 0; k < titles.size(); k++) {
                        String value = null;
                        Cell cell = null;
                        cell = row.getCell(k);
                        int cellType = 99;
                        if (cell != null) {
                            cellType = cell.getCellType();
                        }
                        switch (cellType) {
                            case Cell.CELL_TYPE_STRING:
                                // 1
                                value = cell.getRichStringCellValue().getString();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                // 0
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date date = cell.getDateCellValue();
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    value = df.format(date);
                                } else {
                                    value = String.valueOf(cell.getNumericCellValue());
                                    NumberFormat nf = NumberFormat.getInstance();
                                    String s = nf.format(cell.getNumericCellValue());
                                    if (s.indexOf(",") >= 0) {
                                        s = s.replace(",", "");
                                    }
                                    value = s;
                                }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                // 2
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                value = cell.getStringCellValue();
                                break;
                            default:
                                if (cell != null) {
                                    value = cell.toString();
                                }
                        }
                        String key = titles.get(k);
                        rowMap.put(key, value);
                    }
                    ((ArrayList<Map<String, Object>>) sheetList).add(rowMap);
                }
            }
            return sheetList;
        } finally {
        }
    }

    /**
     * 读取指定sheet名称的sheet的内容   如果没有则读取第一个sheet
     * 读取excel文件中的指定名称的表格 用于没有单元格合并的表格，且第一行是标题行，
     * 每一行构成一个map(key值是列标题，value是列值)。没有值的单元格其value值为null
     * 返回结果最外层的list对应一个sheet页，第二层的map对应sheet页中的一行
     *
     * @param filePath
     * @param sheetName
     * @return
     * @throws Exception
     */
    public static Iterable<Map<String, Object>> readExcelWithTitle(String filePath, String sheetName) throws Exception {
        Workbook wb = null;
        Iterable<Map<String, Object>> sheetList = null;
        try {
            if (filePath.endsWith(EXCEL_XLSX) || filePath.endsWith(EXCEL_XLS)) {
                // 读取Excel文档
                File file = new File(filePath);
                wb = getWorkbook(file);
                Sheet sheet = wb.getSheet(sheetName) != null ? wb.getSheet(sheetName) : wb.getSheetAt(0);
                sheetList = new ArrayList<Map<String, Object>>();
                List<String> titles = new ArrayList<String>();
                int rowSize = sheet.getLastRowNum() + 1;
                for (int j = 0; j < rowSize; j++) {
                    Row row = sheet.getRow(j);
                    if (row == null) {
                        // 略过空行
                        continue;
                    }
                    int cellSize = row.getLastCellNum();
                    if (j == 0) {
                        // 第一行是标题行
                        for (int k = 0; k < cellSize; k++) {
                            Cell cell = row.getCell(k);
                            titles.add(cell.toString());
                        }
                    } else {
                        // 对应一个数据行
                        Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
                        for (int k = 0; k < titles.size(); k++) {
                            String value = null;
                            Cell cell = null;
                            cell = row.getCell(k);
                            int cellType = 99;
                            if (cell != null) {
                                cellType = cell.getCellType();
                            }
                            switch (cellType) {
                                case Cell.CELL_TYPE_STRING:
                                    // 1
                                    value = cell.getRichStringCellValue().getString();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    // 0
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        Date date = cell.getDateCellValue();
                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        value = df.format(date);
                                    } else {
                                        value = String.valueOf(cell.getNumericCellValue());
                                        NumberFormat nf = NumberFormat.getInstance();
                                        String s = nf.format(cell.getNumericCellValue());
                                        if (s.indexOf(",") >= 0) {
                                            s = s.replace(",", "");
                                        }
                                        value = s;
                                    }
                                    break;
                                case Cell.CELL_TYPE_FORMULA:
                                    // 2
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    value = cell.getStringCellValue();
                                    break;
                                default:
                                    if (cell != null) {
                                        value = cell.toString();
                                    }
                            }
                            String key = titles.get(k);
                            rowMap.put(key, value);
                        }
                        ((ArrayList<Map<String, Object>>) sheetList).add(rowMap);
                    }
                }
            } else {
                logger.error("读取的不是excel格式文件");
            }
            return sheetList;

        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            if (wb != null) {
                wb.close();
            }
        }
    }

    /**
     * 删除excel文件中指定表格内的多行记录
     *
     * @param filePath  excel文件路径
     * @param attribte  要查找的属性
     * @param values    要删除的值集合
     * @param sheetName 表名称
     */
    public static void deletebyRowIndex(String filePath, String attribte, List<String> values, String sheetName) {
        Workbook wb = null;
        try {
            if (filePath.endsWith(EXCEL_XLSX) || filePath.endsWith(EXCEL_XLS)) {
                File file = new File(filePath);
                wb = getWorkbook(file);
                Sheet sheet = wb.getSheet(sheetName) != null ? wb.getSheet(sheetName) : wb.getSheetAt(0);
                Row firstRow = sheet.getRow(0);
                int columnNum = firstRow.getPhysicalNumberOfCells();
                int colIndex = 0;
                for (int j = 0; j < columnNum; j++) {
                    Cell cell = firstRow.getCell(j);
                    String cellValue = cell.getStringCellValue();
                    if (attribte.equalsIgnoreCase(cellValue)) {
                        colIndex = j;
                        break;
                    }
                }
                for (String value : values) {
                    deleteRow(filePath, attribte, value, wb, sheet, colIndex);
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void deleteRow(String filePath, String attribte, String value, Workbook wb, Sheet sheet,
                                  int colIndex) throws FileNotFoundException, IOException {
        int rowNum = sheet.getLastRowNum();
        int row = 0;
        // 略过标题行
        for (int i = 1; i <= rowNum; i++) {
            Cell cell = sheet.getRow(i).getCell(colIndex);
            switch (cell.getCellTypeEnum()) {
                case _NONE:
                    break;
                case BLANK:
                    break;
                case BOOLEAN:
                    break;
                case ERROR:
                    break;
                case FORMULA:
                    break;
                case NUMERIC:
                    break;
                case STRING:
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.equalsIgnoreCase(value)) {
                        row = i;
                    }
                    break;
                default:
                    break;
            }
        }
        // 不删除标题所在的第一行
        if (row > 0 && row < rowNum) {
            sheet.shiftRows(row + 1, rowNum, -1);
        } else if (row == rowNum) {
            Row removingRow = sheet.getRow(row);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
        FileOutputStream os = new FileOutputStream(filePath);
        wb.write(os);
        os.close();
    }
}

