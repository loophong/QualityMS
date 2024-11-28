package io.renren.modules.generator.controller;

import io.renren.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@Slf4j
@RestController
@RequestMapping("test")
public class UploadController {
    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
    @PostMapping("/upload")
    public Map<String,Object> fileupload(MultipartFile file, HttpServletRequest req) {

        log.info("文件"+file.getOriginalFilename());

        Map<String, Object> result = new HashMap<>();

        String format = sdf.format(new Date());

        String realPath = req.getServletContext().getRealPath("/") + format;
        System.out.println("cur /test/upload real path===》" + realPath);

        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        try {
            file.transferTo(new File(folder, newName));
            //"/renren-fast" +
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()+"/renren-fast" + format + newName;
            result.put("status", "OK");
            result.put("name", oldName);
            result.put("url", url);
            System.out.println("cur /test/upload ===》" +url);
            return R.ok().put("uploadurl", url);
        } catch (IOException e) {
            result.put("status", "ERROR");
            result.put("msg", e.getMessage());
            return R.ok().put("msg", e.getMessage());
        }
    }
}

