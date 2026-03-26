# Redis Learning

Spring Boot + Redis 学习项目

## 快速开始

1. 启动 Redis
```bash
redis-server
```

2. 运行项目
```bash
mvn spring-boot:run
```

3. 测试接口
```bash
# 设置值
curl -X POST "http://localhost:8082/redis/set?key=name&value=张三"

# 获取值
curl "http://localhost:8082/redis/get?key=name"

# 设置带过期时间的值
curl -X POST "http://localhost:8082/redis/setex?key=code&value=123456&seconds=60"

# 删除值
curl -X DELETE "http://localhost:8082/redis/delete?key=name"
```

## 学习内容

- String 基础操作
- 过期时间设置
- RedisTemplate 使用
