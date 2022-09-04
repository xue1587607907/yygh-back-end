package com.guiji.yygh.hosp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guiji.model.hosp.HospitalSet;
import com.guiji.vo.hosp.HospitalSetQueryVo;
import com.guiji.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {

    IPage<HospitalSet> findPage(Integer current, Integer pageSize, HospitalSetQueryVo hospitalSetQueryVo);

    //2 根据传递过来医院编码，查询数据库，查询签名
    String getSignKey(String hoscode);

    //获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);


}
