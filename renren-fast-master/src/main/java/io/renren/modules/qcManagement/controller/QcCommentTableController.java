package io.renren.modules.qcManagement.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.qcManagement.entity.QcCommentTableEntity;
import io.renren.modules.qcManagement.service.QcCommentTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 *
 */
@RestController
@RequestMapping("qc/table")
public class QcCommentTableController {
    @Autowired
    private QcCommentTableService qcCommentTableService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qc:table:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = qcCommentTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{qcTableId}")
    @RequiresPermissions("qc:table:info")
    public R info(@PathVariable("qcTableId") Integer qcTableId){
		QcCommentTableEntity qcCommentTable = qcCommentTableService.getById(qcTableId);

        return R.ok().put("qcCommentTable", qcCommentTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qc:table:save")
    public R save(@RequestBody QcCommentTableEntity qcCommentTable){
		qcCommentTableService.save(qcCommentTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qc:table:update")
    public R update(@RequestBody QcCommentTableEntity qcCommentTable){
		qcCommentTableService.updateById(qcCommentTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qc:table:delete")
    public R delete(@RequestBody Integer[] qcTableIds){
		qcCommentTableService.removeByIds(Arrays.asList(qcTableIds));

        return R.ok();
    }

}
