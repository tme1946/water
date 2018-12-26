package com.ptteng.water.util;

import com.ptteng.water.system.pojo.Element;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* Spring Beans 包 */

@Slf4j
@Data
public class ExportExcelUtil implements Serializable {
    /* 表头 */
    private static final String[] TITLE = {"一级指标", "分值", "二级指标", "分值", "三级指标", "分值", "四级指标", "指标解释", "指标计划值", "实际完成值", "评价标准", "分值", "得分", "差异"};
    /* 列宽 */
    private static final Integer[] WIDTH = {10, 6, 10, 6, 10, 6, 10, 26, 12, 12, 26, 6, 6, 6};
    /* 前三级指标字段 */
    private static final String[] PROPERTIES = {"Name", "ExpectGrade"};
    /* 四级指标字段 */
    private static final String[] FOURTH_PROPERTIES = {"Name", "Explain", "Expect", "Actual", "Standard", "ExpectGrade", "Grade", "Diff"};

    private XSSFWorkbook wb;
    private XSSFSheet sheet;
    private XSSFCellStyle contentCellStyle;
    private XSSFCellStyle failedCellStyle;
    private XSSFRow[] rowList;

    // 初始化样式
    private AmqpTemplate amqpTemplate;

    public ExportExcelUtil(AmqpTemplate amqpTemplate) {
        this();
        this.amqpTemplate = amqpTemplate;
    }

    public ExportExcelUtil() {
        wb = new XSSFWorkbook();
        sheet = wb.createSheet("sheet1");

        for (int i = 0; i < WIDTH.length; i++) {
            // 每列列宽固定
            int cellWidth = WIDTH[i] * 256;
            sheet.setColumnWidth(i, cellWidth);
        }
        // 初始化样式
        XSSFCellStyle titleCellStyle = wb.createCellStyle();
        // 水平、垂直居中
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 表头背色
        titleCellStyle.setFillForegroundColor((short) 22);
        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 自动换行
        titleCellStyle.setWrapText(true);
        // 字体(宋体 10号)
        XSSFFont titleFont = wb.createFont();
        titleFont.setFontName("宋体");
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setBold(true);
        titleCellStyle.setFont(titleFont);
        XSSFFont contentFont = wb.createFont();
        contentFont.setFontName("宋体");
        contentFont.setFontHeightInPoints((short) 10);
        contentFont.setBold(false);
        contentCellStyle = (XSSFCellStyle) titleCellStyle.clone();
        failedCellStyle = (XSSFCellStyle) titleCellStyle.clone();

        contentCellStyle.setFillForegroundColor((short) 1);
        failedCellStyle.setFillForegroundColor((short) 13);

        contentCellStyle.setFont(contentFont);
        failedCellStyle.setFont(contentFont);
        // 表头内容
        XSSFRow rowTitle = sheet.createRow(1);
        for (int i = 0; i < TITLE.length; i++) {
            XSSFCell cellTitle = rowTitle.createCell(i);
            cellTitle.setCellValue(TITLE[i]);
            cellTitle.setCellStyle(titleCellStyle);
            // 合并单元格，合并单元格样式
            CellRangeAddress region = new CellRangeAddress(1, 2, i, i);
            sheet.addMergedRegion(region);
            setBorder(sheet, region);
        }
    }

    /**
     * 合并单元格边框
     * @param sheet XSSFSheet
     * @param region 单元格
     */
    private void setBorder(XSSFSheet sheet, CellRangeAddress region) {
        // 下边框
        RegionUtil.setBorderBottom(BorderStyle.THICK, region, sheet);
        RegionUtil.setBottomBorderColor(0, region, sheet);
        // 右边框
        RegionUtil.setBorderRight(BorderStyle.THICK, region, sheet);
        RegionUtil.setRightBorderColor(0, region, sheet);
        // 上边框
        RegionUtil.setBorderTop(BorderStyle.THICK, region, sheet);
        RegionUtil.setTopBorderColor(0, region, sheet);
        // 左边框
        RegionUtil.setBorderLeft(BorderStyle.THICK, region, sheet);
        RegionUtil.setLeftBorderColor(0, region, sheet);
    }

    /**
     * 单元格边框
     * @param cellStyle 单元格对应样式
     */
    private void setCellBorder(XSSFCellStyle cellStyle) {
        // 下边框
        cellStyle.setBorderBottom(BorderStyle.THICK);
        cellStyle.setBottomBorderColor((short) 0);
        // 右边框
        cellStyle.setBorderRight(BorderStyle.THICK);
        cellStyle.setRightBorderColor((short) 0);
        // 上边框
        cellStyle.setBorderTop(BorderStyle.THICK);
        cellStyle.setTopBorderColor((short) 0);
        // 左边框
        cellStyle.setBorderLeft(BorderStyle.THICK);
        cellStyle.setLeftBorderColor((short) 0);
    }

