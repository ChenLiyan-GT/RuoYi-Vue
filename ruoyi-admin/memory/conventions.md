# 代码规范与开发约定记忆

## 新增业务模块标准步骤

1. 建表 → SQL 脚本放 sql/
2. Domain → ruoyi-system/domain/, 继承 BaseEntity
3. Mapper → 接口 + ruoyi-system/resources/mapper/ 下 XML
4. Service → IXxxService 接口 + impl/XxxServiceImpl
5. Controller → ruoyi-admin/controller/system/, 继承 BaseController
6. 模板 → ruoyi-admin/resources/templates/
7. 菜单 → sys_menu 表插入或系统管理界面配置

## 自定义注解使用

- `@Log(title, businessType)` — 操作日志记录
- `@DataScope(deptAlias, userAlias)` — 数据权限过滤
- `@DataSource(DataSourceType.SLAVE)` — 多数据源切换
- `@RepeatSubmit` — 防重提交
- `@Anonymous` — 匿名访问（免认证）
- `@Sensitive` — 数据脱敏
- `@Excel` — Excel 导出字段标记

## 响应格式

- 单对象: AjaxResult `{code: 0/500, msg, data}`
- 分页列表: TableDataInfo `{total, rows, code}`

## 权限控制

- 注解: `@RequiresPermissions("system:user:list")`
- 模板: Thymeleaf `shiro:hasPermission`
- 数据权限: `@DataScope` + `DataScopeAspect`

## 命名约定

- Service 接口: `IXxxService`
- Service 实现: `XxxServiceImpl`
- Mapper 接口: `XxxMapper`
- Mapper XML: `XxxMapper.xml`
- Controller: `XxxController`
