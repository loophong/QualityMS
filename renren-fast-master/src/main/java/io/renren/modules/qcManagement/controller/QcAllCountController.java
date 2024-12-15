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

import io.renren.modules.qcManagement.entity.QcAllCountEntity;
import io.renren.modules.qcManagement.service.QcAllCountService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-13 20:23:51
 */
@RestController
@RequestMapping("qcManagement/qcAllCount")
public class QcAllCountController {
    @Autowired
    private QcAllCountService qcAllCountService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qcManagement:qcAllCount:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = qcAllCountService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{qcAllId}")
    @RequiresPermissions("qcManagement:qcAllCount:info")
    public R info(@PathVariable("qcAllId") Integer qcAllId){
		QcAllCountEntity qcAllCount = qcAllCountService.getById(qcAllId);
        return R.ok().put("qcAllCount", qcAllCount);
    }

    /**
     * word信息
     */
    @RequestMapping("/wordInfo")
//    @RequiresPermissions("qcManagement:qcAllCount:info")
    public R wordInfo(){
        QcAllCountEntity qcAllCount = qcAllCountService.getById(1);
        return R.ok().put("qcAllCount", qcAllCount);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qcManagement:qcAllCount:save")
    public R save(@RequestBody QcAllCountEntity qcAllCount){
		qcAllCountService.save(qcAllCount);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qcManagement:qcAllCount:update")
    public R update(@RequestBody QcAllCountEntity qcAllCount){
		qcAllCountService.updateById(qcAllCount);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/word")
//    @RequiresPermissions("qcManagement:qcAllCount:update")
    public R word(@RequestBody QcAllCountEntity qcAllCount){
        qcAllCountService.updateById(qcAllCount);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qcManagement:qcAllCount:delete")
    public R delete(@RequestBody Integer[] qcAllIds){
		qcAllCountService.removeByIds(Arrays.asList(qcAllIds));

        return R.ok();
    }

}
