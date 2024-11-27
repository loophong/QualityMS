package io.renren.modules.qcManagement.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.qcManagement.dao.QcExamineStatusDao;
import io.renren.modules.qcManagement.dao.QcSubjectRegistrationDao;
import io.renren.modules.qcManagement.entity.QcExamineStatusEntity;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;
import io.renren.modules.qcManagement.entity.QcknowledgebaseEntity;
import io.renren.modules.qcManagement.service.QcSubjectRegistrationService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 12:37:20
 */
@Slf4j
@RestController
@RequestMapping("qcSubject/registration")
public class QcSubjectRegistrationController {
    @Autowired
    private QcSubjectRegistrationService qcSubjectRegistrationService;

    @Autowired
    private QcSubjectRegistrationDao  qcSubjectRegistrationDao;

    @Autowired
    private QcExamineStatusDao  qcExamineStatusDao;
    /**
     * 点检统计
     */
    @RequestMapping("/count")
    @RequiresPermissions("qcSubject:registration:list")
    public R activityRateList(){
        //已注册课题数
        Integer countRegistration = qcSubjectRegistrationDao.countRegistration();
        //已提交课题数
        Integer countSubmitted = qcSubjectRegistrationDao.countSubmitted();
        //已提交未审核完课题数
        Integer countWithoutExamined = qcExamineStatusDao.countWithoutExamined();
        //已完成课题数
        Integer countExamined = qcExamineStatusDao.countExamined();
        Map<String, Object> result = new HashMap<>();
        result.put("countRegistration", countRegistration);
        result.put("countSubmitted", countSubmitted);
        result.put("countWithoutExamined", countWithoutExamined);
        result.put("countExamined", countExamined);
        return R.ok().put("count", result);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qcSubject:registration:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = qcSubjectRegistrationService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 大屏展示全部列表
     */
    @RequestMapping("/all")
//    @RequiresPermissions("qcSubject:registration:list")
    public R listAll(@RequestParam Map<String, Object> params){
        PageUtils page = qcSubjectRegistrationService.queryPageAll(params);
        return R.ok().put("page", page);
    }

    /**
     * 统计各个活动状态课题数
     */
    @RequestMapping("/activityList")
    @RequiresPermissions("qcSubject:registration:list")
    public R activityList(@RequestParam Map<String, Object> params){
        PageUtils page = qcSubjectRegistrationService.queryPage(params);
//        for(int i=0;i<10;i++){
//            qcSubjectRegistrationDao.getActivity("1","创新型");
//        }
        return R.ok().put("page", page);
    }

    /**
     * 我开展的列表
     */
    @RequestMapping("/leadList")
    @RequiresPermissions("qcSubject:registration:list")
    public R myListLeader(@RequestParam Map<String, Object> params){
        log.info("我开展："+(String) params.get("key"));
        PageUtils page = qcSubjectRegistrationService.queryPageLead(params);
        return R.ok().put("page", page);
    }


    /**
     * 我参与的列表
     */
    @RequestMapping("/myList")
    @RequiresPermissions("qcSubject:registration:list")
    public R myList(@RequestParam Map<String, Object> params){
        log.info("我参与："+(String) params.get("key"));
        PageUtils page = qcSubjectRegistrationService.queryPageAbout(params);
        return R.ok().put("page", page);
    }
    /**
     * 获取当前登录用户名
     */
    @RequestMapping("/user")
    @RequiresPermissions("qcSubject:registration:list")
    public R getUerName(){
        String userName= ShiroUtils.getUserEntity().getUsername();
        return R.ok().put("userName",userName);
    }
    /**
     * 课题名称重复性检查
     */
    @RequestMapping("/exist")
    @RequiresPermissions("qcSubject:registration:list")
    public R existName(@RequestParam String topicName){
        boolean exist=qcSubjectRegistrationDao.ifExistSubjectName(topicName);
        return R.ok().put("exist",exist);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{qcsrId}")
    @RequiresPermissions("qcSubject:registration:update")
    public R info(@PathVariable("qcsrId") Long qcsrId){
		QcSubjectRegistrationEntity qcSubjectRegistration = qcSubjectRegistrationService.getById(qcsrId);
        String userName= ShiroUtils.getUserEntity().getUsername();
        return R.ok().put("qcSubjectRegistration", qcSubjectRegistration).put("userName",userName);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qcSubject:registration:save")
    public R save(@RequestBody QcSubjectRegistrationEntity qcSubjectRegistration){
        Integer tmpId = qcSubjectRegistrationDao.maxOfId();
        // 获取当前年份
        Date currentDate = new Date();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String currentYear = yearFormat.format(currentDate);
        String topicNumber = "PJHLQCKT-"+currentYear +"-"+(tmpId+1);
        log.info("课题编号:"+topicNumber);
        qcSubjectRegistration.setTopicNumber(topicNumber);
        qcSubjectRegistrationService.save(qcSubjectRegistration);
        return R.ok().put("id", qcSubjectRegistration.getQcsrId());
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

    /**
     * QC知识库
     */

    @RequestMapping("/finishedList")
    @RequiresPermissions("qcSubject:registration:list")
    public R finishedList(@RequestParam Map<String, Object> params){
        PageUtils page =  qcSubjectRegistrationService.queryPageFinishedList(params);
        return R.ok().put("page", page);
    }
    @RequestMapping("/finishedList01")
    @RequiresPermissions("qcSubject:registration:list")
    public List<QcknowledgebaseEntity>list1(@RequestParam Map<String, Object> params){
        List<QcknowledgebaseEntity> list = qcSubjectRegistrationService.queryFinishedList1(params);
         return list;
    }
}


