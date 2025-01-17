package io.renren.modules.generator.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.modules.generator.entity.IssueCarTypeTableEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.VendorTableEntity;
import io.renren.modules.generator.service.VendorTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2025-01-10 15:54:12
 */
@RestController
@RequestMapping("generator/vendortable")
public class VendorTableController {
    @Autowired
    private VendorTableService vendorTableService;


    /**
     * 获取供应商信息
     */
    @RequestMapping("/carname")
    @RequiresPermissions("generator:issuecartypetable:list")
    public R list(){
        System.out.println("=====获取车辆列表----开始");
        List<VendorTableEntity> carType = vendorTableService.listAll();
//        Map<String, Object> response = new HashMap<>();
//        response.put("code", 0);
//        response.put("carTypeTable", carType);
//        System.out.println(response);
//        System.out.println("=====获取车辆列表----结束");
        return R.ok().put("carType", carType);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:vendortable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = vendorTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{vendorId}")
    @RequiresPermissions("generator:vendortable:info")
    public R info(@PathVariable("vendorId") Integer vendorId){
		VendorTableEntity vendorTable = vendorTableService.getById(vendorId);

        return R.ok().put("vendorTable", vendorTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:vendortable:save")
    public R save(@RequestBody VendorTableEntity vendorTable){
		vendorTableService.save(vendorTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:vendortable:update")
    public R update(@RequestBody VendorTableEntity vendorTable){
		vendorTableService.updateById(vendorTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:vendortable:delete")
    public R delete(@RequestBody Integer[] vendorIds){
		vendorTableService.removeByIds(Arrays.asList(vendorIds));

        return R.ok();
    }

}
