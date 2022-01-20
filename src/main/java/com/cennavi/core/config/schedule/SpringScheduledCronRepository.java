package com.cennavi.core.config.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Service
public class SpringScheduledCronRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SpringScheduledCron findByCronKey(String cronKey) {
        String sql = "select * from spring_scheduled_cron where cron_key=? order by createtime";
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
        String sql = "select * from spring_scheduled_cron order by createtime";
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

    public void insertTask(String cron_key, String cron_expression, String task_explain, String status, String cron_name) {
        String sql = "insert into spring_scheduled_cron values(?,?,?,?,?,?)";
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
            jdbcTemplate.update("UPDATE spring_scheduled_cron SET " + sqlvalue + " WHERE cron_id = ?", param.toArray());
        }
    }

    public void deleteTask(String cron_id) {
        String sql = "delete from spring_scheduled_cron where cron_id=?";
        jdbcTemplate.update(sql, new Object[]{cron_id});
    }

    public Map<String, Object> toUpdate(String cron_id) {
        String sql = "select * from  spring_scheduled_cron where cron_id=?";
        return jdbcTemplate.queryForMap(sql, cron_id);
    }

}
