package io.renren.modules.qcManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.qcManagement.entity.QcExamineStatusEntity;
import io.renren.modules.qcManagement.entity.QcknowledgebaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import io.renren.modules.qcManagement.dao.QcGroupMemberDao;
import io.renren.modules.qcManagement.dao.QcSubjectRegistrationDao;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;
import io.renren.modules.qcManagement.service.QcSubjectRegistrationService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
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
    public PageUtils queryPageFinishedList(Map<String, Object> params) {
        log.info("param" + params.get("page") + "------" + params.get("limit"));

        long p = Long.parseLong((String) params.get("page"));
        long l = Long.parseLong((String) params.get("limit"));
        Page<QcknowledgebaseEntity> page = new Page<>(p, l);

        // 提取模糊查询参数
        String topicName = (String) params.get("topicName");
        String keywords = (String) params.get("keywords");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");

        try {
            // 执行分页查询
            List<QcknowledgebaseEntity> result = qcSubjectRegistrationDao.selectFinishedSubjectList(topicName, keywords,startDate,endDate);
            page.setRecords(result);
            page.setTotal(result.size());
            log.info("result" + page);
            return new PageUtils(page);
        } catch (Exception e) {
            log.error("查询出错: " + e.getMessage() + ", params: " + params, e);
            page.setTotal(0);
            return new PageUtils(page);
        }
    }

    public List<QcknowledgebaseEntity> queryFinishedList1(Map<String, Object> params) {
    log.info("param" + params.get("page") + "------" + params.get("limit"));

    // 提取模糊查询参数
    String topicName = (String) params.get("topicName");
    String keywords = (String) params.get("keywords");
    String startDate = (String) params.get("startDate");
    String endDate = (String) params.get("endDate");

    try {
        // 执行查询
        List<QcknowledgebaseEntity> result = qcSubjectRegistrationDao.selectFinishedSubjectList(topicName, keywords, startDate, endDate);
        log.info("result" + result);
        return result;
    } catch (Exception e) {
        log.error("查询出错: " + e.getMessage() + ", params: " + params, e);
        return Collections.emptyList(); // 返回空列表
    }
}
    @Override
    public void updateStorageFlagToZero(Long[] qcsrIds) {
            qcSubjectRegistrationDao.updateStorageFlagToZero(Arrays.asList(qcsrIds));
    }


    ;
