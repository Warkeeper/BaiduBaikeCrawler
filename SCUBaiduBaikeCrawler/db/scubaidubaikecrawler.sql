/*
Navicat MySQL Data Transfer

Source Server         : popo
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : scubaidubaikecrawler

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-01-16 00:42:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for crawlerresult
-- ----------------------------
DROP TABLE IF EXISTS `crawlerresult`;
CREATE TABLE `crawlerresult` (
  `title` mediumtext,
  `url` mediumtext,
  `summary` mediumtext,
  `basicinfo` mediumtext,
  `catalog` mediumtext,
  `context` mediumtext,
  `reference` mediumtext,
  `tags` mediumtext,
  `statics` mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
