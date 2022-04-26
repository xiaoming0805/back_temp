package com.cennavi.modules.inoutflow.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//接口文档的说明
@ApiModel("OD数据返回类")
@Data//不用写get set 方法。
public class OdFlow {
    /**
     * 使用说明
     * example 文档返回字段 样例数据
     * position api文档中显示字段排序
     * @JsonIgnore 注解  比如密码等关键数据 不想返回可以加此注释不返回 密码字段
     */
    @ApiModelProperty(value = "终点中心点")
    //@JsonIgnore
    private String center_d;
    @ApiModelProperty(value = "起点中心点")
    private String center_o;
    @ApiModelProperty(value = "终点区域编码")
    private String code_d;
    @ApiModelProperty(value = "起点区域编码")
    private String code_o;
    @ApiModelProperty(value = "终点区域坐标")
    private String geometry_d;


    @ApiModelProperty(value = "起点区域坐标")
    private String geometry_o;
    @ApiModelProperty(value = "终点区域名字")
    private String name_d;
    @ApiModelProperty(value = "起点区域名字")
    private String name_o;
    @ApiModelProperty(value = "起点到终点数据值")
    private Integer value;
}
