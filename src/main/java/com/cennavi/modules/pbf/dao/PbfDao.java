package com.cennavi.modules.pbf.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by sunpengyan on 2022/2/15.
 */
public interface PbfDao {

    List<Map<String, Object>> getDeviceListPbf(String wktStr);
}
