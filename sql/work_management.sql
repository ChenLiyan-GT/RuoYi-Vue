-- ============================================================
-- 作业安排系统数据库脚本
-- 基于 RuoYi-Vue 框架
-- 创建时间：2026-07-17
-- ============================================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------
-- 1. 员工信息表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `work_employee`;
CREATE TABLE `work_employee` (
  `employee_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '员工 ID',
  `employee_no` varchar(32) NOT NULL COMMENT '员工工号',
  `employee_name` varchar(64) NOT NULL COMMENT '员工姓名',
  `dept_id` bigint(20) NOT NULL COMMENT '部门 ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '关联用户 ID',
  `position_type` varchar(32) NOT NULL COMMENT '职能类型',
  `position_level` int(2) NOT NULL COMMENT '职级 (1-10)',
  `skills` varchar(1024) DEFAULT NULL COMMENT '技能标签 (JSON)',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 空闲 1 工作中 2 过载)',
  `current_workload` decimal(10,2) DEFAULT '0.00' COMMENT '当前工时负载',
  `max_workload` decimal(10,2) DEFAULT '40.00' COMMENT '最大工时容量',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `uk_employee_no` (`employee_no`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_position` (`position_type`, `position_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息表';

-- ------------------------------------------------------------
-- 2. 职能类型表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `work_position_type`;
CREATE TABLE `work_position_type` (
  `type_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类型 ID',
  `type_code` varchar(32) NOT NULL COMMENT '类型编码',
  `type_name` varchar(64) NOT NULL COMMENT '类型名称',
  `sort_order` int(4) DEFAULT '0' COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 正常 1 停用)',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `uk_type_code` (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职能类型表';

-- ------------------------------------------------------------
-- 3. 级别规则表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `work_level_rule`;
CREATE TABLE `work_level_rule` (
  `rule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规则 ID',
  `level` int(2) NOT NULL COMMENT '职级',
  `level_name` varchar(64) DEFAULT NULL COMMENT '级别名称',
  `min_stage` varchar(32) DEFAULT NULL COMMENT '可执行最低阶段',
  `max_stage` varchar(32) DEFAULT NULL COMMENT '可执行最高阶段',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`rule_id`),
  UNIQUE KEY `uk_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='级别规则表';

-- ------------------------------------------------------------
-- 4. 作业阶段模板表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `work_stage_template`;
CREATE TABLE `work_stage_template` (
  `template_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板 ID',
  `template_name` varchar(128) NOT NULL COMMENT '模板名称',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '适用部门 ID(NULL 通用)',
  `stages_config` text NOT NULL COMMENT '阶段配置 (JSON)',
  `is_default` char(1) DEFAULT '0' COMMENT '是否默认 (0 否 1 是)',
  `version` int(4) DEFAULT '1' COMMENT '版本号',
  `usage_count` int(11) DEFAULT '0' COMMENT '使用次数',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 启用 1 禁用)',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`template_id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业阶段模板表';

-- ------------------------------------------------------------
-- 5. 作业主表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `work_job`;
CREATE TABLE `work_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '作业 ID',
  `job_no` varchar(32) NOT NULL COMMENT '作业编号',
  `job_name` varchar(128) NOT NULL COMMENT '作业名称',
  `dept_id` bigint(20) NOT NULL COMMENT '部门 ID',
  `template_id` bigint(20) NOT NULL COMMENT '阶段模板 ID',
  `total_workload` decimal(10,2) NOT NULL COMMENT '总工时',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 未开始 1 进行中 2 已完成 3 已取消)',
  `start_date` date DEFAULT NULL COMMENT '计划开始日期',
  `end_date` date DEFAULT NULL COMMENT '计划结束日期',
  `actual_end_date` date DEFAULT NULL COMMENT '实际结束日期',
  `progress` decimal(5,2) DEFAULT '0.00' COMMENT '整体进度 (%)',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`job_id`),
  UNIQUE KEY `uk_job_no` (`job_no`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业主表';

-- ------------------------------------------------------------
-- 6. 作业阶段表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `work_job_stage`;
CREATE TABLE `work_job_stage` (
  `job_stage_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '作业阶段 ID',
  `job_id` bigint(20) NOT NULL COMMENT '作业 ID',
  `stage_code` varchar(32) NOT NULL COMMENT '阶段编码',
  `stage_name` varchar(64) NOT NULL COMMENT '阶段名称',
  `stage_order` int(4) NOT NULL COMMENT '阶段顺序',
  `planned_workload` decimal(10,2) NOT NULL COMMENT '计划工时',
  `actual_workload` decimal(10,2) DEFAULT '0.00' COMMENT '实际工时',
  `min_level` int(2) NOT NULL COMMENT '最低级别要求',
  `required_skills` varchar(512) DEFAULT NULL COMMENT '技能要求 (JSON)',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 未开始 1 进行中 2 已完成)',
  `assigned_employees` text COMMENT '已分配员工 (JSON)',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `progress` decimal(5,2) DEFAULT '0.00' COMMENT '进度 (%)',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`job_stage_id`),
  KEY `idx_job_id` (`job_id`),
  KEY `idx_stage_code` (`stage_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业阶段表';

-- ------------------------------------------------------------
-- 7. 作业分配表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `work_assignment`;
CREATE TABLE `work_assignment` (
  `assign_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分配 ID',
  `job_stage_id` bigint(20) NOT NULL COMMENT '作业阶段 ID',
  `employee_id` bigint(20) NOT NULL COMMENT '员工 ID',
  `workload` decimal(10,2) DEFAULT '0.00' COMMENT '分配工时',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 进行中 1 已完成 2 已取消)',
  `assign_by` varchar(64) DEFAULT '' COMMENT '分配人',
  `assign_time` datetime DEFAULT NULL COMMENT '分配时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`assign_id`),
  KEY `idx_job_stage` (`job_stage_id`),
  KEY `idx_employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业分配表';

-- ------------------------------------------------------------
-- 8. 工时记录表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `work_timesheet`;
CREATE TABLE `work_timesheet` (
  `timesheet_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工时记录 ID',
  `assign_id` bigint(20) NOT NULL COMMENT '分配 ID',
  `work_date` date NOT NULL COMMENT '工作日期',
  `workload` decimal(10,2) NOT NULL COMMENT '工时数',
  `content` varchar(512) DEFAULT NULL COMMENT '工作内容',
  `submit_by` varchar(64) DEFAULT '' COMMENT '提交人',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 草稿 1 已提交 2 已审核)',
  `audit_by` varchar(64) DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_comment` varchar(256) DEFAULT NULL COMMENT '审核意见',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`timesheet_id`),
  KEY `idx_assign_id` (`assign_id`),
  KEY `idx_work_date` (`work_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工时记录表';