    /**
     * 合并单元格
     * @param firstRow 起始行
     * @param lastRow 结束行
     * @param firstCol 起始列
     * @param lastCol 结束列
     */
    private void merge(int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(region);
        setBorder(sheet, region);
    }

    /**
     * 写入数据
     * @param performances 根节点数组
     * @param row          开始写入的行数
     * @param separated    列的偏移数
     */
    public void setData(ArrayList<Element> performances, int row, int separated) {
        // 遍历所有根节点
        for (int i = 0; i < performances.size(); i++) {
            // 每次从根节点深入，写入数据的列数都要偏移，同级的根节点还要从相同的地方写入
            int flag = separated;
            Element element = performances.get(i);
            // 获取当前根节点的所有叶子节点，做合并单元格用
            ArrayList treeList = new ArrayList();
            PerformanceTree.getTreeList(element, treeList);
            int height = treeList.size();
            try {
                // time记录写入多少次数据，即列的偏移量
                int time = 0;
                // 判断是否是叶子节点
                // 四级节点和前三级节点遍历不同的属性
                if (height == 0) {
                    // 标记成绩是否达标
                    int flag2 = 0;
                    if (element.getGrade() != null && element.getGrade() < element.getExpectGrade()) {
                        flag2 = 1;
                    }
                    for (int j = 0; j < FOURTH_PROPERTIES.length; j++) {
                        Method m = element.getClass().getMethod("get" + FOURTH_PROPERTIES[j]);
                        Object value = m.invoke(element);
                        value = (value == null) ? "——" : value;
                        if (rowList[row + i] == null) {
                            rowList[row + i] = sheet.createRow(row + i);
                        }
                        XSSFCell cellContent = rowList[row + i].createCell(j + separated);
                        if (flag2 == 1) {
                            cellContent.setCellStyle(failedCellStyle);
                            cellContent.setCellValue(value.toString());
                            setCellBorder(failedCellStyle);
                        } else {
                            cellContent.setCellStyle(contentCellStyle);
                            cellContent.setCellValue(value.toString());
                            setCellBorder(contentCellStyle);
                        }
                        time++;
                    }
                } else {
                    for (int j = 0; j < PROPERTIES.length; j++) {
                        Method m = element.getClass().getMethod("get" + PROPERTIES[j]);
                        Object value = m.invoke(element);
                        if (value != null) {
                            if (rowList[row] == null) {
                                rowList[row] = sheet.createRow(row);
                            }
                            XSSFCell cellContent = rowList[row].createCell(j + separated);
                            cellContent.setCellStyle(contentCellStyle);
                            cellContent.setCellValue(value.toString());
                            // 单行不用合并单元格
                            if (height < 2) {
                                setCellBorder(contentCellStyle);
                            }
                            time++;
                        }
                    }
                }
                separated += time;
                // 递归调用该节点下的子节点
                setData(element.getChrildren(), row, separated);
                // 复原偏移量
                separated = flag;
            } catch (Exception e) {
                e.printStackTrace();
            }
            row += height;
        }
    }

    /**
     * 合并需要合并的单元格
     * @param performances 根节点数组数据
     * @param row 行偏移量
     * @param separated 列偏移量
     */
    public void mergeCell(ArrayList<Element> performances, int row, int separated) {
        for (Element performance : performances) {
            int flag = separated;
            ArrayList treeList = new ArrayList();
            PerformanceTree.getTreeList(performance, treeList);
            int height = treeList.size();
            // 判断是否是叶子节点
            try {
                int time = 0;
                // 判断是否需要合并单元格
                if (height > 0) {
                    for (int j = 0; j < PROPERTIES.length; j++) {
                        Method m = performance.getClass().getMethod("get" + PROPERTIES[j]);
                        Object value = m.invoke(performance);
                        if (value != null) {
                            if (height > 1) {
                                merge(row, row + height - 1, j + separated, j + separated);
                            }
                            time++;
                        }
                    }
                }
                separated += time;
                mergeCell(performance.getChrildren(), row, separated);
                separated = flag;
            } catch (Exception e) {
                e.printStackTrace();
            }
            row += height;
        }
    }

    public void getWb(ArrayList<Element> performances, String sheetName, Long random) {
        ArrayList<Element> children = new ArrayList<>();
        Map msg = new HashMap<>();
        msg.put("sheetName", sheetName);
        msg.put("random", random);
        msg.put("performances", performances);
        amqpTemplate.convertAndSend("excel", msg);

    }
}
