package io.renren.modules.spc.controller;

import java.io.IOException;
import java.math.BigDecimal;
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


        //子组大小
        int arrayLength = getCellValueAsInt(sheet.getRow(3).getCell(12));

        int arraySize = getCellValueAsInt(sheet.getRow(5).getCell(12));

        double[][] date = new double[arrayLength][arraySize];
        int n = 0;
        for(int i = 8; i < 8 + arrayLength; i++ ){
            Row row = sheet.getRow(i);
            int m = 0;
            for(int j = 2; j < 2 + arraySize; j++){
                date[n][m] = getNumericCellValue(row.getCell(j));
                m++;
            }
            n++;
        }

//        for (double[] doubles : date) {
//            for (int j = 0; j < date[0].length; j++) {
//                System.out.print(doubles[j] + " ");
//            }
//            System.out.println();
//        }

        //计算求和
        List<Double> sumList = new ArrayList<>();

        for(int i = 0; i < arraySize; i++){
            BigDecimal sum = BigDecimal.ZERO;
            for (int j = 0; j < arrayLength; j++){
                sum = sum.add(BigDecimal.valueOf(date[j][i]));
            }
            sumList.add(sum.doubleValue());
        }
        dataList.add(sumList);

        //计算平均值
        List<Double> averageList = new ArrayList<>();
        for (Double value : sumList) {
            BigDecimal bigDecimalValue = BigDecimal.valueOf(value); // 将Double转换为BigDecimal
            BigDecimal dividedValue = bigDecimalValue.divide(BigDecimal.valueOf(arrayLength)); // 除以5
            averageList.add(dividedValue.doubleValue()); // 将结果转换回Double并添加到新列表中
        }
        dataList.add(averageList);
//        System.out.println(averageList);

        //计算R
        List<Double> R = new ArrayList<>();
        for(int col = 0; col < arraySize; col++){
            BigDecimal max = BigDecimal.valueOf(date[0][col]); // 假设第一行的元素是最大值
            BigDecimal min = BigDecimal.valueOf(date[0][col]); // 假设第一行的元素是最小值


            for (int row = 1; row < date.length; row++) { // 遍历该列的每一行
                BigDecimal current = BigDecimal.valueOf(date[row][col]);
                if (current.compareTo(max) > 0) {
                    max = current; // 更新最大值
                }
                if (current.compareTo(min) < 0) {
                    min = current; // 更新最小值
                }
            }
            BigDecimal difference = max.subtract(min); // 计算最大值与最小值的差
            R.add(difference.doubleValue()); // 将差值转换为Double并添加到列表R中
        }
        dataList.add(R);



        workbook.close();
        return dataList;
    }

    // 将单元格内容转换为int类型值
    private static int getCellValueAsInt(Cell cell) {
        if (cell == null) {
            return 0; // 或者你可以选择抛出异常，根据你的业务逻辑来决定
        }
        // 确保单元格是数值类型
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            // 如果单元格是字符串类型，尝试将其转换为整数
            try {
                return Integer.parseInt(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                // 如果转换失败，可以选择返回默认值或者抛出异常
                return 0;
            }
        } else {
            // 如果单元格不是数值类型也不是字符串类型，可以选择返回默认值或者抛出异常
            return 0;
        }
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
