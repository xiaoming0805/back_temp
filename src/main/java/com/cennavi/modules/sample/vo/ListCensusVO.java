package com.cennavi.modules.sample.vo;

import com.cennavi.core.common.MyTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("test类")
@Data//不用写get set 方法。
public class ListCensusVO {
    @ApiModelProperty(value = "id",example = "主键")
    private String myid;
    @ApiModelProperty(value = "别名",example = "别名")
    private String myname;
}
