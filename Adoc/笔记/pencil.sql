-- ----------------------------
-- Table structure for user
-- ----------------------------
create database springBoot;

use springBoot;

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `salary` decimal(10,0) NOT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `phone` varchar(15) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `password` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `role_id` tinyint(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'zzy', '28000', '0', '15602227266', '120157', '1');
INSERT INTO `user` VALUES ('2', 'zzx', '15000', '0', '13714802768', '120157', '2');
INSERT INTO `user` VALUES ('3', 'zyl', '70000', '0', '18927837656', '120157', '2');
INSERT INTO `user` VALUES ('4', 'zzq', '25000', '0', '17826765739', '120157', '2');
INSERT INTO `user` VALUES ('5', 'zxq', '24000', '0', '13714802788', '120157', '2');
INSERT INTO `user` VALUES ('6', 'jason', '32000', '0', '13714802788', '120157', '2');
INSERT INTO `user` VALUES ('7', 'czy', '22000', '0', '15602228767', '120157', '2');
INSERT INTO `user` VALUES ('8', 'banana', '2000', '0', '16777777777', '120157', '2');
INSERT INTO `user` VALUES ('9', 'apple', '2000', '0', '18888888888', '120157', '2');