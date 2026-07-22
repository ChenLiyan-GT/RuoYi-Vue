# 模块边界

> 证据来源：[wiki/业务逻辑层/业务逻辑层.md](../wiki/业务逻辑层/业务逻辑层.md)

本文档定义了 RuoYi-Vue 项目的模块边界和通信规则。

## 1. 模块依赖图

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ ruoyi-admin │ ──→ │ ruoyi-framework │ ──→ │ ruoyi-system │ ──→ │ ruoyi-common │
└─────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
┌─────────────┐                                              ┌──────────────┐
│ ruoyi-quartz │ ──────────────────────────────────────────→ │ ruoyi-common │
└─────────────┘                                              └──────────────┘
┌────────────────┐                                          ┌──────────────┐
│ ruoyi-generator │ ──────────────────────────────────────→ │ ruoyi-common │
└────────────────┘                                          └──────────────┘
```

> 证据来源：[wiki/业务逻辑层/业务逻辑层.md - 项目结构概述](../wiki/业务逻辑层/业务逻辑层.md)

## 2. 模块职责边界

> 证据来源：[wiki/业务逻辑层/业务逻辑层.md - 核心应用服务](../wiki/业务逻辑层/业务逻辑层.md)

| 模块 | 职责 | 禁止行为 |
|------|------|----------|
| ruoyi-admin | Controller、模板、启动类、配置 | 禁止包含业务逻辑 |
| ruoyi-framework | Shiro 认证、AOP 切面、拦截器、全局异常 | 禁止直接操作数据库 |
| ruoyi-system | 业务 Service/domain/mapper、Work 扩展 | 禁止依赖 admin 或 framework |
| ruoyi-common | 注解、常量、异常、工具类、基础实体 | 禁止依赖任何内部模块 |
| ruoyi-quartz | 定时任务调度 | 禁止依赖 system/framework |
| ruoyi-generator | 代码生成 | 禁止依赖 system/framework |

## 3. 系统管理服务边界

> 证据来源：[wiki/业务逻辑层/业务逻辑层.md - 核心应用服务](../wiki/业务逻辑层/业务逻辑层.md)

| 服务接口 | 实体 | 核心职责 |
|----------|------|----------|
| `ISysUserService` | `SysUser` | 用户 CRUD、角色授权、密码重置 |
| `ISysRoleService` | `SysRole` | 角色 CRUD、数据权限分配 |
| `ISysMenuService` | `SysMenu` | 菜单树管理、权限标识查询 |
| `ISysDeptService` | `SysDept` | 部门树管理、排序 |
| `ISysPostService` | `SysPost` | 岗位 CRUD、唯一性校验 |
| `ISysConfigService` | `SysConfig` | 参数配置 CRUD、缓存管理 |
| `ISysDictTypeService` | `SysDictType` | 字典类型管理、缓存 |
| `ISysDictDataService` | `SysDictData` | 字典数据管理 |
| `ISysOperLogService` | `SysOperLog` | 操作日志记录与查询 |
| `ISysLogininforService` | `SysLogininfor` | 登录日志记录与清理 |
| `ISysUserOnlineService` | `SysUserOnline` | 在线用户监控 |
| `ISysNoticeService` | `SysNotice` | 通知公告 CRUD |
| `ISysNoticeReadService` | `SysNoticeRead` | 通知已读状态管理 |

## 4. Work 扩展模块边界

> 证据来源：[wiki/业务逻辑层/业务逻辑层.md - Work 扩展模块](../wiki/业务逻辑层/业务逻辑层.md)

| 领域模型 | 职责 | 关键依赖 |
|----------|------|----------|
| WorkEmployee | 员工管理 | ISysDeptService |
| WorkJob | 作业管理 | WorkStageTemplate |
| WorkJobStage | 作业阶段 | WorkJob |
| WorkAssignment | 作业分配（含智能分配） | WorkJobStage、WorkEmployee |
| WorkStageTemplate | 阶段模板 | WorkLevelRule |
| WorkLevelRule | 级别规则 | 无 |
| WorkPositionType | 职能类型 | 无 |
| WorkProgressLog | 进度日志 | WorkAssignment |
| WorkTimesheet | 工时记录与审核 | WorkAssignment |

## 5. 跨模块通信规则

> 证据来源：[wiki/业务逻辑层/业务逻辑层.md - 依赖关系分析](../wiki/业务逻辑层/业务逻辑层.md)

- 模块间通信 MUST 通过 Service 接口
- 禁止直接访问其他模块的 Mapper
- 禁止跨模块引用 domain 实体（通过 common 模块的基类共享）
- Work 扩展模块 MUST 通过 system 模块的 Service 接口访问系统服务

## 6. 数据流边界

```
HTTP → Shiro Filter Chain → Controller(BaseController) → Service(@Log/@DataScope) → Mapper → MySQL → AjaxResult/TableDataInfo
```

> 证据来源：[wiki/业务逻辑层/业务逻辑层.md - 架构概览](../wiki/业务逻辑层/业务逻辑层.md)

---

> 证据来源：[wiki/业务逻辑层/业务逻辑层.md](../wiki/业务逻辑层/业务逻辑层.md)