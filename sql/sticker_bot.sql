/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.21 : Database - sticker_bot
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sticker_bot`  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin /*!80016 DEFAULT ENCRYPTION='N' */;

USE `sticker_bot`;

/*Table structure for table `sticker` */

DROP TABLE IF EXISTS `sticker`;

CREATE TABLE `sticker` (
  `sticker_id` bigint NOT NULL AUTO_INCREMENT,
  `set_name` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `emoji` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `is_animated` tinyint(1) DEFAULT NULL,
  `file_unique_id` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `height` int DEFAULT NULL,
  `width` int DEFAULT NULL,
  `file_size` int DEFAULT NULL,
  `localhost_path` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`sticker_id`),
  KEY `set_name_index` (`set_name`),
  KEY `file_unique_id_index` (`file_unique_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26308 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Table structure for table `sticker_set` */

DROP TABLE IF EXISTS `sticker_set`;

CREATE TABLE `sticker_set` (
  `sticker_set_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `title` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `is_animated` tinyint(1) DEFAULT NULL,
  `contains_masks` tinyint(1) DEFAULT NULL,
  `zip_path` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`sticker_set_id`),
  KEY `name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=358 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
