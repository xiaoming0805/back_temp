
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
│  │  │  │  │  ├─ResultObj.java //返回类 配合swagger使用
│  │  │  │  │  ├─ResponseUtils.java //配合 controller使用 
│  │  │  │  │  └─dao
│  │  │  │  │  │  ├─BaseDao.java //封装jdbc的基础操作
│  │  │  │  │  │  └─impl
│  │  │  │  │  │  │  └─BaseDaoImpl.java //BaseDao实现类
│  │  │  │  ├─config
│  │  │  │  │  ├─schedule
│  │  │  │  │  │  ├─demo  // schedule样例
│  │  │  │  │  │  ├─initsql // schedule初始化sql 程序自动读取 
│  │  │  │  │  │  ├─ScheduledConfig // schedule工具类
│  │  │  │  │  │  ├─ScheduledOfTask // schedule父类，新建的定时任务必须implements它
│  │  │  │  │  │  ├─SpringScheduledCron // schedule bean
│  │  │  │  │  │  ├─SpringScheduledCronRepository // schedule dao层
│  │  │  │  │  │  ├─SpringUtils // schedule 动态读取bean
│  │  │  │  │  │  └─TaskController // schedule Controller 给前端页面调用操作定时任务
│  │  │  │  │  ├─DataSourceConfig.java //多数据源配置
│  │  │  │  │  ├─SwaggerConfig.java 
│  │  │  │  │  ├─MyWebConfig.java 
│  │  │  │  │  └─SpringContextUtil.java 
│  │  │  │  ├─exception
│  │  │  │  │  ├─GlobalExceptionHandler.java 
│  │  │  │  │  ├─GlobalException.java 
│  │  │  │  │  └─AuthException.java
│  │  │  │  ├─filter
│  │  │  │  │  └─WebContextFilter.java
│  │  │  │  └─interceptor
│  │  │  │  │  └─CommonInterceptor.java //拦截器-需要登录时修改此类
│  │  │  ├─modules //各模块代码
│  │  │  │  ├─schedule //存放定时任务类 需要 implements ScheduledOfTask 参考com.cennavi.core.config.schedule.demo.DemoTask
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

管理端地址
http://ip:port/项目名/web/login.html
