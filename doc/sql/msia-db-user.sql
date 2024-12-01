/*
 Navicat Premium Data Transfer

 Source Server         : localhostDB
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : localhost:3306
 Source Schema         : msia-db-user

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 06/01/2024 17:29:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of roles
-- ----------------------------
BEGIN;
INSERT INTO `roles` VALUES (1, 'user', 0, '2024-01-01 00:00:00', '2024-01-01 00:00:00');
INSERT INTO `roles` VALUES (2, 'admin', 0, '2024-01-01 00:00:00', '2024-01-01 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `roles` varchar(128) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `avatar` varchar(128) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `mq_message`;
CREATE TABLE `mq_message` (
  `message_id` char(32) NOT NULL,
  `content` json,
  `to_exchange` varchar(128) DEFAULT NULL,
  `routing_key` varchar(128) DEFAULT NULL,
  `class_type` varchar(128) DEFAULT NULL,
  `message_status` int(1) DEFAULT '0' COMMENT '0-新建 1-已发送 2-错误抵达 3-已抵达',
  `remark` varchar(128) DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES (1, 'msia_admin', 'msia.admin', 'admin,user', NULL, NULL, NULL, 0, '2024-01-01 00:00:00', '2024-01-01 00:00:00');
INSERT INTO `users` VALUES (2, 'msia_user', 'msia.user', 'user', NULL, NULL, NULL, 0, '2024-01-01 00:00:00', '2024-01-01 00:00:00');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
