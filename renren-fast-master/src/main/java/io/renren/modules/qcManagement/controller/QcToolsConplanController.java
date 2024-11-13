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
     * 根据T S P获取列表
     * 对应step的 type+subject+process
     */
    @RequestMapping("/TspList")
//    @RequiresPermissions("qcTools:template:list")
    public R templateList(@RequestParam String conplanType, @RequestParam Integer conplanSubject, @RequestParam Integer conplanProcess) {
        List<QcToolsConplanEntity> resultList = qcToolsConplanDao.getDataByTSP(conplanType, conplanSubject, conplanProcess);
        System.out.println("TSP resultList====>" + resultList);
        return R.ok().put("resultList", resultList);
    }
//
//
    /**
     * 列表
     * no use
     */
//    @RequestMapping("/listAll")
////    @RequiresPermissions("qcTools:template:list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = qcToolsConplanService.queryPage(params);
//        return R.ok().put("page", page);
//    }
//
//
    /**
     * 指定信息查看
     */
//    @RequestMapping("/info/{templateId}")
////    @RequiresPermissions("qcTools:template:info")
//    public R info(@PathVariable("templateId") Integer templateId){
////		QcToolsTemplateEntity qcToolsTemplate = qcToolsTemplateService.getById(templateId);
//
//        return R.ok().put("qcToolsTemplate", "ok");
//    }
//

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("qcTools:conplan:save")
    public R save(@RequestBody QcToolsConplanEntity qcToolsConplanEntity) {

        System.out.println("=========qc=======tools========conplan========entity");
        System.out.println(qcToolsConplanEntity);
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
//
    /**
     * 修改更新
     * 按照指定 step的subject+process+type
     * 进行更新对应的数据记录行
     */
//    @RequestMapping("/update")
//    @RequiresPermissions("qcTools:template:update")
//    public R update(@RequestBody QcToolsConplanEntity qcToolsConplanEntity){
//
//        return R.ok();
//    }
//
//    /**
//     * TODO 删除
//     */
//    @RequestMapping("/delete")
//    @RequiresPermissions("qcTools:template:delete")
//    public R delete(@RequestBody Integer[] templateIds){
//		qcToolsTemplateService.removeByIds(Arrays.asList(templateIds));
//
//        return R.ok();
//    }

}
