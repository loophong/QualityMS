package io.renren.modules.taskmanagement.entity;

public enum TaskStatus {
    //已创建未审批
    CREATED_BUT_NOT_APPROVED,

    //初审中
    PREAPPROVAL_IN_PROGRESS,

    //初审未通过
    PREAPPROVAL_NOT_PASSED,


    //未开始
    NOT_STARTED,

    //进行中
    IN_PROGRESS,

    //已完成
    COMPLETED,

    //已逾期
    OVERDUE,

    //审批中
    APPROVAL_IN_PROGRESS

}
