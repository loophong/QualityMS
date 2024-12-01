package io.renren.modules.publicmanagement.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.utils.Query;
import io.renren.modules.indicator.entity.IndicatorDataDictionaryEntity;
import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import io.renren.modules.publicmanagement.dao.InstrumentLedgerDao;
import io.renren.modules.qcManagement.dao.QcSubjectRegistrationDao;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.publicmanagement.entity.InstrumentLedgerEntity;
import io.renren.modules.publicmanagement.service.InstrumentLedgerService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 仪器台账表
 *
 * @author hong
 * @email ${email}
 * @date 2024-08-02 13:37:30
 */
@RestController
@RequestMapping("publicmanagement/instrumentledger")
public class InstrumentLedgerController  {
    @Autowired
    private InstrumentLedgerService instrumentLedgerService;

    @Autowired
    private InstrumentLedgerDao instrumentLedgerDao;


    @Autowired
    private QcSubjectRegistrationDao qcSubjectRegistrationDao;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("publicmanagement:instrumentledger:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = instrumentLedgerService.queryPage(params);
        return R.ok().put("page", page);
    }
//System.out.println("params:" + params);
//    String key = (String) params.get("key");
//    QueryWrapper<InstrumentLedgerEntity> queryWrapper = new QueryWrapper<>();
//
//        if (key != null && !key.isEmpty()) {
//        try {
//            // 将 key 字符串解析为 Map
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, String> keyMap = objectMapper.readValue(key, Map.class);
//
//            // 遍历 Map 并添加查询条件
//            for (Map.Entry<String, String> entry : keyMap.entrySet()) {
//                String field = entry.getKey();
//                String value = entry.getValue();
//
//                if (value != null && !value.isEmpty()) {
//                    switch (field) {
//                        case "companyId":
//                            queryWrapper.like("company_id", value);
//                            break;
//                        case "level":
//                            queryWrapper.like("level", value);
//                            break;
//                        case "calibrationCycle":
//                            queryWrapper.like("calibration_cycle", value);
//                            break;
//                        case "calibrationStatus":
//                            queryWrapper.like("calibration_status", value);
//                            break;
//                        case "assessmentDepartment":
//                            queryWrapper.like("assessment_department", value);
//                            break;
//                        // 可以根据需要添加更多的字段
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 这里可以处理异常，比如返回错误信息给前端
//        }
//    }
//    IPage<IndicatorDataDictionaryEntity> page = this.page(
//            new Query<IndicatorDataDictionaryEntity>().getPage(params),
//            queryWrapper
//    );
//
//    PageUtils pages = new PageUtils(page);
//
//
//        return R.ok().put("page", pages);
    @GetMapping("/Fuzzy_queries")
    public R Fuzzy_queries(@RequestParam Map<String, Object> params) {

        PageUtils page = instrumentLedgerService.queryPage(params);

        return R.ok().put("page", page);

    }

    @PostMapping ("/message")
//    @RequiresPermissions("publicmanagement:instrumentledger:list")
    public  List<ResMessage> message(){
        List<InstrumentLedgerEntity> instrumentLedgerEntities = instrumentLedgerDao.selectList(null);
        System.out.println(instrumentLedgerEntities);
        // 获取当前日期
        Date currentDate = new Date();

// 使用stream过滤出calibrationValidity超过当前日期的实体
//        List<ResMessage> resMessages = instrumentLedgerEntities.stream()
//                .map(entity -> {
//                    // 根据InstrumentLedgerEntity的calibrationValidity与当前日期比较，设置state
//                    int state = entity.getCalibrationValidity() != null && entity.getCalibrationValidity().before(currentDate) ? 1 : 0;
//                    // 创建ResMessage对象
//                    return new ResMessage(entity.getCompanyId(), state);
//                })
//                .collect(Collectors.toList());


        List<ResMessage> resMessages = instrumentLedgerEntities.stream()
                .map(entity -> {
                    // 确保 companyId 不为 null
                    String numberId = entity.getCompanyId() != null ? entity.getCompanyId() : "defaultId";
                    // 根据InstrumentLedgerEntity的calibrationValidity与当前日期比较，设置state
                    int state = entity.getCalibrationValidity() != null && entity.getCalibrationValidity().before(currentDate) ? 1 : 0;
                    // 创建ResMessage对象
                    return new ResMessage(numberId, state);
                })
                .collect(Collectors.toList());
// 打印过滤后的实体列表
        System.out.println(resMessages);

        System.out.println("message查询请求");
        return resMessages;

    }

    @PostMapping("/updatemessage")
//    @RequiresPermissions("publicmanagement:instrumentledger:list")
    public void updatemessage(@RequestBody InstrumentLedgerEntity params){

        System.out.println("message更新操作");

    }
    @PostMapping("/import")
    public R importExcel(@RequestParam("file") MultipartFile excelFile)
    {
        System.out.println("---------import--------------");
        try {
            EasyExcel.read(excelFile.getInputStream(), InstrumentLedgerEntity.class, new PageReadListener<InstrumentLedgerEntity>(dataList -> {
                for (InstrumentLedgerEntity user : dataList) {
                    //将导入的数据用mybatisPlus一个个添加进数据库
                    System.out.println(user);
//                    instrumentLedgerDao.insertinstrumentLedger(user);
                    int insert = instrumentLedgerDao.insert(user);
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
//    @RequiresPermissions("publicmanagement:instrumentledger:info")
    public R info(@PathVariable("id") Integer id){
        InstrumentLedgerEntity instrumentLedger = instrumentLedgerService.getById(id);

        return R.ok().put("instrumentLedger", instrumentLedger);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("publicmanagement:instrumentledger:save")
    public R save(@RequestBody InstrumentLedgerEntity instrumentLedger){
        instrumentLedgerService.save(instrumentLedger);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("publicmanagement:instrumentledger:update")
    public R update(@RequestBody InstrumentLedgerEntity instrumentLedger){
        instrumentLedgerService.updateById(instrumentLedger);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("publicmanagement:instrumentledger:delete")
    public R delete(@RequestBody Integer[] ids){
        instrumentLedgerService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
