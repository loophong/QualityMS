package io.renren.modules.spc.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.spc.entity.SpcXrchartEntity;
import io.renren.modules.spc.service.SpcXrchartService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-27 10:49:53
 */
@RestController
@RequestMapping("spc/spcxrchart")
public class SpcXrchartController {
    @Autowired
    private SpcXrchartService spcXrchartService;

    private static final String uploadDir = "D:/your/upload/directory/"; // 请确保这个路径已存在或可写
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("spc:spcxrchart:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spcXrchartService.queryPage(params);

        System.out.println(page);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{spcxrId}")
    @RequiresPermissions("spc:spcxrchart:info")
    public R info(@PathVariable("spcxrId") Long spcxrId){
		SpcXrchartEntity spcXrchart = spcXrchartService.getById(spcxrId);

        return R.ok().put("spcXrchart", spcXrchart);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("spc:spcxrchart:save")
    public R save(@RequestBody SpcXrchartEntity spcXrchart){
		spcXrchartService.save(spcXrchart);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("spc:spcxrchart:update")
    public R update(@RequestBody SpcXrchartEntity spcXrchart){
		spcXrchartService.updateById(spcXrchart);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("spc:spcxrchart:delete")
    public R delete(@RequestBody Long[] spcxrIds){
		spcXrchartService.removeByIds(Arrays.asList(spcxrIds));

        return R.ok();
    }


    @RequestMapping("/chart/Xbar_R")
    public R getXbar_RChart(@RequestParam String tableName){
        System.out.println(tableName);
        System.out.println("======");
        System.out.println(spcXrchartService.getXbar_RChart(tableName));
        return R.ok().put("Xbar-R_Info", spcXrchartService.getXbar_RChart(tableName));
    }

    @RequestMapping("/chart/Xbar_S")
    public R getXbar_SChart(@RequestParam String tableName){
        System.out.println(tableName);
        return R.ok().put("Xbar-S_Info", spcXrchartService.getXbar_SChart(tableName));
    }

    @RequestMapping("/chart/Me_R")
    public R getMe_RChart(@RequestParam String tableName){
        System.out.println(tableName);
        return R.ok().put("Me-R_Info", spcXrchartService.getMe_RChart(tableName));
    }

    @RequestMapping("/chart/I_MR")
    public R getI_MRChart(@RequestParam String tableName){
        System.out.println(tableName);
        return R.ok().put("I-MR_Info", spcXrchartService.getI_MRChart(tableName));
    }

    @RequestMapping("/options")
    public R getTableName(){
        return R.ok().put("table_name",spcXrchartService.getTableName());
    }

    /**
     * 文件获取
     */
    @RequestMapping("/{flag}")
    public void downloadFile(@PathVariable String flag, HttpServletResponse response) throws Exception {

        // 检查并确保上传目录存在
        if (!FileUtil.isDirectory(uploadDir)) {
            FileUtil.mkdir(uploadDir);
        }

        // 获取上传目录下的文件名列表
        List<String> fileNames = FileUtil.listFileNames(uploadDir);
        String targetFileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");

        try {
            if (StrUtil.isNotEmpty(targetFileName)) {
                // 获取文件的扩展名，判断文件类型
                String fileType = FileUtil.extName(targetFileName).toLowerCase();

                // 根据文件类型设置合适的 Content-Type
                switch (fileType) {
                    case "jpg":
                    case "jpeg":
                        response.setContentType("image/jpeg");
                        break;
                    case "png":
                        response.setContentType("image/png");
                        break;
                    case "gif":
                        response.setContentType("image/gif");
                        break;
                    case "pdf":
                        response.setContentType("application/pdf");
                        break;
                    case "xlsx":
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        break;
                    default:
                        response.setContentType("application/octet-stream");
                        break;
                }

                // 设置响应头以下载文件
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(targetFileName, "UTF-8"));

                // 读取文件并写入响应
                byte[] bytes = FileUtil.readBytes(uploadDir + targetFileName);
                System.out.println("---"+bytes);
                OutputStream os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
                return;
            } else {
                // 文件未找到的处理逻辑
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("File not found");
                return;
            }
        } catch (Exception e) {
            System.out.println("文件下载失败: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("File download failed");
        }
    }
}
