
````
main
├─java
│  └─com
│  │  └─cennavi
│  │  │  ├─ServiceTemplate2Application.java //启动类
│  │  │  ├─core //核心代码
│  │  │  │  ├─common
│  │  │  │  │  ├─IgnoreColumn.java //实体类字段和数据库表字段映射注解，配合BaseDao使用
│  │  │  │  │  ├─MyConstant.java //系统通用的文字配置
│  │  │  │  │  ├─MyTable.java  //实体类名和数据库表的映射注解，配合BaseDao使用
│  │  │  │  │  ├─PageResult.java //分页，配合BaseDao使用
│  │  │  │  │  └─dao
│  │  │  │  │  │  ├─BaseDao.java //封装jdbc的基础操作
│  │  │  │  │  │  └─impl
│  │  │  │  │  │  │  └─BaseDaoImpl.java //BaseDao实现类
│  │  │  │  ├─config
│  │  │  │  │  ├─DataSourceConfig.java //多数据源配置
│  │  │  │  │  ├─MyWebConfig.java 
│  │  │  │  │  └─ScheduleConfig.java
│  │  │  │  ├─filter
│  │  │  │  │  └─WebContextFilter.java
│  │  │  │  └─interceptor
│  │  │  │  │  └─CommonInterceptor.java //拦截器-需要登录时修改此类
│  │  │  ├─modules //各模块代码
│  │  │  │  ├─module-name //模块代码，名称和数据库使用表的前缀对应
│  │  │  │  │  ├─beans
│  │  │  │  │  ├─controller
│  │  │  │  │  ├─service 
│  │  │  │  │  └─dao
│  │  │  │  └─SpringTimers.java //定时任务
│  │  │  └─utils //工具类
└─resources
    ├─application.properties //配置文件
    └─static
```



api接口文档地址
http://ip:port/项目名/doc.html

定时任务管理地址
http://ip:port/项目名/task.html
