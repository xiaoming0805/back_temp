package com.cennavi.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel导入导出工具类
 * Created by sunpengyan on 2017/9/12.
 <dependency>
 <groupId>org.apache.poi</groupId>
 <artifactId>poi-ooxml</artifactId>
 <version>3.12</version>
 </dependency>
 */
public class ExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static void main(String[] args) {


    }
    /**
     * @param @param  is
     * @param @param  excelFileName
     * @Description: 判断excel文件后缀名，生成不同的workbook
     */
    public static Workbook createWorkbook(InputStream is, String excelFileName) throws IOException {
        if (excelFileName.endsWith(".xls")) {
            return new HSSFWorkbook(is);
        } else if (excelFileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(is);
        }
        return null;
    }

    /**
     * @param @param  workbook
     * @param @param  sheetIndex
     * @param @return
     * @return Sheet
     * @throws
     * @Title: getSheet
     * @Description: 根据sheet索引号获取对应的sheet
     */
    public static Sheet getSheet(Workbook workbook, int sheetIndex) {
        return workbook.getSheetAt(0);
    }

    /**
     * 读取excel内容，单元格数据之间用逗号分隔，按行输出
     * @param is
     * @param excelFileName
     * @return
     */
    public static List<String> importLayerDataFromExcel(InputStream is, String excelFileName) {
        List<String> list = new ArrayList<>();
        try {
            //创建工作簿
            Workbook workbook = createWorkbook(is, excelFileName);
            //创建工作表sheet
            Sheet sheet = getSheet(workbook, 0);
            //获取sheet中数据的行数
            int rows = sheet.getPhysicalNumberOfRows();
            //获取表头单元格个数
            int cells = sheet.getRow(0).getPhysicalNumberOfCells();
            StringBuffer buffer = new StringBuffer();
            for (int i = 1; i < rows; i++) {//第一行为标题栏，从第二行开始取数据
                Row row = sheet.getRow(i);
                int index = 0;
                //buffer.append(i+1).append(",");//增加行号
                while (index < cells) {
                    Cell cell = row.getCell(index);
                    if (null == cell) {
                        cell = row.createCell(index);
                    }
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = null == cell.getStringCellValue() ? null : cell.getStringCellValue();
                    buffer.append(value).append(",");
                    index++;
                }
                list.add(buffer.toString().substring(0, buffer.length() - 1));
                buffer.setLength(0);
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                is.close();//关闭流
            } catch (Exception e) {
                logger.info(e.getMessage());
                e.printStackTrace();
            }
        }
        return list;

    }

    /**
     * @param @param  object
     * @param @return
     * @return boolean
     * @throws
     * @Title: isHasValues
     * @Description: 判断一个对象所有属性是否有值，如果一个属性有值(分空)，则返回true
     */
    public static boolean isHasValues(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        boolean flag = false;
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getMethod;
            try {
                getMethod = object.getClass().getMethod(methodName);
                Object obj = getMethod.invoke(object);
                if (null != obj && !"".equals(obj)) {
                    flag = true;
                    break;
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }

        }
        return flag;

    }

    public static <T> void exportDataToExcel(List<T> list, String[] headers, String title, OutputStream os) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        //设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(15);
        //生成一个样式
        HSSFCellStyle style = getCellStyle(workbook);
        //生成一个字体
        HSSFFont font = getFont(workbook);
        //把字体应用到当前样式
        style.setFont(font);

        //生成表格标题
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 300);
        HSSFCell cell = null;

        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //将数据放入sheet中
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            T t = list.get(i);
            //利用反射，根据JavaBean属性的先后顺序，动态调用get方法得到属性的值
            Field[] fields = t.getClass().getDeclaredFields();
            try {
                for (int j = 0; j < fields.length; j++) {
                    cell = row.createCell(j);
                    Field field = fields[j];
                    String fieldName = field.getName();
                    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method getMethod = t.getClass().getMethod(methodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});

                    if (null == value)
                        value = "";
                    cell.setCellValue(value.toString());

                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }

        try {
            workbook.write(os);
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }

    }


    /**
     * 导出数据
     *
     * @param list
     * @param headers
     * @param title
     * @param os
     * @param <T>
     */
    public static <T> void exportMapInfoToExcel(List<Map<String, Object>> list, String[] headers, String title, OutputStream os) {
        Workbook workbook = new HSSFWorkbook();
        if (list.size() > 65536) {
            workbook = new XSSFWorkbook();
        }

        //生成一个表格
        Sheet sheet = workbook.createSheet(title);
        //设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(20);
        //生成表格标题
        Row row = sheet.createRow(0);
        row.setHeight((short) 300);
        Cell cell = null;
        int hc = 0;
        if(headers!=null && headers.length>0){
            hc = 1;
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i);
                RichTextString text = new HSSFRichTextString(headers[i]);
                if (list.size() > 65536) {
                    text = new XSSFRichTextString(headers[i]);
                }
                cell.setCellValue(text);
//            // 调整每一列宽度
//            sheet.autoSizeColumn((short) i);
//            // 解决自动设置列宽中文失效的问题
//            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
            }
        }
        //将数据放入sheet中
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + hc);
            Map<String, Object> map = list.get(i);
            try {
                int j = 0;
                for (String header : headers) {
                    cell = row.createCell(j);
                    cell.setCellValue(map.get(header) == null ? "null" : map.get(header).toString());
                    j++;
                }
                /*for (String key : map.keySet()) {
                    cell = row.createCell(j);
                    cell.setCellValue(map.get(key) == null ? "null" : map.get(key).toString());
                    j++;
                }*/
            } catch (Exception e) {
                logger.info(e.getMessage(), e);
            }
        }
        try {
            workbook.write(os);
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }
    }

    public static void exportExcel(XSSFWorkbook workbook, int sheetNum,
                                   String sheetTitle, String[] headers, List<Map<String, Object>> result,
                                   boolean merge) throws Exception {
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        XSSFCellStyle style = workbook.createCellStyle();
        /*// 生成一个样式

        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        XSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);*/

        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        // 产生表格标题行
        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = row.createCell((short) i);

            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text.toString());
        }
        // 遍历集合数据，产生数据行
        if (result != null) {
            int index = 1;
            for (int i = 0; i < result.size(); i++) {
                row = sheet.createRow(index);
                Map<String, Object> map = result.get(i);
                int cellIndex = 0;
                for (String key : map.keySet()) {
                    XSSFCell cell = row.createCell((short) cellIndex);
                    if(map.get(key)!=null){
                        cell.setCellValue(map.get(key).toString());
                        cellIndex++;
                    }
                }
                index++;
                /*if(merge){
                    CellRangeAddress region = new CellRangeAddress(1, 2, 0, 0);
                    sheet.addMergedRegion(region);
                }*/
            }
        }
    }

    /**
     * 导出数据
     *
     * @param list
     * @param headers
     * @param title
     * @param os
     * @param <T>
     */
    public static <T> void exportMapInfoToExcel2(List<Map<String, Object>> list, String[] headers, String title, OutputStream os) {
        Workbook workbook = new HSSFWorkbook();
        if (list.size() > 65536) {
            workbook = new XSSFWorkbook();
        }

        //生成一个表格
        Sheet sheet = workbook.createSheet(title);
        //设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(20);
        //生成表格标题
        Row row = sheet.createRow(0);
        row.setHeight((short) 300);
        Cell cell = null;

        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            RichTextString text = new HSSFRichTextString(headers[i]);
            if (list.size() > 65536) {
                text = new XSSFRichTextString(headers[i]);
            }
            cell.setCellValue(text);
//            // 调整每一列宽度
//            sheet.autoSizeColumn((short) i);
//            // 解决自动设置列宽中文失效的问题
//            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
        }

        //将数据放入sheet中
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            Map<String, Object> map = list.get(i);
            try {
                int j = 0;
                for (String key : map.keySet()) {
                    cell = row.createCell(j);
                    cell.setCellValue(map.get(key).toString());
                    j++;
                }
            } catch (Exception e) {
                logger.info(e.getMessage(), e);
            }
        }
    }

    /**
     * 导出数据
     *
     * @param list    逗号分隔的字符串
     * @param headers
     * @param title
     * @param os
     * @param <T>
     */
    public static <T> void exportStrToExcel(List<String> list, String[] headers, String title, OutputStream os) {
        Workbook workbook = new HSSFWorkbook();
        if (list.size() > 65536) {
            workbook = new XSSFWorkbook();
        }

        //生成一个表格
        Sheet sheet = workbook.createSheet(title);
        //设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(20);
        //生成表格标题
        Row row = sheet.createRow(0);
        row.setHeight((short) 300);
        Cell cell = null;

        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            RichTextString text = new HSSFRichTextString(headers[i]);
            if (list.size() > 65536) {
                text = new XSSFRichTextString(headers[i]);
            }
            cell.setCellValue(text);
//            // 调整每一列宽度
//            sheet.autoSizeColumn((short) i);
//            // 解决自动设置列宽中文失效的问题
//            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
        }

        //将数据放入sheet中
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            String[] arr = list.get(i).split(",", -1);
            try {
                int j = 0;
                for (String key : arr) {
                    if (key.equals("null")) {
                        key = "";
                    }
                    cell = row.createCell(j);
                    cell.setCellValue(key);
                    j++;
                }
            } catch (Exception e) {
                logger.info(e.getMessage(), e);
            }
        }
        try {
            workbook.write(os);
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }
    }

    /**
     * @param @param  workbook
     * @param @return
     * @return HSSFCellStyle
     * @throws
     * @Title: getCellStyle
     * @Description: 获取单元格格式
     */
    public static HSSFCellStyle getCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        return style;
    }

    /**
     * @param @param  workbook
     * @param @return
     * @return HSSFFont
     * @throws
     * @Title: getFont
     * @Description: 生成字体样式
     */
    public static HSSFFont getFont(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return font;
    }
}
