# 验证计划文档

## 1. 概述

### 1.1 验证目标

确保作业安排系统的所有功能按需求正确实现，包括单元测试、集成测试和端到端测试。

### 1.2 验证范围

| 模块 | 验证内容 | 优先级 |
| --- | --- | --- |
| 员工管理 | CRUD 操作、状态计算、级别匹配 | P0 |
| 模板管理 | 模板 CRUD、阶段配置校验、占比验证 | P0 |
| 作业管理 | 作业创建、工时计算、状态流转 | P0 |
| 智能分配 | 员工推荐、评分计算、分配逻辑 | P0 |
| 进度管理 | 进度更新、工时填报、负载更新 | P0 |
| 报表统计 | 负载统计、效率统计、数据导出 | P1 |

### 1.3 验证策略

采用测试驱动开发 (TDD) 方法：

1. 先编写测试用例
2. 运行测试确认失败 (红灯)
3. 实现功能代码
4. 运行测试确认通过 (绿灯)
5. 重构优化代码

## 2. 单元测试计划

### 2.1 员工服务测试

**测试文件**: `WorkEmployeeServiceTest.java`

| 测试 ID | 测试方法 | 测试内容 | 预期结果 |
| --- | --- | --- | --- |
| UT-EMP-001 | testInsertEmployee | 新增员工 | 成功插入，返回员工 ID |
| UT-EMP-002 | testInsertDuplicateNo | 工号重复校验 | 抛出 WorkException |
| UT-EMP-003 | testUpdateEmployee | 编辑员工信息 | 成功更新，记录变更历史 |
| UT-EMP-004 | testCalculateLoadStatus | 负载状态计算 | 正确计算空闲/工作中/过载 |
| UT-EMP-005 | testSelectByCondition | 条件查询员工 | 返回符合条件的列表 |

**测试代码示例**:

```java
@SpringBootTest
public class WorkEmployeeServiceTest {
    
    @Autowired
    private IWorkEmployeeService employeeService;
    
    @Test
    public void testInsertEmployee() {
        // 准备数据
        WorkEmployee employee = new WorkEmployee();
        employee.setEmployeeNo("EMP001");
        employee.setEmployeeName("张三");
        employee.setDeptId(100L);
        employee.setPositionType("backend");
        employee.setPositionLevel(5);
        
        // 执行
        int result = employeeService.insertEmployee(employee);
        
        // 验证
        assertEquals(1, result);
        assertNotNull(employee.getEmployeeId());
    }
    
    @Test
    public void testCalculateLoadStatus() {
        WorkEmployee employee = new WorkEmployee();
        employee.setCurrentWorkload(new BigDecimal("35.00"));
        employee.setMaxWorkload(new BigDecimal("40.00"));
        
        String status = employeeService.calculateLoadStatus(employee);
        
        assertEquals("1", status); // 工作中 (87.5%)
    }
}
```

### 2.2 模板服务测试

**测试文件**: `WorkStageTemplateServiceTest.java`

| 测试 ID | 测试方法 | 测试内容 | 预期结果 |
| --- | --- | --- | --- |
| UT-TPL-001 | testInsertTemplate | 新增模板 | 成功插入 |
| UT-TPL-002 | testValidateStagesRatio | 阶段占比校验 (正确) | 验证通过 |
| UT-TPL-003 | testValidateStagesRatioInvalid | 阶段占比校验 (错误) | 抛出 WorkException |
| UT-TPL-004 | testCopyTemplate | 复制模板 | 成功复制，usage_count 不变 |
| UT-TPL-005 | testSetDefaultTemplate | 设为默认模板 | 成功设置，原默认取消 |

**测试代码示例**:

```java
@Test
public void testValidateStagesRatioInvalid() {
    List<StageConfig> stages = new ArrayList<>();
    StageConfig s1 = new StageConfig("requirements", 0.10);
    StageConfig s2 = new StageConfig("development", 0.50);
    stages.add(s1);
    stages.add(s2);
    
    assertThrows(WorkException.class, () -> {
        templateService.validateStages(stages);
    });
}
```

### 2.3 作业服务测试

**测试文件**: `WorkJobServiceTest.java`

| 测试 ID | 测试方法 | 测试内容 | 预期结果 |
| --- | --- | --- | --- |
| UT-JOB-001 | testCreateJob | 创建作业 | 成功创建作业和阶段 |
| UT-JOB-002 | testCalculateStageWorkload | 工时计算 | 正确计算各阶段工时 |
| UT-JOB-003 | testGenerateJobNo | 作业编号生成 | 生成唯一编号 |
| UT-JOB-004 | testUpdateJobProgress | 进度更新 | 成功更新，记录日志 |
| UT-JOB-005 | testCancelJob | 取消作业 | 状态变更为已取消 |

