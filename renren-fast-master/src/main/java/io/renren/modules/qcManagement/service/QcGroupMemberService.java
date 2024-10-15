package io.renren.modules.qcManagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * QC小组成员信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 10:16:22
 */
public interface QcGroupMemberService extends IService<QcGroupMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);


    PageUtils myQueryPage(Map<String, Object> params,String username);


}

