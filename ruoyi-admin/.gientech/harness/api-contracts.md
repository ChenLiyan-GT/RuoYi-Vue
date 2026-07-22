# API 契约

> 证据来源：[wiki/API参考/API参考.md](../wiki/API参考/API参考.md)

本文档定义了 RuoYi-Vue 项目的 API 契约规范。

## 1. 通用响应格式

> 证据来源：[wiki/API参考/系统管理API.md - 通用响应模型](../wiki/API参考/系统管理API.md)

### AjaxResult（写操作/单条查询）

```json
{ "code": 0, "msg": "操作成功", "data": null }
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | int | 0=成功, 301=警告, 500=错误 |
| msg | String | 提示消息 |
| data | Object | 返回数据（可选） |

### TableDataInfo（分页列表）

```json
{ "total": 100, "rows": [...], "code": 0, "msg": "查询成功" }
```

| 字段 | 类型 | 说明 |
|------|------|------|
| total | long | 总记录数 |
| rows | List | 数据行 |
| code | int | 状态码 |
| msg | String | 提示消息 |

### R\<T\>（REST API / 工具模块）

```json
{ "code": 200, "msg": "操作成功", "data": {...} }
```

## 2. API 模块总览

> 证据来源：[wiki/API参考/API参考.md - API 模块概览](../wiki/API参考/API参考.md)

| 模块 | 路径前缀 | Controller 数 | 认证要求 |
|------|----------|--------------|----------|
| 系统管理 | `/system/*` | 9 | 需登录 + 权限 |
| 认证与账户 | `/login`, `/register`, `/captcha`, `/system/user/profile` | 4 | 部分匿名 |
| 监控 | `/monitor/*` | 6 | 需登录 + 权限 |
| Work 管理 | `/system/work/*` | 9 | 需登录 + 权限 |
| 通用 | `/common/*` | 1 | 匿名 |
| 工具 | `/tool/*`, `/test/*` | 3 | 需登录 + 权限 |

## 3. 认证与账户 API

> 证据来源：[wiki/API参考/认证与账户API.md](../wiki/API参考/认证与账户API.md)

### 登录

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/login` | 登录页面 | 匿名 |
| POST | `/login` | 登录提交 | 匿名（需验证码） |

POST /login 参数：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |
| validCode | String | 是 | 验证码 |
| rememberMe | Boolean | 否 | 记住我 |

### 注册

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/register` | 注册页面 | 匿名 |
| POST | `/register` | 注册提交 | 匿名 |

### 验证码

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/captcha/captchaImage` | 获取验证码 | 匿名 |

### 个人信息

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/system/user/profile` | - | 查看个人信息 |
| POST | `/system/user/profile` | - | 修改个人信息 |
| POST | `/system/user/profile/avatar` | - | 修改头像 |
| POST | `/system/user/profile/resetPwd` | - | 重置密码 |

## 4. 系统管理 API

> 证据来源：[wiki/API参考/系统管理API.md](../wiki/API参考/系统管理API.md)

### 用户管理 `/system/user`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/system/user` | `system:user:list` | 用户列表页 |
| POST | `/system/user/list` | `system:user:list` | 用户列表查询 |
| POST | `/system/user/export` | `system:user:export` | 导出用户 |
| POST | `/system/user/importData` | `system:user:import` | 导入用户 |
| GET | `/system/user/importTemplate` | `system:user:view` | 导入模板 |
| GET | `/system/user/{id}` | `system:user:query` | 用户详情 |
| POST | `/system/user` | `system:user:add` | 新增用户 |
| PUT | `/system/user` | `system:user:edit` | 修改用户 |
| DELETE | `/system/user/{ids}` | `system:user:remove` | 删除用户 |
| PUT | `/system/user/resetPwd` | `system:user:resetPwd` | 重置密码 |
| GET | `/system/user/authRole/{id}` | `system:user:query` | 分配角色页 |
| PUT | `/system/user/authRole` | `system:user:edit` | 保存角色分配 |

### 部门管理 `/system/dept`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/system/dept` | `system:dept:list` | 部门列表页 |
| POST | `/system/dept/list` | `system:dept:list` | 部门列表 |
| GET | `/system/dept/{id}` | `system:dept:query` | 部门详情 |
| POST | `/system/dept` | `system:dept:add` | 新增部门 |
| PUT | `/system/dept` | `system:dept:edit` | 修改部门 |
| DELETE | `/system/dept/{id}` | `system:dept:remove` | 删除部门 |

### 角色管理 `/system/role`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/system/role` | `system:role:list` | 角色列表页 |
| POST | `/system/role/list` | `system:role:list` | 角色列表 |
| POST | `/system/role/export` | `system:role:export` | 导出角色 |
| GET | `/system/role/{id}` | `system:role:query` | 角色详情 |
| POST | `/system/role` | `system:role:add` | 新增角色 |
| PUT | `/system/role` | `system:role:edit` | 修改角色 |
| DELETE | `/system/role/{ids}` | `system:role:remove` | 删除角色 |
| PUT | `/system/role/dataScope` | `system:role:edit` | 修改数据权限 |
| GET | `/system/role/authUser/{id}` | `system:role:list` | 分配用户页 |
| PUT | `/system/role/authUser/selectAll` | `system:role:edit` | 批量选择用户 |
| PUT | `/system/role/authUser/cancel` | `system:role:edit` | 取消授权 |

### 菜单管理 `/system/menu`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/system/menu` | `system:menu:list` | 菜单列表页 |
| POST | `/system/menu/list` | `system:menu:list` | 菜单列表 |
| GET | `/system/menu/{id}` | `system:menu:query` | 菜单详情 |
| POST | `/system/menu` | `system:menu:add` | 新增菜单 |
| PUT | `/system/menu` | `system:menu:edit` | 修改菜单 |
| DELETE | `/system/menu/{id}` | `system:menu:remove` | 删除菜单 |

### 岗位管理 `/system/post`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/system/post` | `system:post:list` | 岗位列表页 |
| POST | `/system/post/list` | `system:post:list` | 岗位列表 |
| POST | `/system/post/export` | `system:post:export` | 导出岗位 |
| GET | `/system/post/{id}` | `system:post:query` | 岗位详情 |
| POST | `/system/post` | `system:post:add` | 新增岗位 |
| PUT | `/system/post` | `system:post:edit` | 修改岗位 |
| DELETE | `/system/post/{ids}` | `system:post:remove` | 删除岗位 |

### 字典管理 `/system/dict`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/system/dict` | `system:dict:list` | 字典列表页 |
| POST | `/system/dict/type/list` | `system:dict:list` | 字典类型列表 |
| POST | `/system/dict/data/list` | `system:dict:list` | 字典数据列表 |
| GET | `/system/dict/type/{id}` | `system:dict:query` | 字典类型详情 |
| POST | `/system/dict/type` | `system:dict:add` | 新增字典类型 |
| PUT | `/system/dict/type` | `system:dict:edit` | 修改字典类型 |
| DELETE | `/system/dict/type/{ids}` | `system:dict:remove` | 删除字典类型 |
| GET | `/system/dict/data/{id}` | `system:dict:query` | 字典数据详情 |
| POST | `/system/dict/data` | `system:dict:add` | 新增字典数据 |
| PUT | `/system/dict/data` | `system:dict:edit` | 修改字典数据 |
| DELETE | `/system/dict/data/{ids}` | `system:dict:remove` | 删除字典数据 |

### 参数配置 `/system/config`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/system/config` | `system:config:list` | 参数列表页 |
| POST | `/system/config/list` | `system:config:list` | 参数列表 |
| POST | `/system/config/export` | `system:config:export` | 导出参数 |
| GET | `/system/config/{id}` | `system:config:query` | 参数详情 |
| POST | `/system/config` | `system:config:add` | 新增参数 |
| PUT | `/system/config` | `system:config:edit` | 修改参数 |
| DELETE | `/system/config/{ids}` | `system:config:remove` | 删除参数 |

### 通知公告 `/system/notice`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/system/notice` | `system:notice:list` | 公告列表页 |
| POST | `/system/notice/list` | `system:notice:list` | 公告列表 |
| GET | `/system/notice/{id}` | `system:notice:query` | 公告详情 |
| POST | `/system/notice` | `system:notice:add` | 新增公告 |
| PUT | `/system/notice` | `system:notice:edit` | 修改公告 |
| DELETE | `/system/notice/{ids}` | `system:notice:remove` | 删除公告 |

## 5. 监控 API

> 证据来源：[wiki/API参考/监控API.md](../wiki/API参考/监控API.md)

| 功能 | 基路径 | 权限前缀 |
|------|--------|----------|
| 缓存监控 | `/monitor/cache` | `monitor:cache:*` |
| Druid 监控 | `/monitor/data` | `monitor:data:*` |
| 服务器监控 | `/monitor/server` | `monitor:server:*` |
| 登录日志 | `/monitor/logininfor` | `monitor:logininfor:*` |
| 操作日志 | `/monitor/operlog` | `monitor:operlog:*` |
| 在线用户 | `/monitor/online` | `monitor:online:*` |

## 6. 通用 API

> 证据来源：[wiki/API参考/通用API.md](../wiki/API参考/通用API.md)

| 方法 | 路径 | 说明 | 限制 |
|------|------|------|------|
| POST | `/common/upload` | 单文件上传 | 10MB |
| POST | `/common/uploads` | 多文件上传 | 10MB/个 |
| GET | `/common/download` | 文件下载 | - |
| GET | `/common/download/resource` | 本地资源下载 | - |

## 7. Work 管理 API

> 证据来源：[wiki/API参考/Work管理API.md](../wiki/API参考/Work管理API.md)

| 领域 | 基路径 | 权限前缀 |
|------|--------|----------|
| 员工管理 | `/system/work/employee` | `work:employee:*` |
| 作业管理 | `/system/work/job` | `work:job:*` |
| 作业阶段 | `/system/work/jobstage` | `work:jobstage:*` |
| 作业分配 | `/system/work/assignment` | `work:assignment:*` |
| 阶段模板 | `/system/work/stagetemplate` | `work:stagetemplate:*` |
| 级别规则 | `/system/work/levelrule` | `work:levelrule:*` |
| 职能类型 | `/system/work/positiontype` | `work:positiontype:*` |
| 进度日志 | `/system/work/progresslog` | `work:progresslog:*` |
| 工时记录 | `/system/work/timesheet` | `work:timesheet:*` |

## 8. 错误处理

> 证据来源：[wiki/API参考/API参考.md - 错误处理](../wiki/API参考/API参考.md)

| 异常类型 | HTTP 状态 | AjaxResult 返回 |
|----------|-----------|-----------------|
| 未登录/超时 | - | `{code:1, msg:"未登录或登录超时"}` |
| 权限不足 | - | `{code:1, msg:"您没有操作权限"}` |
| 业务异常 | - | `{code:500, msg:"错误消息"}` |
| 参数校验失败 | - | `{code:500, msg:"校验错误消息"}` |
| 演示模式 | - | `{code:500, msg:"演示模式，不允许操作"}` |

---

> 证据来源：[wiki/API参考/API参考.md](../wiki/API参考/API参考.md)