package com.cennavi.modules.clickhouse;

import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.ResultObj;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpengyan on 2022/12/9.
 * <dependency>
 * 			<groupId>ru.yandex.clickhouse</groupId>
 * 			<artifactId>clickhouse-jdbc</artifactId>
 * 			<version>0.2.4</version>
 * 		</dependency>
 */
@RestController
@RequestMapping("/ch")
public class TestController extends ResponseUtils {
    @Resource(name="otherJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @RequestMapping("testCH")
    public ResultObj<Object> testCH(@RequestParam(value = "sql",required = false) String sql) {
        if(StringUtils.isBlank(sql)) {
            sql = "select * from tocc.traffic_history";
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return success(list);
    }
}
