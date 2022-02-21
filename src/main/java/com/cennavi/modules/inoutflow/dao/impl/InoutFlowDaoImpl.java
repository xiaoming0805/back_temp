package com.cennavi.modules.inoutflow.dao.impl;

import com.cennavi.modules.inoutflow.dao.InoutFlowDao;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.dao.SampleDao;
import com.cennavi.core.common.dao.impl.BaseDaoImpl;
import com.cennavi.modules.track.beans.Track;
import com.cennavi.modules.track.dao.TrackDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Repository
public class InoutFlowDaoImpl extends BaseDaoImpl<SampleBean> implements InoutFlowDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void makeAndSaveFlowODBySql(int type, String date) {
        //r1.获取车牌和指定类型区域的关系
        //r2.获取车牌最早和最晚出现的行政区划,注意修改此值来修改基准区域t2.type='2'
        //r3.将1和2关联去重, mincode和code、code和maxcode分别组成od,进行统计，然后在将结果分组求和\n" +
        String sql = "with r1 as (select distinct t1.carplate,t2.code from bayonet_passcar t1,bayonet_relation t2 where t1.kkid = t2.kkid\n" +
                "and  t1.datetime>=:date and t1.datetime<:enddate \n" +
                "), r2 as (\n" +
                "select t3.carplate,split_part(minmaxcode,',',1) as mincode,\n" +
                "coalesce(NULLIF(split_part(minmaxcode,',', 2), ''),split_part(minmaxcode, ',', 1)) as maxcode from (\n" +
                "  select carplate,string_agg(code,',') minmaxcode from (\n" +
                "   select *,row_number() over (partition by carplate order by datetime) rn1,\n" +
                "   row_number() over (partition by carplate order by datetime desc) rn2 from bayonet_passcar\n" +
                "   where datetime>=:date and datetime<:enddate  order by carplate,datetime\n" +
                "  ) t1,bayonet_relation t2 where t1.kkid = t2.kkid and (rn1=1 or rn2=1) group by carplate\n" +
                ") t3) " +
                "insert into flow_od (code_o,code_d,value,type,date) \n" +
                "select code_o,code_d,sum(count),:type,:date from (\n" +
                " select mincode as code_o,code as code_d,count(1) from (\n" +
                "   select r1.code,r2.* from r1 left join r2 on r1.carplate = r2.carplate  where code != mincode\n" +
                " ) t4 group by code,mincode union all\n" +
                " select code as code_o,maxcode as code_d,count(1) from (\n" +
                "   select r1.code,r2.* from r1 left join r2 on r1.carplate = r2.carplate  where code != maxcode\n" +
                " ) t5 group by code,maxcode\n" +
                ") t6 group by code_o,code_d order by code_o,code_d";
        Map<String,Object> param = new HashMap<>();
        param.put("type",type);
        param.put("date",date);
        param.put("enddate",date+" 24");
        NamedParameterJdbcTemplate nameJdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        System.out.println("开始执行od统计");
        nameJdbc.update(sql,param);
        System.out.println("结束执行od统计");
    }


    @Override
    public List<Map<String, Object>> getFlowOD(int type, String date, String code) {
        String condition = "";
        List<String> param = new ArrayList<>();
        if(type>0) {
            condition = " and code_d = ?";
            if(type == 2) {//流出时，指定区域是o
                condition = " and code_o = ?";
            }
            param.add(code);
        }
        String sql = "select t1.*,t2.center as center_o,t2.geometry as geometry_o,t2.name as name_o,t3.center as center_d," +
                "t3.geometry as geometry_d,t3.name as name_d from (\n" +
                "  select code_o,code_d,value from flow_od where date = ? " + condition +
                ") t1 left join base_area t2 on t1.code_o = t2.id\n" +
                "left join base_area t3 on t1.code_d = t3.id";
        param.add(date);
        return jdbcTemplate.queryForList(sql,param.toArray());
    }
}
