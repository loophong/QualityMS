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

import io.renren.modules.spc.entity.SpcPchartEntity;
import io.renren.modules.spc.service.SpcPchartService;
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
@RequestMapping("spc/spcpchart")
public class SpcPchartController {
    @Autowired
    private SpcPchartService spcPchartService;


    private static final String uploadDir = "D:/your/upload/directory/"; // 请确保这个路径已存在或可写

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("spc:spcpchart:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spcPchartService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{spcpId}")
    @RequiresPermissions("spc:spcpchart:info")
    public R info(@PathVariable("spcpId") Long spcpId){
		SpcPchartEntity spcPchart = spcPchartService.getById(spcpId);

        return R.ok().put("spcPchart", spcPchart);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("spc:spcpchart:save")
    public R save(@RequestBody SpcPchartEntity spcPchart){
		spcPchartService.save(spcPchart);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("spc:spcpchart:update")
    public R update(@RequestBody SpcPchartEntity spcPchart){
		spcPchartService.updateById(spcPchart);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("spc:spcpchart:delete")
    public R delete(@RequestBody Long[] spcpIds){
		spcPchartService.removeByIds(Arrays.asList(spcpIds));

        return R.ok();
    }

    @RequestMapping("/chart/p")
    public R getPChart(@RequestParam String tableName){
        return R.ok().put("p-chart_Info", spcPchartService.getPChart(tableName));
    }

    @RequestMapping("/chart/np")
    public R getNPChart(@RequestParam String tableName){
        return R.ok().put("np-chart_Info", spcPchartService.getNPChart(tableName));
    }

    @RequestMapping("/chart/u")
    public R getUChart(@RequestParam String tableName){
        return R.ok().put("u-chart_Info", spcPchartService.getUChart(tableName));
    }

    @RequestMapping("/chart/c")
    public R getCChart(@RequestParam String tableName){
        return R.ok().put("c-chart_Info", spcPchartService.getCChart(tableName));
    }

    @RequestMapping("/options")
    public R getTableName(){
        return R.ok().put("table_name",spcPchartService.getTableName());
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
