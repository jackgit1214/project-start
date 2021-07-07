package com.project.core.common;

public interface SysConstant {

    String INDEX_SIGN = "index"; // 首页存储标志
    String FIRST_LINK_SIGN = "FIRST"; // 一级连接存储标志
    String SECOUND_LINK_SIGN = "SECOUND"; // 二级连接存储标志

    /**
     * 系统缺省管理员
     */
    String SYSDEFAULTMANAGER = "admin";

    /**
     * 系统根菜单缺省值
     */
    String SYSDEFAULTROOTSUPPERID = "0";

    /**
     * 系统缺省口令
     */
    String SYSDEFAULTPASSWORD = "123456";

    /**
     * 系统缺省显示行数
     */
    int SYSDEFAULTROWNUM = 10; // 暂时测试行数

    /* 系统常用分隔常量* */
    String SYSSEPARATOR = "SYSTEM_SEPARATOR";

    // 单个文件大小允许上传为10M
    long SINGLEFILESIZE = 1024 * 1024 * 10;

    // 总文件大小为100M
    long TOTALFILESIZE = 1024 * 1024 * 100;

    // 文件存储路径
    String filePath = "/upload/";

    // 存储文件分类方法
    int CLASSIFIEDTYPE_DATATYPE = 0; // 按数据分类
    int CLASSIFIEDTYPE_FILETYPE = 1; // 按文件类型分类

}
