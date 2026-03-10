# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目定位

这是一个**前端转 AI 全栈的学习项目**，技术栈：
- 前端：React/Next.js
- 后端：Java 1.8 + Spring Boot
- AI：Claude API / 通义千问等

## 学习路径

### 阶段 1：Java 基础（1-2 周）
- [ ] Java 语法、类、接口、泛型
- [ ] Maven/Gradle 构建工具
- [ ] Spring Boot 入门

### 阶段 2：Web 后端（2-3 周）
- [ ] RESTful API 设计
- [ ] Spring MVC、Spring Data JPA
- [ ] MySQL 数据库操作

### 阶段 3：前后端联调（2 周）
- [ ] 跨域配置
- [ ] JWT 认证
- [ ] 文件上传

### 阶段 4：AI 集成（2-3 周）
- [ ] 调用 Claude API
- [ ] RAG 知识库
- [ ] AI Agent 开发

## 常用命令

```bash
# Java
mvn clean install
mvn spring-boot:run

# 前端
npm install
npm run dev
npm run build
```

## 项目结构

```
status/
├── backend/          # Java Spring Boot
│   ├── src/main/java/
│   ├── src/test/java/
│   └── pom.xml
├── frontend/         # React/Next.js
│   ├── src/
│   └── package.json
└── CLAUDE.md
```

## 开发原则

- 每个功能先写后端 API，再写前端页面
- 代码要有中文注释
- 优先使用现成库，避免重复造轮子
