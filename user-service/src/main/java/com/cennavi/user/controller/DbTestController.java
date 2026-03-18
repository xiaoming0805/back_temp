package com.cennavi.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/db")
public class DbTestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test")
    public String testConnection() {
        try {
            // 查询PostgreSQL版本
            List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT version()");
            return "数据库连接成功！PostgreSQL版本: " + result.get(0).get("version");
        } catch (Exception e) {
            return "数据库连接失败: " + e.getMessage();
        }
    }
}