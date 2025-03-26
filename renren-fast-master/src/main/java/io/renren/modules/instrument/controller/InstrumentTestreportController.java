package io.renren.modules.instrument.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import io.renren.modules.instrument.dao.InstrumentTestreportDao;
import io.renren.modules.instrument.entity.InstrumentationEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.instrument.entity.InstrumentTestreportEntity;
import io.renren.modules.instrument.service.InstrumentTestreportService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2025-01-16 16:51:59
 */
@RestController
@RequestMapping("generator/instrumenttestreport")
public class InstrumentTestreportController {
    @Autowired
    private InstrumentTestreportService instrumentTestreportService;
    @Autowired
    private InstrumentTestreportDao instrumentTestreportDao;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("generator:instrumenttestreport:list")
    public R list(@RequestParam Map<String, Object> params){
        System.out.println("test");
        PageUtils page = instrumentTestreportService.queryPage(params);

        return R.ok().put("page", page);
    }
    /*
     * 导入
     * */
    @PostMapping("/import")
    public R importExcel(@RequestParam("file") MultipartFile excelFile)
    {
        System.out.println("---------import--------------");
        try {
            EasyExcel.read(excelFile.getInputStream(), InstrumentTestreportEntity.class, new PageReadListener<InstrumentTestreportEntity>(dataList -> {
                for (InstrumentTestreportEntity user : dataList) {
                    //将导入的数据用mybatisPlus一个个添加进数据库
                    System.out.println(user);
//                    instrumentLedgerDao.insertinstrumentLedger(user);
                    int insert = instrumentTestreportDao.insert(user);
                    System.out.println(insert);

//                    loginMapper.insert(user);
                }
            })).sheet().doRead();

//            marketOrderSumnumberService.importInterests(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:instrumenttestreport:info")
    public R info(@PathVariable("id") Integer id){
		InstrumentTestreportEntity instrumentTestreport = instrumentTestreportService.getById(id);

        return R.ok().put("instrumentTestreport", instrumentTestreport);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:instrumenttestreport:save")
    public R save(@RequestBody InstrumentTestreportEntity instrumentTestreport){
		instrumentTestreportService.save(instrumentTestreport);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:instrumenttestreport:update")
    public R update(@RequestBody InstrumentTestreportEntity instrumentTestreport){
		instrumentTestreportService.updateById(instrumentTestreport);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:instrumenttestreport:delete")
    public R delete(@RequestBody Integer[] ids){
		instrumentTestreportService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
