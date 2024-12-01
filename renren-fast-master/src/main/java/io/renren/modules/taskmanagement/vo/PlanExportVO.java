package io.renren.modules.taskmanagement.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.modules.taskmanagement.entity.TaskStatus;
import io.renren.modules.taskmanagement.utils.JacksonListTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@HeadRowHeight(30) //表头行高
@ContentRowHeight(15) //内容行高
@ColumnWidth(18)  //列宽
@ContentFontStyle(fontHeightInPoints = (short) 12) //字体大小
public class PlanExportVO {


    /**
     * 计划ID
     */
    @ExcelProperty("计划编号")
    private String planId;

    /**
     * 任务ID
     */
    @ExcelProperty("任务编号")
    private String taskId;

    /**
     * 名
     */
    @ExcelProperty("名称")
    private String name;

    /**
     * 内容
     */
    @ExcelProperty("内容")
    private String content;
    /**
     * 开始日期
     */
    @ExcelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 计划完成日期
     */
    @ExcelProperty("计划完成日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scheduleCompletionDate;

    /**
     * 计划天数
     */
    @ExcelProperty("计划天数")
    private Long scheduleDays;

    /**
     * 实际完成日期
     */
    @ExcelProperty("实际完成日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date actualCompletionDate;

    /**
     * 实际天数
     */
    @ExcelProperty("实际天数")
    private Long actualDays;

    /**
     * 进度
     */
    @ExcelProperty("进度")
    private String schedule;
    /**
     * 负责人
     */
    @ExcelProperty("负责人")
    private String principal;
    /**
     * 执行人
     */
    @ExcelProperty("执行人")
    private String executor;

    /**
     * 审核人
     */
    @ExcelProperty("审核人")
    private String auditor;

    /**
     * 当前状态
     */
    @ExcelProperty("当前状态")
    private String currentState;
    /**
     * 是否完成
     */
    @ExcelProperty("是否完成")
    private Integer isCompleted;
    /**
     * 是否超期
     */
    @ExcelProperty("是否超期")
    private Integer isOverdue;
    /**
     * 按时完工
     */
    @ExcelProperty("是否按时完工")
    private Integer isOnTime;
    /**
     * 提前完工
     */
    @ExcelProperty("提前完工")
    private Long earlyCompletionDays;
    /**
     * 滞后天数
     */
    @ExcelProperty("滞后天数")
    private Long lagDays;

    /**
     * 滞后原因
     */
    @ExcelProperty("滞后原因")
    private String lagReasons;

    /**
     * 关联指标编号
     */
    @ExcelProperty("关联指标名称")
    private String associatedIndicatorsId;
}
//package io.renren.modules.taskmanagement.vo;
//
//import com.alibaba.excel.annotation.write.style.ColumnWidth;
//import com.alibaba.excel.annotation.write.style.ContentFontStyle;
//import com.alibaba.excel.annotation.write.style.ContentRowHeight;
//import com.alibaba.excel.annotation.write.style.HeadRowHeight;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import io.renren.modules.taskmanagement.entity.TaskStatus;
//import io.renren.modules.taskmanagement.utils.JacksonListTypeHandler;
//import lombok.Data;
//
//import java.util.Date;
//import java.util.List;
//
//@Data
//@HeadRowHeight(30) //表头行高
//@ContentRowHeight(15) //内容行高
//@ColumnWidth(18)  //列宽
//@ContentFontStyle(fontHeightInPoints = (short) 12) //字体大小
//public class PlanExportVO {
//
//
//    /**
//     * 计划ID
//     */
//    private String planId;
//
//    /**
//     * 任务ID
//     */
//    private String taskId;
//
//    /**
//     * 名
//     */
//    private String name;
//
//    /**
//     * 内容
//     */
//    private String content;
//    /**
//     * 开始日期
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private Date startDate;
//    /**
//     * 计划完成日期
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private Date scheduleCompletionDate;
//    /**
//     * 计划天数
//     */
//    private Long scheduleDays;
//    /**
//     * 实际完成日期
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private Date actualCompletionDate;
//    /**
//     * 实际天数
//     */
//    private Long actualDays;
//
//    /**
//     * 进度
//     */
//    private String schedule;
//    /**
//     * 负责人
//     */
//    private String principal;
//    /**
//     * 执行人
//     */
//
//    @TableField(typeHandler = JacksonListTypeHandler.class)
//    private List<String> executor;
//
//    /**
//     * 审核人
//     */
//    private String planAuditor;
//
//    /**
//     * 当前状态
//     */
//    private TaskStatus planCurrentState;
//    /**
//     * 是否完成
//     */
//    private Integer planIsCompleted;
//    /**
//     * 是否超期
//     */
//    private Integer planIsOverdue;
//    /**
//     * 按时完工
//     */
//    private Integer isOnTime;
//    /**
//     * 提前完工
//     */
//    private Long earlyCompletionDays;
//    /**
//     * 滞后天数
//     */
//    private Long lagDays;
//
//    /**
//     * 滞后原因
//     */
//    private String lagReasons;
//
//    /**
//     * 关联指标编号
//     */
//    private String planAssociatedIndicatorsId;
//}
