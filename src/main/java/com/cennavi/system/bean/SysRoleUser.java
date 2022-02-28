package com.cennavi.system.bean;


import com.cennavi.core.common.MyTable;
import lombok.*;

/**
 * @author zlt
 * @date 2019/7/30
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MyTable("sys_role_user")
public class SysRoleUser  {
	private String user_id;
	private String role_id;
}
