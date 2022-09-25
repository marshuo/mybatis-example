SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '博客id',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '博客标题',
  `author` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '博客作者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `views` int(30) NOT NULL COMMENT '浏览量'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES ('6ff6eec242dd4847ad06260c68fa014e', '这是一个博客', 'marshuo', '2022-09-24 21:54:34', 15);
INSERT INTO `blog` VALUES ('00ddfc3337ea49ce98f336183f387938', 'Java很简单', 'marshuo', '2022-09-24 22:34:47', 123);
INSERT INTO `blog` VALUES ('e239620345074b289de40be1fa690525', 'MyBatis很简单', 'marshuo', '2022-09-24 22:34:48', 123);
INSERT INTO `blog` VALUES ('3f12a013276544b79be0f308c9a7ddcd', '微服务很简单', 'marshuo', '2022-09-24 22:34:48', 123);
INSERT INTO `blog` VALUES ('7f1789f83c354ecd8bfa2d2dcc7a02a6', 'Java很简单', '张三', '2022-09-24 22:50:58', 123);
INSERT INTO `blog` VALUES ('9a7a9529d1334abbbc325209e947c6f7', 'MyBatis很简单', '李四', '2022-09-24 22:50:58', 123);
INSERT INTO `blog` VALUES ('0d3b6ba853df4175b7737b372efd5c4d', '微服务很简单', '王五', '2022-09-24 22:50:58', 123);
INSERT INTO `blog` VALUES ('24e9f0cf72194edf99b1abcf37c30a25', '摇摇晃晃', '布尔', '2022-09-24 22:50:58', 123);

-- ----------------------------
-- Table structure for students
-- ----------------------------
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students`  (
  `id` int(10) NOT NULL,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tid` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fktid`(`tid`) USING BTREE,
  CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teachers` (`tid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of students
-- ----------------------------
INSERT INTO `students` VALUES (1, '小红', 1);
INSERT INTO `students` VALUES (2, '小黄', 1);
INSERT INTO `students` VALUES (3, '小黑', 1);
INSERT INTO `students` VALUES (4, '小白', 1);
INSERT INTO `students` VALUES (5, '小紫', 1);

-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers`  (
  `tid` int(10) NOT NULL,
  `tname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`tid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teachers
-- ----------------------------
INSERT INTO `teachers` VALUES (1, '小王老师');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(20) NOT NULL,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '张三斤', 'abc');
INSERT INTO `user` VALUES (2, '李四', '123456');
INSERT INTO `user` VALUES (3, '老王头', '123');
INSERT INTO `user` VALUES (7, '钱七', '123');
INSERT INTO `user` VALUES (8, '孙八', '456');
INSERT INTO `user` VALUES (9, '李大海', 'aacc');

SET FOREIGN_KEY_CHECKS = 1;
