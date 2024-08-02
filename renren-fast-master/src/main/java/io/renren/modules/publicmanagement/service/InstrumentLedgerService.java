package io.renren.modules.publicmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.publicmanagement.entity.InstrumentLedgerEntity;

import java.util.Map;

/**
 * 仪器台账表
 *
 * @author hong
 * @email ${email}
 * @date 2024-08-02 13:37:30
 */
public interface InstrumentLedgerService extends IService<InstrumentLedgerEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

