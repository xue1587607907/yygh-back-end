package com.guiji.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guiji.model.hosp.HospitalSet;
import com.guiji.result.Result;
import com.guiji.yygh.hosp.service.HospitalSetService;
import com.guiji.util.MD5;
import com.guiji.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
//@CrossOrigin // 允许跨域访问
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取医院信息")
    @GetMapping("findAll")
    public Result<List<HospitalSet>> findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "根据条件查询医院信息带分页")
    @PostMapping("{current}/{pageSize}")
    public Result findPage(@PathVariable("current") Integer current,
                           @PathVariable("pageSize") Integer pageSize,
                           @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        IPage<HospitalSet> page = hospitalSetService.findPage(current, pageSize, hospitalSetQueryVo);
        return Result.ok(page);
    }

    @ApiOperation(value = "添加医院设置")
    @PostMapping("addHospitalSet")
    public Result saveHosp(@RequestBody HospitalSet hospitalSet) {
        hospitalSet.setIsDeleted(0);
        hospitalSet.setStatus(1); // 状态 1:可用 0:不可用
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + new Random().nextInt(10000)));
        boolean flag = hospitalSetService.save(hospitalSet);
        return flag ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "删除医院设置")
    @DeleteMapping("removeHosp/{id}")
    public Result removeHosp(@PathVariable("id") Integer id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "根据id查询医院设置信息")
    @GetMapping("get/{id}")
    public Result getHospById(@PathVariable("id") Integer id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    @ApiOperation(value = "根据id修改医院设置信息")
    @PutMapping("update")
    public Result updateById(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        return flag ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "根据id批量删除医院信息")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Integer> ids){
        boolean flag = hospitalSetService.removeByIds(ids);
        return flag ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "锁定医院")
    @PutMapping("lockHospital/{id}/{status}")
    public Result lockHospital(@PathVariable("id") Integer id, @PathVariable("status") Integer status){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        boolean flag = hospitalSetService.updateById(hospitalSet);
        return flag ? Result.ok() : Result.fail();
    }

    @GetMapping("sendSignKey/{id}")
    public Result sendSignKey(@PathVariable("id") Integer id){
        hospitalSetService.getById(id);
        //TODO 后续补充
        return null;
    }

}