### 2.4 智能分配算法测试

**测试文件**: `SmartAssignServiceTest.java`

| 测试 ID | 测试方法 | 测试内容 | 预期结果 |
| --- | --- | --- | --- |
| UT-ALG-001 | testCalculateScore | 员工评分计算 | 正确计算综合评分 |
| UT-ALG-002 | testRecommendEmployees | 员工推荐 | 返回排序后的列表 |
| UT-ALG-003 | testSmartAssignAll | 一键智能分配 | 所有阶段都有分配 |
| UT-ALG-004 | testNoAvailableEmployee | 无可用员工 | 返回空列表或标记待分配 |
| UT-ALG-005 | testLoadBalance | 负载均衡 | 优先分配空闲员工 |

**测试代码示例**:

```java
@Test
public void testCalculateScore() {
    Employee employee = new Employee();
    employee.setPositionLevel(6);
    employee.setCurrentWorkload(new BigDecimal("20.00"));
    employee.setMaxWorkload(new BigDecimal("40.00"));
    employee.setSkills(Arrays.asList("Java", "Spring"));
    
    StageRequirement req = new StageRequirement();
    req.setMinLevel(5);
    req.setRequiredSkills(Arrays.asList("Java"));
    
    double score = smartAssignService.calculateScore(employee, req);
    
    assertTrue(score > 0 && score <= 100);
}
```

### 2.5 工时计算服务测试

**测试文件**: `WorkloadCalculationServiceTest.java`

| 测试 ID | 测试方法 | 测试内容 | 预期结果 |
| --- | --- | --- | --- |
| UT-WL-001 | testCalculateStageWorkload | 阶段工时计算 | 正确计算 |
| UT-WL-002 | testCalculateRequiredHeadcount | 人员需求计算 | 正确向上取整 |
| UT-WL-003 | testAdjustWorkload | 工时调整 | 保持总和不变 |

## 3. 集成测试计划

### 3.1 作业创建集成测试

**测试文件**: `WorkJobIntegrationTest.java`

| 测试 ID | 测试场景 | 测试步骤 | 预期结果 |
| --- | --- | --- | --- |
| IT-JOB-001 | 完整作业创建流程 | 1. 创建模板 2. 创建作业 3. 验证阶段生成 | 作业和阶段都成功创建 |
| IT-JOB-002 | 工时自动分配 | 1. 创建作业输入总工时 2. 验证各阶段工时 | 各阶段工时正确计算 |
| IT-JOB-003 | 作业状态流转 | 1. 创建作业 (未开始) 2. 分配员工 (进行中) 3. 完成所有阶段 (已完成) | 状态正确流转 |

**测试代码示例**:

```java
@SpringBootTest
@Transactional
public class WorkJobIntegrationTest {
    
    @Autowired
    private IWorkJobService jobService;
    @Autowired
    private IWorkStageTemplateService templateService;
    
    @Test
    public void testCompleteJobCreationFlow() {
        // 1. 创建模板
        WorkStageTemplate template = createTestTemplate();
        templateService.insertTemplate(template);
        
        // 2. 创建作业
        WorkJobDTO jobDTO = new WorkJobDTO();
        jobDTO.setJobName("测试作业");
        jobDTO.setDeptId(100L);
        jobDTO.setTemplateId(template.getTemplateId());
        jobDTO.setTotalWorkload(new BigDecimal("100.00"));
        
        Long jobId = jobService.createJob(jobDTO);
        
        // 3. 验证
        WorkJob job = jobService.selectJobById(jobId);
        assertNotNull(job);
        assertEquals("0", job.getStatus()); // 未开始
        
        List<WorkJobStage> stages = jobService.selectStagesByJobId(jobId);
        assertEquals(8, stages.size()); // 默认 8 个阶段
    }
}
```

### 3.2 智能分配集成测试

**测试文件**: `SmartAssignIntegrationTest.java`

