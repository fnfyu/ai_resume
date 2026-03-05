

# AI Resume (AI智能简历管理系统)

一个基于 Spring Boot 的 AI 智能简历分析与岗位匹配系统。

## 项目简介

AI Resume 是一个全栈简历管理解决方案，利用人工智能技术自动分析简历内容，提取关键技能，并与岗位要求进行智能匹配。系统采用微服务架构设计，支持异步任务处理，适合招聘平台或企业 HR 系统使用。

## 技术栈

- **后端框架**: Spring Boot 4.0.1
- **数据库**: MySQL + MyBatis-Plus
- **缓存**: Redis (用于任务队列)
- **认证**: JWT
- **AI 集成**: OpenAI API (可配置)
- **构建工具**: Maven

## 核心功能

### 1. 用户管理
- 用户注册与登录
- JWT 令牌认证
- 基于角色的权限控制

### 2. 简历管理
- 简历文件上传 (支持 PDF/DOC 等格式)
- 自动文本解析
- AI 智能分析提取技能
- 简历状态跟踪

### 3. 岗位管理
- 岗位信息创建
- 岗位描述与技能要求维护

### 4. 智能匹配
- 简历与岗位技能匹配度计算
- 匹配结果存储与展示
- 支持批量匹配

## 项目结构

```
ai_resume/
├── src/main/java/com/example/ai_resume/
│   ├── common/           # 公共组件 (枚举、异常、响应)
│   ├── config/           # 配置类 (JWT拦截器、Web配置)
│   ├── controller/       # 控制器 (Auth、Resume、Job)
│   ├── dto/              # 数据传输对象
│   ├── entity/           # 实体类
│   ├── repository/       # MyBatis Mapper 接口
│   ├── service/          # 业务逻辑服务
│   ├── task/             # 定时任务
│   └── util/             # 工具类
└── src/main/resources/
    └── application.properties  # 配置文件
```

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.x
- MySQL 5.7+
- Redis 3.x+

### 配置说明

在 `src/main/resources/application.properties` 中配置以下参数：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/ai_resume
spring.datasource.username=root
spring.datasource.password=your_password

# Redis配置
spring.redis.host=localhost
spring.redis.port=6379

# AI API配置
api.key=your_api_key
api.url=https://api.openai.com/v1/chat/completions

# 文件上传配置
file.upload.dir=/path/to/upload

# JWT配置
jwt.secret=your_jwt_secret
jwt.expire=7200000
```

### 启动步骤

1. 创建数据库：
```sql
CREATE DATABASE ai_resume;
```

2. 编译项目：
```bash
./mvnw clean package -DskipTests
```

3. 运行项目：
```bash
./mvnw spring-boot:run
```

## API 接口

### 认证接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /auth/register | 用户注册 |
| POST | /auth/login | 用户登录 |

### 简历接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /resume/upload | 上传并分析简历 |

### 岗位接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /job/create | 创建岗位 |
| POST | /job/match | 简历与岗位匹配 |

## 工作流程

1. 用户注册登录获取 JWT Token
2. 上传简历文件，系统自动解析文本
3. AI 服务异步分析简历，提取技能
4. 创建岗位并设置技能要求
5. 调用匹配接口，计算简历与岗位的匹配度

## 注意事项

- 确保 MySQL 和 Redis 服务正常运行
- AI 分析功能需要配置有效的 API Key
- 文件上传目录需要有写权限
- 生产环境建议使用 HTTPS

## 许可证

本项目仅供学习交流使用。