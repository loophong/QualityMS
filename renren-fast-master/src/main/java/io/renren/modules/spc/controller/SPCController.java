package io.renren.modules.spc.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.aliyun.oss.ServiceException;
import io.renren.modules.spc.entity.*;
import io.renren.modules.spc.service.*;
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



    @Resource
    private SpcXrchartService spcXrchartService;

    @Resource
    private SpcPchartService spcPchartService;

    @Resource
    private SpcPtdService spcPtdService;

    @Resource
    private SpcPtdPvalueService spcPtdPvalueService;

    @Resource
    private SpcXrchartStandardsService spcXrchartStandardsService;

    @Resource
    private SpcPchartStandardsService spcPchartStandardsService;

    @RequestMapping(value = "/XR", method = RequestMethod.POST)
    public ResponseEntity<?> importXR(@RequestParam("file") MultipartFile excelFile){

        System.out.println("------------XR-------XR------------");
        try {

            /**
             * 导入X-R图表数据
             * 从表中获取一些固定值，包括9个基准值，当前日期（以上传日期为准）
             * */
            List<SpcXrchartEntity> XRchartDataList = parseExcel2XRchart(excelFile);
            spcXrchartService.importData(XRchartDataList);

            return ResponseEntity.ok("Success");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("excel上传失败");
        }
    }

    @RequestMapping(value = "/P", method = RequestMethod.POST)
    public ResponseEntity<?> importP(@RequestParam("file") MultipartFile excelFile){

        System.out.println("------------P-------P------------");
        try {

            /**
             * 导入P-chart图表数据
             * 从表中获取一些固定值，当前日期（以上传日期为准）
             * */

            List<SpcPchartEntity> PchartDataList = parseExcel2Pchart(excelFile);
            spcPchartService.importData(PchartDataList);

            return ResponseEntity.ok("Success");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("excel上传失败");
        }
    }

    @RequestMapping(value = "/PTD", method = RequestMethod.POST)
    public ResponseEntity<?> importPTD(@RequestParam("file") MultipartFile excelFile){

        System.out.println("------------test-------test------------");
        try {

            /**
             * 导入PTD图表数据
             *
             * */
            List<SpcPtdEntity> PtdDataList = parseExcel2Ptd(excelFile);
            spcPtdService.importData(PtdDataList);

            /**
             * 导入P值图表数据
             *
             * */
            List<SpcPtdPvalueEntity> PtdPValueDataList = parseExcel2PtdPvalue(excelFile);
            spcPtdPvalueService.importData(PtdPValueDataList);

            return ResponseEntity.ok("Success");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("excel上传失败");
        }
    }

    @RequestMapping(value = "/standardsXR", method = RequestMethod.POST)
    public ResponseEntity<?> importStandardsXR(@RequestParam("file") MultipartFile excelFile){

        System.out.println("------------standardsXR-------standardsXR------------");
        try {

            /**
             * 导入X-R图表数据
             * 从表中获取一些固定值，包括9个基准值，当前日期（以上传日期为准）
             * */
            List<SpcXrchartStandardsEntity> XRchartStandardsDataList = parseExcel2XRchartStandards(excelFile);
            spcXrchartStandardsService.importData(XRchartStandardsDataList);

            return ResponseEntity.ok("Success");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("excel上传失败");
        }
    }

    @RequestMapping(value = "/standardsP", method = RequestMethod.POST)
    public ResponseEntity<?> importStandardsP(@RequestParam("file") MultipartFile excelFile){

        System.out.println("------------standardsP-------standardsP------------");
        try {

            /**
             * 导入P-chart图表数据
             * 从表中获取一些固定值，当前日期（以上传日期为准）
             * */

            List<SpcPchartStandardsEntity> PchartStandardsDataList = parseExcel2PchartStandards(excelFile);
            spcPchartStandardsService.importData(PchartStandardsDataList);

            return ResponseEntity.ok("Success");
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

        String table_name = getStringCellValue(sheet.getRow(0).getCell(0));

        //子组大小
        int arrayLength = getCellValueAsInt(sheet.getRow(3).getCell(12));

        int arraySize = getCellValueAsInt(sheet.getRow(5).getCell(12));

        DecimalFormat df = new DecimalFormat("#.00");


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

        for (double[] doubles : date) {
            SpcXrchartEntity spcXrchartEntity = new SpcXrchartEntity();

            for (int j = 0; j < Math.min(arraySize, 31); j++) {
                spcXrchartEntity.setData(j + 1, (float) doubles[j]);
            }


            //存入日期
            spcXrchartEntity.setDatatime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            spcXrchartEntity.setAlternateFields1(table_name);

            dataList.add(spcXrchartEntity);
        }

        workbook.close();
        return dataList;
    }

    public List<SpcXrchartStandardsEntity> parseExcel2XRchartStandards(MultipartFile file) throws IOException {
        List<SpcXrchartStandardsEntity> dataList = new ArrayList<>();

        ZipSecureFile.setMinInflateRatio(0);
        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        /*
        *
        X-R图
        * */
        Sheet sheet = workbook.getSheetAt(0);
        String table_name = getStringCellValue(sheet.getRow(0).getCell(0));

        //子组大小
        int standards_arrayLength = getCellValueAsInt(sheet.getRow(3).getCell(12));

        int standards_arraySize = getCellValueAsInt(sheet.getRow(5).getCell(12));

        double[][] date = new double[standards_arrayLength][standards_arraySize];
        int n = 0;
        for(int i = 8; i < 8 + standards_arrayLength; i++ ){
            Row row = sheet.getRow(i);
            int m = 0;
            for(int j = 2; j < 2 + standards_arraySize; j++){
                date[n][m] = getNumericCellValue(row.getCell(j));
                m++;
            }
            n++;
        }

        for (double[] doubles : date) {
            SpcXrchartStandardsEntity spcXrchartStandardsEntity = new SpcXrchartStandardsEntity();

            for (int j = 0; j < Math.min(standards_arraySize, 31); j++) {
                spcXrchartStandardsEntity.setData(j + 1, (float) doubles[j]);
            }
            //存入日期
            spcXrchartStandardsEntity.setDatatime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            spcXrchartStandardsEntity.setAlternateFields1(table_name);
            dataList.add(spcXrchartStandardsEntity);
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

        Sheet sheet = workbook.getSheetAt(0);

        String table_name = getStringCellValue(sheet.getRow(0).getCell(0));
        int arraySize = getCellValueAsInt(sheet.getRow(1).getCell(2));

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

        sampling_SpcPchartEntity.setAlternateFields1(table_name);
        defects_SpcPchartEntity.setAlternateFields1(table_name);

        dataList.add(sampling_SpcPchartEntity);
        dataList.add(defects_SpcPchartEntity);

        return dataList;
    }

    public List<SpcPchartStandardsEntity> parseExcel2PchartStandards(MultipartFile file) throws IOException{
        List<SpcPchartStandardsEntity> dataList = new ArrayList<>();

        ZipSecureFile.setMinInflateRatio(0);
        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        /*
        *
        P-chart图
        * */

        Sheet sheet = workbook.getSheetAt(0);
        String table_name = getStringCellValue(sheet.getRow(0).getCell(0));

        int standards_arraySize = getCellValueAsInt(sheet.getRow(1).getCell(2));
        int[] sampling_Tests_Number = new int[standards_arraySize];
        int[] defects_Number = new int[standards_arraySize];

        Row row6 = sheet.getRow(5);   //抽检数一行
        Row row7 = sheet.getRow(6);   //不良数一行

        for (int i = 1; i < 1 + standards_arraySize; i++){
            sampling_Tests_Number[i-1] = getCellValueAsInt(row6.getCell(i));
            defects_Number[i-1] = getCellValueAsInt(row7.getCell(i));
            System.out.println("--" + sampling_Tests_Number[i-1]);
            System.out.println("--" + defects_Number[i-1]);
        }

        int sum_sampling_Tests_Number = 0;
        int sum_defects_Number = 0;



        //对抽检数进行格式转换
        SpcPchartStandardsEntity sampling_SpcPchartStandardsEntity = new SpcPchartStandardsEntity();

        //对不良数进行格式转换
        SpcPchartStandardsEntity defects_SpcPchartStandardsEntity = new SpcPchartStandardsEntity();

        //存入31天的数据，如果不足31天，剩余没有数据则不填
        for (int i = 0; i < Math.min(standards_arraySize, 31); i++) {
            // 根据索引设置值
            sampling_SpcPchartStandardsEntity.setDate(i + 1, sampling_Tests_Number[i]);
            defects_SpcPchartStandardsEntity.setDate(i + 1, defects_Number[i]);

            //计算总和
            sum_sampling_Tests_Number += sampling_Tests_Number[i];
            sum_defects_Number += defects_Number[i];
        }


        System.out.println("========="+sum_sampling_Tests_Number);
        System.out.println("========="+sum_defects_Number);
        //存入补充数据,合计、数据日期、数据内容（抽检数/不良数）

        //合计
        sampling_SpcPchartStandardsEntity.setSum(sum_sampling_Tests_Number);
        defects_SpcPchartStandardsEntity.setSum(sum_defects_Number);


        //数据日期
        sampling_SpcPchartStandardsEntity.setDatatime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        defects_SpcPchartStandardsEntity.setDatatime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        //数据内容（抽检数/不良数）
        sampling_SpcPchartStandardsEntity.setDataContent("抽检数");
        defects_SpcPchartStandardsEntity.setDataContent("不良数");

        sampling_SpcPchartStandardsEntity.setAlternateFields1(table_name);
        defects_SpcPchartStandardsEntity.setAlternateFields1(table_name);

        dataList.add(sampling_SpcPchartStandardsEntity);
        dataList.add(defects_SpcPchartStandardsEntity);

        return dataList;
    }

    public List<SpcPtdEntity> parseExcel2Ptd(MultipartFile file)throws IOException{
        List<SpcPtdEntity> dataList = new ArrayList<>();

        ZipSecureFile.setMinInflateRatio(0);
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();

        String table_name = getStringCellValue(sheet.getRow(0).getCell(4));
        // Skip header row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()){
            Row row = rowIterator.next();

            if(getNumericCellValue(row.getCell(0)) == 0.0 && getNumericCellValue(row.getCell(1)) == 0.0 && getCellValueAsInt(row.getCell(3)) == 0){
                continue;
            }
            SpcPtdEntity spcPtdEntity = new SpcPtdEntity();
            spcPtdEntity.setWorkTime(getNumericCellValue(row.getCell(0)));
            spcPtdEntity.setAcceptanceRegion(getNumericCellValue(row.getCell(1)));
            spcPtdEntity.setFrequency(getCellValueAsInt(row.getCell(3)));
            spcPtdEntity.setDataImportTime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            spcPtdEntity.setSpare(table_name);
            dataList.add(spcPtdEntity);
        }

        return dataList;
    }

    public List<SpcPtdPvalueEntity> parseExcel2PtdPvalue(MultipartFile file)throws IOException{
        List<SpcPtdPvalueEntity> dataList = new ArrayList<>();

        ZipSecureFile.setMinInflateRatio(0);
        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        //获取表名
        Sheet sheet = workbook.getSheetAt(0);
        String table_name = getStringCellValue(sheet.getRow(0).getCell(4));


        Sheet sheet1 = workbook.getSheetAt(1);
        Iterator<Row> rowIterator = sheet1.rowIterator();

        while (rowIterator.hasNext()){
            Row row = rowIterator.next();

            if(getCellValueAsInt(row.getCell(1)) == 0 && getCellValueAsInt(row.getCell(2)) == 0 && getCellValueAsInt(row.getCell(3)) == 0){
                continue;
            }
            SpcPtdPvalueEntity spcPtdPvalueEntity = new SpcPtdPvalueEntity();
            spcPtdPvalueEntity.setBValue(getCellValueAsInt(row.getCell(1)));
            spcPtdPvalueEntity.setCValue(getCellValueAsInt(row.getCell(2)));
            spcPtdPvalueEntity.setDValue(getCellValueAsInt(row.getCell(3)));
            spcPtdPvalueEntity.setDatatime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            spcPtdPvalueEntity.setTableName(table_name);
            dataList.add(spcPtdPvalueEntity);
        }
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

    // 将单元格内容转换为String类型值
    private static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    // 将单元格内容转换为Date类型值
    private static Date getDateCellValue(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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
