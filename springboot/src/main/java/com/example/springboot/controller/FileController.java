package com.example.springboot.controller;
/*
* 文件处理
* */

import cn.hutool.core.io.FileUtil;
import com.example.springboot.common.Result;
import com.example.springboot.exception.CustomerException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController     //标识这个类是一个控制器类，并且它的所有方法返回的对象会自动被序列化为 JSON 格式（
@RequestMapping("/files")       //映射 HTTP 请求的 URL 路径。它用于指定类或方法处理的请求路径。
public class FileController {
    //获取当前项目的根路径
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    /**
     * 文件上传接口
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        // 获取上传的文件的原始文件名
        String originalFilename = file.getOriginalFilename();

        // 检查文件保存的目录是否存在，如果不存在则创建目录
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);  // 如果没有目录，创建一个目录
        }

        // 生成文件的完整路径，给文件名加上唯一标识（例如，当前时间戳）
        String fileName = System.currentTimeMillis() + "_" + originalFilename;

        // 获取文件存储的实际路径
        String realPath = filePath + fileName;

        try {
            // 将文件的字节内容写入指定的路径
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            // 捕获文件上传过程中的 IO 异常
            e.printStackTrace();  // 打印异常信息到控制台
            throw new CustomerException("500", "文件上传失败");  // 抛出自定义的异常
        }
        //返回一个网络链接
        String url = "http://localhost:8080/files/download/" + fileName;
        return Result.success(url);
    }


    /**
     * 文件下载
     * @param fileName
     * @param response
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");                    //第一行：告诉浏览器将返回的数据作为附件下载，并指定文件名。//告诉浏览器这是二进制文件流，应该作为文件处理，而不是直接显示。
                                                                                    // 用户点击下载链接时，浏览器就会自动弹出文件下载框，用户可以保存文件，而不是在浏览器里直接打开文件内容。
            OutputStream outputStream =  response.getOutputStream();
            String realPath = filePath + fileName;
            //获取文件字节数组
            byte[] bytes = FileUtil.readBytes(realPath);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new CustomerException("500", "文件下载失败");
        }
    }

    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) {
        // 获取上传的文件的原始文件名
        String originalFilename = file.getOriginalFilename();

        // 检查文件保存的目录是否存在，如果不存在则创建目录
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);  // 如果没有目录，创建一个目录
        }

        // 生成文件的完整路径，给文件名加上唯一标识（例如，当前时间戳）
        String fileName = System.currentTimeMillis() + "_" + originalFilename;

        // 获取文件存储的实际路径
        String realPath = filePath + fileName;

        try {
            // 将文件的字节内容写入指定的路径
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            // 捕获文件上传过程中的 IO 异常
            e.printStackTrace();
            throw new CustomerException("500", "文件上传失败");
        }

        // 为了适配 wang-editor，返回的数据格式需要符合其要求
        // wang-editor 期望接收以下格式的数据：{ "errno": 0, "data": [ { "url": "图片的URL" } ] }
        String url = "http://localhost:8080/files/download/" + fileName;
        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", url);
        list.add(urlMap);
        resMap.put("errno", 0);
        resMap.put("data", list);
        return resMap;
    }



}
