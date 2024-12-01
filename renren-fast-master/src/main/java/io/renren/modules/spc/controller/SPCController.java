package io.renren.modules.spc.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import com.aliyun.oss.ServiceException;
import io.renren.modules.spc.entity.SpcPchartEntity;
import io.renren.modules.spc.entity.SpcXrchartEntity;
import io.renren.modules.spc.service.SpcPchartService;
import io.renren.modules.spc.service.SpcXrchartService;
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

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;



@RestController
@RequestMapping("SPC/spc")
public class SPCController {

    public int  arrayLength = 0;
    public int arraySize = 0;

    @Resource
    private SpcXrchartService spcXrchartService;

    @Resource
    private SpcPchartService spcPchartService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @RequiresPermissions("SPC:spc:list")
    public ResponseEntity<?> list(@RequestParam("file") MultipartFile excelFile){

        System.out.println("------------import-------import------------");
        try {

            /**
             * 导入X-R图表数据
             * 从表中获取一些固定值，包括9个基准值，当前日期（以上传日期为准）
             * */
            List<SpcXrchartEntity> XRchartDataList = parseExcel2XRchart(excelFile);
            spcXrchartService.importData(XRchartDataList);


            /**
             * 导入P-chart图表数据
             * 从表中获取一些固定值，当前日期（以上传日期为准）
             * */

            List<SpcPchartEntity> PchartDataList = parseExcel2Pchart(excelFile);
            System.out.println(PchartDataList);
            spcPchartService.importData(PchartDataList);


            //旧版本代码
            List<List<Double>> date = parseExcel(excelFile);
            return ResponseEntity.ok(date);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("excel上传失败");
        }
    }

    public List<SpcXrchartEntity> parseExcel2XRchart(MultipartFile file) throws IOException {
        List<SpcXrchartEntity> dataList = new ArrayList<>();

        ZipSecureFile.setMinInflateRatio(0);
        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        /*
        *
        X-R图
        * */

        Sheet sheet = workbook.getSheetAt(0);

        //子组大小
        arrayLength = getCellValueAsInt(sheet.getRow(3).getCell(12));

        arraySize = getCellValueAsInt(sheet.getRow(5).getCell(12));

        DecimalFormat df = new DecimalFormat("#.00");
        //标准上限、中心限、下限
        double Standards_usl = getNumericCellValue(sheet.getRow(3).getCell(10));
        double Standards_cl = getNumericCellValue(sheet.getRow(4).getCell(10));
        double Standards_lsl = getNumericCellValue(sheet.getRow(5).getCell(10));

        String formatted_Standards_usl = df.format(Standards_usl);
        String formatted_Standards_cl = df.format(Standards_cl);
        String formatted_Standards_lsl = df.format(Standards_lsl);

        double rounded_Standards_usl = Double.parseDouble(formatted_Standards_usl);
        double rounded_Standards_cl = Double.parseDouble(formatted_Standards_cl);
        double rounded_Standards_lsl = Double.parseDouble(formatted_Standards_lsl);

        //X图上限、中心限、下限
        double X_ucl = getNumericCellValue(sheet.getRow(3).getCell(16));
        double X_cl = getNumericCellValue(sheet.getRow(4).getCell(16));
        double X_lcl = getNumericCellValue(sheet.getRow(5).getCell(16));

        String formattedX_ucl = df.format(X_ucl);
        String formattedX_cl = df.format(X_cl);
        String formattedX_lcl = df.format(X_lcl);

        double roundedX_ucl = Double.parseDouble(formattedX_ucl);
        double roundedX_cl = Double.parseDouble(formattedX_cl);
        double roundedX_lcl = Double.parseDouble(formattedX_lcl);

        //R图上限、中心限、下限
        double R_ucl = getNumericCellValue(sheet.getRow(3).getCell(18));
        double R_cl = getNumericCellValue(sheet.getRow(4).getCell(18));
        double R_lcl = getNumericCellValue(sheet.getRow(5).getCell(18));

        String formattedR_ucl = df.format(R_ucl);
        String formattedR_cl = df.format(R_cl);
        String formattedR_lcl = df.format(R_lcl);

        double roundedR_ucl = Double.parseDouble(formattedR_ucl);
        double roundedR_cl = Double.parseDouble(formattedR_cl);
        double roundedR_lcl = Double.parseDouble(formattedR_lcl);



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

        for (int i = 0; i < date.length; i++){
            SpcXrchartEntity spcXrchartEntity = new SpcXrchartEntity();

            for (int j = 0; j < Math.min(arraySize, 31); j++) {
                spcXrchartEntity.setData(j + 1, (float) date[i][j]);
            }

            spcXrchartEntity.setUpperLimitStandards((float) rounded_Standards_usl);
            spcXrchartEntity.setCenterLimitStandards((float) rounded_Standards_cl);
            spcXrchartEntity.setLowerLimitStandards((float) rounded_Standards_lsl);

            spcXrchartEntity.setUpperLimitX((float) roundedX_ucl);
            spcXrchartEntity.setCenterLimitX((float) roundedX_cl);
            spcXrchartEntity.setLowerLimitX((float) roundedX_lcl);

            spcXrchartEntity.setUpperLimitR((float) roundedR_ucl);
            spcXrchartEntity.setCenterLimitR((float) roundedR_cl);
            spcXrchartEntity.setLowerLimitR((float) roundedR_lcl);

            //存入日期
            spcXrchartEntity.setDatatime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

            dataList.add(spcXrchartEntity);
        }

        workbook.close();
        return dataList;
    }

