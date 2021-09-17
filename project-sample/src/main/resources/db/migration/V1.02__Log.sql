
# 修改日志表的请求URL及 请求方法的长度
ALTER TABLE `commondb`.`sys_log`
    MODIFY COLUMN `requestUrl` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求url' AFTER `requestIP`,
    MODIFY COLUMN `requestClassMethod` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求类名方法' AFTER `requestUrl`;