package io.renren.modules.generator.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.VerificationDurationTableEntity;
import io.renren.modules.generator.service.VerificationDurationTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-10 16:51:55
 */
@RestController
@RequestMapping("generator/verificationdurationtable")
public class VerificationDurationTableController {
    @Autowired
    private VerificationDurationTableService verificationDurationTableService;

    /**
     * 获取最新的 verificationDuration
     */
    @RequestMapping("/fetchverificationDuration")
    public R fetchVerificationDuration() {
        // 获取 ID 最大的记录
        VerificationDurationTableEntity latestRecord = verificationDurationTableService.getLatestRecord();

        // 如果记录为空，则返回 0
        long verificationDuration = (latestRecord != null) ? latestRecord.getVerificationDuration() : 0;
        System.out.println("最新 verificationDuration：" + verificationDuration);
        return R.ok().put("verificationDuration", verificationDuration);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:verificationdurationtable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = verificationDurationTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{examineId}")
    @RequiresPermissions("generator:verificationdurationtable:info")
    public R info(@PathVariable("examineId") Integer examineId){
        VerificationDurationTableEntity verificationDurationTable = verificationDurationTableService.getById(examineId);

        return R.ok().put("verificationDurationTable", verificationDurationTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:verificationdurationtable:save")
    public R save(@RequestBody VerificationDurationTableEntity verificationDurationTable){
        verificationDurationTableService.save(verificationDurationTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:verificationdurationtable:update")
    public R update(@RequestBody VerificationDurationTableEntity verificationDurationTable){
        verificationDurationTableService.updateById(verificationDurationTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:verificationdurationtable:delete")
    public R delete(@RequestBody Integer[] examineIds){
        verificationDurationTableService.removeByIds(Arrays.asList(examineIds));

        return R.ok();
    }

}
