
drop table if exists base_area;
create table base_area (
  id varchar(36) not null,
  type varchar(10),
  name varchar(30),
  center varchar(256),
  geometry text,
  geom geometry
)
;
comment on column base_area.id is '区域编号';
comment on column base_area.type is '区域类型 ';
comment on column base_area.name is '区域名称';
comment on column base_area.center is '中心点坐标';
comment on column base_area.geometry is '区域坐标';



drop table if exists base_road;
create table base_road (
  id varchar(36) not null,
  type varchar(10),
  kind varchar(10),
  name varchar(50),
  startname varchar(50),
  endname varchar(50),
  direction varchar(10),
  length float4,
  center varchar(256),
  geometry text,
  geom geometry,
  primary key (id)
);


drop table if exists base_rtic;
create table base_rtic (
  id varchar(50) not null,
  name varchar(20),
  startname varchar(50),
  endname varchar(50),
  length float4,
  kind varchar(10),
  width varchar(10),
  speedlimit varchar(20),
  direction varchar(10),
  startpoint varchar(100),
  endpoint varchar(100),
  geometry text,
  center varchar(100),
  geom geometry,
  primary key (id)
)
;
comment on column base_rtic.name is '路段名称';
comment on column base_rtic.startname is '起始路名';
comment on column base_rtic.endname is '结束路名';
comment on column base_rtic.length is '路段长度  km';
comment on column base_rtic.kind is '路段等级 1-高速 2-快速路  3 主干道 4 次干道支路';
comment on column base_rtic.width is '路段宽度  m';
comment on column base_rtic.speedlimit is '限速';
comment on column base_rtic.direction is '1为北，依次顺时针方向';
comment on column base_rtic.startpoint is '起点经纬度 以空格分隔';
comment on column base_rtic.endpoint is '终点经纬度 以空格分隔';
comment on column base_rtic.geometry is 'geojson格式';
comment on column base_rtic.center is '中心点';



drop table if exists base_area_road_rtic;
create table base_area_road_rtic (
  area_id varchar(50),
  road_id varchar(50),
  seq_no int4,
  rtic_id varchar(50)
);
create index in_base_area_road_rtic_area_id on base_area_road_rtic using btree (area_id);
create index in_base_area_road_rtic_road_id on base_area_road_rtic using btree (road_id);
create index in_base_area_road_rtic_rtic_id on base_area_road_rtic using btree (rtic_id);


drop table if exists base_rtic_link;
create table base_rtic_link (
  rticid varchar(50),
  linkid varchar(50),
  sort int4,
  linklen int4
);
create index in_base_rtic_link_rticid on base_rtic_link using btree (rticid);
create index in_base_rtic_link_linkid on base_rtic_link using btree (linkid);


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