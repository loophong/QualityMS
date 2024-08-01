package io.renren.modules.indicator.service;

import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;

import java.util.List;

public interface IDataBaseService {
    void batchInsert(List<IndicatorIndicatorSummaryEntity> dataList);

}
