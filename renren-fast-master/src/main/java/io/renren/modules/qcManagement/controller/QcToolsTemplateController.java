package io.renren.modules.qcManagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.qcManagement.entity.QcToolsTemplateEntity;
import io.renren.modules.qcManagement.service.QcToolsTemplateService;
import io.renren.modules.qcManagement.dao.QcToolsTemplateDao;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-20 17:53:45
 */
@RestController
@RequestMapping("qcTools/template")
public class QcToolsTemplateController {
    @Autowired
    private QcToolsTemplateService qcToolsTemplateService;

    @Autowired
    private QcToolsTemplateDao qcToolsTemplateDao;
    /**
     * 根据类型名获取列表
     */
    @RequestMapping("/templateList")
    @RequiresPermissions("qcTools:template:list")
    public R templateList(@RequestParam String templateType){
        List<QcToolsTemplateEntity> resultList = qcToolsTemplateDao.getTemplateByName(templateType);
        return R.ok().put("resultList", resultList);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qcTools:template:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = qcToolsTemplateService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{templateId}")
    @RequiresPermissions("qcTools:template:info")
    public R info(@PathVariable("templateId") Integer templateId){
		QcToolsTemplateEntity qcToolsTemplate = qcToolsTemplateService.getById(templateId);

        return R.ok().put("qcToolsTemplate", qcToolsTemplate);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qcTools:template:save")
    public R save(@RequestBody QcToolsTemplateEntity qcToolsTemplate){
		qcToolsTemplateService.save(qcToolsTemplate);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qcTools:template:update")
    public R update(@RequestBody QcToolsTemplateEntity qcToolsTemplate){
		qcToolsTemplateService.updateById(qcToolsTemplate);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qcTools:template:delete")
    public R delete(@RequestBody Integer[] templateIds){
		qcToolsTemplateService.removeByIds(Arrays.asList(templateIds));

        return R.ok();
    }

}
