# 健康打卡与运动达标管理系统

基于 **SpringBoot + Vue** 前后端分离架构的健康打卡与运动达标管理系统。本仓库按 **用户端 / 管理端** 拆分为四个独立工程：两端各自拥有独立的前后端，共享同一个 MySQL 数据库。

## 工程结构

| 工程 | 说明 | 端口 |
| --- | --- | --- |
| `backend-user/` | 用户端后端（SpringBoot） | 8080 |
| `backend-admin/` | 管理端后端（SpringBoot） | 8081 |
| `frontend-user/` | 用户端前端（Vue3 + Vite） | 5173 |
| `frontend-admin/` | 管理端前端（Vue3 + Vite） | 5174 |
| `backend/`、`frontend/` | 拆分前的单体工程（备份，不再维护） | - |
| `docs/` | 课程设计报告 | - |

## 技术栈

| 端 | 技术 |
| --- | --- |
| 后端 | SpringBoot 2.7.18 · MyBatis-Plus 3.5.3 · MySQL 8 · JWT · Lombok · Validation |
| 前端 | Vue3 · Vite · Vue Router · Pinia · Axios · Element Plus · ECharts |

## 用户端功能

- 注册 / 登录 / 修改密码 / 编辑资料（JWT 无状态登录，BCrypt 密码加密）
- 打卡记录：新增 / 修改 / 删除 / 分页 / 条件查询（按类型、日期区间）
- 目标设置：每周 / 每月各三项目标（天数、时长、卡路里）
- 数据可视化：近 7 天打卡趋势、运动类型分布、目标达成进度（ECharts）
- 异常提醒：定时任务每日扫描未打卡用户、每周检查目标达成情况，自动推送站内消息

## 管理端功能

- **独立登录**：管理端拥有专属登录页，登录接口校验角色，**仅 ADMIN 账号可登录**（USER 账号登录将被拒绝并提示「非管理员账号，禁止登录管理端」）
- 数据概览：用户总数、管理员数量、打卡总数、累计时长/卡路里、用户运动排行 Top10
- 用户管理：分页 / 关键字搜索 / 启用禁用 / 重置密码 / 删除

## 工程规范（两端通用）

- 统一返回格式 `Result<T>` + 统一状态码 `ResultCode`
- 全局异常处理 `GlobalExceptionHandler`
- 跨域处理 `CorsConfig`
- JWT 拦截器 + `@RequireRole` 注解角色控制
- 参数校验（`@Valid` + JSR-303 注解）
- 分层开发：Controller / Service / Mapper / Entity / DTO / VO

## 快速开始

### 1. 环境准备

- JDK 8（或 11 / 17）
- Maven 3.6+
- MySQL 8.0
- Node.js 16+

### 2. 初始化数据库

执行一次 `backend-user/src/main/resources/sql/init.sql`（两端后端共享同一个数据库 `jiankang`）：

```sql
source backend-user/src/main/resources/sql/init.sql;
```

### 3. 配置数据库账号密码

两端后端各自编辑 `application.yml` 中的 MySQL 账号密码（默认 root / root）：

- `backend-user/src/main/resources/application.yml`
- `backend-admin/src/main/resources/application.yml`

### 4. 启动后端（两个终端）

```bash
cd backend-admin
mvn spring-boot:run        # 管理端后端，8081，启动时自动创建 admin / 123456
```

```bash
cd backend-user
mvn spring-boot:run        # 用户端后端，8080，启动时自动创建 user / 123456 及示例数据
```

### 5. 启动前端（两个终端）

```bash
cd frontend-user
npm install
npm run dev                # 用户端，http://localhost:5173
```

```bash
cd frontend-admin
npm install
npm run dev                # 管理端，http://localhost:5174
```

## 默认账号

| 端口 | 端 | 账号 | 密码 |
| --- | --- | --- | --- |
| 5173 | 用户端 | user | 123456 |
| 5174 | 管理端 | admin | 123456 |

## 主要接口

| 端 | 模块 | 方法 | 路径 |
| --- | --- | --- | --- |
| 用户端 | 认证 | POST | `/api/auth/register` |
| 用户端 | 认证 | POST | `/api/auth/login` |
| 用户端 | 认证 | GET | `/api/auth/info` |
| 用户端 | 打卡 | GET/POST | `/api/check`、`/api/check/page` |
| 用户端 | 目标 | GET/POST | `/api/goal` |
| 用户端 | 消息 | GET | `/api/notice/page`、`/api/notice/unread` |
| 用户端 | 统计 | GET | `/api/stats/dashboard` |
| 管理端 | 认证 | POST | `/api/auth/login`（仅 ADMIN） |
| 管理端 | 统计 | GET | `/api/stats/overview` |
| 管理端 | 用户 | GET | `/api/user/page` |

## 验证定时任务

用户端 `application.yml` 中 `reminder.check-cron` 默认每日 20:00 执行。可临时改为 `"0 * * * * ?"`（每分钟）观察「未打卡提醒」消息是否自动生成。
