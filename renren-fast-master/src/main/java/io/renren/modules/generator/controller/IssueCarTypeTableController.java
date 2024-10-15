package io.renren.modules.generator.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueCarTypeTableEntity;
import io.renren.modules.generator.service.IssueCarTypeTableService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 车型字典
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 18:55:06
 */
@RestController
@RequestMapping("generator/issuecartypetable")
public class IssueCarTypeTableController {
    @Autowired
    private IssueCarTypeTableService issueCarTypeTableService;

    /**
     * Excel上传
     */
    @PostMapping("/uploadExcel")
    @RequiresPermissions("generator:issuetable:update")
    public R uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("接收到的文件名: " + file.getOriginalFilename());
            return issueCarTypeTableService.uploadExcelFile(file);
        } catch (IOException e) {
            return R.error("文件上传时发生错误: " + e.getMessage());
        }
    }
    /**
     * 获取车辆信息
     */
    @RequestMapping("/carname")
    @RequiresPermissions("generator:issuecartypetable:list")
    public R list(){
        System.out.println("=====获取车辆列表----开始");
        List<IssueCarTypeTableEntity> carType = issueCarTypeTableService.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("carTypeTable", carType);
        System.out.println(response);
        System.out.println("=====获取车辆列表----结束");
        return R.ok().put("carType", carType);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:issuecartypetable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = issueCarTypeTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{vehicleTypeId}")
    @RequiresPermissions("generator:issuecartypetable:info")
    public R info(@PathVariable("vehicleTypeId") Integer vehicleTypeId){
		IssueCarTypeTableEntity issueCarTypeTable = issueCarTypeTableService.getById(vehicleTypeId);

        return R.ok().put("issueCarTypeTable", issueCarTypeTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:issuecartypetable:save")
    public R save(@RequestBody IssueCarTypeTableEntity issueCarTypeTable){
		issueCarTypeTableService.save(issueCarTypeTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:issuecartypetable:update")
    public R update(@RequestBody IssueCarTypeTableEntity issueCarTypeTable){
		issueCarTypeTableService.updateById(issueCarTypeTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:issuecartypetable:delete")
    public R delete(@RequestBody Integer[] vehicleTypeIds){
		issueCarTypeTableService.removeByIds(Arrays.asList(vehicleTypeIds));

        return R.ok();
    }

}
