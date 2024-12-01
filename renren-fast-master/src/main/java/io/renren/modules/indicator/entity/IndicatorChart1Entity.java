package io.renren.modules.indicator.entity;

import lombok.Data;

import java.util.Date;

@Data
public class IndicatorChart1Entity {
    private Date yearMonth;

    /**
     * 达成的指标数量
     * */
    private Integer finishedCounts;

    /**
     * 未达成的指标数量
     * */
    private Integer unfinishedCounts;
}
