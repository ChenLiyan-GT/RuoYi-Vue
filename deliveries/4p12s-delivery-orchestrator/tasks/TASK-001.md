# TASK-001: 数据库表结构创建

## 任务信息

| 属性 | 值 |
| --- | --- |
| 任务 ID | TASK-001 |
| 任务名称 | 数据库表结构创建 |
| 模块 | 基础设施 |
| 优先级 | P0 |
| 预估工时 | 4 小时 |
| 状态 | passed |
| 依赖任务 | 无 |

## 任务目标

创建作业安排系统所需的所有数据库表结构，并初始化基础数据。

## 实施内容

### 1. 创建数据库表

基于 `design.md` 中的数据设计，创建以下表：

1. `work_employee` - 员工信息表
2. `work_position_type` - 职能类型表
3. `work_level_rule` - 级别规则表
4. `work_stage_template` - 作业阶段模板表
5. `work_job` - 作业主表
6. `work_job_stage` - 作业阶段表
7. `work_assignment` - 作业分配表
8. `work_timesheet` - 工时记录表
9. `work_progress_log` - 进度更新记录表

### 2. 初始化基础数据

插入以下基础数据：

- 职能类型数据（前端、后端、测试、产品、项目管理）
- 级别规则数据（P1-P10）
- 默认作业阶段模板

### 3. 创建 SQL 脚本文件

在 `sql` 目录下创建 `work_management.sql` 文件。

## 实施步骤

### 步骤 1: 编写 SQL 脚本

创建 `sql/work_management.sql` 文件，包含：

