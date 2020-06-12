内容：SpringCloud + SpringCloud alibaba
---
微服务架构是一种架构模式，它提倡将单一应用程序划分成一组小的服务，服务之间互相协调、
互相配合，为用户提供最终价值。每个服务运行在独立的进程中，服务于服务之间才用轻量级的
通信机制互相协作（通常是基于HTTP协议的RESTful API）。每个服务都围绕着具体业务进行构建，
并且能够被独立的部署到生产环境、类生产环境等。另外，应当尽量避免统一的、集中式的服务管理机制，
对具体的一个服务而言，应根据业务上下文，选择合适的语言、工具对其进行构建。

[SpringBoot与SpringCloud版本支持关系](https://start.spring.io/actuator/info)
https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server

    **约定大于配置大于编码

#### 分布式微服务架构的一站式解决方案，俗称微服务全家桶。
1. 服务注册与发现
2. 服务调用
3. 服务熔断
4. 负载均衡
5. 服务降级
6. 服务消息队列
7. 配置中心管理
8. 服务网关
9. 全链路追踪
10. 自动化构建部署
11. 服务定时任务调度操作

#### 注册中心 
1. Eureka（停止更新了，适合于旧的SpringCloud版本）
2. Zookeeper
3. Consul
4. Nacos（强烈推荐，重点）
#### 服务调用1
1. Ribbon
2. LoadBalancer
#### 服务调用2
1. Feign
2. OpenFeign（推荐）
#### 服务降级
1. Hystrix（思想值得借鉴）
2. resilience4j（官网，国外推荐）
3. sentienl（阿里巴巴的，国内推荐）
#### 服务网关
1. Zuul
2. Zuul2
3. gateway（推荐）
#### 服务配置
1. Config（不推荐）
2. Nacos（推荐）
3. 携程网的阿波罗
#### 服务总线
1. Bus（不推荐）
2. Nacos