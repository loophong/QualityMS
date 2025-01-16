package io.renren.modules.qcManagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.notice.entity.CreateNoticeParams;
import io.renren.modules.notice.service.MessageNotificationService;
import io.renren.modules.qcManagement.dao.QcExamineStatusDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.qcManagement.entity.QcExamineStatusEntity;
import io.renren.modules.qcManagement.service.QcExamineStatusService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author
 * @email ${email}
 * @date 2024-08-27 14:22:12
 */
@Slf4j
@RestController
@RequestMapping("qcManagement/examineStatus")
public class QcExamineStatusController {
    @Autowired
    private QcExamineStatusService qcExamineStatusService;
    @Autowired
    private QcExamineStatusDao qcExamineStatusDao;
    @Autowired
    private MessageNotificationService messageService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qcManagement:examineStatus:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = qcExamineStatusService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{qcExamineId}")
    @RequiresPermissions("qcManagement:examineStatus:info")
    public R info(@PathVariable("qcExamineId") Integer qcExamineId){
		QcExamineStatusEntity qcExamineStatus = qcExamineStatusService.getById(qcExamineId);

        return R.ok().put("qcExamineStatus", qcExamineStatus);
    }

    /**
     * 根据课题id判断审核步骤是否存在
     */
    @RequestMapping("/subjectInfo/{qcExamineSubject}")
    @RequiresPermissions("qcManagement:examineStatus:info")
    public R infoSubject(@PathVariable("qcExamineSubject") Integer qcExamineSubject){
        Boolean ifExist = qcExamineStatusDao.existsBySubject(qcExamineSubject);
        return R.ok().put("ifExist", ifExist);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qcManagement:examineStatus:save")
    public R save(@RequestBody QcExamineStatusEntity qcExamineStatus){
        qcExamineStatusService.saveOrUpdateBySubject(qcExamineStatus);
		qcExamineStatusService.save(qcExamineStatus);
        //发送消息
//        messageService.sendMessages(new CreateNoticeParams());
        return R.ok().put("id", qcExamineStatus.getQcExamineId());
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qcManagement:examineStatus:update")
    public R update(@RequestBody QcExamineStatusEntity qcExamineStatus){
		qcExamineStatusService.updateById(qcExamineStatus);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qcManagement:examineStatus:delete")
    public R delete(@RequestBody Integer[] qcExamineIds){
		qcExamineStatusService.removeByIds(Arrays.asList(qcExamineIds));

        return R.ok();
    }

}
