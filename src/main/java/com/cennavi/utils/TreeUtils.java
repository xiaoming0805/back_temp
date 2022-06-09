package com.cennavi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpengyan on 2020/7/29.
 */
public class TreeUtils {

    public static List<Map<String, Object>> list2Tree(List<Map<String, Object>> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            if (map.get("parent_id") == null) {//判断是否是一级
                List<Map<String, Object>> childList = getChild(list, map.get("id").toString());
                if (childList.size()>0 && map.containsKey("num")){
                    map.put("num",childList.stream().mapToLong((o1)-> (Long) o1.get("num")).sum());
                }
                map.put("child", childList);
                result.add(map);
            }
        }
        // result.sort((o1,o2)->((Integer)o1.get("sort")).compareTo((Integer) o2.get("sort")));
        return result;
    }

    private static List<Map<String, Object>> getChild(List<Map<String, Object>> list , String kid) {
        List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            //判断是否是kid的子菜单，并且排除本身
            if (map.get("parent_id") != null && kid.equals(map.get("parent_id"))) {
                List<Map<String, Object>> childs = getChild(list, map.get("id").toString());
                if (childs.size()>0 && map.containsKey("num")){
                    map.put("num",childs.stream().mapToLong((o1)-> (Long) o1.get("num")).sum());
                }
                map.put("child",childs.size() >0 ? childs : "[]");
                arrayList.add(map);
            }
        }
        return arrayList;
    }

}
