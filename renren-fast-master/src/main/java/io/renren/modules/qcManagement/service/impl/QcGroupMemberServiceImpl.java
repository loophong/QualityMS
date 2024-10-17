package io.renren.modules.qcManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.qcManagement.dao.QcGroupMemberDao;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.service.QcGroupMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("qcGroupMemberService")
public class QcGroupMemberServiceImpl extends ServiceImpl<QcGroupMemberDao, QcGroupMemberEntity> implements QcGroupMemberService {
    @Autowired
    QcGroupMemberDao qcGroupMemberDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QcGroupMemberEntity> page = this.page(
                new Query<QcGroupMemberEntity>().getPage(params),
                new QueryWrapper<QcGroupMemberEntity>()
        );
//        log.info("记录长这样："+page.getRecords().toString());
        return new PageUtils(page);
    }

    @Override
    public PageUtils myQueryPage(Map<String, Object> params,String username) {
        IPage<QcGroupMemberEntity> page = this.page(
                new Query<QcGroupMemberEntity>().getPage(params),
                new QueryWrapper<QcGroupMemberEntity>()
        );
        log.info("记录："+page.getRecords().toString());

        try {
            List<QcGroupMemberEntity> resultList = page.getRecords().stream().filter(e -> e.getName().equals(username)).collect(Collectors.toList());
            page.setRecords(resultList);
//            // 创建一个新的结果列表用于存储最终结果
//            List<QcGroupMemberEntity> finalResultList = new ArrayList<>(resultList);
//
//            // 遍历 resultList 并根据 parentId 或 qcgmId 查找相应的记录
//            for (QcGroupMemberEntity entity : resultList) {
//                int parentId = entity.getParentId();
//
//                if (parentId.exists()) {
//                    // 查找 parentId 相同的元素
//                    QcGroupMemberEntity matchingRecord = page.getRecords().stream()
//                            .filter(e -> e.getParentId().equals(parentId))
//
//
//
//                    if (matchingRecord != null && !finalResultList.contains(matchingRecord)) {
//                        finalResultList.add(matchingRecord);
//                    }
//                } else { // 如果 parentId 为空，则查找 parentId 等于 qcgmId 的记录
//                    Long qcgmId = entity.getQcgmId();
//                    QcGroupMemberEntity matchingRecord = allRecords.stream()
//                            .filter(e -> e.getParentId().equals(qcgmId))
//                            .findFirst()
//                            .orElse(null);
//
//                    if (matchingRecord != null && !finalResultList.contains(matchingRecord)) {
//                        finalResultList.add(matchingRecord);
//                    }
//                }
//            }

//            List<QcGroupMemberEntity> aboutMeList = qcGroupMemberDao.aboutMeList(resultList.getParentId());
//            List<QcGroupMemberEntity> aboutMeList
        } catch (Exception e) {
            System.err.println("自定义查询出错: " + e.getMessage());
            page.setTotal(0);
        }
        return new PageUtils(page);
    }

}
