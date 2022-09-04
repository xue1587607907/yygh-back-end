package com.guiji.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guiji.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Integer id);

    void exportDictData(HttpServletResponse response);

    void importDictData(MultipartFile multipartFile);

    //根据dictCode获取下级节点
    List<Dict> findByDictCode(String dictCode);

    //根据dictcode和value查询
    String getDictName(String dictCode, String value);
}
