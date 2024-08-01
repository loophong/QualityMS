package io.renren.modules.taskmanagement.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.modules.taskmanagement.entity.TaskDataGroupEntity;
import io.renren.modules.taskmanagement.service.TaskDataGroupService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author hong
 * @email ${email}
 * @date 2024-07-18 15:11:00
 */
@RestController
@RequestMapping("taskmanagement/group")
public class TaskDataGroupController {
    @Autowired
    private TaskDataGroupService taskDataGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("taskmanagement:taskdatagroup:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = taskDataGroupService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{tdGid}")
    @RequiresPermissions("taskmanagement:taskdatagroup:info")
    public R info(@PathVariable("tdGid") Integer tdGid){
		TaskDataGroupEntity taskDataGroup = taskDataGroupService.getById(tdGid);

        return R.ok().put("taskDataGroup", taskDataGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("taskmanagement:taskdatagroup:save")
    public R save(@RequestBody TaskDataGroupEntity taskDataGroup){
		taskDataGroupService.save(taskDataGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("taskmanagement:taskdatagroup:update")
    public R update(@RequestBody TaskDataGroupEntity taskDataGroup){
		taskDataGroupService.updateById(taskDataGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("taskmanagement:taskdatagroup:delete")
    public R delete(@RequestBody Integer[] tdGids){
		taskDataGroupService.removeByIds(Arrays.asList(tdGids));

        return R.ok();
    }

}
