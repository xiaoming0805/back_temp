-- ----------------------------
-- Table structure for base_area
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_area";
CREATE TABLE "public"."base_area" (
                                      "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
                                      "type" varchar(10) COLLATE "pg_catalog"."default",
                                      "name" varchar(30) COLLATE "pg_catalog"."default",
                                      "center" varchar(256) COLLATE "pg_catalog"."default",
                                      "geometry" text COLLATE "pg_catalog"."default",
                                      "geom" "public"."geometry"
)
;

-- ----------------------------
-- Table structure for base_area_road_rtic
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_area_road_rtic";
CREATE TABLE "public"."base_area_road_rtic" (
                                                "area_id" varchar(50) COLLATE "pg_catalog"."default",
                                                "road_id" varchar(50) COLLATE "pg_catalog"."default",
                                                "seq_no" int4,
                                                "rtic_id" varchar(50) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for base_link
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_link";
CREATE TABLE "public"."base_link" (
                                      "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
                                      "geometry" text COLLATE "pg_catalog"."default",
                                      "rtic_id" varchar(36) COLLATE "pg_catalog"."default",
                                      "start_name" varchar(255) COLLATE "pg_catalog"."default",
                                      "end_name" varchar(255) COLLATE "pg_catalog"."default",
                                      "direction" int2,
                                      "width" float8,
                                      "length" float8,
                                      "kind" varchar(5) COLLATE "pg_catalog"."default",
                                      "geom" "public"."geometry"
)
;
COMMENT ON TABLE "public"."base_link" IS '21q1link';

-- ----------------------------
-- Table structure for base_road
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_road";
CREATE TABLE "public"."base_road" (
                                      "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
                                      "type" varchar(10) COLLATE "pg_catalog"."default",
                                      "kind" varchar(10) COLLATE "pg_catalog"."default",
                                      "name" varchar(50) COLLATE "pg_catalog"."default",
                                      "startname" varchar(50) COLLATE "pg_catalog"."default",
                                      "endname" varchar(50) COLLATE "pg_catalog"."default",
                                      "direction" varchar(10) COLLATE "pg_catalog"."default",
                                      "length" float4,
                                      "center" varchar(256) COLLATE "pg_catalog"."default",
                                      "geometry" text COLLATE "pg_catalog"."default",
                                      "geom" "public"."geometry",
                                      "center_geom" "public"."geometry"
)
;

-- ----------------------------
-- Table structure for base_rtic
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_rtic";
CREATE TABLE "public"."base_rtic" (
                                      "id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                      "name" varchar(20) COLLATE "pg_catalog"."default",
                                      "startname" varchar(20) COLLATE "pg_catalog"."default",
                                      "endname" varchar(20) COLLATE "pg_catalog"."default",
                                      "length" float4,
                                      "kind" varchar(10) COLLATE "pg_catalog"."default",
                                      "width" varchar(10) COLLATE "pg_catalog"."default",
                                      "speedlimit" varchar(20) COLLATE "pg_catalog"."default",
                                      "direction" varchar(10) COLLATE "pg_catalog"."default",
                                      "startpoint" varchar(100) COLLATE "pg_catalog"."default",
                                      "endpoint" varchar(100) COLLATE "pg_catalog"."default",
                                      "geometry" text COLLATE "pg_catalog"."default",
                                      "center" varchar(100) COLLATE "pg_catalog"."default",
                                      "geom" "public"."geometry",
                                      "center_geom" "public"."geometry"
)
;

-- ----------------------------
-- Table structure for base_rtic_link
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_rtic_link";
CREATE TABLE "public"."base_rtic_link" (
                                           "rticid" varchar(50) COLLATE "pg_catalog"."default",
                                           "linkid" varchar(50) COLLATE "pg_catalog"."default",
                                           "sort" int4
)
;

-- ----------------------------
-- Primary Key structure for table base_area
-- ----------------------------
ALTER TABLE "public"."base_area" ADD CONSTRAINT "base_area_copy_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table base_area_road_rtic
-- ----------------------------
CREATE INDEX "inx_area_id_base_a_r_r" ON "public"."base_area_road_rtic" USING btree (
    "area_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );
CREATE INDEX "inx_road_id_base_a_r_r" ON "public"."base_area_road_rtic" USING btree (
    "road_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );
CREATE INDEX "inx_rtic_id_base_a_r_r" ON "public"."base_area_road_rtic" USING btree (
    "rtic_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table base_road
-- ----------------------------
ALTER TABLE "public"."base_road" ADD CONSTRAINT "base_road_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table base_rtic
-- ----------------------------
ALTER TABLE "public"."base_rtic" ADD CONSTRAINT "base_rtic_pkey" PRIMARY KEY ("id");


DROP TABLE IF EXISTS "public"."rticfreespeed";
CREATE TABLE "public"."rticfreespeed" (
                                          "rticid" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                          "kind" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                          "speed" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                          "samplenum" varchar(50) COLLATE "pg_catalog"."default",
                                          "length" float4 NOT NULL,
                                          "weight" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                          "version" varchar(50) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."rticfreespeed"."rticid" IS 'rticid';
COMMENT ON COLUMN "public"."rticfreespeed"."kind" IS 'rtic等级';
COMMENT ON COLUMN "public"."rticfreespeed"."speed" IS '自由流速度';
COMMENT ON COLUMN "public"."rticfreespeed"."samplenum" IS '样本数量';
COMMENT ON COLUMN "public"."rticfreespeed"."length" IS 'rtic长度';

-- ----------------------------
-- Table structure for rticpattern
-- ----------------------------
DROP TABLE IF EXISTS "public"."rticpattern";
CREATE TABLE "public"."rticpattern" (
                                        "rticid" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                        "kind" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                        "speed" float8 NOT NULL,
                                        "length" float8 NOT NULL,
                                        "traveltime" float8 NOT NULL,
                                        "time" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                        "week" int4
)
;