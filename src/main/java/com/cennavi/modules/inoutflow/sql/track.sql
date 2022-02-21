DROP TABLE IF EXISTS "public"."track";
CREATE TABLE "public"."track" (
                                  "mobile" varchar(20) COLLATE "pg_catalog"."default",
                                  "realname" varchar(50) COLLATE "pg_catalog"."default",
                                  "x" varchar(50) COLLATE "pg_catalog"."default",
                                  "y" varchar(50) COLLATE "pg_catalog"."default",
                                  "createtime" varchar(50) COLLATE "pg_catalog"."default",
                                  "geom" "public"."geometry"
)
;
COMMENT ON COLUMN "public"."track"."mobile" IS '手机号';
COMMENT ON COLUMN "public"."track"."realname" IS '姓名';
COMMENT ON COLUMN "public"."track"."x" IS '经度';
COMMENT ON COLUMN "public"."track"."y" IS '纬度';
COMMENT ON COLUMN "public"."track"."createtime" IS '创建时间';

