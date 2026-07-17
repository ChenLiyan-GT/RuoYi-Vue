# MEMORY.md - RuoYi-Vue 项目记忆

## 项目身份

- **名称**: RuoYi（若依）v4.8.3
- **类型**: 轻量级 Java 快速开发平台（后台管理系统框架）
- **仓库**: https://github.com/ChenLiyan-GT/RuoYi-Vue.git
- **分支**: main

## 技术栈快照

| 层面 | 技术 | 版本 |
|------|------|------|
| 运行时 | JDK | 17+ |
| 框架 | Spring Boot | 3.5.14 |
| 安全 | Apache Shiro | 2.2.0 (Jakarta) |
| ORM | MyBatis | 3.0.5 (starter) |
| 连接池 | Druid | 1.2.28 |
| 模板 | Thymeleaf | (Boot 管理) |
| 数据库 | MySQL | ry-spring |
| API 文档 | SpringDoc | 2.8.17 |
| 代码生成 | Velocity | 2.3 |

## 模块依赖图

```
ruoyi-admin → ruoyi-framework → ruoyi-system → ruoyi-common
ruoyi-admin → ruoyi-quartz    → ruoyi-common
ruoyi-admin → ruoyi-generator → ruoyi-common
```

## 核心决策记录

| 日期 | 决策 | 理由 |
|------|------|------|
| 2026-07 | 采用 Shiro 而非 Spring Security | 项目定位轻量级，Shiro 更简单易用 |
| 2026-07 | Thymeleaf 服务端渲染 + REST API 混合 | 兼顾传统后台页面和 API 接口需求 |
| 2026-07 | Work 管理模块放在 ruoyi-system | 遵循现有分层架构，不新建模块 |
| 2026-07 | Wiki 文档按 harness/ 和根目录分文件夹 | 区分 Harness 工程文档与项目 Wiki |

## 关键文件路径

| 用途 | 路径 |
|------|------|
| 启动类 | ruoyi-admin/src/main/java/com/ruoyi/RuoYiApplication.java |
| 主配置 | ruoyi-admin/src/main/resources/application.yml |
| 数据源配置 | ruoyi-admin/src/main/resources/application-druid.yml |
| Shiro 配置 | ruoyi-framework/.../config/ShiroConfig.java |
| 基础控制器 | ruoyi-common/.../core/controller/BaseController.java |
| 全局异常 | ruoyi-framework/.../web/exception/GlobalExceptionHandler.java |
| SQL 脚本 | sql/ry_20260319.sql, sql/quartz.sql, sql/work_management.sql |
| Mapper XML | ruoyi-system/src/main/resources/mapper/ |

## 构建与部署

- **构建**: `mvn clean package -DskipTests`
- **运行**: `java -jar ruoyi-admin/target/ruoyi-admin.jar`
- **端口**: 80
- **无单元测试**

## 业务领域

### 系统管理（内置）
用户/部门/角色/菜单/岗位/字典/参数/通知/日志

### Work 管理扩展
Assignment(任务分配) · Employee(员工) · Job(职位) · JobStage(职位阶段) · LevelRule(等级规则) · PositionType(岗位类型) · ProgressLog(进度日志) · StageTemplate(阶段模板) · Timesheet(工时表)

## 已知问题与注意事项

- 中文文件名在 `write_to_file` 时可能省略空格（如"常见问题 FAQ.md"→"常见问题FAQ.md"）
- `replace_in_file` 对含中文内容匹配可能失败，建议改用 `write_to_file` 整体重写
- 项目无单元测试，`src/test/` 目录为空
- FastJSON 1.2.83 存在已知安全漏洞，建议后续升级

## 文档体系

Wiki 位于 `ruoyi-admin/.gientech/wiki/`，Harness 文档位于 `wiki/harness/` 子目录。

详见 [目录.md](ruoyi-admin/.gientech/wiki/目录.md)
