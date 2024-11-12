package io.renren.modules.taskmanagement.dto;

import io.renren.modules.taskmanagement.entity.PlanEntity;
import lombok.Data;

import java.util.Map;

@Data
public class PlanQueryParamDTO {
    Integer page;

    Integer limit;

    PlanEntity plan;
}
