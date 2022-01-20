-- ----------------------------
-- Table structure for spring_scheduled_cron
-- ----------------------------
DROP TABLE IF EXISTS "public"."spring_scheduled_cron";
CREATE TABLE "public"."spring_scheduled_cron" (
  "cron_id" varchar COLLATE "pg_catalog"."default" NOT NULL,
  "cron_key" varchar(128) COLLATE "pg_catalog"."default",
  "cron_expression" varchar(20) COLLATE "pg_catalog"."default",
  "task_explain" varchar(500) COLLATE "pg_catalog"."default",
  "status" varchar(10) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."spring_scheduled_cron"."cron_id" IS '主键id';
COMMENT ON COLUMN "public"."spring_scheduled_cron"."cron_key" IS '定时任务完整类名';
COMMENT ON COLUMN "public"."spring_scheduled_cron"."cron_expression" IS 'cron表达式';
COMMENT ON COLUMN "public"."spring_scheduled_cron"."task_explain" IS '任务描述';
COMMENT ON COLUMN "public"."spring_scheduled_cron"."status" IS '状态,1:正常;2:停用';

-- ----------------------------
-- Records of spring_scheduled_cron
-- ----------------------------
INSERT INTO "public"."spring_scheduled_cron" VALUES ('abcd1234', 'cn.com.cennavi.service.base.schedule.demo.DemoTask', '*/5 * * * * ?', '动态定時任务demo', '1');

-- ----------------------------
-- Primary Key structure for table spring_scheduled_cron
-- ----------------------------
ALTER TABLE "public"."spring_scheduled_cron" ADD CONSTRAINT "spring_scheduled_cron_pkey" PRIMARY KEY ("cron_id");
