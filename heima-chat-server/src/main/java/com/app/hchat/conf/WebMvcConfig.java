package com.app.hchat.conf;

import com.app.hchat.util.FileUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @description SpringMVC配置，配置上传文件
 * @author Shubo Dai
 * @time 2020.06.03
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 文件上传路径前缀
     */
    public static String uploadSuffixPath="/fileSuffix";
    /**
     * 本地磁盘目录
     */
    public static String uploadLocalPath = "/home/daishubo/upload/images/";

    /**
     * @title: addResourceHandlers
     * @description:  映射本地磁盘为静态目录
     * @param: registry
     * @throws:W
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        FileUtil.uploadSuffixPath = uploadSuffixPath;
        FileUtil.uploadLocalPath = uploadLocalPath;
        registry.addResourceHandler(uploadSuffixPath +"/**").addResourceLocations("file:"+uploadLocalPath);
    }

}
