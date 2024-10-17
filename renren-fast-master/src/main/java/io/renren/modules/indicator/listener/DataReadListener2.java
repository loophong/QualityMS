package io.renren.modules.indicator.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import io.renren.modules.indicator.entity.IndicatorDataReadEntity;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 自定义监听器，处理读取到的Excel数据
@Slf4j

public class DataReadListener2 extends AnalysisEventListener<IndicatorDataReadEntity> {



    /**
     * 用于暂存数据的集合，直到数量等于batchSize时就会进行插入操作并清空集合
     */
    private List<IndicatorDataReadEntity> batchList = new ArrayList<>();



    @Override
    public void invoke(IndicatorDataReadEntity entity, AnalysisContext analysisContext) {
        // 处理空行数据
        if (isEmptyRow(entity)) {
            log.info("忽略空行数据");
            return;
        }
        log.info("读取到的行数据：{}", entity);
        batchList.add(entity);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("Excel读取完成！");
    }




    private boolean isEmptyRow(IndicatorDataReadEntity entity) {
        // 检查实体的所有字段是否都为空
        return entity.getDataContent() == null &&
                entity.getDataValue() == null;
    }

    public List<IndicatorDataReadEntity> getBatchList() {
        return batchList;
    }

}
