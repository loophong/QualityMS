package io.renren.modules.indicator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.indicator.entity.IndicatorKeyIndicatorsEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-21 18:47:20
 */
public interface IndicatorKeyIndicatorsService extends IService<IndicatorKeyIndicatorsEntity> {

    PageUtils queryPage(Map<String, Object> params);

//    PageUtils queryPageFinishedList(Map<String, Object> params);

    PageUtils queryPage1(Map<String, Object> params);


    List<IndicatorKeyIndicatorsEntity> queryList(Map<String, Object> params);


    boolean clearStorageFlag(List<Integer> ids);
}



