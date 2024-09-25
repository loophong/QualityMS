package io.renren.modules.qcManagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.renren.modules.qcManagement.dao.QcStepDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.qcManagement.entity.QcStepEntity;
import io.renren.modules.qcManagement.service.QcStepService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.security.auth.Subject;


/**
 *
 *
 * @author hong
 * @email ${email}
 * @date 2024-08-25 14:45:45
 */
@Slf4j
@RestController
@RequestMapping("qcPlan/step")
public class QcStepController {
    @Autowired
    private QcStepService qcStepService;

    @Autowired
    private QcStepDao qcStepDao;

    /**
     * 根据课题id获取参数
     */
    @RequestMapping("/idList")
    @RequiresPermissions("qcPlan:step:list")
    public R listById(@RequestParam Map<String, Object> params){
        Object keyValue = params.get("key");
        if (keyValue instanceof String) {
            try {
                Integer subjectID = Integer.valueOf((String) keyValue);
                List<QcStepEntity> idList = qcStepDao.getStepListById(subjectID);
                return R.ok().put("idList", idList);
            } catch (NumberFormatException e) {
                return R.error("Invalid 'key' parameter provided. It must be an integer.");
            }
        } else {
            return R.error("Invalid 'key' parameter provided. It must be an integer.");
        }
    }

    /**
     * 根据课题id重用步骤
     */
    @RequestMapping("/reuse")
    @RequiresPermissions("qcPlan:step:list")
    public R reuse(@RequestParam Map<String, Object> params){
        Object keyValue = params.get("subjectID");
        Object reuseStepIdValue = params.get("reuseStepId");
        if (keyValue instanceof String) {
            try {
                Integer subjectID = Integer.valueOf((String) keyValue);
                Integer reuseStepId = Integer.valueOf((String) reuseStepIdValue);

                List<QcStepEntity> reuseList = qcStepDao.getStepListById(subjectID);

                log.info("遍历列表并修改每个元素的"+reuseList.toString());
                // 遍历列表并修改每个元素的 stepSubjectId
                for (QcStepEntity entity : reuseList) {
                    entity.setStepSubjectId(reuseStepId);
                    entity.setStepId(null);
                }
//                for (QcStepEntity entity : reuseList) {
//                    qcStepService.updateById(entity);
//                }

                for (QcStepEntity entity : reuseList) {
                    qcStepService.save(entity);
                }
                return R.ok();
            } catch (NumberFormatException e) {
                return R.error("Invalid 'key' parameter provided. It must be an integer.");
            }
        } else {
            return R.error("Invalid 'key' parameter provided. It must be an integer.");
        }
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qcPlan:step:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = qcStepService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{stepId}")
    @RequiresPermissions("qcPlan:step:info")
    public R info(@PathVariable("stepId") Integer stepId){
		QcStepEntity qcStep = qcStepService.getById(stepId);

        return R.ok().put("qcStep", qcStep);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qcPlan:step:save")
    public R save(@RequestBody QcStepEntity qcStep){
		qcStepService.save(qcStep);
        return R.ok().put("id", qcStep.getStepId());
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qcPlan:step:update")
    public R update(@RequestBody QcStepEntity qcStep){
		qcStepService.updateById(qcStep);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qcPlan:step:delete")
    public R delete(@RequestBody Integer[] stepIds){
		qcStepService.removeByIds(Arrays.asList(stepIds));

        return R.ok();
    }

}
