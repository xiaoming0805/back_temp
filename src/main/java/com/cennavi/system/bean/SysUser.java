package com.cennavi.system.bean;

import com.cennavi.core.common.MyTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;


@Data
@EqualsAndHashCode(callSuper = false)
@MyTable("sys_user")
public class SysUser  {
	private static final long serialVersionUID = -5886012896705137070L;
	private String id;
	private String username;
	private String password;
	private String nickname;
	private String headImgUrl;
	private String mobile;
	private Integer sex;
	private Integer enabled;
	private String type;
	private String openId;
	private Integer isDel;
	private List<SysRole> roles;
	private String roleId;
	private String oldPassword;
	private String newPassword;
	private Set<String> permissions;
	private String create_time;
	private String update_time;
}
