package io.renren.modules.instrument.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import io.renren.modules.instrument.dao.InstrumentationDao;
import io.renren.modules.instrument.service.InstrumentTestMethodService;
import io.renren.modules.publicmanagement.entity.InstrumentLedgerEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.instrument.entity.InstrumentationEntity;
import io.renren.modules.instrument.service.InstrumentationService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-31 10:36:37
 */
@RestController
@RequestMapping("generator/instrumentation")
public class InstrumentationController {
    @Autowired
    private InstrumentationDao instrumentationDao;
    @Autowired
    private InstrumentationService instrumentationService;
    @Autowired
    private InstrumentTestMethodService instrumentTestMethodService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:instrumentation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = instrumentationService.queryPage(params);

        return R.ok().put("page", page);
    }
    /**
     * 信息
     */
    @RequestMapping("/linkedMethod/{id}")
    @RequiresPermissions("generator:instrumentation:info")
    public R linkedMethod(@PathVariable("id") Integer id){
        InstrumentationEntity instrumentation = instrumentationService.getById(id);
        System.out.println("看关联的检验方法"+instrumentation);
         String toolName = instrumentation.getToolName();
//        instrumentTestMethodService.get
        return R.ok().put("instrumentation", instrumentation);
    }
    /*
    * 模糊查询
    * */
    @GetMapping("/Fuzzy_queries")
    public R Fuzzy_queries(@RequestParam Map<String, Object> params) {

        PageUtils page = instrumentationService.queryPage(params);

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
            EasyExcel.read(excelFile.getInputStream(), InstrumentationEntity.class, new PageReadListener<InstrumentationEntity>(dataList -> {
                for (InstrumentationEntity user : dataList) {
                    //将导入的数据用mybatisPlus一个个添加进数据库
                    System.out.println(user);
//                    instrumentLedgerDao.insertinstrumentLedger(user);
                    int insert = instrumentationDao.insert(user);
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
    @RequiresPermissions("generator:instrumentation:info")
    public R info(@PathVariable("id") Integer id){
		InstrumentationEntity instrumentation = instrumentationService.getById(id);

        return R.ok().put("instrumentation", instrumentation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:instrumentation:save")
    public R save(@RequestBody InstrumentationEntity instrumentation){
		instrumentationService.save(instrumentation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:instrumentation:update")
    public R update(@RequestBody InstrumentationEntity instrumentation){
		instrumentationService.updateById(instrumentation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:instrumentation:delete")
    public R delete(@RequestBody Integer[] ids){
		instrumentationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
