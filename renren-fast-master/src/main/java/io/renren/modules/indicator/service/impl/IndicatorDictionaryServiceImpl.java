package io.renren.modules.indicator.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.modules.indicator.entity.IndicatorResponseByClassificationEntity;
import io.renren.modules.indicator.entity.IndicatorResponseByDepartmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.indicator.dao.IndicatorDictionaryDao;
import io.renren.modules.indicator.entity.IndicatorDictionaryEntity;
import io.renren.modules.indicator.service.IndicatorDictionaryService;


@Service("indicatorDictionaryService")
public class IndicatorDictionaryServiceImpl extends ServiceImpl<IndicatorDictionaryDao, IndicatorDictionaryEntity> implements IndicatorDictionaryService {

    @Autowired
    private IndicatorDictionaryDao indicatorDictionaryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        System.out.println("params:" + params);
        String key = (String) params.get("key");
        QueryWrapper<IndicatorDictionaryEntity> queryWrapper = new QueryWrapper<>();

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
                            case "indicatorName":
                                queryWrapper.lambda().like(IndicatorDictionaryEntity::getIndicatorName, value);
                                break;
                            case "managementDepartment":
                                queryWrapper.lambda().like(IndicatorDictionaryEntity::getManagementDepartment, value);
                                break;
                            case "assessmentDepartment":
                                queryWrapper.lambda().like(IndicatorDictionaryEntity::getAssessmentDepartment, value);
                                break;
                            case "indicatorClassification":
                                queryWrapper.lambda().like(IndicatorDictionaryEntity::getIndicatorClassification, value);
                                break;
                            case "indicatorParentNode":
                                // 获取该节点及其所有子节点
                                List<String> allChildrenNodes = getAllChildrenNodes(value);
                                queryWrapper.lambda().in(IndicatorDictionaryEntity::getIndicatorParentNode, allChildrenNodes);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        queryWrapper.orderByDesc("`indicator_creat_time`");

        IPage<IndicatorDictionaryEntity> page = this.page(
                new Query<IndicatorDictionaryEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryIndicatorList(Map<String, Object> params) {
        IPage<IndicatorDictionaryEntity> page = this.page(
                new Query<IndicatorDictionaryEntity>().getPage(params),
                new QueryWrapper<IndicatorDictionaryEntity>()
        );

        return new PageUtils(page);
    }

    public List<IndicatorDictionaryEntity> getDictionaryEntitiesByName(String indicatorName) {
        QueryWrapper<IndicatorDictionaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("indicator_name", indicatorName);
        return indicatorDictionaryDao.selectList(queryWrapper);
    }

    // 根据部门统计指标数量
    @Override
    public List<IndicatorResponseByDepartmentEntity> countsByDepartmant(IndicatorResponseByDepartmentEntity indicatorResponseByDepartmentEntity) {
        return indicatorDictionaryDao.countsByDepartmant(indicatorResponseByDepartmentEntity);
    }

    // 根据指标分类统计指标数量
    @Override
    public List<IndicatorResponseByClassificationEntity> countsByClassification(IndicatorResponseByClassificationEntity indicatorResponseByClassificationEntity) {
        List<IndicatorResponseByClassificationEntity> list = indicatorDictionaryDao.countsByClassification(indicatorResponseByClassificationEntity);

        // 定义一个比较器
        Comparator<IndicatorResponseByClassificationEntity> classificationComparator = Comparator.comparing(
                entity -> {
                    switch (entity.getIndicatorClassification()) {
                        case "A":
                            return 1;
                        case "B":
                            return 2;
                        case "C":
                            return 3;
                        default:
                            return 4;
                    }
                }
        );

        // 使用比较器对列表进行排序
        List<IndicatorResponseByClassificationEntity> sortedList = list.stream()
                .sorted(classificationComparator)
                .collect(Collectors.toList());

        return sortedList;
    }

    // 递归方法，获取某个节点及其所有子节点
    private List<String> getAllChildrenNodes(String parentNodeName) {
        List<String> allChildrenNodes = new ArrayList<>();
        allChildrenNodes.add(parentNodeName); // 包括根节点本身

        List<IndicatorDictionaryEntity> children = this.list(new QueryWrapper<IndicatorDictionaryEntity>()
                .lambda()
                .eq(IndicatorDictionaryEntity::getIndicatorParentNode, parentNodeName));

        for (IndicatorDictionaryEntity child : children) {
            allChildrenNodes.addAll(getAllChildrenNodes(child.getIndicatorName()));
        }

        return allChildrenNodes;
    }

}