| 测试 ID | 测试场景 | 测试步骤 | 预期结果 |
| --- | --- | --- | --- |
| IT-ALG-001 | 完整分配流程 | 1. 创建作业 2. 创建员工 3. 智能分配 4. 验证分配结果 | 所有阶段都有分配 |
| IT-ALG-002 | 负载感知分配 | 1. 创建多个员工 (不同负载) 2. 智能分配 3. 验证优先分配空闲员工 | 空闲员工优先被分配 |
| IT-ALG-003 | 级别匹配分配 | 1. 创建不同级别员工 2. 智能分配 3. 验证级别匹配 | 员工级别符合要求 |

### 3.3 进度更新集成测试

**测试文件**: `WorkProgressIntegrationTest.java`

| 测试 ID | 测试场景 | 测试步骤 | 预期结果 |
| --- | --- | --- | --- |
| IT-PRG-001 | 进度更新流程 | 1. 创建作业和分配 2. 员工更新进度 3. 验证负载更新 | 进度和负载正确更新 |
| IT-PRG-002 | 工时填报流程 | 1. 员工填报工时 2. 验证实际工时累计 | 实际工时正确累计 |
| IT-PRG-003 | 进度回退确认 | 1. 进度更新为 80% 2. 尝试更新为 50% 3. 验证需要确认 | 提示确认或阻止 |

## 4. 端到端测试计划

### 4.1 E2E 测试场景

| 测试 ID | 场景名称 | 测试步骤 | 预期结果 |
| --- | --- | --- | --- |
| E2E-001 | 完整作业生命周期 | 1. 登录 2. 创建模板 3. 创建作业 4. 智能分配 5. 更新进度 6. 完成作业 | 作业顺利完成 |
| E2E-002 | 多员工作业分配 | 1. 创建作业 2. 创建 5 名员工 3. 智能分配 4. 各员工更新进度 5. 查看报表 | 所有功能正常 |
| E2E-003 | 部门负载统计 | 1. 创建多个作业 2. 分配员工 3. 查看部门负载报表 4. 导出数据 | 报表数据准确 |

### 4.2 E2E 测试工具

- 使用 Selenium 进行浏览器自动化测试
- 或使用 Postman 进行 API 级别 E2E 测试

### 4.3 E2E 测试脚本示例

```javascript
// 使用 Jest + Supertest 进行 API E2E 测试
describe('Work Management E2E', () => {
    it('should complete full job lifecycle', async () => {
        // 1. 登录获取 token
        const token = await login();
        
        // 2. 创建模板
        const template = await createTemplate(token);
        
        // 3. 创建作业
        const job = await createJob(token, template.id);
        
        // 4. 智能分配
        const assignResult = await smartAssign(token, job.id);
        
        // 5. 更新进度
        await updateProgress(token, assignResult.stages[0].id, 50);
        
        // 6. 验证作业状态
        const updatedJob = await getJob(token, job.id);
        expect(updatedJob.progress).toBeGreaterThan(0);
    });
});
```

## 5. 测试数据准备

### 5.1 基础数据

```sql
-- 职能类型
INSERT INTO work_position_type VALUES (1, 'frontend', '前端', 1, '0', NOW(), NOW());
INSERT INTO work_position_type VALUES (2, 'backend', '后端', 2, '0', NOW(), NOW());
INSERT INTO work_position_type VALUES (3, 'test', '测试', 3, '0', NOW(), NOW());
INSERT INTO work_position_type VALUES (4, 'product', '产品', 4, '0', NOW(), NOW());

-- 级别规则
INSERT INTO work_level_rule VALUES 
(1, 3, '初级', '单体测试', '开发', 'P3 初级员工'),
(2, 4, '中级', '开发', '结合测试', 'P4 中级员工'),
(3, 5, '高级', '内部设计', '综合测试', 'P5 高级员工'),
(4, 6, '资深', '基本设计', '内部设计', 'P6 资深员工'),
(5, 7, '专家', '需求分析', '基本设计', 'P7 专家'),
(6, 9, '高级专家', '需求分析', '需求分析', 'P9 高级专家');

-- 默认模板
INSERT INTO work_stage_template VALUES 
(1, '标准软件开发模板', NULL, 
'[{"stage_code":"requirements","stage_name":"需求分析","stage_order":1,"workload_ratio":0.10,"min_level":9},
  {"stage_code":"basic_design","stage_name":"基本设计","stage_order":2,"workload_ratio":0.15,"min_level":6},
  {"stage_code":"detail_design","stage_name":"内部设计","stage_order":3,"workload_ratio":0.15,"min_level":6},
  {"stage_code":"development","stage_name":"开发","stage_order":4,"workload_ratio":0.30,"min_level":4},
  {"stage_code":"unit_test","stage_name":"单体测试","stage_order":5,"workload_ratio":0.10,"min_level":3},
  {"stage_code":"integration_test","stage_name":"结合测试","stage_order":6,"workload_ratio":0.10,"min_level":4},
  {"stage_code":"system_test","stage_name":"综合测试","stage_order":7,"workload_ratio":0.05,"min_level":5},
  {"stage_code":"release","stage_name":"上线","stage_order":8,"workload_ratio":0.05,"min_level":7}]',
'1', 1, 0, '0', 'admin', NOW(), 'admin', NOW());
```

