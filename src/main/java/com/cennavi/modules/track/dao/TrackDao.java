package com.cennavi.modules.track.dao;

import com.cennavi.core.common.dao.BaseDao;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.track.beans.Track;

import java.util.List;
import java.util.Map;

/**
 * 样例dao
 * Created by sunpengyan on 2021/1/5.
 */
public interface TrackDao extends BaseDao<SampleBean> {

    /**
     * 根据名称模糊查询sample
     * @param name 名称
     * @return List<SampleBean>
     */
    List<Track> getTrack(String name,String stime,String etime);
}
