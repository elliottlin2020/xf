package com.mycompany.dashboard.sms.builder;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.mycompany.dashboard.config.ApplicationProperties;
import com.mycompany.dashboard.domain.SmsConfig;
import com.mycompany.dashboard.sms.SmsTemplate;
import com.mycompany.dashboard.sms.aliyun.AliSmsTemplate;
import javax.cache.CacheManager;

/**
 * 阿里云短信构建类
 *
 */
public class AliSmsBuilder {

    public static SmsTemplate template(SmsConfig sms, CacheManager cacheManager) {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.getSms().setTemplateId(sms.getTemplateId());
        applicationProperties.getSms().setAccessKey(sms.getAccessKey());
        applicationProperties.getSms().setSecretKey(sms.getSecretKey());
        applicationProperties.getSms().setRegionId(sms.getRegionId());
        applicationProperties.getSms().setSignName(sms.getSignName());
        IClientProfile profile = DefaultProfile.getProfile(
            applicationProperties.getSms().getRegionId(),
            applicationProperties.getSms().getAccessKey(),
            applicationProperties.getSms().getSecretKey()
        );
        IAcsClient acsClient = new DefaultAcsClient(profile);
        return new AliSmsTemplate(applicationProperties, acsClient, cacheManager);
    }
}
