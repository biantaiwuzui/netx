/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50709
Source Host           : localhost:3306
Source Database       : netx

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2018-07-06 11:33:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for credit_subscription
-- ----------------------------
DROP TABLE IF EXISTS `credit_subscription`;
CREATE TABLE `credit_subscription` (
  `id` varchar(512) NOT NULL,
  `credit_id` varchar(512) NOT NULL,
  `user_id` varchar(512) NOT NULL,
  `subscription_number` int(11) DEFAULT NULL COMMENT '认购数量',
  `deleted` bit(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