    public List<SpcPchartEntity> parseExcel2Pchart(MultipartFile file) throws IOException{
        List<SpcPchartEntity> dataList = new ArrayList<>();

        ZipSecureFile.setMinInflateRatio(0);
        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        /*
        *
        P-chart图
        * */

        Sheet sheet = workbook.getSheetAt(1);

        int[] sampling_Tests_Number = new int[arraySize];
        int[] defects_Number = new int[arraySize];

        Row row6 = sheet.getRow(5);   //抽检数一行
        Row row7 = sheet.getRow(6);   //不良数一行

        for (int i = 1; i < 1 + arraySize; i++){
            sampling_Tests_Number[i-1] = getCellValueAsInt(row6.getCell(i));
            defects_Number[i-1] = getCellValueAsInt(row7.getCell(i));

        }

        int sum_sampling_Tests_Number = 0;
        int sum_defects_Number = 0;



        //对抽检数进行格式转换
        SpcPchartEntity sampling_SpcPchartEntity = new SpcPchartEntity();

        //对不良数进行格式转换
        SpcPchartEntity defects_SpcPchartEntity = new SpcPchartEntity();

        //存入31天的数据，如果不足31天，剩余没有数据则不填
        for (int i = 0; i < Math.min(arraySize, 31); i++) {
            // 根据索引设置值
            sampling_SpcPchartEntity.setDate(i + 1, sampling_Tests_Number[i]);
            defects_SpcPchartEntity.setDate(i + 1, defects_Number[i]);

            //计算总和
            sum_sampling_Tests_Number += sampling_Tests_Number[i];
            sum_defects_Number += defects_Number[i];
        }


        System.out.println("========="+sum_sampling_Tests_Number);
        System.out.println("========="+sum_defects_Number);
        //存入补充数据,合计、数据日期、数据内容（抽检数/不良数）

        //合计
        sampling_SpcPchartEntity.setSum(sum_sampling_Tests_Number);
        defects_SpcPchartEntity.setSum(sum_defects_Number);


        //数据日期
        sampling_SpcPchartEntity.setDatatime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        defects_SpcPchartEntity.setDatatime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        //数据内容（抽检数/不良数）
        sampling_SpcPchartEntity.setDataContent("抽检数");
        defects_SpcPchartEntity.setDataContent("不良数");

        dataList.add(sampling_SpcPchartEntity);
        dataList.add(defects_SpcPchartEntity);

        return dataList;
    }


