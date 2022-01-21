
CREATE TABLE If Not Exists "public"."base_scheduled_cron" (
  "cron_id" varchar(88) COLLATE "pg_catalog"."default" NOT NULL,
  "cron_key" varchar(128) COLLATE "pg_catalog"."default",
  "cron_expression" varchar(20) COLLATE "pg_catalog"."default",
  "task_explain" varchar(500) COLLATE "pg_catalog"."default",
  "status" varchar(10) COLLATE "pg_catalog"."default",
  "cron_name" varchar(50) COLLATE "pg_catalog"."default",
  "createtime" timestamp(6) DEFAULT now()
)
;
COMMENT ON COLUMN "public"."base_scheduled_cron"."cron_id" IS '主键id';
COMMENT ON COLUMN "public"."base_scheduled_cron"."cron_key" IS '定时任务完整类名';
COMMENT ON COLUMN "public"."base_scheduled_cron"."cron_expression" IS 'cron表达式';
COMMENT ON COLUMN "public"."base_scheduled_cron"."task_explain" IS '任务描述';
COMMENT ON COLUMN "public"."base_scheduled_cron"."status" IS '状态,1:正常;2:停用';
COMMENT ON COLUMN "public"."base_scheduled_cron"."cron_name" IS '任务名称';
COMMENT ON COLUMN "public"."base_scheduled_cron"."createtime" IS '创建时间';

CREATE TABLE If Not Exists "public"."base_scheduled_logs" (
    "log_id" varchar(64) COLLATE "pg_catalog"."default",
    "cron_key" varchar(64) COLLATE "pg_catalog"."default",
    "begintime" varchar(32) COLLATE "pg_catalog"."default",
    "endtime" varchar(32) COLLATE "pg_catalog"."default",
    "state" varchar(10) COLLATE "pg_catalog"."default",
    "logs" text COLLATE "pg_catalog"."default",
    "timer" varchar(32) COLLATE "pg_catalog"."default"
);
COMMENT ON COLUMN "public"."base_scheduled_logs"."log_id" IS 'id';
COMMENT ON COLUMN "public"."base_scheduled_logs"."cron_key" IS 'cron_key';
COMMENT ON COLUMN "public"."base_scheduled_logs"."begintime" IS '执行开始时间';
COMMENT ON COLUMN "public"."base_scheduled_logs"."endtime" IS '执行结束时间';
COMMENT ON COLUMN "public"."base_scheduled_logs"."state" IS '是否执行成功';
COMMENT ON COLUMN "public"."base_scheduled_logs"."logs" IS '执行日志';
COMMENT ON COLUMN "public"."base_scheduled_logs"."timer" IS '用时';