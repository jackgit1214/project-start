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
-- ----------------------------
-- sso 客户端表结构，来源于spring 官网
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(256)   NOT NULL COMMENT '客户端ID',
    `resource_ids`            varchar(256)   NULL DEFAULT NULL COMMENT '客户端能够访问的资源ID，暂时没用',
    `client_secret`           varchar(256)   NULL DEFAULT NULL COMMENT '客户端密钥',
    `scope`                   varchar(256)   NULL DEFAULT NULL COMMENT '授权范围',
    `authorized_grant_types`  varchar(256)   NULL DEFAULT NULL COMMENT '授权方式',
    `web_server_redirect_uri` varchar(256)   NULL DEFAULT NULL COMMENT '授权导向地址',
    `authorities`             varchar(256)   NULL DEFAULT NULL COMMENT '',
    `access_token_validity`   int        NULL DEFAULT NULl COMMENT 'token访问时间',
    `refresh_token_validity`  int       NULL DEFAULT NULL COMMENT 'token刷新时间',
    `additional_information`  varchar(4096)  NULL DEFAULT NULL COMMENT '附加信息',
    `autoapprove`             varchar(256)   NULL DEFAULT NULL COMMENT '是否自动授权',
    PRIMARY KEY (`client_id`) USING BTREE
) ;

-- ----------------------------
-- 示例数据
-- ----------------------------
INSERT INTO `oauth_client_details`
VALUES ('OrderManagement', NULL, '$2a$10$8yVwRGY6zB8wv5o0kRgD0ep/HVcvtSZUZsYu/586Egxc1hv3cI9Q6', 'all',
        'authorization_code,refresh_token', 'http://localhost:8083/orderSystem/login', NULL, 7200, NULL, NULL, 'true');
INSERT INTO `oauth_client_details`
VALUES ('UserManagement', NULL, '$2a$10$ZRmPFVgE6o2aoaK6hv49pOt5BZIKBDLywCaFkuAs6zYmRkpKHgyuO', 'all',
        'authorization_code,refresh_token', 'http://localhost:7002/sec/login', NULL, 7200, NULL, NULL, 'true');

