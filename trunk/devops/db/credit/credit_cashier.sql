/*
Navicat MySQL Data Transfer

Source Server         : Wz
Source Server Version : 50720
Source Host           : 112.74.200.88:3306
Source Database       : netx

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-07-11 18:17:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for credit_cashier
-- ----------------------------
DROP TABLE IF EXISTS `credit_cashier`;
CREATE TABLE `credit_cashier` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '标识ID',
  `merchant_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '商家id',
  `merchant_user_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '商家注册者用户ID',
  `merchant_user_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '商家管理人员类型：法人代表，收银人员，业务主管',
  `cashier_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '收银人员姓名',
  `cashier_phone` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '收银人手机号',
  `cashier_network_num` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '收银人网号',
  `is_current` int(2) NOT NULL DEFAULT '0' COMMENT '是否商家使用中：0：不是 1：是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
