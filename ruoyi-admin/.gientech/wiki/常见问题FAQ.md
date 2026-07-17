# 常见问题 FAQ

**本文档中引用的文件**
- [application.yml](../../../ruoyi-admin/src/main/resources/application.yml)
- [application-druid.yml](../../../ruoyi-admin/src/main/resources/application-druid.yml)
- [README.md](../../../README.md)

## 目录
1. [环境配置问题](#环境配置问题)
2. [启动失败问题](#启动失败问题)
3. [数据库问题](#数据库问题)
4. [登录认证问题](#登录认证问题)
5. [权限配置问题](#权限配置问题)
6. [代码生成问题](#代码生成问题)
7. [文件上传问题](#文件上传问题)
8. [缓存问题](#缓存问题)
9. [部署问题](#部署问题)
10. [其他常见问题](#其他常见问题)

## 环境配置问题

### Q1: JDK 版本不匹配

**问题现象**：
```
Exception in thread "main" java.lang.UnsupportedClassVersionError: 
com/ruoyi/RuoYiApplication has been compiled by a more recent version of 
the Java Runtime (class file version 61.0), this version of the Java Runtime 
only recognizes class file versions up to 52.0
```

**原因**：项目使用 JDK 17 编译，但运行环境使用了旧版本 JDK

**解决方案**：
1. 检查当前 JDK 版本
```bash
java -version
```

2. 安装 JDK 17 并配置环境变量
```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

# Linux
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

3. 在 IDE 中配置 JDK
- IntelliJ IDEA: File → Project Structure → Project → SDK → 选择 JDK 17
- Eclipse: Window → Preferences → Java → Installed JREs → 添加 JDK 17

### Q2: Maven 依赖下载失败

**问题现象**：
```
Could not resolve dependencies for project com.ruoyi:ruoyi-admin:jar:4.8.3
Could not transfer artifact org.springframework.boot:spring-boot-starter:jar:3.5.14
```

**解决方案**：
1. 配置 Maven 镜像（settings.xml）
```xml
<mirrors>
    <mirror>
        <id>aliyun</id>
        <name>Aliyun Maven</name>
        <url>https://maven.aliyun.com/repository/public</url>
        <mirrorOf>central</mirrorOf>
    </mirror>
</mirrors>
```

2. 清理本地仓库缓存
```bash
# Windows
rmdir /s /q %USERPROFILE%\.m2\repository

# Linux
rm -rf ~/.m2/repository
```

3. 重新下载依赖
```bash
mvn clean install -U
```

### Q3: 端口被占用

**问题现象**：
```
Port 8080 is already in use
```

**解决方案**：
1. 查找占用端口的进程
```bash
# Windows
netstat -ano | findstr :8080
taskkill /F /PID <PID>

# Linux
lsof -i :8080
kill -9 <PID>
```

2. 修改应用端口（application.yml）
```yaml
server:
  port: 8081  # 修改为其他端口
```

## 启动失败问题

### Q4: 数据库连接失败

**问题现象**：
```
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```

**原因**：数据库连接配置错误

**解决方案**：
1. 检查数据库服务是否启动
```bash
# Windows
services.msc  # 查看 MySQL 服务

# Linux
systemctl status mysqld
```

2. 检查数据库配置（application-druid.yml）
```yaml
spring:
  datasource:
    druid:
      master:
        url: jdbc:mysql://localhost:3306/ry-spring?useUnicode=true&characterEncoding=utf8
        username: root
        password: your_password
```

3. 测试数据库连接
```bash
mysql -u root -p -e "USE ry-spring; SELECT 1;"
```

4. 检查防火墙设置
```bash
# Linux
firewall-cmd --list-ports
firewall-cmd --add-port=3306/tcp --permanent
```

### Q5: Redis 连接失败

**问题现象**：
```
io.lettuce.core.RedisConnectionException: Unable to connect to localhost:6379
```

**解决方案**：
1. 检查 Redis 服务是否启动
```bash
# Windows
redis-server

# Linux
systemctl status redis
```

2. 测试 Redis 连接
```bash
redis-cli ping
# 应返回 PONG
```

3. 修改 Redis 配置（application.yml）
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_password  # 如果有密码
    timeout: 5000ms
```

### Q6: Shiro 配置错误

**问题现象**：
```
org.apache.shiro.authc.AuthenticationException: Authentication failed
```

**解决方案**：
1. 检查 Shiro 配置类是否存在
2. 检查 Realm 配置是否正确
3. 检查用户密码加密方式是否匹配

```java
// 检查密码加密方式
@Service
public class PasswordService {
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
```

## 数据库问题

### Q7: 表不存在

**问题现象**：
```
Table 'ry-spring.sys_user' doesn't exist
```

**解决方案**：
1. 导入数据库脚本
```bash
mysql -u root -p ry-spring < sql/ry_20260319.sql
mysql -u root -p ry-spring < sql/quartz.sql
```

2. 检查表是否创建成功
```sql
USE ry-spring;
SHOW TABLES;
```

### Q8: 数据权限问题

**问题现象**：用户登录后看不到数据

**原因**：数据权限配置错误

**解决方案**：
1. 检查角色数据范围配置
```sql
SELECT role_id, role_name, data_scope FROM sys_role;
```

2. 检查角色部门关联
```sql
SELECT * FROM sys_role_dept WHERE role_id = <角色 ID>;
```

3. 检查用户部门关联
```sql
SELECT u.user_id, u.dept_id, d.dept_name 
FROM sys_user u 
LEFT JOIN sys_dept d ON u.dept_id = d.dept_id 
WHERE u.user_id = <用户 ID>;
```

### Q9: 慢查询问题

**问题现象**：接口响应慢，数据库 CPU 高

**解决方案**：
1. 查看慢 SQL 日志
```sql
SHOW VARIABLES LIKE 'slow_query%';
SHOW VARIABLES LIKE 'long_query_time';
```

2. 查看 Druid 监控台
```
http://localhost/druid/sql.html
```

3. 使用 EXPLAIN 分析 SQL
```sql
EXPLAIN SELECT * FROM sys_user WHERE login_name = 'admin';
```

4. 添加索引优化查询
```sql
-- 为常用查询字段添加索引
ALTER TABLE sys_user ADD INDEX idx_login_name (login_name);
ALTER TABLE sys_user ADD INDEX idx_dept_id (dept_id);
ALTER TABLE sys_user ADD INDEX idx_create_time (create_time);
```

## 登录认证问题

### Q10: 登录失败

**问题现象**：提示"用户名或密码错误"

**解决方案**：
1. 检查用户名是否正确
- 默认管理员账号：admin
- 默认密码：admin123

2. 检查验证码是否正确
```yaml
# 关闭验证码（仅开发环境）
captcha:
  enabled: false
```

3. 检查用户状态
```sql
SELECT user_id, login_name, status, del_flag FROM sys_user WHERE login_name = 'admin';
-- status 应该为 '0'（正常）
-- del_flag 应该为 '0'（未删除）
```

4. 检查密码是否加密
```sql
-- 查看密码字段
SELECT password FROM sys_user WHERE login_name = 'admin';
-- 应该是加密后的字符串
```

### Q11: 登录成功后跳转首页失败

**问题现象**：登录后页面空白或 404

**解决方案**：
1. 检查前端路由配置
2. 检查菜单配置
```sql
SELECT * FROM sys_menu WHERE menu_type = 'C' AND visible = '0';
```

3. 检查用户角色权限
```sql
SELECT r.role_id, r.role_name 
FROM sys_role r
INNER JOIN sys_user_role ur ON r.role_id = ur.role_id
WHERE ur.user_id = <用户 ID>;
```

### Q12: 会话过期

**问题现象**：操作时提示"会话已过期，请重新登录"

**解决方案**：
1. 检查 Session 配置
```yaml
server:
  servlet:
    session:
      timeout: 1800  # 30 分钟
```

2. 检查 Shiro Session 配置
```java
@Bean
public SessionManager sessionManager() {
    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    sessionManager.setSessionTimeout(1800000); // 30 分钟
    return sessionManager;
}
```

3. 前端自动刷新 Token
```javascript
// 定时器刷新 Token
setInterval(() => {
  refreshToken()
}, 15 * 60 * 1000) // 每 15 分钟刷新
```

## 权限配置问题

### Q13: 菜单不显示

**问题现象**：登录后某些菜单不显示

**解决方案**：
1. 检查菜单可见性
```sql
SELECT menu_id, menu_name, visible FROM sys_menu WHERE menu_name = '用户管理';
-- visible 应该为 '0'（显示）
```

2. 检查角色菜单权限
```sql
SELECT rm.menu_id, m.menu_name 
FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = <角色 ID>;
```

3. 检查用户角色
```sql
SELECT ur.role_id, r.role_name 
FROM sys_user_role ur
INNER JOIN sys_role r ON ur.role_id = r.role_id
WHERE ur.user_id = <用户 ID>;
```

### Q14: 按钮权限失效

**问题现象**：按钮应该隐藏但显示了

**解决方案**：
1. 检查权限标识配置
```sql
SELECT menu_id, menu_name, perms FROM sys_menu WHERE menu_type = 'F';
```

2. 检查前端权限指令
```vue
<el-button v-hasPermi="['system:user:add']">新增用户</el-button>
```

3. 检查后端权限注解
```java
@PreAuthorize("@ss.hasPermi('system:user:add')")
@PostMapping
public AjaxResult add(@RequestBody SysUser user) {
    return toAjax(userService.insertUser(user));
}
```

### Q15: 数据权限不生效

**问题现象**：用户能看到其他部门的数据

**解决方案**：
1. 检查角色数据范围
```sql
SELECT role_id, role_name, data_scope FROM sys_role;
-- data_scope: 1-全部数据，2-自定义，3-本部门，4-本部门及以下
```

2. 配置自定义数据权限
```sql
-- 为角色分配部门权限
INSERT INTO sys_role_dept (role_id, dept_id) VALUES (2, 101);
INSERT INTO sys_role_dept (role_id, dept_id) VALUES (2, 103);
```

3. 检查 AOP 切面是否生效
```java
@Aspect
@Component
public class DataScopeAspect {
    // 数据权限切面逻辑
}
```

## 代码生成问题

### Q16: 代码生成失败

**问题现象**：代码生成时提示错误

**解决方案**：
1. 检查数据库表是否存在
```sql
SHOW TABLES LIKE 'sys_%';
```

2. 检查表注释和字段注释
```sql
-- 查看表注释
SHOW TABLE STATUS WHERE Name = 'your_table_name';

-- 查看字段注释
SHOW FULL COLUMNS FROM your_table_name;
```

3. 检查生成配置
```sql
SELECT * FROM gen_table WHERE table_name = 'your_table_name';
```

### Q17: 生成的代码无法运行

**问题现象**：生成的代码编译失败或运行报错

**解决方案**：
1. 检查实体类字段类型
```java
// 检查字段类型是否匹配
private Long userId;      // bigint
private String userName;  // varchar
private Date createTime;  // datetime
```

2. 检查 Mapper XML 文件
```xml
<!-- 检查 resultMap 是否匹配 -->
<resultMap type="SysUser" id="SysUserResult">
    <id property="userId" column="user_id" />
    <result property="userName" column="user_name" />
</resultMap>
```

3. 检查 Controller 路径
```java
@RestController
@RequestMapping("/system/your-module")
public class YourModuleController {
    // ...
}
```

## 文件上传问题

### Q18: 文件上传失败

**问题现象**：上传文件时提示错误

**解决方案**：
1. 检查文件大小限制
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 20MB
```

2. 检查文件类型限制
```java
// 检查文件扩展名
String extension = getExtension(fileName);
if (!isAllowedExtension(extension)) {
    throw new InvalidExtensionException("不允许上传的文件类型");
}
```

3. 检查上传目录权限
```bash
# Linux
chmod 755 /data/upload
chown www:www /data/upload
```

### Q19: 上传文件无法访问

**问题现象**：文件上传成功但无法访问

**解决方案**：
1. 检查文件访问路径配置
```yaml
ruoyi:
  profile: /data/upload  # 上传文件保存路径
```

2. 配置静态资源映射
```java
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profile/**")
            .addResourceLocations("file:/data/upload/");
    }
}
```

3. 检查 Nginx 配置
```nginx
location /profile {
    alias /data/upload;
    autoindex on;
}
```

## 缓存问题

### Q20: 缓存不更新

**问题现象**：数据修改后缓存未更新

**解决方案**：
1. 检查缓存注解配置
```java
@Cacheable(value = "user", key = "#userId")
public SysUser selectUserById(Long userId) {
    return userMapper.selectUserById(userId);
}

@CacheEvict(value = "user", key = "#user.userId")
public int updateUser(SysUser user) {
    return userMapper.updateUser(user);
}
```

2. 手动清除缓存
```java
@Autowired
private CacheManager cacheManager;

public void clearCache() {
    Cache cache = cacheManager.getCache("user");
    cache.clear();
}
```

3. 检查 Redis 缓存
```bash
redis-cli
> KEYS user:*
> DEL user:1
```

### Q21: 缓存穿透

**问题现象**：大量请求查询不存在的数据

**解决方案**：
1. 缓存空值
```java
public SysUser selectUserById(Long userId) {
    String key = "user:" + userId;
    SysUser user = (SysUser) redisTemplate.opsForValue().get(key);
    
    if (user != null) {
        return user;
    }
    
    // 查询数据库
    user = userMapper.selectUserById(userId);
    
    if (user == null) {
        // 缓存空值，设置较短过期时间
        redisTemplate.opsForValue().set(key, null, 5, TimeUnit.MINUTES);
        return null;
    }
    
    redisTemplate.opsForValue().set(key, user, 30, TimeUnit.MINUTES);
    return user;
}
```

2. 使用布隆过滤器
```java
@Autowired
private BloomFilter<String> bloomFilter;

public SysUser selectUserById(Long userId) {
    String key = "user:" + userId;
    
    if (!bloomFilter.mightContain(key)) {
        // 肯定不存在
        return null;
    }
    
    // 可能存在，继续查询
    // ...
}
```

## 部署问题

### Q22: 生产环境启动失败

**问题现象**：本地运行正常，生产环境启动失败

**解决方案**：
1. 检查环境变量
```bash
# 检查 JAVA_HOME
echo $JAVA_HOME

# 检查数据库连接
ping <database-host>
telnet <database-host> 3306
```

2. 检查配置文件
```yaml
# 使用多环境配置
spring:
  profiles:
    active: prod  # 生产环境
```

3. 查看启动日志
```bash
tail -f /data/logs/application.log
```

### Q23: Nginx 反向代理失败

**问题现象**：Nginx 配置后无法访问后端服务

**解决方案**：
1. 检查 Nginx 配置
```nginx
server {
    listen 80;
    server_name localhost;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

2. 测试 Nginx 配置
```bash
nginx -t
nginx -s reload
```

3. 检查防火墙
```bash
firewall-cmd --list-ports
firewall-cmd --add-port=80/tcp --permanent
```

## 其他常见问题

### Q24: 跨域问题

**问题现象**：前端请求被浏览器拦截

**解决方案**：
1. 配置跨域
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
```

2. Nginx 配置跨域
```nginx
location /api {
    add_header Access-Control-Allow-Origin *;
    add_header Access-Control-Allow-Methods 'GET, POST, PUT, DELETE, OPTIONS';
    add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';
    
    if ($request_method = 'OPTIONS') {
        return 204;
    }
}
```

### Q25: 日志不输出

**问题现象**：控制台或日志文件没有输出

**解决方案**：
1. 检查日志配置（application.yml）
```yaml
logging:
  level:
    com.ruoyi: debug
    org.springframework: warn
  file:
    name: /data/logs/application.log
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{50} - %msg%n"
```

2. 检查日志目录权限
```bash
mkdir -p /data/logs
chmod 755 /data/logs
```

### Q26: 定时任务不执行

**问题现象**：配置的定时任务没有执行

**解决方案**：
1. 检查定时任务配置
```sql
SELECT job_id, job_name, cron_expression, status FROM sys_job;
-- status 应该为 '0'（正常）
```

2. 检查 Quartz 配置
```yaml
spring:
  quartz:
    auto-startup: true
    properties:
      org:
        quartz:
          scheduler:
            instanceName: RuoYiScheduler
```

3. 查看定时任务日志
```sql
SELECT * FROM sys_job_log ORDER BY create_time DESC LIMIT 10;
```

## 参考文档
- [application.yml](../../../ruoyi-admin/src/main/resources/application.yml) - 应用配置
- [application-druid.yml](../../../ruoyi-admin/src/main/resources/application-druid.yml) - 数据库配置
- [部署与运维.md](./部署与运维.md) - 部署配置
- [安全配置手册.md](./安全配置手册.md) - 安全配置
