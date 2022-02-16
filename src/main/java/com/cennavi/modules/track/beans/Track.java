package com.cennavi.modules.track.beans;

import com.cennavi.core.common.MyTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//接口文档的说明
@ApiModel("轨迹类")
@Data//不用写get set 方法。
@MyTable("")//使用BeseDao时，需要bean名和表名一致，不一致则需要加此注解；
public class Track {
    /**
     * 使用说明
     * example 文档返回字段 样例数据
     * position api文档中显示字段排序
     * @JsonIgnore 注解  比如密码等关键数据 不想返回可以加此注释不返回 密码字段
     */
    @ApiModelProperty(value = "电话")
    //@JsonIgnore
    private String mobile;
    @ApiModelProperty(value = "姓名",example = "张三")
    private String realname;
    @ApiModelProperty(value = "经度",example = "123.28110999999998")
    private String x;
    @ApiModelProperty(value = "纬度",example = "40.29022166666667")
    private String y;
    @ApiModelProperty(value = "时间",example = "2021-12-21 09:35:09")
    private String createtime;
}
