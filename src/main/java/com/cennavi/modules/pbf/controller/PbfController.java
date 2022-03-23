package com.cennavi.modules.pbf.controller;

import com.cennavi.modules.pbf.service.PbfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by sunpengyan on 2022/2/15.
 */
@Api(tags = "pbf接口")
@RestController
@RequestMapping("pbf")
public class PbfController {

    @Autowired
    PbfService pbfService;

    /**
     * 获取设备、pbf方式
     * @param x
     * @param y
     * @param z
     * @return
     */
    @ApiOperation(value = "获取pbf格式的设备列表", notes = "{'source-layer':'deviceData'}")
    @GetMapping(value = "/listDevicePbf/{z}/{x}/{y}")
    public byte[] listDevicePbf(@PathVariable("z") int z, @PathVariable("x") int x, @PathVariable("y") int y ){
        byte[] bytes = new byte[0];
        try {
            bytes = pbfService.listDevicePbf(x,y,z);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bytes;
    }
}
