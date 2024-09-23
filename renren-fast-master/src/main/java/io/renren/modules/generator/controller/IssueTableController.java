package io.renren.modules.generator.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.generator.entity.IssueUtils;
import io.renren.modules.generator.service.IssueTableService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 问题表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 11:15:58
 */
@RestController
@RequestMapping("generator/issuetable")
public class IssueTableController {
    @Autowired
    private IssueTableService issueTableService;


    SimpleDateFormat saf = new SimpleDateFormat("/yyyy/MM/dd");

    private final String uploadDir = "C:/uploads"; // 请确保这个路径已存在或可写

    /**
     * 上传图片
     */
//    @Value("${file.upload-dir}")
    @PostMapping("/upload")
    @RequiresPermissions("generator:issuetable:update")
    @CrossOrigin(origins = "http://localhost:8001") // 只允许这个源
    public R uploadImage(@RequestParam("file") MultipartFile file , HttpServletRequest request) {
        String originName = file.getOriginalFilename();
        String format = saf.format(new Date());
        String realPath = request.getServletContext().getRealPath("/") + format;
        File floder = new File(realPath);
        if (!floder.exists()){
            floder.mkdirs();
        }
        String newName = UUID.randomUUID().toString()+".jpg";
        try {
            file.transferTo(new File(floder,newName));
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +"/renren-fast" + format+"/" + newName;
            System.out.println("获取的url"+url);
//            R.ok().put("data",url);
            return R.ok().put("data",url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return R.ok();
    }

    /**
     * 关闭相关任务
     */
    // 关闭相关任务
    @RequestMapping("/closeRelatedTasks/{issueId}")
    @RequiresPermissions("generator:issuetable:delete")
    public R closeRelatedTasks(@PathVariable("issueId") Long issueId) {
        return issueTableService.closeRelatedTasks(issueId); // 直接调用服务层方法
    }





    /**
     * 获取所有问题列表
     */
    @RequestMapping("/issuesid")
    @RequiresPermissions("generator:issuetable:list")
    public R list(){
        System.out.println("=====获取问题列表----开始");
        List<IssueTableEntity> issues = issueTableService.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("issueTable", issues);
        System.out.println(response);
        System.out.println("=====获取问题列表----结束");
        return R.ok().put("issues", issues);
    }

    /**
     * 获取用户信息
     */
    @RequestMapping("/useinfo")
    @RequiresPermissions("generator:issuetable:list")
    public R listuserinfo(){
        System.out.println("=====获取用户信息----开始");
        String userinfo = issueTableService.getuserinfo();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("issueTable", userinfo);
        System.out.println(response);
        System.out.println("=====获取用户信息----结束");
        return R.ok().put("userinfo", userinfo);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:issuetable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = issueTableService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{issueId}")
    @RequiresPermissions("generator:issuetable:info")
    public R info(@PathVariable("issueId") Integer issueId){
		IssueTableEntity issueTable = issueTableService.getById(issueId);

        return R.ok().put("issueTable", issueTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:issuetable:save")
    public R save(@RequestBody IssueTableEntity issueTable){
		issueTableService.save(issueTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:issuetable:update")
    public R update(@RequestBody IssueTableEntity issueTable){

        System.out.println("开始修改整改数据"+issueTable);
		issueTableService.updateById(issueTable);
        System.out.println("结束修改整改数据");
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:issuetable:delete")
    public R delete(@RequestBody Integer[] issueIds){
		issueTableService.removeByIds(Arrays.asList(issueIds));

        return R.ok();
    }

}
