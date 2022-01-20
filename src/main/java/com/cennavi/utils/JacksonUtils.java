package com.cennavi.utils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by sunpengyan on 2022/1/5.
 */
public class JacksonUtils {

    /**
     * 定义一个类或接口
     */
    @JsonFilter("fieldFilter")
    interface FieldFilterMixIn{
    }

    static final String DYNC_INCLUDE = "DYNC_INCLUDE";
    static final String DYNC_FILTER = "DYNC_FILTER";
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
    public static ObjectMapper filter(Class<?> clazz, String include, String filter) {
        ObjectMapper mapper = new ObjectMapper();
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
        return mapper;
    }

    public static String objectToJson(Object object, String[] args) {
        ObjectMapper mapper = filter(ResultObj.class, "", StringUtils.join(args,","));
        String json;
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            json = packJsonErr(e.getMessage()).toString();
        }
        return json;
    }

    public static ObjectNode packJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);//设置为false
        ObjectNode json = mapper.createObjectNode();
        json.put("errcode", 0);
        json.put("errmsg", "success");
        json.set("data", mapper.convertValue(obj, JsonNode.class));
        return json;
    }

    public static ObjectNode packJson(Object obj, String[] args) throws JsonProcessingException {
        ObjectMapper mapper = filter(null, "", StringUtils.join(args,","));

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);//设置为false
        ObjectNode json = mapper.createObjectNode();
        json.put("errcode", 0);
        json.put("errmsg", "success");

        json.set("data", mapper.convertValue(obj, JsonNode.class));
        return json;
    }

    public static ObjectNode packJsonErr(String errmsg) {
        ObjectNode json = objectMapper.createObjectNode();
        json.put("errcode",-1);
        json.put("errmsg","失败"+errmsg);
        return json;
    }

    public static ObjectNode packJsonSuceess(String sucessMsg) {
        ObjectNode json = objectMapper.createObjectNode();
        json.put("errcode",0);
        json.put("errmsg",sucessMsg);
        return json;
    }


    private static ObjectMapper objectMapper = new ObjectMapper();
    // 日起格式化
    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //String str = "{\"type\":\"geojson\",\"data\":{\"type\":\"FeatureCollection\",\"features\":[{\"id\":\"fe2dfc29c6135d5b1ddc98dcff29fdb6\",\"type\":\"Feature\",\"properties\":{\"fillOutlineColor\":\"#00a8f3\",\"fillColor\":\"#00a8f3\",\"fillOpacity\":1},\"geometry\":{\"coordinates\":[[[108.83795890837206,34.3271601396483],[108.83795890837206,34.29399268013357],[108.88994853489686,34.29399268013357],[108.88994853489686,34.3271601396483],[108.83795890837206,34.3271601396483]]],\"type\":\"Polygon\"}}]}}";
        Map<String,Object> map = new HashMap<>();
        map.put("date1","2021-01-05 10:22:31");
        map.put("date1","20210105102231");

        ObjectNode objN = packJson(map);
        objN.put("aa",11);
        JsonNode data = objN.get("data");



        String str = "{}";
        JsonNode jsonNode = mapper.readTree(str);
        ObjectNode obj = JsonNodeFactory.instance.objectNode();
        ArrayNode arr = JsonNodeFactory.instance.arrayNode();
        arr.add("aaa");
        arr.add(12);
        obj.set("arr11",arr);

        System.out.println(jsonNode.get("coordinates").get(0));
    }
}
