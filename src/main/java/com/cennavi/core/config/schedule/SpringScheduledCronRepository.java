package com.cennavi.core.config.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
@Service
public class SpringScheduledCronRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SpringScheduledCron findByCronKey(String cronKey) {
        String sql = "select * from base_scheduled_cron  where cron_key=? order by createtime";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, cronKey);
        SpringScheduledCron cron = new SpringScheduledCron();
        cron.setCron_id(map.get("cron_id").toString());
        cron.setCron_key(map.get("cron_key").toString());
        cron.setCron_expression(map.get("cron_expression").toString());
        cron.setTask_explain(map.get("task_explain").toString());
        cron.setStatus(map.get("status").toString());
        cron.setCron_name(map.get("cron_name").toString());
        return cron;
    }

    public List<SpringScheduledCron> findAll() {
        String sql = "select * from base_scheduled_cron  order by createtime";
        List<SpringScheduledCron> list = new ArrayList<>();
        List<Map<String, Object>> listmap = jdbcTemplate.queryForList(sql);
        for (int i = 0; i < listmap.size(); i++) {
            SpringScheduledCron cron = new SpringScheduledCron();
            cron.setCron_id(listmap.get(i).get("cron_id").toString());
            cron.setCron_key(listmap.get(i).get("cron_key").toString());
            cron.setCron_expression(listmap.get(i).get("cron_expression").toString());
            cron.setTask_explain(listmap.get(i).get("task_explain").toString());
            cron.setStatus(listmap.get(i).get("status").toString());
            cron.setCron_name(listmap.get(i).get("cron_name").toString());
            list.add(cron);
        }
        return list;
    }

    public List<Map<String, Object>> findList() {
        //   String sql = "select * from base_scheduled_cron  order by createtime";
        String sql="select t3.*,t4.begintime,t4.endtime,t4.state,t4.logs,t4.timer from base_scheduled_cron t3 \n" +
                "left join (\n" +
                "select t1.begintime,t1.endtime,t1.state,t1.logs,t1.timer,t1.cron_key from base_scheduled_logs t1 right join \n" +
                "(SELECT max(begintime) as begintime , cron_key FROM base_scheduled_logs  GROUP BY cron_key) t2\n" +
                "on t1.begintime=t2.begintime and t1.cron_key=t2.cron_key\n" +
                ") t4\n" +
                "on t3.cron_key=t4.cron_key\n" +
                "order by t3.createtime";
        List<SpringScheduledCron> list = new ArrayList<>();
        List<Map<String, Object>> listmap = jdbcTemplate.queryForList(sql);
        return listmap;
    }

    public void insertTask(String cron_key, String cron_expression, String task_explain, String status, String cron_name) {
        String sql = "insert into base_scheduled_cron values(?,?,?,?,?,?)";
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        jdbcTemplate.update(sql, new Object[]{uuid, cron_key, cron_expression, task_explain, status, cron_name});
    }

    public void updatetTask(String cron_id, String cron_key, String cron_expression, String task_explain, String status, String cron_name) {
        List<Object> param = new ArrayList<>();
        String sqlvalue = "";
        if (!"".equals(cron_key) && cron_key != null) {
            param.add(cron_key);
            sqlvalue += " ,cron_key=?";
        }
        if (!"".equals(cron_expression) && cron_expression != null) {
            param.add(cron_expression);
            sqlvalue += " ,cron_expression=?";
        }
        if (!"".equals(task_explain) && task_explain != null) {
            param.add(task_explain);
            sqlvalue += " ,task_explain=?";
        }
        if (!"".equals(status) && status != null) {
            param.add(status);
            sqlvalue += " ,status=?";
        }
        if (!"".equals(cron_name) && cron_name != null) {
            param.add(cron_name);
            sqlvalue += " ,cron_name=?";
        }
        param.add(cron_id);
        if (!"".equals(sqlvalue)) {
            sqlvalue = sqlvalue.substring(2, sqlvalue.length());
            System.out.println(sqlvalue);
            jdbcTemplate.update("UPDATE base_scheduled_cron SET " + sqlvalue + " WHERE cron_id = ?", param.toArray());
        }
    }

    public void deleteTask(String cron_id) {
        String sql = "delete from base_scheduled_cron where cron_id=?";
        jdbcTemplate.update(sql, new Object[]{cron_id});
    }

    public Map<String, Object> toUpdate(String cron_id) {
        String sql = "select * from  base_scheduled_cron where cron_id=?";
        return jdbcTemplate.queryForMap(sql, cron_id);
    }

    public void InitSql(){
        //初始化 base 任务表
        File f = new File("");
        String cf = null;
        try {
            cf = f.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sql=readFileContent(cf+"/src/main/java/com/cennavi/core/config/schedule/initsql/spring_scheduled_cron.sql");
        jdbcTemplate.update(sql);
        String init_id="00000000000000000000000000000000";
        List<Map<String,Object>> list=jdbcTemplate.queryForList("select * from base_scheduled_cron where cron_id=?",init_id);
        if(list.size()==0){
            String isnertsql="INSERT INTO public.base_scheduled_cron VALUES ('"+init_id+"', 'com.cennavi.core.config.schedule.demo.DemoTask', '*/30 * * * * ?', '日志清理任务不可删除', '1', '日志清理任务', '2022-01-01 00:00:00');";
            jdbcTemplate.update(isnertsql);
        }
    }

    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public void insertLog(String cron_key,String begintime,String endtime,String state,String logs,String timer){
        String sql = "insert into base_scheduled_logs values(?,?,?,?,?,?,?)";
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        jdbcTemplate.update(sql, new Object[]{uuid, cron_key, begintime, endtime, state, logs,timer});
    }

    public String deleteLog(String date){
        String sql = "delete from base_scheduled_logs where begintime < ?";
        int sum=jdbcTemplate.update(sql,date);
        return "本次共删除数据："+sum+"条";
    }

    public Map<String, Object> findLogs(String cron_key,int page,int size) {
        int bg=size*(page-1);
        int ed=size;
        String sql = "select *,COUNT ( * ) OVER ( ) AS total from base_scheduled_logs  where cron_key=? order by begintime desc limit ? offset ?";
        List<Map<String, Object>> listmap = jdbcTemplate.queryForList(sql,cron_key,ed,bg);
        Map<String,Object> map=new HashMap<>();
        if(listmap.size()==0){
            map.put("total",0);
        }else{
            map.put("total",listmap.get(0).get("total"));
        }
        map.put("rows",listmap);
        return map;
    }
}
