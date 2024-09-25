package io.renren.modules.qcManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import io.renren.modules.qcManagement.dao.QcGroupMemberDao;
import io.renren.modules.qcManagement.dao.QcSubjectRegistrationDao;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;
import io.renren.modules.qcManagement.service.QcSubjectRegistrationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("qcSubjectRegistrationService")
public class QcSubjectRegistrationServiceImpl extends ServiceImpl<QcSubjectRegistrationDao, QcSubjectRegistrationEntity> implements QcSubjectRegistrationService {
    @Autowired
    private QcSubjectRegistrationDao  qcSubjectRegistrationDao;

    @Override
    public List<QcGroupMemberEntity> getMembersOfGroup(String groupName) {
//        System.out.println("根据组名获得的成员" + baseMapper.getMembersOfGroup(groupName));
         return baseMapper.getMembersOfGroup(groupName);
    }



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params),
                new QueryWrapper<QcSubjectRegistrationEntity>()
        );
        try {
            // 模糊查询
            List<QcSubjectRegistrationEntity> result = qcSubjectRegistrationDao.fuzzyQueryList((String) params.get("topicName"), (String) params.get("keywords"));
            page.setRecords(result);
            page.setTotal(result.size());
        } catch (Exception e) {
            System.err.println("自定义查询出错: " + e.getMessage());
            page.setTotal(0);
        }

        return new PageUtils(page);
    }


}
