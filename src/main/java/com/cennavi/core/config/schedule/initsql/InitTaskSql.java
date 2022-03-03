package com.cennavi.core.config.schedule.initsql;

public class InitTaskSql {
    public static final String  INIT_SQL= "CREATE TABLE If Not Exists \"public\".\"base_scheduled_cron\" (\n" +
            "  \"cron_id\" varchar(88) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"cron_key\" varchar(128) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"cron_expression\" varchar(20) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"task_explain\" varchar(500) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"status\" varchar(10) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"cron_name\" varchar(50) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"createtime\" timestamp(6) DEFAULT now()\n" +
            ")\n" +
            ";\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_cron\".\"cron_id\" IS '主键id';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_cron\".\"cron_key\" IS '定时任务完整类名';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_cron\".\"cron_expression\" IS 'cron表达式';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_cron\".\"task_explain\" IS '任务描述';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_cron\".\"status\" IS '状态,1:正常;2:停用';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_cron\".\"cron_name\" IS '任务名称';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_cron\".\"createtime\" IS '创建时间';\n" +
            "\n" +
            "CREATE TABLE If Not Exists \"public\".\"base_scheduled_logs\" (\n" +
            "    \"log_id\" varchar(64) COLLATE \"pg_catalog\".\"default\",\n" +
            "    \"cron_key\" varchar(64) COLLATE \"pg_catalog\".\"default\",\n" +
            "    \"begintime\" varchar(32) COLLATE \"pg_catalog\".\"default\",\n" +
            "    \"endtime\" varchar(32) COLLATE \"pg_catalog\".\"default\",\n" +
            "    \"state\" varchar(10) COLLATE \"pg_catalog\".\"default\",\n" +
            "    \"logs\" text COLLATE \"pg_catalog\".\"default\",\n" +
            "    \"timer\" varchar(32) COLLATE \"pg_catalog\".\"default\"\n" +
            ");\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_logs\".\"log_id\" IS 'id';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_logs\".\"cron_key\" IS 'cron_key';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_logs\".\"begintime\" IS '执行开始时间';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_logs\".\"endtime\" IS '执行结束时间';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_logs\".\"state\" IS '是否执行成功';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_logs\".\"logs\" IS '执行日志';\n" +
            "COMMENT ON COLUMN \"public\".\"base_scheduled_logs\".\"timer\" IS '用时';";
}
