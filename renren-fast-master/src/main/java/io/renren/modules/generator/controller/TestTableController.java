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

import io.renren.modules.generator.entity.TestTableEntity;
import io.renren.modules.generator.service.TestTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-18 08:58:50
 */
@RestController
@RequestMapping("generator/testtable")
public class TestTableController {
    @Autowired
    private TestTableService testTableService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:testtable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = testTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:testtable:info")
    public R info(@PathVariable("id") Long id){
		TestTableEntity testTable = testTableService.getById(id);

        return R.ok().put("testTable", testTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:testtable:save")
    public R save(@RequestBody TestTableEntity testTable){
		testTableService.save(testTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:testtable:update")
    public R update(@RequestBody TestTableEntity testTable){
		testTableService.updateById(testTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:testtable:delete")
    public R delete(@RequestBody Long[] ids){
		testTableService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
