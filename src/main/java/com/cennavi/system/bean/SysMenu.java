package com.cennavi.system.bean;

import com.cennavi.core.common.MyTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@MyTable("sys_menu")
public class SysMenu  {
	private static final long serialVersionUID = 749360940290141180L;
	private String id;
	private String create_time;
	private String update_time;
	private String parent_id;
	private String name;
	private String css;
	private String url;
	private String path;
	private Integer sort;
	private Integer type;
	private Integer hidden;
	private String tenant_id;
	/**
	 * 请求的类型
	 */
	private String path_method;
	private List<SysMenu> subMenus;
	private String roleId;
	private Set<String> menuIds;
}
