/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : jjche-boot

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 16/08/2022 10:51:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for code_column_config
-- ----------------------------
CREATE TABLE `code_column_config`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `table_name`      varchar(255) DEFAULT NULL,
    `column_name`     varchar(255) DEFAULT NULL,
    `column_type`     varchar(255) DEFAULT NULL,
    `dict_name`       varchar(255) DEFAULT NULL,
    `extra`           varchar(255) DEFAULT NULL,
    `form_show`       bit(1)       DEFAULT NULL,
    `form_type`       varchar(255) DEFAULT NULL,
    `key_type`        varchar(255) DEFAULT NULL,
    `list_show`       bit(1)       DEFAULT NULL,
    `not_null`        bit(1)       DEFAULT NULL,
    `query_type`      varchar(255) DEFAULT NULL,
    `remark`          varchar(255) DEFAULT NULL,
    `date_annotation` varchar(255) DEFAULT NULL,
    `max_length`      int(11)      DEFAULT NULL COMMENT '最大长度',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 191
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='代码生成字段信息存储';

-- ----------------------------
-- Records of code_column_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for code_gen_config
-- ----------------------------
CREATE TABLE `code_gen_config`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `table_name`  varchar(255) DEFAULT NULL COMMENT '表名',
    `author`      varchar(255) DEFAULT NULL COMMENT '作者',
    `cover`       bit(1)       DEFAULT NULL COMMENT '是否覆盖',
    `module_name` varchar(255) DEFAULT NULL COMMENT '模块名称',
    `pack`        varchar(255) DEFAULT NULL COMMENT '至于哪个包下',
    `path`        varchar(255) DEFAULT NULL COMMENT '前端代码生成的路径',
    `api_path`    varchar(255) DEFAULT NULL COMMENT '前端Api文件路径',
    `prefix`      varchar(255) DEFAULT NULL COMMENT '表前缀',
    `api_alias`   varchar(255) DEFAULT NULL COMMENT '接口名称',
    `api_version` varchar(50) NOT NULL COMMENT '版本号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='代码生成器配置';