-- ------------------------------------------------------------
-- 9. 进度更新记录表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `work_progress_log`;
CREATE TABLE `work_progress_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志 ID',
  `job_stage_id` bigint(20) NOT NULL COMMENT '作业阶段 ID',
  `employee_id` bigint(20) NOT NULL COMMENT '员工 ID',
  `progress` decimal(5,2) NOT NULL COMMENT '进度 (%)',
  `prev_progress` decimal(5,2) DEFAULT '0.00' COMMENT '上次进度',
  `workload` decimal(10,2) DEFAULT '0.00' COMMENT '工时数',
  `content` varchar(512) DEFAULT NULL COMMENT '工作内容',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`log_id`),
  KEY `idx_job_stage` (`job_stage_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='进度更新记录表';

-- ------------------------------------------------------------
-- 初始化基础数据
-- ------------------------------------------------------------

-- 初始化职能类型
INSERT INTO work_position_type VALUES 
(1, 'frontend', '前端', 1, '0', '0', '', NOW(), '', NOW(), NULL),
(2, 'backend', '后端', 2, '0', '0', '', NOW(), '', NOW(), NULL),
(3, 'test', '测试', 3, '0', '0', '', NOW(), '', NOW(), NULL),
(4, 'product', '产品', 4, '0', '0', '', NOW(), '', NOW(), NULL),
(5, 'pm', '项目管理', 5, '0', '0', '', NOW(), '', NOW(), NULL);

-- 初始化级别规则
INSERT INTO work_level_rule VALUES 
(1, 1, '初级 P1', '单体测试', '开发', 'P1 入门级', '0', '', NOW(), '', NOW(), NULL),
(2, 2, '初级 P2', '单体测试', '开发', 'P2 入门级', '0', '', NOW(), '', NOW(), NULL),
(3, 3, '初级 P3', '单体测试', '开发', 'P3 初级员工', '0', '', NOW(), '', NOW(), NULL),
(4, 4, '中级 P4', '开发', '结合测试', 'P4 中级员工', '0', '', NOW(), '', NOW(), NULL),
(5, 5, '高级 P5', '内部设计', '综合测试', 'P5 高级员工', '0', '', NOW(), '', NOW(), NULL),
(6, 6, '资深 P6', '基本设计', '内部设计', 'P6 资深员工', '0', '', NOW(), '', NOW(), NULL),
(7, 7, '专家 P7', '需求分析', '基本设计', 'P7 专家', '0', '', NOW(), '', NOW(), NULL),
(8, 8, '高级专家 P8', '需求分析', '基本设计', 'P8 高级专家', '0', '', NOW(), '', NOW(), NULL),
(9, 9, '资深专家 P9', '需求分析', '需求分析', 'P9 资深专家', '0', '', NOW(), '', NOW(), NULL),
(10, 10, '首席专家 P10', '需求分析', '需求分析', 'P10 首席专家', '0', '', NOW(), '', NOW(), NULL);

-- 初始化默认作业阶段模板
INSERT INTO work_stage_template VALUES 
(1, '标准软件开发模板', NULL, 
'[{"stage_code":"requirements","stage_name":"需求分析","stage_order":1,"workload_ratio":0.10,"min_level":9,"required_skills":["需求分析","业务理解"]},
  {"stage_code":"basic_design","stage_name":"基本设计","stage_order":2,"workload_ratio":0.15,"min_level":6,"required_skills":["系统设计","架构设计"]},
  {"stage_code":"detail_design","stage_name":"内部设计","stage_order":3,"workload_ratio":0.15,"min_level":6,"required_skills":["详细设计"]},
  {"stage_code":"development","stage_name":"开发","stage_order":4,"workload_ratio":0.30,"min_level":4,"required_skills":["编码实现"]},
  {"stage_code":"unit_test","stage_name":"单体测试","stage_order":5,"workload_ratio":0.10,"min_level":3,"required_skills":["单元测试"]},
  {"stage_code":"integration_test","stage_name":"结合测试","stage_order":6,"workload_ratio":0.10,"min_level":4,"required_skills":["集成测试"]},
  {"stage_code":"system_test","stage_name":"综合测试","stage_order":7,"workload_ratio":0.05,"min_level":5,"required_skills":["系统测试"]},
  {"stage_code":"release","stage_name":"上线","stage_order":8,"workload_ratio":0.05,"min_level":7,"required_skills":["发布部署"]}]',
'1', 1, 0, '0', '0', 'admin', NOW(), 'admin', NOW(), NULL);

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 脚本结束
-- ============================================================