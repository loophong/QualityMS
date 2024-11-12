package io.renren.modules.qcManagement.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.qcManagement.dao.QcGroupMemberDao;
import io.renren.modules.qcManagement.dao.QcStepDao;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.entity.QcStepEntity;
import io.renren.modules.qcManagement.service.QcGroupMemberService;
import io.renren.modules.sys.service.SysUserRoleService;
import io.renren.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.objects.Global.undefined;


/**
 * QC小组成员信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 10:16:22
 */
@Slf4j
@RestController
@RequestMapping("qcMembers/qcGroupMember")
public class QcGroupMemberController {
    @Autowired
    private QcGroupMemberService qcGroupMemberService;

    @Autowired
    private QcGroupMemberDao qcGroupMemberDao;

    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysUserService sysUserService;


    /**
     * QC小组活动率=有课题QC小组数/QC小组注册个数
     */
    @RequestMapping("/activityRate")
    @RequiresPermissions("qcMembers:qcGroupMember:list")
    public R activityRateList() {
        Integer registrationNumbers = qcGroupMemberDao.countRegistrationNumbers();
        Integer haveRegistrationNumbers = qcGroupMemberDao.countHaveRegistrationNumbers();
        Map<String, Object> result = new HashMap<>();
        result.put("registrationNumbers", registrationNumbers);
        result.put("haveRegistrationNumbers", haveRegistrationNumbers);
        return R.ok().put("result", result);
    }

    /**
     * QC小组普及率=QC小组注册人数/职工总数
     */
    @RequestMapping("/penetrationRate")
    @RequiresPermissions("qcMembers:qcGroupMember:list")
    public R penetrationRateList() {
        Integer registrationPeopleNumbers = qcGroupMemberDao.countMembersUnique();
        Map<String, Object> result = new HashMap<>();
        result.put("registrationPeopleNumbers", registrationPeopleNumbers);
        return R.ok().put("result", result);
    }

