/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50709
Source Host           : localhost:3306
Source Database       : netx-stats

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2018-07-20 18:27:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_biz_stat
-- ----------------------------
DROP TABLE IF EXISTS `user_biz_stat`;
CREATE TABLE `user_biz_stat` (
  `id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `biz_type` varchar(255) DEFAULT NULL COMMENT 'friend(好友),article(图文）',
  `real_count` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user_biz_stat
-- ----------------------------

-- ----------------------------
-- Table structure for user_biz_tile
-- ----------------------------
DROP TABLE IF EXISTS `user_biz_tile`;
CREATE TABLE `user_biz_tile` (
  `id` int(11) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `action_time` datetime DEFAULT NULL,
  `friend_user_id` varchar(255) DEFAULT NULL,
  `action_type` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user_biz_tile
-- ----------------------------
