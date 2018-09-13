/*
Navicat MySQL Data Transfer

Source Server         : dev
Source Server Version : 50720
Source Host           : 112.74.200.88:3306
Source Database       : netx

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-09-13 10:16:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for active_log
-- ----------------------------
DROP TABLE IF EXISTS `active_log`;
CREATE TABLE `active_log` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `relatable_type` varchar(100) DEFAULT NULL COMMENT '关联类型，值为Model名',
  `relatable_id` varchar(32) DEFAULT NULL COMMENT '关联ID',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文章标题',
  `pic` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '封面图直接存图片url',
  `atta` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '附件',
  `content` text CHARACTER SET utf8mb4 COMMENT '文章内容',
  `length` int(11) DEFAULT '0' COMMENT '图文字数',
  `who` tinyint(4) DEFAULT '0' COMMENT '谁可以看此图文动态：\r\n            0：所有人\r\n            1：全部好友\r\n            2：我关注的人\r\n            3：关注我的人\r\n            4：指定用户（可多个）',
  `receiver` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '当who字段为指定好友时，该字段有效。将指定好友的id以逗号分隔保存',
  `is_anonymity` bit(1) DEFAULT b'0' COMMENT '是否匿名（是不是隐身发布）：\r\n            0：不匿名\r\n            1：匿名',
  `is_show_location` bit(1) DEFAULT b'0' COMMENT '是否显示位置',
  `is_illegal` bit(1) DEFAULT b'0' COMMENT '是否违规',
  `advertorial_type` tinyint(4) DEFAULT '0' COMMENT '软文类型：\r\n            0：免费图文\r\n            1：软文【广告】 ',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `top` tinyint(4) DEFAULT '0' COMMENT '置顶：\r\n            1：列表置顶。\r\n            2：分类置顶。',
  `location` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发布时的位置信息',
  `top_expired_at` datetime DEFAULT NULL COMMENT '置顶过期时间',
  `amount` bigint(11) DEFAULT '0' COMMENT '押金(以分为单位)（点击费用都是从这里扣掉）',
  `amount_type` tinyint(4) DEFAULT NULL COMMENT '押金类型：\r\n            1：零钱\r\n            2：网币',
  `status_code` tinyint(4) DEFAULT '0' COMMENT '状态码：\r\n            0：正常/上架\r\n            1：异常\r\n            2：待审核\r\n            3：待交押金\r\n            4：押金余额不足\r\n            5：下架',
  `reason` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原因',
  `is_lock` tinyint(1) DEFAULT '0' COMMENT '是否封禁',
  `hits` bigint(20) DEFAULT '0' COMMENT '点击数',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `worth_type` varchar(30) DEFAULT NULL,
  `worth_ids` varchar(329) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for article_click_history
-- ----------------------------
DROP TABLE IF EXISTS `article_click_history`;
CREATE TABLE `article_click_history` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `article_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for article_collect
-- ----------------------------
DROP TABLE IF EXISTS `article_collect`;
CREATE TABLE `article_collect` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `article_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_collect` bit(1) DEFAULT b'0' COMMENT '是否是收藏（取消收藏时更改此字段）',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for article_tags
-- ----------------------------
DROP TABLE IF EXISTS `article_tags`;
CREATE TABLE `article_tags` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `article_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tag_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` varchar(32) NOT NULL,
  `merchant_id` varchar(255) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：\r\n0：待结算\r\n1：已下单\r\n2：已支付',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for cart_item
-- ----------------------------
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item` (
  `id` varchar(32) NOT NULL,
  `cart_id` varchar(32) NOT NULL,
  `product_id` varchar(32) NOT NULL,
  `quantity` int(10) NOT NULL DEFAULT '0' COMMENT '数量',
  `sku_id` varchar(32) NOT NULL COMMENT '库存id',
  `unit_price` bigint(20) DEFAULT NULL,
  `status` varchar(32) NOT NULL DEFAULT '0' COMMENT '交易状态',
  `delivery_way` int(20) DEFAULT NULL COMMENT '配送方式\r\n1：支持配送\r\n2：不提供配送，仅限现场消费\r\n3：提供外卖配送',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '类目名',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父集的类目id',
  `used_count` bigint(20) DEFAULT '0' COMMENT '被使用数量',
  `priority` bigint(30) NOT NULL COMMENT '一级目录排序',
  `py` varchar(32) DEFAULT NULL COMMENT '拼音首字母',
  `icon` varchar(32) DEFAULT NULL COMMENT '类别类型',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商家类目表';

-- ----------------------------
-- Table structure for category_product
-- ----------------------------
DROP TABLE IF EXISTS `category_product`;
CREATE TABLE `category_product` (
  `id` varchar(32) NOT NULL,
  `category_id` varchar(500) DEFAULT NULL COMMENT '商品类目',
  `merchant_id` varchar(255) DEFAULT NULL,
  `product_id` varchar(32) DEFAULT NULL COMMENT '商品id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品类目关系表';

-- ----------------------------
-- Table structure for category_property
-- ----------------------------
DROP TABLE IF EXISTS `category_property`;
CREATE TABLE `category_property` (
  `id` varchar(32) NOT NULL,
  `category_id` varchar(32) NOT NULL,
  `merchant_id` varchar(32) NOT NULL,
  `property_id` varchar(32) NOT NULL,
  `property_type` tinyint(4) NOT NULL COMMENT '1销售属性,2关键属性',
  `multi_value` tinyint(1) NOT NULL COMMENT '是否为多值属性',
  `search` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否被搜索用于在前台显示,0不显示,1显示',
  `priority` int(11) NOT NULL COMMENT '排序字段，数字越小排的越前',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `deleted` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品中心-类目属性映射';

-- ----------------------------
-- Table structure for category_property_value
-- ----------------------------
DROP TABLE IF EXISTS `category_property_value`;
CREATE TABLE `category_property_value` (
  `id` varchar(32) NOT NULL,
  `category_id` varchar(32) DEFAULT NULL,
  `merchant_id` varchar(32) DEFAULT NULL,
  `property_id` varchar(32) DEFAULT NULL,
  `value_id` varchar(32) DEFAULT NULL,
  `description` varchar(511) DEFAULT NULL,
  `priority` int(11) DEFAULT '0' COMMENT '排序字段，数字越小排的越前',
  `create_time` datetime DEFAULT '1970-01-01 00:00:00',
  `update_time` datetime DEFAULT '1970-01-01 00:00:00',
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品中心-类目属性属性值映射';

-- ----------------------------
-- Table structure for common_add_friend
-- ----------------------------
DROP TABLE IF EXISTS `common_add_friend`;
CREATE TABLE `common_add_friend` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `send_time` datetime DEFAULT NULL,
  `dispose_time` datetime DEFAULT NULL,
  `dispose_state` int(11) DEFAULT '0',
  `has_read` int(11) DEFAULT '0',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_arbitration_result
-- ----------------------------
DROP TABLE IF EXISTS `common_arbitration_result`;
CREATE TABLE `common_arbitration_result` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `from_user_credit_point` decimal(10,2) DEFAULT '0.00',
  `from_user_credit_point_reason` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扣除投诉人信用值得理由',
  `to_user_credit_point` decimal(10,2) DEFAULT '0.00',
  `to_user_credit_point_reason` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扣除被投诉人的信用值理由',
  `refund_radio_button` tinyint(4) DEFAULT NULL COMMENT ' 同意退款',
  `refund_arbitrate_reason` tinyint(4) DEFAULT NULL COMMENT '不同意退款',
  `return_radio_button` tinyint(4) DEFAULT NULL COMMENT '同意退货',
  `return_arbitrate_reason` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '同意退货的理由',
  `substract_release_frozen_money_refund` int(11) DEFAULT NULL COMMENT '扣除发行者冻结的金额：就是说退款:0.否      1.是',
  `substract_release_frozen_money_reason` varchar(300) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '选中扣除发行者冻结的金额的原因',
  `op_common_answer` tinyint(4) DEFAULT NULL COMMENT '通用仲裁管理员仲裁的答案:0-----不同意,1-----同意',
  `op_common_reason` varchar(300) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '通用仲裁管理员同意的辩解',
  `descriptions` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '如果是拒绝受理,这就是理由',
  `status_code` int(11) DEFAULT NULL COMMENT '仲裁的状态:1.等待申诉,2.受理中,3.对方已申诉,等待处理,4.已裁决,拒绝受理',
  `create_time` datetime NOT NULL COMMENT '扣除发行者冻结的金额：就是说退款:0.否      1.是',
  `op_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作者的ID',
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_area
-- ----------------------------
DROP TABLE IF EXISTS `common_area`;
CREATE TABLE `common_area` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `value` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_article_limit
-- ----------------------------
DROP TABLE IF EXISTS `common_article_limit`;
CREATE TABLE `common_article_limit` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `user_network_num` varchar(20) DEFAULT NULL,
  `operator_nickname` varchar(20) DEFAULT NULL,
  `user_nickname` varchar(20) DEFAULT NULL,
  `user_level` int(11) DEFAULT NULL,
  `user_age` int(11) DEFAULT NULL,
  `user_sex` int(11) DEFAULT NULL,
  `reason` varchar(2000) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `limit_measure` tinyint(4) DEFAULT NULL,
  `release_time` datetime DEFAULT NULL,
  `limit_value` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_bill
-- ----------------------------
DROP TABLE IF EXISTS `common_bill`;
CREATE TABLE `common_bill` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `wallet_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '钱包id',
  `pay_channel` int(11) DEFAULT NULL COMMENT '交易方式，0微信，1支付宝，2网币，3零钱 4.红包',
  `type` tinyint(4) DEFAULT NULL COMMENT '流水类型：0平台，1经营',
  `trade_type` int(11) DEFAULT NULL COMMENT '交易类型，0支出，1收入',
  `amount` bigint(11) DEFAULT '0' COMMENT '金额',
  `total_amount` bigint(20) DEFAULT NULL COMMENT '余额',
  `description` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `third_bill_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '第三方单号',
  `to_account` tinyint(4) DEFAULT '2' COMMENT '第三方充值提现用:0未到账 1已到账 2表示未用到此字段',
  `bak1` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bak2` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bak3` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bak4` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bak5` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_cost_setting
-- ----------------------------
DROP TABLE IF EXISTS `common_cost_setting`;
CREATE TABLE `common_cost_setting` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shared_fee` decimal(10,2) DEFAULT '0.00' COMMENT '分成',
  `withdraw_fee` decimal(10,2) DEFAULT '0.00' COMMENT '提现手续费',
  `shop_manager_fee` decimal(10,2) DEFAULT '0.00' COMMENT '注册商家管理费',
  `shop_manager_fee_limit_date` int(11) DEFAULT NULL COMMENT '注册商家管理费有效期/0:终身有效，1一年，3三年',
  `shop_category_fee` decimal(10,2) DEFAULT '0.00' COMMENT '商品一级类目收费  元/个',
  `shop_tags_fee` decimal(10,2) DEFAULT '0.00' COMMENT '商家二级类目收费  元/个',
  `credit_issue_fee` decimal(10,2) DEFAULT '0.00' COMMENT '网币发行费',
  `credit_funds_interest` decimal(10,2) DEFAULT '0.00' COMMENT '网币竞购系数',
  `credit_subscribe_fee` decimal(10,2) DEFAULT '0.00' COMMENT '网币报名认购费用',
  `credit_inst` decimal(10,2) DEFAULT '0.00' COMMENT '网币资金利息',
  `pic_and_voice_publish_deposit` decimal(10,2) DEFAULT '0.00' COMMENT '图文、音视的发布押金',
  `click_fee` decimal(10,2) DEFAULT '0.00' COMMENT '点击费用',
  `violation_click_fee` decimal(10,2) DEFAULT '0.00' COMMENT ' 违规图文、音视的点击费用',
  `wish_capital_manage_fee` decimal(10,2) DEFAULT '0.00' COMMENT '心愿资金管理费',
  `saler_shared_fee` decimal(10,2) DEFAULT '0.00' COMMENT '销售收入分成',
  `state` tinyint(4) DEFAULT NULL COMMENT '审核状态,0false,1true',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `dispose_user` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核人',
  `dispose_time` datetime DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_deposit_bill
-- ----------------------------
DROP TABLE IF EXISTS `common_deposit_bill`;
CREATE TABLE `common_deposit_bill` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `trade_type` int(11) DEFAULT NULL COMMENT '交易类型，0支出，1收入',
  `amount` bigint(11) DEFAULT '0' COMMENT '金额(以分为单位)',
  `description` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `third_bill_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '第三方单号',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_evaluate
-- ----------------------------
DROP TABLE IF EXISTS `common_evaluate`;
CREATE TABLE `common_evaluate` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `from_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `p_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '事件id',
  `type_name` varchar(300) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '事件名称',
  `evaluate_type` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '评论类型',
  `order_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单id',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_examine_finance
-- ----------------------------
DROP TABLE IF EXISTS `common_examine_finance`;
CREATE TABLE `common_examine_finance` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `interest` decimal(10,2) DEFAULT '0.00' COMMENT '银行利息',
  `check_difference` decimal(10,2) DEFAULT '0.00' COMMENT '核算差额',
  `adjust_account_reason` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '调整账户原因',
  `extract_money` decimal(10,2) DEFAULT '0.00' COMMENT '提现金额',
  `extract_money_reason` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '提现金额原因',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `status` tinyint(4) DEFAULT NULL COMMENT '审核状态 1审核通过 2不通过 3 提交审核',
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核理由',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_friends
-- ----------------------------
DROP TABLE IF EXISTS `common_friends`;
CREATE TABLE `common_friends` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `master_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `friend_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_group
-- ----------------------------
DROP TABLE IF EXISTS `common_group`;
CREATE TABLE `common_group` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `group_id` bigint(20) NOT NULL COMMENT '极光群id',
  `group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '群名',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '群主id',
  `pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for common_group_user
-- ----------------------------
DROP TABLE IF EXISTS `common_group_user`;
CREATE TABLE `common_group_user` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '群id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户id',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户备注',
  `admin` bit(1) DEFAULT b'0' COMMENT '是否是管理员',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for common_im_history
-- ----------------------------
DROP TABLE IF EXISTS `common_im_history`;
CREATE TABLE `common_im_history` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '记录id',
  `from_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送者id',
  `to_user_id` varchar(32) DEFAULT NULL COMMENT '接收者Id',
  `message_payload` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '网值类型：\r\n网能：Activity\r\n网友：User\r\n网商：Product\r\n网信：Credit',
  `type_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '事件id',
  `push_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `doc_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `send_time` datetime NOT NULL,
  `is_read` bit(1) DEFAULT b'0' COMMENT '是否已读',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_job
-- ----------------------------
DROP TABLE IF EXISTS `common_job`;
CREATE TABLE `common_job` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `class_name` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cron_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_jpush_message
-- ----------------------------
DROP TABLE IF EXISTS `common_jpush_message`;
CREATE TABLE `common_jpush_message` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `alert_msg` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '网值类型：\r\n网能：Activity\r\n网友：User\r\n网商：Product\r\n网信：Credit',
  `doc_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `push_params` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `from_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` tinyint(4) DEFAULT '0' COMMENT '发送状态：\r\n0：未发送\r\n1：已发送',
  `create_date` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_lucky_money
-- ----------------------------
DROP TABLE IF EXISTS `common_lucky_money`;
CREATE TABLE `common_lucky_money` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `send_time` time DEFAULT NULL COMMENT '发放时间-根据时间判断哪个是第一个',
  `examine_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核人用户id',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态 1.已审核 2.等待审核,3.明天生效',
  `send_people` decimal(10,2) DEFAULT '0.00' COMMENT '发放比例',
  `send_count` decimal(10,2) DEFAULT '0.00' COMMENT '发放人数',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_manage_arbitration
-- ----------------------------
DROP TABLE IF EXISTS `common_manage_arbitration`;
CREATE TABLE `common_manage_arbitration` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `from_nickname` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to_nickname` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `from_user_level` int(11) DEFAULT NULL,
  `to_user_level` int(11) DEFAULT NULL,
  `from_user_sex` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to_user_sex` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `from_user_age` int(11) DEFAULT NULL,
  `to_user_age` int(11) DEFAULT NULL,
  `status_code` int(11) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '投诉类型',
  `type_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '投诉事件ID',
  `theme` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `from_user_credit_value` int(11) DEFAULT NULL,
  `to_user_credit_value` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `reason` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `from_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `descriptions` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `from_src_url` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `appeal_src_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `appeal_date` datetime DEFAULT NULL,
  `result_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_other_set
-- ----------------------------
DROP TABLE IF EXISTS `common_other_set`;
CREATE TABLE `common_other_set` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `can_use` tinyint(4) DEFAULT '0' COMMENT '是否审核 状态 1.已审核 0.等待审核',
  `dispose_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核用户id',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `skill_limit_type` int(11) DEFAULT '0' COMMENT '需求活动技能限制类别，0人员名单，1限制条件',
  `skill_limit_point` int(11) DEFAULT '0' COMMENT '需求活动技能限制分数',
  `skill_limit_user_Ids` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '需求活动技能限制允许用户id,String，逗号分隔',
  `skill_limit_condition` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '需求活动技能限制条件,0,信用值不低与skillLimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔',
  `pic_limit_type` int(11) DEFAULT '0' COMMENT '图文音视限制类别，0人员名单，1限制条件',
  `pic_limit_point` int(11) DEFAULT '0' COMMENT '图文音视限制分数',
  `pic_limit_user_Ids` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图文音视限制允许用户id,String，逗号分隔',
  `pic_limit_condition` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图文音视限制条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔',
  `wish_limit_type` int(11) DEFAULT '0' COMMENT '心愿限制类别，0人员名单，1限制条件',
  `wish_limit_point` int(11) DEFAULT '0' COMMENT '心愿限制分数',
  `wish_limit_user_Ids` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '心愿限制允许用户id,String，逗号分隔',
  `wish_limit_condition` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '心愿限制条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,4:推荐者与发布者同等资格，String类型,用逗号分隔',
  `reg_merchant_limit_type` int(11) DEFAULT '0' COMMENT '注册商家限制类别，0人员名单，1限制条件',
  `reg_merchant_limit_point` int(11) DEFAULT '0' COMMENT '注册商家限制分数',
  `reg_merchant_limit_user_Ids` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '注册商家制允许用户id,String，逗号分隔',
  `reg_merchant_limit_condition` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '注册商家条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔',
  `credit_limit_type` int(11) DEFAULT '0' COMMENT '发行网币限制类别，0人员名单，1限制条件',
  `credit_limit_Point` int(11) DEFAULT '0' COMMENT '发行网币限制分数',
  `credit_limit_user_ids` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发行网币允许用户id,String，逗号分隔',
  `credit_limit_condition` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发行网币条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证4:上月业绩不低于currencylimitMoney,5:上月发行网币余额小于currencyLimitTotal,6,达到了上期网币发型时的业绩增长承诺,String类型,用逗号分隔',
  `credit_limit_moreThen` int(11) DEFAULT '0' COMMENT '发行网币上月业绩不低于',
  `credit_limit_balance` int(11) DEFAULT '0' COMMENT '发行网币余额小于',
  `share_limit_type` int(11) DEFAULT '0' COMMENT '允许接受礼物,邀请及被分享的限制类别，0人员名单，1限制条件',
  `share_limit_point` int(11) DEFAULT '0' COMMENT '允许接受礼物,邀请及被分享的限制分数',
  `share_limit_condition` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许接受礼物,邀请及被分享的限制条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔',
  `bak1` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用1',
  `bak2` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用2',
  `bak3` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用3',
  `bak4` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用4',
  `bak5` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_receivables_order
-- ----------------------------
DROP TABLE IF EXISTS `common_receivables_order`;
CREATE TABLE `common_receivables_order` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '付款方用户',
  `to_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款方用户',
  `credit_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '网币ID',
  `state` tinyint(4) DEFAULT '0' COMMENT '状态：0：未支付，1,：已支付',
  `amount` bigint(11) DEFAULT '0' COMMENT '金额(以分为单位)',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='网币收款订单表';

-- ----------------------------
-- Table structure for common_sensitive
-- ----------------------------
DROP TABLE IF EXISTS `common_sensitive`;
CREATE TABLE `common_sensitive` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `suggest_user_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '建议者名称',
  `suggest_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '建议者id',
  `count` int(11) DEFAULT NULL COMMENT '过滤次数',
  `del_reason` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_sensitive_suggest
-- ----------------------------
DROP TABLE IF EXISTS `common_sensitive_suggest`;
CREATE TABLE `common_sensitive_suggest` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `suggest_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `suggest_user_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `del_or_save` int(11) DEFAULT NULL COMMENT '建议删除还是新增，0删除，1新增',
  `value` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '词值，多个用，号隔开',
  `audit_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '审核人id，默认0，没人审核',
  `reason` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '建议理由',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_suggestion
-- ----------------------------
DROP TABLE IF EXISTS `common_suggestion`;
CREATE TABLE `common_suggestion` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `suggest_content` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nickname` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reply_content` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `replacer_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_tags
-- ----------------------------
DROP TABLE IF EXISTS `common_tags`;
CREATE TABLE `common_tags` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0:内置标签，1：技能标签，2：兴趣标签，3：商品可选规格',
  `type_cate` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '二级类别',
  `py` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拼音首字母',
  `cate_private` int(11) DEFAULT NULL COMMENT '0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '自定义才有创建人，0为系统标签',
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for common_wallet
-- ----------------------------
DROP TABLE IF EXISTS `common_wallet`;
CREATE TABLE `common_wallet` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户主键',
  `total_amount` bigint(11) DEFAULT '0' COMMENT '总金额(以分为单位)',
  `password` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '钱包密码',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `bak3` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bak4` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bak5` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `vsn` int(11) DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户钱包';

-- ----------------------------
-- Table structure for common_wallet_frozen
-- ----------------------------
DROP TABLE IF EXISTS `common_wallet_frozen`;
CREATE TABLE `common_wallet_frozen` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `frozen_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消费渠道活动,需求,心愿,技能,商品,网币,用类名表示',
  `amount` bigint(11) DEFAULT '0' COMMENT '冻结金额(以分为单位)',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `description` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `type_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '事件id,即参加活动，需求等条件的id',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否生效',
  `has_consume` int(11) DEFAULT '0' COMMENT '是否已抵扣,0未抵扣，1已抵扣',
  `bak1` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bak2` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bak3` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bak4` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bak5` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `vsn` int(11) DEFAULT '0' COMMENT '乐观锁',
  `to_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易对象id，即钱包id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for credit
-- ----------------------------
DROP TABLE IF EXISTS `credit`;
CREATE TABLE `credit` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `name` varchar(100) DEFAULT NULL COMMENT '网币名称',
  `tag_ids` varchar(300) DEFAULT NULL COMMENT '网币标签，多个用逗号隔开',
  `front_style` varchar(1000) DEFAULT NULL COMMENT '网币正面样式',
  `back_style` varchar(1000) DEFAULT NULL COMMENT '网币反面样式',
  `is_fit_scope_one` bit(1) DEFAULT b'0' COMMENT '是否适用范围1\r\n',
  `is_fit_scope_two` bit(1) DEFAULT b'0' COMMENT '是否适用范围2，选择商家\r\n',
  `seller_ids` varchar(2000) DEFAULT NULL COMMENT '选择范围商家，选择适用范围2的时候必填',
  `is_fit_scope_three` bit(1) DEFAULT b'0' COMMENT '是否适用范围3\r\n',
  `import_net_num` varchar(20) DEFAULT NULL COMMENT '结转用户网号',
  `import_name` varchar(20) DEFAULT NULL COMMENT '结转用户姓名',
  `import_phone` varchar(20) DEFAULT NULL COMMENT '结转用户手机号',
  `import_idcard` varchar(20) DEFAULT NULL COMMENT '结转用户身份证',
  `release_num` int(11) DEFAULT '0' COMMENT '发行数量',
  `release_time` datetime DEFAULT NULL COMMENT '发行时间',
  `success_time` datetime DEFAULT NULL COMMENT '发行成功时间',
  `face_value` int(11) DEFAULT NULL COMMENT '网币面值',
  `apply_price` bigint(11) DEFAULT NULL COMMENT '申购单价',
  `buy_factor` decimal(10,2) DEFAULT '0.00' COMMENT '回购系数',
  `growth_rate` int(11) DEFAULT '0' COMMENT '递增幅度',
  `royalty_ratio` int(11) DEFAULT '0' COMMENT '固定分红提成比例',
  `remark` varchar(2000) DEFAULT NULL COMMENT '网币说明',
  `buy_amount` bigint(11) DEFAULT '0' COMMENT '已申购金额',
  `pay_amount` bigint(11) DEFAULT '0' COMMENT '已兑付金额',
  `status` tinyint(4) DEFAULT '0' COMMENT '网币状态\r\n1：等待审核\r\n2：不予批准\r\n3：等待保荐\r\n4：不予保荐\r\n5：正在申购\r\n6：发行成功\r\n7：兑付完成8:正在兑付',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-网币表';

-- ----------------------------
-- Table structure for credit_answer_log
-- ----------------------------
DROP TABLE IF EXISTS `credit_answer_log`;
CREATE TABLE `credit_answer_log` (
  `id` varchar(32) NOT NULL COMMENT '标识id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `complete_id` varchar(32) DEFAULT NULL COMMENT '竞购id',
  `credit_id` varchar(32) DEFAULT NULL COMMENT '网币id',
  `question_id` varchar(32) DEFAULT NULL COMMENT '题库id',
  `answer_status` tinyint(4) DEFAULT NULL COMMENT '回答状态\r\n1：未回答\r\n2：已回答',
  `answer_result` tinyint(4) DEFAULT NULL COMMENT '回答结果\r\n            1：正确\r\n            2：错误',
  `answer_time` datetime DEFAULT NULL COMMENT '答题时间',
  `amount` bigint(10) DEFAULT '0' COMMENT '获得网币金额(以分为单位)=真实值x100',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-答题记录表';

-- ----------------------------
-- Table structure for credit_apply
-- ----------------------------
DROP TABLE IF EXISTS `credit_apply`;
CREATE TABLE `credit_apply` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `credit_id` varchar(32) DEFAULT NULL COMMENT '网币Id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '申购用户ID',
  `amount` bigint(11) DEFAULT '0' COMMENT '申购金额(以分为单位)',
  `way` int(11) DEFAULT '2' COMMENT '申购方式\r\n1：隐身申购\r\n2：立即申购',
  `apply_time` int(11) DEFAULT NULL COMMENT '申购时间',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-申购表';

-- ----------------------------
-- Table structure for credit_back_buy
-- ----------------------------
DROP TABLE IF EXISTS `credit_back_buy`;
CREATE TABLE `credit_back_buy` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `credit_id` varchar(32) DEFAULT NULL COMMENT '网币ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '回购用户ID',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `price` bigint(11) DEFAULT NULL COMMENT '回购网币金额(以分为单位)',
  `income` bigint(11) DEFAULT NULL COMMENT '回购收益(以分为单位)',
  `status` tinyint(4) DEFAULT '0' COMMENT '回购状态\r\n            1：回购申请\r\n            2：同意回购\r\n            3：拒绝回购\r\n            4：支付完成',
  `confirm_time` datetime DEFAULT NULL COMMENT '回购确认时间',
  `pay_money` bigint(11) DEFAULT '0' COMMENT '回购支付费用(以分为单位)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-回购表';

-- ----------------------------
-- Table structure for credit_bonus_send
-- ----------------------------
DROP TABLE IF EXISTS `credit_bonus_send`;
CREATE TABLE `credit_bonus_send` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '发放用户ID',
  `credit_id` varchar(32) DEFAULT NULL COMMENT '发放网币ID',
  `money` bigint(11) DEFAULT '0' COMMENT '分红金额(以分为单位)',
  `send_time` datetime DEFAULT NULL COMMENT '发放时间',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-分红发放记录表';

-- ----------------------------
-- Table structure for credit_cashier
-- ----------------------------
DROP TABLE IF EXISTS `credit_cashier`;
CREATE TABLE `credit_cashier` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '标识ID',
  `merchant_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '商家id',
  `merchant_user_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '商家注册者用户ID',
  `cashier_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '收银人员姓名',
  `cashier_phone` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '收银人手机号',
  `cashier_network_num` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '收银人网号',
  `is_current` int(2) NOT NULL DEFAULT '0' COMMENT '是否商家使用中：0：不是 1：是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for credit_category
-- ----------------------------
DROP TABLE IF EXISTS `credit_category`;
CREATE TABLE `credit_category` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '标识ID',
  `credit_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '商家id',
  `category_id` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '网信类目',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for credit_complete_buy
-- ----------------------------
DROP TABLE IF EXISTS `credit_complete_buy`;
CREATE TABLE `credit_complete_buy` (
  `id` varchar(32) NOT NULL COMMENT '标识id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `credit_id` varchar(32) DEFAULT NULL COMMENT '网币id',
  `chance_num` int(11) DEFAULT '0' COMMENT '购买答题机会数',
  `buy_time` datetime DEFAULT NULL COMMENT '购买时间',
  `currency_amount` bigint(11) DEFAULT '0' COMMENT '获得网币金额(以分为单位)',
  `status` tinyint(4) DEFAULT '0' COMMENT '竞购状态\r\n            1：进行中\r\n            2：已完成',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-答题竞购表';

-- ----------------------------
-- Table structure for credit_enroll_apply
-- ----------------------------
DROP TABLE IF EXISTS `credit_enroll_apply`;
CREATE TABLE `credit_enroll_apply` (
  `id` varchar(32) NOT NULL,
  `credit_id` varchar(32) DEFAULT NULL COMMENT '网币id',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 1.已抽奖 2.未抽奖',
  `relative_id` varchar(32) DEFAULT NULL COMMENT '关联优惠资格记录id,方便重新选人时确定报名申购列表',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='报名申购表';

-- ----------------------------
-- Table structure for credit_enroll_apply_record
-- ----------------------------
DROP TABLE IF EXISTS `credit_enroll_apply_record`;
CREATE TABLE `credit_enroll_apply_record` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `credit_id` varchar(32) DEFAULT NULL,
  `amount` bigint(11) DEFAULT NULL COMMENT '申购优惠金额(以分为单位)',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态:    1.等待申购      2.完成申购 3.放弃申购',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币报名申购资格优惠记录表';

-- ----------------------------
-- Table structure for credit_hold
-- ----------------------------
DROP TABLE IF EXISTS `credit_hold`;
CREATE TABLE `credit_hold` (
  `id` varchar(32) NOT NULL COMMENT '标识id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `credit_id` varchar(32) DEFAULT NULL COMMENT '网币id',
  `amount` bigint(11) DEFAULT '0' COMMENT '网币金额(以分为单位)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-持有表';

-- ----------------------------
-- Table structure for credit_question
-- ----------------------------
DROP TABLE IF EXISTS `credit_question`;
CREATE TABLE `credit_question` (
  `id` varchar(32) NOT NULL COMMENT '标识id',
  `type` int(11) DEFAULT '0' COMMENT '题目类型\r\n            1：数学运算题\r\n            2：进制转换题\r\n            3：单位转换题',
  `content` varchar(300) DEFAULT NULL COMMENT '题目内容',
  `itemA` varchar(50) DEFAULT NULL COMMENT '选项A',
  `itemB` varchar(50) DEFAULT NULL COMMENT '选项B',
  `itemC` varchar(50) DEFAULT NULL COMMENT '选项C',
  `itemD` varchar(50) DEFAULT NULL COMMENT '选项D',
  `correct_answer` varchar(50) DEFAULT NULL COMMENT '正确答案',
  `limit_time` int(11) DEFAULT NULL COMMENT '作答限制时长（单位为秒）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-题库表';

-- ----------------------------
-- Table structure for credit_recommend
-- ----------------------------
DROP TABLE IF EXISTS `credit_recommend`;
CREATE TABLE `credit_recommend` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `credit_id` varchar(32) DEFAULT NULL COMMENT '网币ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '保荐人用户ID',
  `status` tinyint(4) DEFAULT '0' COMMENT '保荐状态\r\n            1：待保荐\r\n            2：已保荐',
  `result` tinyint(4) DEFAULT '0' COMMENT '保荐结果\r\n            1：同意\r\n            2：拒绝',
  `recommend_time` datetime DEFAULT NULL COMMENT '保荐时间',
  `advice` varchar(100) DEFAULT NULL COMMENT '保荐意见',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-保荐人表';

-- ----------------------------
-- Table structure for credit_review
-- ----------------------------
DROP TABLE IF EXISTS `credit_review`;
CREATE TABLE `credit_review` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `credit_id` varchar(32) DEFAULT NULL COMMENT '网币ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '审核用户ID',
  `status` tinyint(4) DEFAULT '0' COMMENT '审核状态\r\n            1：待审核\r\n            2：已审核',
  `result` tinyint(4) DEFAULT '0' COMMENT '审核结果\r\n            1、审核通过\r\n            2、审核不通过',
  `audio_time` datetime DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-审核表';

-- ----------------------------
-- Table structure for credit_stage
-- ----------------------------
DROP TABLE IF EXISTS `credit_stage`;
CREATE TABLE `credit_stage` (
  `id` varchar(512) NOT NULL,
  `credit_id` varchar(512) DEFAULT NULL,
  `stage_name` varchar(255) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `subscription_ratio` double DEFAULT NULL COMMENT '认购比率',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for credit_subscription
-- ----------------------------
DROP TABLE IF EXISTS `credit_subscription`;
CREATE TABLE `credit_subscription` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '标识id',
  `credit_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '网信id',
  `merchant_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '商家id',
  `user_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '认购者id',
  `credit_stage_id` varchar(32) NOT NULL COMMENT '折扣等级',
  `type` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户身份',
  `subscription_number` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '认购金额(单位：分)',
  `status` tinyint(1) DEFAULT '3' COMMENT '认购状态(0.未响应, 1.认购成功, 2.拒绝内购, 3.非内购人员)',
  `message_pyload` varchar(1000) DEFAULT NULL COMMENT '邀请内购信息',
  `send_time` datetime DEFAULT NULL COMMENT '发起邀请时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `amount` int(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for credit_trade_record
-- ----------------------------
DROP TABLE IF EXISTS `credit_trade_record`;
CREATE TABLE `credit_trade_record` (
  `id` varchar(32) NOT NULL COMMENT '标识id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `credit_id` varchar(32) DEFAULT NULL COMMENT '交易的网币id',
  `amount` bigint(11) DEFAULT '0' COMMENT '交易金额( 以分为单位)',
  `trade_type` int(11) DEFAULT NULL COMMENT '交易类型\r\n1：支出\r\n2：收入',
  `trade_time` datetime DEFAULT NULL COMMENT '交易时间',
  `description` varchar(2000) DEFAULT NULL COMMENT '交易描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-交易记录表';

-- ----------------------------
-- Table structure for credit_transfer
-- ----------------------------
DROP TABLE IF EXISTS `credit_transfer`;
CREATE TABLE `credit_transfer` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `credit_id` varchar(32) DEFAULT NULL COMMENT '网币ID',
  `launch_user_id` varchar(32) DEFAULT NULL COMMENT '发起转让用户ID',
  `price` bigint(11) DEFAULT NULL COMMENT '转让网币金额(以分为单位)',
  `set_price` bigint(11) DEFAULT NULL COMMENT '设定转让价格(以分为单位)',
  `launch_time` datetime DEFAULT NULL COMMENT '发起转让时间',
  `be_launch_user_id` varchar(32) DEFAULT NULL COMMENT '受让者用户ID',
  `status` tinyint(4) DEFAULT '0' COMMENT '转让状态\r\n            1：转让中\r\n            2：接收转让\r\n            3：已过期',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='网币-转让表';

-- ----------------------------
-- Table structure for credits
-- ----------------------------
DROP TABLE IF EXISTS `credits`;
CREATE TABLE `credits` (
  `id` varchar(255) NOT NULL,
  `credit_number` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `applicant_user_id` varchar(255) DEFAULT NULL,
  `applicant_merchant_id` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `issue_number` bigint(20) DEFAULT NULL,
  `ensure_deal` bigint(20) DEFAULT NULL,
  `credit_cashier_id` varchar(255) DEFAULT NULL,
  `inner_purchase_date` datetime DEFAULT NULL,
  `scope_merchant_ids` varchar(255) DEFAULT NULL,
  `is_merchant` tinyint(4) DEFAULT NULL,
  `is_scope` tinyint(4) DEFAULT NULL,
  `credit_status` tinyint(4) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for credits_cashier
-- ----------------------------
DROP TABLE IF EXISTS `credits_cashier`;
CREATE TABLE `credits_cashier` (
  `id` varchar(255) DEFAULT NULL,
  `cashier_user_number` varchar(255) DEFAULT NULL,
  `cashier_real_name` varchar(255) DEFAULT NULL,
  `cashier_mobile` varchar(255) DEFAULT NULL,
  `cashier_id_number` varchar(255) DEFAULT NULL,
  `is_current` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  `credit_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for demand
-- ----------------------------
DROP TABLE IF EXISTS `demand`;
CREATE TABLE `demand` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `title` varchar(300) DEFAULT NULL COMMENT '主题',
  `demand_type` tinyint(4) DEFAULT '0' COMMENT '需求分类：\r\n            1：技能\r\n            2：才艺\r\n            3：知识\r\n            4：资源',
  `demand_label` varchar(2000) DEFAULT NULL COMMENT '标签，逗号分隔',
  `is_open_ended` bit(1) DEFAULT b'0' COMMENT '是否长期有效',
  `start_at` datetime DEFAULT NULL COMMENT '开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '结束时间',
  `about` varchar(300) DEFAULT NULL COMMENT '时间要求：只有大概的要求，如：50天内、仅限周末等',
  `amount` int(11) DEFAULT '0' COMMENT '需求人数',
  `unit` varchar(50) DEFAULT NULL COMMENT '单位',
  `obj` tinyint(4) DEFAULT '0' COMMENT '报名对象：\r\n            1：不限制。\r\n            2：仅限女性报名\r\n            3：仅限男性报名\r\n            4：仅允许我的好友报名\r\n            5：仅限指定人员报名',
  `obj_list` varchar(300) DEFAULT NULL COMMENT '指定报名对象列表，逗号分隔',
  `address` varchar(2000) DEFAULT NULL COMMENT '地址：\r\n            这里商家地址，订单之类的都是发布需求时的，在生成订单时还会生成实际的。',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `images_url` varchar(2000) DEFAULT NULL COMMENT '活动图片',
  `details_images_url` varchar(2000) DEFAULT NULL COMMENT '详情图片',
  `order_ids` varchar(2000) DEFAULT NULL COMMENT '订单列表',
  `order_price` bigint(20) DEFAULT '0' COMMENT '订单消费',
  `is_pick_up` bit(1) DEFAULT b'0' COMMENT '如发生消费，是否由我全额承担',
  `wage` bigint(20) DEFAULT NULL COMMENT '报酬，根据isEachWage判断是总报酬还是单位报酬',
  `is_each_wage` bit(1) DEFAULT NULL COMMENT '是否为单位报酬，即：以上报酬是否为每个入选者的单位报酬',
  `bail` bigint(20) DEFAULT '0' COMMENT '已经托管的保证金',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            1：已发布\r\n            2：已取消\r\n            3：已关闭\r\n            4：已结束报名\r\n            5：已结束细节',
  `is_pay` bit(1) DEFAULT b'0' COMMENT '是否支付（托管）',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_lon_lat` (`lon`,`lat`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='需求表';

-- ----------------------------
-- Table structure for demand_order
-- ----------------------------
DROP TABLE IF EXISTS `demand_order`;
CREATE TABLE `demand_order` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `demand_id` varchar(32) DEFAULT NULL,
  `start_at` datetime DEFAULT NULL COMMENT '开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '结束时间',
  `unit` varchar(50) DEFAULT NULL,
  `address` varchar(2000) DEFAULT NULL COMMENT '地址：\r\n            这里商家地址，订单之类的都是发布需求时的，在生成订单时还会生成实际的。',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `order_ids` varchar(2000) DEFAULT NULL COMMENT '订单列表',
  `order_price` bigint(20) DEFAULT '0' COMMENT '订单消费',
  `wage` bigint(20) DEFAULT '0' COMMENT '报酬，根据isEachWage判断是总报酬还是单位报酬',
  `is_each_wage` bit(1) DEFAULT b'0' COMMENT '是否为单位报酬，即：以上报酬是否为每个入选者的单位报酬',
  `bail` bigint(20) DEFAULT '0' COMMENT '已经托管的保证金',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            1：已接受，即已确定入选人\r\n            2：已确定细节\r\n            3：需求启动，只要入选者有人启动成功，就设置为该值\r\n            4：超时未确认细节\r\n            5,：需求成功，即：距离、验证码都通过\r\n            6：退款状态\r\n            7：超时未启动需求\r\n            8: 仲裁中\r\n            9：支付完成，结束',
  `validation_status` bit(1) DEFAULT b'0' COMMENT '验证状态\r\n            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：\r\n            0：未验证\r\n            1：已验证\r\n            2：验证失败\r\n            ',
  `is_pay` bit(1) DEFAULT b'0' COMMENT '是否支付（托管）',
  `is_confirm_pay` bit(1) DEFAULT b'0' COMMENT '入选人是否确认付款',
  `confirm_pay_user_id` varchar(32) DEFAULT NULL COMMENT '确认付款人ID，任意一个入选人确认即可',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`),
  KEY `idx_lon_lat` (`lon`,`lat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='需求单表，每次入选就生成一张，以应对长期的需求，长期需求只能有1人入选。';

-- ----------------------------
-- Table structure for demand_register
-- ----------------------------
DROP TABLE IF EXISTS `demand_register`;
CREATE TABLE `demand_register` (
  `id` varchar(32) NOT NULL,
  `demand_id` varchar(32) DEFAULT NULL COMMENT '主表ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '报名人',
  `start_at` datetime DEFAULT NULL COMMENT '建议开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '建议结束时间',
  `about` varchar(300) DEFAULT NULL COMMENT '建议时间要求：只有大概范围，如：50天内、仅限周末等，具体的时间待申请成功后再与发布者协商确定',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `unit` varchar(50) DEFAULT NULL,
  `wage` bigint(20) DEFAULT '0' COMMENT '希望的报酬',
  `address` varchar(2000) DEFAULT NULL COMMENT '地址',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `is_pay_wage` bit(1) DEFAULT b'0' COMMENT '是否已经支付报酬',
  `status` tinyint(4) DEFAULT '0' COMMENT '报名状态：\r\n            0：已报名，未入选\r\n            1：已入选\r\n            2：已取消\r\n            3：未入选\r\n            4：已启动需求\r\n            5：放弃参与，即已入选放弃\r\n            6：超时未启动需求\r\n            7：发布者取消入选者的需求\r\n           8： 已结束',
  `is_anonymity` bit(1) DEFAULT b'0' COMMENT '是否匿名',
  `validation_status` bit(1) DEFAULT b'0' COMMENT '验证状态\r\n            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：\r\n            0：未验证\r\n            1：已验证\r\n            2：验证失败\r\n            ',
  `code` int(11) DEFAULT '0' COMMENT '邀请码',
  `times` tinyint(4) DEFAULT '0' COMMENT '邀请码重试次数',
  `is_validation` bit(1) DEFAULT b'0' COMMENT '验证码是否通过',
  `demand_order_id` varchar(32) DEFAULT NULL COMMENT '需求订单的ID，入选后，此ID便有值了。',
  `paied_fee` bigint(20) DEFAULT '0' COMMENT '已结算的费用，即在申请退款时，如果提前支付了一部分钱，那么就提现在这里',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`),
  KEY `idx_lon_lat` (`lon`,`lat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='需求报名表';

-- ----------------------------
-- Table structure for gift
-- ----------------------------
DROP TABLE IF EXISTS `gift`;
CREATE TABLE `gift` (
  `id` varchar(32) NOT NULL,
  `title` varchar(300) DEFAULT NULL COMMENT '礼物标题',
  `from_user_id` varchar(32) DEFAULT NULL COMMENT '赠送人',
  `to_user_id` varchar(32) DEFAULT NULL COMMENT '接受人',
  `send_at` datetime DEFAULT NULL COMMENT '赠送时间',
  `gift_type` tinyint(4) DEFAULT '0' COMMENT '礼物类型：\r\n            1：红包\r\n            2：网币\r\n            3：商品',
  `relatable_id` varchar(32) DEFAULT NULL COMMENT '关联主键，没有就是0',
  `amount` bigint(20) DEFAULT '0' COMMENT '金额',
  `description` varchar(2000) DEFAULT NULL COMMENT '留言',
  `is_anonymity` bit(1) DEFAULT b'0' COMMENT '是否匿名',
  `is_set_logistics` bit(1) DEFAULT b'0' COMMENT '是否填写物流',
  `status` tinyint(4) DEFAULT '0' COMMENT '礼物状态：\r\n             1：已送出\r\n             2：已接受\r\n            3：已拒绝',
  `address` varchar(300) DEFAULT NULL COMMENT '物流信息',
  `message` varchar(2000) DEFAULT NULL COMMENT '其它要求',
  `delivery_at` datetime DEFAULT NULL COMMENT '要求送达时间',
  `article_id` varchar(32) DEFAULT NULL COMMENT '来源资讯id，即：从哪里点过来的',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_from` (`from_user_id`),
  KEY `idx_to` (`to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='礼物表';

-- ----------------------------
-- Table structure for invitation
-- ----------------------------
DROP TABLE IF EXISTS `invitation`;
CREATE TABLE `invitation` (
  `id` varchar(32) NOT NULL,
  `from_user_id` varchar(32) DEFAULT NULL COMMENT '邀请人',
  `to_user_id` varchar(32) DEFAULT NULL COMMENT '对象',
  `title` varchar(300) DEFAULT NULL COMMENT '邀请标题（主题）',
  `address` varchar(2000) DEFAULT NULL COMMENT '地址',
  `start_at` datetime DEFAULT NULL COMMENT '开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '结束时间',
  `amout` bigint(20) DEFAULT '0' COMMENT '报酬',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `is_anonymity` bit(1) DEFAULT b'0' COMMENT '是否匿名',
  `is_confirm` bit(1) DEFAULT b'0' COMMENT '是否确认（时间、地址）',
  `code` int(11) DEFAULT '0' COMMENT '邀请码',
  `suggestion` varchar(2000) DEFAULT NULL COMMENT '建议描述',
  `times` tinyint(4) DEFAULT '0' COMMENT '邀请码重试次数',
  `is_validation` bit(1) DEFAULT b'0' COMMENT '验证码是否通过',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            1：发出邀请\r\n            2：接受邀请\r\n            3：拒绝邀请\r\n            4：超时取消，目前由用户自己拒绝\r\n            ',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `from_validation_status` bit(1) DEFAULT b'0' COMMENT '邀请人验证状态\r\n            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：\r\n            0：未验证\r\n            1：已验证\r\n            2：验证失败\r\n            ',
  `to_validation_status` bit(1) DEFAULT b'0' COMMENT '被邀请人验证状态\r\n            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：\r\n            0：未验证\r\n            1：已验证\r\n            2：验证失败\r\n            ',
  `is_online` bit(1) DEFAULT b'0' COMMENT '是否纯线上活动（无需地址）',
  `article_id` varchar(32) DEFAULT NULL COMMENT '来源资讯id，即：从哪里点过来的',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_from` (`from_user_id`),
  KEY `idx_to` (`to_user_id`),
  KEY `idx_lon_lat` (`lon`,`lat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邀请表';

-- ----------------------------
-- Table structure for job_history
-- ----------------------------
DROP TABLE IF EXISTS `job_history`;
CREATE TABLE `job_history` (
  `id` varchar(32) NOT NULL,
  `job_id` int(11) NOT NULL,
  `handler` varchar(100) DEFAULT NULL,
  `type_id` varchar(32) DEFAULT NULL COMMENT '事件id',
  `type_name` varchar(100) DEFAULT NULL COMMENT '事件名',
  `param` varchar(200) DEFAULT NULL COMMENT '参数',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for match_appearance
-- ----------------------------
DROP TABLE IF EXISTS `match_appearance`;
CREATE TABLE `match_appearance` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛ID',
  `progress_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛进程',
  `zone_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛区ID',
  `group_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛组ID',
  `venue_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分赛场ID',
  `performer_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表演者ID',
  `performer_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '演出者名字',
  `project_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '项目名称',
  `head_image_url` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像信息',
  `project_introduct` text COLLATE utf8mb4_unicode_ci COMMENT '项目介绍',
  `appearance_images_url` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '团队图片之类的',
  `appearance_order` int(11) DEFAULT NULL COMMENT '演出次序',
  `performance_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '演出时间',
  `appearance_status` int(11) DEFAULT NULL COMMENT '演出状态',
  PRIMARY KEY (`id`),
  KEY `match_id` (`match_id`),
  KEY `venue_id` (`venue_id`),
  KEY `performer_id` (`performer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='比赛演出次序表';

-- ----------------------------
-- Table structure for match_apply_default_time
-- ----------------------------
DROP TABLE IF EXISTS `match_apply_default_time`;
CREATE TABLE `match_apply_default_time` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛id',
  `type` tinyint(4) DEFAULT '1' COMMENT '类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='默认报名时间表';

-- ----------------------------
-- Table structure for match_audience
-- ----------------------------
DROP TABLE IF EXISTS `match_audience`;
CREATE TABLE `match_audience` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛ID',
  `match_ticket_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '门票的ID',
  `venue_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '场次ID',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `is_pay` bit(1) DEFAULT NULL COMMENT '是否支付',
  `is_attend` bit(1) DEFAULT NULL,
  `is_quit` bit(1) DEFAULT b'0',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ticket_id` (`match_ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='门票支付表';

-- ----------------------------
-- Table structure for match_award
-- ----------------------------
DROP TABLE IF EXISTS `match_award`;
CREATE TABLE `match_award` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `award_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '奖项名字',
  `award_number` int(11) DEFAULT NULL COMMENT '奖项名额',
  `bonus` decimal(16,2) DEFAULT NULL COMMENT '奖金',
  `prize` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '奖品',
  `certificate` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '证书',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `match_id` (`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='奖项表';

-- ----------------------------
-- Table structure for match_child_info
-- ----------------------------
DROP TABLE IF EXISTS `match_child_info`;
CREATE TABLE `match_child_info` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `participant_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '监护人ID',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `head_image` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `sex` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `introduction` text COLLATE utf8mb4_unicode_ci COMMENT '孩子介绍',
  `images_url` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '照片',
  `other_requirement` text COLLATE utf8mb4_unicode_ci COMMENT '其他要求',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '团队好友userId',
  `mobile` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`),
  KEY `participant_id` (`participant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='未成年人表';

-- ----------------------------
-- Table structure for match_event
-- ----------------------------
DROP TABLE IF EXISTS `match_event`;
CREATE TABLE `match_event` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'id',
  `initiator_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发起者ID',
  `title` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `sub_title` varchar(127) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '副标题',
  `match_kind` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '比赛形式_不限/个人/团队/',
  `match_rule` text CHARACTER SET utf8mb4 COMMENT '比赛规则',
  `grading` text CHARACTER SET utf8mb4 COMMENT '评分标准',
  `match_introduction` text CHARACTER SET utf8mb4 COMMENT '比赛介绍_图片',
  `match_image_url` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛事介绍_比赛图文详情',
  `match_status` tinyint(4) DEFAULT '0' COMMENT '比赛状态',
  `is_approved` bit(1) DEFAULT NULL COMMENT '是否通过审核',
  `pass_time` datetime DEFAULT NULL COMMENT '通过时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新的时间',
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拒絕理由',
  `lat` decimal(10,6) DEFAULT NULL,
  `lon` decimal(10,6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='比赛';

-- ----------------------------
-- Table structure for match_group
-- ----------------------------
DROP TABLE IF EXISTS `match_group`;
CREATE TABLE `match_group` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛ID',
  `match_group_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛组名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `quota` int(11) DEFAULT NULL,
  `free` float(4,0) DEFAULT NULL,
  `is_auto_select` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='赛组表';

-- ----------------------------
-- Table structure for match_group_and_zone
-- ----------------------------
DROP TABLE IF EXISTS `match_group_and_zone`;
CREATE TABLE `match_group_and_zone` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `match_zone_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '赛区id',
  `match_group_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '赛组id',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `is_default` bit(1) DEFAULT b'1' COMMENT '0表示是默认时间 1表示不是默认时间',
  `is_zone_time` bit(1) DEFAULT b'0' COMMENT '是否为赛区时间',
  PRIMARY KEY (`id`),
  KEY `GZ_MATCH_ZONE_ID_INDEX` (`match_zone_id`),
  KEY `GZ_MATCH_GROUP_ID_INDEX` (`match_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='赛组和赛区表';

-- ----------------------------
-- Table structure for match_member
-- ----------------------------
DROP TABLE IF EXISTS `match_member`;
CREATE TABLE `match_member` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛id',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '用户编号',
  `user_call` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户称呼',
  `kind` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工作人员类型：领导、嘉宾、会场工作人员',
  `is_accept` bit(1) DEFAULT b'0' COMMENT '是否接受邀请',
  `is_spot` bit(1) DEFAULT b'0' COMMENT '是否在场',
  `is_net_user` bit(1) DEFAULT b'1' COMMENT '是否为网值用户',
  `active_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '邀请码',
  PRIMARY KEY (`id`),
  KEY `match_id` (`match_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='比赛工作人员';

-- ----------------------------
-- Table structure for match_notice
-- ----------------------------
DROP TABLE IF EXISTS `match_notice`;
CREATE TABLE `match_notice` (
  `id` char(32) CHARACTER SET utf8mb4 NOT NULL,
  `title` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `affiche_content` text COLLATE utf8mb4_unicode_ci COMMENT '公共内容',
  `match_id` char(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '賽事id',
  `user_id` char(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '發佈公告者id',
  `user_type` char(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `merchant_id` char(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '發佈公告的商家id',
  `merchant_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商家名称',
  `merchant_type` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商家类型',
  `message_type` tinyint(4) DEFAULT NULL COMMENT '消息类型/0/1/2/3/4越小优先级越高',
  PRIMARY KEY (`id`),
  KEY `MATCH_NOTICE_MATCH_ID` (`match_id`),
  KEY `MATCH_NOTICE_USER` (`user_id`),
  KEY `MATCH_NOTICE_MERCHANT_ID` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='賽事公告表';

-- ----------------------------
-- Table structure for match_participant
-- ----------------------------
DROP TABLE IF EXISTS `match_participant`;
CREATE TABLE `match_participant` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '比赛ID',
  `zone_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `project_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '项目名称',
  `user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `sex` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `head_images_url` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `requirement_text` text COLLATE utf8mb4_unicode_ci COMMENT '提交的要求，JSON格式提交',
  `is_team` bit(1) DEFAULT b'0' COMMENT '是否个人参与',
  `team_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '团队ID',
  `is_pay` bit(1) DEFAULT NULL COMMENT '是否支付报名费',
  `is_pass` bit(1) DEFAULT NULL COMMENT '是否通过比赛审核',
  `is_guardian` bit(1) DEFAULT NULL COMMENT '是否监护人',
  `is_spot` bit(1) DEFAULT b'0' COMMENT '是否在场',
  `status` tinyint(4) unsigned zerofill DEFAULT '0001' COMMENT '报名状态（1表示进行中，0表示失败，2表示已经成功）',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `match_id` (`match_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='参赛报名';

-- ----------------------------
-- Table structure for match_progress
-- ----------------------------
DROP TABLE IF EXISTS `match_progress`;
CREATE TABLE `match_progress` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛ID',
  `match_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛制名称',
  `begin_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
  `sort` int(11) DEFAULT NULL COMMENT '用来排序的',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='赛制表';

-- ----------------------------
-- Table structure for match_progress_participant
-- ----------------------------
DROP TABLE IF EXISTS `match_progress_participant`;
CREATE TABLE `match_progress_participant` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '比赛ID',
  `zone_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '赛区ID',
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '赛组ID',
  `participant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参赛者ID',
  `progress_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛制ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for match_raters
-- ----------------------------
DROP TABLE IF EXISTS `match_raters`;
CREATE TABLE `match_raters` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `raters_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评分者ID',
  `raters_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评分者称呼',
  `progress_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛程',
  `participant_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参赛者ID',
  `score` decimal(10,2) DEFAULT NULL COMMENT '分数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='比赛评分';

-- ----------------------------
-- Table structure for match_requirement
-- ----------------------------
DROP TABLE IF EXISTS `match_requirement`;
CREATE TABLE `match_requirement` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛组ID',
  `requirement_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '要求名称',
  `is_requirement_data` bit(1) DEFAULT NULL COMMENT '是否需要上传资料',
  `requirement_designation` text COLLATE utf8mb4_unicode_ci COMMENT '指定特征',
  `requirement_upper_limit` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '要求上限',
  `requirement_lower_limit` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '要求下限',
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参赛要求';

-- ----------------------------
-- Table structure for match_requirement_data
-- ----------------------------
DROP TABLE IF EXISTS `match_requirement_data`;
CREATE TABLE `match_requirement_data` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `zone_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛区ID',
  `group_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛id',
  `requirement_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '要求ID',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `requirement_title` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '要求名称',
  `introduction` text COLLATE utf8mb4_unicode_ci COMMENT '资料介绍',
  `images_url` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '资料图片',
  `is_child_or_team` bit(1) DEFAULT b'0' COMMENT '是否为团队或者还是的信息',
  PRIMARY KEY (`id`),
  KEY `match_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='需要上传的资料';

-- ----------------------------
-- Table structure for match_review
-- ----------------------------
DROP TABLE IF EXISTS `match_review`;
CREATE TABLE `match_review` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '比赛id',
  `merchant_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '审核者id',
  `organizer_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `organizer_kind` tinyint(4) DEFAULT NULL,
  `is_accept` bit(1) DEFAULT b'1',
  `is_approve` bit(1) DEFAULT b'0',
  `user_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='比赛审核人';

-- ----------------------------
-- Table structure for match_sign_up_requirement
-- ----------------------------
DROP TABLE IF EXISTS `match_sign_up_requirement`;
CREATE TABLE `match_sign_up_requirement` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组别名称',
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `venue_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '会场ID',
  `begin_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '报名开始时间',
  `end_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
  `decription` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `quota` int(11) DEFAULT NULL COMMENT '报名名额',
  `free` float(2,0) DEFAULT NULL COMMENT '报名费用',
  `is_auto_select` bit(1) DEFAULT NULL COMMENT '是否系统筛选',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for match_team
-- ----------------------------
DROP TABLE IF EXISTS `match_team`;
CREATE TABLE `match_team` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `team_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '团队',
  `team_slogan` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '团队口号',
  `team_introduction` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '团队简介',
  `team_image_url` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '团队图标',
  `team_leader` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否通过比赛审核',
  `is_pass` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参赛团队';

-- ----------------------------
-- Table structure for match_ticket
-- ----------------------------
DROP TABLE IF EXISTS `match_ticket`;
CREATE TABLE `match_ticket` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'id',
  `zone_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛ID',
  `ticket_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `venue_ids` varchar(329) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '门票场次',
  `free` decimal(10,2) DEFAULT NULL COMMENT '门票价钱',
  `number` int(11) DEFAULT NULL COMMENT '名额',
  `description` text CHARACTER SET utf8mb4 COMMENT '门票描述',
  `begin_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始发售时间',
  `end_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '结束发售时间',
  `optimistic_locking` int(11) DEFAULT '0' COMMENT '乐观锁',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `is_defalut` bit(1) DEFAULT b'0' COMMENT '是否默认门票档次',
  `use_defalut` bit(1) DEFAULT b'1' COMMENT '使用默认的',
  PRIMARY KEY (`id`),
  KEY `T_MATCH_ZONE_ID_INDEX` (`zone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参赛门票详情';

-- ----------------------------
-- Table structure for match_venue
-- ----------------------------
DROP TABLE IF EXISTS `match_venue`;
CREATE TABLE `match_venue` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `zone_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛id',
  `title` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '场次名称',
  `kind` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛制类型，填赛制ID',
  `group_ids` varchar(329) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛组的ID，多个逗号隔开',
  `begin_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛地址',
  `site` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛场地',
  `site_image_url` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛场地图片',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `flow_path` int(11) DEFAULT NULL COMMENT '存流程的顺序，比如流程1的就扔个1过来',
  `flow_path_sort` int(11) DEFAULT NULL COMMENT '流程顺序中的排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='比赛场次';

-- ----------------------------
-- Table structure for match_venue_and_zone
-- ----------------------------
DROP TABLE IF EXISTS `match_venue_and_zone`;
CREATE TABLE `match_venue_and_zone` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_venue_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `match_progress_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛制id',
  `match_zone_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort` int(11) DEFAULT NULL COMMENT '用于指定添加的顺序',
  PRIMARY KEY (`id`),
  KEY `VZ_MATCH_VENUE_ID_INDEX` (`match_venue_id`),
  KEY `VZ_MATCH_ZONE_ID_INDEX` (`match_zone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='场次和赛区的关系表';

-- ----------------------------
-- Table structure for match_venue_group
-- ----------------------------
DROP TABLE IF EXISTS `match_venue_group`;
CREATE TABLE `match_venue_group` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `venue_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '场次ID',
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '赛组ID',
  `progress_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '赛程ID',
  PRIMARY KEY (`id`),
  KEY `G_ID` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for match_vote
-- ----------------------------
DROP TABLE IF EXISTS `match_vote`;
CREATE TABLE `match_vote` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '比赛ID',
  `project_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '项目ID',
  `project_introduct` text COLLATE utf8mb4_unicode_ci COMMENT '项目介绍',
  `project_images_url` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '项目照片1',
  `project_vote` bigint(20) DEFAULT NULL COMMENT '项目得分',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投票';

-- ----------------------------
-- Table structure for match_zone
-- ----------------------------
DROP TABLE IF EXISTS `match_zone`;
CREATE TABLE `match_zone` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `zone_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分区名称',
  `zone_adress` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分区地址',
  `zone_site` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分区地区',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `match_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='赛区表';

-- ----------------------------
-- Table structure for meeting
-- ----------------------------
DROP TABLE IF EXISTS `meeting`;
CREATE TABLE `meeting` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '主发起人ID',
  `title` varchar(300) DEFAULT NULL,
  `meeting_type` tinyint(4) DEFAULT '0' COMMENT '活动形式：\r\n            1：活动，即1对1\r\n            2：聚合，即多对多\r\n            3：纯线上活动\r\n            4：不发生消费的线下活动',
  `meeting_label` varchar(2000) CHARACTER SET utf8 DEFAULT NULL COMMENT '活动标签，逗号分隔',
  `started_at` datetime DEFAULT NULL COMMENT '活动开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '活动结束时间',
  `reg_stop_at` datetime DEFAULT NULL COMMENT '活动截至报名时间',
  `obj` tinyint(4) DEFAULT '0' COMMENT '报名对象：\r\n            1：不限制。\r\n            2：仅限女性报名\r\n            3：仅限男性报名\r\n            4：仅允许我的好友报名\r\n            5：仅限指定人员报名',
  `obj_list` varchar(300) CHARACTER SET utf8 DEFAULT NULL COMMENT '指定报名对象列表，逗号分隔',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '报名费：0为免费',
  `address` varchar(2000) CHARACTER SET utf8 DEFAULT NULL COMMENT '活动地址',
  `order_ids` varchar(300) CHARACTER SET utf8 DEFAULT NULL COMMENT '订单列表',
  `order_price` bigint(20) DEFAULT '0' COMMENT '活动消费',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `description` varchar(5000) CHARACTER SET utf8 DEFAULT NULL COMMENT '描述',
  `poster_images_url` varchar(2000) CHARACTER SET utf8 DEFAULT NULL COMMENT '海报图片',
  `meeting_images_url` varchar(2000) CHARACTER SET utf8 DEFAULT NULL COMMENT '活动图片',
  `ceil` int(11) DEFAULT '0' COMMENT '入选人数上限',
  `floor` int(11) DEFAULT '0' COMMENT '入选人数下限',
  `reg_count` int(11) DEFAULT '0' COMMENT '已报名人数',
  `reg_success_count` int(11) DEFAULT '0' COMMENT '已入选人数',
  `status` tinyint(4) DEFAULT '0' COMMENT '活动状态：\r\n            0：已发起，报名中\r\n            1：报名截止，已确定入选人\r\n            2：活动取消\r\n            3：活动失败\r\n            4：活动成功\r\n            5：同意开始，分发验证码\r\n            6：无人验证通过，活动失败',
  `fee_not_enough` tinyint(4) DEFAULT '0' COMMENT '费用不足时：\r\n            1：由我补足差额，活动正常进行\r\n            2：活动自动取消，报名费用全部返回',
  `is_confirm` bit(1) DEFAULT b'0' COMMENT '是否确认细节（地址、消费）',
  `lock_version` int(11) DEFAULT '0' COMMENT '版本',
  `all_register_amount` bigint(20) DEFAULT '0' COMMENT '总报名费',
  `balance` bigint(20) DEFAULT '0' COMMENT '消费差额，需补足的部分：总订单金额-总报名费\r\n            ',
  `is_balance_pay` bit(1) DEFAULT b'0' COMMENT '是否补足',
  `pay_from` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '补足人',
  `meeting_fee_pay_amount` decimal(10,2) DEFAULT '0.00' COMMENT '活动费用',
  `meeting_fee_Pay_from` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '活动费用支付人',
  `meeting_fee_pay_type` char(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '活动费用支付方式',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `group_id` bigint(20) DEFAULT NULL COMMENT '群聊组Id',
  `status_description` varchar(1000) CHARACTER SET utf8 DEFAULT '' COMMENT '活动实时状态描述',
  PRIMARY KEY (`id`),
  KEY `idx_from` (`user_id`) USING BTREE,
  KEY `idx_lon_lat` (`lon`,`lat`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动聚会表';

-- ----------------------------
-- Table structure for meeting_register
-- ----------------------------
DROP TABLE IF EXISTS `meeting_register`;
CREATE TABLE `meeting_register` (
  `id` varchar(32) NOT NULL,
  `meeting_id` varchar(32) DEFAULT NULL COMMENT '主表ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '报名人',
  `friends` varchar(2000) DEFAULT NULL COMMENT '邀请的好友',
  `is_anonymity` bit(1) DEFAULT b'0' COMMENT '是否匿名',
  `amount` int(11) DEFAULT '0' COMMENT '报名数量',
  `fee` bigint(20) DEFAULT '0' COMMENT '报名总费用（单价*数量）',
  `status` tinyint(4) DEFAULT '0' COMMENT '报名状态：\r\n            1：已报名\r\n            2：已入选\r\n            3：未入选\r\n            4：已取消\r\n            5：确认出席，准备校验验证码',
  `validation_status` bit(1) DEFAULT b'0' COMMENT '验证状态\r\n            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：\r\n            0：未验证\r\n            1：已验证\r\n            2：验证失败\r\n            ',
  `times` tinyint(4) DEFAULT '0' COMMENT '邀请码重试次数',
  `is_pay` bit(1) DEFAULT b'0' COMMENT '是否支付',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '邀请人',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`),
  KEY `idx_meetingId` (`meeting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动聚会报名表';

-- ----------------------------
-- Table structure for meeting_send
-- ----------------------------
DROP TABLE IF EXISTS `meeting_send`;
CREATE TABLE `meeting_send` (
  `id` varchar(32) NOT NULL,
  `meeting_id` varchar(32) DEFAULT NULL COMMENT '主表ID',
  `user_id` varchar(32) DEFAULT NULL,
  `is_default` bit(1) DEFAULT b'0' COMMENT '是否主发起人',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            0：待同意\r\n            1：同意（已发起）\r\n            2：拒绝\r\n            3：已取消',
  `accept_at` datetime DEFAULT NULL COMMENT '同意时间',
  `refuse_at` datetime DEFAULT NULL COMMENT '拒绝时间',
  `expired_at` datetime DEFAULT NULL COMMENT '过期时间',
  `validation_status` bit(1) DEFAULT b'0' COMMENT '距离验证状态：\r\n            0：未验证\r\n            1：通过验证\r\n            2：验证失败',
  `code` int(11) DEFAULT '0' COMMENT '邀请码',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动聚会表发起表';

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_service_code` varchar(8) DEFAULT NULL COMMENT '客服代码',
  `name` varchar(100) NOT NULL,
  `position` tinyint(4) DEFAULT NULL COMMENT '商家职务：\r\n            0：客服代理\r\n            1：商户代理\r\n            2：市场总监\r\n            3：营运总裁商家职务',
  `parent_merchant_id` varchar(32) DEFAULT NULL COMMENT '父商家ID',
  `referral_service_code` varchar(8) DEFAULT NULL COMMENT '引荐客服代码',
  `province_code` varchar(10) DEFAULT NULL,
  `city_code` varchar(10) DEFAULT NULL,
  `area_code` varchar(10) DEFAULT NULL,
  `addr_country` varchar(100) DEFAULT NULL,
  `addr_detail` varchar(300) DEFAULT NULL,
  `addr_door_number` varchar(100) DEFAULT NULL,
  `addr_tel` varchar(20) DEFAULT NULL,
  `addr_contact` varchar(20) DEFAULT NULL COMMENT '地址-联系人',
  `lon` decimal(10,6) DEFAULT NULL,
  `lat` decimal(10,6) DEFAULT NULL,
  `desc` text,
  `status` int(1) DEFAULT '1' COMMENT '商家状态\r\n            1：正常\r\n            2：拉黑',
  `disable_reason` varchar(300) DEFAULT NULL COMMENT '禁用原因',
  `enable_reason` varchar(255) DEFAULT NULL COMMENT '启用原因',
  `visit_count` int(11) DEFAULT '0' COMMENT '访问量',
  `qrcode` text,
  `pay_status` int(11) DEFAULT '1' COMMENT '注册管理费缴费状态\r\n0：已缴费\r\n1：待缴费\r\n2：待续费',
  `fee_time` datetime DEFAULT NULL COMMENT '缴费时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `is_support_credit` bit(1) DEFAULT b'0' COMMENT '是否支持网信',
  `pac_set_id` varchar(32) DEFAULT NULL COMMENT '红包设置',
  `today_packet_pool_amount` bigint(20) DEFAULT '0' COMMENT '今日红包池金额',
  `packet_pool_amount` bigint(20) DEFAULT '0' COMMENT '昨日红包池金额',
  `second_num` int(11) DEFAULT '0' COMMENT '二级数量',
  `third_num` int(11) DEFAULT '0' COMMENT '三级数量',
  `day_num` int(11) DEFAULT '0' COMMENT '日增加下级数',
  `month_num` int(11) DEFAULT '0' COMMENT '一个月内发展的商家数',
  `month_second_num` int(11) DEFAULT '0' COMMENT '每月累积第二级商家数',
  `month_third_num` int(11) DEFAULT '0' COMMENT '每月累积第三级商家数',
  `group_no` int(11) DEFAULT '0' COMMENT '组内第几名',
  `achievement_month` int(11) DEFAULT '0' COMMENT '月业绩',
  `achievement_total` bigint(20) DEFAULT '0' COMMENT '累计业绩',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for merchant_add_customeragent
-- ----------------------------
DROP TABLE IF EXISTS `merchant_add_customeragent`;
CREATE TABLE `merchant_add_customeragent` (
  `id` varchar(32) NOT NULL COMMENT '唯一标识',
  `merchant_id` varchar(32) DEFAULT NULL COMMENT '商家id',
  `to_merchant_id` varchar(32) DEFAULT NULL COMMENT '被添加的商家id',
  `state` tinyint(1) DEFAULT '0' COMMENT '状态：\r\n            0：等待同意\r\n            1：同意请求\r\n            2：不同意请求',
  `reason` varchar(500) DEFAULT NULL COMMENT '添加理由',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-添加客服代理请求表';

-- ----------------------------
-- Table structure for merchant_area
-- ----------------------------
DROP TABLE IF EXISTS `merchant_area`;
CREATE TABLE `merchant_area` (
  `id` varchar(32) NOT NULL COMMENT '唯一标识',
  `area_name` varchar(32) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-商家区域表';

-- ----------------------------
-- Table structure for merchant_category
-- ----------------------------
DROP TABLE IF EXISTS `merchant_category`;
CREATE TABLE `merchant_category` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `merchant_id` varchar(32) DEFAULT NULL COMMENT '商家id',
  `category_id` varchar(500) DEFAULT NULL COMMENT '商品类目',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-商家类别表';

-- ----------------------------
-- Table structure for merchant_express
-- ----------------------------
DROP TABLE IF EXISTS `merchant_express`;
CREATE TABLE `merchant_express` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '快递公司名称',
  `type` varchar(255) DEFAULT NULL COMMENT '快递公司拼音',
  `letter` varchar(50) DEFAULT NULL COMMENT '开头字母',
  `tel` varchar(50) DEFAULT NULL COMMENT '电话号码',
  `number` varchar(32) DEFAULT NULL COMMENT '代号',
  `hot` int(11) DEFAULT '0' COMMENT '热门度',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-快递公司名单表';

-- ----------------------------
-- Table structure for merchant_favorite
-- ----------------------------
DROP TABLE IF EXISTS `merchant_favorite`;
CREATE TABLE `merchant_favorite` (
  `id` varchar(32) NOT NULL COMMENT '标识id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `merchant_id` varchar(32) DEFAULT NULL COMMENT '商家id',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-商家收藏表';

-- ----------------------------
-- Table structure for merchant_manager
-- ----------------------------
DROP TABLE IF EXISTS `merchant_manager`;
CREATE TABLE `merchant_manager` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `merchant_id` varchar(32) DEFAULT NULL COMMENT '商家id',
  `user_id` varchar(32) NOT NULL COMMENT '商家注册者用户ID',
  `merchant_user_type` varchar(255) DEFAULT NULL COMMENT '商家管理人员类型：法人代表，收银人员，业务主管',
  `user_name` varchar(20) DEFAULT NULL COMMENT '收银人员姓名',
  `user_phone` varchar(20) NOT NULL COMMENT '收银人手机号',
  `user_network_num` varchar(20) NOT NULL COMMENT '收银人网号',
  `is_current` int(2) NOT NULL DEFAULT '0' COMMENT '是否商家使用中： 0：不是   1：是   ',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-收银人员表';

-- ----------------------------
-- Table structure for merchant_order_info
-- ----------------------------
DROP TABLE IF EXISTS `merchant_order_info`;
CREATE TABLE `merchant_order_info` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL COMMENT '下单者id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '下单者名字',
  `merchant_id` varchar(32) DEFAULT NULL COMMENT '商家id',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `order_type` varchar(32) DEFAULT NULL COMMENT '订单类型：\r\n1：normal_order\r\n2：demand_order\r\n3：activity_order',
  `order_type_business_id` varchar(32) DEFAULT NULL COMMENT '业务id',
  `order_total_fee` bigint(20) DEFAULT NULL COMMENT '订单总费用',
  `product_total_fee` bigint(20) DEFAULT NULL COMMENT '商品总费用',
  `order_time` datetime DEFAULT NULL COMMENT '下单时间',
  `finish_time` datetime DEFAULT NULL,
  `verify_time` datetime DEFAULT NULL,
  `order_status` varchar(255) DEFAULT NULL COMMENT '订单状态',
  `pay_code` varchar(255) DEFAULT NULL,
  `pay_submit_time` datetime DEFAULT NULL,
  `pay_receive_time` datetime DEFAULT NULL,
  `pay_status` varchar(255) DEFAULT NULL,
  `pay_out_no` varchar(255) DEFAULT NULL,
  `shipping_fee` bigint(20) DEFAULT NULL,
  `shipping_code` varchar(255) DEFAULT NULL,
  `shipping_logistics_no` varchar(255) DEFAULT NULL,
  `shipping_time` datetime DEFAULT NULL,
  `shipping_status` varchar(255) DEFAULT NULL,
  `shipping_logistics_details` varchar(1000) DEFAULT NULL COMMENT '物流详情',
  `delivery_way` int(20) DEFAULT NULL COMMENT '配送方式\r\n1：支持配送\r\n2：不提供配送，仅限现场消费\r\n3：提供外卖配送',
  `remark` varchar(255) DEFAULT NULL,
  `consignee` varchar(255) DEFAULT NULL COMMENT '收货人',
  `full_address` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `cancel_reason` varchar(1000) DEFAULT NULL COMMENT '取消原因',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `is_comment` tinyint(4) DEFAULT '0',
  `remind_time` datetime DEFAULT NULL COMMENT '上一次催单时间',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for merchant_order_item
-- ----------------------------
DROP TABLE IF EXISTS `merchant_order_item`;
CREATE TABLE `merchant_order_item` (
  `id` varchar(32) NOT NULL,
  `order_no` varchar(32) DEFAULT NULL,
  `merchant_id` varchar(32) DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `sku_id` varchar(32) DEFAULT NULL,
  `sku_desc` varchar(128) DEFAULT NULL,
  `product_name` varchar(512) DEFAULT NULL,
  `product_img_url` varchar(512) DEFAULT NULL,
  `unit_price` bigint(20) DEFAULT NULL,
  `final_unit_price` bigint(20) DEFAULT NULL,
  `product_id` varchar(32) DEFAULT NULL,
  `order_status` varchar(255) DEFAULT NULL,
  `pay_status` varchar(255) DEFAULT NULL,
  `shipping_status` varchar(255) DEFAULT NULL,
  `trade_status` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for merchant_order_put_off
-- ----------------------------
DROP TABLE IF EXISTS `merchant_order_put_off`;
CREATE TABLE `merchant_order_put_off` (
  `id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '业务id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户id',
  `merchant_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '商家id',
  `order_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '订单id',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `agree_time` datetime DEFAULT NULL COMMENT '同意时间',
  `confirm_time` datetime DEFAULT NULL COMMENT '撤销时间',
  `status` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '状态：\r\n1：用户申请延期\r\n2：商家同意延期\r\n3：商家拒收延期\r\n4 : 用户撤销延期',
  `expiration_time` datetime DEFAULT NULL COMMENT '延期到期时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`),
  KEY `FK_OrderId` (`order_id`),
  CONSTRAINT `FK_OrderId` FOREIGN KEY (`order_id`) REFERENCES `merchant_order_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for merchant_order_return
-- ----------------------------
DROP TABLE IF EXISTS `merchant_order_return`;
CREATE TABLE `merchant_order_return` (
  `id` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '下单者id',
  `merchant_user_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '商家用户id',
  `order_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '订单id',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `address` varchar(255) DEFAULT NULL COMMENT '退货地址',
  `agree_time` datetime DEFAULT NULL COMMENT '同意时间',
  `arbitration_description` varchar(1000) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '仲裁结果描述',
  `is_settled` bit(1) DEFAULT b'0',
  `logistics_code` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '退货物流代号',
  `logistics_no` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '退货物流单号',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认退货时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '撤销退货时间',
  `success_time` datetime DEFAULT NULL COMMENT '退货成功时间',
  `status` varchar(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '状态：\r\n1：用户申请退货\r\n2：商家同意退货\r\n3：用户确认退货\r\n4：用户撤销退货\r\n5：双方退货成功\r\n6：商家拒收退货\r\n7：用户退货申诉',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`),
  KEY `FK_Return_OrderId` (`order_id`),
  CONSTRAINT `FK_Return_OrderId` FOREIGN KEY (`order_id`) REFERENCES `merchant_order_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for merchant_packet_set
-- ----------------------------
DROP TABLE IF EXISTS `merchant_packet_set`;
CREATE TABLE `merchant_packet_set` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `is_change_rate` bit(1) DEFAULT b'0' COMMENT '是否变动提成',
  `first_rate` decimal(10,2) DEFAULT '0.00' COMMENT '首单提成比例',
  `gradual_rate` decimal(10,2) DEFAULT '0.00' COMMENT '逐单提成比例',
  `limit_rate` decimal(10,2) DEFAULT '0.00' COMMENT '封顶提成比例',
  `is_fixed_rate` bit(1) DEFAULT b'0' COMMENT '是否固定提成',
  `fixed_rate` decimal(10,2) DEFAULT '0.00' COMMENT '固定提成比例',
  `is_start_packet` bit(1) DEFAULT b'0' COMMENT '是否启动红包金额',
  `packet_money` bigint(20) DEFAULT NULL COMMENT '红包金额',
  `merchant_id` varchar(32) DEFAULT NULL COMMENT '商家id',
  `send_time` datetime DEFAULT NULL COMMENT '红包发放时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-红包设置表';

-- ----------------------------
-- Table structure for merchant_picture
-- ----------------------------
DROP TABLE IF EXISTS `merchant_picture`;
CREATE TABLE `merchant_picture` (
  `id` varchar(32) NOT NULL,
  `merchant_id` varchar(32) NOT NULL,
  `picture_url` varchar(128) NOT NULL,
  `merchant_picture_type` varchar(32) NOT NULL,
  `priority` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段，数字越小排的越前',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for merchant_product
-- ----------------------------
DROP TABLE IF EXISTS `merchant_product`;
CREATE TABLE `merchant_product` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `merchant_id` varchar(32) DEFAULT NULL COMMENT '商家ID',
  `product_id` varchar(32) DEFAULT NULL COMMENT '商品ID',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-商家与商品关联表';

-- ----------------------------
-- Table structure for merchant_recording_history
-- ----------------------------
DROP TABLE IF EXISTS `merchant_recording_history`;
CREATE TABLE `merchant_recording_history` (
  `id` varchar(32) NOT NULL COMMENT '唯一标识',
  `merchant_id` varchar(32) DEFAULT NULL COMMENT '商家id',
  `to_merchant_id` varchar(32) DEFAULT NULL COMMENT '提成金额',
  `type` tinyint(1) DEFAULT '0' COMMENT '提成类型：\r\n            0：直接提成\r\n            1：间接提成',
  `money` bigint(20) DEFAULT NULL COMMENT '金额',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-提成记录表';

-- ----------------------------
-- Table structure for merchant_verify_info
-- ----------------------------
DROP TABLE IF EXISTS `merchant_verify_info`;
CREATE TABLE `merchant_verify_info` (
  `id` varchar(32) NOT NULL,
  `merchant_id` varchar(32) NOT NULL,
  `verify_info` varchar(255) NOT NULL COMMENT '验证信息，如 身份证，手机号码等',
  `verify_type` varchar(255) NOT NULL COMMENT '验证类型，如身份证，手机号码等',
  `verify_status` varchar(255) NOT NULL COMMENT '验证状态： 如已通过验证，未通过验证',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `merchant_id` varchar(100) DEFAULT NULL COMMENT '供应商家',
  `name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `characteristic` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '商品描述',
  `description` text CHARACTER SET utf8mb4 COMMENT '商品描述',
  `is_delivery` bit(1) DEFAULT b'0' COMMENT '是否配送\r\n',
  `is_return` bit(1) DEFAULT b'0' COMMENT '是否支持退换\r\n',
  `delivery_way` int(20) DEFAULT NULL COMMENT '配送方式\r\n1：支持配送\r\n2：不提供配送，仅限现场消费\r\n3：提供外卖配送',
  `online_status` tinyint(20) DEFAULT NULL COMMENT '商品状态\r\n            1：上架\r\n            2：下架 \r\n            3:  系统下架\r\n            4：系统上架',
  `visit_count` int(11) DEFAULT '0' COMMENT '访问量',
  `shipping_fee_id` varchar(255) DEFAULT NULL COMMENT '运费设置id',
  `shipping_fee` bigint(22) NOT NULL DEFAULT '0' COMMENT '商品运费',
  `publisher_user_id` varchar(32) NOT NULL COMMENT '商品发布者userId',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-商品表';

-- ----------------------------
-- Table structure for product_delivery
-- ----------------------------
DROP TABLE IF EXISTS `product_delivery`;
CREATE TABLE `product_delivery` (
  `id` varchar(32) NOT NULL,
  `product_id` varchar(32) NOT NULL,
  `fee` bigint(22) NOT NULL DEFAULT '0',
  `delivery_way` int(20) NOT NULL COMMENT '配送方式\r\n1：支持配送\r\n2：不提供配送，仅限现场消费\r\n3：提供外卖配送',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for product_favorite
-- ----------------------------
DROP TABLE IF EXISTS `product_favorite`;
CREATE TABLE `product_favorite` (
  `id` varchar(32) NOT NULL COMMENT '标识id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `product_id` varchar(32) DEFAULT NULL COMMENT '商品id',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-商品关注表';

-- ----------------------------
-- Table structure for product_management
-- ----------------------------
DROP TABLE IF EXISTS `product_management`;
CREATE TABLE `product_management` (
  `id` varchar(32) NOT NULL COMMENT '标识id',
  `product_id` varchar(32) NOT NULL COMMENT '商品id',
  `user_id` varchar(32) NOT NULL COMMENT '操作者id',
  `online_status` tinyint(20) DEFAULT NULL COMMENT '商品状态\r\n            1：上架\r\n            2：下架\r\n            3：强制下架',
  `reason` varchar(300) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '下架/上架原因',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product_picture
-- ----------------------------
DROP TABLE IF EXISTS `product_picture`;
CREATE TABLE `product_picture` (
  `id` varchar(32) NOT NULL,
  `product_id` varchar(32) NOT NULL,
  `picture_url` varchar(128) NOT NULL,
  `product_picture_type` varchar(32) NOT NULL,
  `priority` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段，数字越小排的越前',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for product_property_effect_pic
-- ----------------------------
DROP TABLE IF EXISTS `product_property_effect_pic`;
CREATE TABLE `product_property_effect_pic` (
  `id` varchar(32) NOT NULL,
  `product_id` varchar(32) NOT NULL,
  `property_id` varchar(32) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录新增的时间',
  `update_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录最近修改的时间，如果为新增后未改动则保持与createTime一致',
  `deleted` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品中心-记录商品属性中影响sku图片的属性';

-- ----------------------------
-- Table structure for product_property_value_detail
-- ----------------------------
DROP TABLE IF EXISTS `product_property_value_detail`;
CREATE TABLE `product_property_value_detail` (
  `id` varchar(32) NOT NULL,
  `product_id` varchar(32) NOT NULL,
  `property_id` varchar(32) NOT NULL,
  `value_id` varchar(32) NOT NULL,
  `property_value` bigint(20) NOT NULL,
  `property_type` tinyint(4) NOT NULL COMMENT '1销售属性,2关键属性',
  `picture_id` varchar(32) NOT NULL,
  `big_picture_id` varchar(32) NOT NULL,
  `color` varchar(15) NOT NULL DEFAULT '0000',
  `alias` varchar(63) NOT NULL,
  `priority` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段，数字越小排的越前',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录新增的时间',
  `update_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录最近修改的时间，如果为新增后未改动则保持与createTime一致',
  `deleted` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品中心-商品属性图与别名';

-- ----------------------------
-- Table structure for product_sku_spec
-- ----------------------------
DROP TABLE IF EXISTS `product_sku_spec`;
CREATE TABLE `product_sku_spec` (
  `id` varchar(32) NOT NULL,
  `sku_id` varchar(32) NOT NULL,
  `property_id` varchar(32) DEFAULT NULL,
  `value_id` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for property
-- ----------------------------
DROP TABLE IF EXISTS `property`;
CREATE TABLE `property` (
  `id` varchar(32) NOT NULL,
  `merchant_id` varchar(32) NOT NULL,
  `name` varchar(127) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录新增的时间',
  `update_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录最近修改的时间，如果为新增后未改动则保持与createTime一致',
  `deleted` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品中心-属性';

-- ----------------------------
-- Table structure for redpacket_pool
-- ----------------------------
DROP TABLE IF EXISTS `redpacket_pool`;
CREATE TABLE `redpacket_pool` (
  `id` varchar(32) NOT NULL,
  `redpacket_amount` bigint(20) DEFAULT '0' COMMENT '今日红包金额',
  `total_amount` bigint(20) DEFAULT '0' COMMENT '总额',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-红包池';

-- ----------------------------
-- Table structure for redpacket_pool_record
-- ----------------------------
DROP TABLE IF EXISTS `redpacket_pool_record`;
CREATE TABLE `redpacket_pool_record` (
  `id` varchar(32) NOT NULL,
  `amount` bigint(20) DEFAULT NULL COMMENT '金额',
  `way` int(11) DEFAULT NULL COMMENT '方式 1.支入 2.支出',
  `source_id` varchar(32) DEFAULT NULL COMMENT '来源绑定Id',
  `source` int(11) DEFAULT NULL COMMENT '来源：1.交易额的提成 2.图文及音视的广告点击费 3.网币发行的溢价部分（扣除平台计提的利润） 4.从活动、需求、技能等收益中的提成 5.心愿款的余额部分 6.上日的红包启动金额 7.答题竞购 8.红包发放 9.报名申购 10注销用户的零钱',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-红包池记录表';

-- ----------------------------
-- Table structure for redpacket_record
-- ----------------------------
DROP TABLE IF EXISTS `redpacket_record`;
CREATE TABLE `redpacket_record` (
  `id` varchar(32) NOT NULL,
  `redpacket_send_id` varchar(32) DEFAULT NULL COMMENT '红包发放id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `amount` bigint(20) DEFAULT '0' COMMENT '红包金额',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-红包记录表';

-- ----------------------------
-- Table structure for redpacket_send
-- ----------------------------
DROP TABLE IF EXISTS `redpacket_send`;
CREATE TABLE `redpacket_send` (
  `id` varchar(32) NOT NULL COMMENT '红包发放id',
  `redpacket_num` int(10) DEFAULT NULL COMMENT '红包个数',
  `num` int(10) DEFAULT NULL COMMENT '发放人数',
  `amount` bigint(20) DEFAULT NULL COMMENT '红包金额',
  `surplus_amount` bigint(20) DEFAULT '0' COMMENT '剩余红包金额',
  `send_time` time DEFAULT NULL COMMENT '发放时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网商-红包发放表';

-- ----------------------------
-- Table structure for refund
-- ----------------------------
DROP TABLE IF EXISTS `refund`;
CREATE TABLE `refund` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '申请人',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `relatable_type` varchar(100) DEFAULT NULL COMMENT '关联类型，值为Model名',
  `relatable_id` varchar(32) DEFAULT NULL COMMENT '关联ID',
  `amount` bigint(20) DEFAULT '0' COMMENT '退款金额',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            0：已申请退款\r\n            1：已同意同款\r\n            2：已拒绝退款\r\n            3：超期自动同意退款',
  `operate_user_id` varchar(32) DEFAULT NULL COMMENT '通过或拒绝人ID',
  `bail` bigint(20) DEFAULT '0' COMMENT '申请退款时支付的费用：\r\n            即选择：解冻部分费用并退回给我，其余部分支付给Ta',
  `pay_way` tinyint(4) DEFAULT '0' COMMENT '支付方式：0：网币 1：现金',
  `expired_at` datetime DEFAULT NULL COMMENT '过期时间，过期后将自动退款',
  `is_process` bit(1) DEFAULT b'0' COMMENT '定时任务是否已处理',
  `process_at` datetime DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_relatableId_and_relatableType` (`relatable_type`,`relatable_id`),
  KEY `idx_userId` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_isProcess` (`is_process`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款表';

-- ----------------------------
-- Table structure for settlement
-- ----------------------------
DROP TABLE IF EXISTS `settlement`;
CREATE TABLE `settlement` (
  `id` varchar(32) NOT NULL,
  `relatable_type` varchar(100) DEFAULT NULL COMMENT '关联类型，值为Model名',
  `relatable_id` varchar(32) DEFAULT NULL COMMENT '关联ID',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `is_can` bit(1) DEFAULT b'0' COMMENT '是否能结算，是指该项是否能够进入结算流程',
  `expired_at` datetime DEFAULT NULL COMMENT '过期时间，过了这个时间就会结算',
  `is_finish` bit(1) DEFAULT b'0' COMMENT '是否完成结算',
  `finish_at` datetime DEFAULT NULL COMMENT '结算时间',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_type_and_id` (`relatable_type`,`relatable_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网值结算表';

-- ----------------------------
-- Table structure for settlement_amount
-- ----------------------------
DROP TABLE IF EXISTS `settlement_amount`;
CREATE TABLE `settlement_amount` (
  `id` varchar(32) NOT NULL,
  `settlement_id` varchar(32) DEFAULT NULL COMMENT '结算表ID',
  `amount` bigint(20) DEFAULT '0' COMMENT '金额，+-表示',
  `user_id` varchar(32) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='结算金额表';

-- ----------------------------
-- Table structure for settlement_credit
-- ----------------------------
DROP TABLE IF EXISTS `settlement_credit`;
CREATE TABLE `settlement_credit` (
  `id` varchar(32) NOT NULL,
  `settlement_id` varchar(32) DEFAULT NULL COMMENT '结算表ID',
  `credit` int(11) DEFAULT '0' COMMENT '信用，+-表示',
  `user_id` varchar(32) DEFAULT NULL,
  `is_can` bit(1) DEFAULT b'0' COMMENT '是否可被计算，业务规定扣信用按最严重的一次算：\r\n            0：不可被计算\r\n            1：可以被计算\r\n            当主表被标记为可以结算时，来这个表找true的记录。一条结算只能存在一条为true的记录。',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='结算信用表';

-- ----------------------------
-- Table structure for settlement_log
-- ----------------------------
DROP TABLE IF EXISTS `settlement_log`;
CREATE TABLE `settlement_log` (
  `id` varchar(32) NOT NULL,
  `settlement_id` varchar(32) DEFAULT NULL COMMENT '结算表Id',
  `settlement_credit_id` varchar(32) DEFAULT NULL COMMENT '结算信用表ID',
  `settlement_amount_id` varchar(32) DEFAULT NULL COMMENT '结算金额表ID',
  `user_id` varchar(32) DEFAULT NULL,
  `credit` int(11) DEFAULT '0' COMMENT '信用，+-表示',
  `amount` bigint(20) DEFAULT '0' COMMENT '金额，+-表示',
  `last_credit` int(11) DEFAULT '0' COMMENT '结算前总信用',
  `last_amount` bigint(20) DEFAULT '0' COMMENT '结算前总金额',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`settlement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='结算流水表 每次结算时插入';

-- ----------------------------
-- Table structure for shipping_fee
-- ----------------------------
DROP TABLE IF EXISTS `shipping_fee`;
CREATE TABLE `shipping_fee` (
  `id` varchar(32) NOT NULL COMMENT '标识ID',
  `merchant_id` varchar(32) NOT NULL COMMENT '商家id',
  `fee` bigint(22) NOT NULL DEFAULT '0' COMMENT '商家统一运费',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for skill
-- ----------------------------
DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `skill` varchar(2000) DEFAULT NULL COMMENT '技能标签，逗号分隔',
  `level` varchar(2000) DEFAULT NULL COMMENT '水平标签，逗号分隔',
  `description` text CHARACTER SET utf8mb4 COMMENT '描述',
  `skill_images_url` varchar(2000) DEFAULT NULL COMMENT '图片',
  `skill_detail_images_url` varchar(2000) DEFAULT NULL COMMENT '图片',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `amount` bigint(20) DEFAULT '0' COMMENT '单价',
  `intr` varchar(2000) DEFAULT NULL COMMENT '价格说明',
  `obj` tinyint(4) DEFAULT '0' COMMENT '预约对象：\r\n            1：不限制。\r\n            2：仅限线上交易\r\n            3：仅接受附近预约\r\n            4：仅限女性预约\r\n            5：仅限男性预约\r\n            6：仅限好友预约',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `register_count` int(11) DEFAULT '0' COMMENT '已预约人数',
  `success_count` int(11) DEFAULT '0' COMMENT '已成功人数',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            1：已发布\r\n            2：已取消\r\n            3：已结束',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_lon_lat` (`lon`,`lat`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='技能表';

-- ----------------------------
-- Table structure for skill_order
-- ----------------------------
DROP TABLE IF EXISTS `skill_order`;
CREATE TABLE `skill_order` (
  `id` varchar(32) NOT NULL,
  `skill_register_id` varchar(32) DEFAULT NULL COMMENT '预约表Id',
  `start_at` datetime DEFAULT NULL COMMENT '开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '结束时间',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `amount` bigint(20) DEFAULT '0' COMMENT '单价',
  `number` int(11) DEFAULT '0' COMMENT '数量',
  `fee` bigint(20) DEFAULT '0' COMMENT '总价（单价*数量）',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `address` varchar(2000) DEFAULT NULL COMMENT '地址',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            0：初始化\r\n            1：已开始\r\n            2：已取消\r\n            3：已成功\r\n            4：已失败\r\n            ',
  `code` int(11) DEFAULT '0' COMMENT '邀请码',
  `validation_status` bit(1) DEFAULT b'0' COMMENT '验证状态\r\n            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：\r\n            0：未验证\r\n            1：已验证\r\n            2：验证失败\r\n            ',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_lon_lat` (`lon`,`lat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='技能单表';

-- ----------------------------
-- Table structure for skill_register
-- ----------------------------
DROP TABLE IF EXISTS `skill_register`;
CREATE TABLE `skill_register` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `skill_id` varchar(32) DEFAULT NULL COMMENT '主表id',
  `start_at` datetime DEFAULT NULL COMMENT '建议的开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '建议的结束时间',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `amount` bigint(20) DEFAULT '0' COMMENT '单价',
  `number` int(11) DEFAULT '0' COMMENT '数量',
  `fee` bigint(20) DEFAULT '0' COMMENT '总价（单价*数量）',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `address` varchar(2000) DEFAULT NULL COMMENT '地址',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `status` tinyint(4) DEFAULT '0' COMMENT '报名状态：\r\n            0：待入选\r\n            1：已入选\r\n            2：已拒绝\r\n            3：已过期\r\n            4：退款中',
  `is_pay` bit(1) DEFAULT b'0' COMMENT '是否支付给发布方',
  `bail` bigint(20) DEFAULT '0' COMMENT '已托管的费用',
  `pay_way` int(11) DEFAULT '0' COMMENT '托管的付款方式：0：网币，1：平台垫付',
  `is_anonymity` bit(1) DEFAULT b'0' COMMENT '是否匿名',
  `validation_status` bit(1) DEFAULT b'0' COMMENT '验证状态\r\n            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：\r\n            0：未验证\r\n            1：已验证\r\n            \r\n            ',
  `is_validation` bit(1) DEFAULT b'0' COMMENT '验证码是否通过',
  `times` tinyint(4) DEFAULT '0' COMMENT '邀请码重试次数',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`),
  KEY `idx_lon_lat` (`lon`,`lat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='技能预约表';

-- ----------------------------
-- Table structure for sku
-- ----------------------------
DROP TABLE IF EXISTS `sku`;
CREATE TABLE `sku` (
  `id` varchar(32) NOT NULL,
  `product_id` varchar(32) NOT NULL COMMENT '对应的商品id',
  `storage_nums` int(11) NOT NULL DEFAULT '0' COMMENT '此sku实际库存量,每次销售需要减少',
  `sell_nums` int(11) NOT NULL DEFAULT '0' COMMENT '此sku当前销量，每次销售需要增加',
  `trade_max_nums` int(11) NOT NULL DEFAULT '5' COMMENT '单笔订单最大能购买的数量,0为不限制',
  `market_price` bigint(20) NOT NULL DEFAULT '0' COMMENT '专柜价',
  `price` bigint(20) NOT NULL DEFAULT '0' COMMENT 'SFM价',
  `sku_bar_code` varchar(127) DEFAULT NULL COMMENT '条形码',
  `default_sku` tinyint(4) NOT NULL DEFAULT '0' COMMENT '为1时作为默认sku',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录新增的时间',
  `update_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '记录最近修改的时间，如果为新增后未改动则保持与createTime一致',
  `deleted` tinyint(4) NOT NULL,
  `unit` varchar(32) DEFAULT NULL COMMENT '商品单位',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品中心-SKU';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_number` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '网号',
  `nickname` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实姓名',
  `sex` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `pay_password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '钱包密码',
  `admin_password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '管理密码',
  `jmessage_password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '手机号',
  `role` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色：\r\n            1.系统管理\r\n            2.用户管理\r\n            3.商家管理\r\n            4.资讯管理\r\n            5.财务管理\r\n            6.仲裁管理',
  `is_admin_user` bit(1) DEFAULT b'0' COMMENT '是否为管理员帐号：\r\n            0：否\r\n            1：是',
  `is_lock` bit(1) DEFAULT b'0' COMMENT '是否锁定，拉黑之后将其锁定',
  `lock_version` int(11) DEFAULT '0' COMMENT '乐观锁：\r\n            查询时先带出，更新时+1。\r\n            更新伪代码：\r\n            lockVersion = lockVersion +1 where lockVersion = 查询时带出的',
  `id_number` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通过认证的证件号，下面这5项认证信息在后台通过认证后更新',
  `video` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '视频信息（根据需求设置内容）',
  `car` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '车辆信息',
  `house` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '房产信息',
  `degree` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学历信息',
  `education_label` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文化教育概况',
  `profession_label` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工作经历概况',
  `interest_label` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '兴趣爱好概况',
  `last_login_at` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_active_at` datetime DEFAULT NULL COMMENT '最后操作时间',
  `login_days` int(11) DEFAULT '0' COMMENT '连续登录天数',
  `lon` decimal(10,6) DEFAULT NULL COMMENT '地址-经度',
  `lat` decimal(10,6) DEFAULT NULL COMMENT '地址-纬度',
  `gift_setting` tinyint(4) DEFAULT '0' COMMENT '礼物设置：\r\n            0：不接受礼物\r\n            1：接受好友礼物\r\n            2：接受我关注的人的礼物',
  `invitation_setting` tinyint(4) DEFAULT '0' COMMENT '邀请设置：\r\n            0：不接受邀请\r\n            1：接受好友的邀请\r\n            2：接受我关注的人的邀请',
  `article_setting` tinyint(4) DEFAULT '0' COMMENT '咨讯设置：\r\n            0：不起任何作用\r\n            1：仅限好友查看',
  `nearly_setting` tinyint(4) DEFAULT '0' COMMENT '附近设置：\r\n0：显示我的信息\r\n1：不显示我的信息',
  `voice_setting` tinyint(4) DEFAULT '1' COMMENT '语音设置：\r\n0：关闭\r\n1：开启',
  `shock_setting` tinyint(4) DEFAULT '1' COMMENT '震动设置：\r\n0：关闭\r\n1：开启',
  `score` decimal(10,2) DEFAULT '0.00' COMMENT '总积分',
  `credit` int(11) DEFAULT '0' COMMENT '总信用',
  `value` decimal(10,2) DEFAULT '0.00' COMMENT '总身价',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '总收益',
  `contribution` decimal(10,2) DEFAULT '0.00' COMMENT '总贡献',
  `lv` int(11) DEFAULT '0' COMMENT '用户等级',
  `user_profile_score` int(11) DEFAULT '0' COMMENT '资料完整度分值',
  `last_complete_percent` decimal(10,2) DEFAULT '0.00' COMMENT '上次统计的资料完成度百分比',
  `current_likes` int(255) DEFAULT '0' COMMENT '当前累积的点赞次数，需按业务要求定量清0',
  `current_watch_to` int(255) DEFAULT '0' COMMENT '当前累积的关注次数，需按业务要求定量清0',
  `current_watch_from` int(255) DEFAULT '0' COMMENT '当前累积的被关注次数，需按业务要求定量清0',
  `approval_time` datetime DEFAULT NULL COMMENT '批准日期（即设为管理员的日期）',
  `is_reg_jMessage` bit(1) DEFAULT b'0' COMMENT '是否注册了极光',
  `is_login` bit(1) DEFAULT b'0' COMMENT '是否登录',
  `is_login_backend` bit(1) DEFAULT b'0' COMMENT '是否后台登录',
  `is_publish_credit` bit(1) DEFAULT b'0' COMMENT '是否发行过网信',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Table structure for user_admin
-- ----------------------------
DROP TABLE IF EXISTS `user_admin`;
CREATE TABLE `user_admin` (
  `id` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
  `user_name` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '登录名',
  `password` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '密码',
  `real_name` varchar(16) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '真实姓名',
  `mobile` varchar(11) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '手机号码',
  `is_super_admin` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是超级管理员',
  `reason` varchar(100) DEFAULT NULL,
  `create_user_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_user_name` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_article_likes
-- ----------------------------
DROP TABLE IF EXISTS `user_article_likes`;
CREATE TABLE `user_article_likes` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `article_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_like` tinyint(1) DEFAULT '0' COMMENT '是否是点赞（取消赞时更改此字段）',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_articleId` (`article_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='资讯点赞表';

-- ----------------------------
-- Table structure for user_blacklist
-- ----------------------------
DROP TABLE IF EXISTS `user_blacklist`;
CREATE TABLE `user_blacklist` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `target_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拉黑目标',
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_targetId` (`target_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户拉黑表（拉黑后对本人是黑名单，不影响其他人）';

-- ----------------------------
-- Table structure for user_blacklist_log
-- ----------------------------
DROP TABLE IF EXISTS `user_blacklist_log`;
CREATE TABLE `user_blacklist_log` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reason` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拉黑或释放理由',
  `system_black_list_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_system_blacklist_id` (`system_black_list_id`) USING BTREE,
  KEY `idx_userId` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='黑名单操作日志流水表(关联主表，取最新的一条，即为拉黑或释放理由)';

-- ----------------------------
-- Table structure for user_contribution
-- ----------------------------
DROP TABLE IF EXISTS `user_contribution`;
CREATE TABLE `user_contribution` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contribution` decimal(10,2) DEFAULT '0.00' COMMENT '本笔贡献，收入支出用正负号标识',
  `relatable_type` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联模型，使用模型的类名，如：User,UserProfile等',
  `relatable_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联主键：\r\n            关联具体得分的uuid，没有就是0',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_valueType` (`relatable_type`(191)) USING BTREE,
  KEY `idx_relatableId` (`relatable_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户贡献表';

-- ----------------------------
-- Table structure for user_credit
-- ----------------------------
DROP TABLE IF EXISTS `user_credit`;
CREATE TABLE `user_credit` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `credit` int(11) DEFAULT '0' COMMENT '本笔信用，收入支持用正负号标识',
  `relatable_type` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联模型，使用模型的类名，如：User,UserProfile等',
  `relatable_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联主键：\r\n            关联具体得分的uuid，没有就是0',
  `code` int(11) DEFAULT '0' COMMENT '代码，详见common项目的枚举',
  `description` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_createTime` (`create_user_id`) USING BTREE,
  KEY `idx_type` (`relatable_type`(191)) USING BTREE,
  KEY `idx_relatableId` (`relatable_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户信用表';

-- ----------------------------
-- Table structure for user_credit_likes
-- ----------------------------
DROP TABLE IF EXISTS `user_credit_likes`;
CREATE TABLE `user_credit_likes` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `credit_id` varchar(255) DEFAULT NULL COMMENT '网信id',
  `is_like` tinyint(4) DEFAULT NULL COMMENT '是否点赞 是 : 1 否 : 2',
  `create_time` datetime DEFAULT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user_id` varchar(255) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_education
-- ----------------------------
DROP TABLE IF EXISTS `user_education`;
CREATE TABLE `user_education` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `school` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学校名称',
  `department` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '院系名称',
  `speciality` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '专业名称',
  `year` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '入学年份',
  `time` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学习年限',
  `degree` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所获学位',
  `position` int(11) DEFAULT '0' COMMENT '位置序号',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户教育经历表\r\n凡是标签，均以字符形式存，以逗号分隔';

-- ----------------------------
-- Table structure for user_income
-- ----------------------------
DROP TABLE IF EXISTS `user_income`;
CREATE TABLE `user_income` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '本笔收益，收入支出用正负号标识',
  `relatable_type` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联模型，使用模型的类名，如：User,UserProfile等',
  `relatable_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联主键：\r\n            关联具体得分的uuid，没有就是0',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_valueType` (`relatable_type`(191)) USING BTREE,
  KEY `idx_relatableId` (`relatable_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户收益表';

-- ----------------------------
-- Table structure for user_interest
-- ----------------------------
DROP TABLE IF EXISTS `user_interest`;
CREATE TABLE `user_interest` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `interest_type` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '兴趣类别',
  `interest_detail` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '具体内容',
  `position` int(11) DEFAULT '0' COMMENT '位置序号',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户兴趣';

-- ----------------------------
-- Table structure for user_login_history
-- ----------------------------
DROP TABLE IF EXISTS `user_login_history`;
CREATE TABLE `user_login_history` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `login_at` datetime DEFAULT NULL COMMENT '登录时间',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_lon_lat` (`lon`,`lat`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='登录记录表';

-- ----------------------------
-- Table structure for user_oauth
-- ----------------------------
DROP TABLE IF EXISTS `user_oauth`;
CREATE TABLE `user_oauth` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `oauth_type` tinyint(4) DEFAULT '0' COMMENT '授权类型：\r\n            1.微信\r\n            2.支付宝\r\n            3.微博\r\n            4.QQ',
  `status` tinyint(4) DEFAULT '1' COMMENT '绑定状态：\r\n            0.解绑\r\n            1.绑定',
  `open_id` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '第三方id',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='第三方授权登录表';

-- ----------------------------
-- Table structure for user_pay_account
-- ----------------------------
DROP TABLE IF EXISTS `user_pay_account`;
CREATE TABLE `user_pay_account` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '主键id',
  `account_display` varchar(32) DEFAULT NULL,
  `account_identity` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '第三方返回的唯一用户id',
  `priority` int(11) DEFAULT NULL COMMENT '999表示当前使用    1.表示非当前使用',
  `account_type` tinyint(4) DEFAULT NULL COMMENT '微信还是支付宝:   1.微信    2.支付宝',
  `user_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '所属用户id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_photo
-- ----------------------------
DROP TABLE IF EXISTS `user_photo`;
CREATE TABLE `user_photo` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `url` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片的前缀url',
  `photo_key` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片的键值',
  `position` int(11) DEFAULT '0' COMMENT '图片的位置，位置为1的就是主图',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_position` (`position`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户照片表';

-- ----------------------------
-- Table structure for user_profession
-- ----------------------------
DROP TABLE IF EXISTS `user_profession`;
CREATE TABLE `user_profession` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `company` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单位全称',
  `department` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门',
  `top_profession` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最高职位',
  `year` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '入职年份',
  `position` int(11) DEFAULT '0' COMMENT '位置序号',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户职业经历表';

-- ----------------------------
-- Table structure for user_profile
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `often_in` text COLLATE utf8mb4_unicode_ci COMMENT '常驻',
  `home_town` text COLLATE utf8mb4_unicode_ci COMMENT '家乡',
  `already_to` text COLLATE utf8mb4_unicode_ci COMMENT '去过',
  `want_to` text COLLATE utf8mb4_unicode_ci COMMENT '想去',
  `address` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `introduce` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '介绍',
  `disposition` text COLLATE utf8mb4_unicode_ci COMMENT '性格',
  `appearance` text COLLATE utf8mb4_unicode_ci COMMENT '外貌',
  `income` int(11) DEFAULT '0' COMMENT '收入',
  `max_income` int(11) DEFAULT '0' COMMENT '最大工资',
  `emotion` text COLLATE utf8mb4_unicode_ci COMMENT '情感',
  `height` int(11) DEFAULT '0' COMMENT '身高',
  `weight` int(11) DEFAULT '0' COMMENT '体重',
  `nation` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '民族',
  `animal_signs` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '属相',
  `star_sign` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '星座',
  `blood_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '血型',
  `description` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图文详情',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户详情表\r\n凡是标签，均以字符形式存，以逗号分隔';

-- ----------------------------
-- Table structure for user_score
-- ----------------------------
DROP TABLE IF EXISTS `user_score`;
CREATE TABLE `user_score` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `score` decimal(10,2) DEFAULT '0.00' COMMENT '本笔积分，收入支持用正负号标识',
  `relatable_type` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联模型，使用模型的类名，如：User,UserProfile等',
  `relatable_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联主键：\r\n            关联具体得分的uuid，没有就是0',
  `code` int(11) DEFAULT '0' COMMENT '代码，详见common项目的枚举',
  `description` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_createTime` (`create_user_id`) USING BTREE,
  KEY `idx_type` (`relatable_type`(191)) USING BTREE,
  KEY `idx_relatableId` (`relatable_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户积分表';

-- ----------------------------
-- Table structure for user_suggest
-- ----------------------------
DROP TABLE IF EXISTS `user_suggest`;
CREATE TABLE `user_suggest` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '建议者id',
  `is_effective` int(30) DEFAULT NULL COMMENT '是否有效',
  `audit_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核人id',
  `audit_user_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '审批人名字',
  `suggest` text CHARACTER SET utf8mb4 NOT NULL COMMENT '建议',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '审批结果',
  `real_time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for user_system_blacklist
-- ----------------------------
DROP TABLE IF EXISTS `user_system_blacklist`;
CREATE TABLE `user_system_blacklist` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            0.已释放（进白名单）\r\n            1.已拉黑',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系统黑名单，拉黑后即锁定用户，类似封禁的功能';

-- ----------------------------
-- Table structure for user_value
-- ----------------------------
DROP TABLE IF EXISTS `user_value`;
CREATE TABLE `user_value` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `value` decimal(10,2) DEFAULT '0.00' COMMENT '本笔身价，收入支出用正负号标识',
  `relatable_type` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联模型，使用模型的类名，如：User,UserProfile等',
  `relatable_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联主键：\r\n            关联具体得分的uuid，没有就是0',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_valueType` (`relatable_type`(191)) USING BTREE,
  KEY `idx_relatableId` (`relatable_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户身价表';

-- ----------------------------
-- Table structure for user_verification_code
-- ----------------------------
DROP TABLE IF EXISTS `user_verification_code`;
CREATE TABLE `user_verification_code` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `code` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '验证码',
  `send_at` datetime DEFAULT NULL COMMENT '发送时间',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            0：已发送\r\n            1：已验证',
  `pass_at` datetime DEFAULT NULL COMMENT '验证通过时间',
  `expired_at` datetime DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户验证码';

-- ----------------------------
-- Table structure for user_verify
-- ----------------------------
DROP TABLE IF EXISTS `user_verify`;
CREATE TABLE `user_verify` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `verify_type` tinyint(4) DEFAULT '0' COMMENT '认证类型：\r\n            1：身份认证\r\n            2：视频认证\r\n            3：车辆认证\r\n            4：房产认证\r\n            5：学历认证',
  `description` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述信息，如身份证，就是身份号码，房产证就是房产证号码，类推',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            0：提交认证\r\n            1：认证通过\r\n            2：认证拒绝',
  `reason` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通过或拒绝原因',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户认证表';

-- ----------------------------
-- Table structure for user_verify_credit
-- ----------------------------
DROP TABLE IF EXISTS `user_verify_credit`;
CREATE TABLE `user_verify_credit` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `id_number` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `credit` int(11) NOT NULL DEFAULT '100' COMMENT '信用',
  `user_id` varchar(32) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user_verify_resource
-- ----------------------------
DROP TABLE IF EXISTS `user_verify_resource`;
CREATE TABLE `user_verify_resource` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_verify_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `url` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片的前缀url',
  `resource_key` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片的键值',
  `position` int(11) DEFAULT '0' COMMENT '图片的位置，位置为1的就是主图',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE,
  KEY `idx_userVerifyId` (`user_verify_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户提交认证资源表，如一些图片或文件';

-- ----------------------------
-- Table structure for user_watch
-- ----------------------------
DROP TABLE IF EXISTS `user_watch`;
CREATE TABLE `user_watch` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `from_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发起人',
  `to_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关注对象',
  `watch_at` datetime DEFAULT NULL COMMENT '关注时间',
  `wach_type` tinyint(4) DEFAULT '0' COMMENT '类型：\r\n            1：主动发起\r\n            其他待定',
  `is_watch` bit(1) DEFAULT b'0' COMMENT '是否是关注（取消关注时更改此字段）',
  `relatable_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联主键，没有就是0.',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_from` (`from_user_id`) USING BTREE,
  KEY `idx_to` (`to_user_id`) USING BTREE,
  KEY `idx_type` (`wach_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户关注表';

-- ----------------------------
-- Table structure for value
-- ----------------------------
DROP TABLE IF EXISTS `value`;
CREATE TABLE `value` (
  `id` varchar(32) NOT NULL,
  `merchant_id` varchar(255) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wish
-- ----------------------------
DROP TABLE IF EXISTS `wish`;
CREATE TABLE `wish` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `title` varchar(300) DEFAULT NULL COMMENT '主题',
  `wish_label` varchar(2000) DEFAULT NULL COMMENT '标签，逗号分隔',
  `amount` bigint(20) DEFAULT '0' COMMENT '希望筹集的金额',
  `current_amount` bigint(20) DEFAULT '0' COMMENT '当前筹集数',
  `current_apply_amount` bigint(20) DEFAULT '0' COMMENT '当前已使用金额',
  `expired_at` datetime DEFAULT NULL COMMENT '截至时间',
  `referee_ids` varchar(2000) DEFAULT NULL COMMENT '推荐人，逗号分隔',
  `referee_count` int(11) DEFAULT '0' COMMENT '推荐人数量',
  `referee_accept_count` int(11) DEFAULT '0' COMMENT '同意推荐数量',
  `referee_refuse_count` int(11) DEFAULT '0' COMMENT '拒绝推荐数量',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `wish_images_url` varchar(2000) DEFAULT NULL COMMENT '图片',
  `wish_images_two_url` varchar(2000) DEFAULT NULL COMMENT '图片',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            1：已发布\r\n            2：已取消\r\n            3：已关闭，即推荐人数不足50%\r\n            4：推荐成功\r\n            5：已失败，即筹款目标未达成\r\n            6：筹集目标达成，即心愿发起成功\r\n            7：已完成，即金额使用完毕',
  `lon` decimal(10,6) DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `is_lock` bit(1) DEFAULT b'1' COMMENT '是否封禁',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`),
  KEY `idx_lon_lat` (`lon`,`lat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心愿表';

-- ----------------------------
-- Table structure for wish_apply
-- ----------------------------
DROP TABLE IF EXISTS `wish_apply`;
CREATE TABLE `wish_apply` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `wish_id` varchar(32) DEFAULT NULL COMMENT '主表ID',
  `bank_id` varchar(32) DEFAULT NULL COMMENT '银行信息表id',
  `amount` bigint(20) DEFAULT '0' COMMENT '申请金额',
  `balance` bigint(20) DEFAULT '0' COMMENT '心愿余额',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `reason` varchar(2000) DEFAULT NULL COMMENT '失败原因',
  `pic` varchar(2000) DEFAULT NULL COMMENT '凭据',
  `apply_type` tinyint(4) DEFAULT '0' COMMENT '使用类型：\r\n            0：提现。\r\n            1：给平台网友。\r\n            2：给第三方。',
  `trading_type` tinyint(10) DEFAULT '0' COMMENT '使用类型：\r\n            0：待交易。\r\n            1：交易中。\r\n            2：交易成功。\r\n            3：交易失败。',
  `apply_info` varchar(2000) DEFAULT NULL COMMENT '使用信息，如本平台的就填网号，如其他，以json形式填',
  `is_pass` tinyint(4) DEFAULT '2' COMMENT '是否通过状态：\r\n            0：未通过\r\n            1：已通过\r\n            2：待通过',
  `manager_count` int(11) DEFAULT '0' COMMENT '监管者数量',
  `opreate_manager_count` int(11) DEFAULT '0' COMMENT '已处理过的管理员数量，不管拒绝还是同意，都算是已处理',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心愿使用表';

-- ----------------------------
-- Table structure for wish_authorize
-- ----------------------------
DROP TABLE IF EXISTS `wish_authorize`;
CREATE TABLE `wish_authorize` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '监管人Id',
  `wish_apply_id` varchar(32) DEFAULT NULL COMMENT '使用表id',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：\r\n            0：待批准\r\n            1：批准。\r\n            2：拒绝。',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心愿授权表';

-- ----------------------------
-- Table structure for wish_bank
-- ----------------------------
DROP TABLE IF EXISTS `wish_bank`;
CREATE TABLE `wish_bank` (
  `id` varchar(32) NOT NULL,
  `wish_id` varchar(32) DEFAULT NULL COMMENT '主表ID',
  `user_id` varchar(32) DEFAULT NULL,
  `deposit_bank` varchar(1000) DEFAULT NULL COMMENT '开户银行',
  `account` varchar(30) DEFAULT NULL COMMENT '账号',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系号码',
  `account_name` varchar(1000) DEFAULT NULL COMMENT '账户名称',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='心愿银行信息表';

-- ----------------------------
-- Table structure for wish_group
-- ----------------------------
DROP TABLE IF EXISTS `wish_group`;
CREATE TABLE `wish_group` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL,
  `group_id` bigint(20) DEFAULT NULL COMMENT '群聊组Id',
  `wish_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '心愿表id',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wish_history
-- ----------------------------
DROP TABLE IF EXISTS `wish_history`;
CREATE TABLE `wish_history` (
  `id` varchar(32) NOT NULL,
  `wish_apply_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `status` tinyint(4) DEFAULT '1' COMMENT '使用类型：\r\n            1：待转账。\r\n            2：转账成功。\r\n            3：转账失败。',
  `reason` varchar(2000) DEFAULT NULL COMMENT '失败原因',
  `admin_user_id` varchar(32) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for wish_manager
-- ----------------------------
DROP TABLE IF EXISTS `wish_manager`;
CREATE TABLE `wish_manager` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '授权人',
  `wish_id` varchar(32) DEFAULT NULL COMMENT '心愿表ID',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心愿监管人表，监管者的数量按支持者的总数确定。支持人数10人及以下，1名监管者；11~49人，3名；50人及以上，5名';

-- ----------------------------
-- Table structure for wish_referee
-- ----------------------------
DROP TABLE IF EXISTS `wish_referee`;
CREATE TABLE `wish_referee` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `wish_id` varchar(32) DEFAULT NULL COMMENT '主表ID',
  `status` tinyint(4) DEFAULT '0' COMMENT '推荐状态：\r\n            0：待确认。\r\n            1：同意。\r\n            2：拒绝。\r\n            3：弃权。',
  `description` varchar(2000) DEFAULT NULL COMMENT '推荐意见',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心愿推荐表';

-- ----------------------------
-- Table structure for wish_support
-- ----------------------------
DROP TABLE IF EXISTS `wish_support`;
CREATE TABLE `wish_support` (
  `id` varchar(32) NOT NULL,
  `wish_id` varchar(32) DEFAULT NULL COMMENT '主表ID',
  `user_id` varchar(32) DEFAULT NULL,
  `is_pay` bit(1) DEFAULT b'0' COMMENT '是否支付',
  `amount` bigint(20) DEFAULT '0' COMMENT '支持金额',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心愿支持表';

-- ----------------------------
-- Table structure for worth_click_history
-- ----------------------------
DROP TABLE IF EXISTS `worth_click_history`;
CREATE TABLE `worth_click_history` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '事件id',
  `type_name` varchar(32) DEFAULT NULL COMMENT '事件类型',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Procedure structure for TEST
-- ----------------------------
DROP PROCEDURE IF EXISTS `TEST`;
DELIMITER ;;
CREATE DEFINER=`wz`@`%` PROCEDURE `TEST`()
BEGIN
	DECLARE i INT DEFAULT 0;
	DECLARE counts INT DEFAULT 0;
	DECLARE temp VARCHAR(20) DEFAULT '';
	SELECT COUNT(*) INTO counts FROM user;
	WHILE i<counts DO
		set temp='';
		SELECT mobile INTO temp FROM user LIMIT i,1;
		UPDATE user SET mobile=CONCAT('0086',temp) WHERE mobile=temp;
		set i=i+1;
	END WHILE;
end
;;
DELIMITER ;
