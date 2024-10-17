package io.renren.modules.indicator.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IndicatorDataReadEntity {
    /**
     * 数据
     */
    @ExcelProperty(value = "数据")
    private String dataContent;
    /**
     * 数据类型
     */
    @ExcelProperty(value = "数据值")
    private BigDecimal dataValue;
}
