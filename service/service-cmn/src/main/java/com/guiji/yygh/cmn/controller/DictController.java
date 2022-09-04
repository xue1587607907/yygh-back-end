package com.guiji.yygh.cmn.controller;

import com.guiji.model.cmn.Dict;
import com.guiji.result.Result;
import com.guiji.yygh.cmn.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "数据字典模块")
@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation(value = "导出数据字典到Excel")
    @GetMapping("exportData")
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
    }

    @PostMapping("importData") // 记得加上@RequestParam  否者会读取不到文件报空指针
    public Result importDictData(@RequestParam("file") MultipartFile multipartFile){
        dictService.importDictData(multipartFile);
        return Result.ok();
    }

    @ApiOperation(value = "根据id查询子节点")
    @GetMapping("findChildData/{id}")
    public Result<List<Dict>> findChildData(@PathVariable("id") Integer id) {
        List<Dict> dictServiceChildData = dictService.findChildData(id);
        return Result.ok(dictServiceChildData);
    }

    //根据dictCode获取下级节点
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

    //根据dictcode和value查询
    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value) {
        return dictService.getDictName(dictCode,value);
    }

    //根据value查询
    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value) {
        return dictService.getDictName("",value);
    }
}
