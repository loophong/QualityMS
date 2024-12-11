package io.renren.modules.taskmanagement.controller;

import io.renren.modules.taskmanagement.entity.DeptDTO;
import io.renren.modules.taskmanagement.service.impl.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @BelongsProject: QualityMS
 * @BelongsPackage: io.renren.modules.taskmanagement.controller
 * @Author: hong
 * @CreateTime: 2024-07-23  15:16
 * @Description: TODO
 * @Version: 1.0
 */

@RestController
@RequestMapping("taskmanagement/user")
public class UserController {
    @Autowired
    private EmployeeService userService;

    @GetMapping("/getEmployeesGroupedByDepartment")
    public List<DeptDTO> getEmployeesGroupedByDepartment() {
        return userService.getEmployeesGroupedByDepartment();
    }

    @GetMapping("/getName")
    public List<DeptDTO> getName() {
        return userService.getName();
    }
}
