package io.renren.modules.qcManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.indicator.entity.IndicatorDataDictionaryEntity;
import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import io.renren.modules.qcManagement.dao.QcGroupMemberDao;
import io.renren.modules.qcManagement.dao.QcSubjectRegistrationDao;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;
import io.renren.modules.qcManagement.service.QcSubjectRegistrationService;
import org.springframework.stereotype.Service;

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
        log.info("param"+params.get("page")+"------"+ params.get("limit"));

        long p = Long.parseLong((String) params.get("page"));
        long l = Long.parseLong((String) params.get("limit"));
        Page<QcSubjectRegistrationEntity> page = new Page<QcSubjectRegistrationEntity>(p,l);

        Page<QcSubjectRegistrationEntity> result = qcSubjectRegistrationDao.selectFinishedSubjectList(page);
        log.info("result"+result);
        return new PageUtils(page);
    }

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
                new Query<QcSubjectRegistrationEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }
    @Override
    public PageUtils queryPageAbout(Map<String, Object> params) {

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
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String valueTwo = ShiroUtils.getUserEntity().getUsername();
        queryWrapper.lambda()
                .like(QcSubjectRegistrationEntity::getTeamNumberIds, valueTwo)
                .or()
                .eq(QcSubjectRegistrationEntity::getTopicConsultant, valueTwo);

        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageLead(Map<String, Object> params) {
        QueryWrapper<QcSubjectRegistrationEntity> queryWrapper = new QueryWrapper<>();
        String valueOne = ShiroUtils.getUserEntity().getUsername();

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
        queryWrapper.lambda()
                .eq(QcSubjectRegistrationEntity::getTopicLeader, valueOne);

        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

}
