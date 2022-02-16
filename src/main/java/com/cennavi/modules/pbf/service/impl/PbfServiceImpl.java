package com.cennavi.modules.pbf.service.impl;

import com.cennavi.modules.pbf.dao.PbfDao;
import com.cennavi.modules.pbf.pbfUtils.TileUtils;
import com.cennavi.modules.pbf.service.PbfService;
import com.vector.tile.VectorTileEncoder;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpengyan on 2022/2/15.
 */
@Service
public class PbfServiceImpl implements PbfService{

    @Autowired
    PbfDao pbfDao;

    @Override
    public byte[] listDevicePbf(int x, int y, int z) {
        //转换成wkt字符串
        String wktStr = TileUtils.parseXyz2Bound2_Mercator(x, y, z);
        List<Map<String, Object>> list = pbfDao.getDeviceListPbf(wktStr);

        VectorTileEncoder vtm = new VectorTileEncoder(4096, 16, false);
        try {
            for (Map<String, Object> map : list) {
                String geom = (String) map.get("wkt");
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("id",map.get("id"));
                hashMap.put("name",map.get("name"));
                Geometry line = new WKTReader().read(geom);
                TileUtils.convert2Piexl_Mercator(x,y,z,line);

                //生成pbf格式数据
                vtm.addFeature("deviceData",hashMap, line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vtm.encode();
    }
}
