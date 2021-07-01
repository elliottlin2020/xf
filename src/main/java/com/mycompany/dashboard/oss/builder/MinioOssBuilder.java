package com.mycompany.dashboard.oss.builder;

import com.mycompany.dashboard.config.ApplicationProperties;
import com.mycompany.dashboard.domain.OssConfig;
import com.mycompany.dashboard.oss.OssRule;
import com.mycompany.dashboard.oss.OssTemplate;
import com.mycompany.dashboard.oss.minio.MinioTemplate;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

/**
 * Minio云存储构建类
 *
 */
public class MinioOssBuilder {

    public static OssTemplate template(OssConfig oss, OssRule ossRule) throws InvalidPortException, InvalidEndpointException {
        // 创建配置类
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.getOss().setEndpoint(oss.getEndpoint());
        applicationProperties.getOss().setAccessKey(oss.getAccessKey());
        applicationProperties.getOss().setSecretKey(oss.getSecretKey());
        applicationProperties.getOss().setBucketName(oss.getBucketName());
        // 创建客户端
        MinioClient minioClient = new MinioClient(oss.getEndpoint(), oss.getAccessKey(), oss.getSecretKey());
        return new MinioTemplate(minioClient, ossRule, applicationProperties);
    }
}
