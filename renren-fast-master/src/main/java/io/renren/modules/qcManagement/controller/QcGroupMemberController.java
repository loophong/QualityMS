package io.renren.modules.qcManagement.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.service.QcGroupMemberService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * QC小组成员信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 10:16:22
 */
@RestController
@RequestMapping("qcMembers/qcGroupMember")
public class QcGroupMemberController {
    @Autowired
    private QcGroupMemberService qcGroupMemberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qcMembers:qcGroupMember:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = qcGroupMemberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{qcgmId}")
    @RequiresPermissions("qcMembers:qcGroupMember:update")
    public R info(@PathVariable("qcgmId") Long qcgmId){
		QcGroupMemberEntity qcGroupMember = qcGroupMemberService.getById(qcgmId);

        return R.ok().put("qcGroupMember", qcGroupMember);
    }

    /**
     * 保存/新增
     */
    @RequestMapping("/save")
    @RequiresPermissions("qcMembers:qcGroupMember:save")
    public R save(@RequestBody QcGroupMemberEntity qcGroupMember){
		qcGroupMemberService.save(qcGroupMember);
        return R.ok().put("id", qcGroupMember.getQcgmId());
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qcMembers:qcGroupMember:update")
    public R update(@RequestBody QcGroupMemberEntity qcGroupMember){
		qcGroupMemberService.updateById(qcGroupMember);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qcMembers:qcGroupMember:delete")
    public R delete(@RequestBody Long[] qcgmIds){
		qcGroupMemberService.removeByIds(Arrays.asList(qcgmIds));

        return R.ok();
    }

}
