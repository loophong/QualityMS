package io.renren.modules.publicmanagement.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.publicmanagement.entity.InstrumentLedgerEntity;
import io.renren.modules.publicmanagement.service.InstrumentLedgerService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 仪器台账表
 *
 * @author hong
 * @email ${email}
 * @date 2024-08-02 13:37:30
 */
@RestController
@RequestMapping("publicmanagement/instrumentledger")
public class InstrumentLedgerController {
    @Autowired
    private InstrumentLedgerService instrumentLedgerService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("publicmanagement:instrumentledger:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = instrumentLedgerService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("taskmanagement:instrumentledger:info")
    public R info(@PathVariable("id") Integer id){
		InstrumentLedgerEntity instrumentLedger = instrumentLedgerService.getById(id);

        return R.ok().put("instrumentLedger", instrumentLedger);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("taskmanagement:instrumentledger:save")
    public R save(@RequestBody InstrumentLedgerEntity instrumentLedger){
		instrumentLedgerService.save(instrumentLedger);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("taskmanagement:instrumentledger:update")
    public R update(@RequestBody InstrumentLedgerEntity instrumentLedger){
		instrumentLedgerService.updateById(instrumentLedger);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("taskmanagement:instrumentledger:delete")
    public R delete(@RequestBody Integer[] ids){
		instrumentLedgerService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
