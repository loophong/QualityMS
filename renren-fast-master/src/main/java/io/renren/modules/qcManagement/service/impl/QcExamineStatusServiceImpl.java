package io.renren.modules.qcManagement.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.qcManagement.dao.QcExamineStatusDao;
import io.renren.modules.qcManagement.entity.QcExamineStatusEntity;
import io.renren.modules.qcManagement.service.QcExamineStatusService;


@Service("qcExamineStatusService")
public class QcExamineStatusServiceImpl extends ServiceImpl<QcExamineStatusDao, QcExamineStatusEntity> implements QcExamineStatusService {

    private final QcExamineStatusDao qcExamineStatusDao;

    public QcExamineStatusServiceImpl(QcExamineStatusDao qcExamineStatusDao) {
        this.qcExamineStatusDao = qcExamineStatusDao;
    }

    @Override
    public boolean saveOrUpdateBySubject(QcExamineStatusEntity qcExamineStatus) {
        Integer qcExamineSubject = qcExamineStatus.getQcExamineSubject();
        System.out.println("是否有需要删除的: "+qcExamineStatusDao.existsBySubject(qcExamineSubject));
        if (qcExamineStatusDao.existsBySubject(qcExamineSubject)) {
            // 如果存在，则删除 qcExamineSubject 相同的记录
            System.out.println("待删除的ID: " + qcExamineSubject);
            qcExamineStatusDao.deleteBySubject(qcExamineSubject);
        }

        return true;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QcExamineStatusEntity> page = this.page(
                new Query<QcExamineStatusEntity>().getPage(params),
                new QueryWrapper<QcExamineStatusEntity>()
        );

        return new PageUtils(page);
    }

}
