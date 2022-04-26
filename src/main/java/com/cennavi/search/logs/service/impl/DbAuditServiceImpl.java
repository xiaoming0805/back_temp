package com.cennavi.search.logs.service.impl;

import com.cennavi.search.common.ESPage;
import com.cennavi.search.common.ESPageResult;
import com.cennavi.search.logs.model.Audit;
import com.cennavi.search.logs.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 审计日志实现类-数据库
 *
 * @author zlt
 * @date 2020/2/8
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "spring.audit-log.log-type", havingValue = "db")
public class DbAuditServiceImpl implements IAuditService {
    private static final String INSERT_SQL = " insert into sys_logger " +
            " (id,application_name, class_name, method_name, user_id, user_name, client_id, operation, timestamp) " +
            " values (?,?,?,?,?,?,?,?,?)";
    @Autowired
    private  JdbcTemplate jdbcTemplate;
    @Autowired
    private ESPage esPage;
    @PostConstruct
    public void init() {
        String sql = "CREATE TABLE IF NOT EXISTS sys_logger  ( " +
                " id varchar(100)  NOT NULL PRIMARY KEY," +
                " application_name varchar(50) ," +
                " class_name  varchar(128) ," +
                " method_name  varchar(64) ," +
                " user_id  varchar(100) ," +
                " user_name  varchar(50) ," +
                "client_id  varchar(32) ," +
                "operation  varchar(1024) ," +
                "timestamp  varchar(30) " +
                ");";
        System.out.println(sql);
        this.jdbcTemplate.execute(sql);
    }

    @Async
    @Override
    public void save(Audit audit) {
        this.jdbcTemplate.update(INSERT_SQL, UUID.randomUUID().toString().replace("-","")
                , audit.getApplication_name(), audit.getClass_name(), audit.getMethod_name()
                , audit.getUser_id(), audit.getUser_name(), audit.getClient_id()
                , audit.getOperation(), audit.getTimestamp());
    }

    @Override
    public ESPageResult<Audit> getPage(Integer curPage, Integer limit, Map<String, Object> params) {
        List<Object> find_params=new ArrayList<>();
        String base_sql="select * from sys_logger t where 1=1 ";
        Object searchKey=params.get("searchKey");
        Object searchValue=params.get("searchValue");
        Object queryStr=params.get("queryStr");
        if(searchKey!=null&&searchValue!=null&& StringUtils.isNotBlank(searchKey.toString())&&StringUtils.isNotBlank(searchValue.toString())) {
            base_sql += "and t."+searchKey.toString()+" like CONCAT ('%',?,'%')";
            find_params.add(searchValue.toString());
        }
        if(queryStr!=null&&!"".equals(queryStr)){
            base_sql+=" and user_name like CONCAT ('%',?,'%') ";
            find_params.add(queryStr);
        }
        base_sql+=" order by t.timestamp desc";
        return esPage.getPageList(jdbcTemplate,base_sql,find_params,curPage,limit, Audit.class);
    }


}
