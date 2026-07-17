-- 作业管理模块菜单权限配置 SQL
-- 执行前请确认 sys_menu 表结构

-- 删除已存在的作业管理相关菜单（如需要重新安装）
-- DELETE FROM sys_menu WHERE menu_name LIKE '%作业%';

-- 1. 作业管理主菜单 (parent_id=0 表示顶级菜单，或根据实际父菜单 ID 调整)
INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('作业管理', 0, 5, '#', 'menuItem', 'M', '0', '1', '#', 'fa fa-tasks', '作业管理目录', sysdate());

-- 获取刚插入的主菜单 ID（需要根据实际数据库调整，这里假设使用 last_insert_id() 或手动查询）
-- 假设主菜单 ID 为 2000，请根据实际插入后的 ID 修改以下所有 parent_id

-- 2. 员工管理子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('员工管理', 2000, 1, '/system/work/employee', 'menuItem', 'C', '0', '1', 'system:work:employee:view', 'fa fa-user', '员工信息管理', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('员工新增', 2000, 2, '#', 'menuItem', 'F', '0', '1', 'system:work:employee:add', 'fa fa-plus', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('员工修改', 2000, 3, '#', 'menuItem', 'F', '0', '1', 'system:work:employee:edit', 'fa fa-edit', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('员工删除', 2000, 4, '#', 'menuItem', 'F', '0', '1', 'system:work:employee:remove', 'fa fa-trash', '', sysdate());

-- 3. 职能类型管理子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('职能类型管理', 2000, 5, '/system/work/positionType', 'menuItem', 'C', '0', '1', 'system:work:positionType:view', 'fa fa-sitemap', '职能类型配置', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('职能类型新增', 2000, 6, '#', 'menuItem', 'F', '0', '1', 'system:work:positionType:add', 'fa fa-plus', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('职能类型修改', 2000, 7, '#', 'menuItem', 'F', '0', '1', 'system:work:positionType:edit', 'fa fa-edit', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('职能类型删除', 2000, 8, '#', 'menuItem', 'F', '0', '1', 'system:work:positionType:remove', 'fa fa-trash', '', sysdate());

-- 4. 级别规则管理子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('级别规则管理', 2000, 9, '/system/work/levelRule', 'menuItem', 'C', '0', '1', 'system:work:levelRule:view', 'fa fa-list-ol', '职级规则配置', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('级别规则新增', 2000, 10, '#', 'menuItem', 'F', '0', '1', 'system:work:levelRule:add', 'fa fa-plus', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('级别规则修改', 2000, 11, '#', 'menuItem', 'F', '0', '1', 'system:work:levelRule:edit', 'fa fa-edit', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('级别规则删除', 2000, 12, '#', 'menuItem', 'F', '0', '1', 'system:work:levelRule:remove', 'fa fa-trash', '', sysdate());

-- 5. 作业阶段模板管理子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('作业阶段模板', 2000, 13, '/system/work/stageTemplate', 'menuItem', 'C', '0', '1', 'system:work:stageTemplate:view', 'fa fa-copy', '作业阶段模板配置', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('模板新增', 2000, 14, '#', 'menuItem', 'F', '0', '1', 'system:work:stageTemplate:add', 'fa fa-plus', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('模板修改', 2000, 15, '#', 'menuItem', 'F', '0', '1', 'system:work:stageTemplate:edit', 'fa fa-edit', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('模板删除', 2000, 16, '#', 'menuItem', 'F', '0', '1', 'system:work:stageTemplate:remove', 'fa fa-trash', '', sysdate());

-- 6. 作业管理子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('作业管理', 2000, 17, '/system/work/job', 'menuItem', 'C', '0', '1', 'system:work:job:view', 'fa fa-briefcase', '作业信息管理', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('作业新增', 2000, 18, '#', 'menuItem', 'F', '0', '1', 'system:work:job:add', 'fa fa-plus', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('作业修改', 2000, 19, '#', 'menuItem', 'F', '0', '1', 'system:work:job:edit', 'fa fa-edit', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('作业删除', 2000, 20, '#', 'menuItem', 'F', '0', '1', 'system:work:job:remove', 'fa fa-trash', '', sysdate());

-- 7. 作业阶段管理子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('作业阶段管理', 2000, 21, '/system/work/jobStage', 'menuItem', 'C', '0', '1', 'system:work:jobStage:view', 'fa fa-list', '作业阶段管理', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('阶段新增', 2000, 22, '#', 'menuItem', 'F', '0', '1', 'system:work:jobStage:add', 'fa fa-plus', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('阶段修改', 2000, 23, '#', 'menuItem', 'F', '0', '1', 'system:work:jobStage:edit', 'fa fa-edit', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('阶段删除', 2000, 24, '#', 'menuItem', 'F', '0', '1', 'system:work:jobStage:remove', 'fa fa-trash', '', sysdate());

-- 8. 作业分配管理子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('作业分配管理', 2000, 25, '/system/work/assignment', 'menuItem', 'C', '0', '1', 'system:work:assignment:view', 'fa fa-users', '作业分配管理', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('分配新增', 2000, 26, '#', 'menuItem', 'F', '0', '1', 'system:work:assignment:add', 'fa fa-plus', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('分配修改', 2000, 27, '#', 'menuItem', 'F', '0', '1', 'system:work:assignment:edit', 'fa fa-edit', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('分配删除', 2000, 28, '#', 'menuItem', 'F', '0', '1', 'system:work:assignment:remove', 'fa fa-trash', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('智能分配', 2000, 29, '#', 'menuItem', 'F', '0', '1', 'system:work:assignment:smartAssign', 'fa fa-magic', '智能分配算法', sysdate());

-- 9. 工时记录管理子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('工时记录管理', 2000, 30, '/system/work/timesheet', 'menuItem', 'C', '0', '1', 'system:work:timesheet:view', 'fa fa-clock-o', '工时记录管理', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('工时新增', 2000, 31, '#', 'menuItem', 'F', '0', '1', 'system:work:timesheet:add', 'fa fa-plus', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('工时修改', 2000, 32, '#', 'menuItem', 'F', '0', '1', 'system:work:timesheet:edit', 'fa fa-edit', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('工时删除', 2000, 33, '#', 'menuItem', 'F', '0', '1', 'system:work:timesheet:remove', 'fa fa-trash', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('工时提交', 2000, 34, '#', 'menuItem', 'F', '0', '1', 'system:work:timesheet:submit', 'fa fa-check', '提交工时', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('工时审核', 2000, 35, '#', 'menuItem', 'F', '0', '1', 'system:work:timesheet:audit', 'fa fa-gavel', '审核工时', sysdate());

-- 10. 进度更新记录子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('进度更新记录', 2000, 36, '/system/work/progressLog', 'menuItem', 'C', '0', '1', 'system:work:progressLog:view', 'fa fa-line-chart', '进度更新记录', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('进度记录', 2000, 37, '#', 'menuItem', 'F', '0', '1', 'system:work:progressLog:add', 'fa fa-plus', '', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, remark, create_time)
VALUES ('进度删除', 2000, 38, '#', 'menuItem', 'F', '0', '1', 'system:work:progressLog:remove', 'fa fa-trash', '', sysdate());

-- 注意：执行后请根据实际插入的主菜单 ID 更新所有子菜单的 parent_id