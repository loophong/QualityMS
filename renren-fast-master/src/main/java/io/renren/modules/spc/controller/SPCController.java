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

        List<Double> unliang = new ArrayList<>();
        unliang.add(1.97);unliang.add(1.24);unliang.add(3.82);unliang.add(1.71);unliang.add(1.95);
        unliang.add(2.40);unliang.add(2.42);unliang.add(1.73);unliang.add(1.29);unliang.add(3.15);
        unliang.add(1.97);unliang.add(1.78);unliang.add(2.38);unliang.add(2.04);unliang.add(1.32);
        unliang.add(1.61);unliang.add(1.86);unliang.add(1.37);unliang.add(4.22);unliang.add(2.58);
        unliang.add(3.78);unliang.add(1.07);unliang.add(1.51);unliang.add(0.71);unliang.add(2.16);
        dataList.add(unliang);

        List<Double> UCL = new ArrayList<>();
        UCL.add(2.95);UCL.add(3.19);UCL.add(3.04);UCL.add(2.94);UCL.add(3.03);
        UCL.add(2.99);UCL.add(3.04);UCL.add(3.04);UCL.add(2.97);UCL.add(3.29);
        UCL.add(2.97);UCL.add(3.29);UCL.add(2.97);UCL.add(3.19);UCL.add(3.01);
        UCL.add(3.12);UCL.add(2.95);UCL.add(2.95);UCL.add(3.17);UCL.add(2.99);
        UCL.add(3.13);UCL.add(3.09);UCL.add(3.03);UCL.add(2.95);UCL.add(3.00);
        dataList.add(UCL);

        List<Double> CL = new ArrayList<>();
        CL.add(2.07);CL.add(2.07);CL.add(2.07);CL.add(2.07);CL.add(2.07);
        CL.add(2.07);CL.add(2.07);CL.add(2.07);CL.add(2.07);CL.add(2.07);
        CL.add(2.07);CL.add(2.07);CL.add(2.07);CL.add(2.07);CL.add(2.07);
        CL.add(2.07);CL.add(2.07);CL.add(2.07);CL.add(2.07);CL.add(2.07);
        CL.add(2.07);CL.add(2.07);CL.add(2.07);CL.add(2.07);CL.add(2.07);
        dataList.add(CL);

        List<Double> LCL = new ArrayList<>();
        LCL.add(1.20);LCL.add(0.95);LCL.add(1.10);LCL.add(1.21);LCL.add(1.12);
        LCL.add(1.15);LCL.add(1.10);LCL.add(1.11);LCL.add(1.17);LCL.add(0.86);
        LCL.add(1.18);LCL.add(0.96);LCL.add(1.13);LCL.add(1.03);LCL.add(1.19);
        LCL.add(1.19);LCL.add(0.97);LCL.add(1.16);LCL.add(1.25);LCL.add(1.17);
        LCL.add(1.02);LCL.add(1.06);LCL.add(1.12);LCL.add(1.20);LCL.add(1.15);
        dataList.add(LCL);

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
