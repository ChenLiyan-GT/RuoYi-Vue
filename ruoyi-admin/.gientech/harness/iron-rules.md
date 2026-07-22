# 铁律规则

> 证据来源：[wiki/编码指引.md](../wiki/编码指引.md)

本文档定义了 RuoYi-Vue 项目中不可违反的铁律规则（Iron Rules）。违反这些规则将导致 CI 失败或代码审查不通过。

## 1. 文件长度铁律

> 证据来源：[wiki/编码指引.md - 方法结构规范](../wiki/编码指引.md)

| 规则 | 阈值 | 说明 |
|------|------|------|
| 单个 Java 文件最大行数 | ≤ 800 行 | 超过应拆分 |
| 单个方法最大行数 | ≤ 80 行 | 超过应拆分为多个小方法 |
| Mapper XML 文件最大行数 | ≤ 500 行 | 超过应拆分 |
| Thymeleaf 模板最大行数 | ≤ 400 行 | 超过应拆分 |

## 2. 命名铁律

> 证据来源：[wiki/编码指引.md - 命名规范](../wiki/编码指引.md)

### 2.1 类命名

| 元素 | 格式 | 示例 |
|------|------|------|
| Service 接口 | `I` + PascalCase + `Service` | `ISysUserService` |
| Service 实现 | PascalCase + `ServiceImpl` | `SysUserServiceImpl` |
| Mapper 接口 | PascalCase + `Mapper` | `SysUserMapper` |
| Controller | PascalCase + `Controller` | `SysUserController` |
| 实体类 | `Sys` + PascalCase | `SysUser`、`SysRole` |
| Work 实体类 | `Work` + PascalCase | `WorkJob`、`WorkEmployee` |

### 2.2 方法命名

| 操作 | 前缀 | 示例 |
|------|------|------|
| 查询 | `select` | `selectUserList` |
| 新增 | `insert` | `insertUser` |
| 修改 | `update` | `updateUser` |
| 删除 | `delete` | `deleteUserByIds` |
| 校验 | `check` | `checkLoginNameUnique` |

### 2.3 常量命名

- MUST use UPPER_SNAKE_CASE
- MUST be `public static final`

## 3. 架构铁律

> 证据来源：[wiki/编码指引.md - 包结构规范](../wiki/编码指引.md)

### 3.1 依赖方向

```
admin → framework → system → common
admin → quartz → common
admin → generator → common
```

- 禁止循环依赖
- 禁止下层模块依赖上层模块
- common 模块不得依赖任何内部模块

### 3.2 分层约束

```
Controller → Service(接口) → ServiceImpl → Mapper → MyBatis XML
```

- Controller 不能直接调用 Mapper
- Service 层必须定义接口（`IXxxService`）
- 新增业务必须遵循：建表 → Domain → Mapper → Service → Controller → 模板 → 菜单

## 4. 响应格式铁律

> 证据来源：[wiki/编码指引.md - 响应封装规范](../wiki/编码指引.md)

| 场景 | 响应类型 | 字段 |
|------|----------|------|
| 写操作（增/删/改） | `AjaxResult` | `{code, msg, data}` |
| 分页查询 | `TableDataInfo` | `{total, rows, code, msg}` |
| REST API | `R<T>` | `{code, msg, data}` |

状态码：SUCCESS(0) / WARN(301) / ERROR(500)

## 5. 安全铁律

> 证据来源：[wiki/编码指引.md - 安全编码规范](../wiki/编码指引.md)

### 5.1 密码安全

- 密码加密：`Md5Utils.hash(loginName + password + salt)`
- 盐值：`ShiroUtils.randomSalt()` 6 位随机盐
- 错误次数限制：默认 5 次锁定 10 分钟

### 5.2 SQL 注入防护

- Mapper XML 必须使用 `#{}` 参数占位符
- 禁止使用 `${}` 拼接 SQL（数据权限 `${params.dataScope}` 除外）

### 5.3 XSS 防护

- XSS 过滤默认开启
- 排除：`/system/notice/*`
- 匹配：`/system/*,/monitor/*,/tool/*`

### 5.4 权限控制

- 接口 MUST 标注 `@RequiresPermissions`
- 权限标识格式：`模块:功能:操作`
- 免认证接口 MUST 标注 `@Anonymous`

## 6. 方法长度铁律

> 证据来源：[wiki/编码指引.md - 方法结构规范](../wiki/编码指引.md)

- 单个方法 MUST 不超过 80 行
- 每个方法 MUST 只做一件事
- 方法参数不宜过多，多参数 Mapper 方法 MUST 使用 `@Param`

## 7. 异常处理铁律

> 证据来源：[wiki/编码指引.md - 异常处理规范](../wiki/编码指引.md)

- 业务校验失败 MUST 抛出 `ServiceException`
- 禁止在业务代码中直接抛出 `BaseException`
- Controller 中禁止手动 try-catch（由 `GlobalExceptionHandler` 统一处理）

## 8. 日志铁律

> 证据来源：[wiki/编码指引.md - 日志规范](../wiki/编码指引.md)

- Service 实现类 MUST 使用 `private static final Logger log`
- Controller 继承 BaseController，使用 `protected final Logger logger`
- 操作日志 MUST 使用 `@Log` 注解
- 日志文件保留 60 天

## 9. 配置铁律

> 证据来源：[wiki/编码指引.md - 安全编码规范](../wiki/编码指引.md)

- `demoEnabled=true` 时，所有写操作 MUST 抛出 `DemoModeException`
- CSRF 防护默认关闭
- 文件上传大小限制：10MB（单文件）

## 10. 实体继承铁律

> 证据来源：[wiki/编码指引.md - 实体继承规范](../wiki/编码指引.md)

- 普通实体 MUST 继承 `BaseEntity`
- 树形结构实体 MUST 继承 `TreeEntity`（extends BaseEntity）
- 树形实体适用于 `SysDept`、`SysMenu` 等具有层级关系的数据

---

> 证据来源：[wiki/编码指引.md](../wiki/编码指引.md)