package com.haibin.common.util;

import com.haibin.common.annotation.XlsColumn;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author haibin.tang
 * @create 2018-07-03 下午4:52
 **/
public class ExcelReadUtil {

    private static final String COL_PREFIXX = "COL";

    private static final String SET_PREFIX = "set";

    /**
     * 读取excel 转换成java pojo
     * 目前只支持字段类型为String
     * 如果字段是超出long型的数据 请把excel字段类型设为文本
     *
     * @param filePath 文件路径
     * @param startRow 从第几行开始读
     * @param startCol 从第几列开始读
     * @param sheetNum 从第几个shell开始读
     * @param clazz    最终转换的类型
     * @param <T>      泛型代表转换的类型
     * @return
     * @throws Exception
     */
    public static <T> List<T> read(String filePath, int startRow, int startCol, int sheetNum, Class<T> clazz) throws Exception {
        return result(read(filePath, startRow, startCol, sheetNum), clazz);
    }

    /**
     * 读取excel 转换成java pojo
     * 目前只支持字段类型为String
     * 如果字段是超出long型的数据 请把excel字段类型设为文本
     * @param inputStream excel文件流
     * @param startRow    从第几行开始读
     * @param startCol    从第几列开始读
     * @param sheetNum    从第几个shell开始读
     * @param clazz       最终转换的类型
     * @param <T>         泛型代表转换的类型
     * @return
     * @throws Exception
     */
    public static <T> List<T> read(InputStream inputStream, int startRow, int startCol, int sheetNum, Class<T> clazz) throws Exception {
        return result(read(inputStream, startRow, startCol, sheetNum), clazz);
    }

    private static <T> List<T> result(List<Map<String, String>> datas, Class<T> clazz) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> fieldNames = new HashMap<>(fields.length);
        for (Field field : fields) {
            XlsColumn annotation = field.getAnnotation(XlsColumn.class);
            if (annotation == null) {
                continue;
            }
            fieldNames.put(COL_PREFIXX + annotation.index(), SET_PREFIX + transFieldNameFirstToUpperCase(field.getName()));
        }
        List<T> result = new LinkedList<>();
        for (Map<String, String> data : datas) {
            T target = clazz.newInstance();
            for (String key : fieldNames.keySet()) {
                Method method = target.getClass().getMethod(fieldNames.get(key), String.class);
                method.invoke(target, data.get(key));
            }
            result.add(target);
        }
        return result;
    }

    private static List<Map<String, String>> read(String filePath, int startRow, int startCol, int sheetNum) throws Exception {
        File target = new File(filePath);
        return read(new FileInputStream(target), startRow, startCol, sheetNum);
    }

    private static List<Map<String, String>> read(InputStream inputStream, int startRow, int startCol, int sheetNum) throws Exception {
        List<Map<String, String>> datas = new ArrayList<>();
        Workbook book;
        try {
            book = new XSSFWorkbook(inputStream);
        } catch (Exception ex) {
            book = new HSSFWorkbook(inputStream);
        }
        //sheet 从0开始
        Sheet sheet = book.getSheetAt(sheetNum);
        //取得最后一行的行号
        int rowNum = sheet.getLastRowNum() + 1;
        //行循环开始
        for (int rowIndex = startRow; rowIndex < rowNum; rowIndex++) {
            Map<String, String> data = new HashMap<>(rowNum);
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            //每行的最后一个单元格位置
            int cellNum = row.getLastCellNum();
            //列循环开始
            for (int colIndex = startCol; colIndex < cellNum; colIndex++) {
                Cell cell = row.getCell(Short.parseShort(colIndex + ""));
                String cellValue = null;
                if (null != cell) {
                    // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            cellValue = String.valueOf((long) cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            cellValue = cell.getCellFormula();
                            break;
                        //BLANK
                        case Cell.CELL_TYPE_BLANK:
                            cellValue = "";
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            cellValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            cellValue = String.valueOf(cell.getErrorCellValue());
                            break;
                        default:
                    }
                } else {
                    cellValue = "";
                }
                data.put(COL_PREFIXX + colIndex, cellValue);
            }
            datas.add(data);
        }
        return datas;
    }

    /**
     * 转换字段名首字母为大写
     *
     * @param fieldName
     * @return
     */
    private static String transFieldNameFirstToUpperCase(String fieldName) {
        char[] fieldNameChars = fieldName.toCharArray();
        char fieldFirstCharToUpperCase = Character.toUpperCase(fieldNameChars[0]);
        fieldNameChars[0] = fieldFirstCharToUpperCase;
        return String.valueOf(fieldNameChars);
    }
}
