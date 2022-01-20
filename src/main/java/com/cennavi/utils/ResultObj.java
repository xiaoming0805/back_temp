package com.cennavi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by sunpengyan on 2022/1/14.
 */
public class ResultObj {
    private String errcode;
    private String errmsg;
    private Object data;

    public ResultObj(){}

    public ResultObj(Object data){
        this.errcode = "1";
        this.errmsg = "成功";
        this.data = data;
    }

    public ResultObj(boolean bool, String errmsg){
        this.errcode = bool?"1":"-1";
        this.errmsg = errmsg;
    }

    public ResultObj(String errcode, String errmsg){
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public ResultObj(String errcode, String errmsg, Object data){
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.data = data;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultObj{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }

    public String toJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode obj = mapper.createObjectNode();
        obj.put("errcode",errcode);
        obj.put("errmsg",errmsg);
        //obj.put("data",data);
        return "ResultObj{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
