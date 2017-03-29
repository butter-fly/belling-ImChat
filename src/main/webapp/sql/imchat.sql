/*
MySQL Data Transfer
Source Host: localhost
Source Database: imchat
Target Host: localhost
Target Database: imchat
Date: 2017/3/29 10:01:41
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `userid` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
  `sex` int(1) DEFAULT NULL COMMENT '性别',
  `age` int(5) DEFAULT NULL COMMENT '年龄',
  `profilehead` varchar(255) DEFAULT NULL COMMENT '头像',
  `profile` varchar(255) DEFAULT NULL COMMENT '简介',
  `firsttime` varchar(255) DEFAULT NULL COMMENT '注册时间',
  `lasttime` varchar(255) DEFAULT NULL COMMENT '最后登录时间',
  `status` int(1) DEFAULT NULL COMMENT '账号状态(1正常 0禁用)',
  KEY `userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `t_user` VALUES ('王五', '123', '测试用户', '1', '23', null, null, '2017-01-11 19:22:21', '2017-03-27 16:27:03', '1');
INSERT INTO `t_user` VALUES ('管理员', '123', '蝴蝶飞飞', '1', '23', null, null, '2017-01-11 19:22:21', '2017-03-27 16:31:54', '1');
INSERT INTO `t_user` VALUES ('张三', '123', '张三', '1', '34', null, null, null, null, '1');
INSERT INTO `t_user` VALUES ('李四', '123', '李四', '1', '15', null, null, null, null, '1');