```sql
-- 1. 员工信息表
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

-- 2. 职能类型表
CREATE TABLE `work_position_type` (
  `type_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类型 ID',
  `type_code` varchar(32) NOT NULL COMMENT '类型编码',
  `type_name` varchar(64) NOT NULL COMMENT '类型名称',
  `sort_order` int(4) DEFAULT '0' COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 正常 1 停用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `uk_type_code` (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职能类型表';

-- 3. 级别规则表
CREATE TABLE `work_level_rule` (
  `rule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规则 ID',
  `level` int(2) NOT NULL COMMENT '职级',
  `level_name` varchar(64) DEFAULT NULL COMMENT '级别名称',
  `min_stage` varchar(32) DEFAULT NULL COMMENT '可执行最低阶段',
  `max_stage` varchar(32) DEFAULT NULL COMMENT '可执行最高阶段',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`rule_id`),
  UNIQUE KEY `uk_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='级别规则表';

-- 4. 作业阶段模板表
CREATE TABLE `work_stage_template` (
  `template_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板 ID',
  `template_name` varchar(128) NOT NULL COMMENT '模板名称',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '适用部门 ID(NULL 通用)',
  `stages_config` text NOT NULL COMMENT '阶段配置 (JSON)',
  `is_default` char(1) DEFAULT '0' COMMENT '是否默认 (0 否 1 是)',
  `version` int(4) DEFAULT '1' COMMENT '版本号',
  `usage_count` int(11) DEFAULT '0' COMMENT '使用次数',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 启用 1 禁用)',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`template_id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业阶段模板表';

-- 5. 作业主表
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

-- 6. 作业阶段表
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
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`job_stage_id`),
  KEY `idx_job_id` (`job_id`),
  KEY `idx_stage_code` (`stage_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业阶段表';

-- 7. 作业分配表
CREATE TABLE `work_assignment` (
  `assign_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分配 ID',
  `job_stage_id` bigint(20) NOT NULL COMMENT '作业阶段 ID',
  `employee_id` bigint(20) NOT NULL COMMENT '员工 ID',
  `workload` decimal(10,2) DEFAULT '0.00' COMMENT '分配工时',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 进行中 1 已完成 2 已取消)',
  `assign_by` varchar(64) DEFAULT '' COMMENT '分配人',
  `assign_time` datetime DEFAULT NULL COMMENT '分配时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`assign_id`),
  KEY `idx_job_stage` (`job_stage_id`),
  KEY `idx_employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业分配表';

-- 8. 工时记录表
CREATE TABLE `work_timesheet` (
  `timesheet_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工时记录 ID',
  `assign_id` bigint(20) NOT NULL COMMENT '分配 ID',
  `work_date` date NOT NULL COMMENT '工作日期',
  `workload` decimal(10,2) NOT NULL COMMENT '工时数',
  `content` varchar(512) DEFAULT NULL COMMENT '工作内容',
  `submit_by` varchar(64) DEFAULT '' COMMENT '提交人',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0 草稿 1 已提交 2 已审核)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`timesheet_id`),
  KEY `idx_assign_id` (`assign_id`),
  KEY `idx_work_date` (`work_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工时记录表';

-- 9. 进度更新记录表
CREATE TABLE `work_progress_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志 ID',
  `job_stage_id` bigint(20) NOT NULL COMMENT '作业阶段 ID',
  `employee_id` bigint(20) NOT NULL COMMENT '员工 ID',
  `progress` decimal(5,2) NOT NULL COMMENT '进度 (%)',
  `prev_progress` decimal(5,2) DEFAULT '0.00' COMMENT '上次进度',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_job_stage` (`job_stage_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='进度更新记录表';
```

### 步骤 2: 初始化基础数据

在 SQL 脚本中添加初始化数据：

```sql
-- 初始化职能类型
INSERT INTO work_position_type VALUES 
(1, 'frontend', '前端', 1, '0', NOW(), NOW()),
(2, 'backend', '后端', 2, '0', NOW(), NOW()),
(3, 'test', '测试', 3, '0', NOW(), NOW()),
(4, 'product', '产品', 4, '0', NOW(), NOW()),
(5, 'pm', '项目管理', 5, '0', NOW(), NOW());

-- 初始化级别规则
INSERT INTO work_level_rule VALUES 
(1, 1, '初级 P1', '单体测试', '开发', 'P1 入门级'),
(2, 2, '初级 P2', '单体测试', '开发', 'P2 入门级'),
(3, 3, '初级 P3', '单体测试', '开发', 'P3 初级员工'),
(4, 4, '中级 P4', '开发', '结合测试', 'P4 中级员工'),
(5, 5, '高级 P5', '内部设计', '综合测试', 'P5 高级员工'),
(6, 6, '资深 P6', '基本设计', '内部设计', 'P6 资深员工'),
(7, 7, '专家 P7', '需求分析', '基本设计', 'P7 专家'),
(8, 8, '高级专家 P8', '需求分析', '基本设计', 'P8 高级专家'),
(9, 9, '资深专家 P9', '需求分析', '需求分析', 'P9 资深专家'),
(10, 10, '首席专家 P10', '需求分析', '需求分析', 'P10 首席专家');

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
'1', 1, 0, '0', 'admin', NOW(), 'admin', NOW());
```

### 步骤 3: 执行 SQL 脚本

在 MySQL 数据库中执行 SQL 脚本：

```bash
mysql -u root -p ruoyi < sql/work_management.sql
```

### 步骤 4: 验证表结构

执行以下 SQL 验证表是否创建成功：

```sql
SHOW TABLES LIKE 'work_%';
```

应返回 9 个表。

## 测试要求

### TDD Checkbox

- [ ] SQL 脚本文件创建成功
- [ ] 9 个数据库表全部创建成功
- [ ] 职能类型数据插入成功（5 条）
- [ ] 级别规则数据插入成功（10 条）
- [ ] 默认模板数据插入成功（1 条）
- [ ] 表结构验证通过（字段、索引、注释）

### 红灯验证

执行 SQL 脚本前，确认表不存在：

```sql
SHOW TABLES LIKE 'work_employee';
-- 应返回空结果
```

### 绿灯验证

执行 SQL 脚本后，验证表和数据：

```sql
-- 验证表数量
SELECT COUNT(*) FROM information_schema.tables 
WHERE table_schema = 'ruoyi' AND table_name LIKE 'work_%';
-- 应返回 9

-- 验证职能类型数据
SELECT COUNT(*) FROM work_position_type;
-- 应返回 5

-- 验证级别规则数据
SELECT COUNT(*) FROM work_level_rule;
-- 应返回 10

-- 验证默认模板
SELECT template_name FROM work_stage_template WHERE is_default = '1';
-- 应返回 '标准软件开发模板'
```

## 输出产物

| 产物 | 路径 | 说明 |
| --- | --- | --- |
| SQL 脚本 | `sql/work_management.sql` | 包含表结构和初始化数据 |

## 需求追溯

- _需求：US-002 员工级别和职能配置_
- _需求：US-003 作业阶段模板管理_

## 完成标准

1. SQL 脚本文件创建在正确位置
2. 所有表结构创建成功，字段、索引、注释完整
3. 基础数据初始化成功
4. TDD Checkbox 全部勾选
5. 绿灯验证命令全部通过

## 备注

- 执行 SQL 脚本前请备份数据库
- 如表已存在，需要先 DROP 或修改脚本添加 IF NOT EXISTS
- 字符集使用 utf8mb4