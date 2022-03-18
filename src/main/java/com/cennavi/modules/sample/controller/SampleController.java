package com.cennavi.modules.sample.controller;

import com.cennavi.core.common.PageResult;
import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.ResultObj;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.service.SampleService;

import com.cennavi.utils.ExcelUtil;
import io.swagger.annotations.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(tags = "Demo示例")
@RestController
@RequestMapping("/sample")
public class SampleController extends ResponseUtils {

    @Autowired
    private SampleService sampleService;

    @ApiOperation(value = "查询模板样例_(返回bean,使用bean中的注释)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = true)
    })
    @PostMapping("/listSampleByName")
    public ResultObj<List<SampleBean>> listSampleByName(@RequestParam(value = "name") String name) {
        /*List<Map<String, Object>> list = sampleService.listByName(name);
        List<SampleBean> json = toBean(list, SampleBean.class);*/
        List<SampleBean> list = sampleService.listByName(name);
        return success(list);
    }

/*    @ApiOperation(value = "查询模板样例_测试json(返回bean,使用bean中的注释)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = true)
    })
    @PostMapping("/listSampleByNameJson")
    public String listSampleByNameJson(@RequestParam(value = "name") String name) {
        *//*List<Map<String, Object>> list = sampleService.listByName(name);
        List<SampleBean> json = toBean(list, SampleBean.class);*//*
        List<SampleBean> list = sampleService.listByName(name);
        return JsonUtil.toJSONString(success(list));
    }*/

    @ApiOperation(value = "查询模板样例1_(返回map,需要单独写返回说明放在notes中)",
            notes = "返回值说明：{code:code, name:名称, age:年龄}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = true)
    })
    @GetMapping("/listSampleByName1")
    public ResultObj listSampleByName1(@RequestParam(value = "name",required = false) String name) {
        List<Map<String, Object>> list = sampleService.listByName1(name);
        return success(list);
//        return JsonUtil.toJSONString(success(list));
    }


    @ApiOperation(value = "分页查询模板样例_(返回map,需要单独写返回说明放在notes中)",
            notes = "返回值说明：{code:code, name:名称, age:年龄}")
    @ApiImplicitParams({//请求参数说明
            @ApiImplicitParam(name = "name", value = "姓名"),
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量，limit")
    })
    @PostMapping("/findByPage")
    public ResultObj findByPage(@RequestParam(value = "name",required = false) String name,
                                @RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize) {
        PageResult<SampleBean> result = sampleService.findByPage(name, pageNum, pageSize);
        return success(result);
    }


    @ApiOperation(value = "分页查询模板样例2_(返回map,需要单独写返回说明放在notes中)",
            notes = "返回值说明：{code:code, name:名称, age:年龄}")
    @PostMapping("/findByPage2")
    public ResultObj findByPage2(@RequestParam(value = "name",required = false) String name,
                                @RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize) {
        PageResult<SampleBean> result = sampleService.findByPage(name, pageNum, pageSize);
        return success(result);
    }

    //SampleBean实体中已经加了Swagger注解
    @ApiOperation(value = "保存模板样例")
    @PostMapping("/save")
    public ResultObj save(SampleBean bean) {
        if(bean.getId()==null) {
            bean.setId(UUID.randomUUID().toString());
        }
        sampleService.save(bean);
        return success();
    }

    @ApiOperation(value = "修改模板样例")
    @PostMapping("/update")
    public ResultObj update(SampleBean bean) {
        SampleBean sa = sampleService.findById(bean.getId());
        if(sa!=null) {
            sampleService.update(bean);
        } else {
            return error("修改失败:id不存在");
        }
        sampleService.update(bean);
        return success();
    }

    @ApiOperation(value = "删除模板样例")
    @PostMapping("/delete")
    public ResultObj update(@RequestParam String id) {
        sampleService.delete(id);
        return success();
    }


    @ApiImplicitParam(name = "file", value = "文件", dataType = "file", required = true)
    @ApiOperation(value = "导入t_area文本数据样例")
    @PostMapping("/importArea")
    public ResultObj importArea(@RequestParam(value = "file") MultipartFile file) throws IOException {
        InputStream in = file.getInputStream();
        InputStreamReader read = new InputStreamReader(in, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(read);

        //List<BaseArea> areas = new ArrayList<>();
        String lineTxt = "";
        while ((lineTxt = bufferedReader.readLine()) != null) {
            if (lineTxt.toLowerCase().contains("area_id")) {
                continue;
            }
            String[] ss = lineTxt.replace("\"", "").split("\t");
            /*BaseArea ba = new BaseArea();
            ba.setId(ss[0]);
            ba.setType(ss[1]);
            ba.setName(ss[2]);
            areas.add(ba);*/
        }
        //sampleService.batchSaveArea(areas);
        return success();
    }

    @ApiImplicitParam(name = "file", value = "文件", dataType = "file", required = true)
    @ApiOperation(value = "导入excel数据样例")
    @PostMapping("/importExcel")
    public ResultObj importExcel(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String filename=file.getOriginalFilename();
        InputStream in = file.getInputStream();
        //解析excel中的数据
        List<String> datas = ExcelUtil.importLayerDataFromExcel(in, filename);
        return success();
    }

    @ApiImplicitParam(name = "file", value = "文件", dataType = "file", required = true)
    @ApiOperation(value = "导出excel样例")
    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        //设备属性
        String ss = "用户名(必填),密码(必填),真实名称(必填),所属组织id(必填),角色名称(必填),联系电话,用户状态,描述";
        String[] headers = ss.split(",");

        //查询要导出的数据
        List<Map<String,Object>> list = new ArrayList<>();

        response.reset();
        response.setContentType("application/excel");
        response.setHeader("content-disposition", "attachment; filename=userinfo.xls");
        ExcelUtil.exportMapInfoToExcel(list, headers,"userinfo",response.getOutputStream());
    }

    /**
     * 下载用户导入模板
     */



    @ApiOperation(value = "文件下载样例")
    @PostMapping("downloadFile")
    public ResponseEntity<byte[]> downloadReport(@RequestParam(value = "filename") String filename) throws IOException {
        String path = "e://";
        //String filename = "t.txt";
        File file = new File(path+filename);
        HttpHeaders headers = new HttpHeaders();
        String downloadFileName = new String(filename.getBytes("UTF-8"), "ISO-8859-1");  //少了这句，可能导致下载中文文件名的文档，只有后缀名的情况
        headers.setContentDispositionFormData("attachment", downloadFileName);//告知浏览器以下载方式打开
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);//设置MIME类型
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

}