-- ----------------------------
-- Records of code_gen_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for mnt_app
-- ----------------------------
CREATE TABLE `mnt_app`
(
    `id`            bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`          varchar(255)         DEFAULT NULL COMMENT '应用名称',
    `upload_path`   varchar(255)         DEFAULT NULL COMMENT '上传目录',
    `deploy_path`   varchar(255)         DEFAULT NULL COMMENT '部署路径',
    `backup_path`   varchar(255)         DEFAULT NULL COMMENT '备份路径',
    `port`          int(255)             DEFAULT NULL COMMENT '应用端口',
    `start_script`  varchar(4000)        DEFAULT NULL COMMENT '启动脚本',
    `deploy_script` varchar(4000)        DEFAULT NULL COMMENT '部署脚本',
    `created_by`    varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`    varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`  datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='应用管理';

-- ----------------------------
-- Records of mnt_app
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for mnt_database
-- ----------------------------
CREATE TABLE `mnt_database`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`         varchar(255) NOT NULL COMMENT '名称',
    `jdbc_url`     varchar(255) NOT NULL COMMENT 'jdbc连接',
    `user_name`    varchar(255) NOT NULL COMMENT '账号',
    `pwd`          varchar(255) NOT NULL COMMENT '密码',
    `created_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='数据库管理';

-- ----------------------------
-- Records of mnt_database
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for mnt_deploy
-- ----------------------------
CREATE TABLE `mnt_deploy`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `app_id`       bigint(20)           DEFAULT NULL COMMENT '应用编号',
    `created_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='部署管理';

-- ----------------------------
-- Records of mnt_deploy
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for mnt_deploy_history
-- ----------------------------
CREATE TABLE `mnt_deploy_history`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `app_name`     varchar(255) NOT NULL COMMENT '应用名称',
    `deploy_date`  datetime     NOT NULL COMMENT '部署日期',
    `deploy_user`  varchar(50)  NOT NULL COMMENT '部署用户',
    `ip`           varchar(20)  NOT NULL COMMENT '服务器IP',
    `deploy_id`    bigint(20)            DEFAULT NULL COMMENT '部署编号',
    `created_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='部署历史管理';

-- ----------------------------
-- Records of mnt_deploy_history
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for mnt_deploy_server
-- ----------------------------
CREATE TABLE `mnt_deploy_server`
(
    `deploy_id` bigint(20) NOT NULL COMMENT '部署ID',
    `server_id` bigint(20) NOT NULL COMMENT '服务ID',
    PRIMARY KEY (`deploy_id`, `server_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='应用与服务器关联';

-- ----------------------------
-- Records of mnt_deploy_server
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for mnt_server
-- ----------------------------
CREATE TABLE `mnt_server`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `account`      varchar(50)          DEFAULT NULL COMMENT '账号',
    `ip`           varchar(20)          DEFAULT NULL COMMENT 'IP地址',
    `name`         varchar(100)         DEFAULT NULL COMMENT '名称',
    `password`     varchar(100)         DEFAULT NULL COMMENT '密码',
    `port`         int(11)              DEFAULT NULL COMMENT '端口',
    `created_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器管理';

-- ----------------------------
-- Records of mnt_server
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for security_app_key
-- ----------------------------
CREATE TABLE `security_app_key`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`         varchar(50)  NOT NULL COMMENT '名称',
    `comment`      varchar(255)          DEFAULT NULL COMMENT '描述',
    `app_id`       varchar(255) NOT NULL COMMENT '应用id',
    `app_secret`   varchar(255) NOT NULL COMMENT '应用密钥',
    `enc_key`      varchar(255) NOT NULL COMMENT '加密密钥',
    `enabled`      bit(1)       NOT NULL DEFAULT b'1' COMMENT '状态：1启用、0禁用',
    `urls`         varchar(500) NOT NULL COMMENT '地址',
    `white_ip`     varchar(500)          DEFAULT NULL COMMENT '白名单',
    `limit_count`  int(11)      NOT NULL DEFAULT '0' COMMENT '限速（N/秒）0不限制',
    `created_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_name` (`name`),
    UNIQUE KEY `uk_app_id` (`app_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4 COMMENT ='应用密钥';

-- ----------------------------
-- Records of security_app_key
-- ----------------------------
BEGIN;
INSERT INTO `security_app_key`
VALUES (1, '测试', NULL, 'default_app_id', 'default_app_secret', 'y5PtpclbYABqpF2x', b'1', '/api/demo/out/students/*',
        NULL, 0, 'System', 'System', '2022-08-11 08:40:11', '2022-08-11 08:40:11');
COMMIT;

-- ----------------------------
-- Table structure for sys_data_permission_field
-- ----------------------------
CREATE TABLE `sys_data_permission_field`
(
    `id`           bigint(20)          NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `menu_id`      bigint(20)          NOT NULL COMMENT '菜单ID',
    `name`         varchar(255)        NOT NULL COMMENT '名称',
    `code`         varchar(255)        NOT NULL COMMENT '标识',
    `sort`         int(5)              NOT NULL COMMENT '排序',
    `is_activated` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否有效 1是 0否',
    `created_by`   varchar(50)         NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)         NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='菜单数据字段权限表';

-- ----------------------------
-- Records of sys_data_permission_field
-- ----------------------------
BEGIN;
INSERT INTO `sys_data_permission_field`
VALUES (10, 133, '姓名', 'name', 999, 1, 'System', 'System', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
INSERT INTO `sys_data_permission_field`
VALUES (11, 133, '性别', 'age', 999, 1, 'System', 'System', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
COMMIT;

-- ----------------------------
-- Table structure for sys_data_permission_field_role
-- ----------------------------
CREATE TABLE `sys_data_permission_field_role`
(
    `id`                       bigint(20)          NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`                  bigint(20)          NOT NULL DEFAULT '0' COMMENT '角色ID',
    `menu_id`                  bigint(20)          NOT NULL DEFAULT '0' COMMENT '菜单ID',
    `data_permission_field_id` bigint(20)          NOT NULL DEFAULT '0' COMMENT '菜单数据字段权限ID',
    `is_accessible`            tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否可见 1是 0否',
    `is_editable`              tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否可编辑 1是 0否',
    `created_by`               varchar(50)         NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`               varchar(50)         NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`               datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`             datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='菜单数据字段权限角色表';

-- ----------------------------
-- Records of sys_data_permission_field_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_data_permission_field_role`
VALUES (7, 2, 133, 10, 1, 0, 'System', 'System', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
INSERT INTO `sys_data_permission_field_role`
VALUES (8, 2, 133, 11, 1, 1, 'System', 'System', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
COMMIT;

-- ----------------------------
-- Table structure for sys_data_permission_rule
-- ----------------------------
CREATE TABLE `sys_data_permission_rule`
(
    `id`           bigint(20)          NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `menu_id`      bigint(20)          NOT NULL COMMENT '菜单ID',
    `name`         varchar(255)        NOT NULL COMMENT '名称',
    `condition`    varchar(50)         NOT NULL COMMENT '条件',
    `column`       varchar(50)                  DEFAULT NULL COMMENT '列名',
    `value`        varchar(500)                 DEFAULT NULL COMMENT '规则值',
    `is_activated` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否有效 1是 0否',
    `created_by`   varchar(50)         NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)         NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4 COMMENT ='数据规则表';

-- ----------------------------
-- Records of sys_data_permission_rule
-- ----------------------------
BEGIN;
INSERT INTO `sys_data_permission_rule`
VALUES (1, 133, '只看年龄等于3的', 'EQUAL', 'age', '3', 1, 'System', 'System', '2022-08-16 10:48:07',
        '2022-08-16 10:48:07');
COMMIT;

-- ----------------------------
-- Table structure for sys_data_permission_rule_role
-- ----------------------------
CREATE TABLE `sys_data_permission_rule_role`
(
    `id`                      bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`                 bigint(20)  NOT NULL DEFAULT '0' COMMENT '角色ID',
    `menu_id`                 bigint(20)  NOT NULL DEFAULT '0' COMMENT '菜单ID',
    `data_permission_rule_id` bigint(20)  NOT NULL DEFAULT '0' COMMENT '数据规则权限ID',
    `created_by`              varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`              varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`              datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`            datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单数据规则权限角色表';

-- ----------------------------
-- Records of sys_data_permission_rule_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_data_permission_rule_role`
VALUES (1, 2, 133, 1, 'System', 'System', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
CREATE TABLE `sys_dept`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`          bigint(20)            DEFAULT NULL COMMENT '上级部门',
    `sub_count`    int(5)                DEFAULT '0' COMMENT '子部门数目',
    `name`         varchar(255) NOT NULL COMMENT '名称',
    `dept_sort`    int(5)                DEFAULT '999' COMMENT '排序',
    `enabled`      bit(1)       NOT NULL COMMENT '状态',
    `created_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 18
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='部门';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept`
VALUES (2, 7, 1, '研发部', 3, b'1', 'admin', 'admin', '2019-03-25 09:15:32', '2020-08-02 14:48:47');
INSERT INTO `sys_dept`
VALUES (5, 7, 0, '运维部', 4, b'1', 'admin', 'admin', '2019-03-25 09:20:44', '2020-05-17 14:27:27');
INSERT INTO `sys_dept`
VALUES (6, 8, 0, '测试部', 6, b'1', 'admin', 'admin', '2019-03-25 09:52:18', '2020-06-08 11:59:21');
INSERT INTO `sys_dept`
VALUES (7, NULL, 2, '华南分部', 0, b'1', 'admin', 'admin', '2019-03-25 11:04:50', '2020-06-08 12:08:56');
INSERT INTO `sys_dept`
VALUES (8, NULL, 2, '华北分部', 1, b'1', 'admin', 'admin', '2019-03-25 11:04:53', '2020-05-14 12:54:00');
INSERT INTO `sys_dept`
VALUES (15, 8, 0, 'UI部门', 7, b'1', 'admin', 'admin', '2020-05-13 22:56:53', '2020-05-14 12:54:13');
INSERT INTO `sys_dept`
VALUES (17, 2, 0, '研发一组', 999, b'1', 'admin', 'admin', '2020-08-02 14:49:07', '2020-08-02 14:49:07');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
CREATE TABLE `sys_dict`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`         varchar(255) NOT NULL COMMENT '字典名称',
    `description`  varchar(255)          DEFAULT NULL COMMENT '描述',
    `created_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='数据字典';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict`
VALUES (1, 'user_status', '用户状态', 'System', 'System', '2019-10-27 20:31:36', '2021-09-10 16:55:06');
INSERT INTO `sys_dict`
VALUES (4, 'dept_status', '部门状态', 'System', 'System', '2019-10-27 20:31:36', '2021-09-10 16:55:06');
INSERT INTO `sys_dict`
VALUES (5, 'job_status', '岗位状态', 'System', 'System', '2019-10-27 20:31:36', '2021-09-10 16:55:06');
INSERT INTO `sys_dict`
VALUES (6, 'dataPermissionRule_condition', '数据规则-条件', 'System', 'System', '2021-12-27 16:23:30',
        '2021-12-27 16:23:30');
INSERT INTO `sys_dict`
VALUES (7, 'course_status', '课程状态', 'System', 'System', '2021-09-13 13:15:30', '2021-09-13 13:15:30');

INSERT INTO `sys_dict`
VALUES (8, 'bpm_model_category', '流程分类', 'System', 'System', SYSDATE(), SYSDATE());

INSERT INTO `sys_dict`
VALUES (9, 'bpm_model_form_type', '流程的表单类型', 'System', 'System', SYSDATE(), SYSDATE());

INSERT INTO `sys_dict`
VALUES (10, 'bpm_task_assign_rule_type', '任务分配规则的类型', 'System', 'System', SYSDATE(), SYSDATE());

INSERT INTO `sys_dict`
VALUES (11, 'bpm_task_assign_script', '任务分配自定义脚本', 'System', 'System', SYSDATE(), SYSDATE());

INSERT INTO `sys_dict`
VALUES (12, 'bpm_process_instance_status', '流程实例的状态', 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict`
VALUES (13, 'bpm_process_instance_result', '流程实例的结果', 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict`
VALUES (14, 'bpm_oa_leave_type', 'OA 请假类型', 'System', 'System', SYSDATE(), SYSDATE());

COMMIT;

-- ----------------------------
-- Table structure for sys_dict_detail
-- ----------------------------
CREATE TABLE `sys_dict_detail`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dict_id`      bigint(11)            DEFAULT NULL COMMENT '字典id',
    `label`        varchar(255) NOT NULL COMMENT '字典标签',
    `value`        varchar(255) NOT NULL COMMENT '字典值',
    `dict_sort`    int(5)                DEFAULT NULL COMMENT '排序',
    `created_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 25
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='数据字典详情';

-- ----------------------------
-- Records of sys_dict_detail
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_detail`
VALUES (1, 1, '激活', 'true', 1, 'System', 'System', '2021-09-10 16:55:06', '2021-09-10 16:55:06');
INSERT INTO `sys_dict_detail`
VALUES (2, 1, '禁用', 'false', 2, 'System', 'System', '2021-09-10 16:55:06', '2021-09-10 16:55:06');
INSERT INTO `sys_dict_detail`
VALUES (3, 4, '启用', 'true', 1, 'System', 'System', '2021-09-10 16:55:06', '2021-09-10 16:55:06');
INSERT INTO `sys_dict_detail`
VALUES (4, 4, '停用', 'false', 2, 'System', 'System', '2021-09-10 16:55:06', '2021-09-10 16:55:06');
INSERT INTO `sys_dict_detail`
VALUES (5, 5, '启用', 'true', 1, 'System', 'System', '2021-09-10 16:55:06', '2021-09-10 16:55:06');
INSERT INTO `sys_dict_detail`
VALUES (6, 5, '停用', 'false', 2, 'System', 'System', '2021-09-10 16:55:06', '2021-09-10 16:55:06');
INSERT INTO `sys_dict_detail`
VALUES (7, 6, '等于', 'EQUAL', 1, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (8, 6, '不等于', 'NE', 2, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (9, 6, '大于', 'GT', 3, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (10, 6, '大于等于', 'GE', 4, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (11, 6, '小于', 'LT', 5, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (12, 6, '小于等于', 'LE', 6, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (13, 6, '全模糊', 'LIKE', 7, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (14, 6, '左模糊', 'LEFT_LIKE', 8, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (15, 6, '右模糊', 'RIGHT_LIKE', 9, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (16, 6, '区间', 'BETWEEN', 10, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (17, 6, '包含', 'IN', 11, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (18, 6, '不为空', 'NOT_NULL', 12, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (19, 6, '为空', 'IS_NULL', 13, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (20, 6, '自定义SQL片段', 'USE_SQL_RULES', 14, 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_dict_detail`
VALUES (21, 7, '图文', '102', 1, 'System', 'System', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
INSERT INTO `sys_dict_detail`
VALUES (22, 7, '音频', '103', 2, 'System', 'System', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
INSERT INTO `sys_dict_detail`
VALUES (23, 7, '视频', '104', 3, 'System', 'System', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
INSERT INTO `sys_dict_detail`
VALUES (24, 7, '外链', '105', 4, 'System', 'System', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
INSERT INTO `sys_dict_detail`
VALUES (25, 8, '默认', '1', 1, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (26, 8, 'OA', '2', 2, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (27, 9, '流程表单', '10', 1, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (28, 9, '业务表单', '20', 2, 'System', 'System', SYSDATE(), SYSDATE());

INSERT INTO `sys_dict_detail`
VALUES (29, 10, '角色', '10', 10, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (30, 10, '部门的成员', '20', 20, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (31, 10, '部门的负责人', '21', 21, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (32, 10, '用户', '30', 30, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (33, 10, '用户组', '40', 40, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (34, 10, '自定义脚本', '50', 50, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (35, 10, '岗位', '22', 22, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (36, 11, '流程发起人', '10', 10, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (37, 11, '流程发起人的一级领导', '20', 20, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (38, 11, '流程发起人的二级领导', '21', 21, 'System', 'System', SYSDATE(), SYSDATE());

INSERT INTO `sys_dict_detail`
VALUES (39, 12, '进行中', '1', 1, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (40, 12, '已完成', '2', 2, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (41, 13, '处理中', '1', 1, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (42, 13, '通过', '2', 2, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (43, 13, '不通过', '3', 3, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (44, 13, '已取消', '4', 5, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (45, 14, '病假', '1', 1, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (46, 14, '事假', '2', 2, 'System', 'System', SYSDATE(), SYSDATE());
INSERT INTO `sys_dict_detail`
VALUES (47, 14, '婚假', '3', 3, 'System', 'System', SYSDATE(), SYSDATE());

COMMIT;

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
CREATE TABLE `sys_job`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`         varchar(255) NOT NULL COMMENT '岗位名称',
    `enabled`      bit(1)       NOT NULL COMMENT '岗位状态',
    `job_sort`     int(5)                DEFAULT NULL COMMENT '排序',
    `created_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='岗位';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
BEGIN;
INSERT INTO `sys_job`
VALUES (8, '人事专员', b'1', 3, 'System', 'System', '2019-03-29 14:52:28', '2021-09-10 16:55:06');
INSERT INTO `sys_job`
VALUES (10, '产品经理', b'1', 4, 'System', 'System', '2019-03-29 14:55:51', '2021-09-10 16:55:06');
INSERT INTO `sys_job`
VALUES (11, '全栈开发', b'1', 2, 'System', 'System', '2019-03-31 13:39:30', '2021-09-10 16:55:06');
INSERT INTO `sys_job`
VALUES (12, '软件测试', b'1', 5, 'System', 'System', '2019-03-31 13:39:43', '2021-09-10 16:55:06');
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
CREATE TABLE `sys_log`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `description`      varchar(255)        DEFAULT NULL,
    `log_type`         varchar(255)        DEFAULT NULL,
    `method`           varchar(255)        DEFAULT NULL,
    `params`           text,
    `request_ip`       varchar(255)        DEFAULT NULL,
    `time`             bigint(20)          DEFAULT NULL,
    `username`         varchar(255)        DEFAULT NULL,
    `address`          varchar(255)        DEFAULT NULL,
    `browser`          varchar(255)        DEFAULT NULL,
    `os`               varchar(255)        DEFAULT NULL,
    `user_agent`       varchar(500)        DEFAULT NULL,
    `exception_detail` text,
    `detail`           text COMMENT '详情',
    `url`              text COMMENT 'url',
    `module`           varchar(255)        DEFAULT NULL COMMENT '模块',
    `tenant`           varchar(255)        DEFAULT NULL COMMENT '租户',
    `biz_key`          varchar(255)        DEFAULT NULL COMMENT '业务key',
    `biz_no`           varchar(255)        DEFAULT NULL COMMENT '业务编号',
    `category`         varchar(50)         DEFAULT NULL COMMENT '分类',
    `result`           text COMMENT '结果',
    `gmt_create`       datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_success`       bit(1)     NOT NULL DEFAULT b'0' COMMENT '是否成功：0:否, 1:是',
    `request_id`       varchar(255)        DEFAULT NULL COMMENT '请求id',
    `app_name`         varchar(50)         DEFAULT NULL COMMENT '应用名',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `inx_log_type` (`log_type`(5)) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3540
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='系统日志';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
CREATE TABLE `sys_menu`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`          bigint(20)           DEFAULT NULL COMMENT '上级菜单ID',
    `sub_count`    int(5)               DEFAULT '0' COMMENT '子菜单数目',
    `type`         int(11)              DEFAULT NULL COMMENT '菜单类型',
    `title`        varchar(255)         DEFAULT NULL COMMENT '菜单标题',
    `name`         varchar(255)         DEFAULT NULL COMMENT '组件名称',
    `component`    varchar(255)         DEFAULT NULL COMMENT '组件',
    `menu_sort`    int(5)               DEFAULT NULL COMMENT '排序',
    `icon`         varchar(255)         DEFAULT NULL COMMENT '图标',
    `path`         varchar(255)         DEFAULT NULL COMMENT '链接地址',
    `i_frame`      bit(1)               DEFAULT NULL COMMENT '是否外链',
    `cache`        bit(1)               DEFAULT b'0' COMMENT '缓存',
    `hidden`       bit(1)               DEFAULT b'0' COMMENT '隐藏',
    `permission`   varchar(255)         DEFAULT NULL COMMENT '权限',
    `created_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 137
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='系统菜单';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu`
VALUES (1, NULL, 7, 0, '系统管理', NULL, NULL, 1, 'system', 'system', b'0', b'0', b'0', NULL, 'System', 'System',
        '2018-12-18 15:11:29', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', b'0', b'0', b'0', 'user:list',
        'System', 'System', '2018-12-18 15:14:44', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/role/index', 3, 'role', 'role', b'0', b'0', b'0', 'roles:list',
        'System', 'System', '2018-12-18 15:16:07', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (5, 1, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', b'0', b'0', b'0', 'menu:list', 'System',
        'System', '2018-12-18 15:17:28', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (6, NULL, 5, 0, '系统监控', NULL, NULL, 10, 'monitor', 'monitor', b'0', b'0', b'0', NULL, 'System', 'System',
        '2018-12-18 15:17:48', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (7, 6, 0, 1, '操作日志', 'Log', 'monitor/log/index', 11, 'log', 'logs', b'0', b'1', b'0', 'log:list', 'System',
        'System', '2018-12-18 15:18:26', '2021-12-27 16:23:30');
INSERT INTO `sys_menu`
VALUES (9, 6, 0, 1, 'SQL监控', 'Sql', 'monitor/sql/index', 18, 'sqlMonitor', 'druid', b'0', b'0', b'0', NULL, 'System',
        'System', '2018-12-18 15:19:34', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (10, NULL, 5, 0, '组件管理', NULL, NULL, 50, 'zujian', 'components', b'0', b'0', b'0', NULL, 'System', 'System',
        '2018-12-19 13:38:16', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (11, 10, 0, 1, '图标库', 'Icons', 'components/icons/index', 51, 'icon', 'icon', b'0', b'0', b'0', NULL, 'System',
        'System', '2018-12-19 13:38:49', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (14, 36, 0, 1, '邮件工具', 'Email', 'tools/email/index', 35, 'email', 'email', b'0', b'0', b'0', NULL, 'System',
        'System', '2018-12-27 10:13:09', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (15, 10, 0, 1, '富文本', 'Editor', 'components/Editor', 52, 'fwb', 'tinymce', b'0', b'0', b'0', NULL, 'System',
        'System', '2018-12-27 11:58:25', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (18, 36, 3, 1, '存储管理', 'Storage', 'tools/storage/index', 34, 'qiniu', 'storage', b'0', b'0', b'0',
        'storage:list', 'System', 'System', '2018-12-31 11:12:15', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (19, 36, 0, 1, '支付宝工具', 'AliPay', 'tools/aliPay/index', 37, 'alipay', 'aliPay', b'0', b'0', b'0', NULL,
        'System', 'System', '2018-12-31 14:52:38', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (28, 1, 3, 1, '任务调度', 'Timing', 'system/timing/index', 9, 'timing', 'timing', b'0', b'0', b'0',
        'timing:list', 'System', 'System', '2019-01-07 20:34:40', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (30, 36, 2, 1, '代码生成', 'GeneratorIndex', 'generator/index', 32, 'dev', 'generator', b'0', b'1', b'0',
        'generator:list', 'System', 'System', '2019-01-11 15:45:55', '2021-12-27 16:23:30');
INSERT INTO `sys_menu`
VALUES (33, 10, 0, 1, 'Markdown', 'Markdown', 'components/MarkDown', 53, 'markdown', 'markdown', b'0', b'0', b'0', NULL,
        'System', 'System', '2019-03-08 13:46:44', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (34, 10, 0, 1, 'Yaml编辑器', 'YamlEdit', 'components/YamlEdit', 54, 'dev', 'yaml', b'0', b'0', b'0', NULL,
        'System', 'System', '2019-03-08 15:49:40', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (35, 1, 3, 1, '部门管理', 'Dept', 'system/dept/index', 6, 'dept', 'dept', b'0', b'0', b'0', 'dept:list',
        'System', 'System', '2019-03-25 09:46:00', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (36, NULL, 7, 0, '系统工具', NULL, '', 30, 'sys-tools', 'sys-tools', b'0', b'0', b'0', NULL, 'System', 'System',
        '2019-03-29 10:57:35', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (37, 1, 3, 1, '岗位管理', 'Job', 'system/job/index', 7, 'Steve-Jobs', 'job', b'0', b'0', b'0', 'job:list',
        'System', 'System', '2019-03-29 13:51:18', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (38, 36, 0, 1, '接口文档', 'Swagger', 'tools/swagger/index', 36, 'swagger', 'swagger2', b'0', b'0', b'0', NULL,
        'System', 'System', '2019-03-29 19:57:53', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (39, 1, 3, 1, '字典管理', 'Dict', 'system/dict/index', 8, 'dictionary', 'dict', b'0', b'0', b'0', 'dict:list',
        'System', 'System', '2019-04-10 11:49:04', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'monitor/online/index', 10, 'Steve-Jobs', 'online', b'0', b'0', b'0',
        'online:list', 'System', 'System', '2019-10-26 22:08:43', '2021-12-27 16:23:30');
INSERT INTO `sys_menu`
VALUES (44, 2, 0, 2, '用户新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'user:add', 'System', 'System',
        '2019-10-29 10:59:46', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (45, 2, 0, 2, '用户编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'user:edit', 'System', 'System',
        '2019-10-29 11:00:08', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (46, 2, 0, 2, '用户删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'user:del', 'System', 'System',
        '2019-10-29 11:00:23', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (48, 3, 0, 2, '角色创建', NULL, '', 2, '', '', b'0', b'0', b'0', 'roles:add', 'System', 'System',
        '2019-10-29 12:45:34', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (49, 3, 0, 2, '角色修改', NULL, '', 3, '', '', b'0', b'0', b'0', 'roles:edit', 'System', 'System',
        '2019-10-29 12:46:16', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (50, 3, 0, 2, '角色删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'roles:del', 'System', 'System',
        '2019-10-29 12:46:51', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (52, 5, 0, 2, '菜单新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'menu:add', 'System', 'System',
        '2019-10-29 12:55:07', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (53, 5, 0, 2, '菜单编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'menu:edit', 'System', 'System',
        '2019-10-29 12:55:40', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (54, 5, 0, 2, '菜单删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'menu:del', 'System', 'System',
        '2019-10-29 12:56:00', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (56, 35, 0, 2, '部门新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dept:add', 'System', 'System',
        '2019-10-29 12:57:09', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (57, 35, 0, 2, '部门编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dept:edit', 'System', 'System',
        '2019-10-29 12:57:27', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (58, 35, 0, 2, '部门删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dept:del', 'System', 'System',
        '2019-10-29 12:57:41', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (60, 37, 0, 2, '岗位新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'job:add', 'System', 'System',
        '2019-10-29 12:58:27', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (61, 37, 0, 2, '岗位编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'job:edit', 'System', 'System',
        '2019-10-29 12:58:45', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (62, 37, 0, 2, '岗位删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'job:del', 'System', 'System',
        '2019-10-29 12:59:04', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (64, 39, 0, 2, '字典新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dict:add', 'System', 'System',
        '2019-10-29 13:00:17', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (65, 39, 0, 2, '字典编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dict:edit', 'System', 'System',
        '2019-10-29 13:00:42', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (66, 39, 0, 2, '字典删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dict:del', 'System', 'System',
        '2019-10-29 13:00:59', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (73, 28, 0, 2, '任务新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'timing:add', 'System', 'System',
        '2019-10-29 13:07:28', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (74, 28, 0, 2, '任务编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'timing:edit', 'System', 'System',
        '2019-10-29 13:07:41', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (75, 28, 0, 2, '任务删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'timing:del', 'System', 'System',
        '2019-10-29 13:07:54', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (77, 18, 0, 2, '上传文件', NULL, '', 2, '', '', b'0', b'0', b'0', 'storage:add', 'System', 'System',
        '2019-10-29 13:09:09', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (78, 18, 0, 2, '文件编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'storage:edit', 'System', 'System',
        '2019-10-29 13:09:22', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (79, 18, 0, 2, '文件删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'storage:del', 'System', 'System',
        '2019-10-29 13:09:34', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (80, 6, 0, 1, '服务监控', 'ServerMonitor', 'monitor/server/index', 14, 'codeConsole', 'server', b'0', b'0', b'0',
        'monitor:list', 'System', 'System', '2019-11-07 13:06:39', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (82, 36, 0, 1, '生成配置', 'GeneratorConfig', 'generator/config', 33, 'dev', 'generator/config/:tableName', b'0',
        b'1', b'1', '', 'System', 'System', '2019-11-17 20:08:56', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (83, 10, 0, 1, '图表库', 'Echarts', 'components/Echarts', 50, 'chart', 'echarts', b'0', b'1', b'0', '', 'System',
        'System', '2019-11-21 09:04:32', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (90, NULL, 5, 1, '运维管理', 'Mnt', '', 20, 'mnt', 'mnt', b'0', b'0', b'0', NULL, 'System', 'System',
        '2019-11-09 10:31:08', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (92, 90, 3, 1, '服务器', 'ServerDeploy', 'mnt/server/index', 22, 'server', 'mnt/serverDeploy', b'0', b'0', b'0',
        'serverDeploy:list', 'System', 'System', '2019-11-10 10:29:25', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (93, 90, 3, 1, '应用管理', 'App', 'mnt/app/index', 23, 'app', 'mnt/app', b'0', b'0', b'0', 'app:list', 'System',
        'System', '2019-11-10 11:05:16', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (94, 90, 3, 1, '部署管理', 'Deploy', 'mnt/deploy/index', 24, 'deploy', 'mnt/deploy', b'0', b'0', b'0',
        'deploy:list', 'System', 'System', '2019-11-10 15:56:55', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (97, 90, 1, 1, '部署备份', 'DeployHistory', 'mnt/deployHistory/index', 25, 'backup', 'mnt/deployHistory', b'0',
        b'0', b'0', 'deployHistory:list', 'System', 'System', '2019-11-10 16:49:44', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (98, 90, 3, 1, '数据库管理', 'Database', 'mnt/database/index', 26, 'database', 'mnt/database', b'0', b'0', b'0',
        'database:list', 'System', 'System', '2019-11-10 20:40:04', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (102, 97, 0, 2, '删除', NULL, '', 999, '', '', b'0', b'0', b'0', 'deployHistory:del', 'System', 'System',
        '2019-11-17 09:32:48', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (103, 92, 0, 2, '服务器新增', NULL, '', 999, '', '', b'0', b'0', b'0', 'serverDeploy:add', 'System', 'System',
        '2019-11-17 11:08:33', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (104, 92, 0, 2, '服务器编辑', NULL, '', 999, '', '', b'0', b'0', b'0', 'serverDeploy:edit', 'System', 'System',
        '2019-11-17 11:08:57', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (105, 92, 0, 2, '服务器删除', NULL, '', 999, '', '', b'0', b'0', b'0', 'serverDeploy:del', 'System', 'System',
        '2019-11-17 11:09:15', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (106, 93, 0, 2, '应用新增', NULL, '', 999, '', '', b'0', b'0', b'0', 'app:add', 'System', 'System',
        '2019-11-17 11:10:03', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (107, 93, 0, 2, '应用编辑', NULL, '', 999, '', '', b'0', b'0', b'0', 'app:edit', 'System', 'System',
        '2019-11-17 11:10:28', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (108, 93, 0, 2, '应用删除', NULL, '', 999, '', '', b'0', b'0', b'0', 'app:del', 'System', 'System',
        '2019-11-17 11:10:55', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (109, 94, 0, 2, '部署新增', NULL, '', 999, '', '', b'0', b'0', b'0', 'deploy:add', 'System', 'System',
        '2019-11-17 11:11:22', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (110, 94, 0, 2, '部署编辑', NULL, '', 999, '', '', b'0', b'0', b'0', 'deploy:edit', 'System', 'System',
        '2019-11-17 11:11:41', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (111, 94, 0, 2, '部署删除', NULL, '', 999, '', '', b'0', b'0', b'0', 'deploy:del', 'System', 'System',
        '2019-11-17 11:12:01', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (112, 98, 0, 2, '数据库新增', NULL, '', 999, '', '', b'0', b'0', b'0', 'database:add', 'System', 'System',
        '2019-11-17 11:12:43', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (113, 98, 0, 2, '数据库编辑', NULL, '', 999, '', '', b'0', b'0', b'0', 'database:edit', 'System', 'System',
        '2019-11-17 11:12:58', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (114, 98, 0, 2, '数据库删除', NULL, '', 999, '', '', b'0', b'0', b'0', 'database:del', 'System', 'System',
        '2019-11-17 11:13:14', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (116, 36, 0, 1, '生成预览', 'Preview', 'generator/preview', 999, 'java', 'generator/preview/:tableName', b'0',
        b'1', b'1', NULL, 'System', 'System', '2019-11-26 14:54:36', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (118, 6, 0, 1, '应用监控', 'Sba', 'monitor/sba/index', 19, 'app', 'sba', b'0', b'0', b'0', NULL, 'admin',
        'admin', '2020-10-30 09:07:50', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (121, 6, 0, 1, '缓存监控', 'CacheMonitor', 'monitor/cache/index', 20, 'redis', 'cache', b'0', b'0', b'0',
        'monitor:list', 'admin', 'admin', '2020-12-24 17:54:40', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (122, 1, 3, 1, '版本管理', 'VersionList', 'system/version/index', 10, 'tree', 'Version', b'0', b'0', b'0',
        'version:list', 'admin', 'admin', '2021-09-10 16:55:06', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (123, 122, 0, 2, '版本新增', 'version_add', '', 5, '', '', b'0', b'0', b'0', 'version:add', 'admin', 'admin',
        '2021-09-10 16:55:06', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (124, 122, 0, 2, '版本编辑', 'version_edit', '', 5, '', '', b'0', b'0', b'0', 'version:edit', 'admin', 'admin',
        '2021-09-10 16:55:06', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (125, 122, 0, 2, '版本删除', 'version_del', '', 5, '', '', b'0', b'0', b'0', 'version:del', 'admin', 'admin',
        '2021-09-10 16:55:06', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (126, 41, 0, 2, '强退', 'online_del', '', 5, '', '', b'0', b'0', b'0', 'online:del', 'admin', 'admin',
        '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_menu`
VALUES (127, 7, 0, 2, '清空', 'log_del', '', 5, '', '', b'0', b'0', b'0', 'log:del', 'admin', 'admin',
        '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_menu`
VALUES (128, NULL, 0, 0, '首页', 'Dashboard', 'Layout', 0, 'index', 'dashboard', b'0', b'0', b'0', 'dashboard:list',
        'admin', 'admin', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
INSERT INTO `sys_menu`
VALUES (129, 1, 3, 1, '密钥管理', 'SecurityAppKeyMenu', 'system/securityAppKey/index', 11, 'anq', 'SecurityAppKey',
        b'0', b'0', b'0', 'securityAppKey:list', 'admin', 'admin', '2022-08-05 15:27:18', '2022-08-05 15:27:18');
INSERT INTO `sys_menu`
VALUES (130, 129, 0, 2, '密钥新增', 'securityAppKey_add', '', 5, '', '', b'0', b'0', b'0', 'securityAppKey:add',
        'admin', 'admin', '2022-08-05 15:27:18', '2022-08-05 15:27:18');
INSERT INTO `sys_menu`
VALUES (131, 129, 0, 2, '密钥编辑', 'securityAppKey_edit', '', 5, '', '', b'0', b'0', b'0', 'securityAppKey:edit',
        'admin', 'admin', '2022-08-05 15:27:18', '2022-08-05 15:27:18');
INSERT INTO `sys_menu`
VALUES (132, 129, 0, 2, '密钥删除', 'securityAppKey_del', '', 5, '', '', b'0', b'0', b'0', 'securityAppKey:del',
        'admin', 'admin', '2022-08-05 15:27:18', '2022-08-05 15:27:18');
INSERT INTO `sys_menu`
VALUES (133, 1, 1, 1, '代码生成-学生菜单', 'StudentMenu', 'system/student/index', 999, 'Steve-Jobs', 'student', b'0',
        b'0', b'0', 'student:list', 'admin', 'admin', '2020-11-10 14:08:10', '2021-09-10 16:55:06');
INSERT INTO `sys_menu`
VALUES (134, 133, 0, 2, '学生测试菜单新增', 'student_add', '', 5, '', '', b'0', b'0', b'0', 'student:add', 'admin',
        'admin', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
INSERT INTO `sys_menu`
VALUES (135, 133, 0, 2, '学生测试菜单编辑', 'student_edit', '', 5, '', '', b'0', b'0', b'0', 'student:edit', 'admin',
        'admin', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
INSERT INTO `sys_menu`
VALUES (136, 133, 0, 2, '学生测试菜单删除', 'student_del', '', 5, '', '', b'0', b'0', b'0', 'student:del', 'admin',
        'admin', '2022-08-16 10:48:07', '2022-08-16 10:48:07');
INSERT INTO `sys_menu`
VALUES (137, NULL, 3, 0, '工作流程', NULL, NULL, 2, 'tools', '/bpm', b'0', b'0', b'0', '', 'System', 'System',
        '2023-03-07 09:46:00', '2023-03-08 09:56:16');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (138, 137, 3, 0, '流程管理', NULL, NULL, 0, 'nested', 'manager', b'0', b'0', b'0', NULL, 'System', 'System',
        '2023-03-08 01:18:15', '2023-03-09 05:23:24');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (139, 137, 3, 0, '任务管理', NULL, NULL, 1, 'list', 'task', b'0', b'0', b'0', '', 'System', 'System',
        '2023-03-08 01:19:57', '2023-03-09 05:53:38');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (140, 137, 1, 0, 'OA示例', NULL, NULL, 2, 'people', 'oa', b'0', b'0', b'0', NULL, 'System', 'System',
        '2023-03-08 01:22:26', '2023-03-09 05:53:35');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (141, 138, 5, 1, '流程表单', NULL, 'bpm/form/index', 0, 'edit', 'form', b'0', b'0', b'0', NULL, 'admin', 'admin',
        '2023-03-08 02:29:17', '2023-03-09 05:53:31');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (142, 138, 4, 1, '用户分组', NULL, 'bpm/group/index', 1, 'user', 'user-group', b'0', b'0', b'0', NULL, 'admin',
        'admin', '2023-03-08 02:31:09', '2023-03-08 05:37:43');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (143, 138, 10, 1, '流程模型', NULL, 'bpm/model/index', 2, 'develop', 'model', b'0', b'0', b'0', NULL, 'admin',
        'admin', '2023-03-08 02:32:30', '2023-03-08 05:37:59');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (144, 141, 0, 2, '表单查询', NULL, NULL, 0, NULL, NULL, b'0', b'0', b'0', 'bpm:form:query', 'admin', 'admin',
        '2023-03-08 02:35:53', '2023-03-08 05:21:23');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (145, 141, 0, 2, '表单创建', NULL, NULL, 1, NULL, NULL, b'0', b'0', b'0', 'bpm:form:create', 'admin', 'admin',
        '2023-03-08 02:36:54', '2023-03-08 05:21:23');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (146, 141, 0, 2, '表单更新', NULL, NULL, 2, '', '', b'0', b'0', b'0', 'bpm:form:update', 'admin', 'admin',
        '2023-03-08 02:37:58', '2023-03-08 05:21:19');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (147, 141, 0, 2, '表单删除', NULL, NULL, 3, NULL, NULL, b'0', b'0', b'0', 'bpm:form:delete', 'admin', 'admin',
        '2023-03-08 02:40:25', '2023-03-08 05:21:19');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (148, 141, 0, 2, '表单导出', NULL, NULL, 4, NULL, NULL, b'0', b'0', b'0', 'bpm:form:export', 'admin', 'admin',
        '2023-03-08 02:41:10', '2023-03-08 05:21:14');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (149, 142, 0, 2, '用户组查询', NULL, NULL, 0, NULL, NULL, b'0', b'0', b'0', 'bpm:user-group:query', 'admin',
        'admin', '2023-03-08 02:42:37', '2023-03-08 02:42:37');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (150, 142, 0, 2, '用户组创建', NULL, NULL, 1, NULL, NULL, b'0', b'0', b'0', 'bpm:user-group:create', 'admin',
        'admin', '2023-03-08 02:43:12', '2023-03-08 02:43:12');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (151, 142, 0, 2, '用户组更新', NULL, NULL, 2, NULL, NULL, b'0', b'0', b'0', 'bpm:user-group:update', 'admin',
        'admin', '2023-03-08 02:43:59', '2023-03-08 02:43:59');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (152, 142, 0, 2, '用户组删除', NULL, NULL, 3, NULL, NULL, b'0', b'0', b'0', 'bpm:user-group:delete', 'admin',
        'admin', '2023-03-08 02:44:47', '2023-03-08 02:44:47');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (153, 143, 0, 2, '模型查询', NULL, NULL, 0, NULL, NULL, b'0', b'0', b'0', 'bpm:model:query', 'admin', 'admin',
        '2023-03-08 02:47:01', '2023-03-08 02:47:01');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (154, 143, 0, 2, '模型创建', NULL, NULL, 1, NULL, NULL, b'0', b'0', b'0', 'bpm:model:create', 'admin', 'admin',
        '2023-03-08 02:47:35', '2023-03-08 02:47:35');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (155, 143, 0, 2, '模型导入', NULL, NULL, 2, NULL, NULL, b'0', b'0', b'0', 'bpm:model:import', 'admin', 'admin',
        '2023-03-08 02:48:08', '2023-03-08 02:48:08');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (156, 143, 0, 2, '模型更新', NULL, NULL, 3, NULL, NULL, b'0', b'0', b'0', 'bpm:model:update', 'admin', 'admin',
        '2023-03-08 02:48:30', '2023-03-08 02:52:41');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (157, 143, 0, 2, '模型删除', NULL, NULL, 4, NULL, NULL, b'0', b'0', b'0', 'bpm:model:delete', 'admin', 'admin',
        '2023-03-08 02:49:06', '2023-03-08 02:49:06');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (158, 143, 0, 2, '模型发布', NULL, NULL, 5, NULL, NULL, b'0', b'0', b'0', 'bpm:model:deploy', 'admin', 'admin',
        '2023-03-08 02:49:42', '2023-03-08 02:49:42');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (159, 143, 0, 2, '流程定义查询', NULL, NULL, 9, NULL, NULL, b'0', b'0', b'0', 'bpm:process-definition:query',
        'admin', 'admin', '2023-03-08 02:50:32', '2023-03-08 02:50:32');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (160, 143, 0, 2, '流程任务分配规则查询', NULL, NULL, 10, NULL, NULL, b'0', b'0', b'0',
        'bpm:task-assign-rule:query', 'admin', 'admin', '2023-03-08 02:48:30', '2023-03-08 02:55:01');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (161, 143, 0, 2, '流程任务分配规则创建', NULL, NULL, 11, NULL, NULL, b'0', b'0', b'0',
        'bpm:task-assign-rule:create', 'admin', 'admin', '2023-03-08 02:48:30', '2023-03-08 02:55:44');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (162, 143, 0, 2, '流程任务分配规则更新', NULL, NULL, 12, NULL, NULL, b'0', b'0', b'0',
        'bpm:task-assign-rule:update', 'admin', 'admin', '2023-03-08 02:48:30', '2023-03-08 02:56:17');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (163, 139, 3, 1, '我的流程', NULL, 'bpm/processInstance/index', 0, 'people', 'my', b'0', b'0', b'0', NULL,
        'admin', 'admin', '2023-03-08 02:48:30', '2023-03-08 05:38:15');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (164, 139, 2, 1, '待办任务', NULL, 'bpm/task/todo', 1, 'skill', 'todo', b'0', b'0', b'0', NULL, 'admin', 'admin',
        '2023-03-08 02:48:30', '2023-03-08 05:38:32');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (165, 139, 0, 1, '已办任务', NULL, 'bpm/task/done', 2, 'swagger', 'done', b'0', b'0', b'0', NULL, 'admin',
        'admin', '2023-03-08 02:48:30', '2023-03-08 05:38:59');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (166, 163, 0, 2, '流程实例的查询', NULL, NULL, 0, NULL, NULL, b'0', b'0', b'0', 'bpm:process-instance:query',
        'admin', 'admin', '2023-03-08 02:48:30', '2023-03-08 03:29:36');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (167, 163, 0, 2, '流程实例的创建', NULL, NULL, 1, NULL, NULL, b'0', b'0', b'0', 'bpm:process-instance:create',
        'admin', 'admin', '2023-03-08 02:48:30', '2023-03-08 03:30:12');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (168, 163, 0, 2, '流程实例的取消', NULL, NULL, 2, NULL, NULL, b'0', b'0', b'0', 'bpm:process-instance:cancel',
        'admin', 'admin', '2023-03-08 02:48:30', '2023-03-08 03:30:52');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (169, 164, 0, 2, '流程任务的查询', NULL, NULL, 0, NULL, NULL, b'0', b'0', b'0', 'bpm:task:query', 'admin',
        'admin', '2023-03-08 02:48:30', '2023-03-08 03:31:35');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (170, 164, 0, 2, '流程任务的更新', NULL, NULL, 1, NULL, NULL, b'0', b'0', b'0', 'bpm:task:update', 'admin',
        'admin', '2023-03-08 02:48:30', '2023-03-08 03:32:04');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (171, 140, 2, 1, '请假查询', NULL, 'bpm/oa/leave/index', 0, 'user', 'leave', b'0', b'0', b'0', NULL, 'admin',
        'admin', '2023-03-08 02:48:30', '2023-03-08 10:02:03');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (172, 171, 0, 2, '请假申请查询', NULL, NULL, 0, NULL, NULL, b'0', b'0', b'0', 'bpm:oa-leave:query', 'admin',
        'admin', '2023-03-08 02:48:30', '2023-03-08 03:34:04');
INSERT INTO `sys_menu`(`id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`,
                       `i_frame`, `cache`, `hidden`, `permission`, `created_by`, `updated_by`, `gmt_create`,
                       `gmt_modified`)
VALUES (173, 171, 0, 2, '请假申请创建', NULL, NULL, 1, NULL, NULL, b'0', b'0', b'0', 'bpm:oa-leave:create', 'admin',
        'admin', '2023-03-08 02:48:30', '2023-03-08 03:34:35');

COMMIT;

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
CREATE TABLE `sys_quartz_job`
(
    `id`                  bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `bean_name`           varchar(255)         DEFAULT NULL COMMENT 'Spring Bean名称',
    `cron_expression`     varchar(255)         DEFAULT NULL COMMENT 'cron 表达式',
    `is_pause`            bit(1)               DEFAULT NULL COMMENT '状态：1暂停、0启用',
    `job_name`            varchar(255)         DEFAULT NULL COMMENT '任务名称',
    `method_name`         varchar(255)         DEFAULT NULL COMMENT '方法名称',
    `params`              varchar(255)         DEFAULT NULL COMMENT '参数',
    `description`         varchar(255)         DEFAULT NULL COMMENT '备注',
    `person_in_charge`    varchar(100)         DEFAULT NULL COMMENT '负责人',
    `email`               varchar(100)         DEFAULT NULL COMMENT '报警邮箱',
    `sub_task`            varchar(100)         DEFAULT NULL COMMENT '子任务ID',
    `pause_after_failure` bit(1)               DEFAULT NULL COMMENT '任务失败后是否暂停',
    `created_by`          varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`          varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`          datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`        datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='定时任务';

-- ----------------------------
-- Records of sys_quartz_job
-- ----------------------------
BEGIN;
INSERT INTO `sys_quartz_job`
VALUES (2, 'testTask', '0/5 * * * * ?', b'1', '测试1', 'run1', 'test', '带参测试，多参使用json', '测试', NULL, NULL,
        NULL, 'System', 'System', '2019-08-22 14:08:29', '2020-05-24 13:58:33');
INSERT INTO `sys_quartz_job`
VALUES (3, 'testTask', '0/5 * * * * ?', b'1', '测试', 'run', '', '不带参测试', 'Zheng Jie', '', '5,6', b'1', 'System',
        'System', '2019-09-26 16:44:39', '2020-05-24 14:48:12');
INSERT INTO `sys_quartz_job`
VALUES (5, 'Test', '0/5 * * * * ?', b'1', '任务告警测试', 'run', NULL, '测试', 'test', '', NULL, b'1', 'admin', 'admin',
        '2020-05-05 20:32:41', '2020-05-05 20:36:13');
INSERT INTO `sys_quartz_job`
VALUES (6, 'testTask', '0/5 * * * * ?', b'1', '测试3', 'run2', NULL, '测试3', 'Zheng Jie', '', NULL, b'1', 'admin',
        'admin', '2020-05-05 20:35:41', '2020-05-05 20:36:07');
INSERT INTO `sys_quartz_job`
VALUES (7, 'userTask', '0 51 23 ? * *', b'1', '用户账号过期检查', 'accountExpired', NULL,
        '用户账号长时间未登录，账号过期', 'admin', '', NULL, b'0', 'admin', 'admin', '2021-01-07 09:12:41',
        '2021-01-07 09:12:41');
INSERT INTO `sys_quartz_job`
VALUES (8, 'userTask', '0 52 23 ? * *', b'1', '用户密码过期检查', 'credentialsExpired', NULL,
        '用户密码长时间未修改，密码过期', 'admin', '', NULL, b'0', 'admin', 'admin', '2021-01-07 09:12:41',
        '2021-01-07 09:12:41');
INSERT INTO `sys_quartz_job`
VALUES (9, 'userTask', '0 53 23 ? * *', b'1', '用户密码过期检查', 'credentialsExpiredAdvanceDayMustReset', NULL,
        '密码过期前N天设置必须修改密码', 'admin', '', NULL, b'0', 'admin', 'admin', '2021-01-07 09:12:41',
        '2021-01-07 09:12:41');
INSERT INTO `sys_quartz_job`
VALUES (10, 'userTask', '0 54 23 ? * *', b'1', '用户密码过期检查', 'credentialsExpiredAdvanceDayTipReset', NULL,
        '提前N天提醒用户修改密码', 'admin', '', NULL, b'0', 'admin', 'admin', '2021-01-07 09:12:41',
        '2021-01-07 09:12:41');
INSERT INTO `sys_quartz_job`
VALUES (11, 'logTask', '0 55 23 ? * *', b'1', '清空N个月之前的操作日志', 'cleanLogs', '-3', '清空N个月之前的操作日志',
        'admin', NULL, NULL, b'0', 'System', 'System', '2021-12-27 16:23:30', '2021-12-27 16:23:30');
COMMIT;

-- ----------------------------
-- Table structure for sys_quartz_log
-- ----------------------------
CREATE TABLE `sys_quartz_log`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `bean_name`        varchar(255)        DEFAULT NULL,
    `gmt_create`       datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `cron_expression`  varchar(255)        DEFAULT NULL,
    `exception_detail` text,
    `is_success`       bit(1)              DEFAULT NULL,
    `job_name`         varchar(255)        DEFAULT NULL,
    `method_name`      varchar(255)        DEFAULT NULL,
    `params`           varchar(255)        DEFAULT NULL,
    `time`             bigint(20)          DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 151
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='定时任务日志';

-- ----------------------------
-- Records of sys_quartz_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
CREATE TABLE `sys_role`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`         varchar(255) NOT NULL COMMENT '名称',
    `code`         varchar(50)  NOT NULL COMMENT '标识',
    `level`        int(255)              DEFAULT NULL COMMENT '角色级别',
    `description`  varchar(255)          DEFAULT NULL COMMENT '描述',
    `data_scope`   varchar(255)          DEFAULT NULL COMMENT '数据权限',
    `created_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `ux_code` (`code`) USING BTREE COMMENT '标识唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role`
VALUES (1, '超级管理员', 'ROLE_ADMIN', 1, '-', 'DATA_SCOPE_ALL', 'System', 'System', '2018-11-23 11:04:37',
        '2021-12-27 16:23:30');
INSERT INTO `sys_role`
VALUES (2, '演示角色', 'ROLE_DEMO', 2, '-', 'DATA_SCOPE_CUSTOM', 'System', 'System', '2018-11-23 13:09:06',
        '2021-12-27 16:23:30');
COMMIT;

-- ----------------------------
-- Table structure for sys_roles_depts
-- ----------------------------
CREATE TABLE `sys_roles_depts`
(
    `role_id` bigint(20) NOT NULL,
    `dept_id` bigint(20) NOT NULL,
    PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='角色部门关联';

-- ----------------------------
-- Records of sys_roles_depts
-- ----------------------------
BEGIN;
INSERT INTO `sys_roles_depts`
VALUES (1, 7);
INSERT INTO `sys_roles_depts`
VALUES (1, 8);
INSERT INTO `sys_roles_depts`
VALUES (2, 6);
COMMIT;

-- ----------------------------
-- Table structure for sys_roles_menus
-- ----------------------------
CREATE TABLE `sys_roles_menus`
(
    `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`menu_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='角色菜单关联';

-- ----------------------------
-- Records of sys_roles_menus
-- ----------------------------
BEGIN;
INSERT INTO `sys_roles_menus`
VALUES (1, 1);
INSERT INTO `sys_roles_menus`
VALUES (1, 2);
INSERT INTO `sys_roles_menus`
VALUES (2, 1);
INSERT INTO `sys_roles_menus`
VALUES (2, 2);
INSERT INTO `sys_roles_menus`
VALUES (3, 1);
INSERT INTO `sys_roles_menus`
VALUES (3, 2);
INSERT INTO `sys_roles_menus`
VALUES (5, 1);
INSERT INTO `sys_roles_menus`
VALUES (5, 2);
INSERT INTO `sys_roles_menus`
VALUES (6, 1);
INSERT INTO `sys_roles_menus`
VALUES (6, 2);
INSERT INTO `sys_roles_menus`
VALUES (7, 1);
INSERT INTO `sys_roles_menus`
VALUES (7, 2);
INSERT INTO `sys_roles_menus`
VALUES (9, 1);
INSERT INTO `sys_roles_menus`
VALUES (9, 2);
INSERT INTO `sys_roles_menus`
VALUES (10, 1);
INSERT INTO `sys_roles_menus`
VALUES (10, 2);
INSERT INTO `sys_roles_menus`
VALUES (11, 1);
INSERT INTO `sys_roles_menus`
VALUES (11, 2);
INSERT INTO `sys_roles_menus`
VALUES (14, 1);
INSERT INTO `sys_roles_menus`
VALUES (14, 2);
INSERT INTO `sys_roles_menus`
VALUES (15, 1);
INSERT INTO `sys_roles_menus`
VALUES (15, 2);
INSERT INTO `sys_roles_menus`
VALUES (18, 1);
INSERT INTO `sys_roles_menus`
VALUES (18, 2);
INSERT INTO `sys_roles_menus`
VALUES (19, 1);
INSERT INTO `sys_roles_menus`
VALUES (19, 2);
INSERT INTO `sys_roles_menus`
VALUES (28, 1);
INSERT INTO `sys_roles_menus`
VALUES (28, 2);
INSERT INTO `sys_roles_menus`
VALUES (30, 1);
INSERT INTO `sys_roles_menus`
VALUES (30, 2);
INSERT INTO `sys_roles_menus`
VALUES (32, 1);
INSERT INTO `sys_roles_menus`
VALUES (33, 1);
INSERT INTO `sys_roles_menus`
VALUES (33, 2);
INSERT INTO `sys_roles_menus`
VALUES (34, 1);
INSERT INTO `sys_roles_menus`
VALUES (34, 2);
INSERT INTO `sys_roles_menus`
VALUES (35, 1);
INSERT INTO `sys_roles_menus`
VALUES (35, 2);
INSERT INTO `sys_roles_menus`
VALUES (36, 1);
INSERT INTO `sys_roles_menus`
VALUES (36, 2);
INSERT INTO `sys_roles_menus`
VALUES (37, 1);
INSERT INTO `sys_roles_menus`
VALUES (37, 2);
INSERT INTO `sys_roles_menus`
VALUES (38, 1);
INSERT INTO `sys_roles_menus`
VALUES (38, 2);
INSERT INTO `sys_roles_menus`
VALUES (39, 1);
INSERT INTO `sys_roles_menus`
VALUES (39, 2);
INSERT INTO `sys_roles_menus`
VALUES (41, 1);
INSERT INTO `sys_roles_menus`
VALUES (41, 2);
INSERT INTO `sys_roles_menus`
VALUES (77, 2);
INSERT INTO `sys_roles_menus`
VALUES (78, 2);
INSERT INTO `sys_roles_menus`
VALUES (79, 2);
INSERT INTO `sys_roles_menus`
VALUES (80, 1);
INSERT INTO `sys_roles_menus`
VALUES (80, 2);
INSERT INTO `sys_roles_menus`
VALUES (82, 1);
INSERT INTO `sys_roles_menus`
VALUES (82, 2);
INSERT INTO `sys_roles_menus`
VALUES (83, 1);
INSERT INTO `sys_roles_menus`
VALUES (83, 2);
INSERT INTO `sys_roles_menus`
VALUES (90, 1);
INSERT INTO `sys_roles_menus`
VALUES (90, 2);
INSERT INTO `sys_roles_menus`
VALUES (92, 1);
INSERT INTO `sys_roles_menus`
VALUES (92, 2);
INSERT INTO `sys_roles_menus`
VALUES (93, 1);
INSERT INTO `sys_roles_menus`
VALUES (93, 2);
INSERT INTO `sys_roles_menus`
VALUES (94, 1);
INSERT INTO `sys_roles_menus`
VALUES (94, 2);
INSERT INTO `sys_roles_menus`
VALUES (97, 1);
INSERT INTO `sys_roles_menus`
VALUES (97, 2);
INSERT INTO `sys_roles_menus`
VALUES (98, 1);
INSERT INTO `sys_roles_menus`
VALUES (98, 2);
INSERT INTO `sys_roles_menus`
VALUES (116, 1);
INSERT INTO `sys_roles_menus`
VALUES (116, 2);
INSERT INTO `sys_roles_menus`
VALUES (118, 1);
INSERT INTO `sys_roles_menus`
VALUES (118, 2);
INSERT INTO `sys_roles_menus`
VALUES (121, 1);
INSERT INTO `sys_roles_menus`
VALUES (121, 2);
INSERT INTO `sys_roles_menus`
VALUES (122, 2);
INSERT INTO `sys_roles_menus`
VALUES (129, 1);
INSERT INTO `sys_roles_menus`
VALUES (129, 2);
INSERT INTO `sys_roles_menus`
VALUES (130, 1);
INSERT INTO `sys_roles_menus`
VALUES (131, 1);
INSERT INTO `sys_roles_menus`
VALUES (132, 1);
INSERT INTO `sys_roles_menus`
VALUES (133, 1);
INSERT INTO `sys_roles_menus`
VALUES (133, 2);
INSERT INTO `sys_roles_menus`
VALUES (134, 1);
INSERT INTO `sys_roles_menus`
VALUES (134, 2);
INSERT INTO `sys_roles_menus`
VALUES (135, 1);
INSERT INTO `sys_roles_menus`
VALUES (135, 2);
INSERT INTO `sys_roles_menus`
VALUES (136, 1);
INSERT INTO `sys_roles_menus`
VALUES (136, 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
CREATE TABLE `sys_user`
(
    `id`                         bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dept_id`                    bigint(20)           DEFAULT NULL COMMENT '部门名称',
    `username`                   varchar(255)         DEFAULT NULL COMMENT '用户名',
    `nick_name`                  varchar(255)         DEFAULT NULL COMMENT '昵称',
    `gender`                     varchar(2)           DEFAULT NULL COMMENT '性别',
    `phone`                      varchar(255)         DEFAULT NULL COMMENT '手机号码',
    `email`                      varchar(255)         DEFAULT NULL COMMENT '邮箱',
    `avatar_name`                varchar(255)         DEFAULT NULL COMMENT '头像地址',
    `avatar_path`                varchar(255)         DEFAULT NULL COMMENT '头像真实路径',
    `password`                   varchar(255)         DEFAULT NULL COMMENT '密码',
    `is_admin`                   bit(1)               DEFAULT b'0' COMMENT '是否为admin账号',
    `is_account_non_expired`     bit(1)      NOT NULL DEFAULT b'1' COMMENT '账号是否未过期：1是、0否',
    `is_account_non_locked`      bit(1)      NOT NULL DEFAULT b'1' COMMENT '账号是否未锁定：1启用、0禁用',
    `is_credentials_non_expired` bit(1)      NOT NULL DEFAULT b'1' COMMENT '账号凭证是否未过期：1是、0否',
    `enabled`                    bit(1)               DEFAULT NULL COMMENT '状态：1启用、0禁用',
    `is_must_reset_pwd`          bit(1)               DEFAULT b'0' COMMENT '是否必须修改密码：1是、0否',
    `pwd_fails_count`            int(5)      NOT NULL DEFAULT '0' COMMENT '密码连续错误次数',
    `is_tip_reset_pwd`           bit(1)               DEFAULT b'0' COMMENT '是否提示修改密码：1是、0否',
    `pwd_reset_time`             datetime             DEFAULT NULL COMMENT '修改密码的时间',
    `last_login_time`            datetime             DEFAULT NULL COMMENT '最后一次登陆时间',
    `created_by`                 varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`                 varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`                 datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`               datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_username` (`username`(50)) USING BTREE,
    UNIQUE KEY `uniq_email` (`email`(50)) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user`
VALUES (1, 2, 'admin', '管理员', '男', '18888888888', 'qq@qq.com', NULL, NULL,
        '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'1', b'1', b'1', b'1', b'1', b'0', 0, b'0',
        '2020-05-03 16:38:31', '2022-08-16 10:49:37', 'admin', 'admin', '2020-09-05 10:43:31', '2020-09-05 10:43:31');
COMMIT;

-- ----------------------------
-- Table structure for sys_users_jobs
-- ----------------------------
CREATE TABLE `sys_users_jobs`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `job_id`  bigint(20) NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (`user_id`, `job_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of sys_users_jobs
-- ----------------------------
BEGIN;
INSERT INTO `sys_users_jobs`
VALUES (1, 11);
COMMIT;

-- ----------------------------
-- Table structure for sys_users_roles
-- ----------------------------
CREATE TABLE `sys_users_roles`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='用户角色关联';

-- ----------------------------
-- Records of sys_users_roles
-- ----------------------------
BEGIN;
INSERT INTO `sys_users_roles`
VALUES (1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_version
-- ----------------------------
CREATE TABLE `sys_version`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `name`         varchar(55)         NOT NULL COMMENT '版本号名称',
    `is_activated` bit(1)              NOT NULL DEFAULT b'0' COMMENT '是否激活：0:未激活, 1:已激活',
    `remark`       text COMMENT '备注',
    `created_by`   varchar(50)         NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)         NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4 COMMENT ='版本';

-- ----------------------------
-- Records of sys_version
-- ----------------------------
BEGIN;
INSERT INTO `sys_version`
VALUES (1, '1.0.0.0', b'1', '初始化', 'admin', 'admin', '2021-09-10 16:55:06', '2021-09-10 16:55:06');
COMMIT;

-- ----------------------------
-- Table structure for tool_alipay_config
-- ----------------------------
CREATE TABLE `tool_alipay_config`
(
    `id`                      bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `app_id`                  varchar(255)         DEFAULT NULL COMMENT '应用ID',
    `charset`                 varchar(255)         DEFAULT NULL COMMENT '编码',
    `format`                  varchar(255)         DEFAULT NULL COMMENT '类型 固定格式json',
    `gateway_url`             varchar(255)         DEFAULT NULL COMMENT '网关地址',
    `notify_url`              varchar(255)         DEFAULT NULL COMMENT '异步回调',
    `private_key`             text COMMENT '私钥',
    `public_key`              text COMMENT '公钥',
    `return_url`              varchar(255)         DEFAULT NULL COMMENT '回调地址',
    `sign_type`               varchar(255)         DEFAULT NULL COMMENT '签名方式',
    `sys_service_provider_id` varchar(255)         DEFAULT NULL COMMENT '商户号',
    `created_by`              varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`              varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`              datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`            datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='支付宝配置类';

-- ----------------------------
-- Records of tool_alipay_config
-- ----------------------------
BEGIN;
INSERT INTO `tool_alipay_config`
VALUES (1, '2016091700532697', 'utf-8', 'JSON', 'https://openapi.alipaydev.com/gateway.do',
        'http://api.auauz.net/api/aliPay/notify',
        'MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC5js8sInU10AJ0cAQ8UMMyXrQ+oHZEkVt5lBwsStmTJ7YikVYgbskx1YYEXTojRsWCb+SH/kDmDU4pK/u91SJ4KFCRMF2411piYuXU/jF96zKrADznYh/zAraqT6hvAIVtQAlMHN53nx16rLzZ/8jDEkaSwT7+HvHiS+7sxSojnu/3oV7BtgISoUNstmSe8WpWHOaWv19xyS+Mce9MY4BfseFhzTICUymUQdd/8hXA28/H6osUfAgsnxAKv7Wil3aJSgaJczWuflYOve0dJ3InZkhw5Cvr0atwpk8YKBQjy5CdkoHqvkOcIB+cYHXJKzOE5tqU7inSwVbHzOLQ3XbnAgMBAAECggEAVJp5eT0Ixg1eYSqFs9568WdetUNCSUchNxDBu6wxAbhUgfRUGZuJnnAll63OCTGGck+EGkFh48JjRcBpGoeoHLL88QXlZZbC/iLrea6gcDIhuvfzzOffe1RcZtDFEj9hlotg8dQj1tS0gy9pN9g4+EBH7zeu+fyv+qb2e/v1l6FkISXUjpkD7RLQr3ykjiiEw9BpeKb7j5s7Kdx1NNIzhkcQKNqlk8JrTGDNInbDM6inZfwwIO2R1DHinwdfKWkvOTODTYa2MoAvVMFT9Bec9FbLpoWp7ogv1JMV9svgrcF9XLzANZ/OQvkbe9TV9GWYvIbxN6qwQioKCWO4GPnCAQKBgQDgW5MgfhX8yjXqoaUy/d1VjI8dHeIyw8d+OBAYwaxRSlCfyQ+tieWcR2HdTzPca0T0GkWcKZm0ei5xRURgxt4DUDLXNh26HG0qObbtLJdu/AuBUuCqgOiLqJ2f1uIbrz6OZUHns+bT/jGW2Ws8+C13zTCZkZt9CaQsrp3QOGDx5wKBgQDTul39hp3ZPwGNFeZdkGoUoViOSd5Lhowd5wYMGAEXWRLlU8z+smT5v0POz9JnIbCRchIY2FAPKRdVTICzmPk2EPJFxYTcwaNbVqL6lN7J2IlXXMiit5QbiLauo55w7plwV6LQmKm9KV7JsZs5XwqF7CEovI7GevFzyD3w+uizAQKBgC3LY1eRhOlpWOIAhpjG6qOoohmeXOphvdmMlfSHq6WYFqbWwmV4rS5d/6LNpNdL6fItXqIGd8I34jzql49taCmi+A2nlR/E559j0mvM20gjGDIYeZUz5MOE8k+K6/IcrhcgofgqZ2ZED1ksHdB/E8DNWCswZl16V1FrfvjeWSNnAoGAMrBplCrIW5xz+J0Hm9rZKrs+AkK5D4fUv8vxbK/KgxZ2KaUYbNm0xv39c+PZUYuFRCz1HDGdaSPDTE6WeWjkMQd5mS6ikl9hhpqFRkyh0d0fdGToO9yLftQKOGE/q3XUEktI1XvXF0xyPwNgUCnq0QkpHyGVZPtGFxwXiDvpvgECgYA5PoB+nY8iDiRaJNko9w0hL4AeKogwf+4TbCw+KWVEn6jhuJa4LFTdSqp89PktQaoVpwv92el/AhYjWOl/jVCm122f9b7GyoelbjMNolToDwe5pF5RnSpEuDdLy9MfE8LnE3PlbE7E5BipQ3UjSebkgNboLHH/lNZA5qvEtvbfvQ==',
        'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAut9evKRuHJ/2QNfDlLwvN/S8l9hRAgPbb0u61bm4AtzaTGsLeMtScetxTWJnVvAVpMS9luhEJjt+Sbk5TNLArsgzzwARgaTKOLMT1TvWAK5EbHyI+eSrc3s7Awe1VYGwcubRFWDm16eQLv0k7iqiw+4mweHSz/wWyvBJVgwLoQ02btVtAQErCfSJCOmt0Q/oJQjj08YNRV4EKzB19+f5A+HQVAKy72dSybTzAK+3FPtTtNen/+b5wGeat7c32dhYHnGorPkPeXLtsqqUTp1su5fMfd4lElNdZaoCI7osZxWWUo17vBCZnyeXc9fk0qwD9mK6yRAxNbrY72Xx5VqIqwIDAQAB',
        'http://api.auauz.net/api/aliPay/return', 'RSA2', '2088102176044281', 'System', 'System', '2021-09-10 16:55:06',
        '2021-09-10 16:55:06');
COMMIT;

-- ----------------------------
-- Table structure for tool_email_config
-- ----------------------------
CREATE TABLE `tool_email_config`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `from_user`    varchar(255)         DEFAULT NULL COMMENT '收件人',
    `host`         varchar(255)         DEFAULT NULL COMMENT '邮件服务器SMTP地址',
    `pass`         varchar(255)         DEFAULT NULL COMMENT '密码',
    `port`         varchar(255)         DEFAULT NULL COMMENT '端口',
    `user`         varchar(255)         DEFAULT NULL COMMENT '发件者用户名',
    `created_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='邮箱配置';

-- ----------------------------
-- Records of tool_email_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tool_local_storage
-- ----------------------------
CREATE TABLE `tool_local_storage`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `real_name`    varchar(255)         DEFAULT NULL COMMENT '文件真实的名称',
    `name`         varchar(255)         DEFAULT NULL COMMENT '文件名',
    `suffix`       varchar(255)         DEFAULT NULL COMMENT '后缀',
    `path`         varchar(255)         DEFAULT NULL COMMENT '路径',
    `type`         varchar(255)         DEFAULT NULL COMMENT '类型',
    `size`         varchar(100)         DEFAULT NULL COMMENT '大小',
    `created_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='本地存储';

-- ----------------------------
-- Records of tool_local_storage
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tool_qiniu_config
-- ----------------------------
CREATE TABLE `tool_qiniu_config`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `access_key`   text COMMENT 'accessKey',
    `bucket`       varchar(255)          DEFAULT NULL COMMENT 'Bucket 识别符',
    `host`         varchar(255) NOT NULL COMMENT '外链域名',
    `secret_key`   text COMMENT 'secretKey',
    `type`         varchar(255)          DEFAULT NULL COMMENT '空间类型',
    `zone`         varchar(255)          DEFAULT NULL COMMENT '机房',
    `created_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)  NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='七牛云配置';

-- ----------------------------
-- Records of tool_qiniu_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tool_qiniu_content
-- ----------------------------
CREATE TABLE `tool_qiniu_content`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `bucket`       varchar(255)         DEFAULT NULL COMMENT 'Bucket 识别符',
    `name`         varchar(255)         DEFAULT NULL COMMENT '文件名称',
    `size`         varchar(255)         DEFAULT NULL COMMENT '文件大小',
    `type`         varchar(255)         DEFAULT NULL COMMENT '文件类型：私有或公开',
    `url`          varchar(255)         DEFAULT NULL COMMENT '文件url',
    `suffix`       varchar(255)         DEFAULT NULL COMMENT '文件后缀',
    `created_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='七牛云文件存储';

-- ----------------------------
-- Records of tool_qiniu_content
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for bpm_form
-- ----------------------------
CREATE TABLE `bpm_form`
(
    `id`           bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name`         varchar(64)   NOT NULL COMMENT '表单名',
    `content`      text(0)       NOT NULL COMMENT '表单的配置',
    `remark`       varchar(255)           DEFAULT NULL COMMENT '备注',
    `created_by`   varchar(50)   NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`   varchar(50)   NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`   datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT = '工作流的表单定义';

-- ----------------------------
-- Table structure for bpm_oa_leave
-- ----------------------------
CREATE TABLE `bpm_oa_leave`
(
    `id`                  bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '请假表单主键',
    `user_id`             bigint(20)   NOT NULL COMMENT '申请人的用户编号',
    `type`                tinyint(4)   NOT NULL COMMENT '请假类型',
    `reason`              varchar(200) NOT NULL COMMENT '请假原因',
    `start_time`          datetime(0)  NOT NULL COMMENT '开始时间',
    `end_time`            datetime(0)  NOT NULL COMMENT '结束时间',
    `day`                 tinyint(4)   NOT NULL COMMENT '请假天数',
    `result`              tinyint(4)   NOT NULL COMMENT '请假结果',
    `process_instance_id` varchar(64)           DEFAULT NULL COMMENT '流程实例的编号',
    `created_by`          varchar(50)  NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`          varchar(50)  NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT = 'OA 请假申请表';

-- ----------------------------
-- Table structure for bpm_process_definition_ext
-- ----------------------------
CREATE TABLE `bpm_process_definition_ext`
(
    `id`                      bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '编号',
    `process_definition_id`   varchar(64) NOT NULL COMMENT '流程定义的编号',
    `model_id`                varchar(64) NOT NULL COMMENT '流程模型的编号',
    `description`             varchar(255)         DEFAULT NULL COMMENT '描述',
    `form_type`               tinyint(4)  NOT NULL COMMENT '表单类型',
    `form_id`                 bigint(20)           DEFAULT NULL COMMENT '表单编号',
    `form_conf`               varchar(1000)        DEFAULT NULL COMMENT '表单的配置',
    `form_fields`             varchar(5000)        DEFAULT NULL COMMENT '表单项的数组',
    `form_custom_create_path` varchar(255)         DEFAULT NULL COMMENT '自定义表单的提交路径',
    `form_custom_view_path`   varchar(255)         DEFAULT NULL COMMENT '自定义表单的查看路径',
    `created_by`              varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`              varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`              datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`            datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT = 'Bpm 流程定义的拓展表';

-- ----------------------------
-- Table structure for bpm_process_instance_ext
-- ----------------------------
CREATE TABLE `bpm_process_instance_ext`
(
    `id`                    bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '编号',
    `start_user_id`         bigint(20)  NOT NULL COMMENT '发起流程的用户编号',
    `name`                  varchar(64)          DEFAULT NULL COMMENT '流程实例的名字',
    `process_instance_id`   varchar(64) NOT NULL COMMENT '流程实例的编号',
    `process_definition_id` varchar(64) NOT NULL COMMENT '流程定义的编号',
    `category`              varchar(64)          DEFAULT NULL COMMENT '流程分类',
    `status`                tinyint(4)  NOT NULL COMMENT '流程实例的状态',
    `result`                tinyint(4)  NOT NULL COMMENT '流程实例的结果',
    `end_time`              datetime(0)          DEFAULT NULL COMMENT '结束时间',
    `form_variables`        varchar(5000)        DEFAULT NULL COMMENT '表单值',
    `created_by`            varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`            varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`            datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`          datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT = '工作流的流程实例的拓展';

-- ----------------------------
-- Table structure for bpm_task_assign_rule
-- ----------------------------
CREATE TABLE `bpm_task_assign_rule`
(
    `id`                    bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '编号',
    `model_id`              varchar(64)   NOT NULL COMMENT '流程模型的编号',
    `process_definition_id` varchar(64)   NOT NULL COMMENT '流程定义的编号',
    `task_definition_key`   varchar(64)   NOT NULL COMMENT '流程任务定义的 key',
    `type`                  tinyint(4)    NOT NULL COMMENT '规则类型',
    `options`               varchar(1024) NOT NULL COMMENT '规则值，JSON 数组',
    `created_by`            varchar(50)   NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`            varchar(50)   NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`            datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`          datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT = 'Bpm 任务规则表';

-- ----------------------------
-- Table structure for bpm_task_ext
-- ----------------------------
CREATE TABLE `bpm_task_ext`
(
    `id`                    bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '编号',
    `assignee_user_id`      bigint(20)           DEFAULT NULL COMMENT '任务的审批人',
    `name`                  varchar(64)          DEFAULT NULL COMMENT '任务的名字',
    `task_id`               varchar(64) NOT NULL COMMENT '任务的编号',
    `result`                tinyint(4)  NOT NULL COMMENT '任务的结果',
    `reason`                varchar(255)         DEFAULT NULL COMMENT '审批建议',
    `end_time`              datetime(0)          DEFAULT NULL COMMENT '任务的结束时间',
    `process_instance_id`   varchar(64) NOT NULL COMMENT '流程实例的编号',
    `process_definition_id` varchar(64) NOT NULL COMMENT '流程定义的编号',
    `created_by`            varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`            varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`            datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`          datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT = '工作流的流程任务的拓展表';

-- ----------------------------
-- Table structure for bpm_user_group
-- ----------------------------
CREATE TABLE `bpm_user_group`
(
    `id`              bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name`            varchar(30)   NOT NULL DEFAULT '' COMMENT '组名',
    `description`     varchar(255)  NOT NULL DEFAULT '' COMMENT '描述',
    `member_user_ids` varchar(1024) NOT NULL DEFAULT '0' COMMENT '成员编号数组',
    `status`          tinyint(4)    NOT NULL COMMENT '状态（0正常 1停用）',
    `created_by`      varchar(50)   NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by`      varchar(50)   NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT = '用户组';


SET FOREIGN_KEY_CHECKS = 1;
