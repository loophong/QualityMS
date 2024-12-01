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
 *
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
     * 根据S获取相应的列表
     * @param conplanSubject
     * @return
     */
    @RequestMapping("/SList")
    public R templateListS(@RequestParam Integer conplanSubject) {
        List<QcToolsConplanEntity> resultList = qcToolsConplanDao.getDataByS(conplanSubject);
        System.out.println("save === save ====");
        return R.ok().put("resultList", resultList);
    }

    /**
     * 根据T获取相应的列表
     * @param conplanType
     * @return
     */
    @RequestMapping("/TList")
    public R templateListS(@RequestParam String conplanType) {
        List<QcToolsConplanEntity> resultList = qcToolsConplanDao.getDataByT(conplanType);
        System.out.println("save === save ====");
        return R.ok().put("resultList", resultList);
    }

    /**
     * 根据Id获取对应的数据
     * @param conplanId
     * @return
     */
    @RequestMapping("/GetById")
    public R templateList(@RequestParam Integer conplanId) {
        System.out.println( "ID conplanId====>" + conplanId);
        List<QcToolsConplanEntity> resultList =  qcToolsConplanDao.getDataById(conplanId);
        System.out.println("ID resultList====>" + resultList);
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
        qcToolsConplanService.save(qcToolsConplanEntity);
        return R.ok();
    }

    /**
     * 修改更新 update
     * 按照指定 step的subject+process+type
     * 进行更新对应的数据记录行
     */
    @RequestMapping("/update")
//    @RequiresPermissions("qcTools:template:update")
    public R update(@RequestBody QcToolsConplanEntity qcToolsConplanEntity){
//        System.out.println("updateById qcToolsConplanEntity =="+qcToolsConplanEntity);
//        Integer conplanId = qcToolsConplanEntity.getConplanId();
//        System.out.println("updateById conplanId====>" + conplanId);
        boolean updateByIdResult = qcToolsConplanService.updateById(qcToolsConplanEntity);
//        System.out.println("updateByIdResult====>" + updateByIdResult);
        return R.ok();
    }

    /**
     * 单个删除QC示例
     * @param conplanId 前端传递过来的要删除的数据的ID
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("qcTools:template:delete")
    public R delete(@RequestBody Integer conplanId){
        qcToolsConplanService.removeById(conplanId);
        return R.ok();
    }


//    @RequestMapping("/delete")
////    @RequiresPermissions("qcTools:template:delete")
//    public R delete(@RequestBody Integer[] templateIds){
//        qcToolsConplanService.removeByIds(Arrays.asList(templateIds));
//        return R.ok();
//    }

}
