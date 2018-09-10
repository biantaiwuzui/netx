/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50709
Source Host           : localhost:3306
Source Database       : netx

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2018-07-06 10:24:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for credit
-- ----------------------------
DROP TABLE IF EXISTS `credit`;
CREATE TABLE `credit` (
  `id` varchar(225) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `merchant_id` varchar(225) DEFAULT NULL COMMENT '商家ID',
  `scope` varchar(255) DEFAULT NULL COMMENT '适用范围，json结构',
  `presale_upper_limit` int(11) DEFAULT NULL COMMENT '预售上限，不超过发行额度(issue_number)的10倍，如果无交易额，则上限不超过36900元',
  `description` text COMMENT '描述',
  `issue_number` int(11) DEFAULT NULL COMMENT '发行数量',
  `dividend_setting` varchar(2048) DEFAULT NULL COMMENT '分红设置，json结构',
  `cashier_id` varchar(255) DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
