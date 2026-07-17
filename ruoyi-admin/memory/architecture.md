# 架构决策与模块关系记忆

## 模块依赖链

```
ruoyi-admin → ruoyi-framework → ruoyi-system → ruoyi-common
ruoyi-admin → ruoyi-quartz    → ruoyi-common
ruoyi-admin → ruoyi-generator → ruoyi-common
```

- ruoyi-common 是最底层模块，无项目内部依赖
- ruoyi-admin 是唯一可独立运行的模块（spring-boot-maven-plugin repackage）

## 分层架构

Controller(ruoyi-admin) → Service(ruoyi-system) → Mapper(ruoyi-system) → MySQL
框架横切关注点(ruoyi-framework): Shiro认证/AOP日志/数据权限/全局异常/多数据源

## Work 管理扩展

9 个领域模型放在 ruoyi-system/work/ 下，遵循现有 domain/mapper/service 分层。
SQL 脚本: sql/work_management.sql, sql/work_menu.sql

## 技术选型决策

- Shiro（非 Spring Security）: 项目定位轻量级
- Thymeleaf + REST API 混合: 兼顾传统页面和 API 需求
- Druid: 连接池 + SQL 监控一体化
- EhCache: Shiro Session 缓存（非 Redis）
