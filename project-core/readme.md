##项目说明
   基本框架，集成许多工具类及mybatis基本核心类，
### 项目目录结构 及说明   
以下是主要类的说明
src
├─main
│  ├─java
│  │  ├─com
│  │  │  └─project
│  │  │      ├─core
│  │  │      │  ├─common
│  │  │      │  │  ├─anaotation
│  │  │      │  │  │      DuplicateSubmission.java   重复提交的处理，自动生成tokenid ,用法见用户等基本管理模块
│  │  │      │  │  │      QueryModelParam.java  查询参数注解模型，主发实现针对查询参数queryModel的处理。
│  │  │      │  │  │      SystemLog.java 日志处理注解，添加后自动形成日志，并插入到日志表中，需要有日志服务支持
│  │  │      │  │  ├─aspect
│  │  │      │  │  │      QueryParamAop.java 查询参数aop处理
│  │  │      │  │  │      SystemLogAop.java  日志AOP处理
│  │  │      │  │  ├─converter  定义表单绑定数据转换类
│  │  │      │  │  ├─exception  业务异常处理类
│  │  │      │  │  ├─response
│  │  │      │  │  │      BaseResult.java AJAX响应请求结果
│  │  │      │  │  │      Captcha.java 图像处理类
│  │  │      │  │  │      ReturnCode.java 返回错误及成功代码定义
│  │  │      │  │  └─util 工具类包
│  │  │      │  │      ├─image 图像工具类处理包，生成缩略图等
│  │  │      │  ├─flyway
│  │  │      │  │      FlywayConfig.java flyway配置
│  │  │      │  ├─mybatis
│  │  │      │  │  ├─dao
│  │  │      │  │  │  └─Base
│  │  │      │  │  │          BaseDao.java  基础接口，无实现任何方法
│  │  │      │  │  │          CommSeqMapper.java 序列接口，实现mysql的序列
│  │  │      │  │  │          IDataMapper.java 基本数据查询接口
│  │  │      │  │  │          IDataMapperByPage.java 分页数据查询接口
│  │  │      │  │  │          IDataMapperCRUD.java 基本CRUD接口
│  │  │      │  │  │          IDataMapperWithBlob.java 包含CLOB的处理
│  │  │      │  │  ├─model
│  │  │      │  │  │      BaseModel.java 基本数据模型，各类数据模型的基础
│  │  │      │  │  │      CommSeq.java mysql序列模型
│  │  │      │  │  │      QueryModel.java 查询模型，
│  │  │      │  │  │      SysLog.java 日志定义模型
│  │  │      │  │  ├─service
│  │  │      │  │  │  │  AbstractBusinessService.java service实现接口
│  │  │      │  │  │  │  IBusinessService.java 基本service实现接口，主要是查询业务
│  │  │      │  │  │  │  ICommonService.java service接口，主要是保存与删除业务
│  │  │      │  │  │  │  ILayuiTreeService.java 实现layui树的service
│  │  │      │  │  │  │  IModuleSecurityService.java 结合security使用
│  │  │      │  │  │  │  IUserSecurityService.java 结合security使用
│  │  │      │  │  │  │  LogDetailService.java 日志保存接口
│  │  │      │  │  │  └─impl
│  │  │      │  │  │          SimpleModuleSecurityServiceImpl.java 结合security使用
│  │  │      │  │  │          SimpleUserSecurityServiceImpl.java 结合security使用
│  │  │      │  │  └─util mybatis基本工具包，包含clob处理，分页拦截器及其配置及分页模型
│  │  │      │  └─web
│  │  │      │      ├─advice
│  │  │      │      │      GlobalExceptionHandler.java  web请求异常处理
│  │  │      │      ├─config
│  │  │      │      │      ProjectConfig.java 项目配置属性
│  │  │      │      │      ResourcePathConfig.java 资源路径配置
│  │  │      │      │      SessionListener.java session监听
│  │  │      │      │      WebMvcConfig.java  基本mvc配置
│  │  │      │      ├─interceptor
│  │  │      │      │      ControllerInterceptor.java 控制拦截类的处理
│  │  │      │      │      DuplicateSubmissionInterceptor.java  重复提交拦截类处理
│  │  │      │      └─util 其它
│  │  │      └─generator 基于mybatis生成工具的代码生成器
│  └─resources
│      │  application.properties
│      │  application.yml
│      │  logback-spring.xml
│      ├─bin
│      │      edit.ftl 模块编辑模板
│      │      index.ftl 基本的首页生成模板
└─     └─mapper
            CommSeqMapper.xml 基本的序列mapper文件
            MysqlTablesMapper.xml mysql数据库模板文件
            PublicMapper.xml 公共mapper文件
