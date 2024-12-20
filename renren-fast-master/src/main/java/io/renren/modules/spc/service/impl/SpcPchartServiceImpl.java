package io.renren.modules.spc.service.impl;

import io.renren.modules.spc.dao.SpcPchartStandardsDao;
import io.renren.modules.spc.entity.SpcPchartStandardsEntity;
import io.renren.modules.spc.entity.SpcXrchartEntity;
import io.swagger.models.auth.In;
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

import io.renren.modules.spc.dao.SpcPchartDao;
import io.renren.modules.spc.entity.SpcPchartEntity;
import io.renren.modules.spc.service.SpcPchartService;

import javax.annotation.Resource;


@Service("spcPchartService")
public class SpcPchartServiceImpl extends ServiceImpl<SpcPchartDao, SpcPchartEntity> implements SpcPchartService {


    @Resource
    private SpcPchartDao spcPchartDao;

    @Resource
    private SpcPchartStandardsDao spcPchartStandardsDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpcPchartEntity> page = this.page(
                new Query<SpcPchartEntity>().getPage(params),
                new QueryWrapper<SpcPchartEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void importData(List<SpcPchartEntity> datalist){
        spcPchartDao.deleteDataByTableName(datalist.get(0).getAlternateFields1());
        spcPchartDao.batchInsertSpcPcharts(datalist);
    }

    @Override
    public List<String> getTableName(){
        return spcPchartDao.getTableName();
    }

    @Override
    public List<List<Double>> getPChart(String tableName){
        List<List<Double>> result = new ArrayList<>();

        //获取数据
        List<SpcPchartEntity> datalist = getDataByTableName(tableName);
        List<SpcPchartStandardsEntity> datalist_standards = getStandardsData();

        //转换标准数据和普通数据的类型
        List<SpcPchartEntity> datalist_standards_convert = new ArrayList<>();
        for(SpcPchartStandardsEntity entity : datalist_standards){
            datalist_standards_convert.add(convert2SpcPchartEntity(entity));
        }

        List<Double> P = getP(datalist);
        List<List<Double>> Limit = getP_Limit(datalist_standards_convert);

        result.add(P);
        result.add(Limit.get(0));
        result.add(Limit.get(1));
        result.add(Limit.get(2));
        return result;
    }

    @Override
    public List<List<Double>> getNPChart(String tableName){
        List<List<Double>> result = new ArrayList<>();

        //获取数据
        List<SpcPchartEntity> datalist = getDataByTableName(tableName);
        List<SpcPchartStandardsEntity> datalist_standards = getStandardsData();

        //转换标准数据和普通数据的类型
        List<SpcPchartEntity> datalist_standards_convert = new ArrayList<>();
        for(SpcPchartStandardsEntity entity : datalist_standards){
            datalist_standards_convert.add(convert2SpcPchartEntity(entity));
        }

        //计算np
        List<Double> np = getNP(datalist);

        List<List<Double>> Limit = getNP_Limit(datalist_standards_convert);

        result.add(np);
        result.add(Limit.get(0));
        result.add(Limit.get(1));
        result.add(Limit.get(2));
        return result;
    }

    @Override
    public List<List<Double>> getUChart(String tableName){
        List<List<Double>> result = new ArrayList<>();

        //获取数据
        List<SpcPchartEntity> datalist = getDataByTableName(tableName);
        List<SpcPchartStandardsEntity> datalist_standards = getStandardsData();

        //转换标准数据和普通数据的类型
        List<SpcPchartEntity> datalist_standards_convert = new ArrayList<>();
        for(SpcPchartStandardsEntity entity : datalist_standards){
            datalist_standards_convert.add(convert2SpcPchartEntity(entity));
        }

        //计算np
        List<Double> u = getU(datalist);

        List<List<Double>> Limit = getU_Limit(datalist_standards_convert);

        result.add(u);
        result.add(Limit.get(0));
        result.add(Limit.get(1));
        result.add(Limit.get(2));
        return result;

    }

    @Override
    public List<List<Double>> getCChart(String tableName){
        List<List<Double>> result = new ArrayList<>();

        //获取数据
        List<SpcPchartEntity> datalist = getDataByTableName(tableName);
        List<SpcPchartStandardsEntity> datalist_standards = getStandardsData();

        //转换标准数据和普通数据的类型
        List<SpcPchartEntity> datalist_standards_convert = new ArrayList<>();
        for(SpcPchartStandardsEntity entity : datalist_standards){
            datalist_standards_convert.add(convert2SpcPchartEntity(entity));
        }

        //计算np
        List<Double> c = getC(datalist);

        List<List<Double>> Limit = getC_Limit(datalist_standards_convert);

        result.add(c);
        result.add(Limit.get(0));
        result.add(Limit.get(1));
        result.add(Limit.get(2));
        return result;

    }

    //策略步骤1：获取数据步骤
    public List<SpcPchartEntity> getData(){
        return spcPchartDao.getSpcPchartEntityByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    //根据表名获取数据
    public List<SpcPchartEntity> getDataByTableName(String tableName){
        return spcPchartDao.getSpcPchartEntityByTableName(tableName);
    }

    //获取标准数据
    public List<SpcPchartStandardsEntity> getStandardsData(){
        return spcPchartStandardsDao.getSpcPchartStandardsEntityByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    //根据表名获取数据
    public List<SpcPchartStandardsEntity> getStandardsDataByTableName(String tableName){
        return spcPchartStandardsDao.getSpcPchartStandardsEntityByTableName(tableName);
    }

    //处理数据，分辨抽检数
    public List<Integer> getSampling_Tests_Number(List<SpcPchartEntity> datalist){

        List<Integer> Sampling_Tests_Number = new ArrayList<>();
        SpcPchartEntity data = new SpcPchartEntity();
        for(SpcPchartEntity spcPchartEntity : datalist){
            if(spcPchartEntity.getDataContent().equals("抽检数")){
                data = spcPchartEntity;
            }
        }

        for (int i = 1; i <= 31; i++){
            Integer num = data.getDate(i);
            if(num != null){
                Sampling_Tests_Number.add(num);
            }
        }

        return Sampling_Tests_Number;
    }

    //处理数据，分辨不良数
    public List<Integer> getDefects_Number(List<SpcPchartEntity> datalist){
        List<Integer> Defects_Number = new ArrayList<>();
        SpcPchartEntity data = new SpcPchartEntity();
        for(SpcPchartEntity spcPchartEntity : datalist){
            if(spcPchartEntity.getDataContent().equals("不良数")){
                data = spcPchartEntity;
            }
        }

        for (int i = 1; i <= 31; i++){
            Integer num = data.getDate(i);
            if(num != null){
                Defects_Number.add(num);
            }
        }

        return Defects_Number;
    }

    //策略步骤2：根据需要生成的图表类型，使用不同方法计算
    //计算不良率p
    public List<Double> getP(List<SpcPchartEntity> datalist){
        List<Double> result = new ArrayList<>();

        //抽检数
        List<Integer> Sampling_Tests_Number = getSampling_Tests_Number(datalist);
        //不良数
        List<Integer> Defects_Number = getDefects_Number(datalist);

        for (int i = 0; i < Sampling_Tests_Number.size(); i++){
            BigDecimal sampling = new BigDecimal(Sampling_Tests_Number.get(i));
            BigDecimal defects = new BigDecimal(Defects_Number.get(i));

            BigDecimal p =  defects.divide(sampling,4, RoundingMode.HALF_UP);

            result.add(p.doubleValue());
        }

        return result;
    }

    //计算不合格数np
    public List<Double> getNP(List<SpcPchartEntity> datalist){

        List<Double> result = new ArrayList<>();

        List<Integer> defects = getDefects_Number(datalist);
        //转换不良数的数据类型
        for (Integer defect : defects){
            result.add(Double.valueOf(defect));
        }
        return result;

    }

    //计算单位缺陷数u(计算方法与p相同)
    public List<Double> getU(List<SpcPchartEntity> datalist){
        List<Double> result = new ArrayList<>();

        //抽检数
        List<Integer> Sampling_Tests_Number = getSampling_Tests_Number(datalist);
        //不良数
        List<Integer> Defects_Number = getDefects_Number(datalist);

        for (int i = 0; i < Sampling_Tests_Number.size(); i++){
            BigDecimal sampling = new BigDecimal(Sampling_Tests_Number.get(i));
            BigDecimal defects = new BigDecimal(Defects_Number.get(i));

            BigDecimal p =  defects.divide(sampling,4, RoundingMode.HALF_UP);

            result.add(p.doubleValue());
        }

        return result;
    }

    //计算不合格数c(计算方法与np相同)
    public List<Double> getC(List<SpcPchartEntity> datalist){

        List<Double> result = new ArrayList<>();

        List<Integer> defects = getDefects_Number(datalist);
        //转换不良数的数据类型
        for (Integer defect : defects){
            result.add(Double.valueOf(defect));
        }
        return result;

    }

    //计算P图三限
    public List<List<Double>> getP_Limit(List<SpcPchartEntity> datalist){
        List<List<Double>> result = new ArrayList<>();

        //抽检数
        List<Integer> Sampling_Tests_Number = getSampling_Tests_Number(datalist);


        //p-bar
        BigDecimal P_bar = calculateP_bar(datalist);

        //ucl
        List<Double> ucl = new ArrayList<>();
        for(Integer sampling : Sampling_Tests_Number){
            ucl.add(PcalculateUCL(P_bar, sampling).doubleValue());
        }


        //cl
        List<Double> cl = new ArrayList<>();
        for(int i = 0; i < Sampling_Tests_Number.size(); i++){
            cl.add(P_bar.doubleValue());
        }

        //lcl
        List<Double> lcl = new ArrayList<>();
        for(Integer sampling : Sampling_Tests_Number){
            lcl.add(PcalculateLCL(P_bar, sampling).doubleValue());
        }

        result.add(ucl);
        result.add(cl);
        result.add(lcl);
        return result;
    }

    //计算NP图三限
    public List<List<Double>> getNP_Limit(List<SpcPchartEntity> datalist){
        List<List<Double>> result = new ArrayList<>();
        //抽检数
        List<Integer> Sampling_Tests_Number = getSampling_Tests_Number(datalist);

        //p-bar
        BigDecimal P_bar = calculateP_bar(datalist);

        //ucl
        List<Double> ucl = new ArrayList<>();
        for(Integer sampling : Sampling_Tests_Number){
            ucl.add(NPcalculateUCL(P_bar, sampling).doubleValue());
        }

        //cl
        List<Double> cl = new ArrayList<>();
        for(Integer sampling : Sampling_Tests_Number){
            cl.add(P_bar.multiply(BigDecimal.valueOf(sampling)).setScale(2,RoundingMode.HALF_UP).doubleValue());
        }

        //ucl
        List<Double> lcl = new ArrayList<>();
        for(Integer sampling : Sampling_Tests_Number){
            lcl.add(NPcalculateLCL(P_bar, sampling).doubleValue());
        }

        result.add(ucl);
        result.add(cl);
        result.add(lcl);

        return result;
    }

    //计算U图三限
    public List<List<Double>> getU_Limit(List<SpcPchartEntity> datalist){
        List<List<Double>> result = new ArrayList<>();

        //抽检数
        List<Integer> Sampling_Tests_Number = getSampling_Tests_Number(datalist);

        //u-bar 计算方法与p-bar相同
        BigDecimal U_bar = calculateP_bar(datalist);

        //ucl
        List<Double> ucl = new ArrayList<>();
        for(Integer sampling : Sampling_Tests_Number){
            ucl.add(UcalculateUCL(U_bar, sampling).doubleValue());
        }

        //cl
        List<Double> cl = new ArrayList<>();
        for(Integer sampling : Sampling_Tests_Number){
            cl.add(U_bar.doubleValue());
        }

        //lcl
        List<Double> lcl = new ArrayList<>();
        for(Integer sampling : Sampling_Tests_Number){
            lcl.add(UcalculateLCL(U_bar, sampling).doubleValue());
        }

        result.add(ucl);
        result.add(cl);
        result.add(lcl);

        return result;
    }

    //计算C图三限
    public List<List<Double>> getC_Limit(List<SpcPchartEntity> datalist){
        List<List<Double>> result = new ArrayList<>();
        //抽检数
        List<Integer> Sampling_Tests_Number = getSampling_Tests_Number(datalist);

        //计算c_bar
        BigDecimal c_bar = calculateC_bar(datalist);

        //ucl
        BigDecimal UCL = CcalculateUCL(c_bar);

        //lcl
        BigDecimal LCL = CcalculateLCL(c_bar);

        List<Double> ucl = new ArrayList<>();
        List<Double> cl = new ArrayList<>();
        List<Double> lcl = new ArrayList<>();

        for(Integer sampling : Sampling_Tests_Number){
            ucl.add(UCL.doubleValue());
            cl.add(c_bar.doubleValue());
            lcl.add(LCL.doubleValue());
        }

        result.add(ucl);
        result.add(cl);
        result.add(lcl);

        return result;
    }

    /**
     *
     * 以下为通用方法
     * */

    //计算平均不合格率p-bar
    public BigDecimal calculateP_bar(List<SpcPchartEntity> datalist){
        //抽检数
        List<Integer> Sampling_Tests_Number = getSampling_Tests_Number(datalist);
        //不良数
        List<Integer> Defects_Number = getDefects_Number(datalist);

        Integer sum_Sampling_Tests_Number = getSum(Sampling_Tests_Number);
        Integer sum_Defects_Number = getSum(Defects_Number);

        return BigDecimal.valueOf(sum_Defects_Number).divide(BigDecimal.valueOf(sum_Sampling_Tests_Number),4, RoundingMode.HALF_UP);
    }

    //计算平c-bar
    public BigDecimal calculateC_bar(List<SpcPchartEntity> datalist){
        //不良数
        List<Integer> Defects_Number = getDefects_Number(datalist);
        //求和
        Integer sum_Defects_Number = getSum(Defects_Number);

        return BigDecimal.valueOf(sum_Defects_Number).divide(BigDecimal.valueOf(Defects_Number.size()),2, RoundingMode.HALF_UP);
    }

    //p图ucl，参数为p：平均不合格率   samplingTestsNumber：当前批次的抽检数
    public BigDecimal PcalculateUCL(BigDecimal p, int samplingTestsNumber) {

        BigDecimal sqrtResult = (p.multiply(BigDecimal.valueOf(1).subtract(p))
                .divide(BigDecimal.valueOf(samplingTestsNumber), RoundingMode.HALF_UP));
        double sqrt = sqrtResult.doubleValue();
        BigDecimal ucl = p.add(BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(Math.sqrt(sqrt))));
        return ucl.setScale(4, RoundingMode.HALF_UP);
    }
    //p图lcl，参数为p：平均不合格率   samplingTestsNumber：当前批次的抽检数
    public BigDecimal PcalculateLCL(BigDecimal p, int samplingTestsNumber) {

        BigDecimal sqrtResult = (p.multiply(BigDecimal.valueOf(1).subtract(p))
                .divide(BigDecimal.valueOf(samplingTestsNumber), RoundingMode.HALF_UP));
        double sqrt = sqrtResult.doubleValue();
        BigDecimal ucl = p.subtract(BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(Math.sqrt(sqrt))));
        return ucl.setScale(4, RoundingMode.HALF_UP);
    }

    //np图ucl，参数为p：平均不合格率  samplingTestsNumber：当前批次的抽检数
    public BigDecimal NPcalculateUCL(BigDecimal p, int samplingTestsNumber){
        BigDecimal sqrtResult = (p.multiply(BigDecimal.valueOf(1).subtract(p)).multiply(BigDecimal.valueOf(samplingTestsNumber)));
        double sqrt = sqrtResult.doubleValue();
        BigDecimal np = p.multiply(BigDecimal.valueOf(samplingTestsNumber));
        return np.add(BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(Math.sqrt(sqrt)))).setScale(2,RoundingMode.HALF_UP);
    }
    //np图lcl，参数为p：平均不合格率  samplingTestsNumber：当前批次的抽检数
    public BigDecimal NPcalculateLCL(BigDecimal p, int samplingTestsNumber){
        BigDecimal sqrtResult = (p.multiply(BigDecimal.valueOf(1).subtract(p)).multiply(BigDecimal.valueOf(samplingTestsNumber)));
        double sqrt = sqrtResult.doubleValue();
        BigDecimal np = p.multiply(BigDecimal.valueOf(samplingTestsNumber));
        return np.subtract(BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(Math.sqrt(sqrt)))).setScale(2,RoundingMode.HALF_UP);
    }

    //u图ucl, 参数为u：平均不合格率  samplingTestsNumber：当前批次的抽检数
    public BigDecimal UcalculateUCL(BigDecimal u, int samplingTestsNumber){
        BigDecimal sqrtResult = u.divide(BigDecimal.valueOf(samplingTestsNumber), 6,RoundingMode.HALF_UP);
        double sqrt = sqrtResult.doubleValue();
        return u.add(BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(Math.sqrt(sqrt)))).setScale(4, RoundingMode.HALF_UP);
    }
    //u图lcl，参数为u：平均不合格率  samplingTestsNumber：当前批次的抽检数
    public BigDecimal UcalculateLCL(BigDecimal u, int samplingTestsNumber){
        BigDecimal sqrtResult = u.divide(BigDecimal.valueOf(samplingTestsNumber), 6,RoundingMode.HALF_UP);
        double sqrt = sqrtResult.doubleValue();
        return u.subtract(BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(Math.sqrt(sqrt)))).setScale(4, RoundingMode.HALF_UP);
    }

    //c图ucl, 参数为c：平均不合格数  samplingTestsNumber：当前批次的抽检数
    public BigDecimal CcalculateUCL(BigDecimal c){
        double sqrt = c.doubleValue();
        return c.add(BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(Math.sqrt(sqrt)))).setScale(2, RoundingMode.HALF_UP);
    }
    //c图lcl, 参数为c：平均不合格数  samplingTestsNumber：当前批次的抽检数
    public BigDecimal CcalculateLCL(BigDecimal c){
        double sqrt = c.doubleValue();
        return c.subtract(BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(Math.sqrt(sqrt)))).setScale(2, RoundingMode.HALF_UP);
    }


    //链表求和
    public Integer getSum(List<Integer> datalist){
        Integer sum = 0;
        for (Integer i : datalist){
            sum += i;
        }
        return sum;
    }

    public SpcPchartEntity convert2SpcPchartEntity(SpcPchartStandardsEntity standardsEntity) {
        if (standardsEntity == null) {
            return null; // 如果传入的对象为 null，返回 null
        }

        SpcPchartEntity pchartEntity = new SpcPchartEntity();

        // 复制主键
        pchartEntity.setSpcpId(standardsEntity.getSpcpId());

        // 复制当月各天数据
        pchartEntity.setDate1(standardsEntity.getDate1());
        pchartEntity.setDate2(standardsEntity.getDate2());
        pchartEntity.setDate3(standardsEntity.getDate3());
        pchartEntity.setDate4(standardsEntity.getDate4());
        pchartEntity.setDate5(standardsEntity.getDate5());
        pchartEntity.setDate6(standardsEntity.getDate6());
        pchartEntity.setDate7(standardsEntity.getDate7());
        pchartEntity.setDate8(standardsEntity.getDate8());
        pchartEntity.setDate9(standardsEntity.getDate9());
        pchartEntity.setDate10(standardsEntity.getDate10());
        pchartEntity.setDate11(standardsEntity.getDate11());
        pchartEntity.setDate12(standardsEntity.getDate12());
        pchartEntity.setDate13(standardsEntity.getDate13());
        pchartEntity.setDate14(standardsEntity.getDate14());
        pchartEntity.setDate15(standardsEntity.getDate15());
        pchartEntity.setDate16(standardsEntity.getDate16());
        pchartEntity.setDate17(standardsEntity.getDate17());
        pchartEntity.setDate18(standardsEntity.getDate18());
        pchartEntity.setDate19(standardsEntity.getDate19());
        pchartEntity.setDate20(standardsEntity.getDate20());
        pchartEntity.setDate21(standardsEntity.getDate21());
        pchartEntity.setDate22(standardsEntity.getDate22());
        pchartEntity.setDate23(standardsEntity.getDate23());
        pchartEntity.setDate24(standardsEntity.getDate24());
        pchartEntity.setDate25(standardsEntity.getDate25());
        pchartEntity.setDate26(standardsEntity.getDate26());
        pchartEntity.setDate27(standardsEntity.getDate27());
        pchartEntity.setDate28(standardsEntity.getDate28());
        pchartEntity.setDate29(standardsEntity.getDate29());
        pchartEntity.setDate30(standardsEntity.getDate30());
        pchartEntity.setDate31(standardsEntity.getDate31());

        // 复制合计
        pchartEntity.setSum(standardsEntity.getSum());

        // 复制数据日期
        pchartEntity.setDatatime(standardsEntity.getDatatime());

        // 复制数据内容
        pchartEntity.setDataContent(standardsEntity.getDataContent());

        // 复制创建人和创建时间
        pchartEntity.setCreateBy(standardsEntity.getCreateBy());
        pchartEntity.setCreateTime(standardsEntity.getCreateTime());

        // 复制备用字段
        pchartEntity.setAlternateFields1(standardsEntity.getAlternateFields1());
        pchartEntity.setAlternateFields2(standardsEntity.getAlternateFields2());
        pchartEntity.setAlternateFields3(standardsEntity.getAlternateFields3());
        pchartEntity.setAlternateFields4(standardsEntity.getAlternateFields4());

        // 复制逻辑删除标志
        pchartEntity.setDeleteFlag(standardsEntity.getDeleteFlag());

        return pchartEntity;
    }

}