//    @Override
//    public PageUtils queryPageFinishedList(Map<String, Object> params) {
//    log.info("param" + params.get("page") + "------" + params.get("limit"));
//
//    long p = Long.parseLong((String) params.get("page"));
//    long l = Long.parseLong((String) params.get("limit"));
//    Page<QcSubjectRegistrationEntity> page = new Page<>(p, l);
//
//    // 提取模糊查询参数
//    String topicName = (String) params.get("topicName");
//    String keywords = (String) params.get("keywords");
//
//    // 构建查询条件
//    QueryWrapper<QcSubjectRegistrationEntity> queryWrapper = new QueryWrapper<>();
//    if (topicName != null && !topicName.isEmpty()) {
//        queryWrapper.like("topic_name", topicName);
//    }
//    if (keywords != null && !keywords.isEmpty()) {
//        queryWrapper.like("keywords", keywords);
//    }
//
//    try {
//        // 执行查询
//        Page<QcSubjectRegistrationEntity> result = qcSubjectRegistrationDao.selectFinishedSubjectList(page);
//        log.info("result" + result);
//        return new PageUtils(result);
//    } catch (Exception e) {
//        log.error("查询出错: " + e.getMessage(), e);
//        return new PageUtils(page);
//    }
//}

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<QcSubjectRegistrationEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (key != null && !key.isEmpty()) {
            try {
                // 将 key 字符串解析为 Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> keyMap = objectMapper.readValue(key, Map.class);

                // 遍历 Map 并添加查询条件
                for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                    String field = entry.getKey();
                    String value = entry.getValue();

                    if (value != null && !value.isEmpty()) {
                        switch (field) {
                            case "topicName":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getTopicName, value);
                                break;
                            case "keywords":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getKeywords, value);
                                break;
                            case "join":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getTeamNumberIds, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params, "qcsr_Id", false),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageFilter(Map<String, Object> params) {
        QueryWrapper<QcSubjectRegistrationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(QcSubjectRegistrationEntity::getTopicReviewStatus,'3');
        String key = (String) params.get("key");
        if (key != null && !key.isEmpty()) {
            try {
                // 将 key 字符串解析为 Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> keyMap = objectMapper.readValue(key, Map.class);

                // 遍历 Map 并添加查询条件
                for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                    String field = entry.getKey();
                    String value = entry.getValue();

                    if (value != null && !value.isEmpty()) {
                        switch (field) {
                            case "topicName":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getTopicName, value);
                                break;
                            case "keywords":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getKeywords, value);
                                break;
                            case "join":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getTeamNumberIds, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params, "qcsr_Id", false),
                queryWrapper
        );
        return new PageUtils(page);
    }
    @Override
    public PageUtils queryPageAbout(Map<String, Object> params) {

        QueryWrapper<QcSubjectRegistrationEntity> queryWrapper = new QueryWrapper<>();
        String valueTwo = ShiroUtils.getUserEntity().getUsername();
        queryWrapper.lambda()
                .like(QcSubjectRegistrationEntity::getTeamNumberIds, valueTwo)
                .or()
                .eq(QcSubjectRegistrationEntity::getTopicConsultant, valueTwo);
        String key = (String) params.get("key");
        if (key != null && !key.isEmpty()) {
            try {
                // 将 key 字符串解析为 Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> keyMap = objectMapper.readValue(key, Map.class);

                // 遍历 Map 并添加查询条件
                for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                    String field = entry.getKey();
                    String value = entry.getValue();

                    if (value != null && !value.isEmpty()) {
                        switch (field) {
                            case "topicName":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getTopicName, value);
                                break;
                            case "keywords":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getKeywords, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params, "qcsr_Id", false),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageAboutAndLead(Map<String, Object> params) {

        QueryWrapper<QcSubjectRegistrationEntity> queryWrapper = new QueryWrapper<>();
        String valueTwo = ShiroUtils.getUserEntity().getUsername();
        queryWrapper.lambda()
                .and(wrapper -> wrapper.eq(QcSubjectRegistrationEntity::getTopicConsultant, valueTwo)
                        .or().like(QcSubjectRegistrationEntity::getTeamNumberIds, valueTwo)
                        .or().eq(QcSubjectRegistrationEntity::getTopicLeader, valueTwo));

        String key = (String) params.get("key");
        if (key != null && !key.isEmpty()) {
            try {
                // 将 key 字符串解析为 Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> keyMap = objectMapper.readValue(key, Map.class);

                // 遍历 Map 并添加查询条件
                for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                    String field = entry.getKey();
                    String value = entry.getValue();

                    if (value != null && !value.isEmpty()) {
                        switch (field) {
                            case "topicName":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getTopicName, value);
                                break;
                            case "keywords":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getKeywords, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params, "qcsr_Id", false),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageAboutFilter(Map<String, Object> params) {

        QueryWrapper<QcSubjectRegistrationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(QcSubjectRegistrationEntity::getTopicReviewStatus,'3');
        String valueTwo = ShiroUtils.getUserEntity().getUsername();
        queryWrapper.lambda()
                .like(QcSubjectRegistrationEntity::getTeamNumberIds, valueTwo)
                .or()
                .eq(QcSubjectRegistrationEntity::getTopicConsultant, valueTwo);
        String key = (String) params.get("key");
        if (key != null && !key.isEmpty()) {
            try {
                // 将 key 字符串解析为 Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> keyMap = objectMapper.readValue(key, Map.class);

                // 遍历 Map 并添加查询条件
                for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                    String field = entry.getKey();
                    String value = entry.getValue();

                    if (value != null && !value.isEmpty()) {
                        switch (field) {
                            case "topicName":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getTopicName, value);
                                break;
                            case "keywords":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getKeywords, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params, "qcsr_Id", false),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageAll(Map<String, Object> params) {

        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params),
                new QueryWrapper<QcSubjectRegistrationEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageLead(Map<String, Object> params) {
        QueryWrapper<QcSubjectRegistrationEntity> queryWrapper = new QueryWrapper<>();
        String valueOne = ShiroUtils.getUserEntity().getUsername();
        queryWrapper.lambda()
                .eq(QcSubjectRegistrationEntity::getTopicLeader, valueOne);
        String key = (String) params.get("key");
        if (key != null && !key.isEmpty()) {
            try {
                // 将 key 字符串解析为 Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> keyMap = objectMapper.readValue(key, Map.class);

                // 遍历 Map 并添加查询条件
                for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                    String field = entry.getKey();
                    String value = entry.getValue();
                    if (value != null && !value.isEmpty()) {
                        switch (field) {
                            case "topicName":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getTopicName, value);
                                break;
                            case "keywords":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getKeywords, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params, "qcsr_Id", false),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageLeadFilter(Map<String, Object> params) {
        QueryWrapper<QcSubjectRegistrationEntity> queryWrapper = new QueryWrapper<>();
        String valueOne = ShiroUtils.getUserEntity().getUsername();
        queryWrapper.lambda()
                .eq(QcSubjectRegistrationEntity::getTopicReviewStatus,'3');
        queryWrapper.lambda()
                .eq(QcSubjectRegistrationEntity::getTopicLeader, valueOne);

        String key = (String) params.get("key");
        if (key != null && !key.isEmpty()) {
            try {
                // 将 key 字符串解析为 Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> keyMap = objectMapper.readValue(key, Map.class);

                // 遍历 Map 并添加查询条件
                for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                    String field = entry.getKey();
                    String value = entry.getValue();
                    if (value != null && !value.isEmpty()) {
                        switch (field) {
                            case "topicName":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getTopicName, value);
                                break;
                            case "keywords":
                                queryWrapper.lambda().like(QcSubjectRegistrationEntity::getKeywords, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params, "qcsr_Id", false),
                queryWrapper
        );
        return new PageUtils(page);
    }


}