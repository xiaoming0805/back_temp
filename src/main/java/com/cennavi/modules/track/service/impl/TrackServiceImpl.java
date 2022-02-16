package com.cennavi.modules.track.service.impl;

import com.cennavi.core.common.PageResult;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.dao.SampleDao;
import com.cennavi.modules.sample.service.SampleService;
import com.cennavi.modules.track.beans.Track;
import com.cennavi.modules.track.dao.TrackDao;
import com.cennavi.modules.track.service.TrackService;
import com.cennavi.utils.DateUtils;
import com.cennavi.utils.GeomtryUtils;
import com.cennavi.utils.PositionUtils;
import com.cennavi.utils.SendUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 样例service
 * Created by sunpengyan on 2021/1/5.
 */
@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackDao trackDao;

    @Value("${trackOpUrl}")
    private String trackUrl;


    @Override
    public List<Track> trackOptimize(String name, String stime, String etime) throws JsonProcessingException {
        //1.先根据人员名字与时间查询出对应的GPS轨迹点数据，按照时间正序排列
        List<Track> list = trackDao.getTrack(name,stime,etime);
        //2.将查询出来的GPS 84坐标转为02坐标
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode params = JsonNodeFactory.instance.objectNode();
        List<ObjectNode> subInfs = new ArrayList<>();
        for (int i = 0; i < list.size() - 1; i++) {
            Track currentTrack = list.get(i);
            String cX = currentTrack.getX();
            String cY = currentTrack.getY();

            Double[] currentPoints = PositionUtils.gps84_To_Gcj02(Double.parseDouble(cX), Double.parseDouble(cY));

            Track nextTrack = list.get(i+1);
            String nX = nextTrack.getX();
            String nY = nextTrack.getY();

            Double[] nextPoints = PositionUtils.gps84_To_Gcj02(Double.parseDouble(nX), Double.parseDouble(nY));

            double dis = GeomtryUtils.GetDistance(currentPoints[0],currentPoints[1],
                    nextPoints[0],nextPoints[1]);
            ObjectNode coors = JsonNodeFactory.instance.objectNode();
            //两次坐标不一致并且距离大于一定米数既可以进行使用
            if(!(cX.equals(nX) && cY.equals(nY)) && dis > 48){
                coors.put("lon",currentPoints[0]+"");
                coors.put("lat",currentPoints[1]+"");
                coors.put("tm", DateUtils.DateFormatUnit.DATE_TIME.getDateByStr(currentTrack.getCreatetime()).getTime()/1000);
                subInfs.add(coors);
            }
        }
        params.put("coordtype","02");//坐标类型
        params.put("deviceid","22222");//设备id
        params.put("fill_mode","driving");//轨迹补充模式
        params.put("extensions","road_info");//返回结果控制
        params.set("point_list",mapper.readTree(mapper.writeValueAsString(subInfs)));
        //调用轨迹优化的接口，传递对应参数
        String result = SendUtils.sendPost(trackUrl, params.toString());
        JsonNode rootNode = mapper.readTree(result);
        JsonNode data = rootNode.path("data");
        JsonNode tracks = data.path("tracks");
        List<List<Double>> coordinates = new ArrayList<>();

        List<Track> re = new ArrayList<>();
        for (int t = 0; t < tracks.size(); t++) {
            Track track = new Track();
            Double lon = tracks.get(t).path("lon").asDouble();
            Double lat = tracks.get(t).path("lat").asDouble();
            track.setX(lon + "");
            track.setY(lat + "");
            track.setCreatetime(DateUtils.DateFormatUnit.DATE_TIME.getDateStr(tracks.get(t).path("tm").asLong()*1000));
            re.add(track);
            List<Double> c = new ArrayList<>();
            c.add(lon);
            c.add(lat);
            coordinates.add(c);
        }
        ObjectNode geometry = JsonNodeFactory.instance.objectNode();
        geometry.put("type", "LineString");
        geometry.set("coordinates", mapper.readTree(mapper.writeValueAsString(coordinates)));
        System.out.println("优化后的轨迹数据:"+geometry);
        return re;
    }
}
