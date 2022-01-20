package com.cennavi.modules.sample.beans;

import com.cennavi.core.common.MyTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//接口文档的说明
@ApiModel("demo类")
@Data//不用写get set 方法。
@MyTable("sample")//使用BeseDao时，需要bean名和表名一致，不一致则需要加此注解；
public class SampleBean {
    /**
     * 使用说明
     * example 文档返回字段 样例数据
     * position api文档中显示字段排序
     * @JsonIgnore 注解  比如密码等关键数据 不想返回可以加此注释不返回 密码字段
     */
    @ApiModelProperty(value = "主键" )
    private String id;
    @ApiModelProperty(value = "姓名",example = "张三")
    private String name;
    @ApiModelProperty(value = "编码",example = "zhangs")
    private String code;
    @ApiModelProperty(value = "年龄",example = "28", position = 1)
    @JsonIgnore
    private int age;
}
