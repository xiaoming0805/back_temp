package com.cennavi.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sunpengyan on 2019-4-12.
 */
public class JsonUtils {
//
//    /**
//     * 序列化对象
//     * @param obj 对象
//     * @param args 排除字段
//     * @param bool 是否需要过滤空值 null或者false 不需要过滤
//     * @return String
//     */
//    public static String objectToJson(Object obj, String[] args, boolean... bool) {
//        //属性过滤器对象
//        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
//
//        //属性排斥集合,强调某些属性不需要或者一定不能被序列化
//        Set<String> excludes = filter.getExcludes();
//
//        //属性包含集合,强调仅需要序列化某些属性.具体用哪一个,看实际情况.此处我用的前者
//        //Set<String> includes = filter.getIncludes();
//
//        //排除不需序列化的属性
//        for (String string : args) {
//            excludes.add(string);
//        }
//
//        //调用fastJson的方法,对象转json,
//        //参数一:需要被序列化的对象
//        //参数二:用于过滤属性的过滤器
//        //参数三:关闭循环引用,若不加这个,页面无法展示重复的属性值
//        String string = "";
//        if(bool!=null && bool.length>0 && bool[0]) {
//            string = JSONObject.toJSONString(obj, filter);
//        } else {
//            string = JSONObject.toJSONString(obj, filter, SerializerFeature.WriteMapNullValue);
//        }
//        string = string.replace("\\\"","\"").replace("\"{","{").replace("}\"","}").replace("\"[","[").replace("]\"","]");//updated sunpengyan
//        return string;
//    }
//
//    /**
//     * 序列化对象
//     * @param obj 对象
//     * @param args 保留字段
//     * @return String
//     */
//    public static String inObjectToJson(Object obj, String[] args) {
//        //属性过滤器对象
//        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
//
//        //属性排斥集合,强调某些属性不需要或者一定不能被序列化
//        Set<String> includes = filter.getIncludes();
//        includes.add("errcode");
//        includes.add("errmsg");
//        includes.add("total");
//        includes.add("rows");
//        includes.add("data");
//        //属性包含集合,强调仅需要序列化某些属性.具体用哪一个,看实际情况.此处我用的前者
//        //Set<String> includes = filter.getIncludes();
//
//        //排除不需序列化的属性
//        for (String string : args) {
//            includes.add(string);
//        }
//
//        //调用fastJson的方法,对象转json,
//        //参数一:需要被序列化的对象
//        //参数二:用于过滤属性的过滤器
//        //参数三:关闭循环引用,若不加这个,页面无法展示重复的属性值
//        String string = JSONObject.toJSONString(obj, filter, SerializerFeature.WriteMapNullValue);
//        return string;
//    }
//
//
//    public static JSONObject packJson(Object obj) {
//        JSONObject json = new JSONObject();
//        json.put("errcode", 0);
//        json.put("errmsg", "success");
//        json.put("data",obj);
//        return json;
//    }
//
//    public static JSONObject packJson(Map map) {
//        JSONObject json = new JSONObject();
//        json.put("errcode", 0);
//        json.put("errmsg", "success");
//        if(map != null) {
//            json.put("data",map);
//        }
//        return json;
//    }
//
//    public static JSONObject packJsonErr(String errmsg) {
//        JSONObject json = new JSONObject();
//        json.put("errcode",-1);
//        json.put("errmsg","失败"+errmsg);
//        return json;
//    }
//
//    public static JSONObject packJsonSuceess(String sucessMsg) {
//        JSONObject json = new JSONObject();
//        json.put("errcode",0);
//        json.put("errmsg",sucessMsg);
//        return json;
//    }
//
//    public static JSONObject packJson2(List list) {
//        JSONObject json = new JSONObject();
//        json.put("errcode", 0);
//        json.put("errmsg", "success");
//        json.put("data",list);
//        return json;
//    }
//    public static JSONObject packJson2(Object list) {
//        JSONObject json = new JSONObject();
//        json.put("errcode", 0);
//        json.put("errmsg", "success");
//        json.put("data",list);
//        return json;
//    }
//
//    //为了适应2.0返回
//    public static JSONObject packJson2(String msg) {
//        JSONObject json = new JSONObject();
//        json.put("errcode", 0);
//        json.put("errmsg", "success");
//        json.put("data",msg);
//        return json;
//    }
//
//    public static JSONObject packJson2(JSONArray list) {
//        JSONObject json = new JSONObject();
//        json.put("errcode", 0);
//        json.put("errmsg", "success");
//        json.put("data",list);
//        return json;
//    }
//
//    //为了适应专家版2.0返回
//    public static JSONObject packJsonExpert(List list) {
//        JSONObject json = new JSONObject();
//        json.put("stateCode",200);
//        json.put("message","");
//        json.put("data",list);
//        return json;
//    }
//    public static JSONObject packJsonExpert(JSONObject list) {
//        JSONObject json = new JSONObject();
//        json.put("stateCode",200);
//        json.put("message","");
//        json.put("data",list);
//        return json;
//    }
//    public static JSONObject packJsonExpert(String msg) {
//        JSONObject json = new JSONObject();
//        json.put("stateCode",200);
//        json.put("message","");
//        json.put("data",msg);
//        return json;
//    }
}
