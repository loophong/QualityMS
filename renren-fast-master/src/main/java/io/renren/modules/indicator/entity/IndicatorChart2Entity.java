package io.renren.modules.indicator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class IndicatorChart2Entity {
    @JsonFormat(pattern = "yyyy-MM")
    private Date yearMonth;
    private String indicatorClassification;
    private Integer finishedCounts;
    private Integer unfinishedCounts;
}
