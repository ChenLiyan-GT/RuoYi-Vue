# 监控API

**本文档引用的文件**
- [CacheController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/CacheController.java)
- [DruidController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/DruidController.java)
- [ServerController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/ServerController.java)
- [SysLogininforController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/SysLogininforController.java)
- [SysOperlogController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/SysOperlogController.java)
- [SysUserOnlineController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/SysUserOnlineController.java)
- [SysLogininfor.java](../../../ruoyi-system/src/main/java/com/ruoyi/system/domain/SysLogininfor.java)
- [SysOperLog.java](../../../ruoyi-system/src/main/java/com/ruoyi/system/domain/SysOperLog.java)
- [SysUserOnline.java](../../../ruoyi-system/src/main/java/com/ruoyi/system/domain/SysUserOnline.java)
- [Server.java](../../../ruoyi-framework/src/main/java/com/ruoyi/framework/web/domain/Server.java)

## 目录

1. [简介](#简介)
2. [端点总览](#端点总览)
3. [缓存监控](#缓存监控)
4. [Druid 监控](#druid-监控)
5. [服务器监控](#服务器监控)
6. [登录日志](#登录日志)
7. [操作日志](#操作日志)
8. [在线用户](#在线用户)

## 简介

监控模块是 RuoYi 后台管理系统的运维支撑部分，提供对系统运行时状态的实时观测能力。所有监控 Controller 均位于 `com.ruoyi.web.controller.monitor` 包下，继承 `BaseController`，使用 Shiro `@RequiresPermissions` 进行权限控制。

覆盖以下六大功能：

- **缓存监控**：查看和管理 EhCache 缓存名称、键、值，支持清理
- **Druid 监控**：跳转到 Druid 内置监控页面（SQL、连接池、URI 等）
- **服务器监控**：展示服务器的 CPU、内存、磁盘、JVM 等信息
- **登录日志**：查询、导出、删除、清空登录日志，支持账户解锁
- **操作日志**：查询、导出、删除、清空操作日志，查看详情
- **在线用户**：查询在线用户列表，支持强退

## 端点总览

| 功能 | 基路径 | 权限前缀 |
|------|--------|----------|
| 缓存监控 | `/monitor/cache` | `monitor:cache:*` |
| Druid 监控 | `/monitor/data` | `monitor:data:*` |
| 服务器监控 | `/monitor/server` | `monitor:server:*` |
| 登录日志 | `/monitor/logininfor` | `monitor:logininfor:*` |
| 操作日志 | `/monitor/operlog` | `monitor:operlog:*` |
| 在线用户 | `/monitor/online` | `monitor:online:*` |

## 缓存监控

**Controller**: [CacheController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/CacheController.java)

基于 EhCache 的缓存管理接口，支持查看缓存名称列表、缓存键列表、缓存值，以及按缓存名称或键清理缓存。

### 端点列表

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/monitor/cache` | `monitor:cache:view` | 缓存监控主页，返回缓存名称列表 |
| POST | `/monitor/cache/getNames` | `monitor:cache:view` | 获取缓存名称列表（局部刷新） |
| POST | `/monitor/cache/getKeys` | `monitor:cache:view` | 获取指定缓存名称下的所有键 |
| POST | `/monitor/cache/getValue` | `monitor:cache:view` | 获取指定缓存键的值 |
| POST | `/monitor/cache/clearCacheName` | `monitor:cache:view` | 清空指定缓存名称下的所有缓存 |
| POST | `/monitor/cache/clearCacheKey` | `monitor:cache:view` | 清空指定的单个缓存键 |
| GET | `/monitor/cache/clearAll` | `monitor:cache:view` | 清空所有缓存 |

### 请求参数示例

**getKeys** — POST `/monitor/cache/getKeys`

```json
{
  "cacheName": "sys-cache"
}
```

**getValue** — POST `/monitor/cache/getValue`

```json
{
  "cacheName": "sys-cache",
  "cacheKey": "sys_config"
}
```

**clearCacheKey** — POST `/monitor/cache/clearCacheKey`

```json
{
  "cacheName": "sys-cache",
  "cacheKey": "sys_config"
}
```

### 响应示例

`clearCacheName`、`clearCacheKey`、`clearAll` 返回 JSON：

```json
{
  "code": 0,
  "msg": "success"
}
```

其余请求返回 Thymeleaf 模板片段（`fragment-cache-names`、`fragment-cache-kyes`、`fragment-cache-value`）。

## Druid 监控

**Controller**: [DruidController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/DruidController.java)

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/monitor/data` | `monitor:data:view` | 重定向到 `/druid/index.html` |

Druid 监控页面由 Druid 内置提供，包含数据源、SQL 监控、URI 监控、Session 监控、Spring 监控等面板。默认访问账号为 `ruoyi` / `123456`（配置于 `application-druid.yml`）。

## 服务器监控

**Controller**: [ServerController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/ServerController.java)

**数据模型**: [Server.java](../../../ruoyi-framework/src/main/java/com/ruoyi/framework/web/domain/Server.java)

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/monitor/server` | `monitor:server:view` | 服务器监控页面 |

`Server` 对象通过 `server.copyTo()` 采集系统运行时数据，包含以下信息维度：

- **CPU**：核心数、使用率
- **内存**：总量、已用、可用
- **磁盘**：文件系统、总量、已用、可用、使用率
- **JVM**：JVM 内存总量、已用、最大内存、GC 信息
- **系统**：服务器名称、操作系统、架构、Java 版本、项目路径等

## 登录日志

**Controller**: [SysLogininforController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/SysLogininforController.java)

**数据模型**: [SysLogininfor.java](../../../ruoyi-system/src/main/java/com/ruoyi/system/domain/SysLogininfor.java)

### 端点列表

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/monitor/logininfor` | `monitor:logininfor:view` | 登录日志页面 |
| POST | `/monitor/logininfor/list` | `monitor:logininfor:list` | 分页查询登录日志列表 |
| POST | `/monitor/logininfor/export` | `monitor:logininfor:export` | 导出登录日志为 Excel |
| POST | `/monitor/logininfor/remove` | `monitor:logininfor:remove` | 删除指定的登录日志（支持批量） |
| POST | `/monitor/logininfor/clean` | `monitor:logininfor:remove` | 清空所有登录日志 |
| POST | `/monitor/logininfor/unlock` | `monitor:logininfor:unlock` | 解锁指定账户（清除登录重试缓存） |

### 请求参数示例

**list** — POST `/monitor/logininfor/list`

```json
{
  "loginName": "admin",
  "status": "0",
  "params": { "beginTime": "2026-01-01", "endTime": "2026-07-21" }
}
```

**remove** — POST `/monitor/logininfor/remove`

```json
{
  "ids": "1,2,3"
}
```

**unlock** — POST `/monitor/logininfor/unlock`

```json
{
  "loginName": "admin"
}
```

### 响应示例

**list** 返回 `TableDataInfo`：

```json
{
  "code": 0,
  "msg": "success",
  "total": 100,
  "rows": [
    {
      "infoId": 1,
      "loginName": "admin",
      "status": "0",
      "ipaddr": "127.0.0.1",
      "loginLocation": "内网IP",
      "browser": "Chrome",
      "os": "Windows 11",
      "msg": "登录成功",
      "loginTime": "2026-07-21 12:00:00"
    }
  ]
}
```

**remove / clean / unlock** 返回 `AjaxResult`：

```json
{
  "code": 0,
  "msg": "success"
}
```

## 操作日志

**Controller**: [SysOperlogController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/SysOperlogController.java)

**数据模型**: [SysOperLog.java](../../../ruoyi-system/src/main/java/com/ruoyi/system/domain/SysOperLog.java)

### 端点列表

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/monitor/operlog` | `monitor:operlog:view` | 操作日志页面 |
| POST | `/monitor/operlog/list` | `monitor:operlog:list` | 分页查询操作日志列表 |
| POST | `/monitor/operlog/export` | `monitor:operlog:export` | 导出操作日志为 Excel |
| POST | `/monitor/operlog/remove` | `monitor:operlog:remove` | 删除指定的操作日志（支持批量） |
| GET | `/monitor/operlog/detail/{operId}` | `monitor:operlog:detail` | 查看操作日志详情 |
| POST | `/monitor/operlog/clean` | `monitor:operlog:remove` | 清空所有操作日志 |

### 请求参数示例

**list** — POST `/monitor/operlog/list`

```json
{
  "title": "用户管理",
  "businessType": 1,
  "operName": "admin",
  "params": { "beginTime": "2026-01-01", "endTime": "2026-07-21" }
}
```

**remove** — POST `/monitor/operlog/remove`

```json
{
  "ids": "1,2,3"
}
```

**detail** — GET `/monitor/operlog/detail/1`

路径参数 `operId`：操作日志 ID。

### 响应示例

**list** 返回 `TableDataInfo`：

```json
{
  "code": 0,
  "msg": "success",
  "total": 100,
  "rows": [
    {
      "operId": 1,
      "title": "用户管理",
      "businessType": 1,
      "method": "com.ruoyi.web.controller.system.SysUserController.add()",
      "operName": "admin",
      "operIp": "127.0.0.1",
      "status": 0,
      "operTime": "2026-07-21 14:00:00"
    }
  ]
}
```

## 在线用户

**Controller**: [SysUserOnlineController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/SysUserOnlineController.java)

**数据模型**: [SysUserOnline.java](../../../ruoyi-system/src/main/java/com/ruoyi/system/domain/SysUserOnline.java)

### 端点列表

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/monitor/online` | `monitor:online:view` | 在线用户页面 |
| POST | `/monitor/online/list` | `monitor:online:list` | 分页查询在线用户列表 |
| POST | `/monitor/online/batchForceLogout` | `monitor:online:batchForceLogout` 或 `monitor:online:forceLogout` | 批量强退在线用户 |

### 请求参数示例

**list** — POST `/monitor/online/list`

```json
{
  "loginName": "admin",
  "ipaddr": "127.0.0.1"
}
```

**batchForceLogout** — POST `/monitor/online/batchForceLogout`

```json
{
  "ids": "sessionId1,sessionId2"
}
```

### 业务逻辑说明

`batchForceLogout` 执行以下步骤（[SysUserOnlineController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/SysUserOnlineController.java)(L59-L88)）：

1. 遍历传入的 sessionId 列表
2. 查询在线用户记录，若不存在返回"用户已下线"
3. 通过 `OnlineSessionDAO` 读取 Session，若不存在返回"用户已下线"
4. 若 sessionId 为当前登录用户，返回"当前登录用户无法强退"
5. 删除 Session、更新在线状态为离线、清除用户缓存

### 响应示例

```json
{
  "code": 0,
  "msg": "success"
}
```

错误场景：

```json
{
  "code": 500,
  "msg": "当前登录用户无法强退"
}