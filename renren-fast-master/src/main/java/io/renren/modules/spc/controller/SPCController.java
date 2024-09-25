package io.renren.modules.spc.controller;

import java.io.IOException;
import java.util.*;

import com.aliyun.oss.ServiceException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("SPC/spc")
public class SPCController {

    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @RequiresPermissions("SPC:spc:list")
    public ResponseEntity<?> list(@RequestParam("file") MultipartFile excelFile){

        System.out.println("------------import-------import------------");
        try {
            List<List<Double>> date = parseExcel(excelFile);
            System.out.println(date);
            return ResponseEntity.ok(date);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("excel上传失败");
        }
    }

    public static List<List<Double>> parseExcel(MultipartFile file) throws IOException {
        List<List<Double>> dataList = new ArrayList<>();

        ZipSecureFile.setMinInflateRatio(0);
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();

        // Skip header row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            /**
             * 将excel设置的字段，写入到数据库对应字段
             */

//            marketSalesTable.setMsId(GenerateId.getUUid());



            List<Double> date = new ArrayList<>();

            date.add(getNumericCellValue(row.getCell(0)));
            date.add(getNumericCellValue(row.getCell(1)));
            date.add(getNumericCellValue(row.getCell(2)));
            date.add(getNumericCellValue(row.getCell(3)));
            date.add(getNumericCellValue(row.getCell(4)));
            date.add(getNumericCellValue(row.getCell(5)));
            date.add(getNumericCellValue(row.getCell(6)));
            date.add(getNumericCellValue(row.getCell(7)));
            date.add(getNumericCellValue(row.getCell(8)));
            date.add(getNumericCellValue(row.getCell(9)));
            date.add(getNumericCellValue(row.getCell(10)));
            date.add(getNumericCellValue(row.getCell(11)));
            date.add(getNumericCellValue(row.getCell(12)));
            date.add(getNumericCellValue(row.getCell(13)));
            date.add(getNumericCellValue(row.getCell(14)));
            date.add(getNumericCellValue(row.getCell(15)));
            date.add(getNumericCellValue(row.getCell(16)));
            date.add(getNumericCellValue(row.getCell(17)));
            date.add(getNumericCellValue(row.getCell(18)));
            date.add(getNumericCellValue(row.getCell(19)));
            date.add(getNumericCellValue(row.getCell(20)));
            date.add(getNumericCellValue(row.getCell(21)));
            date.add(getNumericCellValue(row.getCell(22)));
            date.add(getNumericCellValue(row.getCell(23)));
            date.add(getNumericCellValue(row.getCell(24)));


//            System.out.println(marketSalesTable);

            dataList.add(date);
        }



        workbook.close();
        System.out.println(dataList.size());
        return dataList;
    }

    // 将单元格内容转换为double类型值
    private static double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return 0.0;
        }
        cell.setCellType(CellType.NUMERIC);
        return cell.getNumericCellValue();
    }
}
