package io.renren.modules.qcManagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 12:37:20
 */
public interface QcSubjectRegistrationService extends IService<QcSubjectRegistrationEntity> {


    List<QcGroupMemberEntity> getMembersOfGroup(String groupName);
    PageUtils queryPage(Map<String, Object> params);
    PageUtils queryPageAbout(Map<String, Object> params);
    PageUtils queryPageLead(Map<String, Object> params);
    PageUtils queryPageFinishedList(Map<String, Object> params) ;

}

