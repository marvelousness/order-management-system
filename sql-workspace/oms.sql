/*
Navicat MySQL Data Transfer

Source Server         : localhost_13306
Source Server Version : 50728
Source Host           : localhost:13306
Source Database       : oms

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date                  : 2020-08-08 11:37:46
Author                : 981247127@qq.com
*/

-- 关闭外键检查
SET FOREIGN_KEY_CHECKS=0;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS oms DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

-- 使用并切换数据库
USE oms;


-- ----------------------------
-- Table structure for oms_area
-- ----------------------------
DROP TABLE IF EXISTS `oms_area`;
CREATE TABLE `oms_area` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '地区的名称',
  `pid` int(11) NOT NULL COMMENT '上级地区的ID，顶级地区填 0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地区表';

-- ----------------------------
-- Table structure for oms_customer
-- ----------------------------
DROP TABLE IF EXISTS `oms_customer`;
CREATE TABLE `oms_customer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '该条数据创建的时间',
  `creator_id` bigint(20) NOT NULL COMMENT '该条数据的创建者的ID，参考 user 表的主键',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '表示该数据已经被删除，非零即真',
  `name` varchar(50) DEFAULT NULL COMMENT '客户名称',
  `nick` varchar(50) DEFAULT NULL COMMENT '客户昵称',
  `wangwang` varchar(50) DEFAULT NULL COMMENT '客户的旺旺号',
  `qq` varchar(50) DEFAULT NULL COMMENT '客户的QQ号',
  `wechat` varchar(50) DEFAULT NULL COMMENT '客户的微信号',
  `phone` varchar(50) DEFAULT NULL COMMENT '客户的手机号',
  `region` varchar(100) DEFAULT NULL COMMENT '地域',
  `remark` tinytext COMMENT '备注内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户信息表';

-- ----------------------------
-- Records of oms_customer
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order
-- ----------------------------
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `number` varchar(30) NOT NULL COMMENT '订单编号',
  `customer_id` bigint(20) NOT NULL COMMENT '该订单应归属于哪个客户，参考 customer 表的主键',
  `creator_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT '订单创建人的ID，参考 user 表的主键',
  `executor_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT '订单执行人的ID，参考 user 表的主键',
  `amount` decimal(30,10) unsigned NOT NULL COMMENT '订单服务金额',
  `deal_time` datetime NOT NULL COMMENT '该订单的成交时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '该订单的创建时间',
  `remark` tinytext COMMENT '订单备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_order_number` (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单信息表';

-- ----------------------------
-- Records of oms_order
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_payment
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_payment`;
CREATE TABLE `oms_order_payment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) unsigned NOT NULL COMMENT '订单的ID，参考 order 表的主键',
  `agent_id` bigint(20) unsigned NOT NULL COMMENT '付款信息受理人的ID，参考 user 表的主键',
  `amount` decimal(30,10) unsigned NOT NULL COMMENT '付款金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `payment_time` datetime NOT NULL COMMENT '付款时间',
  `payment_way` varchar(100) NOT NULL COMMENT '付款方式',
  `payment_account` varchar(100) NOT NULL COMMENT '付款账号',
  `payment_proof` varchar(2048) NOT NULL COMMENT '付款凭证',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单付款信息表';

-- ----------------------------
-- Records of oms_order_payment
-- ----------------------------

-- ----------------------------
-- Table structure for oms_sys_department
-- ----------------------------
DROP TABLE IF EXISTS `oms_sys_department`;
CREATE TABLE `oms_sys_department` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `pid` bigint(20) unsigned NOT NULL COMMENT '父部门id，根部门为0',
  `name` varchar(100) NOT NULL COMMENT '部门名称',
  `disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '表示该数据已经被禁用，非零即真',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '表示该数据已经被删除，非零即真',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='系统核心表 - 表示部门信息';

-- ----------------------------
-- Records of oms_sys_department
-- ----------------------------
INSERT INTO `oms_sys_department` VALUES ('1', '0', '奇思妙想工作室', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('2', '1', '总经办', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('3', '2', '财务部', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('4', '2', '人力资源部', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('5', '2', '业务部', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('6', '2', '运营部', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('7', '4', '人事部', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('8', '4', '行政部', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('9', '5', '陌拜组', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('10', '5', '调研组', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('11', '6', '运营一组', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('12', '6', '运营二组', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('13', '6', '运营三组', '0', '0');
INSERT INTO `oms_sys_department` VALUES ('14', '2', '客满部', '0', '0');

-- ----------------------------
-- Table structure for oms_sys_department_user_relation
-- ----------------------------
DROP TABLE IF EXISTS `oms_sys_department_user_relation`;
CREATE TABLE `oms_sys_department_user_relation` (
  `dep_id` bigint(20) unsigned NOT NULL COMMENT '部门ID，参考 department 表的主键',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户ID，参考 user 表的主键',
  `is_leader` tinyint(1) NOT NULL DEFAULT '0' COMMENT '表示该用户是该部门的领导，非零即真',
  PRIMARY KEY (`dep_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统核心表 - 表示部门与用户关联表';

-- ----------------------------
-- Records of oms_sys_department_user_relation
-- ----------------------------

-- ----------------------------
-- Table structure for oms_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `oms_sys_user`;
CREATE TABLE `oms_sys_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` varchar(20) NOT NULL COMMENT '用户ID，代表员工的工号，也是用作登录的用户名',
  `mobile` varchar(20) NOT NULL COMMENT '手机号码，也是用作登录的手机号码',
  `email` varchar(30) NOT NULL COMMENT '电子邮箱，也是用作登录的邮箱',
  `password` varchar(100) NOT NULL COMMENT '登录凭据，用作登录时的密码',
  `name` varchar(20) NOT NULL COMMENT '员工名字',
  `gender` tinyint(1) NOT NULL DEFAULT '1' COMMENT '员工性别，0表示女，其他非零的值均被视作性别男',
  `hired_date` datetime NOT NULL COMMENT '入职日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NOT NULL COMMENT '表示该数据已经被删除，非零即真',
  `is_disabled` tinyint(1) NOT NULL COMMENT '表示该数据已经被禁用，非零即真',
  `avatar` varchar(1024) NOT NULL COMMENT '员工头像，当值为 default 的时候，采用默认的头像',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_user_userid` (`userid`),
  UNIQUE KEY `ix_user_mobile` (`mobile`),
  UNIQUE KEY `ix_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='系统核心表 - 表示用户信息';

-- ----------------------------
-- Records of oms_sys_user
-- ----------------------------
INSERT INTO `oms_sys_user` VALUES ('1', 'root', '02700000000', 'root@root.com', 'b9be11166d72e9e3ae7fd407165e4bd2', '管理员', '1', '2020-08-13 16:23:08', '2020-08-17 16:12:52', '0', '0', 'default');
