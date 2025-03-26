package io.renren.modules.generator.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.generator.entity.IndemnificationTableEntity;
import io.renren.modules.generator.service.IndemnificationTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2025-01-10 15:53:47
 */
@RestController
@RequestMapping("generator/indemnificationtable")
public class IndemnificationTableController {
    @Autowired
    private IndemnificationTableService indemnificationTableService;

    /**
     * Excel上传
     */
    @PostMapping("/uploadExcel")
    @RequiresPermissions("generator:issuetable:update")
    public R uploadExcel(@RequestParam("file") MultipartFile file) {
        // 上传文件
//        System.out.println("接收到的文件名: " + file.getOriginalFilename());
        return indemnificationTableService.uploadExcelFile(file);

    }
    /**
     * 赔偿件列表
     */
    @RequestMapping("/list1")
    public R list1(){
        List<IndemnificationTableEntity> list = indemnificationTableService.getAllIndemnifications();
        return R.ok().put("result", list);
    }
    @RequestMapping("/getDetails")
    public R getDetails(@RequestParam int id) {
        IndemnificationTableEntity indemnification = indemnificationTableService.getIndemnificationById(id);
        if (indemnification == null) {
            return R.error("数据不存在");
        }
        return R.ok()
                .put("figurenumber", indemnification.getFigureNumber())
                .put("qualitycost", indemnification.getQualitycost());
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:indemnificationtable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = indemnificationTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{indeId}")
    @RequiresPermissions("generator:indemnificationtable:info")
    public R info(@PathVariable("indeId") Integer indeId){
		IndemnificationTableEntity indemnificationTable = indemnificationTableService.getById(indeId);

        return R.ok().put("indemnificationTable", indemnificationTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:indemnificationtable:save")
    public R save(@RequestBody IndemnificationTableEntity indemnificationTable){
		indemnificationTableService.save(indemnificationTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:indemnificationtable:update")
    public R update(@RequestBody IndemnificationTableEntity indemnificationTable){
		indemnificationTableService.updateById(indemnificationTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:indemnificationtable:delete")
    public R delete(@RequestBody Integer[] indeIds){
		indemnificationTableService.removeByIds(Arrays.asList(indeIds));

        return R.ok();
    }

}
