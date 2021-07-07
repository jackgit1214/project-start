-- 创建schema
# CREATE DATABASE `testdb` ;
#
# use `testdb`;
-- 创建公共序列表
DROP TABLE IF EXISTS `comm_seq`;

CREATE TABLE `comm_seq`
(
    `sequence_name` varchar(64) NOT NULL COMMENT '序列名称',
    `current_val`   int         NOT NULL COMMENT '当前值',
    `increment_val` int         NOT NULL DEFAULT '1' COMMENT '步长(跨度)',
    PRIMARY KEY (`sequence_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='mysql中没有序列功能，这里建表，完成序列功能';

LOCK TABLES `comm_seq` WRITE;

INSERT INTO `comm_seq`
VALUES ('module_seq', 13, 1);

UNLOCK TABLES;

-- 取当前序列值
DELIMITER $$
CREATE
    DEFINER = `root`@`localhost` FUNCTION `fun_currval`(sequence_name varchar(64)) RETURNS int
BEGIN
    declare cur_val integer;
    set cur_val = 0;
    SELECT t.current_val
    INTO cur_val
    FROM comm_seq t
    WHERE t.sequence_name = sequence_name;

    return cur_val;
END$$
DELIMITER ;

-- 取序列值
DELIMITER $$
CREATE
    DEFINER = `root`@`localhost` FUNCTION `fun_nextval`(sequence_name varchar(64)) RETURNS int
BEGIN
    declare cur_val integer;
    declare increment_val integer;
    set increment_val = 1;
    set cur_val = 0;

    SELECT t.increment_val
    INTO increment_val
    FROM comm_seq t
    WHERE t.sequence_name = sequence_name;

    UPDATE comm_seq t
    SET t.current_val = t.current_val + increment_val
    WHERE t.sequence_name = sequence_name;
    SELECT t.current_val
    INTO cur_val
    FROM comm_seq t
    WHERE t.sequence_name = sequence_name;

    return cur_val;
end$$
DELIMITER ;

-- 模块表
DROP TABLE IF EXISTS `sys_module`;

CREATE TABLE `sys_module`
(
    `funcid`     varchar(4)   NOT NULL COMMENT 'funcid',
    `funcname`   varchar(40)  NOT NULL COMMENT 'funcname',
    `funtype`    tinyint      DEFAULT NULL COMMENT '1：菜单；2：按钮',
    `moddesc`    varchar(255) NOT NULL COMMENT 'moddesc',
    `IsInUse`    int          NOT NULL COMMENT 'IsInUse',
    `SuperModID` varchar(4)   DEFAULT NULL COMMENT 'SuperModID',
    `funicon`    varchar(255) DEFAULT NULL COMMENT 'funicon',
    `funorder`   smallint     DEFAULT NULL COMMENT 'funorder',
    `urllink`    varchar(200) DEFAULT NULL COMMENT 'urllink',
    `targetdiv`  varchar(50)  DEFAULT NULL COMMENT 'targetdiv',
    `SYSTEM`     tinyint      DEFAULT NULL COMMENT 'SYSTEM',
    PRIMARY KEY (`funcid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='sys_module';


LOCK TABLES `sys_module` WRITE;

INSERT INTO `sys_module`
VALUES ('1', '系统管理', 1, '系统管理', 1, '0', 'fa-adjust', 100, '', '', 1),
       ('100', '系统日志', 1, '系统日志', 1, '1', 'fa-home', 80, '/icon/icon-picker.html', NULL, 1),
       ('1001', '模块管理', 1, '模块管理', 1, '0', 'fa-navicon', 110, '/module/index', NULL, 1),
       ('200', '系统设置', 1, '系统设置', 1, '1', 'fa-cog', 70, '', NULL, NULL),
       ('400', '代码维护', 1, '代码维护', 1, '1', 'fa-code', 90, '/system/code/index', NULL, NULL),
       ('500', '角色管理', 1, '角色维护', 1, '1', 'fa-bank', 30, '/role/index', NULL, NULL),
       ('550', '用户管理', 1, '用户管理', 1, '1', 'fa-american-sign-language-interpreting', 35, '/user/index', NULL, NULL),
       ('600', '定制管理', 1, '定制管理', 1, '1', 'fa-cubes', 50, '/system/custom/index', NULL, NULL),
       ('900', '部门管理', 1, '部门管理', 1, '1', 'fa-newspaper-o', 85, '/dept/index', NULL, NULL);

UNLOCK TABLES;

-- 用户表

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user`
(
    `USERID`     varchar(40)  NOT NULL COMMENT 'USERID',
    `LOGINCODE`  varchar(40)           DEFAULT NULL COMMENT 'LOGINCODE',
    `VALID`      tinyint      NOT NULL DEFAULT '1' COMMENT 'VALID',
    `username`   varchar(100)          DEFAULT NULL COMMENT 'username',
    `PASSWORD`   varchar(100) NOT NULL COMMENT 'PASSWORD',
    `gender`     tinyint               DEFAULT NULL COMMENT 'gender',
    `age`        tinyint               DEFAULT NULL COMMENT 'age',
    `avatar`     mediumblob COMMENT '相片，存储一个大字段信息',
    `birthday`   date                  DEFAULT NULL COMMENT 'birthday',
    `qq`         varchar(20)           DEFAULT NULL COMMENT 'qq',
    `email`      varchar(50)           DEFAULT NULL COMMENT 'email',
    `address`    varchar(100)          DEFAULT NULL COMMENT 'address',
    `phone`      varchar(50)           DEFAULT NULL COMMENT 'phone',
    `userorder`  tinyint               DEFAULT NULL COMMENT 'userorder',
    `USERSTATUS` tinyint               DEFAULT NULL COMMENT 'USERSTATUS',
    PRIMARY KEY (`USERID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='sys_user用户表';


LOCK TABLES `sys_user` WRITE;

INSERT INTO `sys_user`
VALUES ('0000', 'admin', 1, '系统管理员', '$2a$10$7.oHY5rbatpFjCVoS6D.FuS1QX4r3C9VBmdDkJCanNUbxRP5l7y3G', 0, 0, NULL, NULL,
        '', 'sdfasdf@sohu.com', '', '', 0, 1);

UNLOCK TABLES;

-- 组织架构 表
DROP TABLE IF EXISTS `sys_dept`;

CREATE TABLE `sys_dept`
(
    `DEPTID`    varchar(40) NOT NULL COMMENT 'DEPTID',
    `DEPT_NAME` varchar(50)   DEFAULT NULL COMMENT 'DEPT_NAME',
    `SUPERID`   varchar(40)   DEFAULT NULL COMMENT 'SUPERID',
    `sequence`  decimal(3, 0) DEFAULT NULL COMMENT '排序号',
    `ENDFLAG`   tinyint       DEFAULT NULL COMMENT 'ENDFLAG',
    `dept_desc` varchar(4000) DEFAULT NULL COMMENT 'dept_desc',
    PRIMARY KEY (`DEPTID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='sys_dept系统组织结构表';

LOCK TABLES `sys_dept` WRITE;

INSERT INTO `sys_dept`
VALUES ('1', '公司', 'xxx', 0, 0, NULL);

UNLOCK TABLES;

-- 部门人员关系表
DROP TABLE IF EXISTS `sys_dept_user`;

CREATE TABLE `sys_dept_user`
(
    `ID`     varchar(40) NOT NULL COMMENT 'ID',
    `DeptID` varchar(40) NOT NULL COMMENT '与部门表 Sys_Dept 的 DeptID进行关联。已禁用。',
    `UserID` varchar(40) NOT NULL COMMENT 'UserID',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='sys_dept_user组织结构与用户关联关系表';

LOCK TABLES `sys_dept_user` WRITE;

INSERT INTO `sys_dept_user`
VALUES ('e574505abb7d4015b78ce22f0531e956', '1', '0000');

UNLOCK TABLES;

-- 角色
DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role`
(
    `ROLEID`   varchar(40) NOT NULL COMMENT 'ROLEID',
    `ROLENAME` varchar(50)   DEFAULT NULL COMMENT 'ROLENAME',
    `roledesc` varchar(4000) DEFAULT NULL COMMENT 'roledesc',
    `isSystem` varchar(1)    DEFAULT '0' COMMENT 'isSystem',
    PRIMARY KEY (`ROLEID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='sys_role系统角色表';


LOCK TABLES `sys_role` WRITE;

INSERT INTO `sys_role`
VALUES ('6b912123f72a465782c91f0f4d330dec', '系统管理员', '系统管理员', '1');


UNLOCK TABLES;

-- 角色用户关系表
DROP TABLE IF EXISTS `sys_role_user`;

CREATE TABLE `sys_role_user`
(
    `ROLEID` varchar(40) NOT NULL COMMENT 'ROLEID',
    `USERID` varchar(40) NOT NULL COMMENT 'USERID',
    PRIMARY KEY (`ROLEID`, `USERID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='sys_role_user角色用户关系表';

LOCK TABLES `sys_role_user` WRITE;

INSERT INTO `sys_role_user`
VALUES ('6b912123f72a465782c91f0f4d330dec', '0000');

UNLOCK TABLES;

-- 角色功能模块表

DROP TABLE IF EXISTS `sys_role_func`;

CREATE TABLE `sys_role_func`
(
    `funcid` varchar(40) NOT NULL COMMENT 'funcid',
    `roleid` varchar(40) NOT NULL COMMENT 'roleid',
    PRIMARY KEY (`roleid`, `funcid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='sys_role_func角色功能对应关系表';


LOCK TABLES `sys_role_func` WRITE;

INSERT INTO `sys_role_func`
VALUES ('1', '6b912123f72a465782c91f0f4d330dec'),
       ('100', '6b912123f72a465782c91f0f4d330dec'),
       ('1001', '6b912123f72a465782c91f0f4d330dec'),
       ('200', '6b912123f72a465782c91f0f4d330dec'),
       ('400', '6b912123f72a465782c91f0f4d330dec'),
       ('500', '6b912123f72a465782c91f0f4d330dec'),
       ('550', '6b912123f72a465782c91f0f4d330dec'),
       ('600', '6b912123f72a465782c91f0f4d330dec'),
       ('900', '6b912123f72a465782c91f0f4d330dec');

UNLOCK TABLES;

