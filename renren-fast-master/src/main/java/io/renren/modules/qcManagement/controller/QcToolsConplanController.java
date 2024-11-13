package io.renren.modules.qcManagement.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.qcManagement.dao.QcToolsConplanDao;
import io.renren.modules.qcManagement.dao.QcToolsTemplateDao;
import io.renren.modules.qcManagement.entity.QcToolsConplanEntity;
import io.renren.modules.qcManagement.entity.QcToolsTemplateEntity;
import io.renren.modules.qcManagement.service.QcToolsConplanService;
import io.renren.modules.qcManagement.service.QcToolsTemplateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author sun
 * @email ${email}
 * @date 2024-10-20 17:53:45
 */
@RestController
@RequestMapping("qcTools/conplan")
public class QcToolsConplanController {
    @Autowired
    private QcToolsConplanService qcToolsConplanService;

    @Autowired
    private QcToolsConplanDao qcToolsConplanDao;

    /**
     * 根据给定的T、S和P获取相应的列表
     * @param conplanType    图的类型
     * @param conplanSubject 对应step的subject字段
     * @param conplanProcess 对应具体的process
     * @return 返回包含符合匹配的结果列表，封装在响应对象中
     */
    @RequestMapping("/TspList")
//    @RequiresPermissions("qcTools:template:list")
    public R templateList(@RequestParam String conplanType, @RequestParam Integer conplanSubject, @RequestParam Integer conplanProcess) {
        List<QcToolsConplanEntity> resultList = qcToolsConplanDao.getDataByTSP(conplanType, conplanSubject, conplanProcess);
        System.out.println("TSP resultList====>" + resultList);
        return R.ok().put("resultList", resultList);
    }


    /**
     * 保存用户自定义的QC图的数据
     * @param qcToolsConplanEntity 前端传递过来的数据实体
     * @return 保存成功标识
     */
    @RequestMapping("/save")
//    @RequiresPermissions("qcTools:conplan:save")
    public R save(@RequestBody QcToolsConplanEntity qcToolsConplanEntity) {
//
//        System.out.println("=========qc=======tools========conplan========entity");
//        System.out.println(qcToolsConplanEntity);
        //判断一下当前记录是否存在
        String conplanType = qcToolsConplanEntity.getConplanType();
        Integer conplanSubject = qcToolsConplanEntity.getConplanSubject();
        Integer conplanProcess = qcToolsConplanEntity.getConplanProcess();
        List<QcToolsConplanEntity> dataByTSP = qcToolsConplanDao.getDataByTSP(conplanType, conplanSubject, conplanProcess);

        //存在则进行update
        if (dataByTSP.size() != 0) {
            Integer conplanId = dataByTSP.get(0).getConplanId();
            qcToolsConplanEntity.setConplanId(conplanId);
            qcToolsConplanService.updateById(qcToolsConplanEntity);
        } else {
            //否则进行save
            qcToolsConplanService.save(qcToolsConplanEntity);
        }


        return R.ok();
    }

    /**
     * 修改更新 update
     * 按照指定 step的subject+process+type
     * 进行更新对应的数据记录行
     */
//    @RequestMapping("/update")
//    @RequiresPermissions("qcTools:template:update")
//    public R update(@RequestBody QcToolsConplanEntity qcToolsConplanEntity){
//
//        return R.ok();
//    }

//    /**
//     * TODO 删除
//     */
//    @RequestMapping("/delete")
//    @RequiresPermissions("qcTools:template:delete")
//    public R delete(@RequestBody Integer[] templateIds){
//		qcToolsTemplateService.removeByIds(Arrays.asList(templateIds));
//        return R.ok();
//    }

}
