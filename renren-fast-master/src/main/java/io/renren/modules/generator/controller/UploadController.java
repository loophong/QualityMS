package io.renren.modules.generator.controller;

import io.renren.common.utils.R;
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
@RestController
@RequestMapping("test")
public class UploadController {
    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
    @PostMapping("/upload")
    public Map<String,Object> fileupload(MultipartFile file, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        String format = sdf.format(new Date());
        String realPath = req.getServletContext().getRealPath("/") + format;
        System.out.println("cur /test/upload real path===》" + realPath);
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
//        File folder = new File(uploadPath);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
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
//@RestController
//@RequestMapping("test")
//public class UploadController {
//
//    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
//
//    // 从配置文件中读取 upload.path
//    @Value("${upload.path}")
//    private String uploadPath;
//
//    @PostMapping("/upload")
//    public Map<String, Object> fileUpload(MultipartFile file, HttpServletRequest req) {
//        Map<String, Object> result = new HashMap<>();
//
//        // 按日期格式化生成子文件夹路径
//        String format = sdf.format(new Date());
//
//        // 使用配置文件中的 upload.path 作为根目录
//        String realPath = uploadPath + format;
//        System.out.println("cur /test/upload real path===》" + realPath);
//
//        File folder = new File(realPath);
//        if (!folder.exists()) {
//            folder.mkdirs(); // 创建存储文件夹
//        }
//
//        // 获取文件的原始名称，并生成唯一文件名
//        String oldName = file.getOriginalFilename();
//        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
//
//        try {
//            // 保存文件到指定路径
//            file.transferTo(new File(folder, newName));
//
//            // 构建返回的文件访问URL
//            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/renren-fast" + format + "/" + newName;
//            result.put("status", "OK");
//            result.put("name", oldName);
//            result.put("url", url);
//            System.out.println("cur /test/upload ===》" + url);
//        } catch (IOException e) {
//            result.put("status", "ERROR");
//            result.put("msg", e.getMessage());
//        }
//
//        return result;
//    }
//}

//@RestController
//@RequestMapping("/test")
//public class UploadController {
//
//    @Value("${upload.path}")  // 读取配置文件中的上传路径
//    private String uploadPath;
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("Please select a file!");
//        }
//
//        // 获取文件原始名称并生成唯一文件名
//        String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
//
//        try {
//            // 确定文件保存的路径
//            File dest = new File(uploadPath + fileName);
//            file.transferTo(dest);
//            System.out.println("cur /test/upload ===》" +"/images/" + fileName);
//            // 返回上传成功后的图片路径
//            return ResponseEntity.ok("/images/" + fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Upload failed!");
//        }
//    }
//}