### 5.2 测试员工数据

```sql
-- 测试员工
INSERT INTO work_employee VALUES 
(1, 'EMP001', '张三', 100, 1, 'backend', 6, '["Java","Spring Boot"]', '1', 30.00, 40.00, '0', 'admin', NOW(), 'admin', NOW()),
(2, 'EMP002', '李四', 100, 2, 'backend', 5, '["Java","MySQL"]', '0', 10.00, 40.00, '0', 'admin', NOW(), 'admin', NOW()),
(3, 'EMP003', '王五', 100, 3, 'frontend', 4, '["Vue.js","CSS"]', '0', 5.00, 40.00, '0', 'admin', NOW(), 'admin', NOW()),
(4, 'EMP004', '赵六', 100, 4, 'test', 3, '["测试","Selenium"]', '0', 0.00, 40.00, '0', 'admin', NOW(), 'admin', NOW());
```

## 6. 验证通过标准

### 6.1 单元测试通过标准

- 所有单元测试通过率 100%
- 代码覆盖率 ≥ 80%
- 关键业务逻辑覆盖率 100%

### 6.2 集成测试通过标准

- 所有集成测试通过率 100%
- 无数据一致性错误
- 无事务回滚异常

### 6.3 E2E 测试通过标准

- 所有 E2E 场景通过率 100%
- 页面响应时间 < 3 秒
- API 响应时间 < 2 秒

## 7. 缺陷管理

### 7.1 缺陷优先级

| 优先级 | 说明 | 响应时间 |
| --- | --- | --- |
| P0 | 阻断性缺陷，流程无法继续 | 立即修复 |
| P1 | 严重缺陷，主要功能异常 | 24 小时内 |
| P2 | 一般缺陷，次要功能异常 | 3 天内 |
| P3 | 轻微缺陷，体验问题 | 下个迭代 |

### 7.2 缺陷修复流程

1. 记录缺陷 (现象、复现步骤、预期结果)
2. 定位原因
3. 修复代码
4. 运行相关测试验证
5. 更新测试用例 (如需要)

## 8. 测试环境

### 8.1 环境配置

| 环境 | 用途 | 数据库 |
| --- | --- | --- |
| 开发环境 | 单元测试、开发自测 | 本地 MySQL |
| 测试环境 | 集成测试、E2E 测试 | 测试服务器 MySQL |
| 预发布环境 | 验收测试 | 类生产环境 |

### 8.2 测试工具

| 工具 | 用途 |
| --- | --- |
| JUnit 5 | 单元测试框架 |
| Mockito | Mock 框架 |
| Spring Boot Test | 集成测试框架 |
| Selenium | E2E 浏览器自动化 |
| Postman | API 测试 |
| JaCoCo | 代码覆盖率 |

## 9. 测试执行计划

### 9.1 阶段划分

| 阶段 | 内容 | 时间占比 |
| --- | --- | --- |
| 阶段 1 | 员工管理模块测试 | 15% |
| 阶段 2 | 模板管理模块测试 | 15% |
| 阶段 3 | 作业管理模块测试 | 25% |
| 阶段 4 | 智能分配算法测试 | 20% |
| 阶段 5 | 进度管理和报表测试 | 15% |
| 阶段 6 | E2E 测试和回归测试 | 10% |

### 9.2 测试轮次

- **第 1 轮**: 单元测试编写和执行
- **第 2 轮**: 集成测试编写和执行
- **第 3 轮**: E2E 测试执行
- **第 4 轮**: 回归测试和缺陷修复验证

## 10. 附录

### 10.1 测试用例模板

```markdown
## 测试用例 [ID]

**测试方法**: [方法名]

**前置条件**:
- [条件 1]
- [条件 2]

**测试步骤**:
1. [步骤 1]
2. [步骤 2]

**预期结果**:
- [结果 1]
- [结果 2]

**实际结果**: [执行后填写]

**状态**: Pass / Fail