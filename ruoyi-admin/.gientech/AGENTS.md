# AGENTS.md - RuoYi-Vue Harness Engineering 指南

RuoYi v4.8.3 | JDK 17+ | Spring Boot 3.5.14 | MySQL (ry-spring) | 端口 80
仓库: https://github.com/ChenLiyan-GT/RuoYi-Vue.git | 分支: main

## Step 1: 项目结构初始化

**模块依赖**: `admin → framework → system → common` | `admin → quartz → common` | `admin → generator → common`

| 模块 | 职责 |
|------|------|
| ruoyi-common | 注解/常量/异常/工具类/基础实体（最底层，无内部依赖） |
| ruoyi-system | 业务 domain/mapper/service + Work 扩展 |
| ruoyi-framework | Shiro 认证/AOP 切面/拦截器/全局异常/配置 |
| ruoyi-admin | Controller + 启动类 + 模板 + 配置（唯一可独立运行模块） |
| ruoyi-quartz | 定时任务调度 |
| ruoyi-generator | 代码生成 |

**关键包**: common(`annotation/@Log/@DataScope/@Excel/@Excels/@RepeatSubmit/@Anonymous/@Sensitive`, `core/BaseController/BaseEntity/TreeEntity/AjaxResult/R<T>/Ztree`, `utils/`, `xss/`) · system(`domain/mapper/service/`, `work/`9个领域模型) · framework(`aspectj/`, `config/ShiroConfig/DruidConfig/CaptchaConfig/FilterConfig`, `shiro/UserRealm`, `manager/AsyncManager+AsyncFactory`, `web/GlobalExceptionHandler`) · admin(`controller/system+monitor+common+tool`, `core/config/SwaggerConfig`)

**实体继承**: BaseEntity(普通实体) · TreeEntity extends BaseEntity(树形结构: SysDept/SysMenu, 含parentId/ancestors/orderNum) · R\<T\>(泛型强类型响应) · Ztree(前端zTree传输: id/pId/name/checked)

**配置文件**: application.yml(主) · application-druid.yml(数据源) · mybatis-config.xml · logback.xml · ehcache-shiro.xml · mapper XML(ruoyi-system/resources/mapper/)

**数据库**: 核心表(sys_user/dept/role/menu/post/dict_type/dict_data/config/oper_log/logininfor/notice) · 关联表(sys_user_role/user_post/role_menu/role_dept) · Work表(9张) · SQL: `sql/ry_20260319.sql`, `sql/quartz.sql`, `sql/work_management.sql`, `sql/work_menu.sql`

## Step 2: 架构文档

**请求流程**: HTTP → Shiro Filter Chain(user,kickout,onlineSession,syncOnlineSession,csrfValidateFilter) → Controller(BaseController) → Service(@Log→AsyncManager异步记录/@DataScope) → Mapper(XML) → MySQL → AjaxResult/TableDataInfo

**分层**: admin(Web层) → framework(横切: Shiro/AOP/异步日志/异常) → system(业务层) → common(基础层) | quartz(定时) + generator(代码生成)

**技术栈**: Spring Boot 3.5.14 · Shiro 2.2.0(Jakarta) · MyBatis 3.0.5 · Druid 1.2.28 · Thymeleaf · PageHelper 2.1.1 · FastJSON 1.2.83 · Velocity 2.3 · SpringDoc 2.8.17 · EhCache

