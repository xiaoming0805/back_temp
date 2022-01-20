package com.cennavi.utils;

import com.cennavi.modules.sample.beans.SampleBean;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerJsonSerializer {

    static final String DYNC_INCLUDE = "DYNC_INCLUDE";
    static final String DYNC_FILTER = "DYNC_FILTER";
    ObjectMapper mapper = new ObjectMapper();

    @JsonFilter(DYNC_FILTER)
    interface DynamicFilter {
    }

    @JsonFilter(DYNC_INCLUDE)
    interface DynamicInclude {
    }

    /**
     * @param clazz 需要设置规则的Class
     * @param include 转换时包含哪些字段
     * @param filter 转换时过滤哪些字段
     */
    public void filter(Class<?> clazz, String include, String filter) {
        if (clazz == null) clazz = Map.class;
        if (include != null && include.length() > 0) {
            mapper.setFilterProvider(new SimpleFilterProvider().addFilter(DYNC_INCLUDE,
                    SimpleBeanPropertyFilter.filterOutAllExcept(include.split(","))));
            mapper.addMixIn(clazz, DynamicInclude.class);
        } else if (filter !=null && filter.length() > 0) {
            mapper.setFilterProvider(new SimpleFilterProvider().addFilter(DYNC_FILTER,
                    SimpleBeanPropertyFilter.serializeAllExcept(filter.split(","))));
            mapper.addMixIn(clazz, DynamicFilter.class);
        }
    }

    public String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static void main(String args[]) throws JsonProcessingException {
        CustomerJsonSerializer cjs= new CustomerJsonSerializer();
        // 设置转换 Article 类时，只包含 id, name
        cjs.filter(Object.class, "id,name", null);

        Map map = new HashMap<>();
        map.put("id","111");
        map.put("name","zhagnsna");
        map.put("age",13);
        List<Map> list = new ArrayList<>();
        list.add(map);
        Map map0 = new HashMap<>();
        map0.put("data",list);
        map0.put("code",0);

        String include = cjs.toJson(map0);

        SampleBean sb = new SampleBean();
        sb.setId("111");
        sb.setName("zhangs");
        sb.setCode("adfaf");
        String include1 = cjs.toJson(list);

        cjs = new CustomerJsonSerializer();
        // 设置转换 Article 类时，过滤掉 id, name
        cjs.filter(Map.class, null, "id,name");

        String filter = cjs.toJson(map);

        System.out.println("include: " + include);
        System.out.println("include1: " + include1);
        System.out.println("filter: " + filter);
    }
}
