package io.renren.modules.generator.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.generator.entity.IssueFaultTableEntity;
import io.renren.modules.generator.service.IssueFaultTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-20 15:20:56
 */
@RestController
@RequestMapping("generator/issuefaulttable")
public class IssueFaultTableController {
    @Autowired
    private IssueFaultTableService issueFaultTableService;


    /**
     * Excel上传
     */
    @PostMapping("/uploadExcel")
    @RequiresPermissions("generator:issuetable:update")
    public R uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("接收到的文件名: " + file.getOriginalFilename());
            return issueFaultTableService.uploadExcelFile(file);
        } catch (IOException e) {
            return R.error("文件上传时发生错误: " + e.getMessage());
        }
    }

    @RequestMapping("/options")
    @RequiresPermissions("generator:issuefaulttable:list")
    public R getFaultOptions() {
        // 获取所有记录
        List<IssueFaultTableEntity> faultList = issueFaultTableService.list();

        // 构建选项数据
        List<Map<String, String>> options = faultList.stream().map(fault -> {
            Map<String, String> option = new HashMap<>();
            option.put("systematicClassification", fault.getSystematicClassification());
            option.put("firstFaultyParts", fault.getFirstFaultyParts());
            option.put("secondFaultyParts", fault.getSecondFaultyParts());
            option.put("faultType", fault.getFaultType());
            option.put("faultModel", fault.getFaultModel());
            return option;
        }).collect(Collectors.toList());

        return R.ok().put("options", options);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:issuefaulttable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = issueFaultTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{faultId}")
    @RequiresPermissions("generator:issuefaulttable:info")
    public R info(@PathVariable("faultId") Integer faultId){
		IssueFaultTableEntity issueFaultTable = issueFaultTableService.getById(faultId);

        return R.ok().put("issueFaultTable", issueFaultTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:issuefaulttable:save")
    public R save(@RequestBody IssueFaultTableEntity issueFaultTable){
		issueFaultTableService.save(issueFaultTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:issuefaulttable:update")
    public R update(@RequestBody IssueFaultTableEntity issueFaultTable){
		issueFaultTableService.updateById(issueFaultTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:issuefaulttable:delete")
    public R delete(@RequestBody Integer[] faultIds){
		issueFaultTableService.removeByIds(Arrays.asList(faultIds));

        return R.ok();
    }

}
