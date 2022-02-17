package com.cennavi.modules.baseimport.beans;


import com.cennavi.core.common.MyTable;

/**
 * Created by sunpengyan on 2019/3/21.
 *  rtic和link关系表
 */
@MyTable("base_rtic_link")
public class BaseRticLink {
    private String rticid;
    private String linkid;
    private int sort;

    public String getRticid() {
        return rticid;
    }

    public void setRticid(String rticid) {
        this.rticid = rticid;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
