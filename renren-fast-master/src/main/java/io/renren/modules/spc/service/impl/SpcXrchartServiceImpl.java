package io.renren.modules.spc.service.impl;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.renren.modules.spc.dao.SpcXrchartStandardsDao;
import io.renren.modules.spc.entity.SpcXrchartStandardsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.spc.dao.SpcXrchartDao;
import io.renren.modules.spc.entity.SpcXrchartEntity;
import io.renren.modules.spc.service.SpcXrchartService;

import javax.annotation.Resource;


@Service("spcXrchartService")
public class SpcXrchartServiceImpl extends ServiceImpl<SpcXrchartDao, SpcXrchartEntity> implements SpcXrchartService {


    @Resource
    private SpcXrchartDao spcXrchartDao;

    @Resource
    private SpcXrchartStandardsDao spcXrchartStandardsDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpcXrchartEntity> page = this.page(
                new Query<SpcXrchartEntity>().getPage(params),
                new QueryWrapper<SpcXrchartEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void importData(List<SpcXrchartEntity> datalist){

        spcXrchartDao.deleteDataByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spcXrchartDao.batchInsertSpcXrcharts(datalist);
    }


    /**
     * Xbar-R图需要返回平均值、R值，以及各自上限、中心限、下限
     *
     * */
    @Override
    public List<List<Double>> getXbar_RChart(){

        List<List<Double>> result = new ArrayList<>();

        List<SpcXrchartEntity> datalist = getData();
        List<SpcXrchartStandardsEntity> datalist_standards = getStandardsData();

        List<Double> average = getAverage(datalist);
        List<Double> R = getR(datalist);

        //存入X图的三限
        List<Double> Xbar_Limit = getXbar_R_Xbar_Limit(datalist_standards);

        List<Double> Xbar_UCL = new ArrayList<>();
        List<Double> Xbar_CL = new ArrayList<>();
        List<Double> Xbar_LCL = new ArrayList<>();
        for(int i = 0; i < average.size(); i++){
            Xbar_UCL.add(Xbar_Limit.get(0));
            Xbar_CL.add(Xbar_Limit.get(1));
            Xbar_LCL.add(Xbar_Limit.get(2));
        }

        //存入R图的三限
        List<Double> R_Limit = getXbar_R_R_Limit(datalist_standards);

        List<Double> R_UCL = new ArrayList<>();
        List<Double> R_CL = new ArrayList<>();
        List<Double> R_LCL = new ArrayList<>();
        for(int i = 0; i < average.size(); i++){
            R_UCL.add(R_Limit.get(0));
            R_CL.add(R_Limit.get(1));
            R_LCL.add(R_Limit.get(2));
        }

        result.add(average);
        result.add(Xbar_UCL);
        result.add(Xbar_CL);
        result.add(Xbar_LCL);
        result.add(R);
        result.add(R_UCL);
        result.add(R_CL);
        result.add(R_LCL);

        return result;
    }

    @Override
    public List<List<Double>> getXbar_SChart(){
        List<List<Double>> result = new ArrayList<>();

        List<SpcXrchartEntity> datalist = getData();
        List<SpcXrchartStandardsEntity> datalist_standards = getStandardsData();

        //计算均值
        List<Double> average = getAverage(datalist);

        //计算标准差S
        List<Double> S = getS(datalist);

        //计算均值Xbar的三限
        List<Double> Xbar_limit = getXbar_S_Xbar_Limit(datalist_standards);


        List<Double> Xbar_UCL = new ArrayList<>();
        List<Double> Xbar_CL = new ArrayList<>();
        List<Double> Xbar_LCL = new ArrayList<>();
        for (int i = 0; i < average.size(); i++){
            Xbar_UCL.add(Xbar_limit.get(0));
            Xbar_CL.add(Xbar_limit.get(1));
            Xbar_LCL.add(Xbar_limit.get(2));
        }

        //计算标准差S的三限
        List<Double> S_limit = getXbar_S_S_Limit(datalist_standards);

        List<Double> S_UCL = new ArrayList<>();
        List<Double> S_CL = new ArrayList<>();
        List<Double> S_LCL = new ArrayList<>();
        for (int i = 0; i < average.size(); i++){
            S_UCL.add(S_limit.get(0));
            S_CL.add(S_limit.get(1));
            S_LCL.add(S_limit.get(2));
        }

        //组装数据
        result.add(average);
        result.add(Xbar_UCL);
        result.add(Xbar_CL);
        result.add(Xbar_LCL);
        result.add(S);
        result.add(S_UCL);
        result.add(S_CL);
        result.add(S_LCL);

        return result;


    }

    @Override
    public List<List<Double>> getMe_RChart(){
        List<List<Double>> result = new ArrayList<>();

        List<SpcXrchartEntity> datalist = getData();
        List<SpcXrchartStandardsEntity> datalist_standards = getStandardsData();

        //计算中位数Me
        List<Double> Me = getMe(datalist);
        result.add(Me);

        //Me三限
        List<Double> Me_Limit = getMe_R_Me_Limit(datalist_standards);

        List<Double> Me_UCL = new ArrayList<>();
        List<Double> Me_CL = new ArrayList<>();
        List<Double> Me_LCL = new ArrayList<>();
        for (int i = 0; i < Me.size(); i++){
            Me_UCL.add(Me_Limit.get(0));
            Me_CL.add(Me_Limit.get(1));
            Me_LCL.add(Me_Limit.get(2));
        }
        result.add(Me_UCL);
        result.add(Me_CL);
        result.add(Me_LCL);

        //计算极差R
        List<Double> R = getR(datalist);
        result.add(R);

        //R三限
        List<Double> R_Limit = getMe_R_R_Limit(datalist_standards);
        List<Double> R_UCL = new ArrayList<>();
        List<Double> R_CL = new ArrayList<>();
        List<Double> R_LCL = new ArrayList<>();
        for (int i = 0; i < Me.size(); i++){
            R_UCL.add(R_Limit.get(0));
            R_CL.add(R_Limit.get(1));
            R_LCL.add(R_Limit.get(2));
        }
        result.add(R_UCL);
        result.add(R_CL);
        result.add(R_LCL);

        return result;
    }

    @Override
    public List<List<Double>> getI_MRChart(){
        List<List<Double>> result = new ArrayList<>();

        List<SpcXrchartEntity> datalist = getData();
        List<SpcXrchartStandardsEntity> datalist_standards = getStandardsData();

        //计算单值I
        List<Double> I = getI(datalist);
        result.add(I);

        //Me三限
        List<Double> I_Limit = getI_MR_I_Limit(datalist_standards);

        List<Double> I_UCL = new ArrayList<>();
        List<Double> I_CL = new ArrayList<>();
        List<Double> I_LCL = new ArrayList<>();
        for (int i = 0; i < I.size(); i++){
            I_UCL.add(I_Limit.get(0));
            I_CL.add(I_Limit.get(1));
            I_LCL.add(I_Limit.get(2));
        }
        result.add(I_UCL);
        result.add(I_CL);
        result.add(I_LCL);


        //计算移动极差MR
        List<Double> MR = getMR(datalist);
        result.add(MR);

        //R三限
        List<Double> MR_Limit = getI_MR_MR_Limit(datalist_standards);
        List<Double> MR_UCL = new ArrayList<>();
        List<Double> MR_CL = new ArrayList<>();
        List<Double> MR_LCL = new ArrayList<>();
        for (int i = 0; i < MR.size(); i++){
            MR_UCL.add(MR_Limit.get(0));
            MR_CL.add(MR_Limit.get(1));
            MR_LCL.add(MR_Limit.get(2));
        }
        result.add(MR_UCL);
        result.add(MR_CL);
        result.add(MR_LCL);

        return result;
    }

    //策略步骤1：获取数据步骤
    public List<SpcXrchartEntity> getData(){
        return spcXrchartDao.getSpcXrchartEntityByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    //获取标准数据
    public List<SpcXrchartStandardsEntity> getStandardsData(){
        return spcXrchartStandardsDao.getSpcXrchartStandardsEntityByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    //策略步骤2：根据需要生成的图表类型，使用不同方法计算
    //数据求和
    public List<Double> getSum(List<SpcXrchartEntity> datalist){
        //计算求和
        List<Double> sumList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            BigDecimal sum = BigDecimal.ZERO;
            if(datalist.get(0).getData(i) == null){
                break;
            }
            for (SpcXrchartEntity spcXrchartEntity : datalist) {
                Float value = spcXrchartEntity.getData(i);
                // 使用 new BigDecimal(value.toString()) 构造 BigDecimal
                sum = sum.add(new BigDecimal(value.toString())); // 确保精度
            }
            sumList.add(sum.doubleValue()); // 将结果添加到列表中
        }
        return sumList;
    }

    //标准数据求和
    public List<Double> getStandardsSum(List<SpcXrchartStandardsEntity> datalist){
        //计算求和
        List<Double> sumList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            BigDecimal sum = BigDecimal.ZERO;
            if(datalist.get(0).getData(i) == null){
                break;
            }
            for (SpcXrchartStandardsEntity spcXrchartStandardsEntity : datalist) {
                Float value = spcXrchartStandardsEntity.getData(i);
                sum = sum.add(new BigDecimal(value.toString())); // 确保精度
            }
            sumList.add(sum.doubleValue()); // 将结果添加到列表中
        }
        return sumList;
    }


    //数据均值Xbar
    public List<Double> getAverage(List<SpcXrchartEntity> datalist){
        List<Double> sum = getSum(datalist);
        int number = datalist.size();

        List<Double> averageList = new ArrayList<>();
        for (Double value : sum){
            BigDecimal bigDecimalValue = BigDecimal.valueOf(value); // 将Double转换为BigDecimal
            BigDecimal dividedValue = bigDecimalValue.divide(BigDecimal.valueOf(number)); // 除以5
            averageList.add(dividedValue.doubleValue()); // 将结果转换回Double并添加到新列表中
        }

        return averageList;
    }

    //标准数据均值Xbar
    public List<Double> getStandardsAverage(List<SpcXrchartStandardsEntity> datalist){
        List<Double> sum = getStandardsSum(datalist);
        int number = datalist.size();

        List<Double> averageList = new ArrayList<>();
        for (Double value : sum){
            BigDecimal bigDecimalValue = BigDecimal.valueOf(value); // 将Double转换为BigDecimal
            BigDecimal dividedValue = bigDecimalValue.divide(BigDecimal.valueOf(number)); // 除以5
            averageList.add(dividedValue.doubleValue()); // 将结果转换回Double并添加到新列表中
        }

        return averageList;
    }

    //数据R值(极差R为最大值与最小值之差)
    public List<Double> getR(List<SpcXrchartEntity> datalist) {
        List<Double> result = new ArrayList<>();

        for (int col = 1; col <= 31; col++) {
            // 初始化最大值和最小值
            Float firstValue = datalist.get(0).getData(col);
            if (firstValue != null) {
                BigDecimal max = new BigDecimal(firstValue.toString());
                BigDecimal min = new BigDecimal(firstValue.toString());

                for (int row = 1; row < datalist.size(); row++) { // 遍历该列的每一行
                    Float currentValue = datalist.get(row).getData(col);
                    if (currentValue != null) {
                        BigDecimal current = new BigDecimal(currentValue.toString());
                        if (current.compareTo(max) > 0) {
                            max = current; // 更新最大值
                        }
                        if (current.compareTo(min) < 0) {
                            min = current; // 更新最小值
                        }
                    }
                }
                BigDecimal difference = max.subtract(min); // 计算最大值与最小值的差
                result.add(difference.doubleValue()); // 将差值转换为Double并添加到列表R中
            }
        }

        return result;
    }

    //标准数据R值
    public List<Double> getStandardsR(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        for (int col = 1; col <= 31; col++) {
            // 初始化最大值和最小值
            Float firstValue = datalist.get(0).getData(col);
            if (firstValue != null) {
                BigDecimal max = new BigDecimal(firstValue.toString());
                BigDecimal min = new BigDecimal(firstValue.toString());

                for (int row = 1; row < datalist.size(); row++) { // 遍历该列的每一行
                    Float currentValue = datalist.get(row).getData(col);
                    if (currentValue != null) {
                        BigDecimal current = new BigDecimal(currentValue.toString());
                        if (current.compareTo(max) > 0) {
                            max = current; // 更新最大值
                        }
                        if (current.compareTo(min) < 0) {
                            min = current; // 更新最小值
                        }
                    }
                }
                BigDecimal difference = max.subtract(min); // 计算最大值与最小值的差
                result.add(difference.doubleValue()); // 将差值转换为Double并添加到列表R中
            }
        }

        return result;
    }

    //数据标准差S
    public List<Double> getS(List<SpcXrchartEntity> datalist) {
        List<Double> result = new ArrayList<>();

        List<Double> average = getAverage(datalist);

        for (int i = 1; i <= average.size(); i++) {
            List<Double> values = new ArrayList<>();

            // 收集每个 SpcXrchartEntity 对象对应 index 的值
            for (SpcXrchartEntity entity : datalist) {
                Float value = entity.getData(i);
                if (value != null) {
                    values.add(value.doubleValue()); // 直接使用 doubleValue()
                }
            }

            double averageValue = average.get(i - 1);
            // 计算方差
            double variance = values.size() > 1 ? values.stream()
                    .mapToDouble(value -> Math.pow(value - averageValue, 2))
                    .sum() / (values.size() - 1) : 0.0;

            double stdDev = Math.sqrt(variance);
            // 将标准差结果保留2位小数
            BigDecimal roundedStdDev = new BigDecimal(stdDev).setScale(2, RoundingMode.HALF_UP);
            result.add(roundedStdDev.doubleValue());
        }

        return result;
    }

    //标准数据标准差S
    public List<Double> getStandardsS(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        List<Double> average = getStandardsAverage(datalist);

        for (int i = 1; i <= average.size(); i++) {
            List<Double> values = new ArrayList<>();

            // 收集每个 SpcXrchartEntity 对象对应 index 的值
            for (SpcXrchartStandardsEntity entity : datalist) {
                Float value = entity.getData(i);
                if (value != null) {
                    values.add(value.doubleValue()); // 直接使用 doubleValue()
                }
            }

            double averageValue = average.get(i - 1);
            // 计算方差
            double variance = values.size() > 1 ? values.stream()
                    .mapToDouble(value -> Math.pow(value - averageValue, 2))
                    .sum() / (values.size() - 1) : 0.0;

            double stdDev = Math.sqrt(variance);
            // 将标准差结果保留2位小数
            BigDecimal roundedStdDev = new BigDecimal(stdDev).setScale(2, RoundingMode.HALF_UP);
            result.add(roundedStdDev.doubleValue());
        }

        return result;
    }

    //数据中位数Me
    public List<Double> getMe(List<SpcXrchartEntity> datalist){
        List<Double> result = new ArrayList<>();

        for (int col = 1; col <= 31; col++) {
            List<Double> values = new ArrayList<>();

            // 收集每个 SpcXrchartEntity 对象对应 index 的值
            for (SpcXrchartEntity entity : datalist) {
                Float value = entity.getData(col);
                if (value != null) {
                    values.add(value.doubleValue()); // 直接使用 doubleValue()
                }
            }

            if (!values.isEmpty()) {
                // 排序
                Collections.sort(values);
                int size = values.size();

                // 判断列表长度是奇数还是偶数
                if (size % 2 == 1) {
                    // 奇数，返回中间的值
                    result.add(round(values.get(size / 2)));
                } else {
                    // 偶数，返回中间两个值的平均
                    double median = (values.get(size / 2 - 1) + values.get(size / 2)) / 2;
                    result.add(round(median));
                }
            }
        }

        return result;
    }

    //标准数据中位数Me
    public List<Double> getStandardsMe(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        for (int col = 1; col <= 31; col++) {
            List<Double> values = new ArrayList<>();

            // 收集每个 SpcXrchartEntity 对象对应 index 的值
            for (SpcXrchartStandardsEntity entity : datalist) {
                Float value = entity.getData(col);
                if (value != null) {
                    values.add(value.doubleValue()); // 直接使用 doubleValue()
                }
            }

            if (!values.isEmpty()) {
                // 排序
                Collections.sort(values);
                int size = values.size();

                // 判断列表长度是奇数还是偶数
                if (size % 2 == 1) {
                    // 奇数，返回中间的值
                    result.add(round(values.get(size / 2)));
                } else {
                    // 偶数，返回中间两个值的平均
                    double median = (values.get(size / 2 - 1) + values.get(size / 2)) / 2;
                    result.add(round(median));
                }
            }
        }

        return result;
    }

    //数据单值I (计算方法和求均值Xbar相同)
    public List<Double> getI(List<SpcXrchartEntity> datalist){
        List<Double> sum = getSum(datalist);
        int number = datalist.size();

        List<Double> averageList = new ArrayList<>();
        for (Double value : sum){
            BigDecimal bigDecimalValue = BigDecimal.valueOf(value); // 将Double转换为BigDecimal
            BigDecimal dividedValue = bigDecimalValue.divide(BigDecimal.valueOf(number)); // 除以5
            averageList.add(dividedValue.doubleValue()); // 将结果转换回Double并添加到新列表中
        }

        return averageList;
    }

    //标准数据单值I
    public List<Double> getStandardsI(List<SpcXrchartStandardsEntity> datalist){
        List<Double> sum = getStandardsSum(datalist);
        int number = datalist.size();

        List<Double> averageList = new ArrayList<>();
        for (Double value : sum){
            BigDecimal bigDecimalValue = BigDecimal.valueOf(value); // 将Double转换为BigDecimal
            BigDecimal dividedValue = bigDecimalValue.divide(BigDecimal.valueOf(number)); // 除以5
            averageList.add(dividedValue.doubleValue()); // 将结果转换回Double并添加到新列表中
        }

        return averageList;
    }

    //数据移动极差MR
    public List<Double> getMR(List<SpcXrchartEntity> datalist){
        List<Double> result = new ArrayList<>();

        for (int col = 1; col <= 31; col++) {
            List<Double> values = new ArrayList<>();

            // 收集每个 SpcXrchartEntity 对象对应 index 的值
            for (SpcXrchartEntity entity : datalist) {
                Float value = entity.getData(col);
                if (value != null) {
                    values.add(value.doubleValue()); // 直接使用 doubleValue()
                }
            }
            if (!values.isEmpty()){
                BigDecimal sum = new BigDecimal(0);
                for (int i = 1; i < values.size(); i++) {
                    BigDecimal range = BigDecimal.valueOf(Math.abs(values.get(i) - values.get(i - 1)));
                    sum = sum.add(range);
                }
                result.add(sum.divide(BigDecimal.valueOf(values.size()-1)).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }

        }

        return result;
    }

    //标准数据移动极差MR
    public List<Double> getStandardsMR(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        for (int col = 1; col <= 31; col++) {
            List<Double> values = new ArrayList<>();

            // 收集每个 SpcXrchartEntity 对象对应 index 的值
            for (SpcXrchartStandardsEntity entity : datalist) {
                Float value = entity.getData(col);
                if (value != null) {
                    values.add(value.doubleValue()); // 直接使用 doubleValue()
                }
            }
            if (!values.isEmpty()){
                BigDecimal sum = new BigDecimal(0);
                for (int i = 1; i < values.size(); i++) {
                    BigDecimal range = BigDecimal.valueOf(Math.abs(values.get(i) - values.get(i - 1)));
                    sum = sum.add(range);
                }
                result.add(sum.divide(BigDecimal.valueOf(values.size()-1)).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }

        }

        return result;
    }

    //Xbar-R图 均值Xbar上限UCL、中心限CL、下限LCL
    public List<Double> getXbar_R_Xbar_Limit(List<SpcXrchartStandardsEntity> datalist){

        //输出Xbar-R图 均值Xbar的上限、中心限、下限
        List<Double> result = new ArrayList<>();

        //计算Xbar的均值、极差R的均值

        List<Double> average = getStandardsAverage(datalist);
        List<Double> R = getStandardsR(datalist);

        double average_bar = calculateAverage(average);
        double R_bar = calculateAverage(R);

        //计算三限
        BigDecimal average_bar_bigDecimal = new BigDecimal(average_bar);
        BigDecimal R_bar_bigDecimal = new BigDecimal(R_bar);

        //cl
        BigDecimal cl = average_bar_bigDecimal.setScale(2, RoundingMode.HALF_UP);

        //读取常数
        Double constant_A2 = spcXrchartDao.getConstantForXbarR_A2(datalist.size());

        if (constant_A2 != null){
            BigDecimal constant_A2_bigDecimal = new BigDecimal(constant_A2);

            //ucl
            BigDecimal ucl = average_bar_bigDecimal.add(constant_A2_bigDecimal.multiply(R_bar_bigDecimal)).setScale(2, RoundingMode.HALF_UP);

            //lcl
            BigDecimal lcl = average_bar_bigDecimal.subtract(constant_A2_bigDecimal.multiply(R_bar_bigDecimal)).setScale(2, RoundingMode.HALF_UP);
            result.add(ucl.doubleValue());
            result.add(cl.doubleValue());
            result.add(lcl.doubleValue());
        }else {
            result.add(0.0);
            result.add(cl.doubleValue());
            result.add(0.0);
        }

        return result;
    }

    //Xbar-R图 极差R上限UCL、中心限CL、下限LCL
    public List<Double> getXbar_R_R_Limit(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        //计算极差R的均值
        List<Double> R = getStandardsR(datalist);

        double R_bar = calculateAverage(R);

        //计算三限
        BigDecimal R_bar_bigDecimal = new BigDecimal(R_bar);

        //读取常数
        Double constant_D3 = spcXrchartDao.getConstantForXbarR_D3(datalist.size());
        Double constant_D4 = spcXrchartDao.getConstantForXbarR_D4(datalist.size());

        //ucl
        if(constant_D4 != null){
            BigDecimal constant_D4_bigDecimal = new BigDecimal(constant_D4);
            BigDecimal ucl = constant_D4_bigDecimal.multiply(R_bar_bigDecimal).setScale(2, RoundingMode.HALF_UP);
            result.add(ucl.doubleValue());
        }else {
            result.add(0.0);
        }

        //cl
        BigDecimal cl = R_bar_bigDecimal.setScale(2, RoundingMode.HALF_UP);
        result.add(cl.doubleValue());

        //lcl
        if(constant_D3 != null){
            BigDecimal constant_D3_bigDecimal = new BigDecimal(constant_D3);
            BigDecimal lcl = constant_D3_bigDecimal.multiply(R_bar_bigDecimal).setScale(2, RoundingMode.HALF_UP);
            result.add(lcl.doubleValue());
        }else {
            result.add(0.0);
        }

        return result;
    }

    //Xbar-S图 均值Xbar上限UCL、中心限CL、下限LCL
    public List<Double> getXbar_S_Xbar_Limit(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        //计算Xbar的均值、标准差S的均值

        List<Double> average = getStandardsAverage(datalist);
        List<Double> S = getStandardsS(datalist);

        double average_bar = calculateAverage(average);
        double S_bar = calculateAverage(S);

        //计算三限
        BigDecimal average_bar_bigDecimal = new BigDecimal(average_bar);
        BigDecimal S_bar_bigDecimal = new BigDecimal(S_bar);

        //cl
        BigDecimal cl = average_bar_bigDecimal.setScale(2, RoundingMode.HALF_UP);

        //读取常数
        Double constant_A3 = spcXrchartDao.getConstantForXbarS_A3(datalist.size());
        if (constant_A3 != null){
            BigDecimal constant_A3_bigDecimal = new BigDecimal(constant_A3);

            //ucl
            BigDecimal ucl = average_bar_bigDecimal.add(constant_A3_bigDecimal.multiply(S_bar_bigDecimal)).setScale(2, RoundingMode.HALF_UP);

            //lcl
            BigDecimal lcl = average_bar_bigDecimal.subtract(constant_A3_bigDecimal.multiply(S_bar_bigDecimal)).setScale(2, RoundingMode.HALF_UP);
            result.add(ucl.doubleValue());
            result.add(cl.doubleValue());
            result.add(lcl.doubleValue());
        }else {
            result.add(0.0);
            result.add(cl.doubleValue());
            result.add(0.0);
        }


        return result;
    }

    //Xbar-S图 标准差S上限UCL、中心限CL、下限LCL
    public List<Double> getXbar_S_S_Limit(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        //计算Xbar的均值、标准差S的均值
        List<Double> S = getStandardsS(datalist);

        double S_bar = calculateAverage(S);

        //计算三限
        BigDecimal S_bar_bigDecimal = new BigDecimal(S_bar);

        //读取常数
        Double constant_B3 = spcXrchartDao.getConstantForXbarS_B3(datalist.size());
        Double constant_B4 = spcXrchartDao.getConstantForXbarS_B4(datalist.size());

        //ucl
        if(constant_B4 != null){
            BigDecimal constant_B4_bigDecimal = new BigDecimal(constant_B4);
            BigDecimal ucl = constant_B4_bigDecimal.multiply(S_bar_bigDecimal).setScale(2, RoundingMode.HALF_UP);
            result.add(ucl.doubleValue());
        }else {
            result.add(0.0);
        }

        //cl
        BigDecimal cl = S_bar_bigDecimal.setScale(2, RoundingMode.HALF_UP);
        result.add(cl.doubleValue());

        //lcl
        if(constant_B3 != null){
            BigDecimal constant_B3_bigDecimal = new BigDecimal(constant_B3);
            BigDecimal lcl = constant_B3_bigDecimal.multiply(S_bar_bigDecimal).setScale(2, RoundingMode.HALF_UP);
            result.add(lcl.doubleValue());
        }else {
            result.add(0.0);
        }

        return result;
    }

    //Me-R图 中位数Me上限UCL、中心限CL、下限LCL
    public List<Double> getMe_R_Me_Limit(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        List<Double> Me = getStandardsMe(datalist);
        List<Double> R = getStandardsR(datalist);

        //计算Me均值、R均值
        double Me_bar = calculateAverage(Me);
        double R_bar = calculateAverage(R);

        //计算三限
        BigDecimal Me_bar_bigDecimal = new BigDecimal(Me_bar);
        BigDecimal R_bar_bigDecimal = new BigDecimal(R_bar);

        //读取常数
        //todo 常数A4没有给出
        Double constant_A4 = spcXrchartDao.getConstantForXbarS_A4(datalist.size());


        //cl
        BigDecimal cl = Me_bar_bigDecimal.setScale(2, RoundingMode.HALF_UP);

        if(constant_A4 != null){
            BigDecimal constant_A4_bigDecimal = new BigDecimal(constant_A4);

            //ucl
            BigDecimal ucl = Me_bar_bigDecimal.add(constant_A4_bigDecimal.multiply(R_bar_bigDecimal)).setScale(2, RoundingMode.HALF_UP);

            //lcl
            BigDecimal lcl = Me_bar_bigDecimal.subtract(constant_A4_bigDecimal.multiply(R_bar_bigDecimal)).setScale(2, RoundingMode.HALF_UP);

            result.add(ucl.doubleValue());
            result.add(cl.doubleValue());
            result.add(lcl.doubleValue());

        }else {
            result.add(0.0);
            result.add(cl.doubleValue());
            result.add(0.0);
        }

        return result;
    }

    //Me-R图 极差R上限UCL、中心限CL、下限LCL
    public List<Double> getMe_R_R_Limit(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        //计算Me均值、R均值
        List<Double> R = getStandardsR(datalist);

        double R_bar = calculateAverage(R);

        //计算三限
        BigDecimal R_bar_bigDecimal = new BigDecimal(R_bar);

        //读取常数
        Double constant_D3 = spcXrchartDao.getConstantForMeR_D3(datalist.size());
        Double constant_D4 = spcXrchartDao.getConstantForMeR_D4(datalist.size());

        //ucl
        if(constant_D4 != null){
            BigDecimal constant_D4_bigDecimal = new BigDecimal(constant_D4);
            BigDecimal ucl = constant_D4_bigDecimal.multiply(R_bar_bigDecimal).setScale(2, RoundingMode.HALF_UP);
            result.add(ucl.doubleValue());
        }else {
            result.add(0.0);
        }

        //cl
        BigDecimal cl = R_bar_bigDecimal.setScale(2, RoundingMode.HALF_UP);
        result.add(cl.doubleValue());

        //lcl
        if(constant_D3 != null){
            BigDecimal constant_D3_bigDecimal = new BigDecimal(constant_D3);
            BigDecimal lcl = constant_D3_bigDecimal.multiply(R_bar_bigDecimal).setScale(2, RoundingMode.HALF_UP);
            result.add(lcl.doubleValue());
        }else {
            result.add(0.0);
        }

        return result;
    }

    //I-MR图 单值I上限UCL、中心限CL、下限LCL
    public List<Double> getI_MR_I_Limit(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        List<Double> I = getStandardsI(datalist);
        List<Double> MR = getStandardsMR(datalist);

        //计算I均值、MR均值
        double I_bar = calculateAverage(I);
        double MR_bar = calculateAverage(MR);

        //计算三限
        BigDecimal I_bar_bigDecimal = new BigDecimal(I_bar);
        BigDecimal MR_bar_bigDecimal = new BigDecimal(MR_bar);

        //读取常数
        Double constant_E2 = spcXrchartDao.getConstantForIMR_E2(datalist.size());

        //cl
        BigDecimal cl = I_bar_bigDecimal.setScale(2, RoundingMode.HALF_UP);

        if(constant_E2 != null){
            BigDecimal constant_E2_bigDecimal = new BigDecimal(constant_E2);

            //ucl
            BigDecimal ucl = I_bar_bigDecimal.add(constant_E2_bigDecimal.multiply(MR_bar_bigDecimal)).setScale(2, RoundingMode.HALF_UP);

            //lcl
            BigDecimal lcl = I_bar_bigDecimal.subtract(constant_E2_bigDecimal.multiply(MR_bar_bigDecimal)).setScale(2, RoundingMode.HALF_UP);

            result.add(ucl.doubleValue());
            result.add(cl.doubleValue());
            result.add(lcl.doubleValue());

        }else {
            result.add(0.0);
            result.add(cl.doubleValue());
            result.add(0.0);
        }

        return result;
    }

    //I-MR图 移动极差MR上限UCL、中心限CL、下限LCL
    public List<Double> getI_MR_MR_Limit(List<SpcXrchartStandardsEntity> datalist){
        List<Double> result = new ArrayList<>();

        //计算Me均值、R均值
        List<Double> MR = getStandardsMR(datalist);

        double MR_bar = calculateAverage(MR);

        //计算三限
        BigDecimal MR_bar_bigDecimal = new BigDecimal(MR_bar);

        //读取常数
        Double constant_D3 = spcXrchartDao.getConstantForIMR_D3(datalist.size());
        Double constant_D4 = spcXrchartDao.getConstantForIMR_D4(datalist.size());

        //ucl
        if(constant_D4 != null){
            BigDecimal constant_D4_bigDecimal = new BigDecimal(constant_D4);
            BigDecimal ucl = constant_D4_bigDecimal.multiply(MR_bar_bigDecimal).setScale(2, RoundingMode.HALF_UP);
            result.add(ucl.doubleValue());
        }else {
            result.add(0.0);
        }

        //cl
        BigDecimal cl = MR_bar_bigDecimal.setScale(2, RoundingMode.HALF_UP);
        result.add(cl.doubleValue());

        //lcl
        if(constant_D3 != null){
            BigDecimal constant_D3_bigDecimal = new BigDecimal(constant_D3);
            BigDecimal lcl = constant_D3_bigDecimal.multiply(MR_bar_bigDecimal).setScale(2, RoundingMode.HALF_UP);
            result.add(lcl.doubleValue());
        }else {
            result.add(0.0);
        }

        return result;
    }

    //计算链表均值
    public double calculateAverage(List<Double> datalist) {
        if (datalist == null || datalist.isEmpty()) {
            throw new IllegalArgumentException("The list cannot be null or empty");
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (Double value : datalist) {
            if (value != null) { // 确保value不是null
                sum = sum.add(BigDecimal.valueOf(value));
            }
        }

        // 计算平均值并设置为保留两位小数
        BigDecimal count = BigDecimal.valueOf(datalist.size());
        BigDecimal averageValue = sum.divide(count, 2, RoundingMode.HALF_UP); // 保留两位小数

        return averageValue.doubleValue(); // 转换为double返回
    }

    private static Double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP); // 设置精度为2位小数并进行四舍五入
        return bd.doubleValue();
    }

}