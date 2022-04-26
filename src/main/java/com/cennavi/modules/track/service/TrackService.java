package com.cennavi.modules.track.service;

import com.cennavi.modules.track.beans.Track;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * Created by sunpengyan on 2021/1/5.
 */
public interface TrackService {

    /**
     *
     * @param name 轨迹人员的名字
     * @param stime 轨迹的开始时间
     * @param etime 轨迹的结束时间
     * @return
     */
    List<Track> trackOptimize(String name, String stime, String etime) throws JsonProcessingException;

}