    /**
     * 注册号
     */
    @RequestMapping("/registration")
    @RequiresPermissions("qcMembers:qcGroupMember:list")
    public R registrationList() {
        Integer registrationNum = qcGroupMemberDao.registrationCount();
        Map<String, Object> result = new HashMap<>();
        result.put("registrationNum", registrationNum);
        return R.ok().put("result", result);
    }
    /**
     * 移交组长
     */
//    @RequestMapping("/change")
//    @RequiresPermissions("qcMembers:leader:change")
//    public R leaderChange(@RequestParam Map<String, Object> params){
//        QcGroupMemberEntity member= qcGroupMemberDao.selectMemberById((Integer) params.get("id"));
//        QcGroupMemberEntity parent= qcGroupMemberDao.selectMemberById((Integer) params.get("ParentId"));
//
//        return R.ok();
//    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qcMembers:qcGroupMember:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = qcGroupMemberService.queryPage(params);
        //获取delete_flag不为1的数据
        List<QcGroupMemberEntity> resultList = qcGroupMemberDao.myAllGroupMemberList();
        page.setList(resultList);
        page.setTotalCount(resultList.size());
        long id = ShiroUtils.getUserId();
        return R.ok().put("page", page);
    }

    /**
     * 我的列表
     */
    @RequestMapping("/myList")
    @RequiresPermissions("qcMembers:qcGroupMember:list")
    public R myList(@RequestParam Map<String, Object> params) {
//        PageUtils page = qcGroupMemberService.queryPage(params);
        String userName = ShiroUtils.getUserEntity().getUsername();

        PageUtils page = qcGroupMemberService.queryPage(params);
//        PageUtils page2 = qcGroupMemberService.myQueryPage(params,userName);
        //获取delete_flag不为1的数据
        List<QcGroupMemberEntity> resultList = qcGroupMemberDao.myAllGroupMemberList();
        page.setList(resultList);
        page.setTotalCount(resultList.size());
//        List<QcGroupMemberEntity> filterList = resultList.stream().filter(e -> e.getName().equals(userName)).collect(Collectors.toList());
        return R.ok().put("page", page).put("userName", userName);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{qcgmId}")
    @RequiresPermissions("qcMembers:qcGroupMember:update")
    public R info(@PathVariable("qcgmId") Long qcgmId) {
        QcGroupMemberEntity qcGroupMember = qcGroupMemberService.getById(qcgmId);

        return R.ok().put("qcGroupMember", qcGroupMember);
    }


    /**
     * @description: 新增小组，如果小组名称重复，则不允许写入，写入时修改用户权限
     * @param: qcGroupMember
     * @return: io.renren.common.utils.R
     * @author: hong
     * @date: 2024/11/11 10:42
     */
    @RequestMapping("/createGroup")
    @RequiresPermissions("qcMembers:qcGroupMember:save")
    public R createTeam(@RequestBody QcGroupMemberEntity qcGroupMember) {
        log.info("新增小组：" + qcGroupMember);
        // 判断小组是否存在，存在则拒绝新增
        int count = qcGroupMemberService.count(new LambdaUpdateWrapper<QcGroupMemberEntity>()
                .eq(QcGroupMemberEntity::getGroupName, qcGroupMember.getGroupName()));
        log.info("小组名称：" + qcGroupMember.getGroupName() + "，查询结果条数：" + count);
        if (count > 0) {
            return R.error("该小组已存在，请勿重复添加");
        } else {
            // 通过用户id获取用户姓名
            String username = sysUserService.getById(qcGroupMember.getUserId()).getUsername();
            qcGroupMember.setName(username);
            qcGroupMemberService.save(qcGroupMember);
            // 修改当前用户权限
            // 获取用户所属的角色列表
            List<Long> roleIdList = sysUserRoleService.queryRoleIdList(qcGroupMember.getUserId());
            log.info("修改前用户角色权限列表：" + roleIdList);
            // 判断当前传入的权限是否在权限列表中，如果不存在则添加
            boolean flag = false;
            for (Long aLong : roleIdList) {
                if (aLong == qcGroupMember.getMemberRole()) {
                    flag = true;
                    log.info("当前用户已拥有该权限，无需添加");
                }
            }
            if (!flag) {
                roleIdList.add(qcGroupMember.getMemberRole());
                log.info("修改后用户角色权限列表：" + roleIdList);
                sysUserRoleService.saveOrUpdate(qcGroupMember.getUserId(), roleIdList);
            }
        }
        return R.ok().put("id", qcGroupMember.getQcgmId());
    }


    @RequestMapping("/save")
    @RequiresPermissions("qcMembers:qcGroupMember:save")
    public R save(@RequestBody QcGroupMemberEntity qcGroupMember){
        Integer parentId = qcGroupMember.getParentId();
        String name=qcGroupMember.getName();
        if(parentId != null){
           Boolean result = qcGroupMemberDao.checkRepeat(parentId,name);
           if(result){
               return R.error("请检查是否有重复成员");
           }
        }

        qcGroupMemberService.save(qcGroupMember);
        return R.ok().put("id", qcGroupMember.getQcgmId());
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qcMembers:qcGroupMember:update")
    public R update(@RequestBody QcGroupMemberEntity qcGroupMember) {
        qcGroupMemberService.updateById(qcGroupMember);
        return R.ok();
    }

    /**
     * 逻辑删除
     */
    @RequestMapping("/updateLogic")
    @RequiresPermissions("qcMembers:qcGroupMember:update")
    public R updateLogic(@RequestBody QcGroupMemberEntity qcGroupMember) {
        if (qcGroupMember.getParentId() == null) {
            log.info("删除父节点：" + qcGroupMember.getQcgmId());
            log.info("getQcgmId：" + qcGroupMember.getQcgmId().intValue());
            List<QcGroupMemberEntity> qcChildrenList = qcGroupMemberDao.getChildrenListById(qcGroupMember.getQcgmId().intValue());
            try {
                for (QcGroupMemberEntity qcGroupMemberEntity : qcChildrenList) {
                    qcGroupMemberEntity.setDeleteFlag(1);
                    qcGroupMemberService.updateById(qcGroupMemberEntity);
                }
            } catch (Exception e) {
                log.error("删除子节点失败：" + e.getMessage());
            }
            qcGroupMemberService.updateById(qcGroupMember);
        } else {
            log.info("删除子节点：" + qcGroupMember.getQcgmId());
            qcGroupMemberService.updateById(qcGroupMember);
        }
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