**安全**: Shiro+验证码(math/char, CaptchaConfig+KaptchaTextCreator) · 密码MD5+salt(Md5Utils.hash(loginName+password+salt)), 错5次锁10分钟(EhCache计数) · Session 30min, maxSession并发控制 · XSS(XssFilter+XssValidator, 匹配/system/*,/monitor/*,/tool/*, 排除/system/notice/*) · CSRF默认关,白名单/druid · RememberMe AES(CustomCookieRememberMeManager) · @DataScope数据权限(范围:全部/自定义/本部门/本部门及以下/仅本人) · @Anonymous注解URL通过PermitAllUrlProperties自动注入Shiro过滤链

**端点**: /login · /register(自注册, sys.account.registerUser控制) · /index · /captcha/captchaImage(anon) · /system/user/profile(个人信息) · /common/upload(10MB)/uploads/download(多文件) · /swagger-ui.html · /v3/api-docs · /druid/*(ruoyi/123456)

**RuoYiConfig**: @ConfigurationProperties(prefix="ruoyi") · demoEnabled(演示模式,禁止写操作) · profile(文件路径D:/giencoder/RuoYi-springboot3) · addressEnabled(IP解析) · getUploadPath/getAvatarPath/getDownloadPath/getImportPath

## Step 3: 约束配置

**新增业务**: 建表(sql/) → Domain(system/domain, 继承BaseEntity或TreeEntity) → Mapper(接口+XML) → Service(IXxxService+Impl) → Controller(admin/controller, 继承BaseController) → 模板(templates/) → 菜单(sys_menu)

**命名**: Service接口`IXxxService` · 实现`XxxServiceImpl` · Mapper`XxxMapper` · XML`XxxMapper.xml` · Controller`XxxController`

**注解**: @Log(操作日志,AsyncManager异步写) · @DataScope(数据权限,deptAlias/userAlias) · @DataSource(DataSourceType.MASTER/SLAVE,从库默认关) · @RepeatSubmit(interval=5000ms,message) · @Anonymous(免认证,自动注入Shiro) · @Sensitive(脱敏,7类型:USERNAME/PASSWORD/ID_CARD/PHONE/EMAIL/BANK_CARD/CAR_LICENSE) · @Excel/@Excels(导出) · @RequiresPermissions(权限)

**异常体系**: BaseException(module/code/args,支持i18n) → ServiceException/GlobalException/DemoModeException/UtilException · 子包: file(FileSizeLimitExceededException/InvalidExtensionException) · job(TaskException) · user(CaptchaException/UserPasswordRetryLimitExceedException/RoleBlockedException/UserBlockedException) · GlobalExceptionHandler处理: BindException/MethodArgumentTypeMismatch/AuthorizationException(区分AJAX)/DemoModeException

**响应**: AjaxResult`{code,msg,data}` · TableDataInfo`{total,rows,code,msg}` · R\<T\>`{code,msg,data}`(泛型)

**构建约束**: JDK17+ · Maven3.6+ · UTF-8 · admin为可执行jar · 依赖单向(admin→framework→system→common) · 禁止循环依赖 · 新依赖在根pom的dependencyManagement声明

**安全约束**: 禁止硬编码密码 · cipherKey生产环境设固定值 · FastJSON 1.2.83有漏洞建议升级 · XSS默认开 · 演示模式(demoEnabled=true)下写操作抛DemoModeException

**数据库**: MySQL ry-spring(root/1234) · Druid 5/10/20 · 慢SQL 1000ms · mapper扫描`classpath*:mapper/**/*Mapper.xml` · TypeAliases`com.ruoyi.**.domain`

**Tomcat**: maxThreads=800 · minSpare=100 · acceptCount=1000 · URI编码UTF-8

## Step 4: CI 配置

```bash
mvn clean package -DskipTests          # 构建
mvn clean compile                      # 编译检查
mvn spring-boot:run -pl ruoyi-admin    # 开发运行
java -jar ruoyi-admin/target/ruoyi-admin.jar  # 运行
```

**部署脚本**: ry.bat/ry.sh — JVM: `-Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:+UseParallelGC -Duser.timezone=Asia/Shanghai` · 支持 start/stop/restart/status

**GitHub Actions**: checkout → setup-java@v4(JDK17/corretto/maven缓存) → `mvn clean package -DskipTests -B` → upload-artifact(ruoyi-admin.jar) · 注: 项目当前无.github/workflows/,此为建议配置

**CI检查**: 编译(`mvn compile -B`) · 打包(`mvn package -DskipTests -B`) · 依赖(`mvn dependency:tree`) · 安全(`owasp dependency-check`)

**环境**: JDK 17+ · Maven 3.6+ · MySQL 5.7+/8.0+

## Step 5: 验证

```bash
mvn clean compile                      # BUILD SUCCESS
mvn clean package -DskipTests          # jar 生成
java -jar ruoyi-admin/target/ruoyi-admin.jar  # 启动成功
curl -s -o /dev/null -w "%{http_code}" http://localhost:80/login  # 200
```

**验证清单**: 编译→打包→启动→/login(200)→/index(200)→/swagger-ui.html(200)→/druid(200)→/captcha/captchaImage(200)→/common/upload(POST)

**已知限制**: 无单元测试(src/test/为空) · 运行验证需MySQL · FastJSON 1.2.83安全扫描可能报错 · 演示模式开启时写操作受限

**Wiki文档**: 详见 `.gientech/wiki/`

## Step 6: Harness 约束体系

**Harness 目录**: `.gientech/harness/` — 从 Wiki 收敛的强制性规则

| 文件 | 来源 | 内容 |
|------|------|------|
| `iron-rules.md` | [编码指引](harness/iron-rules.md) | 不可违反的铁律（文件长度/命名/安全/异常等） |
| `boundaries.md` | [业务逻辑层](harness/boundaries.md) | 模块边界与依赖约束 |
| `conventions.md` | [编码指引](harness/conventions.md) | MUST 规约（包结构/类设计/注解/注释等） |
| `api-contracts.md` | [API参考](harness/api-contracts.md) | API 响应格式与端点契约 |

**验证**: 所有 harness 规则必须有 `> 证据来源：[wiki/` 引用
