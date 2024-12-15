package io.renren.modules.taskmanagement.service.impl;

import io.renren.modules.taskmanagement.dao.UserDeptInfoDao;
import io.renren.modules.taskmanagement.entity.DeptDTO;
import io.renren.modules.taskmanagement.entity.DeptOption;
import io.renren.modules.taskmanagement.entity.UserDeptInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: QualityMS
 * @BelongsPackage: io.renren.modules.taskmanagement.service.impl
 * @Author: hong
 * @CreateTime: 2024-07-23  15:07
 * @Description: TODO
 * @Version: 1.0
 */

@Slf4j
@Service
public class EmployeeService  {

    @Autowired
    private UserDeptInfoDao userDeptInfoDao;

    public List<DeptDTO> getEmployeesGroupedByDepartment() {
        List<UserDeptInfoDTO> employees = userDeptInfoDao.getEmployeesGroupedByDepartment();
        Map<String, List<DeptOption>> groupedData = new LinkedHashMap<>();

        for (UserDeptInfoDTO employee : employees) {
            String departmentName = employee.getDept();
//            String departmentName = employee.getDeptName();
            groupedData.putIfAbsent(departmentName, new ArrayList<>());
            log.info("departmentName: " + departmentName);
            log.info("employees: " + employees);
            String label = employee.getNickname() + "(" + employee.getUserName() + ")";
//            groupedData.get(departmentName).add(new DeptOption(employee.getUserId(), employee.getUserName()));
            groupedData.get(departmentName).add(new DeptOption(employee.getUserId(), label));
        }

        List<DeptDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<DeptOption>> entry : groupedData.entrySet()) {
            result.add(new DeptDTO(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    public List<DeptDTO> getName() {
        List<UserDeptInfoDTO> employees = userDeptInfoDao.getEmployeesGroupedByDepartment();
        Map<String, List<DeptOption>> groupedData = new LinkedHashMap<>();

        for (UserDeptInfoDTO employee : employees) {
            String departmentName = employee.getDept();
//            String departmentName = employee.getDeptName();
            groupedData.putIfAbsent(departmentName, new ArrayList<>());
            log.info("departmentName: " + departmentName);
            log.info("employees: " + employees);
            String label = employee.getNickname();
//            groupedData.get(departmentName).add(new DeptOption(employee.getUserId(), employee.getUserName()));
            groupedData.get(departmentName).add(new DeptOption(employee.getUserId(), label));
        }

        List<DeptDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<DeptOption>> entry : groupedData.entrySet()) {
            result.add(new DeptDTO(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
