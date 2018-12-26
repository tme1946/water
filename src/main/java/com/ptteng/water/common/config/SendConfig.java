package com.ptteng.water.common.config;

import com.ptteng.water.system.pojo.Element;
import com.ptteng.water.util.ExportExcelUtil;
import com.ptteng.water.util.PerformanceTree;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Component
@RabbitListener(queues = "excel")
public class SendConfig {

    @RabbitHandler
    public void process(Map msg) throws IOException {
        String sheetName = (String) msg.get("sheetName");
        Long random = (Long) msg.get("random");
        ArrayList<Element> performances = (ArrayList) msg.get("performances");
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        XSSFWorkbook wb =  exportExcelUtil.getWb();

        wb.setSheetName(0, sheetName);
        // 每行的 XSSFRow 只能创建一次
        ArrayList<Element> children = new ArrayList<>();
        for (Element element : performances) {
            PerformanceTree.getTreeList(element, children);
        }
        exportExcelUtil.setRowList(new XSSFRow[children.size() + 3]);
        exportExcelUtil.setData(performances, 3, 0);
        exportExcelUtil.mergeCell(performances, 3, 0);
        FileOutputStream output = new FileOutputStream("C:\\Users\\Joe\\Desktop\\img\\" + sheetName + random +".xlsx");
        wb.write(output);
        wb.close();
        output.flush();
    }
}