    public static List<List<Double>> parseExcel(MultipartFile file) throws IOException {
        List<List<Double>> dataList = new ArrayList<>();

        ZipSecureFile.setMinInflateRatio(0);
        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        /*
        *
        X-R图
        * */


        Sheet sheet = workbook.getSheetAt(0);


        //子组大小
        int arrayLength = getCellValueAsInt(sheet.getRow(3).getCell(12));

        int arraySize = getCellValueAsInt(sheet.getRow(5).getCell(12));

        DecimalFormat df = new DecimalFormat("#.00");
        //X图上限、中心限、下限
        double X_ucl = getNumericCellValue(sheet.getRow(3).getCell(16));
        double X_cl = getNumericCellValue(sheet.getRow(4).getCell(16));
        double X_lcl = getNumericCellValue(sheet.getRow(5).getCell(16));

        String formattedX_ucl = df.format(X_ucl);
        String formattedX_cl = df.format(X_cl);
        String formattedX_lcl = df.format(X_lcl);

        double roundedX_ucl = Double.parseDouble(formattedX_ucl);
        double roundedX_cl = Double.parseDouble(formattedX_cl);
        double roundedX_lcl = Double.parseDouble(formattedX_lcl);

        //R图上限、中心限、下限
        double R_ucl = getNumericCellValue(sheet.getRow(3).getCell(18));
        double R_cl = getNumericCellValue(sheet.getRow(4).getCell(18));
        double R_lcl = getNumericCellValue(sheet.getRow(5).getCell(18));

        String formattedR_ucl = df.format(R_ucl);
        String formattedR_cl = df.format(R_cl);
        String formattedR_lcl = df.format(R_lcl);

        double roundedR_ucl = Double.parseDouble(formattedR_ucl);
        double roundedR_cl = Double.parseDouble(formattedR_cl);
        double roundedR_lcl = Double.parseDouble(formattedR_lcl);



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

        //存入X图的UCL上限
        List<Double> X_UCL = new ArrayList<>();
        for (int col = 0; col < arraySize; col++){
            X_UCL.add(roundedX_ucl);
        }
        dataList.add(X_UCL);

        //存入X图的CL中心限
        List<Double> X_CL = new ArrayList<>();
        for (int col = 0; col < arraySize; col++){
            X_CL.add(roundedX_cl);
        }
        dataList.add(X_CL);

        //存入X图的LCL下限
        List<Double> X_LCL = new ArrayList<>();
        for (int col = 0; col < arraySize; col++){
            X_LCL.add(roundedX_lcl);
        }
        dataList.add(X_LCL);

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

        //存入X图的UCL上限
        List<Double> R_UCL = new ArrayList<>();
        for (int col = 0; col < arraySize; col++){
            R_UCL.add(roundedR_ucl);
        }
        dataList.add(R_UCL);

        //存入X图的CL中心限
        List<Double> R_CL = new ArrayList<>();
        for (int col = 0; col < arraySize; col++){
            R_CL.add(roundedR_cl);
        }
        dataList.add(R_CL);

        //存入X图的LCL下限
        List<Double> R_LCL = new ArrayList<>();
        for (int col = 0; col < arraySize; col++){
            R_LCL.add(roundedR_lcl);
        }
        dataList.add(R_LCL);


        /*
        *
        P-Chart
        * */

        //获取数据
        Sheet sheet2 = workbook.getSheetAt(1);

        //抽检数
//        List<Integer> sampling_Tests_Number = new ArrayList<>();
//        List<Integer> defects_Number = new ArrayList<>();

        int[] sampling_Tests_Number = new int[arraySize];
        int[] defects_Number = new int[arraySize];

        Row row6 = sheet2.getRow(5);   //抽检数一行
        Row row7 = sheet2.getRow(6);   //不良数一行

        for (int i = 1; i < 1 + arraySize; i++){
            sampling_Tests_Number[i-1] = getCellValueAsInt(row6.getCell(i));
            defects_Number[i-1] = getCellValueAsInt(row7.getCell(i));
        }




        //计算不良率
        List<Double> defect_Rate = new ArrayList<>();

        int sum_Sampling_Tests_Number = 0;
        int sum_Defects_Number = 0;

        for(int i = 0; i < arraySize; i++){
            BigDecimal defectRate = BigDecimal.valueOf(defects_Number[i]).divide(BigDecimal.valueOf(sampling_Tests_Number[i]),4, RoundingMode.HALF_UP);
            defect_Rate.add(defectRate.doubleValue());
            sum_Sampling_Tests_Number += sampling_Tests_Number[i];
            sum_Defects_Number += defects_Number[i];
        }
        dataList.add(defect_Rate);

        //计算总体不良率（p）

        BigDecimal p =  BigDecimal.valueOf(sum_Defects_Number).divide(BigDecimal.valueOf(sum_Sampling_Tests_Number),4, RoundingMode.HALF_UP);

        List<Double> UCL = new ArrayList<>();

        for (int i = 0; i < arraySize; i++){

            //计算公式： UCL = p + 3 * sqrt(p * (1-p) / n) 其中n为抽检数
            BigDecimal ucl = calculateUCL(p, sampling_Tests_Number[i]);
            UCL.add(ucl.doubleValue());
        }
        dataList.add(UCL);

        List<Double> CL = new ArrayList<>();
        for(int i = 0; i < arraySize; i++){
            CL.add(p.doubleValue());
        }
        dataList.add(CL);

        List<Double> LCL = new ArrayList<>();
        for (int i = 0; i < arraySize; i++){

            //计算公式： LCL = p - 3 * sqrt(p * (1-p) / n) 其中n为抽检数
            BigDecimal lcl = calculateLCL(p, sampling_Tests_Number[i]);
            LCL.add(lcl.doubleValue());
        }
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

    public static BigDecimal calculateUCL(BigDecimal p, int samplingTestsNumber) {

        BigDecimal sqrtResult = (p.multiply(BigDecimal.valueOf(1).subtract(p))
                .divide(BigDecimal.valueOf(samplingTestsNumber), RoundingMode.HALF_UP));
        double sqrt = sqrtResult.doubleValue();
        BigDecimal ucl = p.add(BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(Math.sqrt(sqrt))));
        return ucl.setScale(4, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateLCL(BigDecimal p, int samplingTestsNumber) {

        BigDecimal sqrtResult = (p.multiply(BigDecimal.valueOf(1).subtract(p))
                .divide(BigDecimal.valueOf(samplingTestsNumber), RoundingMode.HALF_UP));
        double sqrt = sqrtResult.doubleValue();
        BigDecimal ucl = p.subtract(BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(Math.sqrt(sqrt))));
        return ucl.setScale(4, RoundingMode.HALF_UP);
    }


}
