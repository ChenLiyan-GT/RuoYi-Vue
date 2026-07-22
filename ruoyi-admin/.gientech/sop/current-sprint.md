# Sprint 计划

> 证据来源：[wiki/编码指引.md](../wiki/编码指引.md)

## Sprint 信息

| 字段 | 值 |
|------|-----|
| Sprint | L1 初始化 |
| 开始日期 | YYYY-MM-DD |
| 结束日期 | YYYY-MM-DD |
| 目标 | 完成 Harness L1 约束体系初始化 |

## 任务清单

### P0 - 必须完成

- [ ] 生成 `harness/iron-rules.md`
- [ ] 生成 `harness/boundaries.md`
- [ ] 生成 `harness/conventions.md`
- [ ] 生成 `harness/api-contracts.md`
- [ ] 更新 `AGENTS.md`（合并模式）
- [ ] 生成 `sop/current-sprint.md`
- [ ] 生成 `质量评分.md`
- [ ] 复制 `verify-structure.py`
- [ ] 复制 `verify-evidence.py`
- [ ] 复制 `verify-metadata.py`

### P1 - 应该完成

- [ ] 验证证据链评分 ≥ 95%
- [ ] 验证所有 wiki 文件有 `### 本节来源` 区块
- [ ] 验证所有 harness 文件有 `> 证据来源：[wiki/` 引用

### P2 - 可选

- [ ] 补充缺失的 wiki 文件
- [ ] 完善证据链覆盖

## 验证标准

- `verify-structure.py --level l1` ✅
- `verify-evidence.py` ✅
- `verify-metadata.py` ✅
- AGENTS.md ≤ 120 行 ✅
- 证据链评分 ≥ 95%

---

> 证据来源：[wiki/编码指引.md](../wiki/编码指引.md)