# AGENTS.md - RuoYi-Vue 项目指南

## 项目概述

RuoYi v4.8.3，基于 Spring Boot 3 的轻量级 Java 快速开发平台，单体架构 + Thymeleaf 模板渲染 + REST API 混合模式。

- **JDK**: 17+ | **Spring Boot**: 3.5.14 | **数据库**: MySQL (`ry-spring`) | **端口**: 80

## 模块与依赖

```
ruoyi-admin → ruoyi-framework → ruoyi-system → ruoyi-common
ruoyi-admin → ruoyi-quartz    → ruoyi-common
ruoyi-admin → ruoyi-generator → ruoyi-common
```

| 模块 | 文件数 | 职责 |
|------|--------|------|
| ruoyi-common | 114 | 注解、常量、异常、工具类、基础实体 |
| ruoyi-system | 90 | 业务 domain/mapper/service + Work 扩展 |
| ruoyi-framework | 50 | Shiro、AOP、拦截器、全局异常、配置 |
| ruoyi-admin | 45 | Controller、启动类、配置 |
| ruoyi-quartz | 18 | 定时任务调度 |
| ruoyi-generator | 13 | 代码生成 |

## 关键包结构

**ruoyi-common** (`com.ruoyi.common`):
`annotation/`(@Log/@DataScope/@DataSource/@Excel/@RepeatSubmit/@Anonymous/@Sensitive) · `constant/`(Constants/UserConstants/ShiroConstants) · `core/controller/BaseController` · `core/domain/`(BaseEntity/AjaxResult/R\<T\>/TableDataInfo) · `core/domain/entity/`(SysUser/SysDept/SysRole/SysMenu/SysDictData/SysDictType) · `exception/`(ServiceException/GlobalException + file/job/user 子包) · `utils/`(StringUtils/DateUtils/ShiroUtils/ServletUtils/SpringUtils/ExcelUtil) · `xss/`(XssFilter/XssHttpServletRequestWrapper)

**ruoyi-system** (`com.ruoyi.system`):
`domain/`(SysConfig/SysPost/SysOperLog/SysLogininfor/SysNotice + 关联表) · `mapper/` · `service/`+`service/impl/` · `work/`(WorkAssignment/WorkEmployee/WorkJob/WorkJobStage/WorkLevelRule/WorkPositionType/WorkProgressLog/WorkStageTemplate/WorkTimesheet)

**ruoyi-framework** (`com.ruoyi.framework`):
`aspectj/`(LogAspect/DataScopeAspect/DataSourceAspect/PermissionsAspect) · `config/`(ShiroConfig/DruidConfig/MyBatisConfig/ResourcesConfig) · `shiro/`(UserRealm/SysLoginService/SysPasswordService) · `shiro/web/filter/`(LogoutFilter/CaptchaValidateFilter/CsrfValidateFilter/KickoutSessionFilter) · `web/exception/GlobalExceptionHandler` · `web/service/`(PermissionService/DictService/ConfigService)

**ruoyi-admin** (`com.ruoyi.web`):
`controller/system/`(SysUser/Role/Menu/Dept/Post/Dict/Config/Notice + Work) · `controller/monitor/`(Cache/Druid/Server/Operlog/Logininfor/UserOnline) · `controller/common/`(文件上传下载) · `controller/tool/`(Swagger/代码生成) · `core/config/SwaggerConfig`

## 核心技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.5.14 | 应用框架 |
| Apache Shiro | 2.2.0 (Jakarta) | 认证授权 |
| MyBatis | 3.0.5 (starter) | ORM |
| Druid | 1.2.28 | 连接池 + SQL 监控 |
| Thymeleaf | (Boot 管理) | 模板引擎 |
| PageHelper | 2.1.1 | 分页 |
| FastJSON | 1.2.83 | JSON 解析 |
| Velocity | 2.3 | 代码生成模板 |
| SpringDoc | 2.8.17 | API 文档 |

## 请求流程

```
HTTP → Shiro Filter Chain → Controller(BaseController) → Service(@Log/@DataScope) → Mapper(XML) → MySQL
→ 返回 AjaxResult(单对象) 或 TableDataInfo(分页列表)
```

## 配置文件

| 文件 | 位置 | 说明 |
|------|------|------|
| application.yml | ruoyi-admin/resources/ | 主配置(端口/Thymeleaf/MyBatis/Shiro/XSS) |
| application-druid.yml | ruoyi-admin/resources/ | 数据源 + Druid 监控 |
| mybatis-config.xml | ruoyi-admin/resources/mybatis/ | MyBatis 全局配置 |
| logback.xml | ruoyi-admin/resources/ | 日志配置 |
| ehcache-shiro.xml | ruoyi-admin/resources/ehcache/ | Shiro 缓存 |
| Mapper XML | ruoyi-system/resources/mapper/ | SQL 映射(system/ + work/) |

## 数据库

- **核心表**: sys_user/dept/role/menu/post/dict_type/dict_data/config/oper_log/logininfor/notice/user_online
- **关联表**: sys_user_role/sys_user_post/sys_role_menu/sys_role_dept
- **Work 业务表**: work_assignment/employee/job/job_stage/level_rule/position_type/progress_log/stage_template/timesheet
- **SQL 脚本**: `sql/ry_20260319.sql` · `sql/quartz.sql` · `sql/work_management.sql` · `sql/work_menu.sql`

## 构建与运行

```bash
mvn clean package -DskipTests          # 构建
java -jar ruoyi-admin/target/ruoyi-admin.jar  # 运行
mvn spring-boot:run -pl ruoyi-admin    # 开发模式
```

启动类: `com.ruoyi.RuoYiApplication` | 无单元测试

## 开发规范

**新增业务模块**: 建表(sql/) → Domain(ruoyi-system/domain, 继承BaseEntity) → Mapper(接口+XML) → Service(IXxxService+impl) → Controller(ruoyi-admin/controller, 继承BaseController) → 模板(templates/) → 菜单(sys_menu)

**自定义注解**: `@Log`(操作日志) · `@DataScope`(数据权限) · `@DataSource`(多数据源) · `@RepeatSubmit`(防重提交) · `@Anonymous`(匿名访问)

**权限控制**: `@RequiresPermissions("system:user:list")` | Thymeleaf `shiro:hasPermission` | `@DataScope`+`DataScopeAspect`

**响应格式**: AjaxResult `{code,msg,data}` | TableDataInfo `{total,rows,code}`

## 关键端点

`/login`(登录) · `/index`(首页) · `/swagger-ui.html`(API文档) · `/v3/api-docs`(OpenAPI) · `/druid/*`(Druid监控)

## 安全配置

- **认证**: Shiro + 验证码(captchaType: math/char) · **密码**: 错误5次锁定10分钟
- **Session**: 30分钟超时, 支持并发控制(maxSession) · **XSS**: XssFilter, 匹配 /system/*,/monitor/*,/tool/*
- **CSRF**: 默认关闭, 白名单 /druid · **RememberMe**: AES 加密 Cookie

## Wiki 文档

详见 `ruoyi-admin/.gientech/wiki/`：项目概述 · 架构总览 · 技术栈与依赖 · 快速开始 · 编码指引 · 数据库设计 · API参考 · 测试策略 · 部署与运维 · 前端集成指南 · 安全配置手册 · 性能优化指南 · 扩展开发指南 · CI-CD配置 · 监控告警配置 · 常见问题FAQ · harness/(L1-L3)
