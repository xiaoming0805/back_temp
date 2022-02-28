package com.cennavi.system.bean;

import com.cennavi.core.common.MyTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@MyTable("sys_role")
public class SysRole {
    private String id;
    private String code;
    private String name;
    private String tenant_id;
    private String create_time;
    private String update_time;
}
