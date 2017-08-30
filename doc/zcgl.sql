/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50153
Source Host           : localhost:3306
Source Database       : zcgl2

Target Server Type    : MYSQL
Target Server Version : 50153
File Encoding         : 65001

Date: 2017-08-17 15:30:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for chu_app
-- ----------------------------
DROP TABLE IF EXISTS `chu_app`;
CREATE TABLE `chu_app` (
  `APP_ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_NAME` varchar(60) DEFAULT NULL,
  `CORP_ID` varchar(30) DEFAULT NULL,
  `CORP_NAME` varchar(60) DEFAULT NULL,
  `CORP_ICON` varchar(100) DEFAULT NULL,
  `AGENT_ID` int(3) DEFAULT NULL,
  `SECRET` varchar(100) DEFAULT NULL,
  `TOKEN` varchar(60) DEFAULT NULL,
  `ENCRYPT` int(1) DEFAULT NULL COMMENT '加密（1），明文（0）',
  `AES_KEY` varchar(100) DEFAULT NULL,
  `CALLBACK_URL` varchar(200) DEFAULT NULL,
  `SERVER_URL` varchar(100) DEFAULT NULL,
  `DEPT_ROOT` int(4) DEFAULT NULL,
  `LEVEL` int(1) DEFAULT '0',
  `STOP` int(1) DEFAULT NULL COMMENT '关闭（1），启动（0）',
  PRIMARY KEY (`APP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chu_app
-- ----------------------------
INSERT INTO `chu_app` VALUES ('1', '设备巡检微助手', 'wx2f5f98d168c4b18e', '武汉市第四十九中学', '/media/logo/deefc05c-5d0d-4352-a773-13feff2ce9f8', '26', 'inM-Snm1l47LdKVU0Ub3hiboISWM4btHdLw8rOuLckw', 'gacl', '1', 'n0Ox3od6DAjlwodoyhm63hSbrWWTTTBGbNB8QYxW2G5', 'tide.natapp4.cc', 'http://tide.natapp4.cc/wx', '8', '0', '0');
INSERT INTO `chu_app` VALUES ('17', '设备巡检微助手', 'ww342013a5f3df8c7f', '潮涌信息科技', 'images/Lego_潮涌科技.png', '1000002', 'b-ii7QN-jqkjbCoDT_kmvS7HhI6VCKO6LcmDc9oT5vs', 'gacl', '1', 'n0Ox3od6DAjlwodoyhm63hSbrWWTTTBGbNB8QYxW2G5', 'tide.natapp4.cc', 'http://tide.natapp4.cc/wx', '1', '0', '0');

-- ----------------------------
-- Table structure for chu_backup
-- ----------------------------
DROP TABLE IF EXISTS `chu_backup`;
CREATE TABLE `chu_backup` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `BFSJ` datetime DEFAULT NULL,
  `CZR` varchar(30) DEFAULT NULL,
  `BACKUPSET` varchar(50) DEFAULT NULL,
  `STATUS` int(1) DEFAULT '0' COMMENT '已删除（1）/ 未删除（0）',
  `REMARK` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chu_backup
-- ----------------------------
INSERT INTO `chu_backup` VALUES ('1', '17', '2017-07-31 13:19:28', 'XuQingWei', '潮涌科技_20170731_数据备份', '0', null);

-- ----------------------------
-- Table structure for chu_env
-- ----------------------------
DROP TABLE IF EXISTS `chu_env`;
CREATE TABLE `chu_env` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENV_KEY` varchar(30) DEFAULT NULL,
  `ENV_VALUE` varchar(100) DEFAULT NULL,
  `REMARK` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='系统环境设置表';

-- ----------------------------
-- Records of chu_env
-- ----------------------------
INSERT INTO `chu_env` VALUES ('1', 'RW_EXPIRE_DATE', '7', '任务验收过期预警时间，单位为天');
INSERT INTO `chu_env` VALUES ('7', 'ZC_EXPIRE_DATE', '30', '资产折旧过期预警时间，单位为天');
INSERT INTO `chu_env` VALUES ('8', 'TAG_HQ_ADMIN', '1001', '后勤管理人员的标签编号');

-- ----------------------------
-- Table structure for chu_menu
-- ----------------------------
DROP TABLE IF EXISTS `chu_menu`;
CREATE TABLE `chu_menu` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `URL` varchar(100) NOT NULL,
  `ICON_CLASS` varchar(40) DEFAULT NULL COMMENT '菜单图标',
  `KEY_NAME` varchar(40) NOT NULL COMMENT 'home.js中menu对应的id号',
  `PID` int(5) NOT NULL,
  `MENU_LEVEL` int(5) NOT NULL,
  `FLAG` int(2) DEFAULT '0' COMMENT '为1表示菜单下面有一个分界线',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=506 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chu_menu
-- ----------------------------
INSERT INTO `chu_menu` VALUES ('10', '系统管理', '/console/sys', 'icon-jssm-xtgl', 'xtgl', '0', '1', '0');
INSERT INTO `chu_menu` VALUES ('20', '数据字典', '/console/catalog', 'icon-jssm-sjzd', 'sjzd', '0', '1', '0');
INSERT INTO `chu_menu` VALUES ('30', '固定资产管理', '/console/zcgl', 'icon-jssm-zcgl', 'zcgl', '0', '1', '0');
INSERT INTO `chu_menu` VALUES ('40', '任务管理', '/console/rwgl', 'icon-jssm-rwgl', 'rwgl', '0', '1', '0');
INSERT INTO `chu_menu` VALUES ('50', '资产盘点', '/console/zcpd', 'icon-jssm-zcpd', 'zcpd', '0', '1', '0');
INSERT INTO `chu_menu` VALUES ('101', '用户维护', '/console/user/userListUI.do', 'icon-jssm-yhgl', 'yhwh', '10', '2', '0');
INSERT INTO `chu_menu` VALUES ('102', '角色维护', '/console/role/roleListUI.do', 'icon-jssm-jsgl', 'jswh', '10', '2', '0');
INSERT INTO `chu_menu` VALUES ('103', '系统环境设置', '/console/sys/envListUI.do', 'icon-jssm-xthjsz', 'xthjsz', '10', '2', '0');
INSERT INTO `chu_menu` VALUES ('104', '微信应用管理', '/console/sys/wxapp/wxAppListUI.do', 'icon-jssm-wxapp', 'wxapp', '10', '2', '0');
INSERT INTO `chu_menu` VALUES ('105', '我的备份', '/console/sys/backup/myBackupListUI.do', 'icon-jssm-backup', 'backup', '10', '2', '0');
INSERT INTO `chu_menu` VALUES ('106', '二维码打印', '/console/sys/ewm/printEWMUI.do', 'icon-jssm-ewm', 'ewm', '10', '2', '0');
INSERT INTO `chu_menu` VALUES ('201', '建筑类型管理', '/console/catalog/jzlxgl/jzlxListUI.do', 'icon-jssm-jzlxgl', 'jzlxgl', '20', '2', '0');
INSERT INTO `chu_menu` VALUES ('202', '校区管理', '/console/catalog/xqgl/xqListUI.do', 'icon-jssm-xqgl', 'xqgl', '20', '2', '0');
INSERT INTO `chu_menu` VALUES ('203', '建筑物管理', '/console/catalog/jzwgl/jzwListUI.do', 'icon-jssm-jzwgl', 'jzwgl', '20', '2', '0');
INSERT INTO `chu_menu` VALUES ('204', '房间管理', '/console/catalog/fjgl/fjListUI.do', 'icon-jssm-fjgl', 'fjgl', '20', '2', '0');
INSERT INTO `chu_menu` VALUES ('205', '资产部门管理', '/console/catalog/zcbmgl/zcbmListUI.do', 'icon-jssm-zcbmgl', 'zcbmgl', '20', '2', '0');
INSERT INTO `chu_menu` VALUES ('206', '固定资产类型管理', '/console/catalog/zclxgl/zclxListUI.do', 'icon-jssm-zclxgl', 'zclxgl', '20', '2', '0');
INSERT INTO `chu_menu` VALUES ('207', '固定资产品种管理', '/console/catalog/zcpzgl/zcpzListUI.do', 'icon-jssm-zcpzgl', 'zcpzgl', '20', '2', '0');
INSERT INTO `chu_menu` VALUES ('208', '微信用户管理', '/console/wxgl/wxuser/wxUserListUI.do', 'icon-jssm-wxuser', 'wxuser', '20', '2', '0');
INSERT INTO `chu_menu` VALUES ('301', '固定资产登记', '/console/zcgl/regist/registZCListUI.do', 'icon-jssm-regist', 'regist', '30', '2', '0');
INSERT INTO `chu_menu` VALUES ('303', '固定资产归还审批', '/console/zcgl/revert/revertZCListUI.do', 'icon-jssm-revert', 'revert', '30', '2', '0');
INSERT INTO `chu_menu` VALUES ('304', '固定资产分配', '/console/zcgl/assign/assignZCListUI.do', 'icon-jssm-assign', 'assign', '30', '2', '0');
INSERT INTO `chu_menu` VALUES ('306', '固定资产上交审批', '/console/zcgl/sendback/sendbackZCListUI.do', 'icon-jssm-sendback', 'sendback', '30', '2', '0');
INSERT INTO `chu_menu` VALUES ('307', '固定资产维修审批', '/console/zcgl/wxsq/wxsqListUI.do', 'icon-jssm-wxsq', 'wxsq', '30', '2', '0');
INSERT INTO `chu_menu` VALUES ('308', '固定资产闲置审批', '/console/zcgl/xzsq/xzsqListUI.do', 'icon-jssm-xzsq', 'xzsq', '30', '2', '0');
INSERT INTO `chu_menu` VALUES ('309', '固定资产报废审批', '/console/zcgl/bfsq/bfsqListUI.do', 'icon-jssm-bfsq', 'bfsq', '30', '2', '0');
INSERT INTO `chu_menu` VALUES ('310', '闲置固定资产管理', '/console/zcgl/xzzc/xzzcListUI.do', 'icon-jssm-xzzc', 'xzzc', '30', '2', '0');
INSERT INTO `chu_menu` VALUES ('311', '固定资产领用', '/console/zcgl/leading/leadingZCListUI.do', 'icon-jssm-leading', 'leading', '30', '2', '0');
INSERT INTO `chu_menu` VALUES ('401', '日常巡检任务管理', '/console/rwgl/rcxj/rcxjListUI.do', 'icon-jssm-rcxj', 'rcxj', '40', '2', '0');
INSERT INTO `chu_menu` VALUES ('402', '故障维修任务管理', '/console/rwgl/gzwx/gzwxListUI.do', 'icon-jssm-gzwx', 'gzwx', '40', '2', '0');
INSERT INTO `chu_menu` VALUES ('403', '任务查询', '/console/rwgl/rwcx/rwcxListUI.do', 'icon-jssm-rwcx', 'rwcx', '40', '2', '0');
INSERT INTO `chu_menu` VALUES ('404', '固定资产巡检', '/console/rwgl/zcxj/zcxjListUI.do', 'icon-jssm-zcxj', 'zcxj', '40', '2', '0');
INSERT INTO `chu_menu` VALUES ('405', '固定资产维修', '/console/rwgl/zcwx/zcwxListUI.do', 'icon-jssm-zcwx', 'zcwx', '40', '2', '0');
INSERT INTO `chu_menu` VALUES ('406', '消息通告发布', '/console/rwgl/msg/sendMsgUI.do', 'icon-jssm-msg', 'msg', '40', '2', '0');
INSERT INTO `chu_menu` VALUES ('501', '企业固定资产查询', '/console/zcpd/zccx/ZCListUI.do', 'icon-jssm-qyzccx', 'zccx', '50', '2', '0');
INSERT INTO `chu_menu` VALUES ('502', '部门固定资产查询', '/console/zcpd/zccx/BMZCListUI.do', 'icon-jssm-bmzccx', 'zccx', '50', '2', '0');
INSERT INTO `chu_menu` VALUES ('503', '部门固定资产分类统计', '/console/zcpd/bmzcStat/BMZC_ZCFLStatUI.do', 'icon-jssm-bmzcfl', 'bmzcStat', '50', '2', '0');
INSERT INTO `chu_menu` VALUES ('504', '部门固定资产购置时间统计', '/console/zcpd/bmzcStat/BMZC_GZSJStatUI.do', 'icon-jssm-bmzcgzsj', 'bmzcStat', '50', '2', '0');
INSERT INTO `chu_menu` VALUES ('505', '部门固定资产状态统计', '/console/zcpd/bmzcStat/BMZC_ZTStatUI.do', 'icon-jssm-bmzczt', 'bmzcStat', '50', '2', '0');

-- ----------------------------
-- Table structure for chu_role
-- ----------------------------
DROP TABLE IF EXISTS `chu_role`;
CREATE TABLE `chu_role` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `IS_WX` int(1) DEFAULT NULL COMMENT '1（微信端角色），0（管理控制台端角色）',
  `tybz` int(1) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chu_role
-- ----------------------------
INSERT INTO `chu_role` VALUES ('1', '超级管理员', '0', '0');
INSERT INTO `chu_role` VALUES ('25', '应用管理员', null, '0');
INSERT INTO `chu_role` VALUES ('26', '系统管理员', null, '0');
INSERT INTO `chu_role` VALUES ('27', '开发人员', null, '0');
INSERT INTO `chu_role` VALUES ('28', '后勤管理人员', null, '0');
INSERT INTO `chu_role` VALUES ('29', '部门资产管理员', null, '0');
INSERT INTO `chu_role` VALUES ('30', '巡检人员', null, '0');
INSERT INTO `chu_role` VALUES ('31', '维修人员', null, '0');
INSERT INTO `chu_role` VALUES ('32', '普通用户', null, '0');

-- ----------------------------
-- Table structure for chu_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `chu_role_menu`;
CREATE TABLE `chu_role_menu` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` int(5) DEFAULT NULL,
  `MENU_ID` int(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=611 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chu_role_menu
-- ----------------------------
INSERT INTO `chu_role_menu` VALUES ('23', '26', '10');
INSERT INTO `chu_role_menu` VALUES ('24', '26', '101');
INSERT INTO `chu_role_menu` VALUES ('25', '26', '102');
INSERT INTO `chu_role_menu` VALUES ('26', '26', '103');
INSERT INTO `chu_role_menu` VALUES ('397', '28', '301');
INSERT INTO `chu_role_menu` VALUES ('398', '28', '303');
INSERT INTO `chu_role_menu` VALUES ('399', '28', '307');
INSERT INTO `chu_role_menu` VALUES ('400', '28', '308');
INSERT INTO `chu_role_menu` VALUES ('401', '28', '309');
INSERT INTO `chu_role_menu` VALUES ('402', '28', '310');
INSERT INTO `chu_role_menu` VALUES ('403', '28', '401');
INSERT INTO `chu_role_menu` VALUES ('404', '28', '402');
INSERT INTO `chu_role_menu` VALUES ('405', '28', '403');
INSERT INTO `chu_role_menu` VALUES ('406', '28', '501');
INSERT INTO `chu_role_menu` VALUES ('407', '28', '503');
INSERT INTO `chu_role_menu` VALUES ('408', '28', '504');
INSERT INTO `chu_role_menu` VALUES ('409', '28', '505');
INSERT INTO `chu_role_menu` VALUES ('410', '28', '30');
INSERT INTO `chu_role_menu` VALUES ('411', '28', '40');
INSERT INTO `chu_role_menu` VALUES ('412', '28', '50');
INSERT INTO `chu_role_menu` VALUES ('418', '29', '304');
INSERT INTO `chu_role_menu` VALUES ('419', '29', '306');
INSERT INTO `chu_role_menu` VALUES ('420', '29', '502');
INSERT INTO `chu_role_menu` VALUES ('421', '29', '503');
INSERT INTO `chu_role_menu` VALUES ('422', '29', '504');
INSERT INTO `chu_role_menu` VALUES ('423', '29', '505');
INSERT INTO `chu_role_menu` VALUES ('424', '29', '30');
INSERT INTO `chu_role_menu` VALUES ('425', '29', '50');
INSERT INTO `chu_role_menu` VALUES ('426', '30', '403');
INSERT INTO `chu_role_menu` VALUES ('427', '30', '404');
INSERT INTO `chu_role_menu` VALUES ('428', '30', '40');
INSERT INTO `chu_role_menu` VALUES ('429', '31', '403');
INSERT INTO `chu_role_menu` VALUES ('430', '31', '405');
INSERT INTO `chu_role_menu` VALUES ('431', '31', '40');
INSERT INTO `chu_role_menu` VALUES ('466', '1', '10');
INSERT INTO `chu_role_menu` VALUES ('467', '1', '101');
INSERT INTO `chu_role_menu` VALUES ('468', '1', '102');
INSERT INTO `chu_role_menu` VALUES ('469', '1', '103');
INSERT INTO `chu_role_menu` VALUES ('470', '1', '104');
INSERT INTO `chu_role_menu` VALUES ('471', '25', '20');
INSERT INTO `chu_role_menu` VALUES ('472', '25', '201');
INSERT INTO `chu_role_menu` VALUES ('473', '25', '202');
INSERT INTO `chu_role_menu` VALUES ('474', '25', '203');
INSERT INTO `chu_role_menu` VALUES ('475', '25', '204');
INSERT INTO `chu_role_menu` VALUES ('476', '25', '205');
INSERT INTO `chu_role_menu` VALUES ('477', '25', '206');
INSERT INTO `chu_role_menu` VALUES ('478', '25', '207');
INSERT INTO `chu_role_menu` VALUES ('576', '27', '105');
INSERT INTO `chu_role_menu` VALUES ('577', '27', '106');
INSERT INTO `chu_role_menu` VALUES ('578', '27', '20');
INSERT INTO `chu_role_menu` VALUES ('579', '27', '201');
INSERT INTO `chu_role_menu` VALUES ('580', '27', '202');
INSERT INTO `chu_role_menu` VALUES ('581', '27', '203');
INSERT INTO `chu_role_menu` VALUES ('582', '27', '204');
INSERT INTO `chu_role_menu` VALUES ('583', '27', '205');
INSERT INTO `chu_role_menu` VALUES ('584', '27', '206');
INSERT INTO `chu_role_menu` VALUES ('585', '27', '207');
INSERT INTO `chu_role_menu` VALUES ('586', '27', '208');
INSERT INTO `chu_role_menu` VALUES ('587', '27', '30');
INSERT INTO `chu_role_menu` VALUES ('588', '27', '301');
INSERT INTO `chu_role_menu` VALUES ('589', '27', '303');
INSERT INTO `chu_role_menu` VALUES ('590', '27', '304');
INSERT INTO `chu_role_menu` VALUES ('591', '27', '306');
INSERT INTO `chu_role_menu` VALUES ('592', '27', '307');
INSERT INTO `chu_role_menu` VALUES ('593', '27', '308');
INSERT INTO `chu_role_menu` VALUES ('594', '27', '309');
INSERT INTO `chu_role_menu` VALUES ('595', '27', '310');
INSERT INTO `chu_role_menu` VALUES ('596', '27', '311');
INSERT INTO `chu_role_menu` VALUES ('597', '27', '40');
INSERT INTO `chu_role_menu` VALUES ('598', '27', '401');
INSERT INTO `chu_role_menu` VALUES ('599', '27', '402');
INSERT INTO `chu_role_menu` VALUES ('600', '27', '403');
INSERT INTO `chu_role_menu` VALUES ('601', '27', '404');
INSERT INTO `chu_role_menu` VALUES ('602', '27', '405');
INSERT INTO `chu_role_menu` VALUES ('603', '27', '406');
INSERT INTO `chu_role_menu` VALUES ('604', '27', '50');
INSERT INTO `chu_role_menu` VALUES ('605', '27', '501');
INSERT INTO `chu_role_menu` VALUES ('606', '27', '502');
INSERT INTO `chu_role_menu` VALUES ('607', '27', '503');
INSERT INTO `chu_role_menu` VALUES ('608', '27', '504');
INSERT INTO `chu_role_menu` VALUES ('609', '27', '505');
INSERT INTO `chu_role_menu` VALUES ('610', '27', '10');

-- ----------------------------
-- Table structure for chu_tag
-- ----------------------------
DROP TABLE IF EXISTS `chu_tag`;
CREATE TABLE `chu_tag` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `TAG_NO` int(11) DEFAULT NULL COMMENT '用户应用标签号.',
  `TAG_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8 COMMENT='应用标签表';

-- ----------------------------
-- Records of chu_tag
-- ----------------------------
INSERT INTO `chu_tag` VALUES ('151', '17', '1', '超级管理员');
INSERT INTO `chu_tag` VALUES ('152', '17', '2', '维修组');
INSERT INTO `chu_tag` VALUES ('153', '17', '3', '巡检组');
INSERT INTO `chu_tag` VALUES ('154', '17', '4', '资产管理员组');
INSERT INTO `chu_tag` VALUES ('155', '17', '5', '后勤管理人员组');
INSERT INTO `chu_tag` VALUES ('171', '1', '1', '使用人组');
INSERT INTO `chu_tag` VALUES ('172', '1', '2', '后勤管理人员组');
INSERT INTO `chu_tag` VALUES ('173', '1', '5', '维修组');
INSERT INTO `chu_tag` VALUES ('174', '1', '4', '巡检组');
INSERT INTO `chu_tag` VALUES ('175', '1', '3', '部门资产管理员组');

-- ----------------------------
-- Table structure for chu_user
-- ----------------------------
DROP TABLE IF EXISTS `chu_user`;
CREATE TABLE `chu_user` (
  `UID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `UPW` varchar(100) NOT NULL,
  `UNAME` varchar(100) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `SALT` varchar(6) NOT NULL,
  `ROLE_ID` int(5) DEFAULT NULL,
  `APP_ID` int(11) DEFAULT NULL,
  `WX_USERNAME` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`UID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='登录管理控制台的用户表。';

-- ----------------------------
-- Records of chu_user
-- ----------------------------
INSERT INTO `chu_user` VALUES ('2', 'E416FFB7A44E5D41660CD3064D3C9F4E', 'chujie', '褚杰', '666876', '1', null, null);
INSERT INTO `chu_user` VALUES ('6', 'F212BEBCE9BD99F7E9600C9B3921E8F4', 'admin49', '四十九中', '126010', '27', '1', 'XuQingWei');
INSERT INTO `chu_user` VALUES ('7', 'ACAF2DA08D7EE91A582F15DBB1B063C1', 'dev', '开发人员', '172543', '27', '17', 'shangwei');
INSERT INTO `chu_user` VALUES ('8', 'ACAF2DA08D7EE91A582F15DBB1B063C1', 'user', '普通用户', '172543', '32', null, null);

-- ----------------------------
-- Table structure for chu_wxdept
-- ----------------------------
DROP TABLE IF EXISTS `chu_wxdept`;
CREATE TABLE `chu_wxdept` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `DEPT_NO` int(11) DEFAULT NULL,
  `DEPT_NAME` varchar(100) DEFAULT NULL,
  `DEPT_PNO` int(11) DEFAULT NULL,
  `DEPT_ORDER` int(6) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=210 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chu_wxdept
-- ----------------------------
INSERT INTO `chu_wxdept` VALUES ('68', '17', '2', '计算机学院', '1', '100000000');
INSERT INTO `chu_wxdept` VALUES ('69', '17', '3', '文学院', '1', '99999000');
INSERT INTO `chu_wxdept` VALUES ('70', '17', '4', '测试组1', '2', '100000000');
INSERT INTO `chu_wxdept` VALUES ('71', '17', '5', '测试组2', '2', '99999000');
INSERT INTO `chu_wxdept` VALUES ('72', '17', '6', '测试组3', '3', '100000000');
INSERT INTO `chu_wxdept` VALUES ('73', '17', '7', '测试组4', '3', '99999000');
INSERT INTO `chu_wxdept` VALUES ('142', '1', '78', '2016级', '27', '2147483047');
INSERT INTO `chu_wxdept` VALUES ('143', '1', '64', '2016级', '61', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('144', '1', '63', '2015级', '61', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('145', '1', '26', '总务处', '17', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('146', '1', '50', '2014级', '49', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('147', '1', '41', '2016级', '38', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('148', '1', '20', '教师组', '8', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('149', '1', '72', '2016级', '42', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('150', '1', '61', '历史组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('151', '1', '69', '体育组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('152', '1', '66', '2014级', '65', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('153', '1', '70', '艺术组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('154', '1', '73', '2014级', '19', '2147483447');
INSERT INTO `chu_wxdept` VALUES ('155', '1', '36', '2015级', '34', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('156', '1', '17', '行政组', '8', '2147483047');
INSERT INTO `chu_wxdept` VALUES ('157', '1', '18', '年级管理组', '8', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('158', '1', '16', '校长', '8', '2147483247');
INSERT INTO `chu_wxdept` VALUES ('159', '1', '34', '语文组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('160', '1', '23', '教导处', '17', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('161', '1', '8', '教师(禁止删除)', '6', '2147483247');
INSERT INTO `chu_wxdept` VALUES ('162', '1', '45', '物理组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('163', '1', '77', '2015级', '27', '2147483247');
INSERT INTO `chu_wxdept` VALUES ('164', '1', '52', '2016级', '49', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('165', '1', '53', '生物组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('166', '1', '44', '2015级', '42', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('167', '1', '51', '2015级', '49', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('168', '1', '57', '政治组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('169', '1', '49', '化学组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('170', '1', '55', '2015级', '53', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('171', '1', '46', '2014级', '45', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('172', '1', '56', '2016级', '53', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('173', '1', '35', '2014级', '34', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('174', '1', '25', '电教处', '17', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('175', '1', '62', '2014级', '61', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('176', '1', '65', '地理组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('177', '1', '60', '2016级', '57', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('178', '1', '22', '学工处', '17', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('179', '1', '12', '测试', '8', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('180', '1', '74', '2015级', '19', '2147483247');
INSERT INTO `chu_wxdept` VALUES ('181', '1', '15', '书记', '8', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('182', '1', '39', '2014级', '38', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('183', '1', '21', '党政办', '17', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('184', '1', '19', '年级书记', '18', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('185', '1', '40', '2015级', '38', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('186', '1', '38', '数学组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('187', '1', '24', '教科室', '17', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('188', '1', '27', '年级主任', '18', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('189', '1', '37', '2016级', '34', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('190', '1', '75', '2016级', '19', '2147483047');
INSERT INTO `chu_wxdept` VALUES ('191', '1', '42', '英语组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('192', '1', '43', '2014级', '42', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('193', '1', '58', '2014级', '57', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('194', '1', '181', '美术组', '20', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('195', '1', '68', '2016级', '65', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('196', '1', '67', '2015级', '65', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('197', '1', '54', '2014级', '53', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('198', '1', '59', '2015级', '57', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('199', '1', '48', '2016级', '45', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('200', '1', '47', '2015级', '45', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('201', '1', '76', '2014级', '27', '2147483447');
INSERT INTO `chu_wxdept` VALUES ('202', '1', '183', '班主任', '8', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('203', '1', '184', '教研组', '8', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('204', '1', '185', '备课组长', '8', '2147483647');
INSERT INTO `chu_wxdept` VALUES ('205', '1', '391', '2014级班主任', '183', '2147483646');
INSERT INTO `chu_wxdept` VALUES ('206', '1', '392', '2015级班主任', '183', '2147483645');
INSERT INTO `chu_wxdept` VALUES ('207', '1', '393', '2016级班主任', '183', '2147483644');
INSERT INTO `chu_wxdept` VALUES ('208', '1', '394', '德育管理组', '183', '2147483643');
INSERT INTO `chu_wxdept` VALUES ('209', '1', '395', '2014级教师', '18', '2147483642');

-- ----------------------------
-- Table structure for chu_wxuser
-- ----------------------------
DROP TABLE IF EXISTS `chu_wxuser`;
CREATE TABLE `chu_wxuser` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERID` varchar(30) DEFAULT NULL,
  `NAME` varchar(60) DEFAULT NULL,
  `DEPARTMENT` varchar(60) DEFAULT NULL,
  `POSITION` varchar(60) DEFAULT NULL,
  `MOBILE` varchar(20) DEFAULT NULL,
  `GENDER` int(1) DEFAULT NULL COMMENT '男("1"), 女("2")',
  `EMAIL` varchar(30) DEFAULT NULL,
  `WEIXINID` varchar(30) DEFAULT NULL,
  `AVATAR` varchar(100) DEFAULT NULL,
  `STATUS` int(1) DEFAULT NULL COMMENT '全部成员("0"), 已关注("1"), 禁用("2"), 未关注("4")',
  `EXTATTR` varchar(200) DEFAULT NULL,
  `APP_ID` int(11) DEFAULT NULL,
  `TAG_NO` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=573 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chu_wxuser
-- ----------------------------
INSERT INTO `chu_wxuser` VALUES ('217', 'yangjing', '杨靖', '[6]', '', '15071409747', '1', '', null, '', '1', '{\"attrs\":[]}', '17', '[3,4]');
INSERT INTO `chu_wxuser` VALUES ('218', 'shixiaoping', '石小平', '[2]', '', '13016429139', '1', '', null, 'http://p.qlogo.cn/bizmail/V1ia6KJr9jY6DZQLEvn1eibvgDUx5LTgGL4pDPQ4rMmld099D5RmkpicQ/0', '1', '{\"attrs\":[]}', '17', '[3]');
INSERT INTO `chu_wxuser` VALUES ('219', 'shangwei', '尚尉', '[5]', '', '15172382636', '1', '', null, 'http://p.qlogo.cn/bizmail/ibh6Q0tGtc4fefm5nav5IakWSEQ7G6tMqkCPMqsKzQu6zxCp91ibNYTA/0', '1', '{\"attrs\":[]}', '17', '[1,3,5]');
INSERT INTO `chu_wxuser` VALUES ('220', 'daishaopeng', '戴绍鹏', '[5]', '', '18672319186', '1', '', null, 'http://p.qlogo.cn/bizmail/9cljnn1iaUR5rKJsMkVicZWY3yCp0QU8khlW3qnAHpPpeT3WjJP1nNyA/0', '1', '{\"attrs\":[]}', '17', '');
INSERT INTO `chu_wxuser` VALUES ('221', 'luozhong', '罗忠', '[7]', '', '18963959635', '1', '', null, 'http://wx.qlogo.cn/mmhead/rKBCaRcVshNZNHWCpySePTPKLNCrMJsM5fBXSnRLrnO2h693qwhXEg/0', '1', '{\"attrs\":[]}', '17', '');
INSERT INTO `chu_wxuser` VALUES ('222', 'WenChen', '文臣', '[7]', '', '15071451506', '1', '', null, 'http://p.qlogo.cn/bizmail/ibCficFILTUHQvW80cV4PA08MG3GRIicRibIuWYibwibT06LmNzR48mYp2pA/0', '1', '{\"attrs\":[]}', '17', '[4]');
INSERT INTO `chu_wxuser` VALUES ('223', 'fuzuo', '付卓', '[5]', '', '17671877723', '1', '', null, 'http://p.qlogo.cn/bizmail/7SSHMDq8o7hDToGk7fhp7ZlmFw3TES373xVviaiaPiak3LiatLK6J9j86w/0', '1', '{\"attrs\":[]}', '17', '[5]');
INSERT INTO `chu_wxuser` VALUES ('224', 'chentianyu', '陈天宇', '[4]', '', '15172532627', '1', '656308023@qq.com', null, 'http://p.qlogo.cn/bizmail/Q3EBrNvLQu0Tr8RbjpeWmalz31aQ4TtRL76iarbQZuS1tBSEhJY5J5A/0', '1', '{\"attrs\":[]}', '17', '[3]');
INSERT INTO `chu_wxuser` VALUES ('225', 'baiweibing', '白维兵', '[4]', '', '15071453956', '1', '', null, 'http://wx.qlogo.cn/mmhead/Q3auHgzwzM43bjkWcg7sv1g9EU9yjPHRmEEINaRwmriaUbxKtfeichAQ/0', '1', '{\"attrs\":[]}', '17', '[2]');
INSERT INTO `chu_wxuser` VALUES ('226', 'XuQingWei', '许庆炜', '[3]', '', '18971406369', '1', '25914522@qq.com', null, 'http://p.qlogo.cn/bizmail/Gauh9sASHcpKnYPcicBBia7Ndrtqlx41SS0cKZEFcuAAq2sibX8oTd6lg/0', '1', '{\"attrs\":[]}', '17', '[2,3,5]');
INSERT INTO `chu_wxuser` VALUES ('227', 'lirunfang', '李润方', '[6]', '', '15071345174', '1', '', null, 'http://p.qlogo.cn/bizmail/U3Rc4Jk3yRlvf5lvQtmibic3caic5w6Q4ib8DUiaoHmJlfwoEIafJJcXfUA/0', '1', '{\"attrs\":[]}', '17', '[2,5]');
INSERT INTO `chu_wxuser` VALUES ('228', 'pengxiao', '彭潇', '[6]', '', '15623327262', '1', '', null, 'http://p.qlogo.cn/bizmail/39Fb5dOg19BhjUyx21tLffOTbOJFqxahWhqkB9ppn2aPqRmicVshicjw/0', '1', '{\"attrs\":[]}', '17', '[4,5]');
INSERT INTO `chu_wxuser` VALUES ('401', 'campus_2522325', '何丙杰', '[47]', '', '15629188009', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('402', 'campus_2547331', '张志', '[392]', '', '15871482108', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiav9r6cv6UBHQTQnvaXyWYtxYaHeocp2Xewsuz93GyicV0Q/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('403', 'campus_2547330', '张国圣', '[393]', '', '13667285819', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybQGxyA1rAR6G4nu0B9bfeNQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('404', 'campus_2547333', '郭浩', '[395]', '', '13628675673', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('405', 'campus_2547332', '聂勉', '[392]', '', '15927461062', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVyboiaX4860aJgAed8X278roYw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('406', 'campus_2547335', '李俊舫', '[395]', '', '13006131637', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('407', 'campus_2547334', '汪涛', '[395]', '', '15807189012', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauCdKxcJGfmd4VOzfMm1foSeXQzRtIVNbPnm11iaibr2wsw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('408', 'campus_2547336', '胡贝', '[395]', '', '18062564300', null, '', null, '', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('409', 'campus_2547337', '罗义生', '[393]', '', '18971495932', '1', '', null, '', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('410', 'campus_2547338', '肖建锋', '[24]', '', '18674092589', null, '', null, 'http://p.qlogo.cn/bizmail/JZYIdgUwaDrnXAkwEkA7k2G5ib5T84fnibgPZdCV84B4Wyoxa1gkGLOw/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('411', 'campus_2547339', '齐琳', '[393]', '', '13477067953', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVyb0jUJIEZTXOL48K9yBibG8Fw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('412', 'campus_2547238', '郑中海', '[15]', '', '13986246380', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiataicQHiccvaQebfybPSr7U8QCsHRSIMfIvFWENGLhfnRUg/', '1', '{\"attrs\":[]}', '1', '[2]');
INSERT INTO `chu_wxuser` VALUES ('413', 'campus_2547239', '吕向东', '[16]', '', '13907198122', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('414', 'campus_2522311', '冯艳华', '[40]', '', '15337265652', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiav9r6cv6UBHQTQnvaXyWYtxHeqmDLq5KKx9DyEdLGjU9g/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('415', 'shangwei', '尚尉', '[12]', '', '15172382636', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiavZnLvSiaabwGG0QhmGAwl7LkXyVmh3CW5y2nDDL07acKw/', '1', '{\"attrs\":[]}', '1', '[4]');
INSERT INTO `chu_wxuser` VALUES ('416', 'campus_2689131', '郭少畅', '[12]', '', '13995566063', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiautVoCIzXjqPJt3IkFKILD6ZiaibtGRpqiaz7R0ibS60zASOQ/', '1', '{\"attrs\":[]}', '1', '[2]');
INSERT INTO `chu_wxuser` VALUES ('417', 'campus_2522315', '罗俊', '[395]', '', '18086668339', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rglAYqXdajIonRiaTN60zJnDA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('418', 'campus_2547320', '程凤', '[44]', '', '13638680215', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgoblGkTmF6eiaGV5uHYVBUwg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('419', 'campus_2547324', '蒋婷', '[395]', '', '13659869721', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rg7O0nPkS3McjSibXPTqib7SrA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('420', 'campus_2547323', '黄欢', '[395]', '', '15972008210', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('421', 'campus_2749058', '许庆炜', '[12]', '', '18971406369', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauCHymVxsnwKN6UNahREu0Nzwe2ZhPIc0mE0VehNk9pFg/', '1', '{\"attrs\":[]}', '1', '[3]');
INSERT INTO `chu_wxuser` VALUES ('422', 'campus_2547322', '李向华', '[395]', '', '13971464501', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('423', 'campus_2547321', '胡世平', '[44]', '', '18672965469', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiavFubnGZZIlcedibGng3mzslQByvaFxib07xEiaYhPGibtpicg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('424', 'campus_2547327', '孟旭生', '[395]', '', '18995632691', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('425', 'campus_2547328', '王靓', '[395]', '', '13627138082', null, '', null, 'http://p.qlogo.cn/bizmail/yh93DXculBgtz37oR3EkSdqUTR3Uuqaq3v4DWr66uom7Y2WMU9fHzA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('426', 'campus_2708836', '胡正茂', '[16]', '', '18986084946', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVyb6mUWn9xanYibG0elib9KOK8Q/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('427', 'campus_2547325', '陈光学', '[395]', '', '18971185092', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('428', 'campus_2547326', '欧时才', '[395]', '', '13339986195', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauCdKxcJGfmd4VOzfMm1foSwYIzT4byXvP5sM4cIJqnIA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('429', 'campus_2547329', '蔡红斌', '[48]', '', '15902761942', '1', '', null, 'http://p.qlogo.cn/bizmail/XkWOQq593tJ5rpkSeI0BJ98c9xwia8DvHAFE1CPH0uK25ia5x3GBYvvg/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('430', 'campus_2497258', '程晓', '[12]', '', '13554013853', '2', '', null, 'http://p.qlogo.cn/bizmail/Jd5iaSiaKykxicaIEhLEDaLciciaglRKEZIR14Nwno1obXbSCy0RR6LpcwA/0', '1', '{\"attrs\":[]}', '1', '[2]');
INSERT INTO `chu_wxuser` VALUES ('431', 'campus_3194743', '喻军', '[12]', '', '13277083065', null, '', null, 'http://p.qlogo.cn/bizmail/uS7KqgHIwBsU3bWs0fcibZ93ZAJORfjmSpP1RAp3nk9iakubBMJGYczA/', '1', '{\"attrs\":[]}', '1', '[5]');
INSERT INTO `chu_wxuser` VALUES ('432', 'campus_2698331', '陈雨', '[12]', '', '15102733458', '2', '', null, 'http://p.qlogo.cn/bizmail/X7gOxiap6RCb2Cp7CTwOLnPqEuwkRTzVxt4lr6vYFMgZrVlv2ic9CwnA/0', '1', '{\"attrs\":[]}', '1', '[2]');
INSERT INTO `chu_wxuser` VALUES ('433', 'campus_2547355', '何海波', '[395]', '', '13407144748', '1', '', null, 'http://p.qlogo.cn/bizmail/FBJmOdejlfG9QppzakXfGWmDgenU9lOLJGyQZzmVycXVWYO4GX0UWw/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('434', 'campus_2547354', '刘娟', '[395]', '', '15342776115', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybrsTViav6HHBCgtYf8z9ReLw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('435', 'campus_2547357', '贺振洲', '[60]', '', '13667175855', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('436', 'campus_2547356', '刘国新', '[395]', '', '13628651326', null, '', null, '', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('437', 'campus_2547351', '罗艳', '[392]', '', '13507121677', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybR5JXopPdGReQS5j6Ur2EQQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('438', 'campus_2547350', '李烈俊', '[392]', '', '13507132733', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybgwbNIabvYlakWGdO49TTZw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('439', 'campus_2547353', '王家太', '[395]', '', '18971222692', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiau6Fxus3rE4RvhFx4xGUlpJPB19n1picttCwicqoicicsGMibw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('440', 'campus_2547352', '李葳', '[55]', '', '13871136173', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiau6Fxus3rE4RvhFx4xGUlpJNicNBGhJ4gZ9XYVHKCFmLQA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('441', 'campus_2547298', '陈孝武', '[392]', '', '15327300699', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybh8wqyG58JLgVcMWq3E178w/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('442', 'campus_2547297', '杜春明', '[40]', '', '18986286120', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('443', 'campus_2547296', '唐宗保', '[40]', '', '13986216656', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('444', 'campus_2547295', '魏志平', '[392]', '', '15327288075', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybkGVic8SXp6ZryYoxTJHNtng/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('445', 'campus_2547358', '胡慎友', '[392]', '', '13871568052', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybiaYJwRP9IseicGTGSxibWl6iag/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('446', 'campus_2547359', '段银枝', '[59]', '', '15802785776', null, '', null, 'http://p.qlogo.cn/bizmail/cDbkMRN6S9KWKUoC4v4kWs8Gh6areH6SicI00MxpQSonn108OtQicnyA/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('447', 'campus_2547299', '冯发红', '[40]', '', '13297006668', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('448', 'campus_2547517', '龚武', '[25]', '', '18971499069', null, '', null, 'http://p.qlogo.cn/bizmail/AfQGsropdFVxgxAzvJRrRGLrBmiabb4x2U2jhWqwo2sXiaeaLbibtkYNw/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('449', 'campus_2547290', '李清华', '[41]', '', '13429880887', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('450', 'campus_2547516', '郑华强', '[25]', '', '15629027553', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgPQIjhwNiaLx6iaUKOl1kvu0Q/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('451', 'campus_2547518', '朱光春', '[25]', '', '15827289536', null, '', null, 'http://p.qlogo.cn/bizmail/qwVRJp5nHjllI2EibEWQELKKTgD80akVySVEQvOZTjdsK4cfcqjErVw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('452', 'campus_2522342', '吕中喜', '[395]', '', '15717193053', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiavfibRoe7u7uCMvFBp7gUJsWfKVZE78sqXAeauX6QMNxwg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('453', 'campus_2547294', '岳露丽', '[41]', '', '13437131535', null, '', null, 'http://p.qlogo.cn/bizmail/Vx2rGCpDqImYoxjqbjNBQCT2zuohCFDyypxzic2siaDQVcBFWibRXlSZA/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('454', 'campus_2547293', '贺勇', '[41]', '', '13294199772', '1', '', null, 'http://p.qlogo.cn/bizmail/uDP12dBkKLpXic37ec9mm1eTcfjPpHK4T5qJY0vJq2SFkdGDxcC8fuA/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('455', 'campus_2547292', '李萌', '[393]', '', '15807130675', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybIz8rrHQpRJKSoZcxqM0cXg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('456', 'campus_2547291', '唐和海', '[41]', '', '15827307256', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('457', 'campus_2522335', '刘海艳', '[56]', '', '18971093726', null, '', null, 'http://p.qlogo.cn/bizmail/Q0Bx6apPIKQ6oox1wiaKDPFdRTajUNXefhKY4icC72ACC7oSgbkMFg2A/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('458', 'campus_2547346', '王玲', '[395]', '', '13476012203', '2', '', null, 'http://p.qlogo.cn/bizmail/cvB7XyGfTGsulyEUWnd0ibHCRVPw1nXSXV434ENYRaibkNGI6Hwshb7Q/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('459', 'campus_2547345', '陈鸣', '[395]', '', '18672760982', '2', '', null, 'http://p.qlogo.cn/bizmail/EibEeh9CsPBEbDd3fdAOvUWYAFqgv5Idt7N7icRiaEBDRXhKEVfvB1mxg/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('460', 'campus_2547344', '高礼斌', '[395]', '', '15337239156', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauCdKxcJGfmd4VOzfMm1foStEk8lFXggn5mVtia8uEquPA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('461', 'campus_2547343', '张则申', '[51]', '', '18086613990', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('462', 'campus_2547342', '杨桂香', '[392]', '', '18971553522', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybAPnJ0dd6MMUYLQMyVmF6LQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('463', 'campus_2547341', '卢群超', '[392]', '', '13545873506', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybJUlR3IhcxtcMd1ibXds8ZaQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('464', 'campus_2547340', '严和平', '[51]', '', '15871409271', null, '', null, 'http://p.qlogo.cn/bizmail/JdlaAYrOwLotvUfzic6MQponWtXlWMY0o9ANz2r12EYUVymyyyQo23w/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('465', 'campus_2547285', '王罡', '[395]', '', '18971231873', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybZ11Ikq6YFKcCBZNQKnGedw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('466', 'campus_2547284', '吴连兵', '[395]', '', '15337191280', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiav9r6cv6UBHQTQnvaXyWYtxCxhTufIyaIhNu3h8ia7KkIA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('467', 'campus_2547287', '胡胜杰', '[395]', '', '13545211162', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauCHymVxsnwKN6UNahREu0NN1ve6cQYSJN7ibExbg58D9w/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('468', 'campus_2547286', '朱一军', '[395]', '', '18986158061', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgU97f2QyStPZBshV6gVibBYA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('469', 'campus_2547349', '薛国凤', '[56]', '', '13971661242', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('470', 'campus_2547289', '向前', '[41]', '', '15807191987', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('471', 'campus_2547288', '刘萍', '[395]', '', '13720275181', null, '', null, 'http://p.qlogo.cn/bizmail/qVz5picIVjzickibj7Z6FXEys7cKeLz0kVylv2tQ1bETfyObAQ9OePm2A/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('472', 'campus_2547347', '王永丰', '[395]', '', '15107149869', '1', '', null, 'http://p.qlogo.cn/bizmail/0flRu8odE3W6hxEkGmhDNmDhesClBvxUvZFLEaIPiaibLnh7DKibPPOOw/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('473', 'campus_2547348', '罗会江', '[56]', '', '15392914159', null, '', null, '', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('474', 'campus_2547281', '杨军', '[36]', '', '13607176497', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiautVoCIzXjqPJt3IkFKILD6S1BQMXDk5qHQibD6ftVBqIA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('475', 'campus_2547280', '喻宜发', '[36]', '', '18108673248', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('476', 'campus_2522361', '曾垂武', '[181]', '', '18571712854', '1', '', null, 'http://p.qlogo.cn/bizmail/2PWteItnFspniaWV3agsMdqaBA2vU9oAD1mNADfz0uyXiaymWyDHAAbg/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('477', 'campus_2547283', '成伶俐', '[36]', '', '15071359207', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiau6Fxus3rE4RvhFx4xGUlpJHiaUtaj4FtbVjWmuRFajZbA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('478', 'campus_2547282', '吕晓乐', '[392]', '', '15072346156', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybITicUkrDvJy7mpchJysCXgg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('479', 'campus_3213546', '肖昕', '[25]', '', '15872412019', null, '', null, 'http://p.qlogo.cn/bizmail/scSBaXhrbk5FbKdWIE0VHWEHQ8zSiaibVjO2QrXW37YxhJwK0bjzToPQ/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('480', 'campus_2547374', '廖鸿兴', '[395]', '', '15377636701', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiavfibRoe7u7uCMvFBp7gUJsWdBuibaUecUGSVCFy9tgnM0A/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('481', 'campus_2547271', '熊慧丽', '[37]', '', '13697352549', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiav9r6cv6UBHQTQnvaXyWYtxyufDoCAq0Gibj9ANnBgFF3g/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('482', 'campus_2547375', '王倩', '[395]', '', '13429897505', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauCHymVxsnwKN6UNahREu0NMoKSveoCIjQFhaB0KrVJibQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('483', 'campus_2547272', '张伟', '[37]', '', '13986148890', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('484', 'campus_2547372', '尤奇志', '[392]', '', '13986258573', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauCHymVxsnwKN6UNahREu0NQ7d2oTTulic6CF7zF5uvBlQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('485', 'campus_2547373', '李绵勇', '[395]', '', '13035103801', '1', '', null, 'http://p.qlogo.cn/bizmail/uibLkZM4Ig2KqWqHqd9z4PUoJib7OMHYDWbSSIpl3vfkQpNa9UHnmc4w/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('486', 'campus_2547270', '何胜斌', '[393]', '', '15327190543', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiavZnLvSiaabwGG0QhmGAwl7LXVFXcH8j5RTiaSgW4LVRd1w/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('487', 'campus_2547378', '彭富红', '[69]', '', '13971254640', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('488', 'campus_2547379', '李杏', '[69]', '', '15871685086', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgCn9kHjuicf8OnuVDvuR0stw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('489', 'campus_2547376', '李德谦', '[69]', '', '13397133251', '1', '', null, 'http://p.qlogo.cn/bizmail/4qV7w00d71DiaibeXOcEmia71jR7icw3hibERYicphzobeLqxRIG7DVBONiaQ/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('490', 'campus_2547377', '郑国伟', '[69]', '', '13986133153', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('491', 'campus_2547279', '周宏军', '[36]', '', '15827533685', null, '', null, 'http://p.qlogo.cn/bizmail/VlSx8MibFNLRcoYiajuzfzZOibtyBk2nyaOlqPl2fic3od5QQweKu7BBIg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('492', 'campus_2547277', '王海玉', '[36]', '', '15327285418', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('493', 'campus_2547278', '刘莉红', '[36]', '', '13667125680', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('494', 'campus_2547370', '杜小波', '[392]', '', '15107109838', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('495', 'campus_2547275', '吴卫红', '[37]', '', '13554085539', null, '', null, 'http://p.qlogo.cn/bizmail/9IaNFY7wVN5XX1G882yFI1skicI14VOqyqV81OJhY4rR0DicTeZkSmcA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('496', 'campus_2547371', '周嘉静', '[67]', '', '13129956321', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('497', 'campus_2547276', '陶红兵', '[37]', '', '18086615718', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('498', 'campus_2547273', '肖怀贞', '[37]', '', '18986281067', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('499', 'campus_2547274', '闵璇', '[37]', '', '15871357584', '2', '', null, 'http://p.qlogo.cn/bizmail/HgImVhibGLfcMmSfyruekZTY7Q7Trq0cQ8eiarxXoCeIHmc7q892CN5w/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('500', 'campus_2933143', '龚晴霞', '[394]', '', '15337148980', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('501', 'campus_2547369', '彭自来', '[393]', '', '15827006407', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybmHCAHzM4vQcwKYfGgknibWQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('502', 'campus_2547361', '曾珍', '[395]', '', '15327380237', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('503', 'campus_2547362', '张琛', '[64]', '', '13971158679', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('504', 'campus_2547260', '刘毅', '[26,69]', '', '18502722226', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiau6Fxus3rE4RvhFx4xGUlpJ0WpaHdbpo6S82nzKVkSrBQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('505', 'campus_2547363', '夏艳', '[63]', '', '18971222423', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('506', 'campus_2547261', '冯倪', '[26]', '', '13908657169', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgwzdQaUgr7fgxxr33b6nzlg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('507', 'campus_2547364', '高婕', '[63]', '', '13657271345', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('508', 'campus_2547365', '张宗炳', '[63]', '', '13971142338', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('509', 'campus_2547366', '杨晖', '[395]', '', '15071355539', '1', '', null, 'http://p.qlogo.cn/bizmail/5GmAibVicl5NMZwlcGAOND7sJJYFHLzV4RgqZD8JcUfx9B77MvJw9Rcw/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('510', 'campus_2547367', '张尊健', '[395]', '', '18971538969', '1', '', null, 'http://p.qlogo.cn/bizmail/aDxiczibn9Z5sctNeHq3OacxC6HI5VfC6iaGEo8SdCukRiarAuVzTlVTQw/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('511', 'campus_2547368', '李文娟', '[68]', '', '13407154469', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('512', 'campus_2547266', '陈锋', '[48]', '', '18071137586', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgB6fcJ1JNZs0zldnwJAtmhA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('513', 'campus_2547267', '李宗翔', '[394]', '', '15927641928', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('514', 'campus_2547268', '易小平', '[47]', '', '13971257325', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybVic3qYnibYnGBwehWN5K9k3Q/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('515', 'campus_2547269', '刘巍', '[395]', '', '18007152888', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2AEsMCwFxbu4NLhO8CbSGY218PQVAEOsn8AQAqMRR8w/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('516', 'campus_2547262', '桂小梅', '[26]', '', '13477090606', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgdwyQAE0czL02vjuu0MrsWg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('517', 'campus_2547263', '李萍', '[26]', '', '13871218033', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgM05lEVtWwzKJ0rIV5vdz8A/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('518', 'campus_2547264', '白娟', '[26]', '', '13667147188', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('519', 'campus_2547360', '吴剑芬', '[395]', '', '13627292546', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauJYBtstVTQU1PXoibW7HOl3XeibsreSb9RPCXIqGx4zPUA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('520', 'campus_2547265', '孙元平', '[394]', '', '18971268276', null, '', null, 'http://p.qlogo.cn/bizmail/Zkknp175ticS1Aq5NbYnc7KhuaKAjW3xDKs5TLkn1icSYItIOOnKUC2g/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('521', 'campus_2547310', '罗秀芳', '[393]', '', '13720391323', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybkJDcSicjQ7uE0q0NVkjvh6A/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('522', 'campus_2547311', '周明', '[393]', '', '13971357698', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybwbOoBRZujPXM43aj4sz1Pw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('523', 'campus_2547312', '侯桂骅', '[393]', '', '18674017309', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybJ5tVpAxTy42iaIiawHDoIdiaw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('524', 'campus_2547259', '刘飞', '[24]', '', '18971185690', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiataicQHiccvaQebfybPSr7U8Q1Mwm8jqUfmN7zk8yjGujUw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('525', 'campus_2547313', '徐智勇', '[393]', '', '13607159542', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiav9r6cv6UBHQTQnvaXyWYtxtlIiaVibkoDHSDV28kxzUMIw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('526', 'campus_2547250', '彭维清', '[52]', '', '15827653098', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rg5iaoRM1YjGqqTudLlQ0MNhw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('527', 'campus_2547253', '刘明刚', '[23]', '', '18971553908', '1', '', null, 'http://p.qlogo.cn/bizmail/1sUzxjZTPXR8Amxe6Rebwk82D0uZx7QnCUicAibfbjep0UK2ORV5PibYA/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('528', 'campus_2547319', '赵康', '[44]', '', '13971633966', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('529', 'campus_2547254', '杨卉荣', '[23]', '', '13886071376', null, '', null, '', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('530', 'campus_2547318', '黄文', '[44]', '', '13886050256', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('531', 'campus_2547251', '皮佳荔', '[23]', '', '13971667614', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgj7mLmJyPuoRB5RkI7tZsvg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('532', 'campus_2547390', '何志怀', '[52]', '', '18171417797', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauCdKxcJGfmd4VOzfMm1foScdtqpBFXQ7xuy8byCKxVpQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('533', 'campus_2547252', '彭静', '[23]', '', '13971661632', '2', '', null, 'http://p.qlogo.cn/bizmail/Y8SiaYgkD3tqlSt0PulHUlSmRgDu7ic5rd4UJwsOd4HZaOV2h3wKu67w/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('534', 'campus_2547257', '李腊生', '[23]', '', '13554199917', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('535', 'campus_2547315', '望明', '[392]', '', '15927668885', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeias2K3wE7yBkwnyrd9rFiaVybgNkh64YYD1UASChsxj8MoQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('536', 'campus_2547258', '曹非', '[23]', '', '18717156946', null, '', null, 'http://p.qlogo.cn/bizmail/znNDaR9UyhXp3qvkjSJnRXk6JRtDayM36284xw3InCq5UMQjdQqnRA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('537', 'campus_2547314', '李祎琳', '[72]', '', '15091377350', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('538', 'campus_2547255', '郑文兵', '[23]', '', '15972161826', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgwVeXQicLL169f4LDJWjzrsg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('539', 'campus_2547317', '胡新民', '[44]', '', '13554205432', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('540', 'campus_2547256', '万荣', '[23]', '', '13006309970', null, '', null, '', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('541', 'campus_2547316', '徐启飞', '[44]', '', '18908637267', '2', '', null, 'http://p.qlogo.cn/bizmail/ZOj2yfHeUtBuDibriaP3F9icibmalyFLMhJT4Yibj8Y2xblB6F3SEmCtvKA/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('542', 'campus_2547249', '朱大春', '[394]', '', '13907103585', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgn5n8bv5HD4XCc557ohNCAw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('543', 'campus_2547301', '叶大高', '[392]', '', '15827129799', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiautVoCIzXjqPJt3IkFKILD6ek2pGEOpoFIE5l2dibQE9Vw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('544', 'campus_2547248', '晏如', '[395]', '', '15926456587', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiasxhul5dEtjLJmbFPSokTRcWRhGK3Py99lvnvEhOKSK9w/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('545', 'campus_2547302', '王金华', '[40]', '', '13487078932', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('546', 'campus_2547300', '王华', '[40]', '', '13720139278', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('547', 'campus_2547387', '魏刚毅', '[181]', '', '13986042127', null, '', null, '', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('548', 'campus_2547388', '叶向阳', '[181]', '', '15907193954', null, '', null, 'http://p.qlogo.cn/bizmail/a6mPmiajXc5qAiaR8Kv9ibLLo0Hluib90Qah1EksQe3a1jh283yMhibCCbg/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('549', 'campus_2547389', '蒋忠乾', '[72]', '', '18986158405', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('550', 'campus_2547383', '周晓雯', '[70]', '', '15327199923', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('551', 'campus_2547384', '黄敏', '[70]', '', '18086107676', '2', '', null, 'http://p.qlogo.cn/bizmail/Iff9Ric0T0sLURB6tlrH9OMQxHWsweHuWOtV9wRDxBIXFHScBldibdgg/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('552', 'campus_2547385', '吴汉平', '[181]', '', '13907140825', null, '', null, 'http://p.qlogo.cn/bizmail/wkibOjn8pVpLh0iasNp32QYYIClzoxpP4icumiaiaACMFkWk0CEW0dM4jQg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('553', 'campus_2547386', '雷玲', '[181]', '', '15997478791', null, '', null, 'http://p.qlogo.cn/bizmail/KeDew3f1E2knNWD4Ps5c1NepCLrTia0crZ8fISAG6qjly5QCQfftZRA/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('554', 'campus_2547240', '伍代雁', '[16]', '', '18971268687', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiataicQHiccvaQebfybPSr7U8Qh5A8YibWrbKtdqfKeibVoxXA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('555', 'campus_2547241', '李霓', '[16]', '', '13871091840', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgp6iaSFbXOreMaCkV6I4Vz1A/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('556', 'campus_2547380', '刘波', '[69]', '', '13886157550', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('557', 'campus_2547309', '张凤', '[393]', '', '13477049438', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauCdKxcJGfmd4VOzfMm1foSdYvUibOvMy6HuVCBld7fG7g/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('558', 'campus_2547381', '王弘', '[70]', '', '13907108224', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('559', 'campus_2547242', '殷卫国', '[395]', '', '15927115825', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgV6mPKzXZAcNLIibauPQM8iaw/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('560', 'campus_2547308', '祝佳琳', '[395]', '', '13476225987', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiau6Fxus3rE4RvhFx4xGUlpJfFibDWYBqUgKzSVM0C616Ag/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('561', 'campus_2547243', '康雪松', '[21]', '', '13163243982', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiauCjSoWWYzz3gAUZvhCmCBvbWsT2xdxxqcTBpSEsR6YUQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('562', 'campus_2547382', '唐健梅', '[70]', '', '15327192350', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgWZTsNrmkoNhrFr2lBp2DJA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('563', 'campus_2547307', '周镜', '[395]', '', '13545084888', null, '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiau6Fxus3rE4RvhFx4xGUlpJgCcjeibamHCiakOExW8no7tg/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('564', 'campus_2547244', '万雅丽', '[21]', '', '13797027630', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgfeNHlxib2gfs9jUvuCAtX5A/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('565', 'campus_2547306', '胡牧', '[395]', '', '13397113567', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgoLJweqJtHG1KO11ByLfotA/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('566', 'campus_2547245', '郭彦希', '[21]', '', '13971260811', '2', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgOTBXbEEIH8ia83JE395JzEQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('567', 'campus_2547305', '张丹', '[395]', '', '17771858535', null, '', null, 'http://p.qlogo.cn/bizmail/HVGjb45jeKcdic8LCoTcwJfc2Fk0pqOXJ2EQKCxGOwJ3Ir2ibzRZa4QA/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('568', 'campus_2547246', '李荣', '[395]', '', '15337101061', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rgwHVjzY1NsNn5aXkIEOLeqQ/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('569', 'campus_2547304', '徐方', '[395]', '', '13554260799', '1', '', null, 'http://p.qlogo.cn/bizmail/LPQyRXIepTdoZlJuuZDYjOnMZibibCKz3mibroZak4w8h2rWe9yTGxKPA/0', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('570', 'campus_2592470', '宋波', '[12]', '', '18071050113', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat5icBeU8OE77fJPaGg2Xvk6ANicqTpegc3xeiat0XqCBuOw/', '1', '{\"attrs\":[]}', '1', '[4]');
INSERT INTO `chu_wxuser` VALUES ('571', 'campus_2547247', '陈俊才', '[394]', '', '18971185006', '1', '', null, 'http://shp.qpic.cn/bizmp/CLZClzxgeiat9gUSqO91c3h5VQs2z01rg1GU4I4FyMtoIQb5EJ1qm0Q/', '1', '{\"attrs\":[]}', '1', '[1]');
INSERT INTO `chu_wxuser` VALUES ('572', 'campus_2547303', '陈松德', '[395]', '', '13247128298', null, '', null, '', '4', '{\"attrs\":[]}', '1', '[1]');

-- ----------------------------
-- Table structure for chu_zt_action
-- ----------------------------
DROP TABLE IF EXISTS `chu_zt_action`;
CREATE TABLE `chu_zt_action` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL COMMENT '对应于CHU_TAG的ID。依据TAG的不同，我们为不同用户设定不同的身份。',
  `TAG_NAME` varchar(20) DEFAULT NULL,
  `ZT` int(2) DEFAULT NULL,
  `IMG_URL` varchar(100) DEFAULT NULL,
  `ACTION_TITLE` varchar(20) DEFAULT NULL COMMENT '按钮名称',
  `ACTION_NAME` varchar(20) DEFAULT NULL COMMENT '按钮的操作，对应于VUX中的组件名称',
  `QUERY_PARAM` varchar(200) DEFAULT NULL COMMENT 'JSON字符串',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chu_zt_action
-- ----------------------------
INSERT INTO `chu_zt_action` VALUES ('1', '17', '使用人组', '10', '/images/icons/wx_agree.png', '同意', 'agreeLeadingZC', null);
INSERT INTO `chu_zt_action` VALUES ('2', '17', '使用人组', '10', '/images/icons/wx_refuse.png', '拒绝', null, '/wx/zcsy/refuseLeadingZC?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('3', '17', '使用人组', '1', '/images/icons/wx_sendback.png', '上交资产', null, '/wx/zcsy/sendbackZC?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('4', '17', '使用人组', '1', '/images/icons/wx_zcwx.png', '申请维修', null, '/wx/zcsy/zcwxSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('5', '17', '使用人组', '1', '/images/icons/wx_zcxz.png', '申请闲置', null, '/wx/zcsy/zcxzSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('6', '17', '使用人组', '1', '/images/icons/wx_zcbf.png', '申请报废', null, '/wx/zcsy/zcbfSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('7', '17', '后勤管理人员组', '9', '/images/icons/wx_delete.png', '删除', 'deleteZC', null);
INSERT INTO `chu_zt_action` VALUES ('8', '17', '后勤管理人员组', '9', '/images/icons/wx_allocate.png', '调拨', null, '/wx/zcdj/allocateZC?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('9', '17', '后勤管理人员组', '0', '/images/icons/wx_reallocate.png', '重新调拨', null, '/wx/zcdj/reallocateZC?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('10', '17', '后勤管理人员组', '11', '/images/icons/wx_agree.png', '同意', 'agreeRevertZC', null);
INSERT INTO `chu_zt_action` VALUES ('11', '17', '后勤管理人员组', '11', '/images/icons/wx_refuse.png', '拒绝', null, '/wx/zcdj/refuseRevertZC?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('12', '17', '后勤管理人员组', '4', '/images/icons/wx_agree.png', '同意', 'agreeWXSQ', null);
INSERT INTO `chu_zt_action` VALUES ('13', '17', '后勤管理人员组', '4', '/images/icons/wx_refuse.png', '拒绝', null, '/wx/zcyw/refuseWXSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('14', '17', '后勤管理人员组', '5', '/images/icons/wx_agree.png', '同意', null, '/wx/zcyw/agreeBFSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('15', '17', '后勤管理人员组', '5', '/images/icons/wx_refuse.png', '拒绝', null, '/wx/zcyw/refuseBFSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('16', '17', '后勤管理人员组', '6', '/images/icons/wx_agree.png', '同意', null, '/wx/zcyw/agreeXZSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('17', '17', '后勤管理人员组', '6', '/images/icons/wx_refuse.png', '拒绝', null, '/wx/zcyw/refuseXZSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('18', '17', '后勤管理人员组', '3', '/images/icons/wx_agreeBFZC.png', '报废', null, '/wx/zcgl/agreeBFZC?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('19', '17', '巡检组', '7', '/images/icons/wx_sbzc.png', '设备正常', null, '/wx/rcxj/finishRCXJ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('20', '17', '巡检组', '7', '/images/icons/wx_zcwx.png', '申请维修', null, '/wx/rcxj/zcwxSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('21', '17', '巡检组', '7', '/images/icons/wx_zcxz.png', '申请闲置', null, '/wx/rcxj/zcxzSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('22', '17', '巡检组', '7', '/images/icons/wx_zcbf.png', '申请报废', null, '/wx/rcxj/zcbfSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('23', '17', '维修组', '2', ' /images/icons/wx_finishGZWX.png', '完成维修', null, '/wx/gzwx/finishGZWX?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('24', '17', '维修组', '2', '/images/icons/wx_resubmitWXSQ.png', '再次维修', null, '/wx/gzwx/resubmitWXSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('25', '17', '维修组', '2', '/images/icons/wx_zcxz.png', '申请闲置', null, '/wx/gzwx/zcxzSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('26', '17', '维修组', '2', '/images/icons/wx_zcbf.png', '申请报废', null, '/wx/gzwx/zcbfSQ?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('27', '17', '部门资产管理员组', '0', '/images/icons/wx_revert.png', '申请归还', null, '/wx/zcfp/revertZC?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('28', '17', '部门资产管理员组', '0', '/images/icons/wx_assign.png', '资产分配', null, '/wx/zcfp/assignZC?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('29', '17', '部门资产管理员组', '10', '/images/icons/wx_reassign.png', '重新分配', null, '/wx/zcfp/reassignZC?zcid=%s');
INSERT INTO `chu_zt_action` VALUES ('30', '17', '部门资产管理员组', '12', '/images/icons/wx_agree.png', '同意', 'agreeSendbackZC', null);
INSERT INTO `chu_zt_action` VALUES ('31', '17', '部门资产管理员组', '12', '/images/icons/wx_refuse.png', '拒绝', null, '/wx/zcfp/refuseSendbackZC?zcid=%s');

-- ----------------------------
-- Table structure for c_fj
-- ----------------------------
DROP TABLE IF EXISTS `c_fj`;
CREATE TABLE `c_fj` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `JZW_ID` int(11) DEFAULT NULL,
  `FLOOR` varchar(20) DEFAULT NULL,
  `ROOM` varchar(20) DEFAULT NULL,
  `DEPT_NAME` varchar(100) DEFAULT NULL COMMENT '与企业微信号中的组织架构保持一致',
  `GLR` varchar(30) DEFAULT NULL COMMENT '管理人的微信号userid',
  `TYBZ` int(1) DEFAULT '0' COMMENT '报废（1）、未报废（0）',
  PRIMARY KEY (`ID`),
  KEY `fk_fj` (`JZW_ID`),
  CONSTRAINT `fk_fj` FOREIGN KEY (`JZW_ID`) REFERENCES `c_jzw` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='楼层、房间等相关信息';

-- ----------------------------
-- Records of c_fj
-- ----------------------------
INSERT INTO `c_fj` VALUES ('1', '17', '1', '1楼', '101室', '开发部', 'baiweibing', '0');
INSERT INTO `c_fj` VALUES ('2', '17', '1', '2楼', '201室', '开发部', 'shangwei', '0');
INSERT INTO `c_fj` VALUES ('3', '17', '2', '-1楼', '地下室', '开发部', 'shangwei', '0');
INSERT INTO `c_fj` VALUES ('4', '17', '3', '12楼', 'S4304', '开发部', 'shangwei', '0');
INSERT INTO `c_fj` VALUES ('5', '1', '4', '1楼', '101房间', '体育组', 'campus_2547362', '0');

-- ----------------------------
-- Table structure for c_jzlx
-- ----------------------------
DROP TABLE IF EXISTS `c_jzlx`;
CREATE TABLE `c_jzlx` (
  `ID` int(3) NOT NULL AUTO_INCREMENT,
  `MC` varchar(100) DEFAULT NULL,
  `REMARK` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of c_jzlx
-- ----------------------------
INSERT INTO `c_jzlx` VALUES ('1', '行政办公建筑', '行政办公楼');
INSERT INTO `c_jzlx` VALUES ('2', '文教建筑', '教学楼、图书馆、学术交流中心、培训中心、档案馆、活动中心 ');
INSERT INTO `c_jzlx` VALUES ('3', '生活建筑', '公寓、宿舍');
INSERT INTO `c_jzlx` VALUES ('4', '托教建筑', '托儿所、附小、附中');
INSERT INTO `c_jzlx` VALUES ('5', '科研建筑', '研究所、科学实验楼、科学站、科研基地、科技园');
INSERT INTO `c_jzlx` VALUES ('6', '医疗建筑', '医院、疗养院、门诊部、福利院');
INSERT INTO `c_jzlx` VALUES ('7', '商业建筑', '零售商店、商场、批发市场、专业商店、门市部、购物中心');
INSERT INTO `c_jzlx` VALUES ('8', '观览建筑', '博物馆、展览馆、美术馆、艺术馆、音乐厅、电影院、剧院');
INSERT INTO `c_jzlx` VALUES ('9', '体育建筑', '体育馆、健身房、游泳池、球场、田径场');
INSERT INTO `c_jzlx` VALUES ('10', '旅馆建筑', '宾馆、酒店、招待所');
INSERT INTO `c_jzlx` VALUES ('11', '餐饮建筑', '食堂、餐厅');
INSERT INTO `c_jzlx` VALUES ('12', '其他建筑类', '停车场库');

-- ----------------------------
-- Table structure for c_jzw
-- ----------------------------
DROP TABLE IF EXISTS `c_jzw`;
CREATE TABLE `c_jzw` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `XQ_ID` int(11) DEFAULT NULL,
  `LX_ID` int(3) DEFAULT NULL,
  `MC` varchar(100) DEFAULT NULL,
  `DZ` varchar(200) DEFAULT NULL COMMENT '常规意义上的位置描述。如：XXX路XX号',
  `POI` varchar(200) DEFAULT NULL COMMENT '存放建筑物的地理信息，可以籍此使用微信进行定位。',
  `TYBZ` int(1) DEFAULT '0' COMMENT '报废（1）、未报废（0）',
  PRIMARY KEY (`ID`),
  KEY `fk_jzw` (`XQ_ID`),
  CONSTRAINT `fk_jzw` FOREIGN KEY (`XQ_ID`) REFERENCES `c_xq` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='教学楼、实验楼、教工宿舍、学生宿舍、食堂等';

-- ----------------------------
-- Records of c_jzw
-- ----------------------------
INSERT INTO `c_jzw` VALUES ('1', '17', '1', '3', '西一楼', '集贤院路22号', null, '0');
INSERT INTO `c_jzw` VALUES ('2', '17', '1', '11', '西一食堂', '集贤院路8号', null, '0');
INSERT INTO `c_jzw` VALUES ('3', '17', '1', '1', '测试建筑物', null, null, null);
INSERT INTO `c_jzw` VALUES ('4', '1', '3', '1', '主楼', '', null, '0');
INSERT INTO `c_jzw` VALUES ('5', '1', '3', '2', '明德楼', '', null, '0');

-- ----------------------------
-- Table structure for c_xq
-- ----------------------------
DROP TABLE IF EXISTS `c_xq`;
CREATE TABLE `c_xq` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `XQMC` varchar(50) NOT NULL,
  `XQDZ` varchar(100) DEFAULT NULL,
  `YB` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='某些学校有多个校区。';

-- ----------------------------
-- Records of c_xq
-- ----------------------------
INSERT INTO `c_xq` VALUES ('1', '17', '光谷校区', '光谷大道142号', '430000');
INSERT INTO `c_xq` VALUES ('2', '17', '南湖校区', '南湖大道100号', '420000');
INSERT INTO `c_xq` VALUES ('3', '1', '青山校区', '', '');

-- ----------------------------
-- Table structure for c_zcgl
-- ----------------------------
DROP TABLE IF EXISTS `c_zcgl`;
CREATE TABLE `c_zcgl` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `DEPT_NO` int(11) DEFAULT NULL,
  `DEPT_NAME` varchar(100) DEFAULT NULL COMMENT '与企业微信号中的组织架构保持一致',
  `FZR` varchar(30) DEFAULT NULL,
  `GLR` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8 COMMENT='资产管理部门相关信息';

-- ----------------------------
-- Records of c_zcgl
-- ----------------------------
INSERT INTO `c_zcgl` VALUES ('1', '17', '2', '计算机学院', 'baiweibing', 'shixiaoping');
INSERT INTO `c_zcgl` VALUES ('2', '17', '3', '文学院', 'XuQingWei', 'XuQingWei');
INSERT INTO `c_zcgl` VALUES ('3', '17', '4', '测试组1', 'baiweibing', 'chentianyu');
INSERT INTO `c_zcgl` VALUES ('11', '1', '15', '书记', 'campus_2547238', 'campus_2547238');
INSERT INTO `c_zcgl` VALUES ('12', '1', '12', '测试', null, null);
INSERT INTO `c_zcgl` VALUES ('13', '1', '16', '校长', null, null);
INSERT INTO `c_zcgl` VALUES ('14', '17', '5', '测试组2', 'fuzuo', 'daishaopeng');
INSERT INTO `c_zcgl` VALUES ('18', '17', '6', '测试组3', 'pengxiao', 'lirunfang');
INSERT INTO `c_zcgl` VALUES ('19', '17', '7', '测试组4', 'WenChen', 'luozhong');
INSERT INTO `c_zcgl` VALUES ('24', '1', '70', '艺术组', null, null);
INSERT INTO `c_zcgl` VALUES ('25', '1', '77', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('26', '1', '67', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('27', '1', '41', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('28', '1', '48', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('29', '1', '39', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('30', '1', '25', '电教处', null, null);
INSERT INTO `c_zcgl` VALUES ('31', '1', '76', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('32', '1', '24', '教科室', null, null);
INSERT INTO `c_zcgl` VALUES ('33', '1', '18', '年级管理组', null, null);
INSERT INTO `c_zcgl` VALUES ('34', '1', '78', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('35', '1', '23', '教导处', null, null);
INSERT INTO `c_zcgl` VALUES ('36', '1', '66', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('37', '1', '8', '教师(禁止删除)', null, null);
INSERT INTO `c_zcgl` VALUES ('38', '1', '73', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('39', '1', '72', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('40', '1', '37', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('41', '1', '69', '体育组', null, null);
INSERT INTO `c_zcgl` VALUES ('42', '1', '47', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('43', '1', '26', '总务处', null, null);
INSERT INTO `c_zcgl` VALUES ('44', '1', '21', '党政办', null, null);
INSERT INTO `c_zcgl` VALUES ('45', '1', '52', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('46', '1', '62', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('47', '1', '74', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('48', '1', '60', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('49', '1', '49', '化学组', null, null);
INSERT INTO `c_zcgl` VALUES ('50', '1', '181', '美术组', null, null);
INSERT INTO `c_zcgl` VALUES ('51', '1', '40', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('52', '1', '20', '教师组', null, null);
INSERT INTO `c_zcgl` VALUES ('53', '1', '46', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('54', '1', '44', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('55', '1', '17', '行政组', null, null);
INSERT INTO `c_zcgl` VALUES ('56', '1', '34', '语文组', null, null);
INSERT INTO `c_zcgl` VALUES ('57', '1', '68', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('58', '1', '27', '年级主任', null, null);
INSERT INTO `c_zcgl` VALUES ('59', '1', '50', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('60', '1', '54', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('61', '1', '56', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('62', '1', '51', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('63', '1', '61', '历史组', null, null);
INSERT INTO `c_zcgl` VALUES ('64', '1', '58', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('65', '1', '35', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('66', '1', '63', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('67', '1', '45', '物理组', null, null);
INSERT INTO `c_zcgl` VALUES ('68', '1', '36', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('69', '1', '75', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('70', '1', '19', '年级书记', null, null);
INSERT INTO `c_zcgl` VALUES ('71', '1', '53', '生物组', null, null);
INSERT INTO `c_zcgl` VALUES ('72', '1', '57', '政治组', null, null);
INSERT INTO `c_zcgl` VALUES ('73', '1', '22', '学工处', null, null);
INSERT INTO `c_zcgl` VALUES ('74', '1', '43', '2014级', null, null);
INSERT INTO `c_zcgl` VALUES ('75', '1', '38', '数学组', null, null);
INSERT INTO `c_zcgl` VALUES ('76', '1', '42', '英语组', null, null);
INSERT INTO `c_zcgl` VALUES ('77', '1', '65', '地理组', null, null);
INSERT INTO `c_zcgl` VALUES ('78', '1', '59', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('79', '1', '55', '2015级', null, null);
INSERT INTO `c_zcgl` VALUES ('80', '1', '64', '2016级', null, null);
INSERT INTO `c_zcgl` VALUES ('81', '1', '183', '班主任', null, null);
INSERT INTO `c_zcgl` VALUES ('82', '1', '184', '教研组', null, null);
INSERT INTO `c_zcgl` VALUES ('83', '1', '185', '备课组长', null, null);
INSERT INTO `c_zcgl` VALUES ('84', '1', '391', '2014级班主任', null, null);
INSERT INTO `c_zcgl` VALUES ('85', '1', '392', '2015级班主任', null, null);
INSERT INTO `c_zcgl` VALUES ('86', '1', '393', '2016级班主任', null, null);
INSERT INTO `c_zcgl` VALUES ('87', '1', '394', '德育管理组', null, null);
INSERT INTO `c_zcgl` VALUES ('88', '1', '395', '2014级教师', null, null);

-- ----------------------------
-- Table structure for c_zclx
-- ----------------------------
DROP TABLE IF EXISTS `c_zclx`;
CREATE TABLE `c_zclx` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `LX_ID` varchar(8) NOT NULL,
  `LX_PID` varchar(8) DEFAULT NULL,
  `MC` varchar(100) DEFAULT NULL,
  `ZJNX` decimal(3,1) DEFAULT NULL COMMENT '单位年',
  `REMARK` varchar(80) DEFAULT NULL,
  `PIC_URL` varchar(100) DEFAULT NULL COMMENT '依据资产类型，提供缺省的资产图片',
  `IMG_VERSION` int(5) DEFAULT '0' COMMENT '图片版本号',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of c_zclx
-- ----------------------------
INSERT INTO `c_zclx` VALUES ('1', '17', '03', null, '仪器仪表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/e0beb2cf-1a04-4c6e-8a0a-620283009373', '1');
INSERT INTO `c_zclx` VALUES ('2', '17', '03010105', '03', '点温计', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/1873cb32-84dc-407a-a88c-92ae283f82b1', '1');
INSERT INTO `c_zclx` VALUES ('3', '17', '03010108', '03', '温湿度测量仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/b24ac8d3-c0d9-4e40-ad0e-96b2c0d29a51', '1');
INSERT INTO `c_zclx` VALUES ('4', '17', '03010116', '03', '红外测温仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/86367ee9-4280-4547-8e7b-777da2ab9a2a', '1');
INSERT INTO `c_zclx` VALUES ('5', '17', '03010119', '03', '标准温度计', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/ef08837a-5492-406b-abf9-ea43ff14ea20', '1');
INSERT INTO `c_zclx` VALUES ('6', '17', '03010132', '03', '温度计（含数字温度计)', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/425b8f2d-5587-4fc0-84b9-c34424d1f641', '1');
INSERT INTO `c_zclx` VALUES ('7', '17', '03010133', '03', '测温仪(装置)', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/f5b77854-9289-4f62-8d52-db6b82c153b9', '1');
INSERT INTO `c_zclx` VALUES ('8', '17', '03010201', '03', '压力计', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/58491bdf-df98-4477-8e12-1fbb1ddde5c1', '1');
INSERT INTO `c_zclx` VALUES ('9', '17', '03010206', '03', '真空压力仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/1b3a7986-77c8-4add-9bb6-39c885a6baaf', '1');
INSERT INTO `c_zclx` VALUES ('10', '17', '03010252', '03', '减压装置', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/cafdc953-7c9a-4e67-8721-868b2b79395b', '1');
INSERT INTO `c_zclx` VALUES ('11', '17', '03010253', '03', '压力表（计）', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/1cf61e44-4b61-45ba-88e2-969f5a1bbba1', '1');
INSERT INTO `c_zclx` VALUES ('12', '17', '03010304', '03', '质量流量控制器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/3f70fd3b-3e17-479c-bca5-5ab0ee180b06', '1');
INSERT INTO `c_zclx` VALUES ('13', '17', '03010350', '03', '流量计', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/aeecbeb8-3332-4aa7-92ff-fe1df9fc9bf8', '1');
INSERT INTO `c_zclx` VALUES ('14', '17', '03010501', '03', '测厚仪（厚度计）', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/2d83bd8b-d2e9-437e-8bbf-55619cb25f7e', '1');
INSERT INTO `c_zclx` VALUES ('15', '17', '03010502', '03', '测振仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/eff7dc42-7ab5-4b13-a2ed-64a3d753f10a', '1');
INSERT INTO `c_zclx` VALUES ('16', '17', '03010502', '03', '测振器（头、仪）', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/5f553496-9643-4c02-8ff0-aa529ae10ffd', '1');
INSERT INTO `c_zclx` VALUES ('17', '17', '03010508', '03', '传感器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/8e7cf1ce-8683-4737-99ad-6f79f56a4a59', '1');
INSERT INTO `c_zclx` VALUES ('18', '17', '03010525', '03', '测力仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/752277ce-b129-4fae-ad0e-0ec06d14f45d', '1');
INSERT INTO `c_zclx` VALUES ('19', '17', '03010527', '03', '转速仪（表）', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/9c6db0c5-f441-416c-8714-fc86860c9918', '1');
INSERT INTO `c_zclx` VALUES ('20', '17', '03010531', '03', '测速仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/c949725a-e9c1-41bb-acee-00f9c6a0f142', '1');
INSERT INTO `c_zclx` VALUES ('21', '17', '03010549', '03', '激光测速系统', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/f61f62ac-0221-4116-bbfe-d3eec3fbfbfc', '1');
INSERT INTO `c_zclx` VALUES ('22', '17', '03010550', '03', '直流放大器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/ba580034-33e0-4735-8f03-8e0dfac24698', '1');
INSERT INTO `c_zclx` VALUES ('23', '17', '03010552', '03', '阻抗测量仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/e227f873-a3bb-4476-afa5-18d583c8c150', '1');
INSERT INTO `c_zclx` VALUES ('24', '17', '03010555', '03', '信号源', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/db68e51e-e1aa-4f2c-a13d-c1a98f6a0769', '1');
INSERT INTO `c_zclx` VALUES ('25', '17', '03010611', '03', '高灵敏度记录仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/5e913513-bb65-459e-8a69-ad70a7ddedc5', '1');
INSERT INTO `c_zclx` VALUES ('26', '17', '03010612', '03', '自动平衡记录仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/7fc567e1-936b-4245-8c88-f7160a95af47', '1');
INSERT INTO `c_zclx` VALUES ('27', '17', '03010630', '03', 'X-Y函数记录仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/4765b1a0-7901-4049-a934-521ce4ebd82f', '1');
INSERT INTO `c_zclx` VALUES ('28', '17', '03010722', '03', '温湿度指示控制仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/9031f3ae-5463-4541-9fe2-b0c4c5da640b', '1');
INSERT INTO `c_zclx` VALUES ('29', '17', '03010723', '03', '精密温度（湿度）控制仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/00d080ae-22df-464a-a59f-d66bad7dc85d', '1');
INSERT INTO `c_zclx` VALUES ('30', '17', '03020201', '03', '直流电流表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/097476ad-151d-431d-afc1-98dc4dce12df', '1');
INSERT INTO `c_zclx` VALUES ('31', '17', '03020202', '03', '交流电流表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/6dffe751-65b6-446b-bd9e-77eb2b299a8f', '1');
INSERT INTO `c_zclx` VALUES ('32', '17', '03020203', '03', '交直流电流表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/e272bcde-a999-405b-9269-a3a927081504', '1');
INSERT INTO `c_zclx` VALUES ('33', '17', '03020204', '03', '直流电压表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/b0fb04cb-875f-4569-83a5-8a66a9a7abea', '1');
INSERT INTO `c_zclx` VALUES ('34', '17', '03020205', '03', '交流电压表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/8fe28750-c6c0-4458-8433-24eaf2e3f5cb', '1');
INSERT INTO `c_zclx` VALUES ('35', '17', '03020206', '03', '交直流电压表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/2ee8ffa2-beee-4d26-9b9c-741232c18301', '1');
INSERT INTO `c_zclx` VALUES ('36', '17', '03020210', '03', '瓦特表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/df764888-af37-4798-b7fb-eef6ec7d25a7', '1');
INSERT INTO `c_zclx` VALUES ('37', '17', '03020225', '03', '欧姆表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/806f71d6-8762-49a6-b781-04c30538e908', '1');
INSERT INTO `c_zclx` VALUES ('38', '17', '03020226', '03', '万用电表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/60cd6f7c-691c-4ac6-8b20-f4202d93c5ea', '1');
INSERT INTO `c_zclx` VALUES ('39', '17', '03021001', '03', '直流稳压电源', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/650761a8-da9c-47ff-9b0a-2c448a190aaa', '1');
INSERT INTO `c_zclx` VALUES ('40', '17', '03021009', '03', '交直流稳压器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/a1a735c4-ee06-4c7e-8fc5-47debb69edcf', '1');
INSERT INTO `c_zclx` VALUES ('41', '17', '03021010', '03', '交直流电源', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/50ddb6e4-0084-4fca-a784-be4987976f77', '1');
INSERT INTO `c_zclx` VALUES ('42', '17', '03021011', '03', '交流稳压电源(含磁饱)', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/3024902b-cee6-4832-83c1-14741e072700', '1');
INSERT INTO `c_zclx` VALUES ('43', '17', '03021018', '03', '不间断电源', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/c54220cd-78d8-40e1-9dce-fae525469d38', '1');
INSERT INTO `c_zclx` VALUES ('44', '17', '03021106', '03', '数字万用表', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/fc4be3ec-b5df-4610-9691-87605528cd28', '1');
INSERT INTO `c_zclx` VALUES ('45', '17', '03040101', '03', '生物显微镜', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/18d44024-5e9e-4527-9b0b-664259ef84e1', '1');
INSERT INTO `c_zclx` VALUES ('46', '17', '03040107', '03', '倒置显微镜', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/d738c81f-03b4-4a9d-ab7e-538d2467fa87', '1');
INSERT INTO `c_zclx` VALUES ('47', '17', '03040108', '03', '荧光显微镜', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/6ec4cf99-3c75-4bb3-882d-9852c99a6b26', '1');
INSERT INTO `c_zclx` VALUES ('48', '17', '03040112', '03', '体视显微镜', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/0771aa53-624c-4ecf-a3b5-b3d2647379b3', '1');
INSERT INTO `c_zclx` VALUES ('49', '17', '03040119', '03', '金相显微镜', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/fea83a43-7b37-4563-a6cc-de9b88fa865e', '1');
INSERT INTO `c_zclx` VALUES ('50', '17', '03040124', '03', '偏光显微镜', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/3d1672eb-d52a-426c-9445-15bdd11a5bd5', '1');
INSERT INTO `c_zclx` VALUES ('51', '17', '03060101', '03', '光电分析天平', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/fcacf0cb-5867-4a74-90ea-4b73b68ad123', '1');
INSERT INTO `c_zclx` VALUES ('52', '17', '03060102', '03', '分析天平', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/7682fb52-928b-4f71-a449-3bf47c7515cc', '1');
INSERT INTO `c_zclx` VALUES ('53', '17', '03060103', '03', '精密天平', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/81eb0b57-a229-49c3-89a3-94ba69b49f7c', '1');
INSERT INTO `c_zclx` VALUES ('54', '17', '03060112', '03', '扭力天平', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/38b98a58-6a41-4ae4-a4c6-ef9822873811', '1');
INSERT INTO `c_zclx` VALUES ('55', '17', '03060113', '03', '热电天平', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/2274a5a1-7734-45d2-a46e-9b020c2f3eb2', '1');
INSERT INTO `c_zclx` VALUES ('56', '17', '03060116', '03', '电子天平', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/42d44b4e-fdde-46ee-83a0-48987d55b74d', '1');
INSERT INTO `c_zclx` VALUES ('57', '17', '03060602', '03', '低温冰箱', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/c90ec895-16fc-42d9-afc9-2295079e291e', '1');
INSERT INTO `c_zclx` VALUES ('58', '17', '03060604', '03', '电冰柜', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/26330bc2-318e-413b-9ca4-f4f8f8b920c5', '1');
INSERT INTO `c_zclx` VALUES ('59', '17', '03160601', '03', '幻灯机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/2fb2d6fd-dc93-4379-9120-c6708aa58e95', '1');
INSERT INTO `c_zclx` VALUES ('60', '17', '03160602', '03', '投影机（仪）', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/9dd4a9e5-2c49-4ac1-9c4a-eb2dce76973d', '1');
INSERT INTO `c_zclx` VALUES ('61', '17', '03170101', '03', '135照相机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/806c5d39-f846-4863-b405-1dc7825fc0f6', '1');
INSERT INTO `c_zclx` VALUES ('62', '17', '03170102', '03', '120照相机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/3d7ddb2e-2bbb-4e01-9058-cbe9cbc96401', '1');
INSERT INTO `c_zclx` VALUES ('63', '17', '03170103', '03', '120/135两用照相机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/5b00a9a1-03a2-4938-b51b-81b039eac237', '1');
INSERT INTO `c_zclx` VALUES ('64', '17', '03170318', '03', '数码相机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/413606f7-f377-4db3-bd31-09d8101836b3', '1');
INSERT INTO `c_zclx` VALUES ('65', '17', '03170801', '03', '闪光灯', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/1d571dba-0f83-48db-9884-87db226b1d44', '1');
INSERT INTO `c_zclx` VALUES ('66', '17', '03171102', '03', '三角架', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/f096bbe2-72c7-4d16-8c54-e5af345c894c', '1');
INSERT INTO `c_zclx` VALUES ('67', '17', '04', null, '机电设备', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/654dbd3a-b172-450a-8626-ffc6ec3d76ae', '1');
INSERT INTO `c_zclx` VALUES ('68', '17', '04070704', '04', '空调', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/8628cbd4-eecd-4d80-9b53-e28fd6169220', '1');
INSERT INTO `c_zclx` VALUES ('69', '17', '05', null, '电子设备', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/8b128703-12b9-4cce-8673-64492f9b6b2e', '1');
INSERT INTO `c_zclx` VALUES ('70', '17', '04070706', '04', '去湿机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/548cdde3-1538-47ab-b149-e04032ab4e15', '1');
INSERT INTO `c_zclx` VALUES ('71', '17', '05010104', '05', '服务器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/d58425b4-eb22-4761-bc3c-6dd526ec687f', '1');
INSERT INTO `c_zclx` VALUES ('72', '17', '05010105', '05', '微型电子计算机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/818d2763-7f4b-420f-97ee-277027b1cdca', '1');
INSERT INTO `c_zclx` VALUES ('73', '17', '05010106', '05', '单板机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/72f8012c-c84a-416b-8a41-e3e2b5a575ed', '1');
INSERT INTO `c_zclx` VALUES ('74', '17', '05010109', '05', '小型工作站计算机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/acf2dc8b-8058-4164-b050-4b46e47afc74', '1');
INSERT INTO `c_zclx` VALUES ('75', '17', '05010403', '05', '电子词典', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/3271c10b-f65a-4f8c-8600-ee410328e65b', '1');
INSERT INTO `c_zclx` VALUES ('76', '17', '05010404', '05', '电脑记事卡', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/56924db5-8804-4ecc-a20c-04656b4d8e7d', '1');
INSERT INTO `c_zclx` VALUES ('77', '17', '05010501', '05', '打印机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/e90f38f9-812b-4655-b5b9-93704c5ab21b', '1');
INSERT INTO `c_zclx` VALUES ('78', '17', '05010502', '05', '显示器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/bfe65e8f-62b3-4754-af70-86a0f4665d5c', '1');
INSERT INTO `c_zclx` VALUES ('79', '17', '05010539', '05', '控制装置', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/20b147dd-f084-4c90-8b3d-501f5a0b72b8', '1');
INSERT INTO `c_zclx` VALUES ('80', '17', '05010547', '05', '图像采集系统', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/d97a1200-b145-43c5-b2b8-a03a66c103d9', '1');
INSERT INTO `c_zclx` VALUES ('81', '17', '05010549', '05', '激光打印机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/e995c08b-fb27-4fa4-bcfe-db96b2146994', '1');
INSERT INTO `c_zclx` VALUES ('82', '17', '05010550', '05', '扫描仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/9b7eb6f6-342f-4d09-862e-4105ef1fae66', '1');
INSERT INTO `c_zclx` VALUES ('83', '17', '05010559', '05', '条码阅读器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/f78b815a-0561-4723-beec-015fe28b92b7', '1');
INSERT INTO `c_zclx` VALUES ('84', '17', '05010562', '05', '光盘刻录机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/01241822-12e8-4619-8624-b2a8889f4d75', '1');
INSERT INTO `c_zclx` VALUES ('85', '17', '05010601', '05', '单片机开发系统', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/13622342-257a-47b3-a56c-e1bdc3b985a4', '1');
INSERT INTO `c_zclx` VALUES ('86', '17', '05010901', '05', '集线器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/696a99e9-a80c-4da5-8049-55aafbc94eac', '1');
INSERT INTO `c_zclx` VALUES ('87', '17', '05010902', '05', '桥－路由器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/f16905e7-01d0-41c4-b425-39432e3624fe', '1');
INSERT INTO `c_zclx` VALUES ('88', '17', '05010903', '05', '互联网接入设备', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/1443ee76-b57c-4a34-81fb-d09df6d220a9', '1');
INSERT INTO `c_zclx` VALUES ('89', '17', '05010904', '05', '交换机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/013f9f34-2677-4214-a2fa-b7d2ef204f44', '1');
INSERT INTO `c_zclx` VALUES ('90', '17', '05010906', '05', '线性放大模块', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/42e9d91a-ac98-4f96-a273-441678ee47b5', '1');
INSERT INTO `c_zclx` VALUES ('91', '17', '05010907', '05', 'SCANNER网络测试仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/40d09784-b09b-48df-ba76-8c1317ae7134', '1');
INSERT INTO `c_zclx` VALUES ('92', '17', '05010908', '05', '光缆转接器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/c0ffdf9e-11ed-496c-b3ff-58007e6c68e3', '1');
INSERT INTO `c_zclx` VALUES ('93', '17', '05010909', '05', '光缆放大器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/02795538-ec5d-4925-96a3-71f71a195f4f', '1');
INSERT INTO `c_zclx` VALUES ('94', '17', '05010910', '05', '光纤转换器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/93493e52-5bd9-4c76-87aa-5970bdd14b23', '1');
INSERT INTO `c_zclx` VALUES ('95', '17', '05010919', '05', '快速以太网交换模块', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/64536f5b-a32b-4344-999d-3645ae02dd6e', '1');
INSERT INTO `c_zclx` VALUES ('96', '17', '05010920', '05', '室内网络线路系统', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/ebfdd069-fc27-4bab-94af-6b1051525bb8', '1');
INSERT INTO `c_zclx` VALUES ('97', '17', '05020501', '05', '黑白录像机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/61e44940-d7ba-443c-a1d5-ec834a2b8a4b', '1');
INSERT INTO `c_zclx` VALUES ('98', '17', '05020502', '05', '彩色录像机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/733d85f0-bbd8-45e0-afdc-924ab06e9d29', '1');
INSERT INTO `c_zclx` VALUES ('99', '17', '05020601', '05', '黑白监视器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/d8b78837-0272-44c7-a35c-46c6ddd15a20', '1');
INSERT INTO `c_zclx` VALUES ('100', '17', '05020602', '05', '彩色监视器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/2ec9a75a-ff31-4851-b1a4-0161ab78f76a', '1');
INSERT INTO `c_zclx` VALUES ('101', '17', '05020701', '05', '便携式摄像机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/eadaee7c-6b2d-49dd-851a-83c8afd4d629', '1');
INSERT INTO `c_zclx` VALUES ('102', '17', '05020714', '05', '编缉机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/1664c2eb-210d-464b-9283-f8123e45bc9e', '1');
INSERT INTO `c_zclx` VALUES ('103', '17', '05020717', '05', '电视分配器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/b78e4276-ce02-41d9-96a1-0a596fcac22b', '1');
INSERT INTO `c_zclx` VALUES ('104', '17', '05020721', '05', '电视摄像效果镜', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/f66358cc-e664-438d-9e63-916c112643af', '1');
INSERT INTO `c_zclx` VALUES ('105', '17', '05020723', '05', '信号分配器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/62e83514-ccd1-49ac-bda2-597e295c08c0', '1');
INSERT INTO `c_zclx` VALUES ('106', '17', '05020726', '05', '混响器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/f74ef96e-ece7-462f-859c-283d3dec73ae', '1');
INSERT INTO `c_zclx` VALUES ('107', '17', '05020732', '05', '视频音频选择器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/b25b5314-73d9-4102-b84c-2662aad28437', '1');
INSERT INTO `c_zclx` VALUES ('108', '17', '05020739', '05', '计算机视频特技器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/68d20c38-2a11-4510-8ed4-5b89bf5d12c3', '1');
INSERT INTO `c_zclx` VALUES ('109', '17', '05020742', '05', '电子逆程切换器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/7652e92d-f5b1-44ee-9b08-7e3c05c94ff2', '1');
INSERT INTO `c_zclx` VALUES ('110', '17', '05020743', '05', '视频监测仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/d6acd167-d4ef-49ad-9814-f6781fd88757', '1');
INSERT INTO `c_zclx` VALUES ('111', '17', '05020744', '05', '混影台', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/f3cea0d5-f5a2-432a-a2b6-45e20371e4f7', '1');
INSERT INTO `c_zclx` VALUES ('112', '17', '05020771', '05', '视频图象展示台', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/5c0757e8-a8fa-4434-9937-0f4014b6e727', '1');
INSERT INTO `c_zclx` VALUES ('113', '17', '05020901', '05', '无线话筒', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/723638e4-5d08-4ac7-b694-3ba518bd2afa', '1');
INSERT INTO `c_zclx` VALUES ('114', '17', '05020902', '05', '调音台', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/2962493c-ec32-4701-a9e2-7d2a3747646e', '1');
INSERT INTO `c_zclx` VALUES ('115', '17', '05020903', '05', '播出控制器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/868f00fd-ccd1-40b6-92e5-a38666f7536a', '1');
INSERT INTO `c_zclx` VALUES ('116', '17', '05020910', '05', '有线话筒', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/ec3ac2d8-0f3a-4195-864a-17457164582f', '1');
INSERT INTO `c_zclx` VALUES ('117', '17', '05030212', '05', '电信传真机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/1e315aef-69d4-493a-b2dd-715f58556146', '1');
INSERT INTO `c_zclx` VALUES ('118', '17', '05030822', '05', '天线', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/1c6606dd-b147-4e7a-9760-c137686a60ed', '1');
INSERT INTO `c_zclx` VALUES ('119', '17', '05031020', '05', '均衡器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/df322567-1416-478d-883f-2a9fc2c2fc9b', '1');
INSERT INTO `c_zclx` VALUES ('120', '17', '05031021', '05', '接收机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/db7736db-56b7-4b79-97fa-f7153ff2d688', '1');
INSERT INTO `c_zclx` VALUES ('121', '17', '05031024', '05', 'GPS基准定位仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/49090854-de51-4595-aac1-6a633eae3afd', '1');
INSERT INTO `c_zclx` VALUES ('122', '17', '05031509', '05', '手机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/3c75bc34-6384-483d-ad62-0290f0138552', '1');
INSERT INTO `c_zclx` VALUES ('123', '17', '05040201', '05', '录放机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/6ea9490e-f430-4323-854c-4e9a02231e0a', '1');
INSERT INTO `c_zclx` VALUES ('124', '17', '05040202', '05', '收录两用机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/86393b46-bb43-45d1-aac7-716a1544b1d1', '1');
INSERT INTO `c_zclx` VALUES ('125', '17', '05040208', '05', 'DAT数字录音机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/413dc366-7fae-4857-ad82-3934ad23b58e', '1');
INSERT INTO `c_zclx` VALUES ('126', '17', '05040301', '05', '主控机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/e5418d43-0627-4f6d-ba96-3cbec086e313', '1');
INSERT INTO `c_zclx` VALUES ('127', '17', '05040302', '05', '触摸屏', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/cefcd25b-78dc-4786-83de-d84a1515e8c3', '1');
INSERT INTO `c_zclx` VALUES ('128', '17', '05040303', '05', '幕布(玻珠幕、金属幕)', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/2bff92a9-f1a0-427f-8b50-38d517d0f8f5', '1');
INSERT INTO `c_zclx` VALUES ('129', '17', '05040304', '05', '其它多媒体设备', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/d0d962ec-7aca-4e7a-97f9-85af26261158', '1');
INSERT INTO `c_zclx` VALUES ('130', '17', '05040408', '05', '立体扩音机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/a543ab7a-3a3f-48ef-ba63-839e8def473e', '1');
INSERT INTO `c_zclx` VALUES ('131', '17', '05040501', '05', '音箱', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/f7b5f98f-10d0-4b5f-9d6f-4c7366dec3dd', '1');
INSERT INTO `c_zclx` VALUES ('132', '17', '05040503', '05', '音响组合', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/c7d1ac19-16f8-4736-b37a-bf49ea6f873c', '1');
INSERT INTO `c_zclx` VALUES ('133', '17', '05040506', '05', '卡拉OK机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/1ba0c3a0-902c-42e3-b4cb-73aa6eff6c6c', '1');
INSERT INTO `c_zclx` VALUES ('134', '17', '05040507', '05', '影碟机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/71a702e3-10ed-47d1-a688-b77e5be8809f', '1');
INSERT INTO `c_zclx` VALUES ('135', '17', '05040601', '05', '黑白电视机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/234a2f33-676f-4645-9766-191f9e139bc4', '1');
INSERT INTO `c_zclx` VALUES ('136', '17', '05040602', '05', '彩色电视机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/c0c4a70a-d892-4061-b857-1bf0cb29746f', '1');
INSERT INTO `c_zclx` VALUES ('137', '17', '05040801', '05', '语言实验室成套设备', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/b2b77026-1965-443a-9406-597817fe8dec', '1');
INSERT INTO `c_zclx` VALUES ('138', '17', '05080120', '05', '超声波清洗机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/da3a4f71-944e-484b-b14a-7c46ceee7dec', '1');
INSERT INTO `c_zclx` VALUES ('139', '17', '06', null, '印刷机械', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/daccb8cf-8ecd-42b2-9e5b-819cace8071a', '1');
INSERT INTO `c_zclx` VALUES ('140', '17', '06010410', '06', '碎纸机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/b64bec28-46e8-46e2-8d3a-cdd4fad69421', '1');
INSERT INTO `c_zclx` VALUES ('141', '17', '06020101', '06', '复印机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/00a1ade9-5104-419c-9f59-0f52024ae379', '1');
INSERT INTO `c_zclx` VALUES ('142', '17', '14', null, '行政办公设备', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/77b22dc1-7a14-4444-8a58-3dc2046e4d1d', '1');
INSERT INTO `c_zclx` VALUES ('143', '17', '14010103', '14', '烟雾报警器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/aa4c3c22-4a41-4561-915f-5181ca15657d', '1');
INSERT INTO `c_zclx` VALUES ('144', '17', '14010104', '14', '火灾报警器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/dda178b3-5fda-40f2-b5be-16baf0ecbc3d', '1');
INSERT INTO `c_zclx` VALUES ('145', '17', '14010105', '14', '感烟探测器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/279f6c94-415d-4876-8b01-617993842ad8', '1');
INSERT INTO `c_zclx` VALUES ('146', '17', '14010106', '14', '防盗警报器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/94dd680d-35a3-4267-8d1f-eb5682ef29f2', '1');
INSERT INTO `c_zclx` VALUES ('147', '17', '14010107', '14', '图书监测仪', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/469bd182-986e-49d6-8f88-bf1ba7f9dd84', '1');
INSERT INTO `c_zclx` VALUES ('148', '17', '14010108', '14', '报警器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/744d4b31-4fdb-4b91-a0e3-52a65dba870e', '1');
INSERT INTO `c_zclx` VALUES ('149', '17', '14010109', '14', '保险柜(箱)', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/08d580e2-f561-40f2-90c7-640a8f716a2a', '1');
INSERT INTO `c_zclx` VALUES ('150', '17', '14030102', '14', '吸尘器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/1e91162e-e3f8-4b6b-bb8a-6917142aa661', '1');
INSERT INTO `c_zclx` VALUES ('151', '17', '14040101', '14', '热风器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/401a0c45-6c0e-4c59-91ba-cc6d9f5511c8', '1');
INSERT INTO `c_zclx` VALUES ('152', '17', '14040102', '14', '取暖电炉', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/83913ae9-bd1b-4abe-8bd8-99ce7f679c03', '1');
INSERT INTO `c_zclx` VALUES ('153', '17', '14040103', '14', '电加热器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/7972388e-7a94-4bf8-bf73-aea0825851df', '1');
INSERT INTO `c_zclx` VALUES ('154', '17', '14040104', '14', '远红外加热器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/5a49dc7a-7f3a-40db-b527-c6ef14ca1538', '1');
INSERT INTO `c_zclx` VALUES ('155', '17', '14040105', '14', '浴水加热器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/b249ff93-fcd2-4e69-bd0c-58aa01cce2af', '1');
INSERT INTO `c_zclx` VALUES ('156', '17', '14040106', '14', '电暖器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/400c0e10-67b8-48c2-a93c-6fe7452a91ee', '1');
INSERT INTO `c_zclx` VALUES ('157', '17', '14040201', '14', '洗衣机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/b34ecd92-6531-49da-84c5-676fbf7c8533', '1');
INSERT INTO `c_zclx` VALUES ('158', '17', '14040202', '14', '甩干机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/2c11e7b5-ea54-4135-948a-00a3288f7563', '1');
INSERT INTO `c_zclx` VALUES ('159', '17', '14040203', '14', '烘干机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/8b79881b-437a-4df0-92dd-6c648fad2109', '1');
INSERT INTO `c_zclx` VALUES ('160', '17', '14040303', '14', '电热开水器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/d31091c5-d878-4d1f-a1e6-7f315ad4a1ec', '1');
INSERT INTO `c_zclx` VALUES ('161', '17', '14040304', '14', '饮水机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/850d8f0e-fe26-416a-a741-7a0b3878a76b', '1');
INSERT INTO `c_zclx` VALUES ('162', '17', '14040503', '14', '点钞票机', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/b11c91ee-0f2f-48d7-8fa0-e16c56ca390e', '1');
INSERT INTO `c_zclx` VALUES ('163', '17', '14040507', '14', '考勤机(考勤系统)', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/f4d136c9-a5f9-4fb2-88ef-585f2b4cdbee', '1');
INSERT INTO `c_zclx` VALUES ('164', '17', '14040512', '14', '表决器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/3539ff5a-d359-46d4-a60d-9114ccb8af1d', '1');
INSERT INTO `c_zclx` VALUES ('165', '17', '14040513', '14', '报到器', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/9343ec8c-4f0b-4fbc-b606-113f5a537f50', '1');
INSERT INTO `c_zclx` VALUES ('166', '17', '14040514', '14', '会议系统', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/cb30186d-75e2-4b94-95a4-52572c83f711', '1');
INSERT INTO `c_zclx` VALUES ('167', '17', '14040515', '14', '同声传译系统', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/761e6e31-5baa-48be-9a73-aed57162d7d4', '1');
INSERT INTO `c_zclx` VALUES ('168', '17', '15020206', '14', '白板', '3.0', '南京大学仪器设备分类表', '/upload/zclx_images/17/bdbd0ff7-fc5d-4c55-8355-da0c5cfb524c', '1');
INSERT INTO `c_zclx` VALUES ('169', '1', '001', null, '工作1', null, '', '/upload/zclx_images/1/11b92602-3616-4469-a926-5be15e355ff7', '1');
INSERT INTO `c_zclx` VALUES ('170', '1', '00100170', '001', '工作品种1', '3.0', '', '/upload/zclx_images/1/e6181ce9-3000-4948-9a2b-1f145dc30f53', '1');

-- ----------------------------
-- Table structure for j_rw
-- ----------------------------
DROP TABLE IF EXISTS `j_rw`;
CREATE TABLE `j_rw` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `LX` int(1) DEFAULT NULL COMMENT '日常巡检、故障维修',
  `KSSJ` datetime DEFAULT NULL,
  `JSSJ` datetime DEFAULT NULL,
  `CZR` varchar(30) DEFAULT NULL,
  `CZ_REMARK` varchar(200) DEFAULT NULL,
  `YSR` varchar(30) DEFAULT NULL,
  `YS_REMARK` varchar(200) DEFAULT NULL,
  `YSSJ` datetime DEFAULT NULL,
  `TOTAL` int(4) DEFAULT NULL,
  `FINISH_COUNT` int(4) DEFAULT '0',
  `FINISH` int(1) DEFAULT '0' COMMENT '完成（1），未完成（0）',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='采购、入库、领用、巡检、维修等';

-- ----------------------------
-- Records of j_rw
-- ----------------------------
INSERT INTO `j_rw` VALUES ('1', '17', '0', '2017-04-07 00:00:00', '2017-08-05 00:00:00', 'shangwei', '创建一个随机日常巡检任务', 'fuzuo', null, '2017-09-14 00:00:00', '14', '7', '0');
INSERT INTO `j_rw` VALUES ('2', '17', '0', '2017-02-28 00:00:00', '2017-08-27 00:00:00', 'shangwei', '创建一个随机日常巡检任务', 'baiweibing', null, null, '5', '5', '0');
INSERT INTO `j_rw` VALUES ('3', '17', '0', '2017-03-10 00:00:00', '2017-11-29 00:00:00', 'yangjing', '创建一个随机日常巡检任务', 'lirunfang', '日常巡检验收不通过', '2017-06-28 00:00:00', '5', '0', '1');
INSERT INTO `j_rw` VALUES ('4', '17', '0', '2017-01-07 00:00:00', '2017-12-23 00:00:00', 'XuQingWei', '更新日常巡检任务信息', 'shangwei', null, null, '5', '0', '0');
INSERT INTO `j_rw` VALUES ('5', '17', '0', '2017-04-11 00:00:00', '2017-11-04 00:00:00', 'baiweibing', '更新日常巡检任务信息', 'shangwei', '日常巡检验收不通过', '2017-06-28 00:00:00', '5', '0', '1');
INSERT INTO `j_rw` VALUES ('6', '17', '0', '2017-05-10 00:00:00', '2017-11-20 00:00:00', 'WenChen', '更新日常巡检任务信息', 'pengxiao', null, '2018-01-01 00:00:00', '5', '0', '0');
INSERT INTO `j_rw` VALUES ('7', '17', '0', '2017-05-02 00:00:00', '2017-09-30 00:00:00', 'yangjing', '更新日常巡检任务信息', 'shangwei', null, '2018-01-01 00:00:00', '5', '0', '0');
INSERT INTO `j_rw` VALUES ('8', '17', '0', '2017-01-10 00:00:00', '2017-07-31 00:00:00', 'lirunfang', '更新日常巡检任务信息', 'shangwei', null, '2018-01-01 00:00:00', '5', '0', '0');
INSERT INTO `j_rw` VALUES ('9', '17', '0', '2017-04-11 00:00:00', '2017-10-27 00:00:00', 'XuQingWei', '更新日常巡检任务信息', 'yangjing', '日常巡检验收不通过', '2017-07-04 00:00:00', '10', '0', '1');
INSERT INTO `j_rw` VALUES ('10', '17', '0', '2017-02-26 00:00:00', '2017-12-30 00:00:00', 'baiweibing', '更新日常巡检任务信息', 'yangjing', '日常巡检验收不通过', '2017-07-04 00:00:00', '5', '0', '1');
INSERT INTO `j_rw` VALUES ('11', '17', '0', '2017-02-24 00:00:00', '2017-11-23 00:00:00', 'lirunfang', '更新日常巡检任务信息', 'yangjing', '日常巡检验收不通过', '2017-07-04 00:00:00', '5', '0', '1');
INSERT INTO `j_rw` VALUES ('12', '17', '0', '2017-06-02 00:00:00', '2017-07-18 00:00:00', 'baiweibing', '更新日常巡检任务信息', 'baiweibing', '日常巡检验收不通过', '2017-07-04 00:00:00', '5', '0', '1');
INSERT INTO `j_rw` VALUES ('13', '17', '0', '2017-06-03 00:00:00', '2017-09-25 00:00:00', 'XuQingWei', '更新日常巡检任务信息', 'baiweibing', '日常巡检验收不通过', '2017-07-04 00:00:00', '5', '0', '1');
INSERT INTO `j_rw` VALUES ('14', '17', '0', '2017-07-19 00:00:00', '2017-07-20 00:00:00', 'yangjing', null, 'XuQingWei', null, '2017-07-21 00:00:00', '5', '0', '0');
INSERT INTO `j_rw` VALUES ('15', '17', '0', '2017-08-11 00:00:00', '2017-08-12 00:00:00', 'shangwei', null, 'XuQingWei', null, '2017-08-13 00:00:00', '2', '2', '0');

-- ----------------------------
-- Table structure for j_rwxz
-- ----------------------------
DROP TABLE IF EXISTS `j_rwxz`;
CREATE TABLE `j_rwxz` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `RWID` int(11) DEFAULT NULL,
  `ZCID` int(11) DEFAULT NULL,
  `FINISH` int(1) DEFAULT '0' COMMENT '完成（1），未完成（0）',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of j_rwxz
-- ----------------------------
INSERT INTO `j_rwxz` VALUES ('1', '1', '1', '1');
INSERT INTO `j_rwxz` VALUES ('2', '1', '9', '1');
INSERT INTO `j_rwxz` VALUES ('3', '1', '27', '1');
INSERT INTO `j_rwxz` VALUES ('4', '1', '32', '1');
INSERT INTO `j_rwxz` VALUES ('5', '1', '35', '1');
INSERT INTO `j_rwxz` VALUES ('6', '1', '50', '1');
INSERT INTO `j_rwxz` VALUES ('7', '1', '60', '1');
INSERT INTO `j_rwxz` VALUES ('8', '1', '82', '0');
INSERT INTO `j_rwxz` VALUES ('9', '1', '90', '0');
INSERT INTO `j_rwxz` VALUES ('10', '1', '101', '0');
INSERT INTO `j_rwxz` VALUES ('11', '1', '102', '0');
INSERT INTO `j_rwxz` VALUES ('12', '1', '103', '0');
INSERT INTO `j_rwxz` VALUES ('13', '1', '104', '0');
INSERT INTO `j_rwxz` VALUES ('14', '1', '105', '0');
INSERT INTO `j_rwxz` VALUES ('15', '2', '106', '1');
INSERT INTO `j_rwxz` VALUES ('16', '2', '107', '1');
INSERT INTO `j_rwxz` VALUES ('17', '2', '108', '1');
INSERT INTO `j_rwxz` VALUES ('18', '2', '109', '1');
INSERT INTO `j_rwxz` VALUES ('19', '2', '110', '1');
INSERT INTO `j_rwxz` VALUES ('20', '3', '111', '0');
INSERT INTO `j_rwxz` VALUES ('21', '3', '112', '0');
INSERT INTO `j_rwxz` VALUES ('22', '3', '113', '0');
INSERT INTO `j_rwxz` VALUES ('23', '3', '114', '0');
INSERT INTO `j_rwxz` VALUES ('24', '3', '115', '0');
INSERT INTO `j_rwxz` VALUES ('25', '4', '116', '0');
INSERT INTO `j_rwxz` VALUES ('26', '4', '117', '0');
INSERT INTO `j_rwxz` VALUES ('27', '4', '118', '0');
INSERT INTO `j_rwxz` VALUES ('28', '4', '119', '0');
INSERT INTO `j_rwxz` VALUES ('29', '4', '120', '0');
INSERT INTO `j_rwxz` VALUES ('30', '5', '121', '0');
INSERT INTO `j_rwxz` VALUES ('31', '5', '122', '0');
INSERT INTO `j_rwxz` VALUES ('32', '5', '123', '0');
INSERT INTO `j_rwxz` VALUES ('33', '5', '124', '0');
INSERT INTO `j_rwxz` VALUES ('34', '5', '125', '0');
INSERT INTO `j_rwxz` VALUES ('35', '6', '126', '0');
INSERT INTO `j_rwxz` VALUES ('36', '6', '127', '0');
INSERT INTO `j_rwxz` VALUES ('37', '6', '128', '0');
INSERT INTO `j_rwxz` VALUES ('38', '6', '129', '0');
INSERT INTO `j_rwxz` VALUES ('39', '6', '130', '0');
INSERT INTO `j_rwxz` VALUES ('40', '7', '131', '0');
INSERT INTO `j_rwxz` VALUES ('41', '7', '132', '0');
INSERT INTO `j_rwxz` VALUES ('42', '7', '133', '0');
INSERT INTO `j_rwxz` VALUES ('43', '7', '134', '0');
INSERT INTO `j_rwxz` VALUES ('44', '7', '135', '0');
INSERT INTO `j_rwxz` VALUES ('45', '8', '136', '0');
INSERT INTO `j_rwxz` VALUES ('46', '8', '137', '0');
INSERT INTO `j_rwxz` VALUES ('47', '8', '138', '0');
INSERT INTO `j_rwxz` VALUES ('48', '8', '139', '0');
INSERT INTO `j_rwxz` VALUES ('49', '8', '140', '0');
INSERT INTO `j_rwxz` VALUES ('50', '9', '141', '0');
INSERT INTO `j_rwxz` VALUES ('51', '9', '142', '0');
INSERT INTO `j_rwxz` VALUES ('52', '9', '143', '0');
INSERT INTO `j_rwxz` VALUES ('53', '9', '144', '0');
INSERT INTO `j_rwxz` VALUES ('54', '9', '145', '0');
INSERT INTO `j_rwxz` VALUES ('55', '9', '146', '0');
INSERT INTO `j_rwxz` VALUES ('56', '9', '147', '0');
INSERT INTO `j_rwxz` VALUES ('57', '9', '148', '0');
INSERT INTO `j_rwxz` VALUES ('58', '9', '149', '0');
INSERT INTO `j_rwxz` VALUES ('59', '9', '150', '0');
INSERT INTO `j_rwxz` VALUES ('60', '10', '151', '0');
INSERT INTO `j_rwxz` VALUES ('61', '10', '152', '0');
INSERT INTO `j_rwxz` VALUES ('62', '10', '153', '0');
INSERT INTO `j_rwxz` VALUES ('63', '10', '154', '0');
INSERT INTO `j_rwxz` VALUES ('64', '10', '155', '0');
INSERT INTO `j_rwxz` VALUES ('65', '11', '156', '0');
INSERT INTO `j_rwxz` VALUES ('66', '11', '157', '0');
INSERT INTO `j_rwxz` VALUES ('67', '11', '158', '0');
INSERT INTO `j_rwxz` VALUES ('68', '11', '159', '0');
INSERT INTO `j_rwxz` VALUES ('69', '11', '160', '0');
INSERT INTO `j_rwxz` VALUES ('70', '12', '161', '0');
INSERT INTO `j_rwxz` VALUES ('71', '12', '162', '0');
INSERT INTO `j_rwxz` VALUES ('72', '12', '163', '0');
INSERT INTO `j_rwxz` VALUES ('73', '12', '164', '0');
INSERT INTO `j_rwxz` VALUES ('74', '12', '165', '0');
INSERT INTO `j_rwxz` VALUES ('75', '13', '166', '0');
INSERT INTO `j_rwxz` VALUES ('76', '13', '167', '0');
INSERT INTO `j_rwxz` VALUES ('77', '13', '168', '0');
INSERT INTO `j_rwxz` VALUES ('78', '13', '169', '0');
INSERT INTO `j_rwxz` VALUES ('79', '13', '170', '0');
INSERT INTO `j_rwxz` VALUES ('80', '14', '12', '0');
INSERT INTO `j_rwxz` VALUES ('81', '14', '19', '0');
INSERT INTO `j_rwxz` VALUES ('82', '14', '106', '0');
INSERT INTO `j_rwxz` VALUES ('83', '14', '107', '0');
INSERT INTO `j_rwxz` VALUES ('84', '14', '108', '0');
INSERT INTO `j_rwxz` VALUES ('85', '15', '1', '1');
INSERT INTO `j_rwxz` VALUES ('86', '15', '9', '1');

-- ----------------------------
-- Table structure for j_zc
-- ----------------------------
DROP TABLE IF EXISTS `j_zc`;
CREATE TABLE `j_zc` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `DM` varchar(36) NOT NULL COMMENT '有两种方式。如果用户不提供资产编号，系统使用UUID方式自动生成。资产编号是资产二维码的生成依据',
  `MC` varchar(100) DEFAULT NULL,
  `LX_ID` int(11) DEFAULT NULL COMMENT '依据南京大学仪器设备常用分类号设计',
  `XH` varchar(100) DEFAULT NULL,
  `CCBH` varchar(50) DEFAULT NULL,
  `COST` decimal(9,2) NOT NULL,
  `NUM` int(5) DEFAULT '1',
  `CFDD` varchar(200) DEFAULT NULL COMMENT '可以是建筑物、楼层、房间号等。以JSON方式存放。',
  `SYR` varchar(30) DEFAULT NULL,
  `ZCGL_ID` int(11) DEFAULT NULL COMMENT '与企业微信号中的组织架构保持一致',
  `PIC_URL` varchar(100) DEFAULT NULL COMMENT '资产照片',
  `IMG_VERSION` int(5) DEFAULT '0' COMMENT '图片版本号',
  `GZSJ` date DEFAULT NULL,
  `ZJNX` decimal(3,1) DEFAULT NULL COMMENT '单位年',
  `ZT` int(2) DEFAULT '0' COMMENT '未使用，使用中、维修中、闲置、申请维修、申请报废、申请闲置、巡检中、报废',
  `TYBZ` int(1) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of j_zc
-- ----------------------------
INSERT INTO `j_zc` VALUES ('1', '17', '00001', '资产00001', '64', '00012', null, '0.00', '5', '2', 'shangwei', '2', null, '0', '2017-10-11', '2.0', '1', '0');
INSERT INTO `j_zc` VALUES ('2', '17', '00002', '资产00002', '132', '00013', null, '0.00', '7', '3', 'baiweibing', '2', null, '0', '2017-03-10', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('3', '17', '00003', '资产00003', '123', '00014', null, '0.00', '8', '4', 'baiweibing', '2', null, '0', '2017-07-02', '4.0', '11', '0');
INSERT INTO `j_zc` VALUES ('4', '17', '00004', '资产00004', '66', '00015', null, '0.00', '9', '1', 'WenChen', '2', null, '0', '2017-08-27', '1.0', '4', '0');
INSERT INTO `j_zc` VALUES ('5', '17', '00005', '资产00005', '90', '00016', null, '0.00', '10', '2', 'lirunfang', '1', null, '0', '2017-07-09', '2.0', '4', '0');
INSERT INTO `j_zc` VALUES ('6', '17', '00006', '资产00006', '104', '00017', null, '0.00', '7', '3', 'WenChen', '2', null, '0', '2017-11-26', '1.0', '12', '0');
INSERT INTO `j_zc` VALUES ('7', '17', '00007', '资产00007', '14', '00018', null, '0.00', '3', '4', 'XuQingWei', '1', null, '0', '2017-06-14', '4.0', '2', '0');
INSERT INTO `j_zc` VALUES ('8', '17', '00008', '资产00008', '145', '00019', null, '0.00', '1', '1', 'shangwei', '1', null, '0', '2012-08-16', '1.0', '11', '0');
INSERT INTO `j_zc` VALUES ('9', '17', '00009', '资产00009', '94', '00020', null, '0.00', '1', '2', 'baiweibing', '2', null, '0', '2000-10-26', '4.0', '5', '0');
INSERT INTO `j_zc` VALUES ('10', '17', '00010', '资产00010', '111', '00021', null, '0.00', '1', '3', 'XuQingWei', '2', null, '0', '2017-01-11', '3.0', '11', '0');
INSERT INTO `j_zc` VALUES ('11', '17', '00011', '资产00011', '108', '00022', null, '0.00', '9', '4', 'baiweibing', '1', null, '0', '2017-04-26', '2.0', '5', '0');
INSERT INTO `j_zc` VALUES ('12', '17', '00012', '资产00012', '19', '00023', null, '0.00', '9', '1', 'WenChen', '2', null, '0', '2017-01-05', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('13', '17', '00013', '资产00013', '168', '00024', null, '0.00', '2', '2', 'pengxiao', '1', null, '0', '2017-02-02', '5.0', '10', '0');
INSERT INTO `j_zc` VALUES ('14', '17', '00014', '资产00014', '14', '00025', null, '0.00', '7', '3', 'baiweibing', '2', null, '0', '2017-07-26', '1.0', '5', '0');
INSERT INTO `j_zc` VALUES ('15', '17', '00015', '资产00015', '156', '00026', null, '0.00', '3', '4', 'XuQingWei', '2', null, '0', '2017-05-14', '1.0', '6', '0');
INSERT INTO `j_zc` VALUES ('16', '17', '00016', '资产00016', '7', '00027', null, '0.00', '9', '1', 'WenChen', '1', null, '0', '2017-11-21', '1.0', '5', '0');
INSERT INTO `j_zc` VALUES ('17', '17', '00017', '资产00017', '68', '00028', null, '0.00', '7', '2', 'XuQingWei', '1', null, '0', '2017-06-26', '4.0', '2', '0');
INSERT INTO `j_zc` VALUES ('18', '17', '00018', '资产00018', '10', '00029', null, '0.00', '2', '3', 'baiweibing', '2', null, '0', '2017-09-19', '2.0', '6', '0');
INSERT INTO `j_zc` VALUES ('19', '17', '00019', '资产00019', '168', '00030', null, '0.00', '7', '4', 'pengxiao', '2', null, '0', '2017-12-01', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('20', '17', '00020', '资产00020', '48', '00031', null, '0.00', '2', '1', 'WenChen', '1', null, '0', '2016-12-15', '2.0', '3', '0');
INSERT INTO `j_zc` VALUES ('21', '17', '00021', '资产00021', '57', '00032', null, '0.00', '2', '2', 'yangjing', '2', null, '0', '2017-01-18', '1.0', '8', '0');
INSERT INTO `j_zc` VALUES ('22', '17', '00022', '资产00022', '1', '00033', null, '0.00', '6', '3', 'XuQingWei', '2', null, '0', '2017-03-09', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('23', '17', '00023', '资产00023', '61', '00034', null, '0.00', '10', '4', 'baiweibing', '1', null, '0', '2017-10-06', '1.0', '5', '0');
INSERT INTO `j_zc` VALUES ('24', '17', '00024', '资产00024', '115', '00035', null, '0.00', '9', '1', 'baiweibing', '1', null, '0', '2017-09-27', '3.0', '2', '0');
INSERT INTO `j_zc` VALUES ('25', '17', '00025', '资产00025', '96', '00036', null, '0.00', '1', '2', 'lirunfang', '1', null, '0', '2015-07-18', '4.0', '5', '0');
INSERT INTO `j_zc` VALUES ('26', '17', '00026', '资产00026', '69', '00037', null, '0.00', '6', '3', 'WenChen', '2', null, '0', '2017-06-10', '1.0', '8', '0');
INSERT INTO `j_zc` VALUES ('27', '17', '00027', '资产00027', '80', '00038', null, '0.00', '8', '4', 'yangjing', '1', null, '0', '2017-01-15', '1.0', '1', '0');
INSERT INTO `j_zc` VALUES ('28', '17', '00028', '资产00028', '125', '00039', null, '0.00', '5', '1', 'shangwei', '2', null, '0', '2017-01-21', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('29', '17', '00029', '资产00029', '60', '00040', null, '0.00', '5', '2', 'XuQingWei', '3', null, '0', '2017-06-14', '4.0', '11', '0');
INSERT INTO `j_zc` VALUES ('30', '17', '00030', '资产00030', '11', '00041', null, '0.00', '5', '3', 'lirunfang', '1', null, '0', '2014-04-26', '5.0', '4', '0');
INSERT INTO `j_zc` VALUES ('31', '17', '00031', '资产00031', '129', '00042', null, '0.00', '10', '4', 'baiweibing', '2', null, '0', '2017-09-19', '1.0', '11', '0');
INSERT INTO `j_zc` VALUES ('32', '17', '00032', '资产00032', '88', '00043', null, '0.00', '1', '1', 'lirunfang', '1', null, '0', '2015-06-09', '2.0', '1', '0');
INSERT INTO `j_zc` VALUES ('33', '17', '00033', '资产00033', '82', '00044', null, '0.00', '5', '2', 'lirunfang', '19', null, '0', '2017-10-15', '3.0', '0', '0');
INSERT INTO `j_zc` VALUES ('34', '17', '00034', '资产00034', '126', '00045', null, '0.00', '5', '3', 'pengxiao', '2', null, '0', '2017-01-30', '3.0', '2', '0');
INSERT INTO `j_zc` VALUES ('35', '17', '00035', '资产00035', '147', '00046', null, '0.00', '4', '4', 'baiweibing', '2', null, '0', '2017-11-06', '3.0', '1', '0');
INSERT INTO `j_zc` VALUES ('36', '17', '00036', '资产00036', '93', '00047', null, '0.00', '4', '1', 'pengxiao', '1', null, '0', '2017-03-17', '2.0', '4', '0');
INSERT INTO `j_zc` VALUES ('37', '17', '00037', '资产00037', '105', '00048', null, '0.00', '8', '2', 'lirunfang', '14', null, '0', '2017-03-04', '5.0', '0', '0');
INSERT INTO `j_zc` VALUES ('38', '17', '00038', '资产00038', '32', '00049', null, '0.00', '1', '3', 'baiweibing', '2', null, '0', '2017-06-03', '5.0', '3', '0');
INSERT INTO `j_zc` VALUES ('39', '17', '00039', '资产00039', '113', '00050', null, '0.00', '6', '4', 'yangjing', '2', null, '0', '2017-02-21', '3.0', '6', '0');
INSERT INTO `j_zc` VALUES ('40', '17', '00040', '资产00040', '26', '00051', null, '0.00', '7', '1', 'XuQingWei', '2', null, '0', '2017-10-25', '1.0', '11', '0');
INSERT INTO `j_zc` VALUES ('41', '17', '00041', '资产00041', '42', '00052', null, '0.00', '3', '2', 'XuQingWei', '1', null, '0', '2017-03-02', '1.0', '11', '0');
INSERT INTO `j_zc` VALUES ('42', '17', '00042', '资产00042', '96', '00053', null, '0.00', '5', '3', 'XuQingWei', '14', null, '0', '2017-08-23', '2.0', '0', '0');
INSERT INTO `j_zc` VALUES ('43', '17', '00043', '资产00043', '32', '00054', null, '0.00', '4', '4', 'baiweibing', '1', null, '0', '2017-11-19', '3.0', '11', '0');
INSERT INTO `j_zc` VALUES ('44', '17', '00044', '资产00044', '100', '00055', null, '0.00', '10', '1', 'yangjing', '1', null, '0', '2017-01-17', '4.0', '6', '0');
INSERT INTO `j_zc` VALUES ('45', '17', '00045', '资产00045', '56', '00056', null, '0.00', '10', '2', 'WenChen', '14', null, '0', '2017-05-16', '5.0', '0', '0');
INSERT INTO `j_zc` VALUES ('46', '17', '00046', '资产00046', '3', '00057', null, '0.00', '9', '3', 'lirunfang', '1', null, '0', '2017-09-25', '2.0', '4', '0');
INSERT INTO `j_zc` VALUES ('47', '17', '00047', '资产00047', '162', '00058', null, '0.00', '6', '4', 'shangwei', '14', null, '0', '2017-07-06', '4.0', '0', '0');
INSERT INTO `j_zc` VALUES ('48', '17', '00048', '资产00048', '21', '00059', null, '0.00', '6', '1', 'shangwei', '2', null, '0', '2017-08-24', '2.0', '12', '0');
INSERT INTO `j_zc` VALUES ('49', '17', '00049', '资产00049', '95', '00060', null, '0.00', '2', '2', 'baiweibing', '1', null, '0', '2017-11-21', '2.0', '4', '0');
INSERT INTO `j_zc` VALUES ('50', '17', '00050', '资产00050', '79', '00061', null, '0.00', '3', '3', 'XuQingWei', '1', null, '0', '2017-10-28', '3.0', '1', '0');
INSERT INTO `j_zc` VALUES ('51', '17', '00051', '资产00051', '96', '00062', null, '0.00', '7', '4', 'XuQingWei', '2', null, '0', '2017-10-02', '3.0', '4', '0');
INSERT INTO `j_zc` VALUES ('52', '17', '00052', '资产00052', '109', '00063', null, '0.00', '4', '2', 'WenChen', '14', null, '0', '2017-01-26', '4.0', '0', '0');
INSERT INTO `j_zc` VALUES ('53', '17', '00053', '资产00053', '159', '00064', null, '0.00', '9', '2', 'yangjing', '1', null, '0', '2017-08-23', '5.0', '3', '0');
INSERT INTO `j_zc` VALUES ('54', '17', '00054', '资产00054', '153', '00065', null, '0.00', '8', '3', 'WenChen', '2', null, '0', '2017-01-21', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('55', '17', '00055', '资产00055', '29', '00066', '', '5.00', '7', '2', 'baiweibing', '2', '/upload/zc_images/17/f83c8e5b-96d8-4579-b98b-eb7b88449aa4', '2', '2017-05-13', '3.0', '0', '0');
INSERT INTO `j_zc` VALUES ('56', '17', '00056', '资产00056', '3', '00067', null, '0.00', '6', '1', 'pengxiao', '1', null, '0', '2017-01-05', '1.0', '8', '0');
INSERT INTO `j_zc` VALUES ('57', '17', '00057', '资产00057', '168', '00068', null, '0.00', '5', '2', 'baiweibing', '2', null, '0', '2017-12-26', '4.0', '6', '0');
INSERT INTO `j_zc` VALUES ('58', '17', '00058', '资产00058', '69', '00069', null, '0.00', '4', '3', 'XuQingWei', '2', null, '0', '2017-10-14', '4.0', '2', '0');
INSERT INTO `j_zc` VALUES ('59', '17', '00059', '资产00059', '153', '00070', null, '0.00', '3', '4', 'yangjing', '2', null, '0', '2017-09-01', '2.0', '2', '0');
INSERT INTO `j_zc` VALUES ('60', '17', '00060', '资产00060', '68', '00071', null, '0.00', '7', '1', 'WenChen', '2', null, '0', '2017-07-24', '2.0', '4', '0');
INSERT INTO `j_zc` VALUES ('61', '17', '00061', '资产00061', '118', '00072', null, '0.00', '9', '2', 'baiweibing', '2', null, '0', '2017-04-26', '1.0', '10', '0');
INSERT INTO `j_zc` VALUES ('62', '17', '00062', '资产00062', '20', '00073', null, '0.00', '3', '3', 'pengxiao', '2', null, '0', '2017-07-31', '2.0', '10', '0');
INSERT INTO `j_zc` VALUES ('63', '17', '00063', '资产00063', '90', '00074', null, '0.00', '8', '4', 'WenChen', '14', null, '0', '2017-08-15', '5.0', '0', '0');
INSERT INTO `j_zc` VALUES ('64', '17', '00064', '资产00064', '17', '00075', null, '0.00', '4', '1', 'pengxiao', '1', null, '0', '2017-11-26', '2.0', '11', '0');
INSERT INTO `j_zc` VALUES ('65', '17', '00065', '资产00065', '110', '00076', null, '0.00', '9', '2', 'shangwei', '1', null, '0', '2017-11-09', '5.0', '11', '0');
INSERT INTO `j_zc` VALUES ('66', '17', '00066', '资产00066', '14', '00077', null, '0.00', '8', '3', 'shangwei', '2', null, '0', '2017-06-08', '3.0', '6', '0');
INSERT INTO `j_zc` VALUES ('67', '17', '00067', '资产00067', '74', '00078', null, '0.00', '5', '4', 'shangwei', '1', null, '0', '2017-12-25', '1.0', '5', '0');
INSERT INTO `j_zc` VALUES ('68', '17', '00068', '资产00068', '148', '00079', null, '0.00', '3', '1', 'pengxiao', '1', null, '0', '2017-09-22', '1.0', '8', '0');
INSERT INTO `j_zc` VALUES ('69', '17', '00069', '资产00069', '94', '00080', null, '0.00', '2', '2', 'WenChen', null, null, '0', '2017-10-16', '5.0', '9', '0');
INSERT INTO `j_zc` VALUES ('70', '17', '00070', '资产00070', '89', '00081', null, '0.00', '1', '3', 'yangjing', null, null, '0', '2017-04-13', '3.0', '9', '0');
INSERT INTO `j_zc` VALUES ('71', '17', '00071', '资产00071', '15', '00082', null, '0.00', '10', '4', 'WenChen', '1', null, '0', '2017-03-19', '3.0', '11', '0');
INSERT INTO `j_zc` VALUES ('72', '17', '00072', '资产00072', '130', '00083', null, '0.00', '8', '1', 'lirunfang', null, null, '0', '2017-07-20', '4.0', '9', '0');
INSERT INTO `j_zc` VALUES ('73', '17', '00073', '资产00073', '15', '00084', null, '0.00', '6', '2', 'shangwei', '1', null, '0', '2017-11-01', '2.0', '2', '0');
INSERT INTO `j_zc` VALUES ('74', '17', '00074', '资产00074', '126', '00085', null, '0.00', '2', '3', 'lirunfang', null, null, '0', '2017-10-27', '4.0', '9', '0');
INSERT INTO `j_zc` VALUES ('75', '17', '00075', '资产00075', '149', '00086', null, '0.00', '10', '4', 'pengxiao', '1', null, '0', '2017-03-17', '3.0', '10', '0');
INSERT INTO `j_zc` VALUES ('76', '17', '00076', '资产00076', '81', '00087', null, '0.00', '6', '1', 'XuQingWei', '1', null, '0', '2017-08-25', '2.0', '3', '0');
INSERT INTO `j_zc` VALUES ('77', '17', '00077', '资产00077', '29', '00088', null, '0.00', '4', '2', 'lirunfang', null, null, '0', '2017-11-13', '1.0', '9', '0');
INSERT INTO `j_zc` VALUES ('78', '17', '00078', '资产00078', '113', '00089', null, '0.00', '5', '3', 'baiweibing', '2', null, '0', '2017-07-02', '4.0', '4', '0');
INSERT INTO `j_zc` VALUES ('79', '17', '00079', '资产00079', '140', '00090', null, '0.00', '5', '4', 'pengxiao', '1', null, '0', '2017-06-08', '1.0', '2', '0');
INSERT INTO `j_zc` VALUES ('80', '17', '00080', '资产00080', '105', '00091', null, '0.00', '9', '1', 'XuQingWei', '14', null, '0', '2017-03-21', '2.0', '0', '0');
INSERT INTO `j_zc` VALUES ('81', '17', '00081', '资产00081', '36', '00092', null, '0.00', '8', '2', 'WenChen', '2', null, '0', '2017-11-16', '5.0', '6', '0');
INSERT INTO `j_zc` VALUES ('82', '17', '00082', '资产00082', '88', '00093', null, '0.00', '5', '3', 'pengxiao', '2', null, '0', '2017-05-26', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('83', '17', '00083', '资产00083', '87', '00094', null, '0.00', '8', '4', 'WenChen', null, null, '0', '2017-03-31', '3.0', '9', '0');
INSERT INTO `j_zc` VALUES ('84', '17', '00084', '资产00084', '101', '00095', null, '0.00', '2', '1', 'XuQingWei', '2', null, '0', '2017-12-22', '2.0', '2', '0');
INSERT INTO `j_zc` VALUES ('85', '17', '00085', '资产00085', '51', '00096', null, '0.00', '8', '2', 'baiweibing', '1', null, '0', '2017-05-18', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('86', '17', '00086', '资产00086', '162', '00097', null, '0.00', '2', '3', 'yangjing', '1', null, '0', '2017-09-05', '1.0', '10', '0');
INSERT INTO `j_zc` VALUES ('87', '17', '00087', '资产00087', '129', '00098', null, '0.00', '9', '4', 'baiweibing', '1', null, '0', '2017-01-15', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('88', '17', '00088', '资产00088', '162', '00099', null, '0.00', '6', '1', 'yangjing', '2', null, '0', '2017-07-18', '5.0', '10', '0');
INSERT INTO `j_zc` VALUES ('89', '17', '00089', '资产00089', '115', '00100', null, '0.00', '10', '2', 'baiweibing', null, null, '0', '2017-10-13', '3.0', '9', '0');
INSERT INTO `j_zc` VALUES ('90', '17', '00090', '资产00090', '104', '00101', null, '0.00', '7', '3', 'XuQingWei', '2', null, '0', '2017-04-16', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('91', '17', '00091', '资产00091', '161', '00102', null, '0.00', '7', '4', 'WenChen', '1', null, '0', '2017-10-22', '5.0', '10', '0');
INSERT INTO `j_zc` VALUES ('92', '17', '00092', '资产00092', '46', '00103', null, '0.00', '10', '1', 'shangwei', '2', null, '0', '2017-05-30', '5.0', '5', '0');
INSERT INTO `j_zc` VALUES ('93', '17', '00093', '资产00093', '47', '00104', null, '0.00', '5', '2', 'XuQingWei', '2', null, '0', '2017-08-30', '2.0', '11', '0');
INSERT INTO `j_zc` VALUES ('94', '17', '00094', '资产00094', '126', '00105', null, '0.00', '9', '3', 'shangwei', '2', null, '0', '2017-06-17', '1.0', '5', '0');
INSERT INTO `j_zc` VALUES ('95', '17', '00095', '资产00095', '62', '00106', null, '0.00', '7', '4', 'XuQingWei', '2', null, '0', '2017-01-14', '3.0', '4', '0');
INSERT INTO `j_zc` VALUES ('96', '17', '00096', '资产00096', '145', '00107', null, '0.00', '7', '1', 'pengxiao', '2', null, '0', '2017-07-28', '2.0', '6', '0');
INSERT INTO `j_zc` VALUES ('97', '17', '00097', '资产00097', '20', '00108', null, '0.00', '8', '2', 'yangjing', '1', null, '0', '2017-08-17', '3.0', '2', '0');
INSERT INTO `j_zc` VALUES ('98', '17', '00098', '资产00098', '162', '00109', null, '0.00', '3', '3', 'baiweibing', '2', null, '0', '2017-10-21', '3.0', '10', '0');
INSERT INTO `j_zc` VALUES ('99', '17', '00099', '资产00099', '9', '00110', null, '0.00', '6', '4', 'shangwei', '2', null, '0', '2017-03-29', '3.0', '2', '0');
INSERT INTO `j_zc` VALUES ('100', '17', '00100', '资产00100', '165', '00111', null, '0.00', '8', '1', 'pengxiao', null, null, '0', '2017-06-13', '4.0', '9', '0');
INSERT INTO `j_zc` VALUES ('101', '17', '00101', '资产00101', '67', '00012', null, '0.00', '1', '2', 'pengxiao', '2', null, '0', '2017-12-14', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('102', '17', '00102', '资产00102', '71', '00013', null, '0.00', '1', '3', 'WenChen', '1', null, '0', '2017-12-09', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('103', '17', '00103', '资产00103', '135', '00014', null, '0.00', '9', '4', 'baiweibing', '1', null, '0', '2017-10-08', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('104', '17', '00104', '资产00104', '151', '00015', null, '0.00', '4', '1', 'lirunfang', '2', null, '0', '2017-07-03', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('105', '17', '00105', '资产00105', '82', '00016', null, '0.00', '9', '2', 'lirunfang', '2', null, '0', '2017-08-23', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('106', '17', '00106', '资产00106', '71', '00012', null, '0.00', '6', '3', 'pengxiao', '2', null, '0', '2017-08-26', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('107', '17', '00107', '资产00107', '155', '00013', null, '0.00', '3', '4', 'lirunfang', '2', null, '0', '2017-11-11', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('108', '17', '00108', '资产00108', '42', '00014', null, '0.00', '4', '1', 'XuQingWei', '2', null, '0', '2017-07-18', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('109', '17', '00109', '资产00109', '72', '00015', null, '0.00', '4', '2', 'baiweibing', '1', null, '0', '2017-10-20', '3.0', '1', '0');
INSERT INTO `j_zc` VALUES ('110', '17', '00110', '资产00110', '23', '00016', null, '0.00', '1', '3', 'lirunfang', '1', null, '0', '2017-05-30', '1.0', '1', '0');
INSERT INTO `j_zc` VALUES ('111', '17', '00111', '资产00111', '158', '00012', null, '0.00', '1', '4', 'lirunfang', '2', null, '0', '2017-11-23', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('112', '17', '00112', '资产00112', '95', '00013', null, '0.00', '1', '1', 'baiweibing', '2', null, '0', '2017-07-24', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('113', '17', '00113', '资产00113', '77', '00014', null, '0.00', '10', '2', 'XuQingWei', '2', null, '0', '2017-03-05', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('114', '17', '00114', '资产00114', '19', '00015', null, '0.00', '7', '3', 'shangwei', '2', null, '0', '2017-10-11', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('115', '17', '00115', '资产00115', '159', '00016', null, '0.00', '7', '4', 'yangjing', '1', null, '0', '2017-07-01', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('116', '17', '00116', '资产00116', '32', '00012', null, '0.00', '4', '1', 'XuQingWei', '2', null, '0', '2017-05-24', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('117', '17', '00117', '资产00117', '94', '00013', null, '0.00', '9', '2', 'shangwei', '1', null, '0', '2017-03-23', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('118', '17', '00118', '资产00118', '40', '00014', null, '0.00', '1', '3', 'shangwei', '1', null, '0', '2017-09-27', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('119', '17', '00119', '资产00119', '114', '00015', null, '0.00', '3', '4', 'shangwei', '2', null, '0', '2017-09-13', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('120', '17', '00120', '资产00120', '119', '00016', null, '0.00', '4', '1', 'shangwei', '2', null, '0', '2017-12-13', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('121', '17', '00121', '资产00121', '144', '00012', null, '0.00', '8', '2', 'shangwei', '1', null, '0', '2017-12-27', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('122', '17', '00122', '资产00122', '131', '00013', null, '0.00', '3', '3', 'lirunfang', '1', null, '0', '2017-05-19', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('123', '17', '00123', '资产00123', '8', '00014', null, '0.00', '7', '4', 'yangjing', '1', null, '0', '2017-11-30', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('124', '17', '00124', '资产00124', '29', '00015', null, '0.00', '9', '1', 'baiweibing', '1', null, '0', '2017-05-26', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('125', '17', '00125', '资产00125', '15', '00016', null, '0.00', '1', '2', 'baiweibing', '2', null, '0', '2017-12-25', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('126', '17', '00126', '资产00126', '135', '00012', null, '0.00', '6', '3', 'shangwei', '1', null, '0', '2017-01-21', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('127', '17', '00127', '资产00127', '143', '00013', null, '0.00', '6', '4', 'lirunfang', '1', null, '0', '2017-05-02', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('128', '17', '00128', '资产00128', '39', '00014', null, '0.00', '10', '1', 'shangwei', '2', null, '0', '2017-11-01', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('129', '17', '00129', '资产00129', '38', '00015', null, '0.00', '7', '2', 'XuQingWei', '2', null, '0', '2017-01-31', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('130', '17', '00130', '资产00130', '118', '00016', null, '0.00', '2', '3', 'baiweibing', '2', null, '0', '2017-08-24', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('131', '17', '00131', '资产00131', '85', '00012', null, '0.00', '6', '4', 'WenChen', '2', null, '0', '2017-11-29', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('132', '17', '00132', '资产00132', '135', '00013', null, '0.00', '1', '1', 'lirunfang', '2', null, '0', '2017-01-16', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('133', '17', '00133', '资产00133', '39', '00014', null, '0.00', '5', '2', 'WenChen', '1', null, '0', '2017-04-06', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('134', '17', '00134', '资产00134', '155', '00015', null, '0.00', '2', '3', 'lirunfang', '2', null, '0', '2017-10-21', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('135', '17', '00135', '资产00135', '108', '00016', null, '0.00', '3', '4', 'WenChen', '2', null, '0', '2017-06-03', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('136', '17', '00136', '资产00136', '26', '00012', null, '0.00', '4', '1', 'lirunfang', '2', null, '0', '2017-08-07', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('137', '17', '00137', '资产00137', '163', '00013', null, '0.00', '2', '2', 'shangwei', '2', null, '0', '2017-03-20', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('138', '17', '00138', '资产00138', '21', '00014', null, '0.00', '7', '3', 'lirunfang', '1', null, '0', '2017-06-04', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('139', '17', '00139', '资产00139', '32', '00015', null, '0.00', '7', '4', 'WenChen', '1', null, '0', '2017-03-23', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('140', '17', '00140', '资产00140', '68', '00016', null, '0.00', '4', '1', 'XuQingWei', '2', null, '0', '2017-02-02', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('141', '17', '00141', '资产00141', '106', '00012', null, '0.00', '8', '2', 'pengxiao', '2', null, '0', '2017-02-23', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('142', '17', '00142', '资产00142', '22', '00013', null, '0.00', '4', '3', 'yangjing', '1', null, '0', '2017-05-02', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('143', '17', '00143', '资产00143', '158', '00014', null, '0.00', '9', '4', 'shangwei', '2', null, '0', '2017-05-24', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('144', '17', '00144', '资产00144', '63', '00015', null, '0.00', '4', '1', 'baiweibing', '2', null, '0', '2017-01-27', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('145', '17', '00145', '资产00145', '31', '00016', null, '0.00', '2', '2', 'baiweibing', '2', null, '0', '2017-10-24', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('146', '17', '00146', '资产00146', '150', '00012', null, '0.00', '5', '3', 'WenChen', '1', null, '0', '2017-03-18', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('147', '17', '00147', '资产00147', '31', '00013', null, '0.00', '2', '4', 'WenChen', '2', null, '0', '2017-10-07', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('148', '17', '00148', '资产00148', '106', '00014', null, '0.00', '7', '1', 'baiweibing', '2', null, '0', '2017-09-25', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('149', '17', '00149', '资产00149', '42', '00015', null, '0.00', '6', '2', 'baiweibing', '2', null, '0', '2017-06-14', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('150', '17', '00150', '资产00150', '112', '00016', null, '0.00', '9', '3', 'XuQingWei', '1', null, '0', '2017-04-29', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('151', '17', '00151', '资产00151', '3', '00012', null, '0.00', '8', '4', 'XuQingWei', '2', null, '0', '2017-02-11', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('152', '17', '00152', '资产00152', '167', '00013', null, '0.00', '5', '1', 'baiweibing', '1', null, '0', '2017-01-24', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('153', '17', '00153', '资产00153', '38', '00014', null, '0.00', '7', '2', 'WenChen', '1', null, '0', '2017-01-17', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('154', '17', '00154', '资产00154', '153', '00015', null, '0.00', '9', '3', 'XuQingWei', '2', null, '0', '2017-07-22', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('155', '17', '00155', '资产00155', '142', '00016', null, '0.00', '9', '4', 'XuQingWei', '1', null, '0', '2017-02-06', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('156', '17', '00156', '资产00156', '6', '00012', null, '0.00', '4', '1', 'pengxiao', '1', null, '0', '2017-09-05', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('157', '17', '00157', '资产00157', '138', '00013', null, '0.00', '6', '2', 'shangwei', '2', null, '0', '2017-05-14', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('158', '17', '00158', '资产00158', '33', '00014', null, '0.00', '10', '3', 'yangjing', '1', null, '0', '2017-04-26', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('159', '17', '00159', '资产00159', '83', '00015', null, '0.00', '8', '4', 'yangjing', '1', null, '0', '2017-05-27', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('160', '17', '00160', '资产00160', '164', '00016', null, '0.00', '7', '1', 'yangjing', '2', null, '0', '2017-04-25', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('161', '17', '00161', '资产00161', '70', '00012', null, '0.00', '2', '2', 'WenChen', '1', null, '0', '2017-08-18', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('162', '17', '00162', '资产00162', '43', '00013', null, '0.00', '2', '3', 'pengxiao', '2', null, '0', '2017-12-18', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('163', '17', '00163', '资产00163', '61', '00014', null, '0.00', '3', '4', 'baiweibing', '2', null, '0', '2017-11-15', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('164', '17', '00164', '资产00164', '162', '00015', null, '0.00', '1', '1', 'lirunfang', '1', null, '0', '2017-09-27', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('165', '17', '00165', '资产00165', '42', '00016', null, '0.00', '10', '2', 'yangjing', '2', null, '0', '2017-07-27', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('166', '17', '00166', '资产00166', '86', '00012', null, '0.00', '7', '3', 'WenChen', '2', null, '0', '2017-09-13', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('167', '17', '00167', '资产00167', '148', '00013', null, '0.00', '6', '4', 'pengxiao', '2', null, '0', '2017-07-23', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('168', '17', '00168', '资产00168', '61', '00014', null, '0.00', '1', '1', 'XuQingWei', '2', null, '0', '2017-10-30', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('169', '17', '00169', '资产00169', '90', '00015', null, '0.00', '4', '2', 'XuQingWei', '2', null, '0', '2017-06-07', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('170', '17', '00170', '资产00170', '86', '00016', null, '0.00', '9', '3', 'shangwei', '2', null, '0', '2017-08-15', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('171', '17', '00171', '资产00171', '43', '00012', null, '0.00', '10', null, 'yangjing', '2', null, '0', '2017-05-29', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('172', '17', '00172', '资产00172', '100', '00013', null, '0.00', '4', null, 'chentianyu', '2', null, '0', '2017-11-10', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('173', '17', '00173', '资产00173', '150', '00014', null, '0.00', '3', null, 'WenChen', '2', null, '0', '2017-04-27', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('174', '17', '00174', '资产00174', '45', '00015', null, '0.00', '7', null, 'XuQingWei', '2', null, '0', '2017-08-16', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('175', '17', '00175', '资产00175', '30', '00016', null, '0.00', '5', null, 'chentianyu', '1', null, '0', '2017-10-25', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('176', '17', '00176', '资产00176', '42', '00017', null, '0.00', '10', null, 'baiweibing', '2', null, '0', '2017-10-15', '5.0', '7', '0');
INSERT INTO `j_zc` VALUES ('177', '17', '00177', '资产00177', '155', '00018', null, '0.00', '2', null, 'XuQingWei', '1', null, '0', '2017-07-12', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('178', '17', '00178', '资产00178', '151', '00019', null, '0.00', '3', null, 'lirunfang', '1', null, '0', '2017-08-02', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('179', '17', '00179', '资产00179', '54', '00020', null, '0.00', '5', null, 'fuzuo', '2', null, '0', '2017-10-05', '3.0', '7', '0');
INSERT INTO `j_zc` VALUES ('180', '17', '00180', '资产00180', '40', '00021', null, '0.00', '9', null, 'chentianyu', '2', null, '0', '2017-09-06', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('181', '17', '00181', '资产00181', '102', '00022', null, '0.00', '4', null, 'chentianyu', '2', null, '0', '2017-12-09', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('182', '17', '00182', '资产00182', '47', '00023', null, '0.00', '1', null, 'XuQingWei', '1', null, '0', '2017-01-18', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('183', '17', '00183', '资产00183', '124', '00024', null, '0.00', '10', null, 'chentianyu', '1', null, '0', '2017-05-19', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('184', '17', '00184', '资产00184', '54', '00025', null, '0.00', '5', null, 'pengxiao', '2', null, '0', '2017-02-08', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('185', '17', '00185', '资产00185', '40', '00026', null, '0.00', '5', null, 'pengxiao', '2', null, '0', '2017-09-29', '1.0', '7', '0');
INSERT INTO `j_zc` VALUES ('186', '17', '00186', '资产00186', '121', '00027', null, '0.00', '3', null, 'chentianyu', '1', null, '0', '2017-12-09', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('187', '17', '00187', '资产00187', '132', '00028', null, '0.00', '1', null, 'yangjing', '2', null, '0', '2017-10-28', '2.0', '7', '0');
INSERT INTO `j_zc` VALUES ('188', '17', '00188', '资产00188', '147', '00029', null, '0.00', '4', null, 'pengxiao', '2', null, '0', '2017-10-11', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('189', '17', '00189', '资产00189', '56', '00030', null, '0.00', '5', null, 'chentianyu', '2', null, '0', '2017-02-07', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('190', '17', '00190', '资产00190', '105', '00031', null, '0.00', '3', null, 'yangjing', '1', null, '0', '2017-11-23', '4.0', '7', '0');
INSERT INTO `j_zc` VALUES ('191', '17', '123123', '12312', '4', '12', '00001', '0.00', '1', '2', null, '14', null, '0', '2017-07-19', '3.0', '0', '0');
INSERT INTO `j_zc` VALUES ('192', '17', '00192', '资产00192', '125', '00012', null, '0.00', '6', null, 'WenChen', '1', null, '0', '2017-07-16', '1.0', '12', '0');
INSERT INTO `j_zc` VALUES ('193', '17', '00193', '资产00193', '91', '00013', null, '0.00', '5', null, 'lirunfang', '1', null, '0', '2017-01-06', '4.0', '12', '0');
INSERT INTO `j_zc` VALUES ('194', '17', '00194', '资产00194', '11', '00014', null, '0.00', '2', null, 'lirunfang', '2', null, '0', '2017-03-02', '4.0', '12', '0');
INSERT INTO `j_zc` VALUES ('195', '17', '00195', '资产00195', '92', '00015', null, '0.00', '9', null, 'yangjing', '2', null, '0', '2017-12-10', '5.0', '12', '0');
INSERT INTO `j_zc` VALUES ('196', '17', '00196', '资产00196', '38', '00016', null, '0.00', '10', null, 'yangjing', '1', null, '0', '2017-05-29', '4.0', '12', '0');
INSERT INTO `j_zc` VALUES ('197', '17', '00197', '资产00197', '7', '00017', null, '0.00', '2', null, 'chentianyu', '2', null, '0', '2017-10-23', '2.0', '12', '0');
INSERT INTO `j_zc` VALUES ('198', '17', '00198', '资产00198', '142', '00018', null, '0.00', '5', null, 'fuzuo', '2', null, '0', '2017-05-01', '2.0', '12', '0');
INSERT INTO `j_zc` VALUES ('199', '17', '00199', '资产00199', '156', '00019', null, '0.00', '8', null, 'baiweibing', '1', null, '0', '2017-03-22', '4.0', '12', '0');
INSERT INTO `j_zc` VALUES ('200', '17', '00200', '资产00200', '46', '00020', null, '0.00', '9', null, 'yangjing', '1', null, '0', '2017-09-29', '3.0', '12', '0');
INSERT INTO `j_zc` VALUES ('201', '17', '00201', '资产00201', '164', '00021', null, '0.00', '7', null, 'XuQingWei', '1', null, '0', '2017-06-19', '5.0', '12', '0');
INSERT INTO `j_zc` VALUES ('202', '17', '00202', '资产00202', '54', '00022', null, '0.00', '1', null, 'WenChen', '1', null, '0', '2017-05-26', '3.0', '12', '0');
INSERT INTO `j_zc` VALUES ('203', '17', '00203', '资产00203', '31', '00023', null, '0.00', '5', null, '', '3', null, '0', '2017-07-08', '4.0', '11', '0');
INSERT INTO `j_zc` VALUES ('204', '17', '00204', '资产00204', '69', '00024', null, '0.00', '6', null, '', '14', null, '0', '2017-04-10', '1.0', '0', '0');
INSERT INTO `j_zc` VALUES ('205', '17', '00205', '资产00205', '44', '00025', null, '0.00', '1', null, 'shangwei', '3', null, '0', '2017-04-12', '4.0', '12', '0');
INSERT INTO `j_zc` VALUES ('206', '17', '00206', '资产00206', '45', '00026', null, '0.00', '9', null, 'shangwei', '3', null, '0', '2017-03-22', '3.0', '12', '0');
INSERT INTO `j_zc` VALUES ('207', '17', '00207', '资产00207', '164', '00027', null, '0.00', '5', null, 'shangwei', '3', null, '0', '2017-03-01', '5.0', '12', '0');
INSERT INTO `j_zc` VALUES ('208', '17', '00208', '资产00208', '136', '00028', null, '0.00', '9', null, 'shangwei', '3', null, '0', '2017-09-26', '3.0', '12', '0');
INSERT INTO `j_zc` VALUES ('209', '17', '00209', '资产00209', '12', '00029', null, '0.00', '1', null, 'shangwei', '2', null, '0', '2017-12-08', '3.0', '12', '0');
INSERT INTO `j_zc` VALUES ('210', '17', '00210', '资产00210', '123', '00030', null, '0.00', '7', null, 'baiweibing', '1', null, '0', '2017-10-26', '3.0', '12', '0');
INSERT INTO `j_zc` VALUES ('211', '17', '00211', '资产00211', '84', '00031', null, '0.00', '7', null, 'baiweibing', '1', null, '0', '2017-07-11', '4.0', '12', '0');
INSERT INTO `j_zc` VALUES ('212', '17', '4fed2621a75745409a3e6247dc4c4e89', '资产4fed2621a75745409a3e6247dc4c4e89', '91', '资产4fed2621a75745409a3e6247dc4c4e89', null, '100.00', '1', null, null, null, null, '0', '2017-11-23', '1.0', '9', '0');
INSERT INTO `j_zc` VALUES ('213', '17', '6ec4ff57307a46b1a358fc3cb8b1fde5', '资产6ec4ff57307a46b1a358fc3cb8b1fde5', '57', '资产6ec4ff57307a46b1a358fc3cb8b1fde5', null, '100.00', '1', null, null, null, null, '0', '2017-08-07', '1.0', '9', '0');
INSERT INTO `j_zc` VALUES ('214', '17', '9d480138b1ce47f297bd1bf92e205035', '资产9d480138b1ce47f297bd1bf92e205035', '66', '资产9d480138b1ce47f297bd1bf92e205035', null, '100.00', '1', '1', null, '2', null, '0', '2017-06-29', '3.0', '0', '0');
INSERT INTO `j_zc` VALUES ('215', '17', 'a1955b9aae784d279e4179f7ff94f870', '资产a1955b9aae784d279e4179f7ff94f870', '150', '资产a1955b9aae784d279e4179f7ff94f870', null, '100.00', '1', '2', null, '2', null, '0', '2017-09-12', '5.0', '0', '0');
INSERT INTO `j_zc` VALUES ('216', '1', '1000013', '消防栓1', '170', '', '', '300.00', '1', null, null, null, '/upload/zc_images/1/ee6bf9df-54b8-4ba0-8852-78dbd1c0d6a9', '2', '1900-02-14', '2.0', '9', '0');
INSERT INTO `j_zc` VALUES ('217', '1', '1000014', '消防栓2', '2', '', '', '301.00', '2', null, null, null, null, '0', '1900-01-02', '2.0', '13', '0');
INSERT INTO `j_zc` VALUES ('218', '1', '1000019', '消防栓3', '2', '', '', '302.00', '3', null, null, null, null, '0', '2017-01-06', '2.0', '13', '0');
INSERT INTO `j_zc` VALUES ('219', '1', '1000016', '消防栓4', '2', '', '', '303.00', '4', null, null, null, null, '0', '2017-01-06', '2.0', '13', '0');
INSERT INTO `j_zc` VALUES ('220', '1', '1000017', '消防栓5', '2', '', '', '1.00', '5', null, null, null, null, '0', '2017-01-06', '2.0', '13', '0');
INSERT INTO `j_zc` VALUES ('221', '1', '1000018', '消防栓6', '2', '', '', '200.00', '6', null, null, null, null, '0', '2017-01-06', '2.0', '13', '0');

-- ----------------------------
-- Table structure for l_msg
-- ----------------------------
DROP TABLE IF EXISTS `l_msg`;
CREATE TABLE `l_msg` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `FSSJ` datetime DEFAULT NULL,
  `JSR` varchar(30) DEFAULT NULL,
  `LX` int(1) DEFAULT NULL COMMENT '系统通知（0），系统预警（1）、消息通告（2）、隐患上报（3）',
  `NR` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of l_msg
-- ----------------------------

-- ----------------------------
-- Table structure for l_zt
-- ----------------------------
DROP TABLE IF EXISTS `l_zt`;
CREATE TABLE `l_zt` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `ZCDM` varchar(36) DEFAULT NULL COMMENT '对应于J_ZC中的DM',
  `JLSJ` datetime DEFAULT NULL,
  `JLR` varchar(30) DEFAULT NULL,
  `POI` varchar(200) DEFAULT NULL,
  `OLD_ZT` int(2) DEFAULT NULL,
  `NEW_ZT` int(2) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of l_zt
-- ----------------------------

-- ----------------------------
-- Table structure for l_ztxz
-- ----------------------------
DROP TABLE IF EXISTS `l_ztxz`;
CREATE TABLE `l_ztxz` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APP_ID` int(11) DEFAULT NULL,
  `JL_ID` int(11) DEFAULT NULL COMMENT '指向上级L_ZT主键',
  `MEDIA_TYPE` varchar(30) NOT NULL,
  `WX_MEDIA_ID` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of l_ztxz
-- ----------------------------

-- ----------------------------
-- View structure for v_fj
-- ----------------------------
DROP VIEW IF EXISTS `v_fj`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`127.0.0.1`  VIEW `v_fj` AS select fj.id fj_id, fj.app_id, fj.floor, fj.room, fj.dept_name, fj.glr,
       jzw.XQ_ID xqid, xq.XQMC, jzw.id jzwid, jzw.mc jzw, jzw.dz, jzw.poi, fj.tybz
from c_fj fj
left outer join c_jzw jzw on (jzw.id = fj.jzw_id)
left outer join c_xq xq on (jzw.xq_id = xq.id)
order by fj.id ; 

-- ----------------------------
-- View structure for v_jzw
-- ----------------------------
DROP VIEW IF EXISTS `v_jzw`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`127.0.0.1`  VIEW `v_jzw` AS select jzw.id jzw_id, jzw.app_id, xq.id xqid, xq.xqmc, lx.id lxid, lx.mc lxmc, jzw.mc jzw, jzw.dz, jzw.poi, jzw.tybz
from c_jzw jzw
join c_xq xq on (jzw.xq_id = xq.id)
join c_jzlx lx on (jzw.lx_id = lx.id)
order by jzw.id ; 

-- ----------------------------
-- View structure for v_zc
-- ----------------------------
DROP VIEW IF EXISTS `v_zc`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`127.0.0.1` SQL SECURITY DEFINER  VIEW `v_zc` AS select zc.id zcid, zc.app_id, zc.dm zcdm, zc.mc zc,
       lx.LX_ID zclxId,lx.mc zclx, zc.xh, zc.ccbh, zc.COST, zc.num, zc.cfdd, zc.syr,
	   gl.DEPT_NO,gl.dept_name, gl.fzr, gl.glr, 
     IFNULL(zc.pic_url, lx.PIC_URL) PIC_URL, 
     case when zc.PIC_URL is null then lx.IMG_VERSION
          else zc.IMG_VERSION
     end IMG_VERSION,
	   zc.gzsj, zc.zjnx, zc.zt zczt
from j_zc zc
join c_zclx lx on (zc.lx_id = lx.id)
left outer join c_zcgl gl on (zc.zcgl_id = gl.id) ;

-- ----------------------------
-- View structure for v_zclx
-- ----------------------------
DROP VIEW IF EXISTS `v_zclx`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`127.0.0.1`  VIEW `v_zclx` AS select lx.id, lx.app_id, lx.mc lx, plx.mc plx, lx.zjnx, lx.remark 
from c_zclx lx
left outer join c_zclx plx on (lx.app_id = plx.app_id and lx.lx_pid = plx.lx_id)
order by lx.id ; 

-- ----------------------------
-- View structure for v_zt
-- ----------------------------
DROP VIEW IF EXISTS `v_zt`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`127.0.0.1` SQL SECURITY DEFINER  VIEW `v_zt` AS select zt.id ztid, zt.app_id, zt.zcdm, zt.jlsj, zt.jlr, zt.poi, zt.old_zt, zt.new_zt, zt.remark, xz.MEDIA_TYPE, 
			 min(case when xz.MEDIA_TYPE is null then '/default_img'
               when xz.MEDIA_TYPE = 'image' then CONCAT('/', zt.APP_ID, '/', date_format(zt.jlsj, '%Y-%m-%d'), '/image/', xz.wx_media_id) 
               else '/default_voice'
           end) image_url
from l_zt zt
left outer join l_ztxz xz on (zt.id = xz.jl_id)
group by ztid, app_id, zcdm, jlsj, jlr, poi, old_zt, new_zt, remark, xz.MEDIA_TYPE
order by jlsj desc ;

-- ----------------------------
-- Function structure for getWXDeptList
-- ----------------------------
DROP FUNCTION IF EXISTS `getWXDeptList`;
DELIMITER ;;
CREATE DEFINER=`root`@`127.0.0.1` FUNCTION `getWXDeptList`(appId INT, rootId INT) RETURNS varchar(1000) CHARSET utf8
BEGIN
      DECLARE sTemp VARCHAR(1000);
      DECLARE sTempChd VARCHAR(1000);
    
       SET sTemp = '$';
       SET sTempChd =cast(rootId as CHAR);
    
      WHILE sTempChd is not null DO
         SET sTemp = concat(sTemp,',',sTempChd);
         SELECT group_concat(dept_no) INTO sTempChd FROM chu_wxdept where FIND_IN_SET(dept_pno,sTempChd)>0 and app_id = appId;
      END WHILE;
       RETURN substr(sTemp, 3);
     END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for getWXDeptRootList
-- ----------------------------
DROP FUNCTION IF EXISTS `getWXDeptRootList`;
DELIMITER ;;
CREATE DEFINER=`root`@`127.0.0.1` FUNCTION `getWXDeptRootList`(appId INT, rootId INT) RETURNS varchar(1000) CHARSET utf8
BEGIN
      DECLARE sTemp VARCHAR(1000);
      DECLARE sTempChd VARCHAR(1000);
    
       SET sTemp = '$';
       SET sTempChd =cast(rootId as CHAR);
    
      WHILE sTempChd is not null DO
         SET sTemp = concat(sTempChd,',',sTemp);
         SELECT group_concat(dept_pno) INTO sTempChd FROM chu_wxdept where FIND_IN_SET(dept_no,sTempChd)>0 and app_id = appId;
      END WHILE;
       RETURN substr(sTemp, 1, length(sTemp)-2);
     END
;;
DELIMITER ;
