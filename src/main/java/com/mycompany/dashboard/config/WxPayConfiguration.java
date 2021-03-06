package com.mycompany.dashboard.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Binary Wang
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
public class WxPayConfiguration {

    private ApplicationProperties properties;

    public WxPayConfiguration(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(this.properties.getPay().getWxPay().getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(this.properties.getPay().getWxPay().getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(this.properties.getPay().getWxPay().getMchKey()));
        payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getPay().getWxPay().getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getPay().getWxPay().getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(this.properties.getPay().getWxPay().getKeyPath()));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
