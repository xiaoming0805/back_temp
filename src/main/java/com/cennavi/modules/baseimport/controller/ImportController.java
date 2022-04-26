package com.cennavi.modules.baseimport.controller;

import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.ResultObj;
import com.cennavi.modules.baseimport.beans.*;
import com.cennavi.modules.baseimport.service.BaseImportService;
import com.cennavi.utils.GeomtryUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"交警基础数据导入"},value = "baseimport")
@RestController
@RequestMapping("/baseImport")
public class ImportController extends ResponseUtils {
    private Logger logger = LoggerFactory.getLogger(ImportController.class);
    @Autowired
    private BaseImportService baseImportService;


    /**
     * 说明
     * 统计类接口查询-1
     * 场景：使用多表查询或者 sum count 出来的数据 表中没有相应的字段
     * 例如多表查询且使用 jdbcTemplate 返回了 List<Map<String,Object>> 或者 Map<String,Object> 类型的数据，生成api标准接口文档时
     * 可以 使用 ResponseUtils 类中 toBean 方法 转化为类 并且返回
     * 缺点：写大量的 VO 类文件 影响效率
     * 如果不想写大量的类文件。可以参考例子，允许简便写法
     */
    @ApiOperation(value = "导入area区域表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", required=true, dataType = "Integer", name = "file", value = "文件", example = ""),
    })
    @PostMapping("/importArea")
    public ResultObj<String> getTrack(@RequestParam(value = "file") MultipartFile file) {
        String res = "";
        int size = 0;
        try {
            InputStream in = file.getInputStream();
            InputStreamReader read = new InputStreamReader(in,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);

            List<BaseArea> areas = new ArrayList<>();
            String lineTxt = "";
            while((lineTxt = bufferedReader.readLine()) != null) {
                if(lineTxt.toLowerCase().contains("area_id")) {
                    continue;
                }
                String[] ss = lineTxt.replace("\"","").split("\t");
                BaseArea ba = new BaseArea();
                ba.setId(ss[0]);
                ba.setType(ss[1]);
                ba.setName(ss[2]);
                if(StringUtils.isNotBlank(ss[4])) {
                    ba.setCenter(GeomtryUtils.wkt2GeoJson(ss[4]));
                } else {
                    ba.setCenter(ss[4]);
                }
                ba.setGeometry(ss[5]);
                //ba.set("area_version",ss[6]);
                areas.add(ba);
            }
            size = areas.size();
            baseImportService.batchSaveArea(areas);
        } catch (Exception e)    {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            res = "导入出现问题";
            return success(res);
        }
        res = "导出"+size+"条区域数据成功!";
        return success(res);
    }


    /**
     * 导入t_raod.txt文件到base_road表中
     * @return
     */
    @PostMapping("/importRoad")
    @ResponseBody
    public String importRoad(@RequestParam(value = "file") MultipartFile file) {
        try {
            /*String filePath = "E:\\交警平台\\b基础配置表\\张家口\\18Q1_M3W1\\130700\\t_road_130700.txt";
            String encoding="utf-8";
            File file = new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);*/

            InputStream in = file.getInputStream();
            InputStreamReader read = new InputStreamReader(in,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);

            List<BaseRoad> list = new ArrayList<>();

            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null){
                if(lineTxt.contains("ROAD_ID")) {
                    continue;
                }
                String[] ss = lineTxt.replace("\"","").split("\t");
                BaseRoad r = new BaseRoad();
                r.setId(ss[0]);
                r.setType(ss[1]);
                r.setKind(ss[2]);
                r.setName(ss[3]);
                //r.setAliasName(ss[4]);
                r.setStartname(ss[5]);
                r.setEndname(ss[6]);
                r.setDirection(ss[7]);
                r.setLength(Float.parseFloat(ss[8]));
                r.setCenter("{\"type\":\"Point\",\"coordinates\":["+ss[9].replace(" ",",")+"]}");
                r.setGeometry(GeomtryUtils.wkt2GeoJson(ss[10]));
                //r.setInvalid(ss[11]);
                list.add(r);
                if(list.size()>=3000) {
                    baseImportService.batchSaveRoad(list);
                    list = new ArrayList<>();
                }
            }
            read.close();
            baseImportService.batchSaveRoad(list);
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return "fail:"+e.getMessage();
        }
        return "success";
    }


    /**
     * 导入 rtic表  -》 rtic
     * @return
     */
    @PostMapping("/importRtic")
    @ResponseBody
    public String importRtic(@RequestParam(value = "file") MultipartFile file) {
        try {
            // String filePath = "E:\\交警平台\\b基础配置表\\张家口\\18Q1_M3W1\\130700\\t_rtic_130700.txt";
            List<BaseRtic> list = new ArrayList<>();
            InputStream in = file.getInputStream();
            InputStreamReader read = new InputStreamReader(in,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);

            String lineTxt = "";
            Map<String,BaseRtic> map = new HashMap<>();
            while((lineTxt = bufferedReader.readLine()) != null) {
                if(lineTxt.toLowerCase().contains("rtic_id")) {
                    continue;
                }
                String[] ss = lineTxt.replace("\"","").split("\t");
                BaseRtic baseRtic = new BaseRtic();
                baseRtic.setId(ss[0]);
                baseRtic.setName(ss[1]);
                baseRtic.setStartName(ss[2]);
                baseRtic.setEndName(ss[3]);
                baseRtic.setLength(Float.parseFloat(ss[4]));
                //baseRtic.setLaneNum(ss[5]);
                baseRtic.setWidth(ss[6]);
                baseRtic.setSpeedLimit(ss[7]);
                baseRtic.setDirection(ss[8]);
                baseRtic.setKind(ss[9]);
                baseRtic.setStartpoint(ss[10]+","+ss[11]);
                baseRtic.setEndpoint(ss[12]+","+ss[13]);
                baseRtic.setCenter("{\"type\":\"Point\",\"coordinates\":["+ss[14].replace(" ",",")+"]}");
                baseRtic.setGeometry(GeomtryUtils.wkt2GeoJson(ss[15]));
                //baseRtic.setVersion(ss[16]);
                map.put(ss[0],baseRtic);
            }
            list.addAll(map.values());
            baseImportService.batchSaveRtic(list);
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return "fail:"+e.getMessage();
        }
        return "success";
    }



    /**
     * 导入BaseAreaRoadRtic 配置表之间的关系  -》 t_area_road_roadsection_rtic
     * @return
     */
    @PostMapping("/importAreaRoadRtic")
    @ResponseBody
    public String importAreaRoadRtic(@RequestParam(value = "file") MultipartFile file) {
        try {
            InputStream in = file.getInputStream();

            InputStreamReader read = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(read);

            List<BaseAreaRoadRtic> maps = new ArrayList<>();
            String lineTxt = "";
            while((lineTxt = bufferedReader.readLine()) != null) {
                if(lineTxt.toLowerCase().contains("area_id")) { continue;  }
                String[] ss = lineTxt.replace("\"","").split("\t");
                BaseAreaRoadRtic barr = new BaseAreaRoadRtic();
                barr.setArea_id(ss[0]);
                barr.setRoad_id(ss[1]);
                barr.setSeq_no(Integer.parseInt(ss[2]));
                barr.setRtic_id(ss[5]);
                //map.put("rtic_no",ss[4]);
                //map.put("map_version",ss[6]);
                maps.add(barr);
            }
            baseImportService.batchSaveAreaRoadRtic(maps);
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return "fail:"+e.getMessage();
        }
        return "success";
    }

    /**
     * 导入静态数据 r表  -》 link
     * @return
     */
    @PostMapping("/importLink")
    @ResponseBody
    public String importLink(@RequestParam(value = "file") MultipartFile file) {

        try {
//            String filePath = "E:\\document\\济南交警\\文件\\19Q2版本数据\\mif2csv2.0\\source\\Rjinan.csv";
//            String encoding="GBK";
            //File file = new File(filePath);
            //InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(),"gbk"));

            List<BaseLink> links = new ArrayList<>();
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null){
                if(lineTxt.contains("MapID")) {
                    continue;
                }
                String[] ss = lineTxt.replace("\"","").split("\t");
                System.out.println("sss---"+ss[0]);
                String allLine = ss[0];
                String[] line = allLine.split(",");
                String geometry = "";
                for (int i = 46; i < line.length; i++) {
                    geometry += line[i] +";";
                }
                geometry = geometry.substring(0,geometry.length()-1);
                BaseLink link = new BaseLink();
                link.setId(line[1]);
                link.setStartName(line[44]);
                link.setEndName(line[45]);
                link.setDirection(Short.parseShort(String.valueOf(line[5])));
                link.setWidth(Double.parseDouble(String.valueOf(line[4])));
                link.setLength(Double.parseDouble(String.valueOf(line[12])));
                String kind = line[3].substring(1,2);
                link.setKind(kind);
                if(kind.equals("a") || kind.equals("b") || kind.equals("c")) {
                    continue;
                }
                String geo = geometry.substring(22,geometry.length()-1);
                String[] lonlatAll = geo.split(";");
                String coordinates = "";
                for (int i = 0; i < lonlatAll.length; i++) {
                    String lonlat = lonlatAll[i];
                    String[] lonlats = lonlat.split(" ");
                    coordinates += "["+lonlats[0]+","+lonlats[1]+"],";
                }
                coordinates = coordinates.substring(0, coordinates.length()-1);
                link.setGeometry("{\"type\":\"LineString\",\"coordinates\":["+coordinates+"]}");
                links.add(link);
                if(link.getDirection() == 1){//说明这个是双向的，需要增加一个id为-的link
                    BaseLink link_ = new BaseLink();
                    link_.setId("-"+line[1]);
                    link_.setStartName(link.getStartName());
                    link_.setEndName(link.getEndName());
                    link_.setDirection(link.getDirection());
                    link_.setWidth(link.getWidth());
                    link_.setLength(link.getLength());
                    link_.setKind(link.getKind());
                    if(kind.equals("a") || kind.equals("b") || kind.equals("c")) {
                        continue;
                    }
                    String coordinates_ = "";
                    for (int i = lonlatAll.length-1; i >=0; i--) {
                        String lonlat = lonlatAll[i];
                        String[] lonlats = lonlat.split(" ");
                        coordinates_ += "["+lonlats[0]+","+lonlats[1]+"],";
                    }
                    coordinates_ = coordinates_.substring(0, coordinates_.length()-1);
                    link_.setGeometry("{\"type\":\"LineString\",\"coordinates\":["+coordinates_+"]}");
                    links.add(link_);
                }
            }
            bufferedReader.close();
            baseImportService.batchSaveLinkList(links);
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return "fail";
        }
        return "success";

    }


    /**
     * 导入correspondingOfxian表  -》 base_rtic_link
     * @return
     */
    @PostMapping("/importRticLink")
    @ResponseBody
    public String importRticLink(@RequestParam(value = "file") MultipartFile file) {
        try {
            List<BaseRticLink> list = new ArrayList<>();
            InputStream in = file.getInputStream();
            InputStreamReader read = new InputStreamReader(in,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);

            String lineTxt = "";
            while((lineTxt = bufferedReader.readLine()) != null) {
                if(lineTxt.toLowerCase().contains("meshno")) {
                    continue;
                }
                String[] ss = lineTxt.replace("\"","").split(",");
                //rtic解析得到rticid
                long rticid = Long.parseLong(ss[1])*10000+Long.parseLong(ss[2]);
                //ertic解析得到erticic
//                long rticid =Long.parseLong(ss[0] + ss[1] + String.format("%04d",Integer.valueOf(ss[2])));
                int n = 1;
                for (int i = 6; i < ss.length; i+=2) {
                    BaseRticLink rl = new BaseRticLink();
                    rl.setRticid(rticid+"");
                    rl.setLinkid(ss[i]);
                    rl.setSort(n);
                    n++;
                    list.add(rl);
                }
                if(list.size()>=3000) {
                    baseImportService.batchSaveRticLink(list);
                    list = new ArrayList<>();
                }
            }
            baseImportService.batchSaveRticLink(list);
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return "fail:"+e.getMessage();
        }
        return "success";
    }

}
