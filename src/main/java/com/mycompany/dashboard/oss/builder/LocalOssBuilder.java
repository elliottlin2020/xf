package com.mycompany.dashboard.oss.builder;

import com.mycompany.dashboard.config.ApplicationProperties;
import com.mycompany.dashboard.domain.OssConfig;
import com.mycompany.dashboard.oss.OssRule;
import com.mycompany.dashboard.oss.OssTemplate;
import com.mycompany.dashboard.oss.local.LocalOssTemplate;

/**
 * 本地存储构建类
 *
 */
public class LocalOssBuilder {

    public static OssTemplate template(OssConfig ossConfig, OssRule ossRule) {
        // 创建配置类
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.getOss().setEndpoint(ossConfig.getEndpoint());
        applicationProperties.getOss().setAccessKey(ossConfig.getAccessKey());
        applicationProperties.getOss().setSecretKey(ossConfig.getSecretKey());
        applicationProperties.getOss().setBucketName(ossConfig.getBucketName());
        return new LocalOssTemplate(applicationProperties, ossRule);
    }
}
