package io.renren.modules.spc.service.impl;

import io.renren.modules.spc.dao.SpcXrchartDao;
import io.renren.modules.spc.entity.SpcXrchartEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.spc.dao.SpcPtdDao;
import io.renren.modules.spc.entity.SpcPtdEntity;
import io.renren.modules.spc.service.SpcPtdService;
import org.w3c.dom.ls.LSException;

import javax.annotation.Resource;


@Service("spcPtdService")
public class SpcPtdServiceImpl extends ServiceImpl<SpcPtdDao, SpcPtdEntity> implements SpcPtdService {

    @Resource
    private SpcPtdDao spcPtdDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpcPtdEntity> page = this.page(
                new Query<SpcPtdEntity>().getPage(params),
                new QueryWrapper<SpcPtdEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void importData(List<SpcPtdEntity> datalist){
        spcPtdDao.deleteDataByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spcPtdDao.batchInsertSpcPtdcharts(datalist);
    }

    @Override
    public List<List<Double>> getPTDChart(){

        List<List<Double>> result = new ArrayList<>();

        //获取数据
        List<SpcPtdEntity> datalist = getData();
        List<Double> acceptance_region = getAcceptanceRegion(datalist);
        List<Double> frequency = getFrequency(datalist);

        List<Double> PTD = getPTD(datalist);

        //返回X轴数据：接受区域
        result.add(acceptance_region);

        //返回频率
        result.add(frequency);

        //返回正态分布值
        result.add(PTD);

        return result;
    }

    //获取数据
    public List<SpcPtdEntity> getData(){
        return spcPtdDao.getSpcPtdEntityByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    //拆解数据，获取工时、接受区域、频率
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

    //频率
    public List<Double> getFrequency(List<SpcPtdEntity> datalist){
        List<Double> frequency = new ArrayList<>();
        for(SpcPtdEntity spcPtdEntity : datalist){
            if(spcPtdEntity.getFrequency() != null){
                frequency.add(Double.valueOf(spcPtdEntity.getFrequency()));
            }
            else {
                frequency.add(0.0);
            }
        }
        return frequency;
    }

    //计算正态分布值
    public List<Double> getPTD(List<SpcPtdEntity>datalist){
        List<Double> result = new ArrayList<>();

        //先获取工时，计算均值、标准差
        List<Double> work_time = getWorkTime(datalist);

        double average = calculateAverage(work_time);
        double stdev = calculateStdev(work_time);

        //获取接收区域，计算正态分布值
        List<Double> acceptance_region = getAcceptanceRegion(datalist);
        for(Double region : acceptance_region){
            double num = calculateNormDist(region, average, stdev, false);
            result.add(BigDecimal.valueOf(num).setScale(7, RoundingMode.HALF_UP).doubleValue());
        }

        return result;
    }

    //计算方法部分
    //计算标准差
    public double calculateStdev(List<Double> workTime) {
        if (workTime == null || workTime.size() < 2) {
            throw new IllegalArgumentException("List must contain at least two values.");
        }

        double mean = calculateAverage(workTime);
        double sumOfSquaredDiffs = 0.0;

        for (Double value : workTime) {
            sumOfSquaredDiffs += Math.pow(value - mean, 2);
        }

        return Math.sqrt(sumOfSquaredDiffs / (workTime.size() - 1)); // 样本标准差
    }

    //计算平均值
    public double calculateAverage(List<Double> workTime) {
        double sum = 0.0;
        for (Double value : workTime) {
            sum += value;
        }
        return sum / workTime.size();
    }

    // 实现 NORM.DIST 函数  参数分别是：x：需要计算的接收区域，mean；均值，standardDev：标准差，cumulative：一个布尔值，决定函数返回 PDF 还是 CDF
    public double calculateNormDist(double x, double mean, double standardDev, boolean cumulative) {
        if (standardDev <= 0) {
            throw new IllegalArgumentException("Standard deviation must be greater than 0.");
        }

        double exponent = Math.exp(-Math.pow(x - mean, 2) / (2 * Math.pow(standardDev, 2)));
        double coefficient = 1 / (standardDev * Math.sqrt(2 * Math.PI));

        if (cumulative) {
            // 计算累积分布函数
            return 0.5 * (1 + erf((x - mean) / (standardDev * Math.sqrt(2))));
        } else {
            // 计算概率密度函数
            return coefficient * exponent;
        }
    }

    // 计算误差函数 erf
    private double erf(double z) {
        // 使用近似公式计算误差函数
        double sign = (z >= 0) ? 1 : -1;
        z = Math.abs(z);
        double a1 =  0.254829592;
        double a2 = -0.284496736;
        double a3 =  1.421413741;
        double a4 = -1.453152027;
        double a5 =  1.061405429;
        double p  =  0.3275911;

        double t = 1.0 / (1.0 + p * z);
        double y = 1.0 - ((((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t) * Math.exp(-z * z);

        return sign * y; // 返回带符号的结果
    }
}