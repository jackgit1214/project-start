# springboot 基础框架
## 描述    
   实现基本组织结构 、人员、角色、权限、单点登录等功能，后端spring 系列以及mybatsi实现，前端layui + freemarker实现,后期考虑用vue以及react 实现前后端分离。
   过程中单点登录由于版本的变化参考了很多资料，主要来源于官网，以及百度收集的资料，很多坑。填坑的过程，也学习了很多。
## 技术特点
   spring 系列，权限框架加入security，单点登录采用spring-security-oauth2实现，集成了代码生成工具
    数据库采用mysql8 
## project-core 
   架构核心，主要是对以前项目中用到的工具进行了整合，包含了mybatis分页工具，查询的封装，代码生成工具、异常的处理、项目通用配置、数据库版本管理、security配置以及部分工具类等。    
## project-sso
  单点登录框架依   
## project-sample
   项目实例，实现单点登录客户端管理、人员的管理、模块管理、部门管理、角色授权管理      
   依赖于project-sso
   用户管理：完成      
   权限管理：完成      
   角色管理：完成      
   组织结构管理：完成   
   数据字典管理：完成    
   数据指标管理：完成    
   模块管理：完成  
   基本代码生成工具：完成  

## sso-client
    单点客户端，简单实现，依赖于spring-security-oauth2   
## sso-client2
    单点客户端，简单实现，依赖于spring-boot-starter-oauth2-client
## project-api
    前后端分离api, 用户、角色、权限、组织结构等
## project-vue      
    结合后端api,基于vue框架的用户权限管理，
    用户管理：完成
    权限管理：完成
    角色管理：完成
    组织结构管理：完成
    数据字典：完成 ，vuex实现
    ![image](https://github.com/jackgit1214/resource/blob/main/image/login.jpg)  
    ![image](https://github.com/jackgit1214/resource/blob/main/image/first.jpg)  
    ![image](https://github.com/jackgit1214/resource/blob/main/image/user.jpg)   

![image](https://github.com/jackgit1214/resource/blob/main/image/vue.gif)
    


## project-react-admin
    利用后端API，基于react框架（redux,sagga等），结合element-ui react框架的用户权限管理。          
    基本框架：完成     
    用户管理：完成     
      
##相关资源

  QQ：19707910   
  spring: https://spring.io/    
  elementUi-plus: https://element-plus.org/#/zh-CN  
  layUi: https://www.layui.com/     
  vue.js 官网：https://v3.vuejs.org/   
  vue-router 文档：http://router.vuejs.org/zh-cn/index.html/   
  vuex 文档：http://vuex.vuejs.org/    
  webpack 文档：https://webpack.github.io/docs/    
  ES2015 入门教程：http://es6.ruanyifeng.com/    
  scss 文档：http://sass-lang.com/documentation/file.SASS_REFERENCE.html   
  mocha 文档: http://mochajs.org/ 

