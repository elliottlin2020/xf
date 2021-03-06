package com.mycompany.dashboard.oss.qiniu;

import com.mycompany.dashboard.config.ApplicationProperties;
import com.mycompany.dashboard.oss.OssRule;
import com.mycompany.dashboard.oss.OssTemplate;
import com.mycompany.dashboard.oss.model.BladeFile;
import com.mycompany.dashboard.oss.model.OssFile;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * QiniuTemplate
 *
 */
public class QiniuTemplate implements OssTemplate {

    private final Auth auth;
    private final UploadManager uploadManager;
    private final BucketManager bucketManager;
    private final ApplicationProperties applicationProperties;
    private final OssRule ossRule;

    @Override
    public void makeBucket(String bucketName) throws QiniuException {
        if (Arrays.stream(bucketManager.buckets()).noneMatch(name -> name.equals(getBucketName(bucketName)))) {
            bucketManager.createBucket(getBucketName(bucketName), Zone.autoZone().getRegion());
        }
    }

    @Override
    public void removeBucket(String bucketName) {}

    @Override
    public boolean bucketExists(String bucketName) throws QiniuException {
        return Arrays.stream(bucketManager.buckets()).anyMatch(name -> name.equals(getBucketName(bucketName)));
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName) throws QiniuException {
        bucketManager.copy(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) throws QiniuException {
        bucketManager.copy(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
    }

    @Override
    public OssFile statFile(String fileName) throws QiniuException {
        return statFile(applicationProperties.getOss().getBucketName(), fileName);
    }

    @Override
    public OssFile statFile(String bucketName, String fileName) throws QiniuException {
        FileInfo stat = bucketManager.stat(getBucketName(bucketName), fileName);
        OssFile ossFile = new OssFile();
        ossFile.setName(StringUtils.isEmpty(stat.key) ? fileName : stat.key);
        ossFile.setLink(fileLink(ossFile.getName()));
        ossFile.setHash(stat.hash);
        ossFile.setLength(stat.fsize);
        ossFile.setPutTime(new Date(stat.putTime / 10000));
        ossFile.setContentType(stat.mimeType);
        return ossFile;
    }

    @Override
    public String filePath(String fileName) {
        return getBucketName().concat("/").concat(fileName);
    }

    @Override
    public String filePath(String bucketName, String fileName) {
        return getBucketName(bucketName).concat("/").concat(fileName);
    }

    @Override
    public String fileLink(String fileName) {
        return applicationProperties.getOss().getEndpoint().concat("/").concat(fileName);
    }

    @Override
    public String fileLink(String bucketName, String fileName) {
        return applicationProperties.getOss().getEndpoint().concat("/").concat(fileName);
    }

    @Override
    public BladeFile putFile(MultipartFile file) throws IOException {
        return putFile(applicationProperties.getOss().getBucketName(), file.getOriginalFilename(), file);
    }

    @Override
    public BladeFile putFile(String fileName, MultipartFile file) throws IOException {
        return putFile(applicationProperties.getOss().getBucketName(), fileName, file);
    }

    @Override
    public BladeFile putFile(String bucketName, String fileName, MultipartFile file) throws IOException {
        return putFile(bucketName, fileName, file.getInputStream());
    }

    @Override
    public BladeFile putFile(String fileName, InputStream stream) throws QiniuException {
        return putFile(applicationProperties.getOss().getBucketName(), fileName, stream);
    }

    @Override
    public BladeFile putFile(String bucketName, String fileName, InputStream stream) throws QiniuException {
        return put(bucketName, stream, fileName, false);
    }

    public BladeFile put(String bucketName, InputStream stream, String key, boolean cover) throws QiniuException {
        makeBucket(bucketName);
        String originalName = key;
        key = getFileName(key);
        // ????????????
        if (cover) {
            uploadManager.put(stream, key, getUploadToken(bucketName, key), null, null);
        } else {
            Response response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
            int retry = 0;
            int retryCount = 5;
            while (response.needRetry() && retry < retryCount) {
                response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
                retry++;
            }
        }
        BladeFile file = new BladeFile();
        file.setOriginalName(originalName);
        file.setName(key);
        file.setDomain(getOssHost());
        file.setLink(fileLink(bucketName, key));
        return file;
    }

    @Override
    public void removeFile(String fileName) throws QiniuException {
        bucketManager.delete(getBucketName(), fileName);
    }

    @Override
    public void removeFile(String bucketName, String fileName) throws QiniuException {
        bucketManager.delete(getBucketName(bucketName), fileName);
    }

    @Override
    public void removeFiles(List<String> fileNames) {
        fileNames.forEach(
            fileName -> {
                try {
                    removeFile(fileName);
                } catch (QiniuException e) {
                    e.printStackTrace();
                }
            }
        );
    }

    @Override
    public void removeFiles(String bucketName, List<String> fileNames) {
        fileNames.forEach(
            fileName -> {
                try {
                    removeFile(getBucketName(bucketName), fileName);
                } catch (QiniuException e) {
                    e.printStackTrace();
                }
            }
        );
    }

    /**
     * ???????????????????????????????????????
     *
     * @return String
     */
    private String getBucketName() {
        return getBucketName(applicationProperties.getOss().getBucketName());
    }

    /**
     * ???????????????????????????????????????
     *
     * @param bucketName ???????????????
     * @return String
     */
    private String getBucketName(String bucketName) {
        return ossRule.bucketName(bucketName);
    }

    /**
     * ????????????????????????????????????
     *
     * @param originalFilename ???????????????
     * @return string
     */
    private String getFileName(String originalFilename) {
        return ossRule.fileName(originalFilename);
    }

    /**
     * ?????????????????????????????????
     */
    public String getUploadToken(String bucketName) {
        return auth.uploadToken(getBucketName(bucketName));
    }

    /**
     * ?????????????????????????????????
     */
    private String getUploadToken(String bucketName, String key) {
        return auth.uploadToken(getBucketName(bucketName), key);
    }

    /**
     * ????????????
     *
     * @return String
     */
    public String getOssHost() {
        return applicationProperties.getOss().getEndpoint();
    }

    public QiniuTemplate(
        Auth auth,
        UploadManager uploadManager,
        BucketManager bucketManager,
        ApplicationProperties applicationProperties,
        OssRule ossRule
    ) {
        this.auth = auth;
        this.uploadManager = uploadManager;
        this.bucketManager = bucketManager;
        this.applicationProperties = applicationProperties;
        this.ossRule = ossRule;
    }
}
