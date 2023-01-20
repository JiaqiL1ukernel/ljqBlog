package com.ljq.service.impl;

import com.google.gson.Gson;
import com.ljq.domain.ResponseResult;
import com.ljq.enums.AppHttpCodeEnum;
import com.ljq.exception.SystemException;
import com.ljq.service.UploadService;
import com.ljq.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

@Data
@Service
@ConfigurationProperties(prefix = "oss")
public class ImgUploadService implements UploadService {

    private String accessKey;
    private String secretKey;
    private String bucket;
    @Override
    public ResponseResult upload(MultipartFile img) {
        //判断图片的合法性
        String originalFilename = img.getOriginalFilename();
        if(!originalFilename.endsWith(".png")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //拼接文件名
        String filename = PathUtils.generateFilePath(originalFilename);
        //上传到oss
        String url = Ossupload(img,filename);
        return ResponseResult.okResult(url);
    }


    private String Ossupload(MultipartFile img, String filename) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        String key = filename;

        try {

            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://ropupyhln.hb-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }

        return "www";
    }


}
