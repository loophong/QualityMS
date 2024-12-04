package io.renren.modules.indicator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class IndicatorChart1Entity {
    @JsonFormat(pattern = "yyyy-MM")
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
