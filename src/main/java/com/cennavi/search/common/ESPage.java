package com.cennavi.search.common;

import com.cennavi.system.common.WebPageResult;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ESPage {
    public ESPageResult getPageList(JdbcTemplate jdbcTemplate, String base_sql, List<Object> find_params, Integer page, Integer limit, Class Z){
        int start=(page-1)*limit;
        int end=limit;
        String sql=" select *,COUNT ( * ) OVER ( ) AS total from  ("+base_sql+") tttttttt_1 " ;
        sql+=" limit "+end+" OFFSET "+start;
        //System.out.println(sql);
        List<Map<String,Object>> list =jdbcTemplate.queryForList(sql, find_params.toArray());//BeanPropertyRowMapper.newInstance(z),
        long total=0;
        if(list.size()==0){
            total=0;
        }else{
            total=Long.valueOf(list.get(0).get("total").toString());
        }
        return  ESPageResult.builder().data(toBean(list,Z)).code(0).count(total).build();
    }
    /**
     * 把 jdbcTemplate 生成的 List<Map<String, Object>> 数据转化为 List<bean>
     *
     * @param map
     * @param T
     * @param <T>
     * @return
     */
    public static <T> List<T> toBean(List<Map<String, Object>> map, Class<T> T) {
        List<T> result = null;
        if (map == null) {
            return result;
        } else {
            result = new ArrayList<>();
            for (int i = 0; i < map.size(); i++) {
                result.add(toBean(map.get(i), T));
            }
        }
        return result;
    }

    /**
     * 把 jdbcTemplate 生成的 Map<String, Object> 数据转化为bean
     *
     * @param map
     * @param class1
     * @param <T>
     * @return
     */
    public static <T> T toBean(Map<String, Object> map, Class<T> class1) {
        T bean = null;
        try {
            bean = class1.newInstance();
            BeanUtils.populate(bean, map);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }


}
