package com.guiji.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guiji.model.cmn.Dict;
import com.guiji.vo.cmn.DictEeVo;
import com.guiji.yygh.cmn.listener.DictListener;
import com.guiji.yygh.cmn.mapper.DictMapper;
import com.guiji.yygh.cmn.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Integer id) {
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dict::getParentId, id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        return dictList.stream().peek(dict -> {
            boolean children = isChildren(dict.getId());
            dict.setHasChildren(children);
        }).collect(Collectors.toList());
    }

    @Override
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        List<Dict> dictList = baseMapper.selectList(null);
        List<DictEeVo> dictEeVoList = dictList.stream().map(dict->{
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            return dictEeVo;
        }).collect(Collectors.toList());
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet().doWrite(dictEeVoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void importDictData(MultipartFile multipartFile) {
        try {
            EasyExcel.read(multipartFile.getInputStream(), DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //根据dictcode和value查询
    @Override
    public String getDictName(String dictCode, String value) {
        //如果dictCode为空，直接根据value查询
        if(StringUtils.isEmpty(dictCode)) {
            //直接根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else {//如果dictCode不为空，根据dictCode和value查询
            //根据dictcode查询dict对象，得到dict的id值
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long parent_id = codeDict.getId();
            //根据parent_id和value进行查询
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parent_id)
                    .eq("value", value));
            return finalDict.getName();
        }
    }

    //根据dictCode获取下级节点
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictcode获取对应id
        Dict dict = this.getDictByDictCode(dictCode);
        //根据id获取子节点
        return this.findChildData(Math.toIntExact(dict.getId()));
    }

    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        return baseMapper.selectOne(wrapper);
    }

    public boolean isChildren(Long id) {
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dict::getParentId, id);
        int count = Math.toIntExact(baseMapper.selectCount(wrapper));
         return count > 0;
    }

}
