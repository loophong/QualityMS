package io.renren.modules.qcManagement.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;
import io.renren.modules.qcManagement.service.QcSubjectRegistrationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 12:37:20
 */
@RestController
@RequestMapping("qcSubject/registration")
public class QcSubjectRegistrationController {
    @Autowired
    private QcSubjectRegistrationService qcSubjectRegistrationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qcSubject:registration:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = qcSubjectRegistrationService.queryPage(params);

        return R.ok().put("page", page);
    }

//    /**
//     * 根据组名获得成员
//     */
//    @RequestMapping("/getMembers")
//    @RequiresPermissions("qcSubject:registration:list")
//    public R getMembers(@RequestParam String groupName){
//        List<QcGroupMemberEntity> qcGroupMemberEntityList = qcSubjectRegistrationService.getMembersOfGroup(groupName);
//        return R.ok().put("成员",qcGroupMemberEntityList);
//    }

    /**
     * 信息
     */
    @RequestMapping("/info/{qcsrId}")
    @RequiresPermissions("qcSubject:registration:update")
    public R info(@PathVariable("qcsrId") Long qcsrId){
		QcSubjectRegistrationEntity qcSubjectRegistration = qcSubjectRegistrationService.getById(qcsrId);
        return R.ok().put("qcSubjectRegistration", qcSubjectRegistration);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qcSubject:registration:save")
    public R save(@RequestBody QcSubjectRegistrationEntity qcSubjectRegistration){
		qcSubjectRegistrationService.save(qcSubjectRegistration);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qcSubject:registration:update")
    public R update(@RequestBody QcSubjectRegistrationEntity qcSubjectRegistration){
		qcSubjectRegistrationService.updateById(qcSubjectRegistration);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qcSubject:registration:delete")
    public R delete(@RequestBody Long[] qcsrIds){
		qcSubjectRegistrationService.removeByIds(Arrays.asList(qcsrIds));

        return R.ok();
    }

    /**
     * 获取提交
     */
    @RequestMapping("/getResult")
    @RequiresPermissions("qcSubject:registration:list")
        public R getResult(@PathVariable("qcsrId") Long qcsrId){
            QcSubjectRegistrationEntity qcSubjectRegistration = qcSubjectRegistrationService.getById(qcsrId);
            return R.ok().put("resultType", qcSubjectRegistration.getResultType());
        }
    }


