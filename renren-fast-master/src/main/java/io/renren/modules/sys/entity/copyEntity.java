package io.renren.modules.sys.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class copyEntity {
    @ExcelProperty(value = "员工编号", index = 1)
    private String username;
    @ExcelProperty(value = "三级组织", index = 5)
    private String dept;

    /**
     * 2024/12/10 新增
     * 昵称
     */
    @ExcelProperty(value = "姓名", index = 2)
    private String nickname;

}
