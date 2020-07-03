package com.app.hchat.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @description 文件上传
 * @author Shubo Dai
 * @time 2020.06.03
 */
public class FileUtil {
    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    /**
     * 文件上传路径前缀
     */
    public static String uploadSuffixPath;
    /**
     * 本地磁盘目录
     */
    public static String uploadLocalPath;

    public static String CHAR_STR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * @Title: uploadFile
     * @Description: 单文件上传到本地磁盘
     * @param: multipartFile
     * @return: java.lang.String
     * @throws:
     */
    public static String uploadFile(MultipartFile multipartFile){
        if(multipartFile == null){
            return null;
        }
        //获取文件相对路径
        String fileName = getUploadFileName(multipartFile.getOriginalFilename());
        String dateDir = DateUtil.format(null,DateUtil.PATTERN_yyyyMMdd);
        File destFileDir = new File(uploadLocalPath + File.separator + dateDir);
        if(!destFileDir.exists()){
            destFileDir.mkdirs();
        }
        try {
            File destFile = new File(destFileDir.getAbsoluteFile()+File.separator+fileName);
            multipartFile.transferTo(destFile);
            logger.info("文件【"+multipartFile.getOriginalFilename()+"】上传成功");
            return uploadSuffixPath + "/" + dateDir+"/"+fileName;
        } catch (IOException e) {
            logger.error("文件上传异常："+multipartFile.getOriginalFilename(),e);
            return null;
        }
    }
    /**
     * @Title: getUploadFilePath
     * @Description: 获取上传后的文件相对路径  --数据库存储该路径
     * @param: fileName
     * @return: java.lang.String
     * @throws:
     */
    public static String getUploadFileName(String fileName){
        return new StringBuilder()
                .append(DateUtil.format(null, DateUtil.PATTERN_yyyyMMddHHmmssSSS))
                .append("_").append(getRandomStrByNum(6))
                .append(".").append(FilenameUtils.getExtension(fileName))
                .toString();
    }

    /**
     * @Title: isFileBySuffix
     * @Description: 通过后缀名判断是否是某种文件
     * @param: fileName 文件名称
     * @param: suffix 后缀名
     * @return: boolean
     * @throws:
     */
    public static boolean isFileBySuffix(String fileName,String suffix){
        if(StringUtils.isNoneBlank(fileName) && StringUtils.isNoneBlank(suffix)){
            return fileName.endsWith(suffix.toLowerCase()) || fileName.endsWith(suffix.toUpperCase());
        }
        return false;
    }
    public static String getRandomStrByNum(int factor) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < factor; i++) {
            sb.append(CHAR_STR.charAt(random.nextInt(36)));
        }
        return sb.toString();
    }
}
