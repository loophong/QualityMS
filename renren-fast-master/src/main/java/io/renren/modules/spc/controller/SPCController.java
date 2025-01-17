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
import javafx.scene.chart.Chart;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.checkerframework.checker.units.qual.C;
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


    /**
     * 此接口用于图表预警，前端调用后，一个页签数据统一输入
     * 返回结果按照八种准则的顺序以此填装，图表类型由前端判断
     * */
    @RequestMapping("/XR/warning")
    public R XRwarning(@RequestBody List<List<Double>> ChartData) {
        // 打印接收到的数据
        System.out.println("接收到的 XRChartData: " + ChartData);

        List<List<Double>> firstFour = new ArrayList<>();
        List<List<Double>> lastFour = new ArrayList<>();
        // 取前四个子列表
        for (int i = 0; i < 4; i++) {
            firstFour.add(ChartData.get(i));
        }
        // 取后四个子列表
        for (int i = 8 - 4; i < 8; i++) {
            lastFour.add(ChartData.get(i));
        }

        List<List<Boolean>> result = new ArrayList<>();
        List<Boolean> result_firstFour = warning(firstFour);
        List<Boolean> result_lastFour = warning(lastFour);

        result.add(result_firstFour);
        result.add(result_lastFour);

        return R.ok().put("Warning", result);
    }

    @RequestMapping("/P/warning")
    public R Pwarning(@RequestBody List<List<Double>> ChartData) {
        // 打印接收到的数据
        System.out.println("接收到的 PChartData: " + ChartData);

        List<Boolean> result = warning(ChartData);

        return R.ok().put("Warning", result);
    }

    //预警部分八条准则的判断

    /**
     *
     * 计算过程中，首先需要使用三条控制限进行区域划分，从中心限到上限分别为C、B、A区域，从中心限到下限分别为C、B、A区域
     * 这六个区域平均划分
     *
     */

    public List<Boolean> warning(List<List<Double>> ChartData){
        List<Boolean> result = new ArrayList<>();

        result.add(Criterion1(ChartData));
        result.add(Criterion2(ChartData));
        result.add(Criterion3(ChartData));
        result.add(Criterion4(ChartData));
        result.add(Criterion5(ChartData));
        result.add(Criterion6(ChartData));
        result.add(Criterion7(ChartData));
        result.add(Criterion8(ChartData));
        
        return result;
    }

    //准则1：数据中任意一点落在界限外  返回true为需要预警，返回false为不需预警
    public boolean Criterion1(List<List<Double>> datalist){
        List<Double> data = datalist.get(0);
        List<Double> ucl = datalist.get(1);
        List<Double> lcl = datalist.get(3);

        for(int i = 0; i < data.size(); i++){
            if(data.get(i) < lcl.get(i) || data.get(i) > ucl.get(i)){
                return true;
            }
        }
        return false;
    }

    //准则2：数据中连续9点落在中心限同一侧  返回true为需要预警，返回false为不需预警
    public boolean Criterion2(List<List<Double>> datalist){
        List<Double> data = datalist.get(0);

        List<Double> cl = datalist.get(2);

        // 确保列表的长度足够以避免索引越界
        if (data.size() < 9 || cl.size() < 9) {
            return false;
        }

        // 遍历 data 列表，检查是否有连续的 9 个数大于或小于 cl 列表对应位置的数
        int consecutiveCount; // 用于计算连续的数字数量

        // 检查大于的情形
        for (int i = 0; i <= data.size() - 9; i++) {
            consecutiveCount = 0; // 重置连续计数器
            for (int j = 0; j < 9; j++) {
                if (data.get(i + j) > cl.get(i + j)) {
                    consecutiveCount++;
                } else {
                    break;
                }
            }
            if (consecutiveCount == 9) {
                return true; // 找到连续的 9 个数大于 cl 中的对应数
            }
        }

        // 检查小于的情形
        for (int i = 0; i <= data.size() - 9; i++) {
            consecutiveCount = 0; // 重置连续计数器
            for (int j = 0; j < 9; j++) {
                if (data.get(i + j) < cl.get(i + j)) {
                    consecutiveCount++;
                } else {
                    break;
                }
            }
            if (consecutiveCount == 9) {
                return true; // 找到连续的 9 个数小于 cl 中的对应数
            }
        }

        return false; // 未找到任何符合条件的连续数字
    }

    //准则3：连续6点递增或递减  返回true为需要预警，返回false为不需预警
    public boolean Criterion3(List<List<Double>> datalist){
        List<Double> data = datalist.get(0);
        // 确保列表的长度足够以避免索引越界
        if (data.size() < 6) {
            return false;
        }

        int consecutiveCount = 1; // 用于计算连续的数字数量

        // 遍历数据
        for (int i = 1; i < data.size(); i++) {
            // 检查当前元素与前一个元素的关系
            if (data.get(i) > data.get(i - 1)) {
                // 当前元素大于前一个元素，增加递增计数
                consecutiveCount++;
                if (consecutiveCount == 6) {
                    return true; // 找到连续的 6 个递增数
                }
            } else if (data.get(i) < data.get(i - 1)) {
                // 当前元素小于前一个元素，增加递减计数
                consecutiveCount = -1; // 记录为递减状态
                int decreaseCount = 1; // 重新计数递减的数量
                while (i < data.size() && data.get(i) < data.get(i - 1)) {
                    decreaseCount++;
                    if (decreaseCount == 6) {
                        return true; // 找到连续的 6 个递减数
                    }
                    i++;
                }
                i--; // 由于 while 循环会增加 i，需减回
                consecutiveCount = 1; // 重置递增计数
            } else {
                // 当前元素与前一个元素相等，重置计数
                consecutiveCount = 1;
            }
        }

        return false; // 未找到符合条件的连续数字
    }

    //准则4：连续14点中相邻点升降交错  返回true为需要预警，返回false为不需预警
    public boolean Criterion4(List<List<Double>> datalist){
        List<Double> data = datalist.get(0);
        // 确保列表的长度足够以避免索引越界
        if (data.size() < 14) {
            return false;
        }
        // 遍历数据，检查是否有交错序列
        for (int i = 0; i <= data.size() - 14; i++) {
            boolean alternates = true;

            // 检查当前 14 个连续点是否交错
            for (int j = 1; j < 14; j++) {
                // 判断相邻点的升降关系
                if ((data.get(i + j) > data.get(i + j - 1) && j % 2 == 0) ||
                        (data.get(i + j) < data.get(i + j - 1) && j % 2 == 1)) {
                    // 若不符合交错，设置标志为 false
                    alternates = false;
                    break;
                }
            }

            // 若已存在符合条件的交错序列
            if (alternates) {
                return true;
            }
        }

        return false; // 未找到符合条件的连续数字
    }

    //准则5：连续3点中有2点落在中心线同一侧的B区之外  返回true为需要预警，返回false为不需预警
    public boolean Criterion5(List<List<Double>> datalist){
        List<Double> data = datalist.get(0);
        List<Double> ucl = datalist.get(1);
        List<Double> cl = datalist.get(2);
        List<Double> lcl = datalist.get(3);

        int dataSize = data.size();

        for (int i = 0; i < dataSize - 2; i++) {
            // 取当前的中心线和控制限
            double centerLine = cl.get(i);
            double upperControlLimit = ucl.get(i);
            double lowerControlLimit = lcl.get(i);

            // 区域边界
            double upperB = upperControlLimit - (upperControlLimit + centerLine) / 3;
            double lowerB = lowerControlLimit + (centerLine - lowerControlLimit) / 3;

            int count = 0;
            boolean sameSide = true;
            Boolean firstPointSide = null; // 用于记录第一点的侧

            // 检查连续的 3 个点
            for (int j = 0; j < 3; j++) {
                double point = data.get(i + j);
                boolean isOutsideB = (point > upperB) || (point < lowerB); // 判断是否在 B 区之外

                if (isOutsideB) {
                    count++;
                    // 确定点的侧
                    if (point > centerLine) {
                        if (firstPointSide == null) {
                            firstPointSide = true; // 方向上面
                        } else if (!firstPointSide) {
                            sameSide = false;
                        }
                    } else {
                        if (firstPointSide == null) {
                            firstPointSide = false; // 方向下面
                        } else if (firstPointSide) {
                            sameSide = false;
                        }
                    }
                }
            }
            // 如果有 2 个点在同一侧且不在 B 区
            if (count >= 2 && sameSide) {
                return true; // 找到符合条件的点
            }
        }
        return false; // 没找到符合条件的点
    }

    //准则6：连续5点中有4点落在中心线同一侧的C区之外  返回true为需要预警，返回false为不需预警
    public boolean Criterion6(List<List<Double>> datalist){
        List<Double> data = datalist.get(0);
        List<Double> ucl = datalist.get(1);
        List<Double> cl = datalist.get(2);
        List<Double> lcl = datalist.get(3);

        int dataSize = data.size();

        for (int i = 0; i <= dataSize - 5; i++) {  // 允许检查连续 5 个点
            // 取当前的中心线和控制限
            double centerLine = cl.get(i);
            double upperControlLimit = ucl.get(i);
            double lowerControlLimit = lcl.get(i);

            // 根据给定的公式计算 B 区边界，但现在用于判断 C 区
            double upperC = upperControlLimit - 2 * (upperControlLimit - centerLine) / 3;
            double lowerC = lowerControlLimit + 2 * (centerLine - lowerControlLimit) / 3;

            int count = 0;
            boolean sameSide = true;
            Boolean firstPointSide = null; // 用于记录第一点的侧

            // 检查连续的 5 个点
            for (int j = 0; j < 5; j++) {
                double point = data.get(i + j);
                boolean isOutsideC = (point > upperC) || (point < lowerC); // 判断是否在 C 区之外

                if (isOutsideC) {
                    count++;
                    // 确定点的侧
                    if (point > centerLine) {
                        if (firstPointSide == null) {
                            firstPointSide = true; // 方向上面
                        } else if (!firstPointSide) {
                            sameSide = false;
                        }
                    } else {
                        if (firstPointSide == null) {
                            firstPointSide = false; // 方向下面
                        } else if (firstPointSide) {
                            sameSide = false;
                        }
                    }
                }
            }
            // 如果有 4 个点在同一侧且不在 C 区
            if (count >= 4 && sameSide) {
                return true; // 找到符合条件的点
            }
        }
        return false; // 没找到符合条件的点
    }

    //准则7：连续15个点落在c区之外  返回true为需要预警，返回false为不需预警
    public boolean Criterion7(List<List<Double>> datalist){
        List<Double> data = datalist.get(0);
        List<Double> ucl = datalist.get(1);
        List<Double> cl = datalist.get(2);
        List<Double> lcl = datalist.get(3);

        int dataSize = data.size();

        for (int i = 0; i <= dataSize - 15; i++) {  // 允许检查连续 15 个点
            // 取当前的中心线和控制限
            double centerLine = cl.get(i);
            double upperControlLimit = ucl.get(i);
            double lowerControlLimit = lcl.get(i);

            // 根据给定的公式计算 C 区的边界
            double upperC = upperControlLimit - (upperControlLimit - centerLine) / 3;
            double lowerC = lowerControlLimit + (centerLine - lowerControlLimit) / 3;

            int outsideCount = 0; // 记录落在 C 区之外的点数量

            // 检查连续的 15 个点
            for (int j = 0; j < 15; j++) {
                double point = data.get(i + j);
                boolean isOutsideC = (point > upperC) || (point < lowerC); // 判断是否在 C 区之外

                if (isOutsideC) {
                    outsideCount++;
                }
            }

            // 如果所有 15 个点都在 C 区之外
            if (outsideCount == 15) {
                return true; // 找到符合条件的点
            }
        }
        return false; // 没找到符合条件的点
    }

    //准则8：连续8个点落在中心线两侧，但是无1点落在C区之内  返回true为需要预警，返回false为不需预警
    public boolean Criterion8(List<List<Double>> datalist){
        List<Double> data = datalist.get(0);
        List<Double> ucl = datalist.get(1);
        List<Double> cl = datalist.get(2);
        List<Double> lcl = datalist.get(3);

        int dataSize = data.size();

        for (int i = 0; i <= dataSize - 8; i++) {  // 允许检查连续 8 个点
            // 取当前的中心线和控制限
            double centerLine = cl.get(i);
            double upperControlLimit = ucl.get(i);
            double lowerControlLimit = lcl.get(i);

            // 根据给定的公式计算 C 区的边界
            double upperC = upperControlLimit - (upperControlLimit - centerLine) / 3;
            double lowerC = lowerControlLimit + (centerLine - lowerControlLimit) / 3;

            boolean hasAbove = false;  // 是否存在大于中心线的点
            boolean hasBelow = false;   // 是否存在小于中心线的点
            boolean inCZone = false;    // 是否有点在 C 区内

            // 检查连续的 8 个点
            for (int j = 0; j < 8; j++) {
                double point = data.get(i + j);
                // 判断是否在 C 区内
                if ((point > upperC) || (point < lowerC)) {
                    // 点不在 C 区范围内，检查与中心线的关系
                    if (point > centerLine) {
                        hasAbove = true;   // 记录大于中心线的点
                    }
                    else {
                        hasBelow = true;    // 记录小于中心线的点
                    }
                } else {
                    inCZone = true; // 有点在 C 区内
                    break; // 一旦确定有点在 C 区，直接退出循环
                }
            }

            // 如果有点在 C 区内，则不满足条件
            if (!inCZone && hasAbove && hasBelow) {
                return true; // 找到符合条件的点
            }
        }
        return false; // 没找到符合条件的点
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
            spcPtdEntity.setDataImportTime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            spcPtdEntity.setSpare(table_name);
            dataList.add(spcPtdEntity);
        }
        List<Double> work_time = getWorkTime(dataList);
        List<Double> acceptanceRegion = getAcceptanceRegion(dataList);

        List<Integer> frequency = calculateWorkTimeForRegions(work_time, acceptanceRegion);

        for(int i = 0; i < dataList.size(); i++){
            SpcPtdEntity spcPtdEntity = dataList.get(i);
            spcPtdEntity.setFrequency(frequency.get(i));
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

    //工时
    public List<Double> getWorkTime(List<SpcPtdEntity> datalist){
        List<Double> work_time = new ArrayList<>();
        for(SpcPtdEntity spcPtdEntity : datalist){
            if(spcPtdEntity.getWorkTime() != null){
                work_time.add(spcPtdEntity.getWorkTime());
            }
            else {
                work_time.add(0.0);
            }
        }
        return work_time;
    }

    //接收区域
    public List<Double> getAcceptanceRegion(List<SpcPtdEntity> datalist){
        List<Double> acceptance_region = new ArrayList<>();
        for(SpcPtdEntity spcPtdEntity : datalist){
            if(spcPtdEntity.getAcceptanceRegion() != null){
                acceptance_region.add(spcPtdEntity.getAcceptanceRegion());
            }
            else {
                acceptance_region.add(0.0);
            }
        }
        return acceptance_region;
    }

    // 计算在指定范围内的工作时间数量
    public int countWorkTimeInAcceptanceRegion(List<Double> workTime, double lowerBound, Double upperBound) {
        int count = 0;

        for (Double time : workTime) {
            // 处理上界为 null 时，只检查下界
            if (upperBound == null) {
                if (time >= lowerBound) {
                    count++;
                }
            } else {
                // 标准范围检验
                if (time >= lowerBound && time < upperBound) {
                    count++;
                }
            }
        }

        return count;
    }

    // 对 acceptanceRegion 中的每个区间计算工作时间
    public List<Integer> calculateWorkTimeForRegions(List<Double> workTime, List<Double> acceptanceRegion) {
        // 确保 acceptanceRegion 至少包含一个元素
        if (acceptanceRegion.isEmpty()) {
            throw new IllegalArgumentException("acceptanceRegion list must contain at least one element for calculations.");
        }

        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < acceptanceRegion.size(); i++) {
            double lowerBound = acceptanceRegion.get(i);
            Double upperBound = (i < acceptanceRegion.size() - 1) ? acceptanceRegion.get(i + 1) : null; // 最后一项不提供上界

            int count = countWorkTimeInAcceptanceRegion(workTime, lowerBound, upperBound);
            result.add(count);
        }

        return result;
    }


}
