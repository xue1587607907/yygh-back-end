package com.guiji.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guiji.exception.YyghException;
import com.guiji.result.ResultCodeEnum;
import com.guiji.vo.order.SignInfoVo;
import com.guiji.yygh.hosp.mapper.HospitalSetMapper;
import com.guiji.model.hosp.HospitalSet;
import com.guiji.yygh.hosp.service.HospitalSetService;
import com.guiji.vo.hosp.HospitalSetQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    @Override
    public IPage<HospitalSet> findPage(Integer current, Integer pageSize, HospitalSetQueryVo hospitalSetQueryVo) {
        IPage<HospitalSet> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<HospitalSet> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(hospitalSetQueryVo)) {
            wrapper.like(!StringUtils.isEmpty(hospitalSetQueryVo.getHosname()), HospitalSet::getHosname, hospitalSetQueryVo.getHosname());
            wrapper.like(!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode()), HospitalSet::getHoscode, hospitalSetQueryVo.getHoscode());
        }
        return baseMapper.selectPage(page, wrapper);
    }

    //2 根据传递过来医院编码，查询数据库，查询签名
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return hospitalSet.getSignKey();
    }

    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }

}
