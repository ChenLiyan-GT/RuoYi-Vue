# 编码规约

> 证据来源：[wiki/编码指引.md](../wiki/编码指引.md)

本文档将 wiki 编码指引中的建议性规范收紧为 MUST 强制性规约。

## 1. 包结构规约

> 证据来源：[wiki/编码指引.md - 包结构规范](../wiki/编码指引.md)

### 1.1 模块包结构

- `ruoyi-common` 下的包名 MUST 使用 `com.ruoyi.common.*`
- `ruoyi-system` 下的包名 MUST 使用 `com.ruoyi.system.*`
- `ruoyi-framework` 下的包名 MUST 使用 `com.ruoyi.framework.*`
- `ruoyi-admin` 下的包名 MUST 使用 `com.ruoyi.web.*`

### 1.2 目录结构

新增业务 MUST 遵循以下目录结构：

```
ruoyi-system/src/main/java/com/ruoyi/system/
├── domain/      # 实体类（继承 BaseEntity 或 TreeEntity）
├── mapper/      # Mapper 接口
├── service/     # 服务接口（IXxxService）+ 实现（XxxServiceImpl）
└── work/        # Work 扩展模块（domain + mapper + service）
```

## 2. 类设计规约

> 证据来源：[wiki/编码指引.md - 类设计规范](../wiki/编码指引.md)

### 2.1 继承关系

- 普通实体 MUST 继承 `BaseEntity`
- 树形实体 MUST 继承 `TreeEntity`
- Controller MUST 继承 `BaseController`
- Service 实现 MUST 实现对应的 `IXxxService` 接口

### 2.2 实体类规约

- 实体类字段 MUST 使用包装类型（Long/Integer 而非 long/int）
- 实体类 MUST 提供 getter/setter 方法
- 实体类 MUST 提供 toString 方法
- 实体类字段名 MUST 与数据库列名对应（通过 MyBatis 自动映射）

## 3. 注解使用规约

> 证据来源：[wiki/编码指引.md - 注解体系](../wiki/编码指引.md)

| 场景 | MUST 使用注解 | 说明 |
|------|--------------|------|
| 写操作日志 | `@Log` | 必须标注 title 和 businessType |
| 数据权限过滤 | `@DataScope` | 必须指定 deptAlias 和 userAlias |
| 接口权限控制 | `@RequiresPermissions` | 权限标识格式：模块:功能:操作 |
| 免认证访问 | `@Anonymous` | 标注后自动注入 Shiro 过滤链 |
| 防重复提交 | `@RepeatSubmit` | interval 默认 5000ms |
| 数据脱敏 | `@Sensitive` | 指定 desensitizedType |
| 参数校验 | `@Validated` | Controller 参数校验 |
| 事务管理 | `@Transactional` | Service 写操作 |

## 4. 编码风格规约

> 证据来源：[wiki/编码指引.md - 编码风格规范](../wiki/编码指引.md)

### 4.1 格式规约

- 缩进 MUST 使用 4 个空格（非 Tab）
- 换行 MUST 使用 LF（Unix）格式
- 文件编码 MUST 为 UTF-8
- 大括号 MUST 采用 Egyptian 风格（左大括号不换行）
- 一行 MUST 不超过 120 个字符

### 4.2 命名规约

- 类名 MUST 使用 PascalCase
- 方法名 MUST 使用 camelCase
- 常量 MUST 使用 UPPER_SNAKE_CASE
- 包名 MUST 全小写
- 禁止使用拼音命名（国际通用术语除外）

### 4.3 方法规约

- 每个方法 MUST 不超过 80 行
- 每个方法 MUST 只做一件事
- Mapper 接口方法参数较多时 MUST 使用 `@Param` 注解

## 5. 注释规约

> 证据来源：[wiki/编码指引.md - 注释规范](../wiki/编码指引.md)

- 每个类 MUST 有类级别 JavaDoc，包含 `@author` 标签
- Service 接口方法 MUST 有完整 JavaDoc（含 `@param` 和 `@return`）
- Controller 方法 MUST 有简短 JavaDoc 说明功能
- 行内注释 MUST 解释"为什么"而不是"做什么"

## 6. 异常处理规约

> 证据来源：[wiki/编码指引.md - 异常处理规范](../wiki/编码指引.md)

- 业务校验失败 MUST 抛出 `ServiceException`
- 禁止在业务代码中直接抛出 `BaseException`
- Controller 中 MUST NOT 手动 try-catch（由 `GlobalExceptionHandler` 统一处理）

## 7. 日志规约

> 证据来源：[wiki/编码指引.md - 日志规范](../wiki/编码指引.md)

- Service 实现类 MUST 使用 `private static final Logger log`
- Controller 继承 BaseController，使用 `protected final Logger logger`
- 操作日志 MUST 使用 `@Log` 注解
- 日志 MUST NOT 记录密码等敏感信息

## 8. 安全编码规约

> 证据来源：[wiki/编码指引.md - 安全编码规范](../wiki/编码指引.md)

- 密码 MUST 使用 `Md5Utils.hash(loginName + password + salt)` 加密
- Mapper XML MUST 使用 `#{}` 参数占位符（数据权限除外）
- 敏感字段 MUST 使用 `@Sensitive` 注解脱敏
- 接口 MUST 标注 `@RequiresPermissions` 控制权限
- `demoEnabled=true` 时写操作 MUST 受限

## 9. 代码审查规约

> 证据来源：[wiki/编码指引.md - 代码审查规范](../wiki/编码指引.md)

审查 MUST 覆盖以下检查点：

1. 功能性：边界条件、异常处理、数据权限
2. 代码质量：命名规范、分层结构、代码重复
3. 安全性：输入验证、敏感信息保护、SQL 注入
4. RuoYi 特有：@Log 注解、startPage() 调用、响应封装

---

> 证据来源：[wiki/编码指引.md](../wiki/编码指引.md)