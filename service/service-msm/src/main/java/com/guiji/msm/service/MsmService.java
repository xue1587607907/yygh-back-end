package com.guiji.msm.service;

import com.guiji.vo.msm.MsmVo;

import java.util.concurrent.ExecutionException;

public interface MsmService {

    //发送手机验证码
    boolean send(String phone, String code) throws ExecutionException, InterruptedException;

}
