package com.guiji.msm.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtils implements InitializingBean {

    @Value("${aliyun.accessKey}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String secret;

    public static String ACCESS_KEY_ID;
    public static String SECRECT;

    @Override
    public void afterPropertiesSet(){
        ACCESS_KEY_ID=accessKeyId;
        SECRECT=secret;
    }
}
